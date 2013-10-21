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
package lineage2.gameserver.model.entity.residence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.tables.SkillTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ResidenceFunction
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ResidenceFunction.class);
	/**
	 * Field TELEPORT. (value is 1)
	 */
	public static final int TELEPORT = 1;
	/**
	 * Field ITEM_CREATE. (value is 2)
	 */
	public static final int ITEM_CREATE = 2;
	/**
	 * Field RESTORE_HP. (value is 3)
	 */
	public static final int RESTORE_HP = 3;
	/**
	 * Field RESTORE_MP. (value is 4)
	 */
	public static final int RESTORE_MP = 4;
	/**
	 * Field RESTORE_EXP. (value is 5)
	 */
	public static final int RESTORE_EXP = 5;
	/**
	 * Field SUPPORT. (value is 6)
	 */
	public static final int SUPPORT = 6;
	/**
	 * Field CURTAIN. (value is 7)
	 */
	public static final int CURTAIN = 7;
	/**
	 * Field PLATFORM. (value is 8)
	 */
	public static final int PLATFORM = 8;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _endDate.
	 */
	private final Calendar _endDate;
	/**
	 * Field _inDebt.
	 */
	private boolean _inDebt;
	/**
	 * Field _active.
	 */
	private boolean _active;
	/**
	 * Field _leases.
	 */
	private final Map<Integer, Integer> _leases = new ConcurrentSkipListMap<>();
	/**
	 * Field _teleports.
	 */
	private final Map<Integer, TeleportLocation[]> _teleports = new ConcurrentSkipListMap<>();
	/**
	 * Field _buylists.
	 */
	private final Map<Integer, int[]> _buylists = new ConcurrentSkipListMap<>();
	/**
	 * Field _buffs.
	 */
	private final Map<Integer, Object[][]> _buffs = new ConcurrentSkipListMap<>();
	
	/**
	 * Constructor for ResidenceFunction.
	 * @param id int
	 * @param type int
	 */
	public ResidenceFunction(int id, int type)
	{
		_id = id;
		_type = type;
		_endDate = Calendar.getInstance();
	}
	
	/**
	 * Method getResidenceId.
	 * @return int
	 */
	public int getResidenceId()
	{
		return _id;
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	public int getType()
	{
		return _type;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method setLvl.
	 * @param lvl int
	 */
	public void setLvl(int lvl)
	{
		_level = lvl;
	}
	
	/**
	 * Method getEndTimeInMillis.
	 * @return long
	 */
	public long getEndTimeInMillis()
	{
		return _endDate.getTimeInMillis();
	}
	
	/**
	 * Method setEndTimeInMillis.
	 * @param time long
	 */
	public void setEndTimeInMillis(long time)
	{
		_endDate.setTimeInMillis(time);
	}
	
	/**
	 * Method setInDebt.
	 * @param inDebt boolean
	 */
	public void setInDebt(boolean inDebt)
	{
		_inDebt = inDebt;
	}
	
	/**
	 * Method isInDebt.
	 * @return boolean
	 */
	public boolean isInDebt()
	{
		return _inDebt;
	}
	
	/**
	 * Method setActive.
	 * @param active boolean
	 */
	public void setActive(boolean active)
	{
		_active = active;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method updateRentTime.
	 * @param inDebt boolean
	 */
	public void updateRentTime(boolean inDebt)
	{
		setEndTimeInMillis(System.currentTimeMillis() + 86400000);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE residence_functions SET endTime=?, inDebt=? WHERE type=? AND id=?");
			statement.setInt(1, (int) (getEndTimeInMillis() / 1000));
			statement.setInt(2, inDebt ? 1 : 0);
			statement.setInt(3, getType());
			statement.setInt(4, getResidenceId());
			statement.executeUpdate();
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
	 * Method getTeleports.
	 * @return TeleportLocation[]
	 */
	public TeleportLocation[] getTeleports()
	{
		return getTeleports(_level);
	}
	
	/**
	 * Method getTeleports.
	 * @param level int
	 * @return TeleportLocation[]
	 */
	public TeleportLocation[] getTeleports(int level)
	{
		return _teleports.get(level);
	}
	
	/**
	 * Method addTeleports.
	 * @param level int
	 * @param teleports TeleportLocation[]
	 */
	public void addTeleports(int level, TeleportLocation[] teleports)
	{
		_teleports.put(level, teleports);
	}
	
	/**
	 * Method getLease.
	 * @return int
	 */
	public int getLease()
	{
		if (_level == 0)
		{
			return 0;
		}
		return getLease(_level);
	}
	
	/**
	 * Method getLease.
	 * @param level int
	 * @return int
	 */
	public int getLease(int level)
	{
		return _leases.get(level);
	}
	
	/**
	 * Method addLease.
	 * @param level int
	 * @param lease int
	 */
	public void addLease(int level, int lease)
	{
		_leases.put(level, lease);
	}
	
	/**
	 * Method getBuylist.
	 * @return int[]
	 */
	public int[] getBuylist()
	{
		return getBuylist(_level);
	}
	
	/**
	 * Method getBuylist.
	 * @param level int
	 * @return int[]
	 */
	public int[] getBuylist(int level)
	{
		return _buylists.get(level);
	}
	
	/**
	 * Method addBuylist.
	 * @param level int
	 * @param buylist int[]
	 */
	public void addBuylist(int level, int[] buylist)
	{
		_buylists.put(level, buylist);
	}
	
	/**
	 * Method getBuffs.
	 * @return Object[][]
	 */
	public Object[][] getBuffs()
	{
		return getBuffs(_level);
	}
	
	/**
	 * Method getBuffs.
	 * @param level int
	 * @return Object[][]
	 */
	public Object[][] getBuffs(int level)
	{
		return _buffs.get(level);
	}
	
	/**
	 * Method addBuffs.
	 * @param level int
	 */
	public void addBuffs(int level)
	{
		_buffs.put(level, buffs_template[level]);
	}
	
	/**
	 * Method getLevels.
	 * @return Set<Integer>
	 */
	public Set<Integer> getLevels()
	{
		return _leases.keySet();
	}
	
	/**
	 * Field A. (value is """")
	 */
	public static final String A = "";
	/**
	 * Field W. (value is ""W"")
	 */
	public static final String W = "W";
	/**
	 * Field M. (value is ""M"")
	 */
	public static final String M = "M";
	/**
	 * Field buffs_template.
	 */
	/*
	15374	1	Clan Hall - Horn Melody	For 30 min., P. Atk. + 17%, P. Def. + 15%, M. Atk. + 79%, and M. Def. + 30%.	none	none\0
	15375	1	Clan Hall - Drum Melody	For 30 min., Max MP + 20%, HP Recovery Bonus + 20%, M. Critical Rate + 20, Critical Damage during a standard attack + 20%, P. Atk. + 10%, P. Def. + 20%, Atk. Spd. + 20%, M. Atk. + 20%, M. Def. + 20%, Casting Spd. + 20%, Debuff Resistance + 10%, and P. Def. when receiving a critical attack. Also, Spd. - 15%, and Critical Damage during a standard attack is increased after receiving a certain amount of damage.	none	none\0
	15376	1	Clan Hall - Pipe Organ Melody	For 30 min., Max HP/ MP/ CP + 37%, and HP Recovery Bonus + 20%.	none	none\0
	15377	1	Clan Hall - Guitar Melody	For 30 min., P. Critical Rate + 32%, P. Critical Damage + 35%, and M. Critical Rate + 20. Also, P. Skill MP Consumption - 20%, M. Skill MP Consumption -10%, and the Iss Enchanter's Enchant Skill MP Consumption - 20%.	none	none\0
	15378	1	Clan Hall - Harp Melody	For 30 min., P. Accuracy + 5, P. Evasion/ M. Evasion + 5, and Spd. + 34. Also, Received Critical Damage - 30%.	none	none\0
	15379	1	Clan Hall - Lute Melody	For 30 min., Atk. Spd. + 34%, Casting Spd. + 31%, and has a chance of applying 9% Vampiric Rage effect.	none	none\0
	
	15374	2	Clan Hall - Horn Melody	For 30 min., P. Atk. + 19%, P. Def. + 15%, M. Atk. + 83%, and M. Def. + 30%.	none	none\0
	15375	2	Clan Hall - Drum Melody	For 30 min., Max MP + 20%, HP Recovery Bonus + 20%, M. Critical Rate + 20, Critical Damage during a standard attack + 20%, P. Atk. + 10%, P. Def. + 20%, Atk. Spd. + 20%, M. Atk. + 20%, M. Def. + 20%, Casting Spd. + 20%, Debuff Resistance + 10%, and P. Def. when receiving a critical attack. Also, Spd. - 10%, and Critical Damage during a standard attack is increased after receiving a certain amount of damage.	none	none\0
	15376	2	Clan Hall - Pipe Organ Melody	For 30 min., Max HP/ MP/ CP + 39%, and HP Recovery Bonus + 20%.	none	none\0
	15377	2	Clan Hall - Guitar Melody	For 30 min., P. Critical Rate + 34%, P. Critical Damage + 35%, and M. Critical Rate + 20. Also, P. Skill MP Consumption - 20%, M. Skill MP Consumption - 10%, and the Iss Enchanter's Enchant Skill MP Consumption - 20%.	none	none\0
	15378	2	Clan Hall - Harp Melody	For 30 min., P. Accuracy + 6, P. Evasion/ M. Evasion + 6, and Spd. + 35. Also, Received Critical Damage - 30%.	none	none\0
	15379	2	Clan Hall - Lute Melody	For 30 min., Atk. Spd. + 35%, Casting Spd. + 32%, and has a chance of applying 9% Vampiric Rage effect.	none	none\0
	15380	1	Clan Hall - Knight's Harmony	For 30 min., P. Atk. + 12%, P. Critical Damage + 35%, P. Critical Rate + 100%, P. Def. + 45%, Shield Defense Rate + 30%, and Shield Defense + 50%.	none	none\0
	15381	1	Clan Hall - Warrior's Harmony	For 30 min., P. Atk. + 35%, P. Critical Damage + 35%, P. Critical Rate + 100%, Atk. Spd. + 8%, M. Atk. + 16%, Casting Spd. + 8%, and Spd. + 8.	none	none\0
	15382	1	Clan Hall - Wizard's Harmony	For 30 min., M. Atk. + 40%, M. Critical Rate + 100%, Casting Spd. + 8%, P. Atk. + 8%, Atk. Spd. + 8%, Spd. + 8, M. Def. + 14%, and MP Recovery Bonus + 20%. Also, M. Skill MP Consumption - 15%, P. Skill MP Consumption - 5%, Skill Cooldown - 20%.	none	none\0
	*/
	private static final Object[][][] buffs_template = {
		{
        		// level 0 - no buff
		},
		{
                // level 1
                { SkillTable.getInstance().getInfo(4342, 1), A }, { SkillTable.getInstance().getInfo(4343, 1), A }, { SkillTable.getInstance().getInfo(4344, 1), A },
                { SkillTable.getInstance().getInfo(4346, 1), A }, { SkillTable.getInstance().getInfo(4345, 1), W },
		},
		{
                // level 2
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W },
        },
        {
                // level 3
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W },
        },
        {
                // level 4
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W }, { SkillTable.getInstance().getInfo(4347, 2), A },
                { SkillTable.getInstance().getInfo(4349, 1), A }, { SkillTable.getInstance().getInfo(4350, 1), W }, { SkillTable.getInstance().getInfo(4348, 2), A },
        },
        {
                // level 5
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W }, { SkillTable.getInstance().getInfo(4347, 2), A },
                { SkillTable.getInstance().getInfo(4349, 1), A }, { SkillTable.getInstance().getInfo(4350, 1), W }, { SkillTable.getInstance().getInfo(4348, 2), A },
                { SkillTable.getInstance().getInfo(4351, 2), M }, { SkillTable.getInstance().getInfo(4352, 1), A }, { SkillTable.getInstance().getInfo(4353, 2), W },
                { SkillTable.getInstance().getInfo(4358, 1), W }, { SkillTable.getInstance().getInfo(4354, 1), W },
        },
        {
        // level 6 - unused
        },
        {
                // level 7
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W }, { SkillTable.getInstance().getInfo(4347, 6), A },
                { SkillTable.getInstance().getInfo(4349, 2), A }, { SkillTable.getInstance().getInfo(4350, 4), W }, { SkillTable.getInstance().getInfo(4348, 6), A },
                { SkillTable.getInstance().getInfo(4351, 6), M }, { SkillTable.getInstance().getInfo(4352, 2), A }, { SkillTable.getInstance().getInfo(4353, 6), W },
                { SkillTable.getInstance().getInfo(4358, 3), W }, { SkillTable.getInstance().getInfo(4354, 4), W },
                //GOD
                { SkillTable.getInstance().getInfo(15374, 1), M },
                { SkillTable.getInstance().getInfo(15375, 1), M },
                { SkillTable.getInstance().getInfo(15376, 1), M },
                { SkillTable.getInstance().getInfo(15377, 1), M },
                { SkillTable.getInstance().getInfo(15378, 1), M },
                { SkillTable.getInstance().getInfo(15379, 1), M },
                { SkillTable.getInstance().getInfo(15380, 1), M },
                { SkillTable.getInstance().getInfo(15381, 1), M },
                { SkillTable.getInstance().getInfo(15382, 1), M }
        },
        {		
                // level 8
                { SkillTable.getInstance().getInfo(4342, 2), A }, { SkillTable.getInstance().getInfo(4343, 3), A }, { SkillTable.getInstance().getInfo(4344, 3), A },
                { SkillTable.getInstance().getInfo(4346, 4), A }, { SkillTable.getInstance().getInfo(4345, 3), W }, { SkillTable.getInstance().getInfo(4347, 6), A },
                { SkillTable.getInstance().getInfo(4349, 2), A }, { SkillTable.getInstance().getInfo(4350, 4), W }, { SkillTable.getInstance().getInfo(4348, 6), A },
                { SkillTable.getInstance().getInfo(4351, 6), M }, { SkillTable.getInstance().getInfo(4352, 2), A }, { SkillTable.getInstance().getInfo(4353, 6), W },
                { SkillTable.getInstance().getInfo(4358, 3), W }, { SkillTable.getInstance().getInfo(4354, 4), W }, { SkillTable.getInstance().getInfo(4355, 1), M },
                { SkillTable.getInstance().getInfo(4356, 1), M }, { SkillTable.getInstance().getInfo(4357, 1), W }, { SkillTable.getInstance().getInfo(4359, 1), W },
                { SkillTable.getInstance().getInfo(4360, 1), W },
                //GOD
                { SkillTable.getInstance().getInfo(15374, 2), M },
                { SkillTable.getInstance().getInfo(15375, 2), M },
                { SkillTable.getInstance().getInfo(15376, 2), M },
                { SkillTable.getInstance().getInfo(15377, 2), M },
                { SkillTable.getInstance().getInfo(15378, 2), M },
                { SkillTable.getInstance().getInfo(15379, 2), M },
                { SkillTable.getInstance().getInfo(15380, 1), M },
                { SkillTable.getInstance().getInfo(15381, 1), M },
                { SkillTable.getInstance().getInfo(15382, 1), M }
        },
        {
        // Level 9 - unused 
        },
        {
        // level 10 - unused
        },
        {
                // level 11
                { SkillTable.getInstance().getInfo(4342, 3), A }, { SkillTable.getInstance().getInfo(4343, 4), A }, { SkillTable.getInstance().getInfo(4344, 4), A },
                { SkillTable.getInstance().getInfo(4346, 5), A }, { SkillTable.getInstance().getInfo(4345, 4), W },
        },
        {
                // level 12
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W },
        },
        {
                // level 13
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W },
        },
        {
                // level 14
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W }, { SkillTable.getInstance().getInfo(4347, 8), A },
                { SkillTable.getInstance().getInfo(4349, 3), A }, { SkillTable.getInstance().getInfo(4350, 5), W }, { SkillTable.getInstance().getInfo(4348, 8), A },
        },
        {
                // level 15
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W }, { SkillTable.getInstance().getInfo(4347, 8), A },
                { SkillTable.getInstance().getInfo(4349, 3), A }, { SkillTable.getInstance().getInfo(4350, 5), W }, { SkillTable.getInstance().getInfo(4348, 8), A },
                { SkillTable.getInstance().getInfo(4351, 8), M }, { SkillTable.getInstance().getInfo(4352, 3), A }, { SkillTable.getInstance().getInfo(4353, 8), W },
                { SkillTable.getInstance().getInfo(4358, 4), W }, { SkillTable.getInstance().getInfo(4354, 5), W },
        },
        {
        // level 16 - unused
        },
        {
                // level 17
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W }, { SkillTable.getInstance().getInfo(4347, 12), A },
                { SkillTable.getInstance().getInfo(4349, 4), A }, { SkillTable.getInstance().getInfo(4350, 8), W }, { SkillTable.getInstance().getInfo(4348, 12), A },
                { SkillTable.getInstance().getInfo(4351, 12), M }, { SkillTable.getInstance().getInfo(4352, 4), A }, { SkillTable.getInstance().getInfo(4353, 12), W },
                { SkillTable.getInstance().getInfo(4358, 6), W }, { SkillTable.getInstance().getInfo(4354, 8), W },
        },
        {
                // level 18
                { SkillTable.getInstance().getInfo(4342, 4), A }, { SkillTable.getInstance().getInfo(4343, 6), A }, { SkillTable.getInstance().getInfo(4344, 6), A },
                { SkillTable.getInstance().getInfo(4346, 8), A }, { SkillTable.getInstance().getInfo(4345, 6), W }, { SkillTable.getInstance().getInfo(4347, 12), A },
                { SkillTable.getInstance().getInfo(4349, 4), A }, { SkillTable.getInstance().getInfo(4350, 8), W }, { SkillTable.getInstance().getInfo(4348, 12), A },
                { SkillTable.getInstance().getInfo(4351, 12), M }, { SkillTable.getInstance().getInfo(4352, 4), A }, { SkillTable.getInstance().getInfo(4353, 12), W },
                { SkillTable.getInstance().getInfo(4358, 6), W }, { SkillTable.getInstance().getInfo(4354, 8), W }, { SkillTable.getInstance().getInfo(4355, 4), M },
                { SkillTable.getInstance().getInfo(4356, 4), M }, { SkillTable.getInstance().getInfo(4357, 3), W }, { SkillTable.getInstance().getInfo(4359, 4), W },
                { SkillTable.getInstance().getInfo(4360, 4), W },
        },
	};
}