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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.entity.residence.ResidenceFunction;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.support.MerchantGuard;
import lineage2.gameserver.utils.Location;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ResidenceParser extends AbstractDirParser<ResidenceHolder>
{
	/**
	 * Field _instance.
	 */
	private static ResidenceParser _instance = new ResidenceParser();
	
	/**
	 * Method getInstance.
	 * @return ResidenceParser
	 */
	public static ResidenceParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ResidenceParser.
	 */
	private ResidenceParser()
	{
		super(ResidenceHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/residences/");
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
		return "residence.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		String impl = rootElement.attributeValue("impl");
		Class<?> clazz = null;
		StatsSet set = new StatsSet();
		for (Iterator<Attribute> iterator = rootElement.attributeIterator(); iterator.hasNext();)
		{
			Attribute element = iterator.next();
			set.set(element.getName(), element.getValue());
		}
		Residence residence = null;
		try
		{
			clazz = Class.forName("lineage2.gameserver.model.entity.residence." + impl);
			Constructor<?> constructor = clazz.getConstructor(StatsSet.class);
			residence = (Residence) constructor.newInstance(set);
			getHolder().addResidence(residence);
		}
		catch (Exception e)
		{
			error("fail to init: " + getCurrentFileName(), e);
			return;
		}
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			String nodeName = element.getName();
			int level = element.attributeValue("level") == null ? 0 : Integer.valueOf(element.attributeValue("level"));
			int lease = (int) ((element.attributeValue("lease") == null ? 0 : Integer.valueOf(element.attributeValue("lease"))) * Config.RESIDENCE_LEASE_FUNC_MULTIPLIER);
			int npcId = element.attributeValue("npcId") == null ? 0 : Integer.valueOf(element.attributeValue("npcId"));
			int listId = element.attributeValue("listId") == null ? 0 : Integer.valueOf(element.attributeValue("listId"));
			ResidenceFunction function = null;
			if (nodeName.equalsIgnoreCase("teleport"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.TELEPORT);
				List<TeleportLocation> targets = new ArrayList<>();
				for (Iterator<Element> it2 = element.elementIterator(); it2.hasNext();)
				{
					Element teleportElement = it2.next();
					if ("target".equalsIgnoreCase(teleportElement.getName()))
					{
						int npcStringId = Integer.parseInt(teleportElement.attributeValue("name"));
						long price = Long.parseLong(teleportElement.attributeValue("price"));
						int itemId = teleportElement.attributeValue("item") == null ? ItemTemplate.ITEM_ID_ADENA : Integer.parseInt(teleportElement.attributeValue("item"));
						TeleportLocation loc = new TeleportLocation(itemId, price, npcStringId, 0);
						loc.set(Location.parseLoc(teleportElement.attributeValue("loc")));
						targets.add(loc);
					}
				}
				function.addTeleports(level, targets.toArray(new TeleportLocation[targets.size()]));
			}
			else if (nodeName.equalsIgnoreCase("support"))
			{
				if ((level > 9) && !Config.ALT_CH_ALLOW_1H_BUFFS)
				{
					continue;
				}
				function = checkAndGetFunction(residence, ResidenceFunction.SUPPORT);
				function.addBuffs(level);
			}
			else if (nodeName.equalsIgnoreCase("item_create"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.ITEM_CREATE);
				function.addBuylist(level, new int[]
				{
					npcId,
					listId
				});
			}
			else if (nodeName.equalsIgnoreCase("curtain"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.CURTAIN);
			}
			else if (nodeName.equalsIgnoreCase("platform"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.PLATFORM);
			}
			else if (nodeName.equalsIgnoreCase("restore_exp"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_EXP);
			}
			else if (nodeName.equalsIgnoreCase("restore_hp"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_HP);
			}
			else if (nodeName.equalsIgnoreCase("restore_mp"))
			{
				function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_MP);
			}
			else if (nodeName.equalsIgnoreCase("skills"))
			{
				for (Iterator<Element> nextIterator = element.elementIterator(); nextIterator.hasNext();)
				{
					Element nextElement = nextIterator.next();
					int id2 = Integer.parseInt(nextElement.attributeValue("id"));
					int level2 = Integer.parseInt(nextElement.attributeValue("level"));
					Skill skill = SkillTable.getInstance().getInfo(id2, level2);
					if (skill != null)
					{
						residence.addSkill(skill);
					}
				}
			}
			else if (nodeName.equalsIgnoreCase("banish_points"))
			{
				for (Iterator<Element> banishPointsIterator = element.elementIterator(); banishPointsIterator.hasNext();)
				{
					Location loc = Location.parse(banishPointsIterator.next());
					residence.addBanishPoint(loc);
				}
			}
			else if (nodeName.equalsIgnoreCase("owner_restart_points"))
			{
				for (Iterator<Element> ownerRestartPointsIterator = element.elementIterator(); ownerRestartPointsIterator.hasNext();)
				{
					Location loc = Location.parse(ownerRestartPointsIterator.next());
					residence.addOwnerRestartPoint(loc);
				}
			}
			else if (nodeName.equalsIgnoreCase("other_restart_points"))
			{
				for (Iterator<Element> otherRestartPointsIterator = element.elementIterator(); otherRestartPointsIterator.hasNext();)
				{
					Location loc = Location.parse(otherRestartPointsIterator.next());
					residence.addOtherRestartPoint(loc);
				}
			}
			else if (nodeName.equalsIgnoreCase("chaos_restart_points"))
			{
				for (Iterator<Element> chaosRestartPointsIterator = element.elementIterator(); chaosRestartPointsIterator.hasNext();)
				{
					Location loc = Location.parse(chaosRestartPointsIterator.next());
					residence.addChaosRestartPoint(loc);
				}
			}
			else if (nodeName.equalsIgnoreCase("related_fortresses"))
			{
				for (Iterator<Element> subElementIterator = element.elementIterator(); subElementIterator.hasNext();)
				{
					Element subElement = subElementIterator.next();
					if (subElement.getName().equalsIgnoreCase("domain"))
					{
						((Castle) residence).addRelatedFortress(Fortress.DOMAIN, Integer.parseInt(subElement.attributeValue("fortress")));
					}
					else if (subElement.getName().equalsIgnoreCase("boundary"))
					{
						((Castle) residence).addRelatedFortress(Fortress.BOUNDARY, Integer.parseInt(subElement.attributeValue("fortress")));
					}
				}
			}
			else if (nodeName.equalsIgnoreCase("merchant_guards"))
			{
				for (Iterator<Element> subElementIterator = element.elementIterator(); subElementIterator.hasNext();)
				{
					Element subElement = subElementIterator.next();
					int itemId = Integer.parseInt(subElement.attributeValue("item_id"));
					int npcId2 = Integer.parseInt(subElement.attributeValue("npc_id"));
					int maxGuard = Integer.parseInt(subElement.attributeValue("max"));
					IntSet intSet = new HashIntSet(3);
					String[] ssq = subElement.attributeValue("ssq").split(";");
					for (String q : ssq)
					{
						if (q.equalsIgnoreCase("cabal_null"))
						{
							intSet.add(0);
						}
					}
					((Castle) residence).addMerchantGuard(new MerchantGuard(itemId, npcId2, maxGuard, intSet));
				}
			}
			if (function != null)
			{
				function.addLease(level, lease);
			}
		}
	}
	
	/**
	 * Method checkAndGetFunction.
	 * @param residence Residence
	 * @param type int
	 * @return ResidenceFunction
	 */
	private ResidenceFunction checkAndGetFunction(Residence residence, int type)
	{
		ResidenceFunction function = residence.getFunction(type);
		if (function == null)
		{
			function = new ResidenceFunction(residence.getId(), type);
			residence.addFunction(function);
		}
		return function;
	}
}
