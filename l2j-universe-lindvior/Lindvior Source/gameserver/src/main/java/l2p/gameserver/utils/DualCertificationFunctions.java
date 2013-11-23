/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.utils;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.SkillAcquireHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.SkillLearn;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.base.AcquireType;
import l2p.gameserver.model.base.ClassType2;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;

import java.util.Collection;

public class DualCertificationFunctions {
    public static final String PATH = "villagemaster/certification/";

    public static void showDualCertificationList(NpcInstance npc, Player player) {
        if (!checkConditions(85, npc, player, true))
            return;

        Functions.show(PATH + "33490-dual_certificatelist.htm", player, npc);
    }

    public static void getDualCertification85(NpcInstance npc, Player player) {
        if (!checkConditions(85, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();
        if (clzz.isCertificationGet(SubClass.CERTIFICATION_85)) {
            Functions.show(PATH + "33490-dual_certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 36078, 1);
        clzz.addCertification(SubClass.CERTIFICATION_85);
        player.store(true);
    }

    public static void getDualCertification90(NpcInstance npc, Player player) {
        if (!checkConditions(90, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_85)) {
            Functions.show(PATH + "33490-dual_certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_90)) {
            Functions.show(PATH + "33490-dual_certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 36078, 1);
        clzz.addCertification(SubClass.CERTIFICATION_90);
        player.store(true);
    }

    public static void getDualCertification95(NpcInstance npc, Player player) {
        if (!checkConditions(95, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_90)) {
            Functions.show(PATH + "33490-dual_certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_95)) {
            Functions.show(PATH + "33490-dual_certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 36078, 1);
        clzz.addCertification(SubClass.CERTIFICATION_95);
        player.store(true);
    }

    public static void getDualCertification99(NpcInstance npc, Player player) {
        if (!checkConditions(99, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_95)) {
            Functions.show(PATH + "33490-dual_certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_99)) {
            Functions.show(PATH + "33490-dual_certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 36078, 1);
        clzz.addCertification(SubClass.CERTIFICATION_99);
        player.store(true);
    }

    public static void cancelDualCertification(NpcInstance npc, Player player) {
        if (player.getInventory().getAdena() < 10000000) {
            player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            return;
        }

        if (!player.getActiveSubClass().isBase())
            return;

        player.getInventory().reduceAdena(10000000);

        for (ClassType2 classType2 : ClassType2.VALUES) {
            player.getInventory().destroyItemByItemId(classType2.getCertificateId(), player.getInventory().getCountOf(classType2.getCertificateId()));
            player.getInventory().destroyItemByItemId(classType2.getTransformationId(), player.getInventory().getCountOf(classType2.getTransformationId()));
        }

        Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.DUALCERTIFICATION, false);
        for (SkillLearn learn : skillLearnList) {
            Skill skill = player.getKnownSkill(learn.getId());
            if (skill != null)
                player.removeSkill(skill, true);
        }

        for (SubClass subClass : player.getSubClassList().values())
            if (!subClass.isBase())
                subClass.setCertification(0);

        player.sendSkillList();
        Functions.show(new CustomMessage("scripts.services.SubclassSkills.SkillsDeleted", player), player);
    }

    public static boolean checkConditions(int level, NpcInstance npc, Player player, boolean first) {
        if (player.getLevel() < level) {
            Functions.show(PATH + "33490-dual_certificate-nolevel.htm", player, npc, "%level%", level);
            return false;
        }

        if (player.getActiveSubClass().isBase()) {
            Functions.show(PATH + "33490-dual_certificate-nosub.htm", player, npc);
            return false;
        }

        if (first)
            return true;

        for (ClassType2 type : ClassType2.VALUES)
            if (player.getInventory().getCountOf(type.getCertificateId()) > 0 || player.getInventory().getCountOf(type.getTransformationId()) > 0) {
                Functions.show(PATH + "33490-dual_certificate-already.htm", player, npc);
                return false;
            }

        return true;
    }
}
