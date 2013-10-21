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

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.PetDataHolder;
import lineage2.gameserver.templates.StatsSet;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetDataParser extends AbstractDirParser<PetDataHolder>
{
	/**
	 * Field _instance.
	 */
	private static PetDataParser _instance = new PetDataParser();
	
	/**
	 * Method getInstance.
	 * @return PetDataParser
	 */
	public static PetDataParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for PetDataParser.
	 * @param holder PetDataHolder
	 */
	protected PetDataParser(PetDataHolder holder)
	{
		super(holder);
	}
	
	/**
	 * Constructor for PetDataParser.
	 */
	public PetDataParser()
	{
		super(PetDataHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/stats/pets/");
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
		return "pets.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 */
	@Override
	protected void readData(Element rootElement)
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("pet"); iterator.hasNext();)
		{
			Element pet = iterator.next();
			StatsSet set = new StatsSet();
			set.set("id", pet.attributeValue("id"));
			set.set("index", pet.attributeValue("index"));
			for (Iterator<Element> i1 = pet.elementIterator(); i1.hasNext();)
			{
				Element element = i1.next();
				if (element.getName().equals("set"))
				{
					set.set(element.attributeValue("name"), element.attributeValue("val"));
				}
				else if (element.getName().equals("stats"))
				{
					for (Iterator<Element> itr = element.elementIterator("stat"); itr.hasNext();)
					{
						Element stat = itr.next();
						set.set("level", stat.attributeValue("level"));
						for (Iterator<Element> it = stat.elementIterator("set"); it.hasNext();)
						{
							Element e = it.next();
							set.set(e.attributeValue("name"), e.attributeValue("val"));
						}
					}
				}
			}
			getHolder().addPetData(set);
		}
	}
}
