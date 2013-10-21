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

import lineage2.commons.data.xml.DocumentParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.model.reward.RewardGroup;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DropListDataParser extends DocumentParser
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(DropListDataParser.class);
	/**
	 * Field _instance.
	 */
	private static DropListDataParser _instance = new DropListDataParser();
	/**
	 * Field _dropsParsed.
	 */
	private int _dropsParsed;
	/**
	 * Field _spoilsParsed.
	 */
	private int _spoilsParsed;
	
	/**
	 * Method getInstance.
	 * @return DropListDataParser
	 */
	public static final DropListDataParser getInstance()
	{
		if (_instance == null)
		{
			_instance = new DropListDataParser();
		}
		return _instance;
	}
	
	/**
	 * Method load.
	 */
	@Override
	public void load()
	{
		_dropsParsed = (_spoilsParsed = 0);
		parseDirectory(Config.DATAPACK_ROOT + "/data/xml/stats/npc/droplist/");
		_log.info("Loaded " + _dropsParsed + " drops & " + _spoilsParsed + " spoils.");
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * Method parseDocument.
	 */
	@Override
	protected void parseDocument()
	{
		for (Node globalNode = getCurrentDocument().getFirstChild(); globalNode != null; globalNode = globalNode.getNextSibling())
		{
			if (!"list".equalsIgnoreCase(globalNode.getNodeName()))
			{
				continue;
			}
			for (Node npcNode = globalNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
			{
				if (!"npc".equalsIgnoreCase(npcNode.getNodeName()))
				{
					continue;
				}
				NamedNodeMap attrs = npcNode.getAttributes();
				RewardList list = null;
				RewardType type = null;
				int npcId = parseInt(attrs, "id");
				NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
				if (template == null)
				{
					_log.warn("Omitted NPC ID: " + npcId + " - NPC template does not exists!");
				}
				else
				{
					for (Node dropNode = npcNode.getFirstChild(); dropNode != null; dropNode = dropNode.getNextSibling())
					{
						if ("droplist".equalsIgnoreCase(dropNode.getNodeName()))
						{
							type = RewardType.RATED_GROUPED;
							list = new RewardList(type, false);
							for (Node catNode = dropNode.getFirstChild(); catNode != null; catNode = catNode.getNextSibling())
							{
								if (!"category".equalsIgnoreCase(catNode.getNodeName()))
								{
									continue;
								}
								attrs = catNode.getAttributes();
								int chance = (int) (parseDouble(attrs, "chance") * 10000.0D);
								RewardGroup group = new RewardGroup(chance);
								for (Node itemNode = catNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling())
								{
									if (!"item".equalsIgnoreCase(itemNode.getNodeName()))
									{
										continue;
									}
									_dropsParsed += 1;
									attrs = itemNode.getAttributes();
									RewardData data = parseReward(attrs);
									group.addData(data);
								}
								list.add(group);
							}
						}
						else
						{
							if (!"spoillist".equalsIgnoreCase(dropNode.getNodeName()))
							{
								continue;
							}
							RewardGroup g = new RewardGroup(RewardList.MAX_CHANCE);
							type = RewardType.SWEEP;
							list = new RewardList(type, false);
							for (Node itemNode = dropNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling())
							{
								if (!"item".equalsIgnoreCase(itemNode.getNodeName()))
								{
									continue;
								}
								_spoilsParsed += 1;
								attrs = itemNode.getAttributes();
								RewardData data = parseReward(attrs);
								g.addData(data);
							}
							list.add(g);
						}
						template.putRewardList(type, list);
					}
				}
			}
		}
	}
	
	/**
	 * Method parseReward.
	 * @param attrs NamedNodeMap
	 * @return RewardData
	 */
	private RewardData parseReward(NamedNodeMap attrs)
	{
		int itemId = parseInt(attrs, "id");
		int min = parseInt(attrs, "min");
		int max = parseInt(attrs, "max");
		double chance = parseDouble(attrs, "chance") * 10000.0D;
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
