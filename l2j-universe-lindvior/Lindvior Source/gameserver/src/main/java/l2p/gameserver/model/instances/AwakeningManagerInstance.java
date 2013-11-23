/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.instances;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;

import java.util.StringTokenizer;

public final class AwakeningManagerInstance extends NpcInstance {
    private static final long serialVersionUID = 1L;
    public static final int SCROLL_OF_AFTERLIFE = 17600;
    public static final int STONE_OF_DESTINY = 17722;
    public static final int STONE_OF_AWEKENING = 32227;
    private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;
    private static final int[] SECOND_CLASS_ESSENCE_COMPENSATION = {0, 0, 0, 0, 1, 1, 2, 3, 4, 5, 6, 7, 9, 10, 12, 13, 15, 17, 19, 22, 24, 27, 29, 32, 35, 42, 45, 48, 63, 70, 83};

    private static final int[] THIRD_CLASS_ESSENCE_COMPENSATION = {0, 0, 0, 0, 1, 1, 2, 3, 4, 5, 7, 9, 10, 19, 24, 35};
    private static final String AWEKENING_REQUEST_VAR = "@awakening_request";
    public static final Location TELEPORT_LOC = new Location(-114962, 226564, -2864);

    public AwakeningManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public void showChatWindow(Player player, int val, Object[] arg) {
        if (val == 0) {
            if (player == null) {
                return;
            }
            if (!player.getClassId().isOfLevel(ClassLevel.THIRD) || player.getLevel() < 85 || ItemFunctions.getItemCount(player, SCROLL_OF_AFTERLIFE) < 1) {
                showChatWindow(player, "default/" + getNpcId() + "-no.htm");
                return;
            }

            if (!checkClassIdForNpc(player)) {
                showChatWindow(player, "default/" + getNpcId() + "-no_class.htm");
                return;
            }

            for (ClassId cl : getClassIdByNpc()) {
                if (ItemFunctions.getItemCount(player, STONE_OF_AWEKENING) > 0 && player.getVarInt(getAwakeningRequestVar(player.getClassId())) == cl.getId()) {
                    player.sendPacket(new ExChangeToAwakenedClass(player, this, cl.getId()));
                    return;
                }
            }
        }
        super.showChatWindow(player, val, arg);
    }

    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }
        StringTokenizer st = new StringTokenizer(command, "_");
        String cmd = st.nextToken();
        if (cmd.equalsIgnoreCase("awakening")) {
            if (!st.hasMoreTokens()) {
                return;
            }
            if (!player.getClassId().isOfLevel(ClassLevel.THIRD) || player.getLevel() < 85 || ItemFunctions.getItemCount(player, SCROLL_OF_AFTERLIFE) < 1) {
                return;
            }
            if (!checkClassIdForNpc(player)) {
                return;
            }
            if (player.getSummonList().size() > 0) {
                showChatWindow(player, "default/" + getNpcId() + "-no_summon.htm");
                return;
            }

            int steep = Integer.parseInt(st.nextToken());
            if (steep == 0) {
                int[] compensation = calcCompensation(player);
                showChatWindow(player, "default/awakening_compensation.htm", "<?SP?>", compensation[0], "<?ESSENCE?>", compensation[1]);
                return;
            }
            if (steep == 1) {
                /* ClassId[] awakedClassId = getClassIdByNpc();
          ClassId awakedParentClassId = awakedClassId.getAwakeParentId(player.getClassId());
          Collection<SkillLearn> availableSkills = SkillAcquireHolder.getInstance().getAwakeParentSkillTree(awakedClassId, awakedParentClassId);
          StringBuilder skillList = new StringBuilder();
          for (SkillLearn sl : availableSkills) {
              if (sl != null) {
                  Skill skill = SkillTable.getInstance().getInfo(sl.getId(), sl.getLevel());
                  if (skill != null) {
                      skillList.append("<table><tr><td width=40 height=40><img src=\"").append(skill.getIcon()).append("\" width=32 height=32></td><td width=200>").append(skill.getName(player)).append("</td></tr></table>");
                  }
              }
          }      */
                StringBuilder skillList = new StringBuilder();
                showChatWindow(player, "default/awakening_skill_training.htm", "<?SKILL_LIST?>", skillList.toString());
                return;
            }
            if (steep == 2) {
                if (player.getVarInt(getAwakeningRequestVar(player.getClassId())) > 0) {
                    return;
                }
                ItemFunctions.addItem(player, STONE_OF_AWEKENING, 1L, true);
                int[] compensation = calcCompensation(player);
                addExpAndSp(0L, compensation[0]);
                ItemFunctions.addItem(player, ESSENCE_OF_THE_LESSER_GIANTS, compensation[1], true);
                for (ClassId cl : getClassIdByNpc()) {
                    player.setVar(getAwakeningRequestVar(player.getClassId()), String.valueOf(cl.getId()), -1L);
                    player.sendPacket(new ExChangeToAwakenedClass(player, this, cl.getId()));
                }
            }
        } else {
            super.onBypassFeedback(player, command);
        }
    }

    private static int[] calcCompensation(Player player) {
        if (player == null) {
            return new int[]{0, 0};
        }
        int spCompensation = 0;

        int essenceCompensation = 0;
        for (Skill skill : player.getAllSkillsArray()) {
            if (skill != null) {
                if (skill.getEnchantLevel() >= 1) {
                    int[] compensation_list = skill.getEnchantGrade() == Skill.EnchantGrade.THIRD ? THIRD_CLASS_ESSENCE_COMPENSATION : SECOND_CLASS_ESSENCE_COMPENSATION;
                    essenceCompensation += compensation_list[java.lang.Math.min(compensation_list.length - 1, skill.getEnchantLevel())];
                }
            }
        }
        return new int[]{spCompensation, essenceCompensation};
    }

    private boolean checkClassIdForNpc(Player player) {
        if (ItemFunctions.getItemCount(player, STONE_OF_DESTINY) > 0) {
            return true;
        }
        ClassId[] awakedClassId = getClassIdByNpc();
        for (ClassId cl : awakedClassId) {
            return cl.childOf(player.getClassId());
        }
        return false;
    }

    public ClassId[] getClassIdByNpc() {
        switch (getNpcId()) {
            case 33404:
                return new ClassId[]{ClassId.AeoreHealer_Cardinal, ClassId.AeoreHealer_EvaSaint, ClassId.AeoreHealer_ShillenSaint};
            case 33397:
                return new ClassId[]{ClassId.SigelKnight_PhoenixKnight, ClassId.SigelKnight_HellKnight, ClassId.SigelKnight_EvaTemplar, ClassId.SigelKnight_ShillienTemplar};
            case 33400:
                return new ClassId[]{ClassId.YulArcher_Saggitarius, ClassId.YulArcher_MoonlightSentinel, ClassId.YulArcher_GhostSentinel, ClassId.YulArcher_Trickster};
            case 33402:
                return new ClassId[]{ClassId.IssEnchanter_Hierophant, ClassId.IssEnchanter_SwordMuse, ClassId.IssEnchanter_SpectralDancer, ClassId.IssEnchanter_Dominator, ClassId.IssEnchanter_Doomcryer};
            case 33399:
                return new ClassId[]{ClassId.OthellRogue_Adventurer, ClassId.OthellRogue_WindRider, ClassId.OthellRogue_GhostHunter, ClassId.OthellRogue_FortuneSeeker};
            case 33398:
                return new ClassId[]{ClassId.TyrrWarrior_Duelist, ClassId.TyrrWarrior_GrandKhavatari, ClassId.TyrrWarrior_Titan, ClassId.TyrrWarrior_Maestro, ClassId.TyrrWarrior_Doombringer, ClassId.TyrrWarrior_Dreadnought};
            case 33401:
                return new ClassId[]{ClassId.FeohWizard_Archmage, ClassId.FeohWizard_Soultaker, ClassId.FeohWizard_MysticMuse, ClassId.FeohWizard_StormScreamer, ClassId.FeohWizard_SOUL_HOUND};
            case 33403:
                return new ClassId[]{ClassId.WynnSummoner_ArcanaLord, ClassId.WynnSummoner_ElementalMaster, ClassId.WynnSummoner_SpectralMaster};
        }
        return null;
    }

    public static String getAwakeningRequestVar(ClassId classId) {
        return "@awakening_request_" + classId.getId();
    }
}