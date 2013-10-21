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
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.templates.item.RecipeTemplate;
import lineage2.gameserver.templates.item.RecipeTemplate.RecipeComponent;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RecipeParser extends AbstractFileParser<RecipeHolder>
{
	/**
	 * Field _instance.
	 */
	private static final RecipeParser _instance = new RecipeParser();
	
	/**
	 * Method getInstance.
	 * @return RecipeParser
	 */
	public static RecipeParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for RecipeParser.
	 */
	private RecipeParser()
	{
		super(RecipeHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/recipes.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "recipes.dtd";
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
			final int id = Integer.parseInt(element.attributeValue("id"));
			final int level = Integer.parseInt(element.attributeValue("level"));
			final int mpConsume = element.attributeValue("mp_consume") == null ? 0 : Integer.parseInt(element.attributeValue("mp_consume"));
			final int successRate = Integer.parseInt(element.attributeValue("success_rate"));
			final int itemId = Integer.parseInt(element.attributeValue("item_id"));
			final boolean isDwarven = element.attributeValue("is_dwarven") == null ? false : Boolean.parseBoolean(element.attributeValue("is_dwarven"));
			RecipeTemplate recipe = new RecipeTemplate(id, level, mpConsume, successRate, itemId, isDwarven);
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				if ("materials".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("item".equalsIgnoreCase(e.getName()))
						{
							int item_id = Integer.parseInt(e.attributeValue("id"));
							long count = Long.parseLong(e.attributeValue("count"));
							recipe.addMaterial(new RecipeComponent(item_id, count));
						}
					}
				}
				else if ("products".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("item".equalsIgnoreCase(e.getName()))
						{
							int item_id = Integer.parseInt(e.attributeValue("id"));
							long count = Long.parseLong(e.attributeValue("count"));
							int chance = Integer.parseInt(e.attributeValue("chance"));
							recipe.addProduct(new RecipeComponent(item_id, count, chance));
						}
					}
				}
				else if ("npc_fee".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("item".equalsIgnoreCase(e.getName()))
						{
							int item_id = Integer.parseInt(e.attributeValue("id"));
							long count = Long.parseLong(e.attributeValue("count"));
							recipe.addNpcFee(new RecipeComponent(item_id, count));
						}
					}
				}
			}
			getHolder().addRecipe(recipe);
		}
	}
}
