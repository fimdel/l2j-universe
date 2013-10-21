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
package lineage2.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.SummonInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectDiscord extends Effect
{
	/**
	 * Constructor for EffectDiscord.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectDiscord(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		int skilldiff = _effected.getLevel() - _skill.getMagicLevel();
		int lvldiff = _effected.getLevel() - _effector.getLevel();
		if ((skilldiff > 10) || ((skilldiff > 5) && Rnd.chance(30)) || Rnd.chance(Math.abs(lvldiff) * 2))
		{
			return false;
		}
		boolean multitargets = _skill.isAoE();
		if (!_effected.isMonster())
		{
			if (!multitargets)
			{
				getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			}
			return false;
		}
		if (_effected.isFearImmune() || _effected.isRaid())
		{
			if (!multitargets)
			{
				getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			}
			return false;
		}
		Player player = _effected.getPlayer();
		if (player != null)
		{
			SiegeEvent<?, ?> siegeEvent = player.getEvent(SiegeEvent.class);
			if (_effected.isServitor() && (siegeEvent != null) && siegeEvent.containsSiegeSummon((SummonInstance) _effected))
			{
				if (!multitargets)
				{
					getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
				}
				return false;
			}
		}
		if (_effected.isInZonePeace())
		{
			if (!multitargets)
			{
				getEffector().sendPacket(Msg.YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE);
			}
			return false;
		}
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startConfused();
		onActionTime();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if (!_effected.stopConfused())
		{
			_effected.abortAttack(true, true);
			_effected.abortCast(true, true);
			_effected.stopMove();
			_effected.getAI().setAttackTarget(null);
			_effected.setWalking();
			_effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		List<Creature> targetList = new ArrayList<>();
		for (Creature character : _effected.getAroundCharacters(900, 200))
		{
			if (character.isNpc() && (character != getEffected()))
			{
				targetList.add(character);
			}
		}
		if (targetList.isEmpty())
		{
			return true;
		}
		Creature target = targetList.get(Rnd.get(targetList.size()));
		_effected.setRunning();
		_effected.getAI().Attack(target, true, false);
		return false;
	}
}
