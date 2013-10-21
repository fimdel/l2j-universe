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

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.PetitionGroupHolder;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.model.petition.PetitionSubGroup;
import lineage2.gameserver.utils.Language;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetitionGroupParser extends AbstractFileParser<PetitionGroupHolder>
{
	/**
	 * Field _instance.
	 */
	private static PetitionGroupParser _instance = new PetitionGroupParser();
	
	/**
	 * Method getInstance.
	 * @return PetitionGroupParser
	 */
	public static PetitionGroupParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for PetitionGroupParser.
	 */
	private PetitionGroupParser()
	{
		super(PetitionGroupHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/petition_group.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "petition_group.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element groupElement = iterator.next();
			PetitionMainGroup group = new PetitionMainGroup(Integer.parseInt(groupElement.attributeValue("id")));
			getHolder().addPetitionGroup(group);
			for (Iterator<Element> subIterator = groupElement.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				if ("name".equals(subElement.getName()))
				{
					group.setName(Language.valueOf(subElement.attributeValue("lang")), subElement.getText());
				}
				else if ("description".equals(subElement.getName()))
				{
					group.setDescription(Language.valueOf(subElement.attributeValue("lang")), subElement.getText());
				}
				else if ("sub_group".equals(subElement.getName()))
				{
					PetitionSubGroup subGroup = new PetitionSubGroup(Integer.parseInt(subElement.attributeValue("id")), subElement.attributeValue("handler"));
					group.addSubGroup(subGroup);
					for (Iterator<Element> sub2Iterator = subElement.elementIterator(); sub2Iterator.hasNext();)
					{
						Element sub2Element = sub2Iterator.next();
						if ("name".equals(sub2Element.getName()))
						{
							subGroup.setName(Language.valueOf(sub2Element.attributeValue("lang")), sub2Element.getText());
						}
						else if ("description".equals(sub2Element.getName()))
						{
							subGroup.setDescription(Language.valueOf(sub2Element.attributeValue("lang")), sub2Element.getText());
						}
					}
				}
			}
		}
	}
}
