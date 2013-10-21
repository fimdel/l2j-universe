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
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Sowing extends Skill
{
	/**
	 * Constructor for Sowing.
	 * @param set StatsSet
	 */
	public Sowing(StatsSet set)
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
		int seedId = player.getUseSeed();
		boolean altSeed = ItemHolder.getInstance().getTemplate(seedId).isAltSeed();
		if (!player.getInventory().destroyItemByItemId(seedId, 1L))
		{
			activeChar.sendActionFailed();
			return;
		}
		player.sendPacket(SystemMessage2.removeItems(seedId, 1L));
		for (Creature target : targets)
		{
			if (target != null)
			{
				MonsterInstance monster = (MonsterInstance) target;
				if (monster.isSeeded())
				{
					continue;
				}
				double SuccessRate = Config.MANOR_SOWING_BASIC_SUCCESS;
				double diffPlayerTarget = Math.abs(activeChar.getLevel() - target.getLevel());
				double diffSeedTarget = Math.abs(Manor.getInstance().getSeedLevel(seedId) - target.getLevel());
				if (diffPlayerTarget > Config.MANOR_DIFF_PLAYER_TARGET)
				{
					SuccessRate -= (diffPlayerTarget - Config.MANOR_DIFF_PLAYER_TARGET) * Config.MANOR_DIFF_PLAYER_TARGET_PENALTY;
				}
				if (diffSeedTarget > Config.MANOR_DIFF_SEED_TARGET)
				{
					SuccessRate -= (diffSeedTarget - Config.MANOR_DIFF_SEED_TARGET) * Config.MANOR_DIFF_SEED_TARGET_PENALTY;
				}
				if (altSeed)
				{
					SuccessRate *= Config.MANOR_SOWING_ALT_BASIC_SUCCESS / Config.MANOR_SOWING_BASIC_SUCCESS;
				}
				if (SuccessRate < 1)
				{
					SuccessRate = 1;
				}
				if (player.isGM())
				{
					activeChar.sendMessage(new CustomMessage("lineage2.gameserver.skills.skillclasses.Sowing.Chance", player).addNumber((long) SuccessRate));
				}
				if (Rnd.chance(SuccessRate) && monster.setSeeded(player, seedId, altSeed))
				{
					activeChar.sendPacket(Msg.THE_SEED_WAS_SUCCESSFULLY_SOWN);
				}
				else
				{
					activeChar.sendPacket(Msg.THE_SEED_WAS_NOT_SOWN);
				}
			}
		}
	}
}
