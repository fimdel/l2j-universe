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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.ItemFunctions;

/**
 */
public class Plunder extends Skill
{
	/**
	 * Constructor for Sweep.
	 * @param set StatsSet
	 */
	public Plunder(StatsSet set)
	{
		super(set);
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
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (isNotTargetAoE())
		{
			return super.checkCondition(activeChar, target, forceUse, dontMove, first);
		}
		if (target == null)
		{
			return false;
		}
		if (!target.isMonster())
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
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
		int ss = isSSPossible() ? (isMagic() ? activeChar.getChargedSpiritShot() : activeChar.getChargedSoulShot() ? 2 : 0) : 0;
		if ((ss > 0) && (getPower() > 0))
		{
			activeChar.unChargeShots(false);
		}
		for (Creature target : targets)
		{
			//SPOIL PART
			if ((target != null) && !target.isDead())
			{
				if (target.isMonster())
				{
					if (!((MonsterInstance) target).isSpoiled())
					{
						MonsterInstance monster = (MonsterInstance) target;
						boolean success;
						success = Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate());
						if (success && monster.setSpoiled((Player) activeChar))
						{
							activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_SUCCEEDED).addSkillName(_id, getDisplayLevel()));
						}
						else
						{
							activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_FAILED).addSkillName(_id, getDisplayLevel()));
							return;
						}
					}
				}
				if (getPower() > 0)
				{
					double damage, reflectableDamage = 0;
					if (isMagic())
					{
						AttackInfo info = Formulas.calcMagicDam(activeChar, target, this, ss);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
					}
					else
					{
						AttackInfo info = Formulas.calcPhysDam(activeChar, target, this, false, false, ss > 0, false);
						damage = info.damage;
						reflectableDamage = info.reflectableDamage;
						if (info.lethal_dmg > 0)
						{
							target.reduceCurrentHp(info.lethal_dmg, reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
						}
					}
					target.reduceCurrentHp(damage, reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
					target.doCounterAttack(this, activeChar, false);
				}
				getEffects(activeChar, target, false, false);
				target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, Math.max(_effectPoint, 1));
			}
			//SWEEP PART
			Player player = (Player) activeChar;
			if ((target == null) || !target.isMonster())
			{
				continue;
			}
			MonsterInstance targetMonster = (MonsterInstance) target;
			List<RewardItem> items = targetMonster.takeSweep();
			if (items == null)
			{
				continue;
			}
			for (RewardItem item : items)
			{
				ItemInstance sweep = ItemFunctions.createItem(item.itemId);
				sweep.setCount(item.count);
				if (player.isInParty() && player.getParty().isDistributeSpoilLoot())
				{
					player.getParty().distributeItem(player, sweep, null);
					continue;
				}
				if (!player.getInventory().validateCapacity(sweep) || !player.getInventory().validateWeight(sweep))
				{
					sweep.dropToTheGround(player, targetMonster);
					continue;
				}
				player.getInventory().addItem(sweep);
				SystemMessage smsg;
				if (item.count == 1)
				{
					smsg = new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1);
					smsg.addItemName(item.itemId);
					player.sendPacket(smsg);
				}
				else
				{
					smsg = new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S2_S1);
					smsg.addItemName(item.itemId);
					smsg.addNumber(item.count);
					player.sendPacket(smsg);
				}
			}
		}
	}
}
