/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.util.Rnd;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.base.EnchantSkillLearn;
import l2p.gameserver.network.serverpackets.ExEnchantSkillInfo;
import l2p.gameserver.network.serverpackets.ExEnchantSkillResult;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.tables.SkillTreeTable;
import l2p.gameserver.utils.Log;

/**
 * Format (ch) dd
 */
public final class RequestExEnchantSkillSafe extends L2GameClientPacket {
    private int _skillId;
    private int _skillLvl;

    @Override
    protected void readImpl() {
        _skillId = readD();
        _skillLvl = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.getTransformation() != 0) {
            activeChar.sendMessage("You must leave transformation mode first.");
            return;
        }

        if (activeChar.getLevel() < 76 || activeChar.getClassLevel() < 3) {
            activeChar.sendMessage("You must have 3rd class change quest completed.");
            return;
        }

        EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);

        if (sl == null)
            return;

        int slevel = activeChar.getSkillLevel(_skillId);
        if (slevel == -1)
            return;

        int enchantLevel = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel());

        // already knows the skill with this level
        if (slevel >= enchantLevel)
            return;

        // Можем ли мы перейти с текущего уровня скилла на данную заточку
        if (slevel == sl.getBaseLevel() ? _skillLvl % 100 != 1 : slevel != enchantLevel - 1) {
            activeChar.sendMessage("Incorrect enchant level.");
            return;
        }

        Skill skill = SkillTable.getInstance().getInfo(_skillId, enchantLevel);
        if (skill == null)
            return;

        int[] cost = sl.getCost();
        int requiredSp = cost[1] * SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
        int requiredAdena = cost[0] * SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER * sl.getCostMult();

        int rate = sl.getRate(activeChar);

        if (activeChar.getSp() < requiredSp) {
            sendPacket(Msg.SP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT);
            return;
        }

        if (activeChar.getAdena() < requiredAdena) {
            sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            return;
        }
        if (_skillId < 10000) {
            if (Functions.getItemCount(activeChar, SkillTreeTable.SAFE_ENCHANT_BOOK) == 0) {
                activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
                return;
            }
            Functions.removeItem(activeChar, SkillTreeTable.SAFE_ENCHANT_BOOK, 1);
        } else if (_skillId >= 10000) {
            if (Functions.getItemCount(activeChar, SkillTreeTable.NEW_SAFE_ENCHANT_BOOK) == 0) {
                activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
                return;
            }
            Functions.removeItem(activeChar, SkillTreeTable.NEW_SAFE_ENCHANT_BOOK, 1);
        }

        if (Rnd.chance(rate)) {
            activeChar.addSkill(skill, true);
            activeChar.addExpAndSp(0, -1 * requiredSp);
            Functions.removeItem(activeChar, 57, requiredAdena);
            activeChar.sendPacket(new SystemMessage(SystemMessage.SP_HAS_DECREASED_BY_S1).addNumber(requiredSp), new SystemMessage(SystemMessage.SUCCEEDED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(1));
            activeChar.sendSkillList();
            RequestExEnchantSkill.updateSkillShortcuts(activeChar, _skillId, _skillLvl);
            Log.add(activeChar.getName() + "|Successfully safe enchanted|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
        } else {
            activeChar.sendPacket(new SystemMessage(SystemMessage.Skill_enchant_failed_Current_level_of_enchant_skill_S1_will_remain_unchanged).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(0));
            Log.add(activeChar.getName() + "|Failed to safe enchant|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
        }

        activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)));
    }
}