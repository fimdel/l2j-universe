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
package lineage2.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Couple;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CoupleManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CoupleManager.class);
	/**
	 * Field _instance.
	 */
	private static CoupleManager _instance;
	/**
	 * Field _couples.
	 */
	private List<Couple> _couples;
	/**
	 * Field _deletedCouples.
	 */
	private List<Couple> _deletedCouples;
	
	/**
	 * Method getInstance.
	 * @return CoupleManager
	 */
	public static CoupleManager getInstance()
	{
		if (_instance == null)
		{
			new CoupleManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for CoupleManager.
	 */
	public CoupleManager()
	{
		_instance = this;
		_log.info("Initializing CoupleManager");
		_instance.load();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new StoreTask(), 10 * 60 * 1000, 10 * 60 * 1000);
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM couples ORDER BY id");
			rs = statement.executeQuery();
			while (rs.next())
			{
				Couple c = new Couple(rs.getInt("id"));
				c.setPlayer1Id(rs.getInt("player1Id"));
				c.setPlayer2Id(rs.getInt("player2Id"));
				c.setMaried(rs.getBoolean("maried"));
				c.setAffiancedDate(rs.getLong("affiancedDate"));
				c.setWeddingDate(rs.getLong("weddingDate"));
				getCouples().add(c);
			}
			_log.info("Loaded: " + getCouples().size() + " couples(s)");
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method getCouple.
	 * @param coupleId int
	 * @return Couple
	 */
	public final Couple getCouple(int coupleId)
	{
		for (Couple c : getCouples())
		{
			if ((c != null) && (c.getId() == coupleId))
			{
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Method engage.
	 * @param cha Player
	 */
	public void engage(Player cha)
	{
		int chaId = cha.getObjectId();
		for (Couple cl : getCouples())
		{
			if (cl != null)
			{
				if ((cl.getPlayer1Id() == chaId) || (cl.getPlayer2Id() == chaId))
				{
					if (cl.getMaried())
					{
						cha.setMaried(true);
					}
					cha.setCoupleId(cl.getId());
					if (cl.getPlayer1Id() == chaId)
					{
						cha.setPartnerId(cl.getPlayer2Id());
					}
					else
					{
						cha.setPartnerId(cl.getPlayer1Id());
					}
				}
			}
		}
	}
	
	/**
	 * Method notifyPartner.
	 * @param cha Player
	 */
	public void notifyPartner(Player cha)
	{
		if (cha.getPartnerId() != 0)
		{
			Player partner = GameObjectsStorage.getPlayer(cha.getPartnerId());
			if (partner != null)
			{
				partner.sendMessage(new CustomMessage("lineage2.gameserver.instancemanager.CoupleManager.PartnerEntered", partner));
			}
		}
	}
	
	/**
	 * Method createCouple.
	 * @param player1 Player
	 * @param player2 Player
	 */
	public void createCouple(Player player1, Player player2)
	{
		if ((player1 != null) && (player2 != null))
		{
			if ((player1.getPartnerId() == 0) && (player2.getPartnerId() == 0))
			{
				getCouples().add(new Couple(player1, player2));
			}
		}
	}
	
	/**
	 * Method getCouples.
	 * @return List<Couple>
	 */
	public final List<Couple> getCouples()
	{
		if (_couples == null)
		{
			_couples = new CopyOnWriteArrayList<>();
		}
		return _couples;
	}
	
	/**
	 * Method getDeletedCouples.
	 * @return List<Couple>
	 */
	public List<Couple> getDeletedCouples()
	{
		if (_deletedCouples == null)
		{
			_deletedCouples = new CopyOnWriteArrayList<>();
		}
		return _deletedCouples;
	}
	
	/**
	 * Method store.
	 */
	public void store()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if ((_deletedCouples != null) && !_deletedCouples.isEmpty())
			{
				statement = con.prepareStatement("DELETE FROM couples WHERE id = ?");
				for (Couple c : _deletedCouples)
				{
					statement.setInt(1, c.getId());
					statement.execute();
				}
				_deletedCouples.clear();
			}
			if ((_couples != null) && !_couples.isEmpty())
			{
				for (Couple c : _couples)
				{
					if ((c != null) && c.isChanged())
					{
						c.store(con);
						c.setChanged(false);
					}
				}
			}
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
	 * @author Mobius
	 */
	private class StoreTask extends RunnableImpl
	{
		/**
		 * Constructor for StoreTask.
		 */
		public StoreTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			store();
		}
	}
}
