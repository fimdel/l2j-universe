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
package lineage2.gameserver.data.xml.parser;

import gnu.trove.map.hash.TIntIntHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.CubicHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.CubicTemplate;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class CubicParser extends AbstractFileParser<CubicHolder>
{
	/**
	 * Field _instance.
	 */
	private static CubicParser _instance = new CubicParser();
	
	/**
	 * Method getInstance.
	 * @return CubicParser
	 */
	public static CubicParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for CubicParser.
	 */
	protected CubicParser()
	{
		super(CubicHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/cubics.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "cubics.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element cubicElement = (Element) iterator.next();
			int id = Integer.parseInt(cubicElement.attributeValue("id"));
			int level = Integer.parseInt(cubicElement.attributeValue("level"));
			int delay = Integer.parseInt(cubicElement.attributeValue("delay"));
			CubicTemplate template = new CubicTemplate(id, level, delay);
			getHolder().addCubicTemplate(template);
			for (Iterator<?> skillsIterator = cubicElement.elementIterator(); skillsIterator.hasNext();)
			{
				Element skillsElement = (Element) skillsIterator.next();
				int chance = Integer.parseInt(skillsElement.attributeValue("chance"));
				List<CubicTemplate.SkillInfo> skills = new ArrayList<>(1);
				for (Iterator<?> skillIterator = skillsElement.elementIterator(); skillIterator.hasNext();)
				{
					Element skillElement = (Element) skillIterator.next();
					int id2 = Integer.parseInt(skillElement.attributeValue("id"));
					int level2 = Integer.parseInt(skillElement.attributeValue("level"));
					int chance2 = skillElement.attributeValue("chance") == null ? 0 : Integer.parseInt(skillElement.attributeValue("chance"));
					boolean canAttackDoor = Boolean.parseBoolean(skillElement.attributeValue("can_attack_door"));
					CubicTemplate.ActionType type = CubicTemplate.ActionType.valueOf(skillElement.attributeValue("action_type"));
					TIntIntHashMap set = new TIntIntHashMap();
					for (Iterator<?> chanceIterator = skillElement.elementIterator(); chanceIterator.hasNext();)
					{
						Element chanceElement = (Element) chanceIterator.next();
						int min = Integer.parseInt(chanceElement.attributeValue("min"));
						int max = Integer.parseInt(chanceElement.attributeValue("max"));
						int value = Integer.parseInt(chanceElement.attributeValue("value"));
						for (int i = min; i <= max; i++)
						{
							set.put(i, value);
						}
					}
					if ((chance2 == 0) && set.isEmpty())
					{
						warn("Wrong skill chance. Cubic: " + id + "/" + level);
					}
					Skill skill = SkillTable.getInstance().getInfo(id2, level2);
					if (skill != null)
					{
						skill.setCubicSkill(true);
						skills.add(new CubicTemplate.SkillInfo(skill, chance2, type, canAttackDoor, set));
					}
				}
				template.putSkills(chance, skills);
			}
		}
	}
}
