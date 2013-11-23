/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SubClass;
import l2p.gameserver.model.actor.instances.player.SubClassInfo;
import l2p.gameserver.model.actor.instances.player.SubClassList;
import l2p.gameserver.model.base.AcquireType;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SubClassTable;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.CertificationFunctions;
import l2p.gameserver.utils.DualCertificationFunctions;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.ItemFunctions;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;

public final class SubClassManagerInstance extends NpcInstance {
    private static final long serialVersionUID = 1L;

    // Предмет: Сертификат на Смену Профессии
    private static final int CERTIFICATE_ID = 30433;

    public SubClassManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        StringTokenizer st = new StringTokenizer(command, "_");
        String cmd = st.nextToken();
        if (cmd.equalsIgnoreCase("subclass")) {
            //Глобальные проверки, которые распространяются на любые операции с саб-классами.
            if (player.getSummonList().size() > 0) {
                showChatWindow(player, "class_manager/" + getNpcId() + "-no_servitor.htm");
                return;
            }

            if (player.getTransformation() != 0) {
                showChatWindow(player, "class_manager/" + getNpcId() + "-no_transform.htm");
                return;
            }

            if (player.getWeightPenalty() >= 3 || player.getInventoryLimit() * 0.8 < player.getInventory().getSize()) {
                showChatWindow(player, "class_manager/" + getNpcId() + "-no_weight.htm");
                return;
            }

            if (player.getLevel() < 40) {
                showChatWindow(player, "class_manager/" + getNpcId() + "-no_level.htm");
                return;
            }

            // Действия с саб-классами.
            String cmd2 = st.nextToken();
            if (cmd2.equalsIgnoreCase("add")) // TODO: [K1mel] Сверить с оффом.
            {
                if (!checkSubClassQuest(player)) {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
                    return;
                }

                if (player.getSubClassList().size() >= SubClassList.MAX_SUB_COUNT) {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-add_no_limit.htm");
                    return;
                }

                // Проверка хватает ли уровня
                Collection<SubClass> subClasses = player.getSubClassList().values();
                for (SubClass subClass : subClasses) {
                    if (subClass.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS) {
                        showChatWindow(player, "class_manager/" + getNpcId() + "-add_no_level.htm", "<?LEVEL?>", Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS);
                        return;
                    }
                }

                if (!st.hasMoreTokens()) {
                    StringBuilder availSubList = new StringBuilder();
                    int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, player.getActiveClassId());
                    for (int subClsId : availSubClasses) {
                        // На оффе оно вбито в npcstring-*.dat. Из-за несоответствия байпассов, рисуем сами.
                        availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_add_").append(subClsId).append("\">").append(HtmlUtils.htmlClassName(subClsId)).append("</a><br>");
                    }
                    showChatWindow(player, "class_manager/" + getNpcId() + "-add_list.htm", "<?ADD_SUB_LIST?>", availSubList.toString());
                } else {
                    int addSubClassId = Integer.parseInt(st.nextToken());
                    if (!st.hasMoreTokens()) {
                        String addSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_add_" + addSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(addSubClassId) + "</a>";
                        showChatWindow(player, "class_manager/" + getNpcId() + "-add_confirm.htm", "<?ADD_SUB_CONFIRM?>", addSubConfirm);
                    } else {
                        String cmd3 = st.nextToken();
                        if (cmd3.equalsIgnoreCase("confirm")) {
                            if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player)) {
                                player.sendPacket(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD);
                                return;
                            }

                            if (player.addSubClass(addSubClassId, true, 0)) {
                                player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
                                showChatWindow(player, "class_manager/" + getNpcId() + "-add_success.htm");
                            } else {
                                showChatWindow(player, "class_manager/" + getNpcId() + "-add_error.htm");
                            }
                        }
                    }
                }
            } else if (cmd2.equalsIgnoreCase("change")) // TODO: [K1mel] Доделать.
            {
                if (!player.getSubClassList().haveSubClasses()) {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
                    return;
                }

                if (ItemFunctions.getItemCount(player, CERTIFICATE_ID) == 0) {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-no_certificate.htm");
                }
            } else if (cmd2.equalsIgnoreCase("cancel")) {
                if (!checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses()) {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
                    return;
                }

                if (checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses()) // TODO: [K1mel] Проверить сообщение на оффе.
                {
                    showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_no_subs.htm");
                    return;
                }

                if (!st.hasMoreTokens()) {
                    StringBuilder mySubList = new StringBuilder();
                    Collection<SubClass> subClasses = player.getSubClassList().values();
                    for (SubClass sub : subClasses) {
                        if (sub == null) // Не может быть, но на всякий пожарный.
                            continue;

                        if (sub.isBase())
                            continue;

                        if (sub.isDouble()) // Двойной саб-класс отменить нельзя.
                            continue;

                        int classId = sub.getClassId();
                        // На оффе оно вбито в npcstring-*.dat. Из-за несоответствия байпассов, рисуем сами.
                        mySubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_").append(classId).append("\">").append(HtmlUtils.htmlClassName(classId)).append("</a><br>");
                    }
                    showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_list.htm", "<?CANCEL_SUB_LIST?>", mySubList.toString());
                } else {
                    int cancelClassId = Integer.parseInt(st.nextToken());
                    if (!st.hasMoreTokens()) {
                        StringBuilder availSubList = new StringBuilder();
                        int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, cancelClassId);
                        for (int subClsId : availSubClasses) {
                            // На оффе оно вбито в npcstring-*.dat. Из-за несоответствия байпассов, рисуем сами.
                            availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_").append(cancelClassId).append("_").append(subClsId).append("\">").append(HtmlUtils.htmlClassName(subClsId)).append("</a><br>");
                        }
                        showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_change_list.htm", "<?CANCEL_CHANGE_SUB_LIST?>", availSubList.toString());
                    } else {
                        int newSubClassId = Integer.parseInt(st.nextToken());
                        if (!st.hasMoreTokens()) {
                            String newSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + newSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(newSubClassId) + "</a>";
                            showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_confirm.htm", "<?CANCEL_SUB_CONFIRM?>", newSubConfirm);
                        } else {
                            String cmd3 = st.nextToken();
                            if (cmd3.equalsIgnoreCase("confirm")) {
                                if (player.modifySubClass(cancelClassId, newSubClassId)) {
                                    player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
                                    showChatWindow(player, "class_manager/" + getNpcId() + "-add_success.htm");
                                } else {
                                    showChatWindow(player, "class_manager/" + getNpcId() + "-add_error.htm");
                                }
                            }
                        }
                    }
                }
            } else if (cmd2.equalsIgnoreCase("CertificationList")) {
                CertificationFunctions.showCertificationList(this, player);
            } else if (cmd2.equalsIgnoreCase("GetCertification65")) {
                CertificationFunctions.getCertification65(this, player);
            } else if (cmd2.equalsIgnoreCase("GetCertification70")) {
                CertificationFunctions.getCertification70(this, player);
            } else if (cmd2.equalsIgnoreCase("GetCertification75")) {
                CertificationFunctions.getCertification75(this, player);
            } else if (cmd2.equalsIgnoreCase("GetCertification80")) {
                CertificationFunctions.getCertification80(this, player);
            } else if (cmd2.equalsIgnoreCase("CertificationSkillList")) {
                showSertifikationSkillList(player, AcquireType.CERTIFICATION);
            } else if (cmd2.equalsIgnoreCase("CertificationCancel")) {
                CertificationFunctions.cancelCertification(this, player);
            } else if (cmd2.equalsIgnoreCase("DualCertificationList")) {
                DualCertificationFunctions.showDualCertificationList(this, player);
            } else if (cmd2.equalsIgnoreCase("GetDualCertification85")) {
                DualCertificationFunctions.getDualCertification85(this, player);
            } else if (cmd2.equalsIgnoreCase("GetDualCertification90")) {
                DualCertificationFunctions.getDualCertification90(this, player);
            } else if (cmd2.equalsIgnoreCase("GetDualCertification95")) {
                DualCertificationFunctions.getDualCertification95(this, player);
            } else if (cmd2.equalsIgnoreCase("GetDualCertification99")) {
                DualCertificationFunctions.getDualCertification99(this, player);
            } else if (cmd2.equalsIgnoreCase("DualCertificationCancel")) {
                DualCertificationFunctions.cancelDualCertification(this, player);
            } else if (cmd2.equalsIgnoreCase("DualCertificationSkillList")) {
                showDualSertifikationSkillList(player, AcquireType.DUALCERTIFICATION);
            }
        } else
            super.onBypassFeedback(player, command);
    }

    public void showSertifikationSkillList(Player player, AcquireType type) {
        if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
            if (!player.isQuestCompleted("_136_MoreThanMeetsTheEye")) {
                showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
                return;
            }
        if (player.getActiveSubClass().isSub()) {
            showChatWindow(player, "villagemaster/certification/33490-certificate-noactiveclass.htm");
            return;
        }
        if (Functions.getItemCount(player, 10280) < 1) {
            showChatWindow(player, "villagemaster/certification/33490-certificate-noitem.htm");
            return;
        }
        showAcquireList(type, player);
    }

    public void showDualSertifikationSkillList(Player player, AcquireType type) {
        if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
            if (!player.isQuestCompleted("_177_StartTheNewDestiny")) {
                showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
                return;
            }
        if (player.getActiveSubClass().isDouble()) {
            showChatWindow(player, "villagemaster/certification/33490-dual_certificate-noactiveclass.htm");
            return;
        }
        if (Functions.getItemCount(player, 36078) < 1) {
            showChatWindow(player, "villagemaster/certification/33490-dual_certificate-noitem.htm");
            return;
        }
        showAcquireList(type, player);
    }

    private static boolean checkSubClassQuest(Player player) {
        return Config.ALT_GAME_SUBCLASS_WITHOUT_QUESTS || player.isQuestCompleted("_10385_RedThreadofFate");
    }
}
