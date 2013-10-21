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
import lineage2.gameserver.data.xml.holder.LevelBonusHolder;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class LevelBonusParser extends AbstractFileParser<LevelBonusHolder>
{
	/**
	 * Field _instance.
	 */
	private static final LevelBonusParser _instance = new LevelBonusParser();
	
	/**
	 * Method getInstance.
	 * @return LevelBonusParser
	 */
	public static LevelBonusParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for LevelBonusParser.
	 */
	private LevelBonusParser()
	{
		super(LevelBonusHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/pc_parameters/lvl_bonus_data.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "lvl_bonus_data.dtd";
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
			Element element = iterator.next();
			if ("lvl_bonus".equalsIgnoreCase(element.getName()))
			{
				for (Element e : element.elements())
				{
					int lvl = Integer.parseInt(e.attributeValue("lvl"));
					double bonusMod = Double.parseDouble(e.attributeValue("value"));
					(getHolder()).addLevelBonus(lvl, bonusMod);
				}
			}
		}
	}
}
