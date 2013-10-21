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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Sweep extends Skill
{
	/**
	 * Constructor for Sweep.
	 * @param set StatsSet
	 */
	public Sweep(StatsSet set)
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
		if (!target.isMonster() || !target.isDead())
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (!((MonsterInstance) target).isSpoiled())
		{
			activeChar.sendPacket(Msg.SWEEPER_FAILED_TARGET_NOT_SPOILED);
			return false;
		}
		if (!((MonsterInstance) target).isSpoiled((Player) activeChar))
		{
			activeChar.sendPacket(Msg.THERE_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER);
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
		Player player = (Player) activeChar;
		for (Creature targ : targets)
		{
			if ((targ == null) || !targ.isMonster() || !targ.isDead() || !((MonsterInstance) targ).isSpoiled())
			{
				continue;
			}
			MonsterInstance target = (MonsterInstance) targ;
			if (!target.isSpoiled(player))
			{
				activeChar.sendPacket(Msg.THERE_ARE_NO_PRIORITY_RIGHTS_ON_A_SWEEPER);
				continue;
			}
			List<RewardItem> items = target.takeSweep();
			if (items == null)
			{
				activeChar.getAI().setAttackTarget(null);
				target.endDecayTask();
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
					sweep.dropToTheGround(player, target);
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
				if (player.isInParty())
				{
					if (item.count == 1)
					{
						smsg = new SystemMessage(SystemMessage.S1_HAS_OBTAINED_S2_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						player.getParty().broadcastToPartyMembers(player, smsg);
					}
					else
					{
						smsg = new SystemMessage(SystemMessage.S1_HAS_OBTAINED_3_S2_S_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						smsg.addNumber(item.count);
						player.getParty().broadcastToPartyMembers(player, smsg);
					}
				}
			}
			activeChar.getAI().setAttackTarget(null);
			target.endDecayTask();
		}
	}
}
