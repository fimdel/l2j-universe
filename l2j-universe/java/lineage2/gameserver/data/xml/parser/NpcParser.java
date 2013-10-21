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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.model.reward.RewardGroup;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.AbsorbInfo;
import lineage2.gameserver.templates.npc.Faction;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.npc.RandomActions;
import lineage2.gameserver.templates.npc.WalkerRoute;
import lineage2.gameserver.templates.npc.WalkerRoutePoint;
import lineage2.gameserver.templates.npc.WalkerRouteType;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NpcParser extends AbstractDirParser<NpcHolder>
{
	/**
	 * Field _instance.
	 */
	private static final NpcParser _instance = new NpcParser();
	
	/**
	 * Method getInstance.
	 * @return NpcParser
	 */
	public static NpcParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for NpcParser.
	 */
	private NpcParser()
	{
		super(NpcHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/npc/");
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
		return "npc.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement org.dom4j.Element
	 * @throws Exception
	 */
	@Override
	protected void readData(org.dom4j.Element rootElement) throws Exception
	{
		for (Iterator<org.dom4j.Element> npcIterator = rootElement.elementIterator(); npcIterator.hasNext();)
		{
			org.dom4j.Element npcElement = npcIterator.next();
			int npcId = Integer.parseInt(npcElement.attributeValue("id"));
			int templateId = npcElement.attributeValue("template_id") == null ? 0 : Integer.parseInt(npcElement.attributeValue("template_id"));
			String name = npcElement.attributeValue("name");
			String title = npcElement.attributeValue("title");
			StatsSet set = new StatsSet();
			set.set("npcId", npcId);
			set.set("displayId", templateId);
			set.set("name", name);
			set.set("title", title);
			set.set("baseCpReg", 0);
			set.set("baseCpMax", 0);
			for (Iterator<org.dom4j.Element> firstIterator = npcElement.elementIterator(); firstIterator.hasNext();)
			{
				org.dom4j.Element firstElement = firstIterator.next();
				if (firstElement.getName().equalsIgnoreCase("set"))
				{
					set.set(firstElement.attributeValue("name"), firstElement.attributeValue("value"));
				}
				else if (firstElement.getName().equalsIgnoreCase("equip"))
				{
					for (Iterator<org.dom4j.Element> eIterator = firstElement.elementIterator(); eIterator.hasNext();)
					{
						org.dom4j.Element eElement = eIterator.next();
						set.set(eElement.getName(), eElement.attributeValue("item_id"));
					}
				}
				else if (firstElement.getName().equalsIgnoreCase("ai_params"))
				{
					StatsSet ai = new StatsSet();
					for (Iterator<org.dom4j.Element> eIterator = firstElement.elementIterator(); eIterator.hasNext();)
					{
						org.dom4j.Element eElement = eIterator.next();
						ai.set(eElement.attributeValue("name"), eElement.attributeValue("value"));
					}
					set.set("aiParams", ai);
				}
				else if (firstElement.getName().equalsIgnoreCase("attributes"))
				{
					int[] attributeAttack = new int[6];
					int[] attributeDefence = new int[6];
					for (Iterator<org.dom4j.Element> eIterator = firstElement.elementIterator(); eIterator.hasNext();)
					{
						org.dom4j.Element eElement = eIterator.next();
						Element element;
						if (eElement.getName().equalsIgnoreCase("defence"))
						{
							element = Element.getElementByName(eElement.attributeValue("attribute"));
							attributeDefence[element.getId()] = Integer.parseInt(eElement.attributeValue("value"));
						}
						else if (eElement.getName().equalsIgnoreCase("attack"))
						{
							element = Element.getElementByName(eElement.attributeValue("attribute"));
							attributeAttack[element.getId()] = Integer.parseInt(eElement.attributeValue("value"));
						}
					}
					set.set("baseAttributeAttack", attributeAttack);
					set.set("baseAttributeDefence", attributeDefence);
				}
			}
			NpcTemplate template = new NpcTemplate(set);
			for (Iterator<org.dom4j.Element> secondIterator = npcElement.elementIterator(); secondIterator.hasNext();)
			{
				org.dom4j.Element secondElement = secondIterator.next();
				String nodeName = secondElement.getName();
				if (nodeName.equalsIgnoreCase("faction"))
				{
					String factionId = secondElement.attributeValue("name");
					Faction faction = new Faction(factionId);
					int factionRange = Integer.parseInt(secondElement.attributeValue("range"));
					faction.setRange(factionRange);
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						final org.dom4j.Element nextElement = nextIterator.next();
						int ignoreId = Integer.parseInt(nextElement.attributeValue("npc_id"));
						faction.addIgnoreNpcId(ignoreId);
					}
					template.setFaction(faction);
				}
				else if (nodeName.equalsIgnoreCase("rewardlist"))
				{
					RewardType type = RewardType.valueOf(secondElement.attributeValue("type"));
					boolean autoLoot = (secondElement.attributeValue("auto_loot") != null) && Boolean.parseBoolean(secondElement.attributeValue("auto_loot"));
					RewardList list = new RewardList(type, autoLoot);
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						final org.dom4j.Element nextElement = nextIterator.next();
						final String nextName = nextElement.getName();
						if (nextName.equalsIgnoreCase("group"))
						{
							double enterChance = nextElement.attributeValue("chance") == null ? RewardList.MAX_CHANCE : Double.parseDouble(nextElement.attributeValue("chance")) * 10000;
							RewardGroup group = (type == RewardType.SWEEP) || (type == RewardType.NOT_RATED_NOT_GROUPED) ? null : new RewardGroup(enterChance);
							for (Iterator<org.dom4j.Element> rewardIterator = nextElement.elementIterator(); rewardIterator.hasNext();)
							{
								org.dom4j.Element rewardElement = rewardIterator.next();
								RewardData data = parseReward(rewardElement);
								if ((type == RewardType.SWEEP) || (type == RewardType.NOT_RATED_NOT_GROUPED))
								{
									warn("Can't load rewardlist from group: " + npcId + "; type: " + type);
								}
								else
								{
									group.addData(data);
								}
							}
							if (group != null)
							{
								list.add(group);
							}
						}
						else if (nextName.equalsIgnoreCase("reward"))
						{
							if ((type != RewardType.SWEEP) && (type != RewardType.NOT_RATED_NOT_GROUPED))
							{
								warn("Reward can't be without group(and not grouped): " + npcId + "; type: " + type);
								continue;
							}
							RewardData data = parseReward(nextElement);
							RewardGroup g = new RewardGroup(RewardList.MAX_CHANCE);
							g.addData(data);
							list.add(g);
						}
					}
					if ((type == RewardType.RATED_GROUPED) || (type == RewardType.NOT_RATED_GROUPED))
					{
						if (!list.validate())
						{
							warn("Problems with rewardlist for npc: " + npcId + "; type: " + type);
						}
					}
					template.putRewardList(type, list);
				}
				else if (nodeName.equalsIgnoreCase("skills"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));
						if (id == 4416)
						{
							template.setRace(level);
						}
						Skill skill = SkillTable.getInstance().getInfo(id, level);
						if (skill == null)
						{
							continue;
						}
						template.addSkill(skill);
					}
				}
				else if (nodeName.equalsIgnoreCase("minions"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("npc_id"));
						int count = Integer.parseInt(nextElement.attributeValue("count"));
						template.addMinion(new MinionData(id, count));
					}
				}
				else if (nodeName.equalsIgnoreCase("teach_classes"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						template.addTeachInfo(ClassId.VALUES[id]);
					}
				}
				else if (nodeName.equalsIgnoreCase("absorblist"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = secondElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int chance = Integer.parseInt(nextElement.attributeValue("chance"));
						int cursedChance = nextElement.attributeValue("cursed_chance") == null ? 0 : Integer.parseInt(nextElement.attributeValue("cursed_chance"));
						int minLevel = Integer.parseInt(nextElement.attributeValue("min_level"));
						int maxLevel = Integer.parseInt(nextElement.attributeValue("max_level"));
						boolean skill = (nextElement.attributeValue("skill") != null) && Boolean.parseBoolean(nextElement.attributeValue("skill"));
						AbsorbInfo.AbsorbType absorbType = AbsorbInfo.AbsorbType.valueOf(nextElement.attributeValue("type"));
						template.addAbsorbInfo(new AbsorbInfo(skill, absorbType, chance, cursedChance, minLevel, maxLevel));
					}
				}
				else if (nodeName.equalsIgnoreCase("teleportlist"))
				{
					for (Iterator<org.dom4j.Element> sublistIterator = secondElement.elementIterator(); sublistIterator.hasNext();)
					{
						org.dom4j.Element subListElement = sublistIterator.next();
						int id = Integer.parseInt(subListElement.attributeValue("id"));
						List<TeleportLocation> list = new ArrayList<>();
						for (Iterator<org.dom4j.Element> targetIterator = subListElement.elementIterator(); targetIterator.hasNext();)
						{
							org.dom4j.Element targetElement = targetIterator.next();
							int itemId = Integer.parseInt(targetElement.attributeValue("item_id", "57"));
							long price = Integer.parseInt(targetElement.attributeValue("price"));
							int npcStringId = Integer.parseInt(targetElement.attributeValue("name"));
							int castleId = Integer.parseInt(targetElement.attributeValue("castle_id", "0"));
							TeleportLocation loc = new TeleportLocation(itemId, price, npcStringId, castleId);
							loc.set(Location.parseLoc(targetElement.attributeValue("loc")));
							list.add(loc);
						}
						template.addTeleportList(id, list.toArray(new TeleportLocation[list.size()]));
					}
				}
				else if (nodeName.equalsIgnoreCase("walker_route"))
				{
					int id = Integer.parseInt(secondElement.attributeValue("id"));
					WalkerRouteType type = secondElement.attributeValue("type") == null ? WalkerRouteType.LENGTH : WalkerRouteType.valueOf(secondElement.attributeValue("type").toUpperCase());
					WalkerRoute walkerRoute = new WalkerRoute(id, type);
					for (Iterator<?> nextIterator = secondElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = (org.dom4j.Element)nextIterator.next();
						Location loc = Location.parse(nextElement);
						NpcString phrase = nextElement.attributeValue("phrase_id") == null ? null : NpcString.valueOf(Integer.parseInt(nextElement.attributeValue("phrase_id").toUpperCase()));
						int socialActionId = nextElement.attributeValue("social_action_id") == null ? -1 : Integer.parseInt(nextElement.attributeValue("social_action_id"));
						int delay = nextElement.attributeValue("delay") == null ? 0 : Integer.parseInt(nextElement.attributeValue("delay"));
						boolean running = nextElement.attributeValue("running") == null ? false : Boolean.parseBoolean(nextElement.attributeValue("running"));
						walkerRoute.addPoint(new WalkerRoutePoint(loc, phrase, socialActionId, delay, running));
					}
					template.addWalkerRoute(walkerRoute);
				}
				else if (nodeName.equalsIgnoreCase("random_actions"))
				{
					boolean random_order = secondElement.attributeValue("random_order") == null ? false : Boolean.parseBoolean(secondElement.attributeValue("random_order"));
					RandomActions randomActions = new RandomActions(random_order);
					for (Iterator<?> nextIterator = secondElement.elementIterator(); nextIterator.hasNext(); )
					{
						org.dom4j.Element nextElement = (org.dom4j.Element)nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						NpcString phrase = nextElement.attributeValue("phrase_id") == null ? null : NpcString.valueOf(Integer.parseInt(nextElement.attributeValue("phrase_id")));
						int socialActionId = nextElement.attributeValue("social_action_id") == null ? -1 : Integer.parseInt(nextElement.attributeValue("social_action_id"));
						int delay = nextElement.attributeValue("delay") == null ? 0 : Integer.parseInt(nextElement.attributeValue("delay"));
						randomActions.addAction(new RandomActions.Action(id, phrase, socialActionId, delay));
					}
					template.setRandomActions(randomActions);
				}
			}
			getHolder().addTemplate(template);
		}
	}
	
	/**
	 * Method parseReward.
	 * @param rewardElement org.dom4j.Element
	 * @return RewardData
	 */
	private RewardData parseReward(org.dom4j.Element rewardElement)
	{
		int itemId = Integer.parseInt(rewardElement.attributeValue("item_id"));
		int min = Integer.parseInt(rewardElement.attributeValue("min"));
		int max = Integer.parseInt(rewardElement.attributeValue("max"));
		int chance = (int) (Double.parseDouble(rewardElement.attributeValue("chance")) * 10000);
		RewardData data = new RewardData(itemId);
		if (data.getItem().isCommonItem())
		{
			data.setChance(chance * Config.RATE_DROP_COMMON_ITEMS);
		}
		else
		{
			data.setChance(chance);
		}
		data.setMinDrop(min);
		data.setMaxDrop(max);
		return data;
	}
}
