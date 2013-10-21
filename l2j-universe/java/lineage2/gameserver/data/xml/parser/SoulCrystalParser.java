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
import lineage2.gameserver.data.xml.holder.SoulCrystalHolder;
import lineage2.gameserver.templates.SoulCrystal;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SoulCrystalParser extends AbstractFileParser<SoulCrystalHolder>
{
	/**
	 * Field _instance.
	 */
	private static final SoulCrystalParser _instance = new SoulCrystalParser();
	
	/**
	 * Method getInstance.
	 * @return SoulCrystalParser
	 */
	public static SoulCrystalParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SoulCrystalParser.
	 */
	private SoulCrystalParser()
	{
		super(SoulCrystalHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/soul_crystals.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "soul_crystals.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<?> iterator = rootElement.elementIterator("crystal"); iterator.hasNext();)
		{
			Element element = (Element) iterator.next();
			int itemId = Integer.parseInt(element.attributeValue("item_id"));
			int level = Integer.parseInt(element.attributeValue("level"));
			int nextItemId = Integer.parseInt(element.attributeValue("next_item_id"));
			int cursedNextItemId = element.attributeValue("cursed_next_item_id") == null ? 0 : Integer.parseInt(element.attributeValue("cursed_next_item_id"));
			getHolder().addCrystal(new SoulCrystal(itemId, level, nextItemId, cursedNextItemId));
		}
	}
}
