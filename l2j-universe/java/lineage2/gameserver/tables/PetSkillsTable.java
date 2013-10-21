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
package lineage2.gameserver.tables;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.Summon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetSkillsTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PetSkillsTable.class);
	/**
	 * Field _skillTrees.
	 */
	private final TIntObjectHashMap<List<SkillLearn>> _skillTrees = new TIntObjectHashMap<>();
	/**
	 * Field _instance.
	 */
	private static PetSkillsTable _instance = new PetSkillsTable();
	
	/**
	 * Method getInstance.
	 * @return PetSkillsTable
	 */
	public static PetSkillsTable getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		_instance = new PetSkillsTable();
	}
	
	/**
	 * Constructor for PetSkillsTable.
	 */
	private PetSkillsTable()
	{
		load();
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		int npcId = 0;
		int count = 0;
		int id = 0;
		int lvl = 0;
		int minLvl = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM pets_skills ORDER BY templateId");
			rset = statement.executeQuery();
			while (rset.next())
			{
				npcId = rset.getInt("templateId");
				id = rset.getInt("skillId");
				lvl = rset.getInt("skillLvl");
				minLvl = rset.getInt("minLvl");
				List<SkillLearn> list = _skillTrees.get(npcId);
				if (list == null)
				{
					_skillTrees.put(npcId, list = new ArrayList<>());
				}
				SkillLearn skillLearn = new SkillLearn(id, lvl, minLvl, 0, 0, 0, false, false, null, new HashMap<Integer, Long>(), new ArrayList<Integer>());
				list.add(skillLearn);
				count++;
			}
		}
		catch (Exception e)
		{
			_log.error("Error while creating pet skill tree (Pet ID " + npcId + ")", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("PetSkillsTable: Loaded " + count + " skills.");
	}
	
	/**
	 * Method getAvailableLevel.
	 * @param cha Summon
	 * @param skillId int
	 * @return int
	 */
	public int getAvailableLevel(Summon cha, int skillId)
	{
		List<SkillLearn> skills = _skillTrees.get(cha.getNpcId());
		if (skills == null)
		{
			return 0;
		}
		int lvl = 0;
		for (SkillLearn temp : skills)
		{
			if (temp.getId() != skillId)
			{
				continue;
			}
			if (temp.getLevel() == 0)
			{
				if (cha.getLevel() < 70)
				{
					lvl = cha.getLevel() / 10;
					if (lvl <= 0)
					{
						lvl = 1;
					}
				}
				else
				{
					lvl = 7 + ((cha.getLevel() - 70) / 5);
				}
				int maxLvl = SkillTable.getInstance().getMaxLevel(temp.getId());
				if (lvl > maxLvl)
				{
					lvl = maxLvl;
				}
				break;
			}
			else if (temp.getMinLevel() <= cha.getLevel())
			{
				if (temp.getLevel() > lvl)
				{
					lvl = temp.getLevel();
				}
			}
		}
		return lvl;
	}
}
