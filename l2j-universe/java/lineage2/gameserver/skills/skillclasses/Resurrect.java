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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Resurrect extends Skill
{
	/**
	 * Field _canPet.
	 */
	private final boolean _canPet;
	
	/**
	 * Constructor for Resurrect.
	 * @param set StatsSet
	 */
	public Resurrect(StatsSet set)
	{
		super(set);
		_canPet = set.getBool("canPet", false);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(final Creature activeChar, final Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!activeChar.isPlayer())
		{
			return false;
		}
		if ((target == null) || ((target != activeChar) && !target.isDead()))
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		Player player = (Player) activeChar;
		Player pcTarget = target.getPlayer();
		if (pcTarget == null)
		{
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		if (player.isInOlympiadMode() || pcTarget.isInOlympiadMode())
		{
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		for (GlobalEvent e : player.getEvents())
		{
			if (!e.canRessurect(player, target, forceUse))
			{
				player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
				return false;
			}
		}
		if (oneTarget())
		{
			if (target.isPet())
			{
				Pair<Integer, OnAnswerListener> ask = pcTarget.getAskListener(false);
				ReviveAnswerListener reviveAsk = (ask != null) && (ask.getValue() instanceof ReviveAnswerListener) ? (ReviveAnswerListener) ask.getValue() : null;
				if (reviveAsk != null)
				{
					if (reviveAsk.isForPet())
					{
						activeChar.sendPacket(Msg.BETTER_RESURRECTION_HAS_BEEN_ALREADY_PROPOSED);
					}
					else
					{
						activeChar.sendPacket(Msg.SINCE_THE_MASTER_WAS_IN_THE_PROCESS_OF_BEING_RESURRECTED_THE_ATTEMPT_TO_RESURRECT_THE_PET_HAS_BEEN_CANCELLED);
					}
					return false;
				}
				if (!(_canPet || (_targetType == SkillTargetType.TARGET_PET)))
				{
					player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
					return false;
				}
			}
			else if (target.isPlayer())
			{
				Pair<Integer, OnAnswerListener> ask = pcTarget.getAskListener(false);
				ReviveAnswerListener reviveAsk = (ask != null) && (ask.getValue() instanceof ReviveAnswerListener) ? (ReviveAnswerListener) ask.getValue() : null;
				if (reviveAsk != null)
				{
					if (reviveAsk.isForPet())
					{
						activeChar.sendPacket(Msg.WHILE_A_PET_IS_ATTEMPTING_TO_RESURRECT_IT_CANNOT_HELP_IN_RESURRECTING_ITS_MASTER);
					}
					else
					{
						activeChar.sendPacket(Msg.BETTER_RESURRECTION_HAS_BEEN_ALREADY_PROPOSED);
					}
					return false;
				}
				if (_targetType == SkillTargetType.TARGET_PET)
				{
					player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
					return false;
				}
			}
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		double percent = _power;
		if ((percent < 100) && !isHandler())
		{
			double wit_bonus = _power * (BaseStats.WIT.calcBonus(activeChar) - 1);
			percent += wit_bonus > 20 ? 20 : wit_bonus;
			if (percent > 90)
			{
				percent = 90;
			}
		}
		for (Creature target : targets)
		{
			Loop:
			if (target != null)
			{
				if (target.getPlayer() == null)
				{
					continue;
				}
				for (GlobalEvent e : target.getEvents())
				{
					if (!e.canRessurect((Player) activeChar, target, true))
					{
						break Loop;
					}
				}
				if (target.isPet() && _canPet)
				{
					if (target.getPlayer() == activeChar)
					{
						((PetInstance) target).doRevive(percent);
					}
					else
					{
						target.getPlayer().reviveRequest((Player) activeChar, percent, true);
					}
				}
				else if (target.isPlayer())
				{
					if (_targetType == SkillTargetType.TARGET_PET)
					{
						continue;
					}
					Player targetPlayer = (Player) target;
					Pair<Integer, OnAnswerListener> ask = targetPlayer.getAskListener(false);
					ReviveAnswerListener reviveAsk = (ask != null) && (ask.getValue() instanceof ReviveAnswerListener) ? (ReviveAnswerListener) ask.getValue() : null;
					if (reviveAsk != null)
					{
						continue;
					}
					targetPlayer.reviveRequest((Player) activeChar, percent, false);
				}
				else
				{
					continue;
				}
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
