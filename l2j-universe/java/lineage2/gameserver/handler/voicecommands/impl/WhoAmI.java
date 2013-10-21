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
package lineage2.gameserver.handler.voicecommands.impl;

import java.text.NumberFormat;
import java.util.Locale;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.utils.Strings;

import org.apache.commons.lang3.text.StrBuilder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WhoAmI implements IVoicedCommandHandler
{
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList = new String[]
	{
		"whoami",
		"whoiam"
	};
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[] * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param player Player
	 * @param args String
	 * @return boolean * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player player, String args)
	{
		Creature target = null;
		double hpRegen = Formulas.calcHpRegen(player);
		double cpRegen = Formulas.calcCpRegen(player);
		double mpRegen = Formulas.calcMpRegen(player);
		double hpDrain = player.calcStat(Stats.ABSORB_DAMAGE_PERCENT, 0., target, null);
		double mpDrain = player.calcStat(Stats.ABSORB_DAMAGEMP_PERCENT, 0., target, null);
		double hpGain = player.calcStat(Stats.HEAL_EFFECTIVNESS, 100., target, null);
		double mpGain = player.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100., target, null);
		double critPerc = 2 * player.calcStat(Stats.CRITICAL_DAMAGE, target, null);
		double critStatic = player.calcStat(Stats.CRITICAL_DAMAGE_STATIC, target, null);
		double mCritDmg = player.calcStat(Stats.MCRITICAL_DAMAGE, target, null);
		double blowRate = player.calcStat(Stats.FATALBLOW_RATE, target, null);
		ItemInstance shld = player.getSecondaryWeaponInstance();
		boolean shield = (shld != null) && (shld.getItemType() == WeaponType.NONE);
		double shieldDef = shield ? player.calcStat(Stats.SHIELD_DEFENCE, player.getTemplate().getBaseShldDef(), target, null) : 0.;
		double shieldRate = shield ? player.calcStat(Stats.SHIELD_RATE, target, null) : 0.;
		double xpRate = player.getRateExp();
		double spRate = player.getRateSp();
		double dropRate = player.getRateItems();
		double adenaRate = player.getRateAdena();
		double spoilRate = player.getRateSpoil();
		double fireResist = player.calcStat(Element.FIRE.getDefence(), 0., target, null);
		double windResist = player.calcStat(Element.WIND.getDefence(), 0., target, null);
		double waterResist = player.calcStat(Element.WATER.getDefence(), 0., target, null);
		double earthResist = player.calcStat(Element.EARTH.getDefence(), 0., target, null);
		double holyResist = player.calcStat(Element.HOLY.getDefence(), 0., target, null);
		double unholyResist = player.calcStat(Element.UNHOLY.getDefence(), 0., target, null);
		double bleedPower = player.calcStat(Stats.BLEED_POWER, target, null);
		double bleedResist = player.calcStat(Stats.BLEED_RESIST, target, null);
		double poisonPower = player.calcStat(Stats.POISON_POWER, target, null);
		double poisonResist = player.calcStat(Stats.POISON_RESIST, target, null);
		double stunPower = player.calcStat(Stats.STUN_POWER, target, null);
		double stunResist = player.calcStat(Stats.STUN_RESIST, target, null);
		double rootPower = player.calcStat(Stats.ROOT_POWER, target, null);
		double rootResist = player.calcStat(Stats.ROOT_RESIST, target, null);
		double sleepPower = player.calcStat(Stats.SLEEP_POWER, target, null);
		double sleepResist = player.calcStat(Stats.SLEEP_RESIST, target, null);
		double paralyzePower = player.calcStat(Stats.PARALYZE_POWER, target, null);
		double paralyzeResist = player.calcStat(Stats.PARALYZE_RESIST, target, null);
		double mentalPower = player.calcStat(Stats.MENTAL_POWER, target, null);
		double mentalResist = player.calcStat(Stats.MENTAL_RESIST, target, null);
		double debuffPower = player.calcStat(Stats.DEBUFF_POWER, target, null);
		double debuffResist = player.calcStat(Stats.DEBUFF_RESIST, target, null);
		double cancelPower = player.calcStat(Stats.CANCEL_POWER, target, null);
		double cancelResist = player.calcStat(Stats.CANCEL_RESIST, target, null);
		double swordResist = 100. - player.calcStat(Stats.SWORD_WPN_VULNERABILITY, target, null);
		double dualResist = 100. - player.calcStat(Stats.DUAL_WPN_VULNERABILITY, target, null);
		double bluntResist = 100. - player.calcStat(Stats.BLUNT_WPN_VULNERABILITY, target, null);
		double daggerResist = 100. - player.calcStat(Stats.DAGGER_WPN_VULNERABILITY, target, null);
		double bowResist = 100. - player.calcStat(Stats.BOW_WPN_VULNERABILITY, target, null);
		double crossbowResist = 100. - player.calcStat(Stats.CROSSBOW_WPN_VULNERABILITY, target, null);
		double poleResist = 100. - player.calcStat(Stats.POLE_WPN_VULNERABILITY, target, null);
		double fistResist = 100. - player.calcStat(Stats.FIST_WPN_VULNERABILITY, target, null);
		double critChanceResist = 100. - player.calcStat(Stats.CRIT_CHANCE_RECEPTIVE, target, null);
		double critDamResistStatic = player.calcStat(Stats.CRIT_DAMAGE_RECEPTIVE, target, null);
		double critDamResist = 100. - (100 * (player.calcStat(Stats.CRIT_DAMAGE_RECEPTIVE, 1., target, null) - critDamResistStatic));
		String dialog = HtmCache.getInstance().getNotNull("command/whoami.htm", player);
		NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(1);
		df.setMinimumFractionDigits(1);
		StrBuilder sb = new StrBuilder(dialog);
		sb.replaceFirst("%hpRegen%", df.format(hpRegen));
		sb.replaceFirst("%cpRegen%", df.format(cpRegen));
		sb.replaceFirst("%mpRegen%", df.format(mpRegen));
		sb.replaceFirst("%hpDrain%", df.format(hpDrain));
		sb.replaceFirst("%mpDrain%", df.format(mpDrain));
		sb.replaceFirst("%hpGain%", df.format(hpGain));
		sb.replaceFirst("%mpGain%", df.format(mpGain));
		sb.replaceFirst("%critPerc%", df.format(critPerc));
		sb.replaceFirst("%critStatic%", df.format(critStatic));
		sb.replaceFirst("%mCritDmg%", df.format(mCritDmg));
		sb.replaceFirst("%blowRate%", df.format(blowRate));
		sb.replaceFirst("%shieldDef%", df.format(shieldDef));
		sb.replaceFirst("%shieldRate%", df.format(shieldRate));
		sb.replaceFirst("%xpRate%", df.format(xpRate));
		sb.replaceFirst("%spRate%", df.format(spRate));
		sb.replaceFirst("%dropRate%", df.format(dropRate));
		sb.replaceFirst("%adenaRate%", df.format(adenaRate));
		sb.replaceFirst("%spoilRate%", df.format(spoilRate));
		sb.replaceFirst("%fireResist%", df.format(fireResist));
		sb.replaceFirst("%windResist%", df.format(windResist));
		sb.replaceFirst("%waterResist%", df.format(waterResist));
		sb.replaceFirst("%earthResist%", df.format(earthResist));
		sb.replaceFirst("%holyResist%", df.format(holyResist));
		sb.replaceFirst("%darkResist%", df.format(unholyResist));
		sb.replaceFirst("%bleedPower%", df.format(bleedPower));
		sb.replaceFirst("%bleedResist%", df.format(bleedResist));
		sb.replaceFirst("%poisonPower%", df.format(poisonPower));
		sb.replaceFirst("%poisonResist%", df.format(poisonResist));
		sb.replaceFirst("%stunPower%", df.format(stunPower));
		sb.replaceFirst("%stunResist%", df.format(stunResist));
		sb.replaceFirst("%rootPower%", df.format(rootPower));
		sb.replaceFirst("%rootResist%", df.format(rootResist));
		sb.replaceFirst("%sleepPower%", df.format(sleepPower));
		sb.replaceFirst("%sleepResist%", df.format(sleepResist));
		sb.replaceFirst("%paralyzePower%", df.format(paralyzePower));
		sb.replaceFirst("%paralyzeResist%", df.format(paralyzeResist));
		sb.replaceFirst("%mentalPower%", df.format(mentalPower));
		sb.replaceFirst("%mentalResist%", df.format(mentalResist));
		sb.replaceFirst("%debuffPower%", df.format(debuffPower));
		sb.replaceFirst("%debuffResist%", df.format(debuffResist));
		sb.replaceFirst("%cancelPower%", df.format(cancelPower));
		sb.replaceFirst("%cancelResist%", df.format(cancelResist));
		sb.replaceFirst("%swordResist%", df.format(swordResist));
		sb.replaceFirst("%dualResist%", df.format(dualResist));
		sb.replaceFirst("%bluntResist%", df.format(bluntResist));
		sb.replaceFirst("%daggerResist%", df.format(daggerResist));
		sb.replaceFirst("%bowResist%", df.format(bowResist));
		sb.replaceFirst("%crossbowResist%", df.format(crossbowResist));
		sb.replaceFirst("%fistResist%", df.format(fistResist));
		sb.replaceFirst("%poleResist%", df.format(poleResist));
		sb.replaceFirst("%critChanceResist%", df.format(critChanceResist));
		sb.replaceFirst("%critDamResist%", df.format(critDamResist));
		NpcHtmlMessage msg = new NpcHtmlMessage(0);
		msg.setHtml(Strings.bbParse(sb.toString()));
		player.sendPacket(msg);
		return true;
	}
}
