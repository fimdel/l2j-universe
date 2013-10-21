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

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.entity.residence.ResidenceType;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.stats.conditions.ConditionCastleDark;
import lineage2.gameserver.stats.conditions.ConditionCastleDarkClanLeader;
import lineage2.gameserver.stats.conditions.ConditionCastleLight;
import lineage2.gameserver.stats.conditions.ConditionCastleLightClanLeader;
import lineage2.gameserver.stats.conditions.ConditionLogicAnd;
import lineage2.gameserver.stats.conditions.ConditionLogicNot;
import lineage2.gameserver.stats.conditions.ConditionLogicOr;
import lineage2.gameserver.stats.conditions.ConditionPlayerClassId;
import lineage2.gameserver.stats.conditions.ConditionPlayerInstanceZone;
import lineage2.gameserver.stats.conditions.ConditionPlayerMinMaxDamage;
import lineage2.gameserver.stats.conditions.ConditionPlayerOlympiad;
import lineage2.gameserver.stats.conditions.ConditionPlayerRace;
import lineage2.gameserver.stats.conditions.ConditionPlayerResidence;
import lineage2.gameserver.stats.conditions.ConditionSlotItemId;
import lineage2.gameserver.stats.conditions.ConditionTargetPlayable;
import lineage2.gameserver.stats.conditions.ConditionUsingItemType;
import lineage2.gameserver.stats.conditions.ConditionUsingSkill;
import lineage2.gameserver.stats.conditions.ConditionZoneType;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.stats.triggers.TriggerInfo;
import lineage2.gameserver.stats.triggers.TriggerType;
import lineage2.gameserver.templates.item.ArmorTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class StatParser<H extends AbstractHolder> extends AbstractDirParser<H>
{
	/**
	 * Constructor for StatParser.
	 * @param holder H
	 */
	protected StatParser(H holder)
	{
		super(holder);
	}
	
	/**
	 * Method parseFirstCond.
	 * @param sub Element
	 * @return Condition
	 */
	protected Condition parseFirstCond(Element sub)
	{
		List<Element> e = sub.elements();
		if (e.isEmpty())
		{
			return null;
		}
		Element element = e.get(0);
		return parseCond(element);
	}
	
	/**
	 * Method parseCond.
	 * @param element Element
	 * @return Condition
	 */
	protected Condition parseCond(Element element)
	{
		String name = element.getName();
		if (name.equalsIgnoreCase("and"))
		{
			return parseLogicAnd(element);
		}
		else if (name.equalsIgnoreCase("or"))
		{
			return parseLogicOr(element);
		}
		else if (name.equalsIgnoreCase("not"))
		{
			return parseLogicNot(element);
		}
		else if (name.equalsIgnoreCase("target"))
		{
			return parseTargetCondition(element);
		}
		else if (name.equalsIgnoreCase("player"))
		{
			return parsePlayerCondition(element);
		}
		else if (name.equalsIgnoreCase("using"))
		{
			return parseUsingCondition(element);
		}
		else if (name.equalsIgnoreCase("zone"))
		{
			return parseZoneCondition(element);
		}
		return null;
	}
	
	/**
	 * Method parseLogicAnd.
	 * @param n Element
	 * @return Condition
	 */
	protected Condition parseLogicAnd(Element n)
	{
		ConditionLogicAnd cond = new ConditionLogicAnd();
		for (Iterator<Element> iterator = n.elementIterator(); iterator.hasNext();)
		{
			Element condElement = iterator.next();
			cond.add(parseCond(condElement));
		}
		if ((cond._conditions == null) || (cond._conditions.length == 0))
		{
			error("Empty <and> condition in " + getCurrentFileName());
		}
		return cond;
	}
	
	/**
	 * Method parseLogicOr.
	 * @param n Element
	 * @return Condition
	 */
	protected Condition parseLogicOr(Element n)
	{
		ConditionLogicOr cond = new ConditionLogicOr();
		for (Iterator<Element> iterator = n.elementIterator(); iterator.hasNext();)
		{
			Element condElement = iterator.next();
			cond.add(parseCond(condElement));
		}
		if ((cond._conditions == null) || (cond._conditions.length == 0))
		{
			error("Empty <or> condition in " + getCurrentFileName());
		}
		return cond;
	}
	
	/**
	 * Method parseLogicNot.
	 * @param n Element
	 * @return Condition
	 */
	protected Condition parseLogicNot(Element n)
	{
		for (Object element : n.elements())
		{
			return new ConditionLogicNot(parseCond((Element) element));
		}
		error("Empty <not> condition in " + getCurrentFileName());
		return null;
	}
	
	/**
	 * Method parseTargetCondition.
	 * @param element Element
	 * @return Condition
	 */
	protected Condition parseTargetCondition(Element element)
	{
		Condition cond = null;
		for (Iterator<Attribute> iterator = element.attributeIterator(); iterator.hasNext();)
		{
			Attribute attribute = iterator.next();
			String name = attribute.getName();
			String value = attribute.getValue();
			if (name.equalsIgnoreCase("pvp"))
			{
				cond = joinAnd(cond, new ConditionTargetPlayable(Boolean.valueOf(value)));
			}
		}
		return cond;
	}
	
	/**
	 * Method parseZoneCondition.
	 * @param element Element
	 * @return Condition
	 */
	protected Condition parseZoneCondition(Element element)
	{
		Condition cond = null;
		for (Iterator<Attribute> iterator = element.attributeIterator(); iterator.hasNext();)
		{
			Attribute attribute = iterator.next();
			String name = attribute.getName();
			String value = attribute.getValue();
			if (name.equalsIgnoreCase("type"))
			{
				cond = joinAnd(cond, new ConditionZoneType(value));
			}
		}
		return cond;
	}
	
	/**
	 * Method parsePlayerCondition.
	 * @param element Element
	 * @return Condition
	 */
	protected Condition parsePlayerCondition(Element element)
	{
		Condition cond = null;
		for (Iterator<Attribute> iterator = element.attributeIterator(); iterator.hasNext();)
		{
			Attribute attribute = iterator.next();
			String name = attribute.getName();
			String value = attribute.getValue();
			if (name.equalsIgnoreCase("residence"))
			{
				String[] st = value.split(";");
				cond = joinAnd(cond, new ConditionPlayerResidence(Integer.parseInt(st[1]), ResidenceType.valueOf(st[0])));
			}
			else if (name.equalsIgnoreCase("classId"))
			{
				cond = joinAnd(cond, new ConditionPlayerClassId(value.split(",")));
			}
			else if (name.equalsIgnoreCase("olympiad"))
			{
				cond = joinAnd(cond, new ConditionPlayerOlympiad(Boolean.valueOf(value)));
			}
			else if (name.equalsIgnoreCase("instance_zone"))
			{
				cond = joinAnd(cond, new ConditionPlayerInstanceZone(Integer.parseInt(value)));
			}
			else if (name.equalsIgnoreCase("race"))
			{
				cond = joinAnd(cond, new ConditionPlayerRace(value));
			}
			else if (name.equalsIgnoreCase("damage"))
			{
				String[] st = value.split(";");
				cond = joinAnd(cond, new ConditionPlayerMinMaxDamage(Double.parseDouble(st[0]), Double.parseDouble(st[1])));
			}
			else if (name.equalsIgnoreCase("castleLight"))
			{
				cond = joinAnd(cond, new ConditionCastleLight());
			}
			else if (name.equalsIgnoreCase("castleLightClanLeader"))
			{
				cond = joinAnd(cond, new ConditionCastleLightClanLeader());
			}
			else if (name.equalsIgnoreCase("castleDark"))
			{
				cond = joinAnd(cond, new ConditionCastleDark());
			}
			else if (name.equalsIgnoreCase("castleDarkClanLeader"))
			{
				cond = joinAnd(cond, new ConditionCastleDarkClanLeader());
			}
		}
		return cond;
	}
	
	/**
	 * Method parseUsingCondition.
	 * @param element Element
	 * @return Condition
	 */
	protected Condition parseUsingCondition(Element element)
	{
		Condition cond = null;
		for (Iterator<Attribute> iterator = element.attributeIterator(); iterator.hasNext();)
		{
			Attribute attribute = iterator.next();
			String name = attribute.getName();
			String value = attribute.getValue();
			if (name.equalsIgnoreCase("slotitem"))
			{
				StringTokenizer st = new StringTokenizer(value, ";");
				int id = Integer.parseInt(st.nextToken().trim());
				int slot = Integer.parseInt(st.nextToken().trim());
				int enchant = 0;
				if (st.hasMoreTokens())
				{
					enchant = Integer.parseInt(st.nextToken().trim());
				}
				cond = joinAnd(cond, new ConditionSlotItemId(slot, id, enchant));
			}
			else if (name.equalsIgnoreCase("kind") || name.equalsIgnoreCase("weapon"))
			{
				long mask = 0;
				StringTokenizer st = new StringTokenizer(value, ",");
				tokens:
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					for (WeaponTemplate.WeaponType wt : WeaponTemplate.WeaponType.VALUES)
					{
						if (wt.toString().equalsIgnoreCase(item))
						{
							mask |= wt.mask();
							continue tokens;
						}
					}
					for (ArmorTemplate.ArmorType at : ArmorTemplate.ArmorType.VALUES)
					{
						if (at.toString().equalsIgnoreCase(item))
						{
							mask |= at.mask();
							continue tokens;
						}
					}
					error("Invalid item kind: \"" + item + "\" in " + getCurrentFileName());
				}
				if (mask != 0)
				{
					cond = joinAnd(cond, new ConditionUsingItemType(mask));
				}
			}
			else if (name.equalsIgnoreCase("skill"))
			{
				cond = joinAnd(cond, new ConditionUsingSkill(Integer.parseInt(value)));
			}
		}
		return cond;
	}
	
	/**
	 * Method joinAnd.
	 * @param cond Condition
	 * @param c Condition
	 * @return Condition
	 */
	protected Condition joinAnd(Condition cond, Condition c)
	{
		if (cond == null)
		{
			return c;
		}
		if (cond instanceof ConditionLogicAnd)
		{
			((ConditionLogicAnd) cond).add(c);
			return cond;
		}
		ConditionLogicAnd and = new ConditionLogicAnd();
		and.add(cond);
		and.add(c);
		return and;
	}
	
	/**
	 * Method parseFor.
	 * @param forElement Element
	 * @param template StatTemplate
	 */
	protected void parseFor(Element forElement, StatTemplate template)
	{
		for (Iterator<Element> iterator = forElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			final String elementName = element.getName();
			if (elementName.equalsIgnoreCase("add"))
			{
				attachFunc(element, template, "Add");
			}
			else if (elementName.equalsIgnoreCase("set"))
			{
				attachFunc(element, template, "Set");
			}
			else if (elementName.equalsIgnoreCase("sub"))
			{
				attachFunc(element, template, "Sub");
			}
			else if (elementName.equalsIgnoreCase("mul"))
			{
				attachFunc(element, template, "Mul");
			}
			else if (elementName.equalsIgnoreCase("div"))
			{
				attachFunc(element, template, "Div");
			}
			else if (elementName.equalsIgnoreCase("enchant"))
			{
				attachFunc(element, template, "Enchant");
			}
		}
	}
	
	/**
	 * Method parseTriggers.
	 * @param f Element
	 * @param triggerable StatTemplate
	 */
	protected void parseTriggers(Element f, StatTemplate triggerable)
	{
		for (Iterator<Element> iterator = f.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			int id = parseNumber(element.attributeValue("id")).intValue();
			int level = parseNumber(element.attributeValue("level")).intValue();
			TriggerType t = TriggerType.valueOf(element.attributeValue("type"));
			double chance = parseNumber(element.attributeValue("chance")).doubleValue();
			TriggerInfo trigger = new TriggerInfo(id, level, t, chance);
			triggerable.addTrigger(trigger);
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				Condition condition = parseFirstCond(subElement);
				if (condition != null)
				{
					trigger.addCondition(condition);
				}
			}
		}
	}
	
	/**
	 * Method attachFunc.
	 * @param n Element
	 * @param template StatTemplate
	 * @param name String
	 */
	protected void attachFunc(Element n, StatTemplate template, String name)
	{
		Stats stat = Stats.valueOfXml(n.attributeValue("stat"));
		String order = n.attributeValue("order");
		int ord = parseNumber(order).intValue();
		Condition applyCond = parseFirstCond(n);
		double val = 0;
		if (n.attributeValue("value") != null)
		{
			val = parseNumber(n.attributeValue("value")).doubleValue();
		}
		template.attachFunc(new FuncTemplate(applyCond, name, stat, ord, val));
	}
	
	/**
	 * Method parseNumber.
	 * @param value String
	 * @return Number
	 */
	protected Number parseNumber(String value)
	{
		if (value.charAt(0) == '#')
		{
			value = getTableValue(value).toString();
		}
		try
		{
			if (value.indexOf('.') == -1)
			{
				int radix = 10;
				if ((value.length() > 2) && value.substring(0, 2).equalsIgnoreCase("0x"))
				{
					value = value.substring(2);
					radix = 16;
				}
				return Integer.valueOf(value, radix);
			}
			return Double.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
	
	/**
	 * Method getTableValue.
	 * @param name String
	 * @return Object
	 */
	protected abstract Object getTableValue(String name);
}
