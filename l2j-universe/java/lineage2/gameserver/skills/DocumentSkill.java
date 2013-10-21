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
package lineage2.gameserver.skills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DocumentSkill extends DocumentBase
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(DocumentSkill.class);
	
	/**
	 * @author Mobius
	 */
	public class Skill
	{
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field name.
		 */
		public String name;
		/**
		 * Field sets.
		 */
		public StatsSet[] sets;
		/**
		 * Field currentLevel.
		 */
		public int currentLevel;
		/**
		 * Field skills.
		 */
		public List<lineage2.gameserver.model.Skill> skills = new ArrayList<>();
		/**
		 * Field currentSkills.
		 */
		public List<lineage2.gameserver.model.Skill> currentSkills = new ArrayList<>();
	}
	
	/**
	 * Field currentSkill.
	 */
	private Skill currentSkill;
	/**
	 * Field usedTables.
	 */
	private final Set<String> usedTables = new HashSet<>();
	/**
	 * Field skillsInFile.
	 */
	private final List<lineage2.gameserver.model.Skill> skillsInFile = new LinkedList<>();
	
	/**
	 * Constructor for DocumentSkill.
	 * @param file File
	 */
	DocumentSkill(File file)
	{
		super(file);
	}
	
	/**
	 * Method resetTable.
	 */
	@Override
	protected void resetTable()
	{
		if (!usedTables.isEmpty())
		{
			for (String table : tables.keySet())
			{
				if (!usedTables.contains(table))
				{
					if (_log.isDebugEnabled())
					{
						_log.debug("Unused table " + table + " for skill " + currentSkill.id);
					}
				}
			}
		}
		usedTables.clear();
		super.resetTable();
	}
	
	/**
	 * Method setCurrentSkill.
	 * @param skill Skill
	 */
	private void setCurrentSkill(Skill skill)
	{
		currentSkill = skill;
	}
	
	/**
	 * Method getSkills.
	 * @return List<lineage2.gameserver.model.Skill>
	 */
	protected List<lineage2.gameserver.model.Skill> getSkills()
	{
		return skillsInFile;
	}
	
	/**
	 * Method getTableValue.
	 * @param name String
	 * @return Object
	 */
	@Override
	protected Object getTableValue(String name)
	{
		try
		{
			usedTables.add(name);
			Object[] a = tables.get(name);
			if ((a.length - 1) >= currentSkill.currentLevel)
			{
				return a[currentSkill.currentLevel];
			}
			return a[a.length - 1];
		}
		catch (RuntimeException e)
		{
			_log.error("Error in table " + name + " of skill Id " + currentSkill.id, e);
			return 0;
		}
	}
	
	/**
	 * Method getTableValue.
	 * @param name String
	 * @param idx int
	 * @return Object
	 */
	@Override
	protected Object getTableValue(String name, int idx)
	{
		idx--;
		try
		{
			usedTables.add(name);
			Object[] a = tables.get(name);
			if ((a.length - 1) >= idx)
			{
				return a[idx];
			}
			return a[a.length - 1];
		}
		catch (Exception e)
		{
			_log.error("Wrong level count in skill Id " + currentSkill.id + " table " + name + " level " + idx, e);
			return 0;
		}
	}
	
	/**
	 * Method parseDocument.
	 * @param doc Document
	 */
	@Override
	protected void parseDocument(Document doc)
	{
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("skill".equalsIgnoreCase(d.getNodeName()))
					{
						setCurrentSkill(new Skill());
						parseSkill(d);
						skillsInFile.addAll(currentSkill.skills);
						resetTable();
					}
				}
			}
			else if ("skill".equalsIgnoreCase(n.getNodeName()))
			{
				setCurrentSkill(new Skill());
				parseSkill(n);
				skillsInFile.addAll(currentSkill.skills);
			}
		}
	}
	
	/**
	 * Method parseSkill.
	 * @param n Node
	 */
	protected void parseSkill(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		int skillId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
		String skillName = attrs.getNamedItem("name").getNodeValue();
		String levels = attrs.getNamedItem("levels").getNodeValue();
		int lastLvl = Integer.parseInt(levels);
		try
		{
			Map<Integer, Integer> displayLevels = new HashMap<>();
			Node enchant = null;
			Map<String, Object[]> etables = new HashMap<>();
			int[] realEnchantRoute = new int[10];
			
			int count = 0, eLevels = 0;
			Node d = n.cloneNode(true);
			for (int k = 0; k < d.getChildNodes().getLength(); k++)
			{
				enchant = d.getChildNodes().item(k);
				if (!enchant.getNodeName().startsWith("enchant"))
				{
					continue;
				}
				if (eLevels == 0)
				{
					if (enchant.getAttributes().getNamedItem("levels") != null)
					{
						eLevels = Integer.parseInt(enchant.getAttributes().getNamedItem("levels").getNodeValue());
					}
					else
					{
						eLevels = 30;
					}
				}
				String ename = enchant.getAttributes().getNamedItem("name").getNodeValue();
				int enchRoute = 0;
				if (enchant.getAttributes().getNamedItem("enchRoute") != null)
				{
					enchRoute = Integer.parseInt(enchant.getAttributes().getNamedItem("enchRoute").getNodeValue());
				}
				else
				{
					enchRoute = count +1;
				}
				for (int r = 1; r <= eLevels; r++)
				{
					int level;
					int levelskilllearn;
					if (Config.ENCHANT_SKILLSID_RETAIL)
					{
						level = (enchRoute * 100) + r;
						levelskilllearn = level;
					}
					else
					{
						level = lastLvl + (eLevels * count) + r;
						levelskilllearn = (100 * (count + 1)) + r;
					}
					EnchantSkillLearn e = new EnchantSkillLearn(skillId, levelskilllearn, skillName, "+" + r + " " + ename, r == 1 ? lastLvl : levelskilllearn - 1, lastLvl, eLevels);
					List<EnchantSkillLearn> t = SkillTreeTable._enchant.get(skillId);
					if (t == null)
					{
						t = new ArrayList<>();
					}
					t.add(e);
					SkillTreeTable._enchant.put(skillId, t);
					displayLevels.put(level, levelskilllearn);
				}
				count++;
				realEnchantRoute[count] = enchRoute;
				
				Node first = enchant.getFirstChild();
				Node curr = null;
				for (curr = first; curr != null; curr = curr.getNextSibling())
				{
					if ("table".equalsIgnoreCase(curr.getNodeName()))
					{
						NamedNodeMap a = curr.getAttributes();
						String name = a.getNamedItem("name").getNodeValue();
						Object[] table = parseTable(curr);
						table = fillTableToSize(table, eLevels);
						Object[] fulltable = etables.get(name);
						if (fulltable == null)
						{
							fulltable = new Object[lastLvl + (eLevels * 9) + 1];
						}
						System.arraycopy(table, 0, fulltable, lastLvl + ((count - 1) * eLevels), eLevels);
						etables.put(name, fulltable);
					}
				}
			}
			lastLvl += eLevels * count;
			currentSkill.id = skillId;
			currentSkill.name = skillName;
			currentSkill.sets = new StatsSet[lastLvl];
			int bLevels = Integer.parseInt(levels);
			for (int i = 0; i < lastLvl; i++)
			{
				int skilllevel = i + 1;
				if (Config.ENCHANT_SKILLSID_RETAIL)
				{
					int current = i + 1;
					if ((current - bLevels) > 0) // is enchant
					{
						int tmplvl = current - bLevels - 1;
						int enchantRoute = (tmplvl - (tmplvl % eLevels)) / eLevels;
						int enchantLevel = tmplvl - (enchantRoute * eLevels);
						skilllevel = (100 * (realEnchantRoute[enchantRoute + 1])) + enchantLevel + 1;
					}
				}
				currentSkill.sets[i] = new StatsSet();
				currentSkill.sets[i].set("skill_id", currentSkill.id);
				currentSkill.sets[i].set("level", skilllevel);
				currentSkill.sets[i].set("name", currentSkill.name);
				currentSkill.sets[i].set("base_level", levels);
			}
			if (currentSkill.sets.length != lastLvl)
			{
				throw new RuntimeException("Skill id=" + skillId + " number of levels missmatch, " + lastLvl + " levels expected");
			}
			Node first = n.getFirstChild();
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("table".equalsIgnoreCase(n.getNodeName()))
				{
					parseTable(n);
				}
			}
			for (String tn : tables.keySet())
			{
				Object[] et = etables.get(tn);
				if (et != null)
				{
					Object[] t = tables.get(tn);
					Object max = t[t.length - 1];
					System.arraycopy(t, 0, et, 0, t.length);
					for (int j = 0; j < et.length; j++)
					{
						if (et[j] == null)
						{
							et[j] = max;
						}
					}
					tables.put(tn, et);
				}
			}
			for (int i = 1; i <= lastLvl; i++)
			{
				for (n = first; n != null; n = n.getNextSibling())
				{
					if ("set".equalsIgnoreCase(n.getNodeName()))
					{
						parseBeanSet(n, currentSkill.sets[i - 1], i);
					}
				}
			}
			makeSkills();
			for (int i = 0; i < lastLvl; i++)
			{
				int skilllevel = i;
				currentSkill.currentLevel = skilllevel;
				
				lineage2.gameserver.model.Skill current = currentSkill.currentSkills.get(i);
				if (displayLevels.get(current.getLevel()) != null)
				{
					current.setDisplayLevel(displayLevels.get(current.getLevel()));
				}
				current.setEnchantLevelCount(eLevels);
				for (n = first; n != null; n = n.getNextSibling())
				{
					if ("cond".equalsIgnoreCase(n.getNodeName()))
					{
						Condition condition = parseCondition(n.getFirstChild());
						if (condition != null)
						{
							Node msgAttribute = n.getAttributes().getNamedItem("msgId");
							if (msgAttribute != null)
							{
								int msgId = parseNumber(msgAttribute.getNodeValue()).intValue();
								condition.setSystemMsg(msgId);
							}
							current.attach(condition);
						}
					}
					else if ("for".equalsIgnoreCase(n.getNodeName()))
					{
						parseTemplate(n, current);
					}
					else if ("triggers".equalsIgnoreCase(n.getNodeName()))
					{
						parseTrigger(n, current);
					}
				}
			}
			currentSkill.skills.addAll(currentSkill.currentSkills);
		}
		catch (Exception e)
		{
			_log.error("Error loading skill " + skillId, e);
		}
	}
	
	/**
	 * Method fillTableToSize.
	 * @param table Object[]
	 * @param size int
	 * @return Object[]
	 */
	private Object[] fillTableToSize(Object[] table, int size)
	{
		if (table.length < size)
		{
			Object[] ret = new Object[size];
			System.arraycopy(table, 0, ret, 0, table.length);
			table = ret;
		}
		for (int j = 1; j < size; j++)
		{
			if (table[j] == null)
			{
				table[j] = table[j - 1];
			}
		}
		return table;
	}
	
	/**
	 * Method makeSkills.
	 */
	private void makeSkills()
	{
		currentSkill.currentSkills = new ArrayList<>(currentSkill.sets.length);
		for (int i = 0; i < currentSkill.sets.length; i++)
		{
			currentSkill.currentSkills.add(i, currentSkill.sets[i].getEnum("skillType", SkillType.class).makeSkill(currentSkill.sets[i]));
		}
	}
}
