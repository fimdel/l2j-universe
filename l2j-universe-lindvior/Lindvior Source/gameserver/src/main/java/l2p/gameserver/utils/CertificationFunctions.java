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

/**
 * @author VISTALL
 * @date 17:49/08.12.2010
 */
public class CertificationFunctions {
    public static final String PATH = "villagemaster/certification/";

    public static void showCertificationList(NpcInstance npc, Player player) {
        if (!checkConditions(65, npc, player, true))
            return;

        Functions.show(PATH + "33490-certificatelist.htm", player, npc);
    }

    public static void showDualCertificationList(NpcInstance npc, Player player) {
        if (!checkConditions(85, npc, player, true))
            return;

        Functions.show(PATH + "33490-dual_certificatelist.htm", player, npc);
    }

    public static void getCertification65(NpcInstance npc, Player player) {
        if (!checkConditions(65, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();
        if (clzz.isCertificationGet(SubClass.CERTIFICATION_65)) {
            Functions.show(PATH + "33490-certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 10280, 1);
        clzz.addCertification(SubClass.CERTIFICATION_65);
        player.store(true);
    }

    public static void getCertification70(NpcInstance npc, Player player) {
        if (!checkConditions(70, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_65)) {
            Functions.show(PATH + "33490-certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_70)) {
            Functions.show(PATH + "33490-certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 10280, 1);
        clzz.addCertification(SubClass.CERTIFICATION_70);
        player.store(true);
    }

    public static void getCertification75(NpcInstance npc, Player player) {
        if (!checkConditions(75, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_70)) {
            Functions.show(PATH + "33490-certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_75)) {
            Functions.show(PATH + "33490-certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 10280, 1);
        clzz.addCertification(SubClass.CERTIFICATION_75);
        player.store(true);
    }

    public static void getCertification80(NpcInstance npc, Player player) {
        if (!checkConditions(75, npc, player, false))
            return;

        SubClass clzz = player.getActiveSubClass();

        // если не взят преведущий сертификат
        if (!clzz.isCertificationGet(SubClass.CERTIFICATION_75)) {
            Functions.show(PATH + "33490-certificate-fail.htm", player, npc);
            return;
        }

        if (clzz.isCertificationGet(SubClass.CERTIFICATION_80)) {
            Functions.show(PATH + "33490-certificate-already.htm", player, npc);
            return;
        }

        Functions.addItem(player, 10280, 1);
        clzz.addCertification(SubClass.CERTIFICATION_80);
        player.store(true);
    }

    public static void cancelCertification(NpcInstance npc, Player player) {
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

        Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.CERTIFICATION, false);
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
            Functions.show(PATH + "33490-certificate-nolevel.htm", player, npc, "%level%", level);
            return false;
        }

        if (player.getActiveSubClass().isBase()) {
            Functions.show(PATH + "33490-certificate-nosub.htm", player, npc);
            return false;
        }

        if (first)
            return true;

        for (ClassType2 type : ClassType2.VALUES)
            if (player.getInventory().getCountOf(type.getCertificateId()) > 0 || player.getInventory().getCountOf(type.getTransformationId()) > 0) {
                Functions.show(PATH + "33490-certificate-already.htm", player, npc);
                return false;
            }

        return true;
    }
}
