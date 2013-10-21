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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Experience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LifeParamTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(LifeParamTable.class.getName());
	/**
	 * Field CLASSES_COUNT.
	 */
	private static final int CLASSES_COUNT = ClassId.VALUES.length;
	/**
	 * Field LEVEL_COUNTER.
	 */
	private static final int LEVEL_COUNTER = Experience.LEVEL.length;
	/**
	 * Field hpTable.
	 */
	private static final double[][] hpTable = new double[CLASSES_COUNT][LEVEL_COUNTER];
	/**
	 * Field mpTable.
	 */
	private static final double[][] mpTable = new double[CLASSES_COUNT][LEVEL_COUNTER];
	/**
	 * Field cpTable.
	 */
	private static final double[][] cpTable = new double[CLASSES_COUNT][LEVEL_COUNTER];
	static
	{
		final File dir = new File(Config.DATAPACK_ROOT, "data/xml/asc/model/player/pclifeparam");
		for (final File list : dir.listFiles())
		{
			if (list.getName().endsWith(".xml"))
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringComments(true);
				Document doc = null;
				try
				{
					try
					{
						doc = factory.newDocumentBuilder().parse(list);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					catch (ParserConfigurationException e)
					{
						e.printStackTrace();
					}
				}
				catch (SAXException e)
				{
					_log.error("", e);
				}
				int classIndex;
				int level;
				double value;
				if (doc != null)
				{
					for (Node z = doc.getFirstChild(); z != null; z = z.getNextSibling())
					{
						for (Node n = z.getFirstChild(); n != null; n = n.getNextSibling())
						{
							if (n.getNodeName().equalsIgnoreCase("hp_table"))
							{
								classIndex = Integer.parseInt(n.getAttributes().getNamedItem("classId").getNodeValue());
								for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
								{
									String node = d.getNodeName();
									if (node.equalsIgnoreCase("set"))
									{
										level = Integer.parseInt(d.getAttributes().getNamedItem("level").getNodeValue());
										value = Double.parseDouble(d.getAttributes().getNamedItem("value").getNodeValue());
										hpTable[classIndex][level] = value;
									}
								}
							}
							if (n.getNodeName().equalsIgnoreCase("mp_table"))
							{
								classIndex = Integer.parseInt(n.getAttributes().getNamedItem("classId").getNodeValue());
								for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
								{
									String node = d.getNodeName();
									if (node.equalsIgnoreCase("set"))
									{
										level = Integer.parseInt(d.getAttributes().getNamedItem("level").getNodeValue());
										value = Double.parseDouble(d.getAttributes().getNamedItem("value").getNodeValue());
										mpTable[classIndex][level] = value;
									}
								}
							}
							if (n.getNodeName().equalsIgnoreCase("cp_table"))
							{
								classIndex = Integer.parseInt(n.getAttributes().getNamedItem("classId").getNodeValue());
								for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
								{
									String node = d.getNodeName();
									if (node.equalsIgnoreCase("set"))
									{
										level = Integer.parseInt(d.getAttributes().getNamedItem("level").getNodeValue());
										value = Double.parseDouble(d.getAttributes().getNamedItem("value").getNodeValue());
										cpTable[classIndex][level] = value;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method getHp.
	 * @param player Player
	 * @return double
	 */
	public static double getHp(final Player player)
	{
		return hpTable[player.getClassId().getId()][player.getLevel()];
	}
	
	/**
	 * Method getCp.
	 * @param player Player
	 * @return double
	 */
	public static double getCp(final Player player)
	{
		return cpTable[player.getClassId().getId()][player.getLevel()];
	}
	
	/**
	 * Method getMp.
	 * @param player Player
	 * @return double
	 */
	public static double getMp(final Player player)
	{
		return mpTable[player.getClassId().getId()][player.getLevel()];
	}
}
