package handler.items;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.tables.SkillTable;

public class FlyTransformBooks extends ScriptItemHandler {
    private static final int[] _itemIds = {13553, 13554};

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;
        Player player = (Player) playable;
        if (player.getLevel() < 75 || player.getLevel() > 99) {
            player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
            return false;
        }

        int skillId = 0;
        switch (item.getItemId()) {
            case 13553:
                skillId = 841;
                break;
            case 13554:
                skillId = 842;
                break;
        }
        if (skillId == 0 || player.getKnownSkill(skillId) != null) {
            player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
            return false;
        }

        Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
        if (skill == null)
            return false;

        if (!player.consumeItem(item.getItemId(), 1L))
            return false;

        player.addSkill(skill, true);
        return true;
    }

    @Override
    public int[] getItemIds() {
        return _itemIds;
    }
}
