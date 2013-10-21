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
package services;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class VitaminManager extends Functions
{
	/**
	 * Minion Coupons
	 */
	private static final int MinionCoupon = 21275; // Minion Coupon
	private static final int SuperiorMinionCoupon = 21279; // Superior Minion Coupon
	private static final int EnhancedRoseSpiritCoupon = 20914; // Enhanced Rose Spirit Coupon
	
	/**
	 * Minion Pets
	 */
	private static final int WhiteWeasellWhistle = 13017;
	private static final int FairyPrincessWhistle = 13018;
	private static final int WildBeastFighterWhistle = 13019;
	private static final int FoxShamanWhistle = 13020;
	
	/**
	 * Superior Minion Pets
	 */
	private static final int ToyKnightWhistle = 14061;
	private static final int SpiritShamanWhistle = 14062;
	private static final int TurtleAsceticWhistle = 14064;
	
	/**
	 * Rose Spirit
	 */
	private static final int DeshelophNeck = 20915;
	private static final int HyumNeck = 20916;
	private static final int LekangNeck = 20917;
	private static final int LiliasNeck = 20918;
	private static final int LaphamNeck = 20919;
	private static final int MafumNeck = 20920;
	
	/**
	 * Method giveWeasel.
	 */
	public void giveWhiteWeasel()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, MinionCoupon) > 0)
		{
			removeItem(player, MinionCoupon, 1);
			addItem(player, WhiteWeasellWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method givePrinc.
	 */
	public void giveFairyPrincess()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, MinionCoupon) > 0)
		{
			removeItem(player, MinionCoupon, 1);
			addItem(player, FairyPrincessWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveBeast.
	 */
	public void giveWildBeastFighter()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, MinionCoupon) > 0)
		{
			removeItem(player, MinionCoupon, 1);
			addItem(player, WildBeastFighterWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveFox.
	 */
	public void giveFoxShaman()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, MinionCoupon) > 0)
		{
			removeItem(player, MinionCoupon, 1);
			addItem(player, FoxShamanWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveKnight.
	 */
	public void giveToyKnight()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SuperiorMinionCoupon) > 0)
		{
			removeItem(player, SuperiorMinionCoupon, 1);
			addItem(player, ToyKnightWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveSpirit.
	 */
	public void giveSpiritShaman()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SuperiorMinionCoupon) > 0)
		{
			removeItem(player, SuperiorMinionCoupon, 1);
			addItem(player, SpiritShamanWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveTurtle.
	 */
	public void giveTurtleAscetic()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SuperiorMinionCoupon) > 0)
		{
			removeItem(player, SuperiorMinionCoupon, 1);
			addItem(player, TurtleAsceticWhistle, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveDesheloph.
	 */
	public void giveDesheloph()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, DeshelophNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveHyum.
	 */
	public void giveHyum()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, HyumNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveLekang.
	 */
	public void giveLekang()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, LekangNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveLilias.
	 */
	public void giveLilias()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, LiliasNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveLapham.
	 */
	public void giveLapham()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, LaphamNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveMafum.
	 */
	public void giveMafum()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, EnhancedRoseSpiritCoupon) > 0)
		{
			removeItem(player, EnhancedRoseSpiritCoupon, 1);
			addItem(player, MafumNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no2.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
}