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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.AttributeCap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AttributeDamageResistTable
{

	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(AttributeDamageResistTable.class);

	private static ArrayList <AttributeCap> _cappedAttributeList = new ArrayList<AttributeCap>();
	
	private static Double _baseAtk;
	private static Double _baseDef;
	
	private static Integer _baseCap;
	private static Integer _overCap;
	
	static final Comparator <AttributeCap> CapOrder = new Comparator<AttributeCap>(){
		public int compare(AttributeCap a1, AttributeCap a2)
		{
			if(a2.getCap() < a1.getCap())
				return -1;
			else if(a1.getCap() == a2.getCap())
				return 0;
			else
				return 1;
		}	
	};
	
	private static AttributeDamageResistTable _instance = new AttributeDamageResistTable();
	
	/**
	 * Method getInstance.
	 * @return AttributeDamageResistTable
	 */
	public static AttributeDamageResistTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new AttributeDamageResistTable();
		}
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		_cappedAttributeList.clear();
		_instance = new AttributeDamageResistTable();
	}
	
	/**
	 * Constructor for AttributeDamageResistTable.
	 */
	private AttributeDamageResistTable()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/attribute_resistdamage.xml");
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						Integer cap;
						Double atk;
						Double def;
						
						NamedNodeMap attrs = d.getAttributes();
						Node att;
						if("attribute".equalsIgnoreCase(d.getNodeName()))
						{
							att = attrs.getNamedItem("cap");
							if(att == null)
							{
								_log.info("AttributeDamageResistTable: no cap difference has been specified. skipping");
								continue;
							}
							cap = Integer.parseInt(att.getNodeValue());
							if(cap % 10 != 0 && cap > 2000)
							{
								_log.info("AttributeDamageResistTable: the cap value is incorrect. rest of division by 10 may give 0. the value can't be up of 2000");
								continue;								
							}
							att = attrs.getNamedItem("atk");
							if(att == null)
							{
								_log.info("AttributeDamageResistTable: no atk difference has been specified. skipping");
								continue;								
							}
							atk = Double.parseDouble(att.getNodeValue());
							att = attrs.getNamedItem("def");
							if(att == null)
							{
								_log.info("AttributeDamageResistTable: no def difference has been specified. skipping");
								continue;								
							}
							def = Double.parseDouble(att.getNodeValue());
							_cappedAttributeList.add(new AttributeCap(cap,atk,def));
						}
						else if("baseattribute".equalsIgnoreCase(d.getNodeName()))
						{
							att = attrs.getNamedItem("atk");
							if(att == null)
							{
								_log.info("AttributeDamageResistTable: no base atk difference has been specified. giving a official value");
								atk = 0.1;
							}
							else
							{
								atk = Double.parseDouble(att.getNodeValue());
							}
							att = attrs.getNamedItem("def");
							if(att == null)
							{
								_log.info("AttributeDamageResistTable: no base def difference has been specified. giving a official value");
								def = 0.1;								
							}
							else
							{
								def = Double.parseDouble(att.getNodeValue());
							}
							if(atk < 1)
							{
								_baseAtk = 1D;
							}
							else
							{
								_baseAtk = ((atk+100)/100);
							}
							if(def < 1)
							{
								_baseDef = 1D;
							}
							else
							{
								_baseDef = ((def+100)/100);
							}
						}						
					}
				}
			}
			Collections.sort(_cappedAttributeList, Collections.reverseOrder(CapOrder));
			_baseCap = _cappedAttributeList.get(0).getCap();
			_overCap = _cappedAttributeList.get(_cappedAttributeList.size()-1).getCap();
			_log.info("AttributeDamageResistTable: All caps has been loaded, base cap: " + _baseCap + " OverCap: " + _overCap);			
		}
		catch (Exception e)
		{
			_log.warn("EnchantStatBonusTable: Lists could not be initialized.");
			e.printStackTrace();
		}
	}	
	public double getAttributeBonus(double difference)
	{		
		double finalDifference = 1D;
		boolean isAttackBonus = true;
		if(difference < 0)
			isAttackBonus = false;
		finalDifference = Math.abs(difference);
		if(finalDifference == 0)
		{
			return 1D;
		}
		else if(finalDifference < _baseCap)
		{
			if(isAttackBonus)
			{
				return _baseAtk;
			}
			else
			{
				return _baseDef;
			}
		}
		else if(finalDifference >= _overCap)
		{
			if(isAttackBonus)
			{
				return ((_cappedAttributeList.get(_cappedAttributeList.size()-1).getAttackBonus()+100)/100);
			}
			else
			{
				return ((_cappedAttributeList.get(_cappedAttributeList.size()-1).getDefenseBonus()+100)/100);
			}
		}
		else
		{
			for(int i = 0; i < _cappedAttributeList.size();i++)
			{			
				if(finalDifference >= _cappedAttributeList.get(i).getCap() && finalDifference < _cappedAttributeList.get(i+1).getCap())
				{
					if(isAttackBonus)
					{
						return ((_cappedAttributeList.get(i).getAttackBonus()+100)/100);
					}
					else
					{
						return ((_cappedAttributeList.get(i).getDefenseBonus()+100)/100);
					}
				}
			}
		}
		return 1D;
	}
}
