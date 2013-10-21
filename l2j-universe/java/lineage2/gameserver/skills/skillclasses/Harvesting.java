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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Harvesting extends Skill
{
	/**
	 * Constructor for Harvesting.
	 * @param set StatsSet
	 */
	public Harvesting(StatsSet set)
	{
		super(set);
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
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (!target.isMonster())
				{
					continue;
				}
				MonsterInstance monster = (MonsterInstance) target;
				if (!monster.isSeeded())
				{
					activeChar.sendPacket(Msg.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
					continue;
				}
				if (!monster.isSeeded(player))
				{
					activeChar.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_HARVEST);
					continue;
				}
				double SuccessRate = Config.MANOR_HARVESTING_BASIC_SUCCESS;
				int diffPlayerTarget = Math.abs(activeChar.getLevel() - monster.getLevel());
				if (diffPlayerTarget > Config.MANOR_DIFF_PLAYER_TARGET)
				{
					SuccessRate -= (diffPlayerTarget - Config.MANOR_DIFF_PLAYER_TARGET) * Config.MANOR_DIFF_PLAYER_TARGET_PENALTY;
				}
				if (SuccessRate < 1)
				{
					SuccessRate = 1;
				}
				if (player.isGM())
				{
					player.sendMessage(new CustomMessage("lineage2.gameserver.skills.skillclasses.Harvesting.Chance", player).addNumber((long) SuccessRate));
				}
				if (!Rnd.chance(SuccessRate))
				{
					activeChar.sendPacket(Msg.THE_HARVEST_HAS_FAILED);
					monster.clearHarvest();
					continue;
				}
				RewardItem item = monster.takeHarvest();
				if (item == null)
				{
					continue;
				}
				ItemInstance harvest;
				if (!player.getInventory().validateCapacity(item.itemId, item.count) || !player.getInventory().validateWeight(item.itemId, item.count))
				{
					harvest = ItemFunctions.createItem(item.itemId);
					harvest.setCount(item.count);
					harvest.dropToTheGround(player, monster);
					continue;
				}
				player.getInventory().addItem(item.itemId, item.count);
				player.sendPacket(new SystemMessage(SystemMessage.S1_HARVESTED_S3_S2_S).addName(player).addNumber(item.count).addItemName(item.itemId));
				if (player.isInParty())
				{
					SystemMessage smsg = new SystemMessage(SystemMessage.S1_HARVESTED_S3_S2_S).addString(player.getName()).addNumber(item.count).addItemName(item.itemId);
					player.getParty().broadcastToPartyMembers(player, smsg);
				}
			}
		}
	}
}
