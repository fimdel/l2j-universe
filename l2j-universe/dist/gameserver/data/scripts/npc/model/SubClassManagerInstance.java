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
package npc.model;

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.actor.instances.player.SubClassInfo;
import lineage2.gameserver.model.actor.instances.player.SubClassList;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.DualClassTable;
import lineage2.gameserver.tables.SubClassTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.CertificationFunctions;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;

/**
 */
public final class SubClassManagerInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is 1)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field CERTIFICATE_ID. (value is 30433)
	 */
	private static final int CERTIFICATE_ID = 30433;
	
	private static TIntIntHashMap _DESTINYCHANGECLASSES = new TIntIntHashMap(8);
		
	private static Map <Integer, Double> _REAWAKENINGCOST = new HashMap<Integer, Double>();
			
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(SubClassManagerInstance.class);
		
	/**
	 * Constructor for SubClassManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SubClassManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_DESTINYCHANGECLASSES.clear();
		_REAWAKENINGCOST.clear();
		_DESTINYCHANGECLASSES.put(139,90);
		_DESTINYCHANGECLASSES.put(140,88);
		_DESTINYCHANGECLASSES.put(141,93);
		_DESTINYCHANGECLASSES.put(142,92);
		_DESTINYCHANGECLASSES.put(143,94);
		_DESTINYCHANGECLASSES.put(144,98);
		_DESTINYCHANGECLASSES.put(145,96);
		_DESTINYCHANGECLASSES.put(146,97);
		_REAWAKENINGCOST.put(85, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[0]);
		_REAWAKENINGCOST.put(86, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[1]);
		_REAWAKENINGCOST.put(87, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[2]);
		_REAWAKENINGCOST.put(88, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[3]);
		_REAWAKENINGCOST.put(89, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[4]);
		_REAWAKENINGCOST.put(90, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[5]);
		_REAWAKENINGCOST.put(91, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[6]);
		_REAWAKENINGCOST.put(92, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[7]);
		_REAWAKENINGCOST.put(93, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[8]);
		_REAWAKENINGCOST.put(94, Config.ALT_GAME_DUALCLASS_REAWAKENING_COST[9]);
		_REAWAKENINGCOST = sortByLevels(_REAWAKENINGCOST); 
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		StringTokenizer st = new StringTokenizer(command, "_");
		String cmd = st.nextToken();
		if (cmd.equalsIgnoreCase("subclass"))
		{
			if (player.getSummonList().size() > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_servitor.htm");
				return;
			}
			if (player.getTransformation() != 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_transform.htm");
				return;
			}
			if ((player.getWeightPenalty() >= 3) || ((player.getInventoryLimit() * 0.8) < player.getInventory().getSize()))
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_weight.htm");
				return;
			}
			if (player.getLevel() < 40)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_level.htm");
				return;
			}
			String cmd2 = st.nextToken();
			if (cmd2.equalsIgnoreCase("add"))
			{
				if (player.getSubClassList().size() >= SubClassList.MAX_SUB_COUNT)
				{
					showChatWindow(player, "default/" + getNpcId() + "-add_no_limit.htm");
					return;
				}
				Collection<SubClass> subClasses = player.getSubClassList().values();
				for (SubClass subClass : subClasses)
				{
					if (subClass.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS)
					{
						showChatWindow(player, "default/" + getNpcId() + "-add_no_level.htm", "<?LEVEL?>", Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS);
						return;
					}
				}
				if (!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					Set<ClassId> availSubClasses = SubClassInfo.getAvailableSubClasses(player, null, null, true);
					for (ClassId subClsId : availSubClasses)
					{						
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_add_" + subClsId.getId() + "\">" + HtmlUtils.htmlClassName(subClsId.getId())  + "/" + HtmlUtils.htmlClassName(getChildClass(subClsId.getId())) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_list.htm", "<?ADD_SUB_LIST?>", availSubList.toString());
					return;
				}
				int addSubClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					String addSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_add_" + addSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(addSubClassId) + "/" + HtmlUtils.htmlClassName(getChildClass(addSubClassId)) + "</a>";
					showChatWindow(player, "default/" + getNpcId() + "-add_confirm.htm", "<?ADD_SUB_CONFIRM?>", addSubConfirm);
					return;
				}
				String cmd3 = st.nextToken();
				if (cmd3.equalsIgnoreCase("confirm"))
				{
					if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player))
					{
						player.sendPacket(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD);
						return;
					}
					if (player.addSubClass(addSubClassId, true, 0, 0, false, 0))
					{
						player.rewardSkills(false,true);
						player.sendSkillList();
						player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
						showChatWindow(player, "default/" + getNpcId() + "-add_success.htm");
						return;
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_error.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("change"))
			{
				if (!player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_quest.htm");
					return;
				}
				if (ItemFunctions.getItemCount(player, CERTIFICATE_ID) == 0)
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_certificate.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("cancel"))
			{
				if (!player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "default/" + getNpcId() + "-cancel_no_subs.htm");
					return;
				}
				if (!st.hasMoreTokens())
				{
					StringBuilder mySubList = new StringBuilder();
					Collection<SubClass> subClasses = player.getSubClassList().values();
					for (SubClass sub : subClasses)
					{
						if (sub == null)
						{
							continue;
						}
						if (sub.isBase())
						{
							continue;
						}
						if (sub.isDouble())
						{
							continue;
						}
						int classId = sub.getClassId();
						mySubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + classId + "\">" + HtmlUtils.htmlClassName(classId) + "/" + HtmlUtils.htmlClassName(getChildClass(classId)) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-cancel_list.htm", "<?CANCEL_SUB_LIST?>", mySubList.toString());
					return;
				}
				int cancelClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, cancelClassId);
					for (int subClsId : availSubClasses)
					{
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + subClsId + "\">" + HtmlUtils.htmlClassName(subClsId) + "/" + HtmlUtils.htmlClassName(getChildClass(subClsId)) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-cancel_change_list.htm", "<?CANCEL_CHANGE_SUB_LIST?>", availSubList.toString());
					return;
				}
				int newSubClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					String newSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + newSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(newSubClassId) + "/" + HtmlUtils.htmlClassName(getChildClass(newSubClassId)) +"</a>";
					showChatWindow(player, "default/" + getNpcId() + "-cancel_confirm.htm", "<?CANCEL_SUB_CONFIRM?>", newSubConfirm);
					return;
				}
				String cmd3 = st.nextToken();
				if (cmd3.equalsIgnoreCase("confirm"))
				{
					if (player.modifySubClass(cancelClassId, newSubClassId,false))
					{
						player.rewardSkills(false,true);
						player.sendSkillList();
						player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
						showChatWindow(player, "default/" + getNpcId() + "-add_success.htm");
						return;
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_error.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("reawakendualclass"))
			{
				if (!player.getActiveSubClass().isDouble() || !player.isAwaking() || !(player.getLevel() > 84))
				{
					showChatWindow(player, "default/" + getNpcId() + "-reawaken_nodual.htm");
					return;
				}
				if (!st.hasMoreTokens())
				{
					StringBuilder costList = new StringBuilder();
					for(Iterator <Entry<Integer,Double>> i = _REAWAKENINGCOST.entrySet().iterator(); i.hasNext();)
					{
						Map.Entry<Integer,Double> e = (Map.Entry<Integer, Double>)i.next();
						costList.append("<tr><td><center><font color=\"LEVEL\">"+ e.getKey() + (e.getKey() == 94 ? "+" : " ") + "</font></center></td><td><center><font color=\"LEVEL\">" + e.getValue() + " Million</font></center></td></tr>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-reawaken_cost.htm","<?COST?>", costList.toString());
					return;
				}
				st.nextToken();
				if (!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					int[] availDualAwakenClasses = DualClassTable.getInstance().getAvailableDualClasses(player, player.getActiveSubClass().getClassId());
					for (int subClsId : availDualAwakenClasses)
					{
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_reawakendualclass_select_" + subClsId + "\">" + HtmlUtils.htmlClassName(subClsId) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-reawaken_list.htm", "<?CANCEL_CHANGE_SUB_LIST?>", availSubList.toString());
					return;
				}
				int newSubClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					String newSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_reawakendualclass_select_" + newSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(newSubClassId) + "</a>";
					showChatWindow(player, "default/" + getNpcId() + "-reawaken_confirm.htm", "<?CANCEL_SUB_CONFIRM?>", newSubConfirm);
					return;
				}
				String cmd3 = st.nextToken();
				if (cmd3.equalsIgnoreCase("confirm"))
				{
					long reawakeningCost = Math.round(_REAWAKENINGCOST.get(player.getLevel() < 95 ? player.getLevel() : 94) * 1000000);
					if(player.getInventory().getAdena() < reawakeningCost)
					{
						player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
						return;
					}
					player.getInventory().reduceAdena(reawakeningCost);
					int previousClassId = player.getActiveSubClass().getClassId();
					CertificationFunctions.cancelCertification(this, player, true, true);
					int humanSkillClassId = _DESTINYCHANGECLASSES.get(newSubClassId);
					if (player.modifySubClass(previousClassId, newSubClassId,true))
					{
						AwakingManager.getInstance().onTransferOnlyRemoveSkills(player, newSubClassId, humanSkillClassId);
						AwakingManager.getInstance().giveItems(player, previousClassId, newSubClassId);
						player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
						showChatWindow(player, "default/" + getNpcId() + "-add_success.htm");
						player.rewardSkills(false,true);
						player.sendSkillList();
						return;
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_error.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("CertificationSkillList"))
			{
				if(CertificationFunctions.checkConditionSkillList(this, player, 65))
					showTransformationSkillList(player, AcquireType.CERTIFICATION);
				return;
			}
			else if (cmd2.equalsIgnoreCase("DualCertificationSkillList"))
			{
				if(CertificationFunctions.checkConditionSkillList(this, player, 85))
					showTransformationSkillList(player, AcquireType.DUAL_CERTIFICATION);
				return;
			}
			else if (cmd2.equalsIgnoreCase("CancelRequest"))
			{
				showChatWindow(player, "default/" + getNpcId() + "-cancelrequest.htm");
				return;
			}
			else if (cmd2.equalsIgnoreCase("CertificationList"))
			{
				if(!st.hasMoreTokens())
				{
					return;
				}
				int levelCertification = Integer.parseInt(st.nextToken());	
				CertificationFunctions.showCertificationList(this, player, levelCertification);
			}
			else if (cmd2.equalsIgnoreCase("GetCertification"))
			{
				if(!st.hasMoreTokens())
				{
					return;
				}
				int levelCertification = Integer.parseInt(st.nextToken());				
				if(((levelCertification >= 65 && levelCertification <= 95) && levelCertification % 5 == 0) || levelCertification == 99)
				{
					CertificationFunctions.getInstance();
					CertificationFunctions.getCertification(levelCertification, this, player);
				}
				else
				{
					_log.info("Condition for select level are incorrect");
					return;
				}				
			}
			else if (cmd2.equalsIgnoreCase("confirmCertification"))
			{
				if(!st.hasMoreTokens())
				{
					return;
				}
				int levelCertification = Integer.parseInt(st.nextToken());
				if(((levelCertification >= 65 && levelCertification <= 95) && levelCertification % 5 == 0) || levelCertification == 99)
				{
					CertificationFunctions.confirmCertification(levelCertification, this, player);
				}
				else
				{
					_log.info("Condition level are incorrect");
					return;
				}				
				
			}
			else if (cmd2.equalsIgnoreCase("CertificationCancel"))
			{
				if(!st.hasMoreTokens())
				{
					showChatWindow(player, "default/" + getNpcId() + "-certification_cancel.htm", "<?COST?>", Config.ALT_GAME_RESET_CERTIFICATION_COST / 1000000 );
					return;
				}
				boolean confirm = Boolean.parseBoolean(st.nextToken());
				if(confirm)
					CertificationFunctions.cancelCertification(this, player, false, false);
			}
			else if (cmd2.equalsIgnoreCase("DualCertificationCancel"))
			{
				if(!st.hasMoreTokens())
				{
					showChatWindow(player, "default/" + getNpcId() + "-dualcertification_cancel.htm", "<?COST?>", Config.ALT_GAME_RESET_DUALCERTIFICATION_COST / 1000000);
					return;
				}
				boolean confirm = Boolean.parseBoolean(st.nextToken());
				if(confirm)
					CertificationFunctions.cancelCertification(this, player, true, false);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showSertifikationSkillList.
	 * @param player Player
	 * @param type AcquireType
	 */
	public void showSertifikationSkillList(Player player, AcquireType type)
	{
		if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if (!player.isQuestCompleted("_136_MoreThanMeetsTheEye"))
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
				return;
			}
		}
		showAcquireList(type, player);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <K extends Comparable, V extends Comparable> Map <K,V> sortByLevels(Map<K,V> map)
	{
		List <K> keys = new LinkedList<K>(map.keySet());
		Collections.sort(keys);
		Map <K,V> sortedMap = new LinkedHashMap<K,V>();
		for(K key: keys)
		{
			sortedMap.put(key, map.get(key));
		}
		return sortedMap;
	}
	
	private static Integer getChildClass(Integer previousClass)
	{
		int childClass = 0;
		for (ClassId clid : ClassId.values())
		{
			if(!clid.isOfLevel(ClassLevel.Fourth))
			{
				continue;
			}
			if(clid.getParentId() == previousClass)
			{
				childClass = clid.getId();
				break;
			}
		}
		return childClass;
	}
}
