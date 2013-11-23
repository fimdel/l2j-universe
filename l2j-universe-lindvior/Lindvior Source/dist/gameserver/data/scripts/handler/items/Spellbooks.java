/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package handler.items;

import gnu.trove.TIntHashSet;
import l2p.gameserver.data.xml.holder.SkillAcquireHolder;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.SkillLearn;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.MagicSkillUse;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.tables.SkillTable;

import java.util.List;

public class Spellbooks extends ScriptItemHandler {
    private int[] _itemIds = null;

    public Spellbooks() {
        TIntHashSet list = new TIntHashSet();
        List<SkillLearn> l = SkillAcquireHolder.getInstance().getAllSpellbookSkillTree();
        for (SkillLearn learn : l)
            list.add(learn.getItemId());

        _itemIds = list.toArray();
    }

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (!playable.isPlayer())
            return false;

        Player player = (Player) playable;

        if (item.getCount() < 1) {
            player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
            return false;
        }

        List<SkillLearn> list = SkillAcquireHolder.getInstance().getSkillLearnForSpellBook(player, item.getItemId());
        if (list.isEmpty()) {
            player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
            return false;
        }

        boolean alreadyHas = true;
        for (SkillLearn learn : list) {
            if (player.getSkillLevel(learn.getId()) != learn.getLevel()) {
                alreadyHas = false;
                break;
            }
        }
        if (alreadyHas) {
            player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
            return false;
        }

        boolean wrongLvl = false;
        for (SkillLearn learn : list) {
            if (player.getLevel() < learn.getMinLevel())
                wrongLvl = true;
        }

        if (wrongLvl) {
            player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
            return false;
        }

        if (!player.consumeItem(item.getItemId(), 1L))
            return false;

        for (SkillLearn skillLearn : list) {
            Skill skill = SkillTable.getInstance().getInfo(skillLearn.getId(), skillLearn.getLevel());
            if (skill == null)
                continue;

            player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skill.getId(), skill.getLevel()));
            player.addSkill(skill, true);
        }

        player.updateStats();
        player.sendSkillList();
        player.broadcastPacket(new MagicSkillUse(player, player, 2790, 1, 1, -1));
        return true;
    }

    @Override
    public int[] getItemIds() {
        return _itemIds;
    }
}