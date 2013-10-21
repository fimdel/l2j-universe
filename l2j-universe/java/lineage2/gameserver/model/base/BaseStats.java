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
package lineage2.gameserver.model.base;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum BaseStats
{
	/**
	 * Field STR.
	 */
	STR
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getSTR();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : STRbonus[actor.getSTR()];
		}
		
		@Override
		public final double calcChanceMod(Creature actor)
		{
			return Math.min(2. - Math.sqrt(calcBonus(actor)), 1.);
		}
	},
	/**
	 * Field INT.
	 */
	INT
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getINT();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : INTbonus[actor.getINT()];
		}
	},
	/**
	 * Field DEX.
	 */
	DEX
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getDEX();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : DEXbonus[actor.getDEX()];
		}
	},
	/**
	 * Field WIT.
	 */
	WIT
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getWIT();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : WITbonus[actor.getWIT()];
		}
	},
	/**
	 * Field CON.
	 */
	CON
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getCON();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : CONbonus[actor.getCON()];
		}
	},
	/**
	 * Field MEN.
	 */
	MEN
	{
		@Override
		public final int getStat(Creature actor)
		{
			return actor == null ? 1 : actor.getMEN();
		}
		
		@Override
		public final double calcBonus(Creature actor)
		{
			return actor == null ? 1. : MENbonus[actor.getMEN()];
		}
	},
	/**
	 * Field NONE.
	 */
	NONE;
	/**
	 * Field VALUES.
	 */
	public static final BaseStats[] VALUES = values();
	/**
	 * Field _log.
	 */
	protected static final Logger _log = LoggerFactory.getLogger(BaseStats.class);
	/**
	 * Field MAX_STAT_VALUE. (value is 201)
	 */
	private static final int MAX_STAT_VALUE = 201;
	/**
	 * Field STRbonus.
	 */
	static final double[] STRbonus = new double[MAX_STAT_VALUE];
	/**
	 * Field INTbonus.
	 */
	static final double[] INTbonus = new double[MAX_STAT_VALUE];
	/**
	 * Field DEXbonus.
	 */
	static final double[] DEXbonus = new double[MAX_STAT_VALUE];
	/**
	 * Field WITbonus.
	 */
	static final double[] WITbonus = new double[MAX_STAT_VALUE];
	/**
	 * Field CONbonus.
	 */
	static final double[] CONbonus = new double[MAX_STAT_VALUE];
	/**
	 * Field MENbonus.
	 */
	static final double[] MENbonus = new double[MAX_STAT_VALUE];
	
	/**
	 * Method getStat.
	 * @param actor Creature
	 * @return int
	 */
	public int getStat(Creature actor)
	{
		return 1;
	}
	
	/**
	 * Method calcBonus.
	 * @param actor Creature
	 * @return double
	 */
	public double calcBonus(Creature actor)
	{
		return 1.;
	}
	
	/**
	 * Method calcChanceMod.
	 * @param actor Creature
	 * @return double
	 */
	public double calcChanceMod(Creature actor)
	{
		return 2. - Math.sqrt(calcBonus(actor));
	}
	
	/**
	 * Method valueOfXml.
	 * @param name String
	 * @return BaseStats
	 */
	public static final BaseStats valueOfXml(String name)
	{
		name = name.intern();
		for (BaseStats s : VALUES)
		{
			if (s.toString().equalsIgnoreCase(name))
			{
				if (s == NONE)
				{
					return null;
				}
				return s;
			}
		}
		throw new NoSuchElementException("Unknown name '" + name + "' for enum BaseStats");
	}
	
	static
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		File file = new File(Config.DATAPACK_ROOT, "data/xml/asc/model/player/BaseStatBonusData.xml");
		Document doc = null;
		try
		{
			doc = factory.newDocumentBuilder().parse(file);
		}
		catch (SAXException e)
		{
			_log.error("", e);
		}
		catch (IOException e)
		{
			_log.error("", e);
		}
		catch (ParserConfigurationException e)
		{
			_log.error("", e);
		}
		int i;
		double val;
		if (doc != null)
		{
			for (Node z = doc.getFirstChild(); z != null; z = z.getNextSibling())
			{
				for (Node n = z.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if (n.getNodeName().equalsIgnoreCase("STR"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								STRbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
					if (n.getNodeName().equalsIgnoreCase("INT"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								INTbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
					if (n.getNodeName().equalsIgnoreCase("CON"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								CONbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
					if (n.getNodeName().equalsIgnoreCase("MEN"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								MENbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
					if (n.getNodeName().equalsIgnoreCase("DEX"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								DEXbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
					if (n.getNodeName().equalsIgnoreCase("WIT"))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							String node = d.getNodeName();
							if (node.equalsIgnoreCase("stat"))
							{
								i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
								val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
								WITbonus[i + 1] = (1000 + val) / 1000;
							}
						}
					}
				}
			}
		}
	}
}
