package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Skill.SkillType;
import l2p.gameserver.model.items.attachment.FlagItemAttachment;
import l2p.gameserver.network.serverpackets.ExAlterSkillRequest;
import l2p.gameserver.tables.SkillTable;
import org.apache.commons.lang3.ArrayUtils;

public class RequestMagicSkillUse extends L2GameClientPacket {
    private Integer _magicId;
    private boolean _ctrlPressed;
    private boolean _shiftPressed;

    /**
     * packet type id 0x39
     * format:		cddc
     */
    @Override
    protected void readImpl() {
        _magicId = readD();
        _ctrlPressed = readD() != 0;
        _shiftPressed = readC() != 0;
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        activeChar.setActive();

        if (activeChar.isOutOfControl()) {
            activeChar.sendActionFailed();
            return;
        }
        boolean isChangeClass = ArrayUtils.contains(new int[]{Skill.CHANGE_CLASS_SKILLS}, _magicId);
        Skill skill = SkillTable.getInstance().getInfo(_magicId, activeChar.getSkillLevel(_magicId));
        if (skill != null) {
            if (!(skill.isActive() || skill.isToggle()))
                return;

            FlagItemAttachment attachment = activeChar.getActiveWeaponFlagAttachment();
            if (attachment != null && !attachment.canCast(activeChar, skill)) {
                activeChar.sendActionFailed();
                return;
            }

            // В режиме трансформации доступны только скилы трансформы
            if (!isChangeClass && activeChar.getTransformation() != 0 && !activeChar.getAllSkills().contains(skill))
                return;

            if (activeChar.isInAlterMode() && !skill.isAlterSkill())  {
                activeChar.setIsInAlterMode(false);
                activeChar.sendPacket(new ExAlterSkillRequest(skill.getId(), skill.getAlterSkillId(), 5));
            }

            if (skill.isToggle())
                if (activeChar.getEffectList().getEffectsBySkill(skill) != null) {
                    activeChar.getEffectList().stopEffect(skill.getId());
                    activeChar.sendActionFailed();
                    return;
                }

            if (skill.getSkillType() == SkillType.EMDAM) {
                int inc = 0;
                if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_FIRE) != null)
                    inc = 1;
                else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_WATER) != null)
                    inc = 2;
                else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_WIND) != null)
                    inc = 3;
                else if (activeChar.getEffectList().getEffectsBySkillId(Skill.SKILL_TRUE_EARTH) != null)
                    inc = 4;

                skill = SkillTable.getInstance().getInfo(skill.getId() + inc, skill.getLevel());
                activeChar.setDoubleCastingNow(true);
            }
            Creature target = skill.getAimingTarget(activeChar, activeChar.getTarget());

            activeChar.setGroundSkillLoc(null);
            activeChar.getAI().Cast(skill, target, _ctrlPressed, _shiftPressed);
        } else
            activeChar.sendActionFailed();
    }
}
