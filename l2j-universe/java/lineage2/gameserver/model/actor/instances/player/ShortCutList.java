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
package lineage2.gameserver.model.actor.instances.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.ShortCutInit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShortCutList
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ShortCutList.class);
	/**
	 * Field player.
	 */
	private final Player player;
	/**
	 * Field _shortCuts.
	 */
	private final Map<Integer, ShortCut> _shortCuts = new ConcurrentHashMap<>();
	
	/**
	 * Constructor for ShortCutList.
	 * @param owner Player
	 */
	public ShortCutList(Player owner)
	{
		player = owner;
	}
	
	/**
	 * Method getAllShortCuts.
	 * @return Collection<ShortCut>
	 */
	public Collection<ShortCut> getAllShortCuts()
	{
		return _shortCuts.values();
	}
	
	/**
	 * Method validate.
	 */
	public void validate()
	{
		for (ShortCut sc : _shortCuts.values())
		{
			if (sc.getType() == ShortCut.TYPE_ITEM)
			{
				if (player.getInventory().getItemByObjectId(sc.getId()) == null)
				{
					deleteShortCut(sc.getSlot(), sc.getPage());
				}
			}
		}
	}
	
	/**
	 * Method getShortCut.
	 * @param slot int
	 * @param page int
	 * @return ShortCut
	 */
	public ShortCut getShortCut(int slot, int page)
	{
		ShortCut sc = _shortCuts.get(slot + (page * 12));
		if ((sc != null) && (sc.getType() == ShortCut.TYPE_ITEM))
		{
			if (player.getInventory().getItemByObjectId(sc.getId()) == null)
			{
				player.sendPacket(Msg.THERE_ARE_NO_MORE_ITEMS_IN_THE_SHORTCUT);
				deleteShortCut(sc.getSlot(), sc.getPage());
				sc = null;
			}
		}
		return sc;
	}
	
	/**
	 * Method registerShortCut.
	 * @param shortcut ShortCut
	 */
	public void registerShortCut(ShortCut shortcut)
	{
		ShortCut oldShortCut = _shortCuts.put(shortcut.getSlot() + (12 * shortcut.getPage()), shortcut);
		registerShortCutInDb(shortcut, oldShortCut);
	}
	
	/**
	 * Method registerShortCutInDb.
	 * @param shortcut ShortCut
	 * @param oldShortCut ShortCut
	 */
	private synchronized void registerShortCutInDb(ShortCut shortcut, ShortCut oldShortCut)
	{
		if (oldShortCut != null)
		{
			deleteShortCutFromDb(oldShortCut);
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO character_shortcuts SET object_id=?,slot=?,page=?,type=?,shortcut_id=?,level=?,character_type=?,class_index=?");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, shortcut.getSlot());
			statement.setInt(3, shortcut.getPage());
			statement.setInt(4, shortcut.getType());
			statement.setInt(5, shortcut.getId());
			statement.setInt(6, shortcut.getLevel());
			statement.setInt(7, shortcut.getCharacterType());
			statement.setInt(8, player.getActiveClassId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("could not store shortcuts:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteShortCutFromDb.
	 * @param shortcut ShortCut
	 */
	private void deleteShortCutFromDb(ShortCut shortcut)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE object_id=? AND slot=? AND page=? AND class_index=?");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, shortcut.getSlot());
			statement.setInt(3, shortcut.getPage());
			statement.setInt(4, player.getActiveClassId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("could not delete shortcuts:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteShortCut.
	 * @param slot int
	 * @param page int
	 */
	public void deleteShortCut(int slot, int page)
	{
		ShortCut old = _shortCuts.remove(slot + (page * 12));
		if (old == null)
		{
			return;
		}
		deleteShortCutFromDb(old);
		if (old.getType() == ShortCut.TYPE_SKILL)
		{
			player.sendPacket(new ShortCutInit(player));
			for (int shotId : player.getAutoSoulShot())
			{
				player.sendPacket(new ExAutoSoulShot(shotId, true));
			}
		}
	}
	
	/**
	 * Method deleteShortCutByObjectId.
	 * @param objectId int
	 */
	public void deleteShortCutByObjectId(int objectId)
	{
		for (ShortCut shortcut : _shortCuts.values())
		{
			if ((shortcut != null) && (shortcut.getType() == ShortCut.TYPE_ITEM) && (shortcut.getId() == objectId))
			{
				deleteShortCut(shortcut.getSlot(), shortcut.getPage());
			}
		}
	}
	
	/**
	 * Method deleteShortCutBySkillId.
	 * @param skillId int
	 */
	public void deleteShortCutBySkillId(int skillId)
	{
		for (ShortCut shortcut : _shortCuts.values())
		{
			if ((shortcut != null) && (shortcut.getType() == ShortCut.TYPE_SKILL) && (shortcut.getId() == skillId))
			{
				deleteShortCut(shortcut.getSlot(), shortcut.getPage());
			}
		}
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		_shortCuts.clear();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT character_type, slot, page, type, shortcut_id, level FROM character_shortcuts WHERE object_id=? AND class_index=?");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, player.getActiveClassId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int slot = rset.getInt("slot");
				int page = rset.getInt("page");
				int type = rset.getInt("type");
				int id = rset.getInt("shortcut_id");
				int level = rset.getInt("level");
				int character_type = rset.getInt("character_type");
				_shortCuts.put(slot + (page * 12), new ShortCut(slot, page, type, id, level, character_type));
			}
		}
		catch (Exception e)
		{
			_log.error("could not store shortcuts:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
}
