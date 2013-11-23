package handler.items;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Summon;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.MagicSkillUse;

public class BeastShot extends ScriptItemHandler {
    private final static int[] _itemIds = {6645, 6646, 6647, 20332, 20333, 20334};

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;
        Player player = (Player) playable;

        boolean isAutoSoulShot = false;
        if (player.getAutoSoulShot().contains(item.getItemId()))
            isAutoSoulShot = true;

        if (player.getSummonList().size() == 0) {
            if (!isAutoSoulShot)
                player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
            return false;
        }

        int deadCount = 0;
        for (Summon summon : player.getSummonList()) {
            if (summon.isDead()) deadCount++;
        }
        if (deadCount == player.getSummonList().size()) {
            if (!isAutoSoulShot)
                player.sendPacket(Msg.WHEN_PET_OR_SERVITOR_IS_DEAD_SOULSHOTS_OR_SPIRITSHOTS_FOR_PET_OR_SERVITOR_ARE_NOT_AVAILABLE);
            return false;
        }

        int consumption = 0;
        int skillid = 0;

        switch (item.getItemId()) {
            case 6645:
            case 20332:
                for (Summon summon : player.getSummonList()) {
                    if (summon.getChargedSoulShot())
                        continue;
                    consumption = summon.getSoulshotConsumeCount();
                    if (!player.getInventory().destroyItem(item, consumption)) {
                        player.sendPacket(Msg.YOU_DONT_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_PET_SERVITOR);
                        return false;
                    }
                    summon.chargeSoulShot();
                    skillid = 2033;
                }
                break;
            case 6646:
            case 20333:
                for (Summon summon : player.getSummonList()) {
                    if (summon.getChargedSpiritShot() > 0)
                        continue;
                    consumption = summon.getSpiritshotConsumeCount();
                    if (!player.getInventory().destroyItem(item, consumption)) {
                        player.sendPacket(Msg.YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
                        return false;
                    }
                    summon.chargeSpiritShot(ItemInstance.CHARGED_SPIRITSHOT);
                    skillid = 2008;
                }
                break;
            case 6647:
            case 20334:
                for (Summon summon : player.getSummonList()) {
                    if (summon.getChargedSpiritShot() > 1)
                        continue;
                    consumption = summon.getSpiritshotConsumeCount();
                    if (!player.getInventory().destroyItem(item, consumption)) {
                        player.sendPacket(Msg.YOU_DONT_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
                        return false;
                    }
                    summon.chargeSpiritShot(ItemInstance.CHARGED_BLESSED_SPIRITSHOT);
                    skillid = 2009;
                }
                break;
        }
        for (Summon summon : player.getSummonList()) {
            summon.broadcastPacket(new MagicSkillUse(summon, summon, skillid, 1, 0, 0));
        }
        return true;
    }

    @Override
    public final int[] getItemIds() {
        return _itemIds;
    }
}