package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.listener.actor.player.impl.MentorAnswerListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.network.serverpackets.ConfirmDlg;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

/**
 * @author Cain
 * @date 12:47/25.04.2012
 */
public class SummonMentor extends Skill {
    public SummonMentor(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!activeChar.isPlayer())
            return false;

        if (activeChar.isPlayer()) {
            Player p = (Player) activeChar;
            if (p.getActiveWeaponFlagAttachment() != null) {
                activeChar.sendPacket(SystemMsg.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD);
                return false;
            }
            if (p.isInDuel() || p.getTeam() != TeamType.NONE) {
                activeChar.sendMessage(new CustomMessage("common.RecallInDuel", p));
                return false;
            }
            if (p.isInOlympiadMode()) {
                activeChar.sendPacket(Msg.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
                return false;
            }
        }

        if (activeChar.isInZone(Zone.ZoneType.no_escape) && activeChar.getReflection() != null && activeChar.getReflection().getCoreLoc() != null) {
            if (activeChar.isPlayer())
                activeChar.sendMessage(new CustomMessage("l2p.gameserver.skills.skillclasses.Recall.Here", (Player) activeChar));
            return false;
        }

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!activeChar.isPlayer())
            return;

        Player player = (Player) activeChar;
        int mentorId = player.getMenteeList().getMentor();
        Player mentor;
        if (mentorId != 0) {
            mentor = World.getPlayer(mentorId);
            if (mentor == null)
                return;
        } else
            return;

        mentor.ask(new ConfirmDlg(SystemMsg.S1, 30000).addString("Teleport to Mentee? " + activeChar.getName()), new MentorAnswerListener(mentor, activeChar.getName()));
    }
}
