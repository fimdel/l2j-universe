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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BookMarkList
{
	/**
	 * Field FORBIDDEN_ZONES.
	 */
	public static final ZoneType[] FORBIDDEN_ZONES = new ZoneType[]
	{
		ZoneType.RESIDENCE,
		ZoneType.ssq_zone,
		ZoneType.battle_zone,
		ZoneType.SIEGE,
		ZoneType.no_restart,
		ZoneType.no_summon,
	};
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(BookMarkList.class);
	/**
	 * Field owner.
	 */
	private final Player owner;
	/**
	 * Field elementData.
	 */
	private final List<BookMark> elementData;
	/**
	 * Field capacity.
	 */
	private int capacity;
	
	/**
	 * Constructor for BookMarkList.
	 * @param owner Player
	 * @param acapacity int
	 */
	public BookMarkList(Player owner, int acapacity)
	{
		this.owner = owner;
		elementData = new ArrayList<>(acapacity);
		capacity = acapacity;
	}
	
	/**
	 * Method setCapacity.
	 * @param val int
	 */
	public synchronized void setCapacity(int val)
	{
		capacity = val;
	}
	
	/**
	 * Method getCapacity.
	 * @return int
	 */
	public int getCapacity()
	{
		return capacity;
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		elementData.clear();
	}
	
	/**
	 * Method toArray.
	 * @return BookMark[]
	 */
	public BookMark[] toArray()
	{
		return elementData.toArray(new BookMark[elementData.size()]);
	}
	
	/**
	 * Method incCapacity.
	 * @return int
	 */
	public int incCapacity()
	{
		capacity++;
		owner.sendPacket(Msg.THE_NUMBER_OF_MY_TELEPORTS_SLOTS_HAS_BEEN_INCREASED);
		return getCapacity();
	}
	
	/**
	 * Method add.
	 * @param e BookMark
	 * @return boolean
	 */
	public synchronized boolean add(BookMark e)
	{
		if (elementData.size() >= getCapacity())
		{
			return false;
		}
		return elementData.add(e);
	}
	
	/**
	 * Method get.
	 * @param slot int
	 * @return BookMark
	 */
	public BookMark get(int slot)
	{
		if ((slot < 1) || (slot > elementData.size()))
		{
			return null;
		}
		return elementData.get(slot - 1);
	}
	
	/**
	 * Method remove.
	 * @param slot int
	 */
	public void remove(int slot)
	{
		if ((slot < 1) || (slot > elementData.size()))
		{
			return;
		}
		elementData.remove(slot - 1);
	}
	
	/**
	 * Method tryTeleport.
	 * @param slot int
	 * @return boolean
	 */
	public boolean tryTeleport(int slot)
	{
		if (!checkFirstConditions(owner) || !checkTeleportConditions(owner))
		{
			return false;
		}
		if ((slot < 1) || (slot > elementData.size()))
		{
			return false;
		}
		BookMark bookmark = elementData.get(slot - 1);
		if (!checkTeleportLocation(owner, bookmark.x, bookmark.y, bookmark.z))
		{
			return false;
		}
		if (Functions.removeItem(owner, 20025, 1) != 1)
		{
			//owner.sendPacket(SystemMsg.YOU_CANNOT_BOOKMARK_THIS_LOCATION_BECAUSE_YOU_DO_NOT_HAVE_A_MY_TELEPORT_FLAG);
			owner.sendMessage("You cannot teleport because you do not have a My Teleport Spellbook.");
			return false;
		}
		owner.teleToLocation(bookmark.x, bookmark.y, bookmark.z);
		return true;
	}
	
	/**
	 * Method add.
	 * @param aname String
	 * @param aacronym String
	 * @param aiconId int
	 * @return boolean
	 */
	public boolean add(String aname, String aacronym, int aiconId)
	{
		return add(aname, aacronym, aiconId, true);
	}
	
	/**
	 * Method add.
	 * @param aname String
	 * @param aacronym String
	 * @param aiconId int
	 * @param takeFlag boolean
	 * @return boolean
	 */
	public boolean add(String aname, String aacronym, int aiconId, boolean takeFlag)
	{
		return (owner != null) && add(owner.getLoc(), aname, aacronym, aiconId, takeFlag);
	}
	
	/**
	 * Method add.
	 * @param loc Location
	 * @param aname String
	 * @param aacronym String
	 * @param aiconId int
	 * @param takeFlag boolean
	 * @return boolean
	 */
	public boolean add(Location loc, String aname, String aacronym, int aiconId, boolean takeFlag)
	{
		if (!checkFirstConditions(owner) || !checkTeleportLocation(owner, loc))
		{
			return false;
		}
		if (elementData.size() >= getCapacity())
		{
			owner.sendPacket(Msg.YOU_HAVE_NO_SPACE_TO_SAVE_THE_TELEPORT_LOCATION);
			return false;
		}
		if (takeFlag)
		{
			if (Functions.removeItem(owner, 20033, 1) != 1)
			{
				owner.sendPacket(Msg.YOU_CANNOT_BOOKMARK_THIS_LOCATION_BECAUSE_YOU_DO_NOT_HAVE_A_MY_TELEPORT_FLAG);
				return false;
			}
		}
		add(new BookMark(loc, aiconId, aname, aacronym));
		return true;
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
			statement = con.prepareStatement("DELETE FROM `character_bookmarks` WHERE char_Id=?");
			statement.setInt(1, owner.getObjectId());
			statement.execute();
			DbUtils.close(statement);
			statement = con.prepareStatement("INSERT INTO `character_bookmarks` VALUES(?,?,?,?,?,?,?,?);");
			int slotId = 0;
			for (BookMark bookmark : elementData)
			{
				statement.setInt(1, owner.getObjectId());
				statement.setInt(2, ++slotId);
				statement.setString(3, bookmark.getName());
				statement.setString(4, bookmark.getAcronym());
				statement.setInt(5, bookmark.getIcon());
				statement.setInt(6, bookmark.x);
				statement.setInt(7, bookmark.y);
				statement.setInt(8, bookmark.z);
				statement.execute();
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
	 * Method restore.
	 */
	public synchronized void restore()
	{
		if (getCapacity() == 0)
		{
			elementData.clear();
			return;
		}
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * FROM `character_bookmarks` WHERE `char_Id`=" + owner.getObjectId() + " ORDER BY `idx` LIMIT " + getCapacity());
			elementData.clear();
			while (rs.next())
			{
				add(new BookMark(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getInt("icon"), rs.getString("name"), rs.getString("acronym")));
			}
		}
		catch (final Exception e)
		{
			_log.error("Could not restore " + owner + " bookmarks!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method checkFirstConditions.
	 * @param player Player
	 * @return boolean
	 */
	public static boolean checkFirstConditions(Player player)
	{
		if (player == null)
		{
			return false;
		}
		if (player.getActiveWeaponFlagAttachment() != null)
		{
			player.sendPacket(Msg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
			return false;
		}
		if (player.isInOlympiadMode())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_IN_AN_OLYMPIAD_MATCH);
			return false;
		}
		if (player.getReflection() != ReflectionManager.DEFAULT)
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_IN_AN_INSTANT_ZONE);
			return false;
		}
		if (player.isInDuel())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_DUEL);
			return false;
		}
		if (player.isInCombat() || (player.getPvpFlag() != 0))
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_DURING_A_BATTLE);
			return false;
		}
		if (player.isOnSiegeField() || player.isInZoneBattle())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_PARTICIPATING_A_LARGE_SCALE_BATTLE_SUCH_AS_A_CASTLE_SIEGE);
			return false;
		}
		if (player.isFlying())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_FLYING);
			return false;
		}
		if (player.isInWater() || player.isInBoat())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_UNDERWATER);
			return false;
		}
		return true;
	}
	
	/**
	 * Method checkTeleportConditions.
	 * @param player Player
	 * @return boolean
	 */
	public static boolean checkTeleportConditions(Player player)
	{
		if (player == null)
		{
			return false;
		}
		if (player.isAlikeDead())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_DEAD);
			return false;
		}
		if (player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(Msg.YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS);
			return false;
		}
		if (player.isInBoat() || player.isParalyzed() || player.isStunned() || player.isSleeping() || player.isAirBinded() || player.isKnockedBack() || player.isKnockedDown() || player.isPulledNow())
		{
			player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_WHILE_YOU_ARE_IN_A_FLINT_OR_PARALYZED_STATE);
			return false;
		}
		return true;
	}
	
	/**
	 * Method checkTeleportLocation.
	 * @param player Player
	 * @param loc Location
	 * @return boolean
	 */
	public static boolean checkTeleportLocation(Player player, Location loc)
	{
		return checkTeleportLocation(player, loc.x, loc.y, loc.z);
	}
	
	/**
	 * Method checkTeleportLocation.
	 * @param player Player
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 */
	public static boolean checkTeleportLocation(Player player, int x, int y, int z)
	{
		if (player == null)
		{
			return false;
		}
		for (ZoneType zoneType : FORBIDDEN_ZONES)
		{
			Zone zone = player.getZone(zoneType);
			if (zone != null)
			{
				player.sendPacket(Msg.YOU_CANNOT_USE_MY_TELEPORTS_TO_REACH_THIS_AREA);
				return false;
			}
		}
		return true;
	}
}
