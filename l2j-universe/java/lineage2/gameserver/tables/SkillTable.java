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

import gnu.trove.map.hash.TIntIntHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.skills.SkillsEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SkillTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SkillTable.class);
	/**
	 * Field _instance.
	 */
	private static final SkillTable _instance = new SkillTable();
	/**
	 * Field identifySkills.
	 */
	public Map<Integer, Integer> identifySkills = new HashMap<>();
	/**
	 * Field _skills.
	 */
	private Map<Integer, Skill> _skills;
	/**
	 * Field _maxLevelsTable.
	 */
	private TIntIntHashMap _maxLevelsTable;
	/**
	 * Field _baseLevelsTable.
	 */
	private TIntIntHashMap _baseLevelsTable;
	
	/**
	 * Method getInstance.
	 * @return SkillTable
	 */
	public static final SkillTable getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		_skills = SkillsEngine.getInstance().loadAllSkills();
		makeLevelsTable();
		loadAlfaData();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * Method getInfo.
	 * @param skillId int
	 * @param level int
	 * @return Skill
	 */
	public Skill getInfo(int skillId, int level)
	{
		return _skills.get(getSkillHashCode(skillId, level));
	}
	
	/**
	 * Method getMaxLevel.
	 * @param skillId int
	 * @return int
	 */
	public int getMaxLevel(int skillId)
	{
		return _maxLevelsTable.get(skillId);
	}
	
	/**
	 * Method getBaseLevel.
	 * @param skillId int
	 * @return int
	 */
	public int getBaseLevel(int skillId)
	{
		return _baseLevelsTable.get(skillId);
	}
	
	/**
	 * Method getSkillHashCode.
	 * @param skill Skill
	 * @return int
	 */
	public static int getSkillHashCode(Skill skill)
	{
		return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel());
	}
	
	/**
	 * Method getSkillHashCode.
	 * @param skillId int
	 * @param skillLevel int
	 * @return int
	 */
	public static int getSkillHashCode(int skillId, int skillLevel)
	{
		return (skillId * 1000) + skillLevel;
	}
	
	/**
	 * Method makeLevelsTable.
	 */
	private void makeLevelsTable()
	{
		_maxLevelsTable = new TIntIntHashMap();
		_baseLevelsTable = new TIntIntHashMap();
		for (Skill s : _skills.values())
		{
			int skillId = s.getId();
			int level = s.getLevel();
			int maxLevel = _maxLevelsTable.get(skillId);
			if (level > maxLevel)
			{
				_maxLevelsTable.put(skillId, level);
			}
			if (_baseLevelsTable.get(skillId) == 0)
			{
				_baseLevelsTable.put(skillId, s.getBaseLevel());
			}
		}
	}
	
	/**
	 * Method loadAlfaData.
	 */
	public void loadAlfaData()
	{
		LineNumberReader lnr = null;
		try
		{
			File rsData = new File(Config.DATAPACK_ROOT, "data/xml/asc/skills.txt");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(rsData)));
			String line = null;
			loop:
			while ((line = lnr.readLine()) != null)
			{
				if ((line.trim().length() == 0) || ((line.length() > 0) && (line.charAt(0) == '#')))
				{
					continue;
				}
				String args[] = line.split("\t", -1);
				int id = getInt(args[0]);
				int lvl = getInt(args[1]);
				int mp_consume = getInt(args[2]);
				int cast_range = getInt(args[3]);
				int hit_time = getInt(args[4]);
				int cool = getInt(args[5]);
				int reuse = getInt(args[6]);
				boolean is_magic = (getInt(args[7]) == 1) || (getInt(args[7]) == 2);
				if (lvl > 100)
				{
					EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(id, lvl);
					if (sl == null)
					{
						for (int i = 1; i < 8; i++)
						{
							int l = lvl - (i * 100);
							if (l < 100)
							{
								continue;
							}
							sl = SkillTreeTable.getSkillEnchant(id, l);
							if (sl != null)
							{
								lvl = l;
								break;
							}
						}
						if (sl == null)
						{
							continue loop;
						}
					}
					lvl = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), lvl, sl.getMaxLevel());
				}
				Skill s = getInfo(id, lvl);
				if (s != null)
				{
					if (reuse > 0)
					{
						s.setReuseDelay(reuse);
					}
					if (cool > 0)
					{
						s.setCoolTime(cool);
					}
					if (hit_time > 0)
					{
						s.setHitTime(hit_time);
					}
					if (s.getSkillInterruptTime() == 0)
					{
						s.setSkillInterruptTime((s.getHitTime() * 3) / 4);
					}
					if (mp_consume > 0)
					{
						if (((mp_consume / 4) >= 1) && is_magic)
						{
							s.setMpConsume1((mp_consume * 1.) / 4);
							s.setMpConsume2((mp_consume * 3.) / 4);
						}
						else
						{
							s.setMpConsume2(mp_consume);
						}
					}
					if (cast_range > 0)
					{
						s.setCastRange(cast_range);
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			_log.info("data/xml/asc/skills.txt is missing in data folder");
		}
		catch (Exception e)
		{
			_log.info("error while loading alfas-style skills " + e);
		}
		finally
		{
			try
			{
				if (lnr != null)
				{
					lnr.close();
				}
			}
			catch (Exception e1)
			{
			}
		}
		_log.info("Load ASC skills...");
	}
	
	/**
	 * Method getInt.
	 * @param name String
	 * @return int
	 */
	private static int getInt(String name)
	{
		String[] args = name.split("=", -1);
		return Integer.parseInt(args[1]);
	}
}
