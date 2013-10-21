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

import java.io.File;
import java.util.Iterator;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.OptionDataHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.OptionDataTemplate;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class OptionDataParser extends StatParser<OptionDataHolder>
{
	/**
	 * Field _instance.
	 */
	private static final OptionDataParser _instance = new OptionDataParser();
	
	/**
	 * Method getInstance.
	 * @return OptionDataParser
	 */
	public static OptionDataParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for OptionDataParser.
	 */
	protected OptionDataParser()
	{
		super(OptionDataHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/option_data");
	}
	
	/**
	 * Method isIgnored.
	 * @param f File
	 * @return boolean
	 */
	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "option_data.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> itemIterator = rootElement.elementIterator(); itemIterator.hasNext();)
		{
			Element optionDataElement = itemIterator.next();
			OptionDataTemplate template = new OptionDataTemplate(Integer.parseInt(optionDataElement.attributeValue("id")));
			for (Iterator<Element> subIterator = optionDataElement.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				String subName = subElement.getName();
				if (subName.equalsIgnoreCase("for"))
				{
					parseFor(subElement, template);
				}
				else if (subName.equalsIgnoreCase("triggers"))
				{
					parseTriggers(subElement, template);
				}
				else if (subName.equalsIgnoreCase("skills"))
				{
					for (Iterator<Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));
						Skill skill = SkillTable.getInstance().getInfo(id, level);
						if (skill != null)
						{
							template.addSkill(skill);
						}
						else
						{
							info("Skill not found(" + id + "," + level + ") for option data:" + template.getId() + "; file:" + getCurrentFileName());
						}
					}
				}
			}
			getHolder().addTemplate(template);
		}
	}
	
	/**
	 * Method getTableValue.
	 * @param name String
	 * @return Object
	 */
	@Override
	protected Object getTableValue(String name)
	{
		return null;
	}
}
