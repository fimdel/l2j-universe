/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.net.nio.impl.MMOClient;
import lineage2.commons.net.nio.impl.MMOConnection;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.CharSelectionInfo;
import lineage2.gameserver.model.CharSelectionInfo.CharSelectInfoPackage;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.SessionKey;
import lineage2.gameserver.network.loginservercon.gspackets.PlayerLogout;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.utils.SecondaryPasswordAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class GameClient extends MMOClient<MMOConnection<GameClient>>
{
	private static final Logger _log = LoggerFactory.getLogger(GameClient.class);
	private static final String NO_IP = "?.?.?.?";
	public GameCrypt _crypt = null;
	public GameClientState _state;
	
	public static enum GameClientState
	{
		CONNECTED,
		AUTHED,
		IN_GAME,
		DISCONNECTED
	}
	
	private String _login;
	private double _bonus = 1.0;
	private int _bonusExpire;
	private Player _activeChar;
	private SessionKey _sessionKey;
	private String _ip = NO_IP;
	private int revision = 0;
	private SecondaryPasswordAuth _secondaryAuth;
	private final List<Integer> _charSlotMapping = new ArrayList<>();
	
	/**
	 * Constructor for GameClient.
	 * @param con MMOConnection<GameClient>
	 */
	public GameClient(MMOConnection<GameClient> con)
	{
		super(con);
		_state = GameClientState.CONNECTED;
		_crypt = new GameCrypt();
		_ip = con.getSocket().getInetAddress().getHostAddress();
	}
	
	/**
	 * Method onDisconnection.
	 */
	@Override
	protected void onDisconnection()
	{
		final Player player;
		setState(GameClientState.DISCONNECTED);
		player = getActiveChar();
		setActiveChar(null);
		if (player != null)
		{
			player.setNetConnection(null);
			player.scheduleDelete();
		}
		if (getSessionKey() != null)
		{
			if (isAuthed())
			{
				LoginServerCommunication.getInstance().removeAuthedClient(getLogin());
				LoginServerCommunication.getInstance().sendPacket(new PlayerLogout(getLogin()));
			}
			else
			{
				LoginServerCommunication.getInstance().removeWaitingClient(getLogin());
			}
		}
	}
	
	/**
	 * Method onForcedDisconnection.
	 */
	@Override
	protected void onForcedDisconnection()
	{
	}
	
	/**
	 * Method getSecondaryAuth.
	 * @return SecondaryPasswordAuth
	 */
	public SecondaryPasswordAuth getSecondaryAuth()
	{
		return _secondaryAuth;
	}
	
	/**
	 * Method markRestoredChar.
	 * @param charslot int
	 */
	public void markRestoredChar(int charslot)
	{
		int objid = getObjectIdForSlot(charslot);
		if (objid < 0)
		{
			return;
		}
		if ((_activeChar != null) && (_activeChar.getObjectId() == objid))
		{
			_activeChar.setDeleteTimer(0);
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=0 WHERE obj_id=?");
			statement.setInt(1, objid);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method markToDeleteChar.
	 * @param charslot int
	 */
	public void markToDeleteChar(int charslot)
	{
		int objid = getObjectIdForSlot(charslot);
		if (objid < 0)
		{
			return;
		}
		if ((_activeChar != null) && (_activeChar.getObjectId() == objid))
		{
			_activeChar.setDeleteTimer((int) (System.currentTimeMillis() / 1000));
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=? WHERE obj_id=?");
			statement.setLong(1, (int) (System.currentTimeMillis() / 1000L));
			statement.setInt(2, objid);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("data error on update deletime char:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteChar.
	 * @param charslot int
	 */
	public void deleteChar(int charslot)
	{
		if (_activeChar != null)
		{
			return;
		}
		int objid = getObjectIdForSlot(charslot);
		if (objid == -1)
		{
			return;
		}
		CharacterDAO.getInstance().deleteCharByObjId(objid);
	}
	
	/**
	 * Method loadCharFromDisk.
	 * @param charslot int
	 * @return Player
	 */
	public Player loadCharFromDisk(int charslot)
	{
		int objectId = getObjectIdForSlot(charslot);
		if (objectId == -1)
		{
			return null;
		}
		Player character = null;
		Player oldPlayer = GameObjectsStorage.getPlayer(objectId);
		if (oldPlayer != null)
		{
			if (oldPlayer.isInOfflineMode() || oldPlayer.isLogoutStarted())
			{
				oldPlayer.kick();
				return null;
			}
			oldPlayer.sendPacket(Msg.ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT);
			GameClient oldClient = oldPlayer.getNetConnection();
			if (oldClient != null)
			{
				oldClient.setActiveChar(null);
				oldClient.closeNow(false);
			}
			oldPlayer.setNetConnection(this);
			character = oldPlayer;
		}
		if (character == null)
		{
			character = Player.restore(objectId);
		}
		if (character != null)
		{
			setActiveChar(character);
		}
		else
		{
			_log.warn("could not restore obj_id: " + objectId + " in slot:" + charslot);
		}
		return character;
	}
	
	/**
	 * Method getObjectIdForSlot.
	 * @param charslot int
	 * @return int
	 */
	public int getObjectIdForSlot(int charslot)
	{
		if ((charslot < 0) || (charslot >= _charSlotMapping.size()))
		{
			_log.warn(getLogin() + " tried to modify Character in slot " + charslot + " but no characters exits at that slot.");
			return -1;
		}
		return _charSlotMapping.get(charslot);
	}
	
	/**
	 * Method getActiveChar.
	 * @return Player
	 */
	public Player getActiveChar()
	{
		return _activeChar;
	}
	
	/**
	 * Method getSessionKey.
	 * @return SessionKey
	 */
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	/**
	 * Method getLogin.
	 * @return String
	 */
	public String getLogin()
	{
		return _login;
	}
	
	/**
	 * Method setSecondaryAuth.
	 * @param sec SecondaryPasswordAuth
	 */
	public void setSecondaryAuth(SecondaryPasswordAuth sec)
	{
		_secondaryAuth = sec;
	}
	
	/**
	 * Method setLoginName.
	 * @param loginName String
	 */
	public void setLoginName(String loginName)
	{
		_login = loginName;
	}
	
	/**
	 * Method setActiveChar.
	 * @param player Player
	 */
	public void setActiveChar(Player player)
	{
		_activeChar = player;
		if (player != null)
		{
			player.setNetConnection(this);
		}
	}
	
	/**
	 * Method setSessionId.
	 * @param sessionKey SessionKey
	 */
	public void setSessionId(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	/**
	 * Method setCharSelection.
	 * @param selectionInfo CharSelectionInfo
	 */
	public void setCharSelection(CharSelectionInfo selectionInfo)
	{
		_charSlotMapping.clear();
		for (CharSelectInfoPackage element : selectionInfo)
		{
			int objectId = element.getObjectId();
			_charSlotMapping.add(objectId);
		}
	}
	
	/**
	 * Method getRevision.
	 * @return int
	 */
	public int getRevision()
	{
		return revision;
	}
	
	/**
	 * Method setRevision.
	 * @param revision int
	 */
	public void setRevision(int revision)
	{
		this.revision = revision;
	}
	
	/**
	 * Method encrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	@Override
	public boolean encrypt(final ByteBuffer buf, final int size)
	{
		_crypt.encrypt(buf.array(), buf.position(), size);
		buf.position(buf.position() + size);
		return true;
	}
	
	/**
	 * Method decrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret = _crypt.decrypt(buf.array(), buf.position(), size);
		return ret;
	}
	
	/**
	 * Method sendPacket.
	 * @param gsp L2GameServerPacket
	 */
	public void sendPacket(L2GameServerPacket gsp)
	{
		if (isConnected())
		{
			getConnection().sendPacket(gsp);
		}
	}
	
	/**
	 * Method sendPacket.
	 * @param gsp L2GameServerPacket[]
	 */
	public void sendPacket(L2GameServerPacket... gsp)
	{
		if (isConnected())
		{
			getConnection().sendPacket(gsp);
		}
	}
	
	/**
	 * Method sendPackets.
	 * @param gsp List<L2GameServerPacket>
	 */
	public void sendPackets(List<L2GameServerPacket> gsp)
	{
		if (isConnected())
		{
			getConnection().sendPackets(gsp);
		}
	}
	
	/**
	 * Method close.
	 * @param gsp L2GameServerPacket
	 */
	public void close(L2GameServerPacket gsp)
	{
		if (isConnected())
		{
			getConnection().close(gsp);
		}
	}
	
	/**
	 * Method getIpAddr.
	 * @return String
	 */
	public String getIpAddr()
	{
		return _ip;
	}
	
	/**
	 * Method enableCrypt.
	 * @return byte[]
	 */
	public byte[] enableCrypt()
	{
		byte[] key = BlowFishKeygen.getRandomKey();
		_crypt.setKey(key);
		return key;
	}
	
	/**
	 * Method getBonus.
	 * @return double
	 */
	public double getBonus()
	{
		return _bonus;
	}
	
	/**
	 * Method getBonusExpire.
	 * @return int
	 */
	public int getBonusExpire()
	{
		return _bonusExpire;
	}
	
	/**
	 * Method setBonus.
	 * @param bonus double
	 */
	public void setBonus(double bonus)
	{
		_bonus = bonus;
	}
	
	/**
	 * Method setBonusExpire.
	 * @param bonusExpire int
	 */
	public void setBonusExpire(int bonusExpire)
	{
		_bonusExpire = bonusExpire;
	}
	
	/**
	 * Method getState.
	 * @return GameClientState
	 */
	public GameClientState getState()
	{
		return _state;
	}
	
	/**
	 * Method setState.
	 * @param state GameClientState
	 */
	public void setState(GameClientState state)
	{
		_state = state;
	}
	
	/**
	 * Field _failedPackets.
	 */
	private int _failedPackets = 0;
	/**
	 * Field _unknownPackets.
	 */
	private int _unknownPackets = 0;
	
	/**
	 * Method onPacketReadFail.
	 */
	public void onPacketReadFail()
	{
		if (_failedPackets++ >= 10)
		{
			_log.warn("Too many client packet fails, connection closed : " + this);
			if (!Config.ALLOW_PACKET_FAIL)
			{
				closeNow(true);
			}
		}
	}
	
	/**
	 * Method onUnknownPacket.
	 */
	public void onUnknownPacket()
	{
		if (_unknownPackets++ >= 10)
		{
			_log.warn("Too many client unknown packets, connection closed : " + this);
			if (!Config.ALLOW_PACKET_FAIL)
			{
				closeNow(true);
			}
		}
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _state + " IP: " + getIpAddr() + (_login == null ? "" : " Account: " + _login) + (_activeChar == null ? "" : " Player : " + _activeChar);
	}
}
