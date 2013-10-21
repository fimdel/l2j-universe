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
package actions;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.model.reward.RewardGroup;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.model.reward.RewardType;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.utils.HtmlUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RewardListInfo
{
	/**
	 * Field pf.
	 */
	private static final NumberFormat pf = NumberFormat.getPercentInstance(Locale.ENGLISH);
	/**
	 * Field df.
	 */
	private static final NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
	static
	{
		pf.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(2);
	}
	
	/**
	 * Method showInfo.
	 * @param player Player
	 * @param npc NpcInstance
	 */
	public static void showInfo(Player player, NpcInstance npc)
	{
		final int diff = npc.calculateLevelDiffForDrop(player.isInParty() ? player.getParty().getLevel() : player.getLevel());
		double mod = npc.calcStat(Stats.REWARD_MULTIPLIER, 1., player, null);
		mod *= Experience.penaltyModifier(diff, 9);
		final NpcHtmlMessage htmlMessage = new NpcHtmlMessage(5);
		htmlMessage.replace("%npc_name%", HtmlUtils.htmlNpcName(npc.getNpcId()));
		if (mod <= 0)
		{
			htmlMessage.setFile("actions/rewardlist_to_weak.htm");
			player.sendPacket(htmlMessage);
			return;
		}
		if (npc.getTemplate().getRewards().isEmpty())
		{
			htmlMessage.setFile("actions/rewardlist_empty.htm");
			player.sendPacket(htmlMessage);
			return;
		}
		htmlMessage.setFile("actions/rewardlist_info.htm");
		final StringBuilder builder = new StringBuilder(100);
		for (Map.Entry<RewardType, RewardList> entry : npc.getTemplate().getRewards().entrySet())
		{
			RewardList rewardList = entry.getValue();
			switch (entry.getKey())
			{
				case RATED_GROUPED:
					ratedGroupedRewardList(builder, npc, rewardList, player, mod);
					break;
				case NOT_RATED_GROUPED:
					notRatedGroupedRewardList(builder, rewardList, mod);
					break;
				case NOT_RATED_NOT_GROUPED:
					notGroupedRewardList(builder, rewardList, 1.0, mod);
					break;
				case SWEEP:
					notGroupedRewardList(builder, rewardList, (Config.RATE_DROP_SPOIL + player.getVitalityBonus()) * player.getRateSpoil(), mod);
					break;
			}
		}
		htmlMessage.replace("%info%", builder.toString());
		player.sendPacket(htmlMessage);
	}
	
	/**
	 * Method ratedGroupedRewardList.
	 * @param tmp StringBuilder
	 * @param npc NpcInstance
	 * @param list RewardList
	 * @param player Player
	 * @param mod double
	 */
	public static void ratedGroupedRewardList(StringBuilder tmp, NpcInstance npc, RewardList list, Player player, double mod)
	{
		tmp.append("<table width=270 border=0>");
		tmp.append("<tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">").append(list.getType()).append("</font></td></tr></table></td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareWhite\" width=270 height=1> </td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
		for (RewardGroup g : list)
		{
			List<RewardData> items = g.getItems();
			double gchance = g.getChance();
			double gmod = mod;
			double grate;
			double gmult;
			double rateDrop = (npc instanceof RaidBossInstance ? Config.RATE_DROP_RAIDBOSS : npc.isSiegeGuard() ? (Config.RATE_DROP_SIEGE_GUARD + player.getVitalityBonus()) : Config.RATE_DROP_ITEMS * player.getRateItems());
			double rateAdena = (Config.RATE_DROP_ADENA + player.getVitalityBonus()) * player.getRateAdena();
			if (g.isAdena())
			{
				if (rateAdena == 0)
				{
					continue;
				}
				grate = rateAdena;
				if (gmod > 10)
				{
					gmod *= g.getChance() / RewardList.MAX_CHANCE;
					gchance = RewardList.MAX_CHANCE;
				}
				grate *= gmod;
			}
			else
			{
				if (rateDrop == 0)
				{
					continue;
				}
				grate = rateDrop;
				if (g.notRate())
				{
					grate = Math.min(gmod, 1.0);
				}
				else
				{
					grate *= gmod;
				}
			}
			gmult = Math.ceil(grate);
			tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
			tmp.append("<tr><td>");
			tmp.append("<table width=270 border=0 bgcolor=333333>");
			tmp.append("<tr><td width=170><font color=\"a2a0a2\">Group Chance: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td>");
			tmp.append("<td width=100 align=right>");
			tmp.append("</td></tr>");
			tmp.append("</table>").append("</td></tr>");
			tmp.append("<tr><td><table>");
			for (RewardData d : items)
			{
				double imult = d.notRate() ? 1.0 : gmult;
				String icon = d.getItem().getIcon();
				if ((icon == null) || icon.equals(StringUtils.EMPTY))
				{
					icon = "icon.etc_question_mark_i00";
				}
				tmp.append("<tr><td width=32><img src=").append(icon).append(" width=32 height=32></td><td width=238>").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1>");
				tmp.append("<font color=\"b09979\">[").append(Math.round(d.getMinDrop() * (g.isAdena() ? gmult : 1.0))).append("..").append(Math.round(d.getMaxDrop() * imult)).append("]&nbsp;");
				tmp.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
			}
			tmp.append("</table></td></tr>");
		}
		tmp.append("</table>");
	}
	
	/**
	 * Method notRatedGroupedRewardList.
	 * @param tmp StringBuilder
	 * @param list RewardList
	 * @param mod double
	 */
	public static void notRatedGroupedRewardList(StringBuilder tmp, RewardList list, double mod)
	{
		tmp.append("<table width=270 border=0>");
		tmp.append("<tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">").append(list.getType()).append("</font></td></tr></table></td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareWhite\" width=270 height=1> </td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
		for (RewardGroup g : list)
		{
			List<RewardData> items = g.getItems();
			double gchance = g.getChance();
			tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
			tmp.append("<tr><td>");
			tmp.append("<table width=270 border=0 bgcolor=333333>");
			tmp.append("<tr><td width=170><font color=\"a2a0a2\">Group Chance: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td>");
			tmp.append("<td width=100 align=right>");
			tmp.append("</td></tr>");
			tmp.append("</table>").append("</td></tr>");
			tmp.append("<tr><td><table>");
			for (RewardData d : items)
			{
				String icon = d.getItem().getIcon();
				if ((icon == null) || icon.equals(StringUtils.EMPTY))
				{
					icon = "icon.etc_question_mark_i00";
				}
				tmp.append("<tr><td width=32><img src=").append(icon).append(" width=32 height=32></td><td width=238>").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1>");
				tmp.append("<font color=\"b09979\">[").append(Math.round(d.getMinDrop())).append("..").append(Math.round(d.getMaxDrop())).append("]&nbsp;");
				tmp.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
			}
			tmp.append("</table></td></tr>");
		}
		tmp.append("</table>");
	}
	
	/**
	 * Method notGroupedRewardList.
	 * @param tmp StringBuilder
	 * @param list RewardList
	 * @param rate double
	 * @param mod double
	 */
	public static void notGroupedRewardList(StringBuilder tmp, RewardList list, double rate, double mod)
	{
		tmp.append("<table width=270 border=0>");
		tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
		tmp.append("<tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">").append(list.getType()).append("</font></td></tr></table></td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareWhite\" width=270 height=1> </td></tr>");
		tmp.append("<tr><td><img src=\"L2UI.SquareBlank\" width=270 height=10> </td></tr>");
		tmp.append("<tr><td><table>");
		for (RewardGroup g : list)
		{
			List<RewardData> items = g.getItems();
			double gmod = mod;
			double grate;
			double gmult;
			if (rate == 0)
			{
				continue;
			}
			grate = rate;
			if (g.notRate())
			{
				grate = Math.min(gmod, 1.0);
			}
			else
			{
				grate *= gmod;
			}
			gmult = Math.ceil(grate);
			for (RewardData d : items)
			{
				double imult = d.notRate() ? 1.0 : gmult;
				String icon = d.getItem().getIcon();
				if ((icon == null) || icon.equals(StringUtils.EMPTY))
				{
					icon = "icon.etc_question_mark_i00";
				}
				tmp.append("<tr><td width=32><img src=").append(icon).append(" width=32 height=32></td><td width=238>").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1>");
				tmp.append("<font color=\"b09979\">[").append(d.getMinDrop()).append("..").append(Math.round(d.getMaxDrop() * imult)).append("]&nbsp;");
				tmp.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
			}
		}
		tmp.append("</table></td></tr>");
		tmp.append("</table>");
	}
}
