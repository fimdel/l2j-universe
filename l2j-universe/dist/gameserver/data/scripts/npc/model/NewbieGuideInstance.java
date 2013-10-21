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

import java.util.Arrays;
import java.util.List;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NewbieGuideInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(NewbieGuideInstance.class);
	/**
	 * Field mainHelpers.
	 */
	private static final List<?> mainHelpers = Arrays.asList(30598, 30599, 30600, 30601, 30602, 32135);
	
	/**
	 * Constructor for NewbieGuideInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public NewbieGuideInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if ((val == 0) && mainHelpers.contains(getNpcId()))
		{
			if (player.getClassLevel() == 1)
			{
				if (player.getVar("NewGuidReward") == null)
				{
					QuestState qs = player.getQuestState("_999_T1Tutorial");
					if (qs != null)
					{
						qs.unset("step");
					}
					player.setVar("NewGuidReward", "1", -1);
					boolean isMage = (player.getRace() != Race.orc) && player.getClassId().isMage();
					if (isMage)
					{
						player.sendPacket(new PlaySound("tutorial_voice_027"));
						Functions.addItem(player, 5790, 100);
					}
					else
					{
						player.sendPacket(new PlaySound("tutorial_voice_026"));
						Functions.addItem(player, 5789, 200);
					}
					Functions.addItem(player, 8594, 2);
					if (player.getLevel() == 1)
					{
						player.addExpAndSp(Experience.LEVEL[2] - player.getExp(), 50, 0, 0, true, false);
					}
					else
					{
						player.addExpAndSp(0, 50, 0, 0, true, false);
					}
				}
				if (player.getLevel() < 6)
				{
					if (player.isQuestCompleted("_001_LettersOfLove") || player.isQuestCompleted("_002_WhatWomenWant") || player.isQuestCompleted("_004_LongLivethePaagrioLord") || player.isQuestCompleted("_005_MinersFavor") || player.isQuestCompleted("_166_DarkMass") || player.isQuestCompleted("_174_SupplyCheck"))
					{
						if (!player.getVarB("ng1"))
						{
							String oldVar = player.getVar("ng1");
							player.setVar("ng1", oldVar == null ? "1" : String.valueOf(Integer.parseInt(oldVar) + 1), -1);
							player.addExpAndSp(Experience.LEVEL[6] - player.getExp(), 127, 0, 0, true, false);
							player.addAdena(11567);
						}
						player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q1-2.htm", val));
						return;
					}
					player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q1-1.htm", val).replace("%tonpc%", getQuestNpc(1, player)));
					return;
				}
				if (player.getLevel() < 10)
				{
					if (player.getVarB("p1q2"))
					{
						if (!player.getVarB("ng2"))
						{
							String oldVar = player.getVar("ng2");
							player.setVar("ng2", oldVar == null ? "1" : String.valueOf(Integer.parseInt(oldVar) + 1), -1);
							long addexp = Experience.LEVEL[10] - player.getExp();
							player.addExpAndSp(addexp, addexp / 24, 0, 0, true, false);
						}
						player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q3-1.htm", val).replace("%tonpc%", getQuestNpc(3, player)));
						return;
					}
					player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q2-1.htm", val).replace("%tonpc%", getQuestNpc(2, player)));
					return;
				}
				if (player.getLevel() < 15)
				{
					if (player.getVarB("p1q3"))
					{
						if (!player.getVarB("ng3"))
						{
							String oldVar = player.getVar("ng3");
							player.setVar("ng3", oldVar == null ? "1" : String.valueOf(Integer.parseInt(oldVar) + 1), -1);
							long addexp = Experience.LEVEL[15] - player.getExp();
							player.addExpAndSp(addexp, addexp / 22, 0, 0, true, false);
							player.addAdena(38180);
						}
						player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q4-1.htm", val).replace("%tonpc%", getQuestNpc(4, player)));
						return;
					}
					player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q3-1.htm", val).replace("%tonpc%", getQuestNpc(3, player)));
					return;
				}
				if (player.getLevel() < 18)
				{
					if (player.getVarB("p1q4"))
					{
						if (!player.getVarB("ng4"))
						{
							String oldVar = player.getVar("ng4");
							player.setVar("ng4", oldVar == null ? "1" : String.valueOf(Integer.parseInt(oldVar) + 1), -1);
							long addexp = Experience.LEVEL[18] - player.getExp();
							player.addExpAndSp(addexp, addexp / 5, 0, 0, true, false);
							player.addAdena(10018);
						}
						player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q4-2.htm", val));
						return;
					}
					player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q4-1.htm", val).replace("%tonpc%", getQuestNpc(4, player)));
					return;
				}
				player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q-no.htm", val));
				return;
			}
			player.sendPacket(new NpcHtmlMessage(player, this, "newbiehelper/q-no.htm", val));
			return;
		}
		super.showChatWindow(player, val);
	}
	
	/**
	 * Method getQuestNpc.
	 * @param quest int
	 * @param player Player
	 * @return String
	 */
	public String getQuestNpc(int quest, Player player)
	{
		int val = 0;
		switch (quest)
		{
			case 1:
				switch (getNpcId())
				{
					case 30598:
						val = 30048;
						break;
					case 30599:
						val = 30223;
						break;
					case 30600:
						val = 30130;
						break;
					case 30601:
						val = 30554;
						break;
					case 30602:
						val = 30578;
						break;
					case 32135:
						val = 32173;
						break;
				}
				break;
			case 2:
				switch (getNpcId())
				{
					case 30598:
						val = 30039;
						break;
					case 30599:
						val = 30221;
						break;
					case 30600:
						val = 30357;
						break;
					case 30601:
						val = 30535;
						break;
					case 30602:
						val = 30566;
						break;
					case 32135:
						val = 32173;
						break;
				}
				break;
			case 3:
				switch (player.getClassId())
				{
					case HUMAN_FIGHTER:
						val = 30008;
						break;
					case HUMAN_MAGE:
						val = 30017;
						break;
					case ELVEN_FIGHTER:
					case ELVEN_MAGE:
						val = 30218;
						break;
					case DARK_FIGHTER:
					case DARK_MAGE:
						val = 30358;
						break;
					case ORC_FIGHTER:
					case ORC_MAGE:
						val = 30568;
						break;
					case DWARVEN_FIGHTER:
						val = 30523;
						break;
					case KAMAEL_M_SOLDIER:
					case KAMAEL_F_SOLDIER:
						val = 32138;
						break;
					default:
						break;
				}
				break;
			case 4:
				switch (getNpcId())
				{
					case 30598:
						val = 30050;
						break;
					case 30599:
						val = 30222;
						break;
					case 30600:
						val = 30145;
						break;
					case 30601:
						val = 30519;
						break;
					case 30602:
						val = 30571;
						break;
					case 32135:
						val = 32133;
						break;
				}
				break;
		}
		if (val == 0)
		{
			_log.warn("WTF? L2NewbieGuideInstance " + getNpcId() + " not found next step " + quest + " for " + player.getClassId());
			return null;
		}
		NpcInstance npc = GameObjectsStorage.getByNpcId(val);
		if (npc == null)
		{
			return "";
		}
		player.sendPacket(new RadarControl(2, 1, npc.getLoc()));
		player.sendPacket(new RadarControl(0, 2, npc.getLoc()));
		return npc.getName();
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "newbiehelper/" + pom + ".htm";
	}
}
