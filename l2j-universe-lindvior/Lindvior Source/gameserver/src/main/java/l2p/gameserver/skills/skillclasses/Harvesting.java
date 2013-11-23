package l2p.gameserver.skills.skillclasses;

import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.reward.RewardItem;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.utils.ItemFunctions;

import java.util.List;

public class Harvesting extends Skill {
    public Harvesting(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!activeChar.isPlayer())
            return;

        Player player = (Player) activeChar;

        for (Creature target : targets)
            if (target != null) {
                if (!target.isMonster())
                    continue;

                MonsterInstance monster = (MonsterInstance) target;

                // Не посеяно
                if (!monster.isSeeded()) {
                    activeChar.sendPacket(Msg.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
                    continue;
                }

                if (!monster.isSeeded(player)) {
                    activeChar.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_HARVEST);
                    continue;
                }

                double SuccessRate = Config.MANOR_HARVESTING_BASIC_SUCCESS;
                int diffPlayerTarget = Math.abs(activeChar.getLevel() - monster.getLevel());

                // Штраф, на разницу уровней между мобом и игроком
                // 5% на каждый уровень при разнице >5 - по умолчанию
                if (diffPlayerTarget > Config.MANOR_DIFF_PLAYER_TARGET)
                    SuccessRate -= (diffPlayerTarget - Config.MANOR_DIFF_PLAYER_TARGET) * Config.MANOR_DIFF_PLAYER_TARGET_PENALTY;

                // Минимальный шанс успеха всегда 1%
                if (SuccessRate < 1)
                    SuccessRate = 1;

                if (player.isGM())
                    player.sendMessage(new CustomMessage("l2p.gameserver.skills.skillclasses.Harvesting.Chance", player).addNumber((long) SuccessRate));

                if (!Rnd.chance(SuccessRate)) {
                    activeChar.sendPacket(Msg.THE_HARVEST_HAS_FAILED);
                    monster.clearHarvest();
                    continue;
                }

                RewardItem item = monster.takeHarvest();
                if (item == null)
                    continue;

                ItemInstance harvest;
                if (!player.getInventory().validateCapacity(item.itemId, item.count) || !player.getInventory().validateWeight(item.itemId, item.count)) {
                    harvest = ItemFunctions.createItem(item.itemId);
                    harvest.setCount(item.count);
                    harvest.dropToTheGround(player, monster);
                    continue;
                }

                player.getInventory().addItem(item.itemId, item.count);

                player.sendPacket(new SystemMessage(SystemMessage.S1_HARVESTED_S3_S2_S).addName(player).addNumber(item.count).addItemName(item.itemId));
                if (player.isInParty()) {
                    SystemMessage smsg = new SystemMessage(SystemMessage.S1_HARVESTED_S3_S2_S).addString(player.getName()).addNumber(item.count).addItemName(item.itemId);
                    player.getParty().broadcastToPartyMembers(player, smsg);
                }
            }
    }
}
