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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.CursedWeapon;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CursedWeaponsManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CursedWeaponsManager.class);
	/**
	 * Field _instance.
	 */
	private static final CursedWeaponsManager _instance = new CursedWeaponsManager();
	
	/**
	 * Method getInstance.
	 * @return CursedWeaponsManager
	 */
	public static CursedWeaponsManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _cursedWeapons.
	 */
	CursedWeapon[] _cursedWeapons;
	/**
	 * Field _cursedWeaponsMap.
	 */
	private TIntObjectHashMap<CursedWeapon> _cursedWeaponsMap;
	/**
	 * Field _removeTask.
	 */
	private ScheduledFuture<?> _removeTask;
	/**
	 * Field CURSEDWEAPONS_MAINTENANCE_INTERVAL.
	 */
	private static final int CURSEDWEAPONS_MAINTENANCE_INTERVAL = 5 * 60 * 1000;
	
	/**
	 * Constructor for CursedWeaponsManager.
	 */
	public CursedWeaponsManager()
	{
		_cursedWeaponsMap = new TIntObjectHashMap<>();
		_cursedWeapons = new CursedWeapon[0];
		if (!Config.ALLOW_CURSED_WEAPONS)
		{
			return;
		}
		load();
		restore();
		checkConditions();
		cancelTask();
		_removeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RemoveTask(), CURSEDWEAPONS_MAINTENANCE_INTERVAL, CURSEDWEAPONS_MAINTENANCE_INTERVAL);
		_log.info("CursedWeaponsManager: Loaded " + _cursedWeapons.length + " cursed weapon(s).");
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/cursed_weapons.xml");
			if (!file.exists())
			{
				return;
			}
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("item".equalsIgnoreCase(d.getNodeName()))
						{
							NamedNodeMap attrs = d.getAttributes();
							int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							int skillId = Integer.parseInt(attrs.getNamedItem("skillId").getNodeValue());
							String name = "Unknown cursed weapon";
							if (attrs.getNamedItem("name") != null)
							{
								name = attrs.getNamedItem("name").getNodeValue();
							}
							else if (ItemHolder.getInstance().getTemplate(id) != null)
							{
								name = ItemHolder.getInstance().getTemplate(id).getName();
							}
							if (id == 0)
							{
								continue;
							}
							CursedWeapon cw = new CursedWeapon(id, skillId, name);
							for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
							{
								if ("dropRate".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setDropRate(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("duration".equalsIgnoreCase(cd.getNodeName()))
								{
									attrs = cd.getAttributes();
									cw.setDurationMin(Integer.parseInt(attrs.getNamedItem("min").getNodeValue()));
									cw.setDurationMax(Integer.parseInt(attrs.getNamedItem("max").getNodeValue()));
								}
								else if ("durationLost".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setDurationLost(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("disapearChance".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setDisapearChance(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("stageKills".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setStageKills(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("transformationId".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setTransformationId(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("transformationTemplateId".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setTransformationTemplateId(Integer.parseInt(cd.getAttributes().getNamedItem("val").getNodeValue()));
								}
								else if ("transformationName".equalsIgnoreCase(cd.getNodeName()))
								{
									cw.setTransformationName(cd.getAttributes().getNamedItem("val").getNodeValue());
								}
							}
							_cursedWeaponsMap.put(id, cw);
						}
					}
				}
			}
			_cursedWeapons = _cursedWeaponsMap.values(new CursedWeapon[_cursedWeaponsMap.size()]);
		}
		catch (Exception e)
		{
			_log.error("CursedWeaponsManager: Error parsing cursed_weapons file. " + e);
		}
	}
	
	/**
	 * Method restore.
	 */
	private void restore()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM cursed_weapons");
			rset = statement.executeQuery();
			while (rset.next())
			{
				int itemId = rset.getInt("item_id");
				CursedWeapon cw = _cursedWeaponsMap.get(itemId);
				if (cw != null)
				{
					cw.setPlayerId(rset.getInt("player_id"));
					cw.setPlayerKarma(rset.getInt("player_karma"));
					cw.setPlayerPkKills(rset.getInt("player_pkkills"));
					cw.setNbKills(rset.getInt("nb_kills"));
					cw.setLoc(new Location(rset.getInt("x"), rset.getInt("y"), rset.getInt("z")));
					cw.setEndTime(rset.getLong("end_time") * 1000L);
					if (!cw.reActivate())
					{
						endOfLife(cw);
					}
				}
				else
				{
					removeFromDb(itemId);
					_log.warn("CursedWeaponsManager: Unknown cursed weapon " + itemId + ", deleted");
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("CursedWeaponsManager: Could not restore cursed_weapons data: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method checkConditions.
	 */
	private void checkConditions()
	{
		Connection con = null;
		PreparedStatement statement1 = null, statement2 = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement1 = con.prepareStatement("DELETE FROM character_skills WHERE skill_id=?");
			statement2 = con.prepareStatement("SELECT owner_id FROM items WHERE item_id=?");
			for (CursedWeapon cw : _cursedWeapons)
			{
				int itemId = cw.getItemId();
				int skillId = cw.getSkillId();
				boolean foundedInItems = false;
				statement1.setInt(1, skillId);
				statement1.executeUpdate();
				statement2.setInt(1, itemId);
				rset = statement2.executeQuery();
				while (rset.next())
				{
					int playerId = rset.getInt("owner_id");
					if (!foundedInItems)
					{
						if ((playerId != cw.getPlayerId()) || (cw.getPlayerId() == 0))
						{
							emptyPlayerCursedWeapon(playerId, itemId, cw);
							_log.info("CursedWeaponsManager[254]: Player " + playerId + " owns the cursed weapon " + itemId + " but he shouldn't.");
						}
						else
						{
							foundedInItems = true;
						}
					}
					else
					{
						emptyPlayerCursedWeapon(playerId, itemId, cw);
						_log.info("CursedWeaponsManager[262]: Player " + playerId + " owns the cursed weapon " + itemId + " but he shouldn't.");
					}
				}
				if (!foundedInItems && (cw.getPlayerId() != 0))
				{
					removeFromDb(cw.getItemId());
					_log.info("CursedWeaponsManager: Unownered weapon, removing from table...");
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("CursedWeaponsManager: Could not check cursed_weapons data: " + e);
		}
		finally
		{
			DbUtils.closeQuietly(statement1);
			DbUtils.closeQuietly(con, statement2, rset);
		}
	}
	
	/**
	 * Method emptyPlayerCursedWeapon.
	 * @param playerId int
	 * @param itemId int
	 * @param cw CursedWeapon
	 */
	private void emptyPlayerCursedWeapon(int playerId, int itemId, CursedWeapon cw)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM items WHERE owner_id=? AND item_id=?");
			statement.setInt(1, playerId);
			statement.setInt(2, itemId);
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("UPDATE characters SET karma=?, pkkills=? WHERE obj_id=?");
			statement.setInt(1, cw.getPlayerKarma());
			statement.setInt(2, cw.getPlayerPkKills());
			statement.setInt(3, playerId);
			if (statement.executeUpdate() != 1)
			{
				_log.warn("Error while updating karma & pkkills for userId " + cw.getPlayerId());
			}
			removeFromDb(itemId);
		}
		catch (SQLException e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method removeFromDb.
	 * @param itemId int
	 */
	public void removeFromDb(int itemId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM cursed_weapons WHERE item_id = ?");
			statement.setInt(1, itemId);
			statement.executeUpdate();
			if (getCursedWeapon(itemId) != null)
			{
				getCursedWeapon(itemId).initWeapon();
			}
		}
		catch (SQLException e)
		{
			_log.error("CursedWeaponsManager: Failed to remove data: " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method cancelTask.
	 */
	private void cancelTask()
	{
		if (_removeTask != null)
		{
			_removeTask.cancel(false);
			_removeTask = null;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class RemoveTask extends RunnableImpl
	{
		/**
		 * Constructor for RemoveTask.
		 */
		public RemoveTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (CursedWeapon cw : _cursedWeapons)
			{
				if (cw.isActive() && (cw.getTimeLeft() <= 0))
				{
					endOfLife(cw);
				}
			}
		}
	}
	
	/**
	 * Method endOfLife.
	 * @param cw CursedWeapon
	 */
	public void endOfLife(CursedWeapon cw)
	{
		if (cw.isActivated())
		{
			Player player = cw.getOnlineOwner();
			if (player != null)
			{
				_log.info("CursedWeaponsManager: " + cw.getName() + " being removed online from " + player + ".");
				player.abortAttack(true, true);
				player.setKarma(cw.getPlayerKarma());
				player.setPkKills(cw.getPlayerPkKills());
				player.setCursedWeaponEquippedId(0);
				player.setTransformation(0);
				player.setTransformationName(null);
				player.removeSkill(SkillTable.getInstance().getInfo(cw.getSkillId(), player.getSkillLevel(cw.getSkillId())), false);
				player.getInventory().destroyItemByItemId(cw.getItemId(), 1L);
				player.broadcastCharInfo();
			}
			else
			{
				_log.info("CursedWeaponsManager: " + cw.getName() + " being removed offline.");
				Connection con = null;
				PreparedStatement statement = null;
				try
				{
					con = DatabaseFactory.getInstance().getConnection();
					statement = con.prepareStatement("DELETE FROM items WHERE owner_id=? AND item_id=?");
					statement.setInt(1, cw.getPlayerId());
					statement.setInt(2, cw.getItemId());
					statement.executeUpdate();
					DbUtils.close(statement);
					statement = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND skill_id=?");
					statement.setInt(1, cw.getPlayerId());
					statement.setInt(2, cw.getSkillId());
					statement.executeUpdate();
					DbUtils.close(statement);
					statement = con.prepareStatement("UPDATE characters SET karma=?, pkkills=? WHERE obj_Id=?");
					statement.setInt(1, cw.getPlayerKarma());
					statement.setInt(2, cw.getPlayerPkKills());
					statement.setInt(3, cw.getPlayerId());
					statement.executeUpdate();
				}
				catch (SQLException e)
				{
					_log.warn("CursedWeaponsManager: Could not delete : " + e);
				}
				finally
				{
					DbUtils.closeQuietly(con, statement);
				}
			}
		}
		else if ((cw.getPlayer() != null) && (cw.getPlayer().getInventory().getItemByItemId(cw.getItemId()) != null))
		{
			Player player = cw.getPlayer();
			if (!cw.getPlayer().getInventory().destroyItemByItemId(cw.getItemId(), 1))
			{
				_log.info("CursedWeaponsManager[453]: Error! Cursed weapon not found!!!");
			}
			player.sendChanges();
			player.broadcastUserInfo();
		}
		else if (cw.getItem() != null)
		{
			cw.getItem().deleteMe();
			cw.getItem().delete();
			_log.info("CursedWeaponsManager: " + cw.getName() + " item has been removed from World.");
		}
		cw.initWeapon();
		removeFromDb(cw.getItemId());
		announce(new SystemMessage(SystemMessage.S1_HAS_DISAPPEARED_CW).addString(cw.getName()));
	}
	
	/**
	 * Method saveData.
	 * @param cw CursedWeapon
	 */
	public void saveData(CursedWeapon cw)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM cursed_weapons WHERE item_id = ?");
			statement.setInt(1, cw.getItemId());
			statement.executeUpdate();
			if (cw.isActive())
			{
				DbUtils.close(statement);
				statement = con.prepareStatement("REPLACE INTO cursed_weapons (item_id, player_id, player_karma, player_pkkills, nb_kills, x, y, z, end_time) VALUES (?,?,?,?,?,?,?,?,?)");
				statement.setInt(1, cw.getItemId());
				statement.setInt(2, cw.getPlayerId());
				statement.setInt(3, cw.getPlayerKarma());
				statement.setInt(4, cw.getPlayerPkKills());
				statement.setInt(5, cw.getNbKills());
				statement.setInt(6, cw.getLoc().x);
				statement.setInt(7, cw.getLoc().y);
				statement.setInt(8, cw.getLoc().z);
				statement.setLong(9, cw.getEndTime() / 1000);
				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			_log.error("CursedWeapon: Failed to save data: " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method saveData.
	 */
	public void saveData()
	{
		for (CursedWeapon cw : _cursedWeapons)
		{
			saveData(cw);
		}
	}
	
	/**
	 * Method checkPlayer.
	 * @param player Player
	 * @param item ItemInstance
	 */
	public void checkPlayer(Player player, ItemInstance item)
	{
		if ((player == null) || (item == null) || player.isInOlympiadMode())
		{
			return;
		}
		CursedWeapon cw = _cursedWeaponsMap.get(item.getItemId());
		if (cw == null)
		{
			return;
		}
		if ((player.getObjectId() == cw.getPlayerId()) || (cw.getPlayerId() == 0) || cw.isDropped())
		{
			activate(player, item);
			showUsageTime(player, cw);
		}
		else
		{
			_log.warn("CursedWeaponsManager: " + player + " tried to obtain " + item + " in wrong way");
			player.getInventory().destroyItem(item, item.getCount());
		}
	}
	
	/**
	 * Method activate.
	 * @param player Player
	 * @param item ItemInstance
	 */
	public void activate(Player player, ItemInstance item)
	{
		if ((player == null) || player.isInOlympiadMode())
		{
			return;
		}
		CursedWeapon cw = _cursedWeaponsMap.get(item.getItemId());
		if (cw == null)
		{
			return;
		}
		if (player.isCursedWeaponEquipped())
		{
			if (player.getCursedWeaponEquippedId() != item.getItemId())
			{
				CursedWeapon cw2 = _cursedWeaponsMap.get(player.getCursedWeaponEquippedId());
				cw2.setNbKills(cw2.getStageKills() - 1);
				cw2.increaseKills();
			}
			endOfLife(cw);
			player.getInventory().destroyItem(item, 1);
		}
		else if (cw.getTimeLeft() > 0)
		{
			cw.activate(player, item);
			saveData(cw);
			announce(new SystemMessage(SystemMessage.THE_OWNER_OF_S2_HAS_APPEARED_IN_THE_S1_REGION).addZoneName(player.getLoc()).addString(cw.getName()));
		}
		else
		{
			endOfLife(cw);
			player.getInventory().destroyItem(item, 1);
		}
	}
	
	/**
	 * Method doLogout.
	 * @param player Player
	 */
	public void doLogout(Player player)
	{
		for (CursedWeapon cw : _cursedWeapons)
		{
			if (player.getInventory().getItemByItemId(cw.getItemId()) != null)
			{
				cw.setPlayer(null);
				cw.setItem(null);
			}
		}
	}
	
	/**
	 * Method dropAttackable.
	 * @param attackable NpcInstance
	 * @param killer Player
	 */
	public void dropAttackable(NpcInstance attackable, Player killer)
	{
		if (killer.isInOlympiadMode() || killer.isCursedWeaponEquipped() || (_cursedWeapons.length == 0) || (killer.getReflection() != ReflectionManager.DEFAULT))
		{
			return;
		}
		synchronized (_cursedWeapons)
		{
			CursedWeapon[] cursedWeapons = new CursedWeapon[0];
			for (CursedWeapon cw : _cursedWeapons)
			{
				if (cw.isActive())
				{
					continue;
				}
				cursedWeapons = ArrayUtils.add(cursedWeapons, cw);
			}
			if (cursedWeapons.length > 0)
			{
				CursedWeapon cw = cursedWeapons[Rnd.get(cursedWeapons.length)];
				if (Rnd.get(100000000) <= cw.getDropRate())
				{
					cw.create(attackable, killer);
				}
			}
		}
	}
	
	/**
	 * Method dropPlayer.
	 * @param player Player
	 */
	public void dropPlayer(Player player)
	{
		CursedWeapon cw = _cursedWeaponsMap.get(player.getCursedWeaponEquippedId());
		if (cw == null)
		{
			return;
		}
		if (cw.dropIt(null, null, player))
		{
			saveData(cw);
			announce(new SystemMessage(SystemMessage.S2_WAS_DROPPED_IN_THE_S1_REGION).addZoneName(player.getLoc()).addItemName(cw.getItemId()));
		}
		else
		{
			endOfLife(cw);
		}
	}
	
	/**
	 * Method increaseKills.
	 * @param itemId int
	 */
	public void increaseKills(int itemId)
	{
		CursedWeapon cw = _cursedWeaponsMap.get(itemId);
		if (cw != null)
		{
			cw.increaseKills();
			saveData(cw);
		}
	}
	
	/**
	 * Method getLevel.
	 * @param itemId int
	 * @return int
	 */
	public int getLevel(int itemId)
	{
		CursedWeapon cw = _cursedWeaponsMap.get(itemId);
		return cw != null ? cw.getLevel() : 0;
	}
	
	/**
	 * Method announce.
	 * @param sm SystemMessage
	 */
	public void announce(SystemMessage sm)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(sm);
		}
	}
	
	/**
	 * Method showUsageTime.
	 * @param player Player
	 * @param itemId int
	 */
	public void showUsageTime(Player player, int itemId)
	{
		CursedWeapon cw = _cursedWeaponsMap.get(itemId);
		if (cw != null)
		{
			showUsageTime(player, cw);
		}
	}
	
	/**
	 * Method showUsageTime.
	 * @param player Player
	 * @param cw CursedWeapon
	 */
	public void showUsageTime(Player player, CursedWeapon cw)
	{
		SystemMessage sm = new SystemMessage(SystemMessage.S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
		sm.addString(cw.getName());
		sm.addNumber(new Long(cw.getTimeLeft() / 60000).intValue());
		player.sendPacket(sm);
	}
	
	/**
	 * Method isCursed.
	 * @param itemId int
	 * @return boolean
	 */
	public boolean isCursed(int itemId)
	{
		return _cursedWeaponsMap.containsKey(itemId);
	}
	
	/**
	 * Method getCursedWeapons.
	 * @return CursedWeapon[]
	 */
	public CursedWeapon[] getCursedWeapons()
	{
		return _cursedWeapons;
	}
	
	/**
	 * Method getCursedWeaponsIds.
	 * @return int[]
	 */
	public int[] getCursedWeaponsIds()
	{
		return _cursedWeaponsMap.keys();
	}
	
	/**
	 * Method getCursedWeapon.
	 * @param itemId int
	 * @return CursedWeapon
	 */
	public CursedWeapon getCursedWeapon(int itemId)
	{
		return _cursedWeaponsMap.get(itemId);
	}
}
