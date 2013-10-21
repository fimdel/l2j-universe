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

import java.util.List;
import java.util.StringTokenizer;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class CaravanTraderInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field NativeTreasure. (value is 9684)
	 */
	private static final int NativeTreasure = 9684;
	/**
	 * Field HolyWater. (value is 9673)
	 */
	private static final int HolyWater = 9673;
	/**
	 * Field DarionsBadge. (value is 9674)
	 */
	private static final int DarionsBadge = 9674;
	/**
	 * Field FirstMark. (value is 9850)
	 */
	private static final int FirstMark = 9850;
	/**
	 * Field SecondMark. (value is 9851)
	 */
	private static final int SecondMark = 9851;
	/**
	 * Field ThirdMark. (value is 9852)
	 */
	private static final int ThirdMark = 9852;
	/**
	 * Field ForthMark. (value is 9853)
	 */
	private static final int ForthMark = 9853;
	/**
	 * Field ScorpionPoisonStinger. (value is 10012)
	 */
	private static final int ScorpionPoisonStinger = 10012;
	/**
	 * Field MarkOfBetrayal. (value is 9676)
	 */
	private static final int MarkOfBetrayal = 9676;
	/**
	 * Field MagicBottle. (value is 9672)
	 */
	private static final int MagicBottle = 9672;
	/**
	 * Field NativeHelmet. (value is 9669)
	 */
	private static final int NativeHelmet = 9669;
	/**
	 * Field NativeTunic. (value is 9670)
	 */
	private static final int NativeTunic = 9670;
	/**
	 * Field NativePants. (value is 9671)
	 */
	private static final int NativePants = 9671;
	/**
	 * Field LifeForce. (value is 9681)
	 */
	private static final int LifeForce = 9681;
	/**
	 * Field DimLifeForce. (value is 9680)
	 */
	private static final int DimLifeForce = 9680;
	/**
	 * Field ContainedLifeForce. (value is 9682)
	 */
	private static final int ContainedLifeForce = 9682;
	/**
	 * Field FieryDemonBloodSkill. (value is 2357)
	 */
	private static final int FieryDemonBloodSkill = 2357;
	
	/**
	 * Constructor for CaravanTraderInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CaravanTraderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		if (command.startsWith("Chat"))
		{
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			showDialog(player, getHtmlPath(getNpcId(), val, player));
			return;
		}
		else if (command.startsWith("give_treasures"))
		{
			if (player.getInventory().getCountOf(NativeTreasure) >= 40)
			{
				Functions.removeItem(player, NativeTreasure, 40);
				ServerVariables.set("HB_judesBoxes", true);
				showDialog(player, getHtmlPath(getNpcId(), 3, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 4, player));
			return;
		}
		else if (command.startsWith("buy_holy_water"))
		{
			if (player.getInventory().getCountOf(HolyWater) >= 1)
			{
				showDialog(player, getHtmlPath(getNpcId(), 10, player));
				return;
			}
			if (player.getInventory().getCountOf(DarionsBadge) >= 5)
			{
				Functions.removeItem(player, DarionsBadge, 5);
				Functions.addItem(player, HolyWater, 1);
				showDialog(player, getHtmlPath(getNpcId(), 6, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 3, player));
			return;
		}
		else if (command.startsWith("one_treasure"))
		{
			if ((player.getInventory().getCountOf(NativeTreasure) >= 1) && !ServerVariables.getBool("HB_bernardBoxes", false))
			{
				Functions.removeItem(player, NativeTreasure, 1);
				ServerVariables.set("HB_bernardBoxes", true);
				showDialog(player, getHtmlPath(getNpcId(), 8, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 9, player));
			return;
		}
		else if (command.startsWith("request_1_badge"))
		{
			if (hasProperMark(player, 1))
			{
				showDialog(player, getHtmlPath(getNpcId(), 3, player));
				return;
			}
			if (player.getInventory().getCountOf(DarionsBadge) >= 20)
			{
				Functions.removeItem(player, DarionsBadge, 20);
				Functions.addItem(player, FirstMark, 1);
				showDialog(player, getHtmlPath(getNpcId(), 4, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 5, player));
			return;
		}
		else if (command.startsWith("bdgc"))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				if (!st.hasMoreTokens())
				{
					return;
				}
				String param = st.nextToken();
				if ((param.length() < 1) || !Util.isNumber(param))
				{
					player.sendMessage("Incorrect count");
					return;
				}
				int val = Integer.parseInt(param);
				if (val <= 0)
				{
					player.sendMessage("Incorrect count");
					return;
				}
				if (player.getInventory().getCountOf(DarionsBadge) < val)
				{
					showDialog(player, getHtmlPath(getNpcId(), 2, player));
					return;
				}
				Functions.removeItem(player, DarionsBadge, val);
				HellboundManager.addConfidence(val * 10L);
				showDialog(player, getHtmlPath(getNpcId(), 3, player));
				return;
			}
			catch (NumberFormatException nfe)
			{
				showDialog(player, getHtmlPath(getNpcId(), 4, player));
				return;
			}
		}
		else if (command.startsWith("buy_magic_bottle"))
		{
			if ((player.getInventory().getCountOf(ScorpionPoisonStinger) >= 20) && hasProperMark(player, 1))
			{
				Functions.removeItem(player, ScorpionPoisonStinger, 20);
				Functions.addItem(player, MagicBottle, 1);
				showDialog(player, getHtmlPath(getNpcId(), 6, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 7, player));
			return;
		}
		else if (command.startsWith("cntf"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(5));
				if (val <= 0)
				{
					return;
				}
				switch (val)
				{
					case 1:
						if (player.getInventory().getCountOf(LifeForce) < 10)
						{
							showDialog(player, getHtmlPath(getNpcId(), 2, player));
							return;
						}
						Functions.removeItem(player, LifeForce, 10);
						HellboundManager.addConfidence(100);
						showDialog(player, getHtmlPath(getNpcId(), 3, player));
						break;
					case 2:
						if (player.getInventory().getCountOf(DimLifeForce) < 5)
						{
							showDialog(player, getHtmlPath(getNpcId(), 2, player));
							return;
						}
						Functions.removeItem(player, DimLifeForce, 5);
						HellboundManager.addConfidence(100);
						showDialog(player, getHtmlPath(getNpcId(), 3, player));
						break;
					case 3:
						if (player.getInventory().getCountOf(ContainedLifeForce) < 1)
						{
							showDialog(player, getHtmlPath(getNpcId(), 2, player));
							return;
						}
						Functions.removeItem(player, ContainedLifeForce, 1);
						HellboundManager.addConfidence(50);
						showDialog(player, getHtmlPath(getNpcId(), 3, player));
						break;
				}
			}
			catch (NumberFormatException nfe)
			{
				return;
			}
		}
		else if (command.startsWith("getc"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(5));
				if (val <= 0)
				{
					return;
				}
				if (player.getInventory().getCountOf(DarionsBadge) < 10)
				{
					showDialog(player, getHtmlPath(getNpcId(), 3, player));
					return;
				}
				switch (val)
				{
					case 1:
						Functions.removeItem(player, DarionsBadge, 10);
						Functions.addItem(player, NativeHelmet, 1);
						showDialog(player, getHtmlPath(getNpcId(), 4, player));
						break;
					case 2:
						Functions.removeItem(player, DarionsBadge, 10);
						Functions.addItem(player, NativeTunic, 1);
						showDialog(player, getHtmlPath(getNpcId(), 4, player));
						break;
					case 3:
						Functions.removeItem(player, DarionsBadge, 10);
						Functions.addItem(player, NativePants, 1);
						showDialog(player, getHtmlPath(getNpcId(), 4, player));
						break;
				}
			}
			catch (NumberFormatException nfe)
			{
				return;
			}
		}
		else if (command.startsWith("get_second"))
		{
			if ((player.getInventory().getCountOf(FirstMark) >= 1) && (player.getInventory().getCountOf(MarkOfBetrayal) >= 30) && (player.getInventory().getCountOf(ScorpionPoisonStinger) >= 60))
			{
				Functions.removeItem(player, FirstMark, 1);
				Functions.removeItem(player, MarkOfBetrayal, 30);
				Functions.removeItem(player, ScorpionPoisonStinger, 60);
				Functions.addItem(player, SecondMark, 1);
				showDialog(player, getHtmlPath(getNpcId(), 3, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 4, player));
			return;
		}
		else if (command.startsWith("secret_med"))
		{
			MultiSellHolder.getInstance().SeparateAndSend(250980014, player, 0);
			return;
		}
		else if (command.startsWith("get_third"))
		{
			if ((player.getInventory().getCountOf(SecondMark) >= 1) && (player.getInventory().getCountOf(LifeForce) >= 56) && (player.getInventory().getCountOf(ContainedLifeForce) >= 14))
			{
				Functions.removeItem(player, SecondMark, 1);
				Functions.removeItem(player, LifeForce, 56);
				Functions.removeItem(player, ContainedLifeForce, 14);
				Functions.addItem(player, ThirdMark, 1);
				Functions.addItem(player, 9994, 1);
				showDialog(player, getHtmlPath(getNpcId(), 6, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 4, player));
			return;
		}
		else if (command.startsWith("s80_trade"))
		{
			MultiSellHolder.getInstance().SeparateAndSend(250980013, player, 0);
			return;
		}
		else if (command.startsWith("try_open_door"))
		{
			if (player.getInventory().getCountOf(MarkOfBetrayal) >= 10)
			{
				Functions.removeItem(player, MarkOfBetrayal, 10);
				ReflectionUtils.getDoor(19250003).openMe();
				ReflectionUtils.getDoor(19250004).openMe();
				ThreadPoolManager.getInstance().schedule(new CloseDoor(), 60 * 1000L);
			}
			else
			{
				showDialog(player, getHtmlPath(getNpcId(), 4, player));
				return;
			}
		}
		else if (command.startsWith("supply_badges"))
		{
			if (player.getInventory().getCountOf(DarionsBadge) >= 5)
			{
				Functions.removeItem(player, DarionsBadge, 5);
				HellboundManager.addConfidence(20);
				showDialog(player, getHtmlPath(getNpcId(), 2, player));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 3, player));
			return;
		}
		else if (command.startsWith("tully_entrance"))
		{
			if (player.isQuestCompleted("_132_MatrasCuriosity"))
			{
				player.teleToLocation(new Location(17947, 283205, -9696));
				return;
			}
			showDialog(player, getHtmlPath(getNpcId(), 1, player));
			return;
		}
		else if (command.startsWith("infinitum_entrance"))
		{
			if ((player.getParty() == null) || !player.getParty().isLeader(player))
			{
				showDialog(player, getHtmlPath(getNpcId(), 1, player));
				return;
			}
			List<Player> members = player.getParty().getPartyMembers();
			for (Player member : members)
			{
				if ((member == null) || !isInRange(member, 500) || (member.getEffectList().getEffectsBySkillId(FieryDemonBloodSkill) == null))
				{
					showDialog(player, getHtmlPath(getNpcId(), 2, player));
					return;
				}
			}
			for (Player member : members)
			{
				member.teleToLocation(new Location(-22204, 277056, -15045));
			}
			return;
		}
		else if (command.startsWith("tully_dorian_entrance"))
		{
			if ((player.getParty() == null) || !player.getParty().isLeader(player))
			{
				showDialog(player, getHtmlPath(getNpcId(), 2, player));
				return;
			}
			List<Player> members = player.getParty().getPartyMembers();
			for (Player member : members)
			{
				if ((member == null) || !isInRange(member, 500) || !member.isQuestCompleted("_132_MatrasCuriosity"))
				{
					showDialog(player, getHtmlPath(getNpcId(), 1, player));
					return;
				}
			}
			for (Player member : members)
			{
				member.teleToLocation(new Location(-13400, 272827, -15304));
			}
		}
		else if (command.startsWith("enter_urban"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(2))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(2))
			{
				ReflectionUtils.enterReflection(player, 2);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
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
		String htmlpath = null;
		switch (getNpcId())
		{
			case 32356:
				if (HellboundManager.getHellboundLevel() <= 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if (HellboundManager.getHellboundLevel() == 5)
				{
					htmlpath = getHtmlPath(getNpcId(), 5, player);
				}
				else if (!ServerVariables.getBool("HB_judesBoxes", false))
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				break;
			case 32300:
				if (player.getTransformation() != 101)
				{
					htmlpath = getHtmlPath(getNpcId(), 5, player);
				}
				else if (HellboundManager.getHellboundLevel() < 2)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if (HellboundManager.getHellboundLevel() == 2)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if ((HellboundManager.getHellboundLevel() == 3) && !ServerVariables.getBool("HB_bernardBoxes", false))
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				else if (HellboundManager.getHellboundLevel() >= 3)
				{
					htmlpath = getHtmlPath(getNpcId(), 7, player);
				}
				break;
			case 32297:
				if (HellboundManager.getHellboundLevel() <= 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if (HellboundManager.getHellboundLevel() > 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				break;
			case 32354:
				if (HellboundManager.getHellboundLevel() <= 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if ((HellboundManager.getHellboundLevel() == 2) || (HellboundManager.getHellboundLevel() == 3))
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (HellboundManager.getHellboundLevel() == 6)
				{
					htmlpath = getHtmlPath(getNpcId(), 9, player);
				}
				else if (HellboundManager.getHellboundLevel() == 7)
				{
					htmlpath = getHtmlPath(getNpcId(), 10, player);
				}
				else if (HellboundManager.getHellboundLevel() > 7)
				{
					htmlpath = getHtmlPath(getNpcId(), 5, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 8, player);
				}
				break;
			case 32345:
				if (HellboundManager.getHellboundLevel() <= 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if (HellboundManager.getHellboundLevel() == 5)
				{
					htmlpath = getHtmlPath(getNpcId(), 7, player);
				}
				else if (HellboundManager.getHellboundLevel() == 6)
				{
					htmlpath = getHtmlPath(getNpcId(), 5, player);
				}
				else if (HellboundManager.getHellboundLevel() == 8)
				{
					htmlpath = getHtmlPath(getNpcId(), 6, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				break;
			case 32355:
				if (HellboundManager.getHellboundLevel() == 5)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				break;
			case 32298:
				if (HellboundManager.getHellboundLevel() <= 1)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else if (!hasProperMark(player, 1))
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (player.getInventory().getCountOf(FirstMark) > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				else if (player.getInventory().getCountOf(SecondMark) > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 5, player);
				}
				else if (player.getInventory().getCountOf(ThirdMark) > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 8, player);
				}
				break;
			case 32364:
				if (HellboundManager.getHellboundLevel() == 5)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 6, player);
				}
				break;
			case 32357:
				if (HellboundManager.getHellboundLevel() == 9)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (HellboundManager.getHellboundLevel() == 10)
				{
					htmlpath = getHtmlPath(getNpcId(), 4, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				break;
			case 32346:
				if (HellboundManager.getHellboundLevel() >= 10)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				break;
			case 32313:
				if (HellboundManager.getHellboundLevel() >= 11)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				break;
			case 32302:
				if (HellboundManager.getHellboundLevel() >= 11)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				break;
			case 32373:
				if (HellboundManager.getHellboundLevel() >= 11)
				{
					htmlpath = getHtmlPath(getNpcId(), 0, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				break;
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		if (htmlpath == null)
		{
			htmlpath = getHtmlPath(getNpcId(), 0, player);
		}
		html.setFile(htmlpath);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
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
		return "hellbound/" + pom + ".htm";
	}
	
	/**
	 * Method showDialog.
	 * @param player Player
	 * @param path String
	 */
	private void showDialog(Player player, String path)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(path);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
	}
	
	/**
	 * Method hasProperMark.
	 * @param player Player
	 * @param mark int
	 * @return boolean
	 */
	private boolean hasProperMark(Player player, int mark)
	{
		switch (mark)
		{
			case 1:
				if ((player.getInventory().getCountOf(FirstMark) != 0) || (player.getInventory().getCountOf(SecondMark) != 0) || (player.getInventory().getCountOf(ThirdMark) != 0) || (player.getInventory().getCountOf(ForthMark) != 0))
				{
					return true;
				}
				break;
			case 2:
				if ((player.getInventory().getCountOf(SecondMark) != 0) || (player.getInventory().getCountOf(ThirdMark) != 0) || (player.getInventory().getCountOf(ForthMark) != 0))
				{
					return true;
				}
				break;
			case 3:
				if ((player.getInventory().getCountOf(ThirdMark) != 0) || (player.getInventory().getCountOf(ForthMark) != 0))
				{
					return true;
				}
				break;
			case 4:
				if (player.getInventory().getCountOf(ForthMark) != 0)
				{
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}
	
	/**
	 * @author Mobius
	 */
	private class CloseDoor extends RunnableImpl
	{
		/**
		 * Constructor for CloseDoor.
		 */
		public CloseDoor()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			ReflectionUtils.getDoor(19250003).closeMe();
			ReflectionUtils.getDoor(19250004).closeMe();
		}
	}
}
