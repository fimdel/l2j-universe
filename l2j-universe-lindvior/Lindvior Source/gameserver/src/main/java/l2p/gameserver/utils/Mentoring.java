/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.utils;

import l2p.gameserver.database.mysql;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.actor.instances.player.Mentee;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.network.serverpackets.ExNoticePostArrived;
import l2p.gameserver.network.serverpackets.SkillList;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:45
 */
public class Mentoring {
    public static final int MENTEE_MARKS = 33804;
    public static final Map<Integer, Integer> SIGN_OF_TUTOR = new HashMap<Integer, Integer>();

    public static int skillForMentee = 9379;

    public static int[] effectsForMentee = {9227, 9228, 9229, 9230, 9231, 9232, 9233};

    public static int[] skillsForMentor = {9376, 9377, 9378};

    public static int effectForMentor = 9256;

    public static void removeSkills(Player activeChar) {
        for (int skillToRemove : skillsForMentor)
            activeChar.removeSkill(skillToRemove);
        activeChar.removeSkill(skillForMentee);
    }

    public static void addEffectToPlayer(Skill skill, Player target) {
        for (EffectTemplate et : skill.getEffectTemplates()) {
            Env env = new Env(target, target, skill);
            Effect effect = et.getEffect(env);
            target.getEffectList().addEffect(effect);
        }
    }

    public static void removeConditionsFromMentee(Player player) {
        for (Mentee mentee : player.getMenteeList().getMentees()) {
            Player activeChar = World.getPlayer(mentee.getObjectId());
            if (activeChar != null)
                removeEffectsFromPlayer(activeChar);
        }
    }

    public static void sendMentorMail(Player receiver, int level) {
        if ((receiver == null) || (!receiver.isOnline())) {
            return;
        }
        if (!SIGN_OF_TUTOR.containsKey(level)) {
            return;
        }
        Mail mail = new Mail();
        mail.setSenderId(1);
        mail.setSenderName("Помощник Наставника");
        mail.setReceiverId(receiver.getObjectId());
        mail.setReceiverName(receiver.getName());
        mail.setTopic("Знак Наставника, полученный за повышение уровня");
        mail.setBody("Ваш Ученик {1} {0} достиг необходимого уровня, поэтому мы высылаем Вам знак Наставника. Когда получите это письмо, заберите предмет, а само письмо удалите. Если почтовый ящик переполнится, предмет не будет доставлен. Запомните это.".replace("{0}", String.valueOf(receiver.getLevel())).replace("{1}", receiver.getName()));

        ItemInstance item = null;

        item = ItemFunctions.createItem(MENTEE_MARKS);

        item.setCount(SIGN_OF_TUTOR.get(level));
        item.setLocation(ItemInstance.ItemLocation.MAIL);
        item.save();

        mail.addAttachment(item);

        mail.setType(Mail.SenderType.MENTOR);
        mail.setUnread(true);
        mail.setExpireTime(2592000 + (int) (System.currentTimeMillis() / 1000L));
        mail.save();

        receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
        receiver.sendPacket(SystemMessageId.THE_MAIL_HAS_ARRIVED);
    }

    public static void removeConditions(Player player) {
        if (player.isMentor()) {
            removeConditionsFromMentee(player);
            removeEffectsFromPlayer(player);
        } else if ((!player.isMentor()) && (World.getPlayer(player.getMenteeList().getMentor()) != null)) {
            removeEffectsFromPlayer(player);
            Player mentor = World.getPlayer(player.getMenteeList().getMentor());
            int menteeOnline = 0;
            for (Mentee mentee : mentor.getMenteeList().getMentees()) {
                if (mentee.isOnline())
                    menteeOnline++;
            }
            if (menteeOnline < 1)
                removeEffectsFromPlayer(mentor);
        }
    }

    public static void removeEffectsFromPlayer(Player activeChar) {
        for (int buff : effectsForMentee)
            activeChar.getEffectList().stopEffect(buff);
        activeChar.getEffectList().stopEffect(effectForMentor);
    }

    public static void setTimePenalty(int mentorId, long timeTo, long expirationTime) {
        Player mentor = World.getPlayer(mentorId);
        if ((mentor != null) && (mentor.isOnline()))
            mentor.setVar("mentorPenalty", timeTo, -1L);
        else
            mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (?,'user-var','mentorPenalty',?,?)", new Object[]{Integer.valueOf(mentorId), Long.valueOf(timeTo), Long.valueOf(expirationTime)});
    }

    public static void applyMentoringConditions(Player activeChar) {
        if (activeChar.isMentor()) {
            addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), activeChar);

            for (Mentee menteeInfo : activeChar.getMenteeList().getMentees()) {
                if (menteeInfo.isOnline()) {
                    Player mentee = World.getPlayer(menteeInfo.getObjectId());
                    for (int effect : effectsForMentee)
                        addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), mentee);
                }
            }
        } else {
            for (int effect : effectsForMentee) {
                addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), activeChar);
            }
            if (World.getPlayer(activeChar.getMenteeList().getMentor()) != null) {
                addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), World.getPlayer(activeChar.getMenteeList().getMentor()));
            }
        }
    }

    public static void addSkillsToMentor(Player mentor) {
        if (mentor.getMenteeList().getMentor() == 0) {
            for (int skillId : skillsForMentor) {
                Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
                mentor.addSkill(skill, true);
                mentor.sendPacket(new SkillList(mentor));
            }
        }
    }

    public static void addSkillsToMentee(Player mentee) {
        if (mentee.getMenteeList().getMentor() != 0) {
            Skill skill = SkillTable.getInstance().getInfo(skillForMentee, 1);
            mentee.addSkill(skill, true);
            mentee.sendPacket(new SkillList(mentee));
        }
    }
}
