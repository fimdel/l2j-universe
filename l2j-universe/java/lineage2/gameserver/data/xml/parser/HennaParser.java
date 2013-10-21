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

import gnu.trove.list.array.TIntArrayList;

import java.io.File;
import java.util.Iterator;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.HennaHolder;
import lineage2.gameserver.templates.Henna;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class HennaParser extends AbstractFileParser<HennaHolder>
{
	/**
	 * Field _instance.
	 */
	private static final HennaParser _instance = new HennaParser();
	
	/**
	 * Method getInstance.
	 * @return HennaParser
	 */
	public static HennaParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for HennaParser.
	 */
	protected HennaParser()
	{
		super(HennaHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/hennas.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "hennas.dtd";
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
			Element hennaElement = iterator.next();
			int symbolId = Integer.parseInt(hennaElement.attributeValue("symbol_id"));
			int dyeId = Integer.parseInt(hennaElement.attributeValue("dye_id"));
			long price = Integer.parseInt(hennaElement.attributeValue("price"));
			long drawCount = hennaElement.attributeValue("draw_count") == null ? 10 : Integer.parseInt(hennaElement.attributeValue("draw_count"));
			int wit = Integer.parseInt(hennaElement.attributeValue("wit"));
			int str = Integer.parseInt(hennaElement.attributeValue("str"));
			int _int = Integer.parseInt(hennaElement.attributeValue("int"));
			int con = Integer.parseInt(hennaElement.attributeValue("con"));
			int dex = Integer.parseInt(hennaElement.attributeValue("dex"));
			int men = Integer.parseInt(hennaElement.attributeValue("men"));
			int skillId = hennaElement.attributeValue("skillId") == null ? 0 : Integer.parseInt(hennaElement.attributeValue("skillId"));
			TIntArrayList list = new TIntArrayList();
			for (Iterator<Element> classIterator = hennaElement.elementIterator("class"); classIterator.hasNext();)
			{
				Element classElement = classIterator.next();
				list.add(Integer.parseInt(classElement.attributeValue("id")));
			}
			Henna henna = new Henna(symbolId, dyeId, price, drawCount, wit, _int, con, str, dex, men, skillId, list);
			getHolder().addHenna(henna);
		}
	}
}
