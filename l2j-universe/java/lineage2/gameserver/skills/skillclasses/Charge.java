/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Charge extends Skill
{
	/**
	 * Field MAX_CHARGE. (value is 10)
	 */
	public static final int MAX_CHARGE = 10;
	/**
	 * Field _charges.
	 */
	private final int _charges;
	/**
	 * Field _fullCharge.
	 */
	private final boolean _fullCharge;
	
	/**
	 * Constructor for Charge.
	 * @param set StatsSet
	 */
	public Charge(StatsSet set)
	{
		super(set);
		_charges = set.getInteger("charges", getLevel());
		_fullCharge = set.getBool("fullCharge", false);
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
		Player player = (Player) activeChar;
		if ((getPower() <= 0) && (getId() != 2165) && (player.getIncreasedForce() >= _charges))
		{
			activeChar.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			return false;
		}
		else if (getId() == 2165)
		{
			player.sendPacket(new MagicSkillUse(player, player, 2165, 1, 0, 0));
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
		if (!activeChar.isPlayer())
		{
			return;
		}
		boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
		if (ss && (getTargetType() != SkillTargetType.TARGET_SELF))
		{
			activeChar.unChargeShots(false);
		}
		Creature realTarget;
		boolean reflected;
		for (Creature target : targets)
		{
			if (target.isDead() || (target == activeChar))
			{
				continue;
			}
			reflected = target.checkReflectSkill(activeChar, this);
			realTarget = reflected ? activeChar : target;
			if (getPower() > 0)
			{
				AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);
				if (info.lethal_dmg > 0)
				{
					realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
				}
				realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
				if (!reflected)
				{
					realTarget.doCounterAttack(this, activeChar, false);
				}
			}
			getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
		}
		chargePlayer((Player) activeChar, getId());
	}
	
	/**
	 * Method chargePlayer.
	 * @param player Player
	 * @param skillId Integer
	 */
	public void chargePlayer(Player player, Integer skillId)
	{
		if (player.getIncreasedForce() >= _charges)
		{
			player.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			return;
		}
		if (_fullCharge)
		{
			player.setIncreasedForce(_charges);
		}
		else
		{
			player.setIncreasedForce(player.getIncreasedForce() + 1);
		}
	}
}
