/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package handler.items;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.SkillList;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.tables.SkillTable;

/**
 * Created by IntelliJ IDEA.
 * User: Darvin
 * Date: 24.02.12
 * Time: 20:42
 */

public class SupportPower extends ScriptItemHandler {
    private static final int[] ITEM_IDS = new int[]{24001};

    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;

        Player player = playable.getPlayer();

        int itemId = item.getItemId();
        int classId = player.getBaseClassId();

        if (player.isInOlympiadMode()) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
            return false;
        }

        if (player.getLevel() < 76) {
            player.sendMessage(player.isLangRus() ? "Разрешено использовать только с 3-й профессией!" : "Use only a third profession!");
            player.sendMessage(player.isLangRus() ? "Разрешено использовать только на основном классе!" : "Use only on the main class!");
            return false;
        }

        if (player.getActiveClassId() != player.getBaseClassId()) {
            player.sendMessage(player.isLangRus() ? "Разрешено использовать только на основном классе!" : "Use only on the main class!");
            return false;
        }

        switch (classId) {
            case 97://Cardinal
                player.addSkill(SkillTable.getInstance().getInfo(24001, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 98://Hierophant
                player.addSkill(SkillTable.getInstance().getInfo(24002, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 100://SwordMuse
                player.addSkill(SkillTable.getInstance().getInfo(24003, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 105://EvaSaint
                player.addSkill(SkillTable.getInstance().getInfo(24004, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 107://SpectralDancer
                player.addSkill(SkillTable.getInstance().getInfo(24005, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 112://ShillienSaint
                player.addSkill(SkillTable.getInstance().getInfo(24006, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 115://Dominator
                player.addSkill(SkillTable.getInstance().getInfo(24007, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            case 116://Doomcryer
                player.addSkill(SkillTable.getInstance().getInfo(24008, 1), false);
                player.updateStats();
                player.sendPacket(new SkillList(player));
                break;
            default:
                return false;
        }
        return true;
    }
}