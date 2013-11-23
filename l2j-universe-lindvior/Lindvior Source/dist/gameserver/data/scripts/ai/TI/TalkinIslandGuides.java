/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.TI;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.listener.actor.player.OnTeleportListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.NpcSay;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.utils.Location;
import quests._10365_SeekerEscort;

public class TalkinIslandGuides extends DefaultAI implements OnTeleportListener {
    private final static int SEARCHING_MYST_POWER_SOLDIER = 33014;
    private final static int GOING_INTO_REAL_WAR_SOLDIER = 33016;
    private final static int BACKUP_SEEKERS_ASSASSIN = 33204;
    private final static int SEEKER_ESCORT_BLOODHOUND = 32988;
    private final static int RESPECT_YOUR_ELDERS_HANDEMONKEY = 32971;

    private final static NpcString SEARCHING_MYST_POWER_STRING = NpcString.S1_COME_FOLLOW_ME;
    private final static NpcString GOING_INTO_REAL_WAR_STRING = NpcString.S1_COME_FOLLOW_ME;
    private final static NpcString BACKUP_SEEKERS_STRING = NpcString.HEY_KID_HARRY_UP_AND_FOLLOW_ME;
    private final static NpcString SEEKER_ESCORT_BLOODHOUND_STRING = NpcString.CATCH_UP_TO_KING_HES_WAITING;
    private final static NpcString SEEKER_ESCORT_TELETO_STRING = NpcString.YOU_MUST_MOVE_TO_EXPLORATION_AREA_5_IN_ORDER_TO_CONTINUE;
    private final static NpcString SEEKER_ESCORT_FAIL = NpcString.KING_HAS_RETURNED_TO_DEF_RETURN_TO_DEF_AND_START_AGAIN;
    private final static NpcString RESPECT_YOUR_ELDERS_STRING = NpcString.COME_ON_CREEK;

    private final static int[][] SMP_COORDS = {
            {-111416, 255864, -1469},
            {-111915, 255335, -1432},
            {-112271, 254838, -1504},
            {-112209, 254385, -1504},
            {-111527, 254007, -1696},
            {-110773, 253754, -1784}};

    private final static int[][] GRW_COORDS_LEFT = {
            {-110885, 253533, -1776},
            {-111050, 253183, -1776},
            {-111007, 252706, -1832},
            {-110957, 252400, -1928},
            {-110643, 252365, -1976}};

    private final static int[][] GRW_COORDS_RIGHT = {
            {-110618, 253655, -1792},
            {-110296, 253160, -1848},
            {-110271, 253163, -1816},
            {-110156, 252874, -1888},
            {-110206, 252422, -1984}};

    private final static int[][] BS_COORDS = {
            {-117996, 255845, -1320},
            {-117103, 255538, -1296},
            {-115719, 254792, -1504},
            {-114695, 254741, -1528},
            {-114589, 253517, -1528}};

    private final static int[][] SE_COORDS = {
            {-110628, 238359, -2920},
            {-110581, 238930, -2920},
            {-110711, 239262, -2920},
            {-110958, 239505, -2920},
            {-111022, 239669, -2920},
            {-110989, 239911, -2920},
            {-110780, 240165, -2920},
            {-110790, 240522, -2920},
            {-110999, 240602, -2920},
            {-111297, 240382, -2920},
            {-111642, 239879, -2920},
            {-111975, 239699, -2920},
            {-112280, 239783, -2920},
            {-112645, 239914, -2920},
            {-112705, 240236, -2920},
            {-112483, 240516, -2920},
            {-112126, 240498, -2920},
            {-112027, 240300, -2920},
            {-112228, 240123, -2920},};

    private final static int[][] SE_COORDS2 = {
            {-111066, 233798, -3200},
            {-112436, 233701, -3096},
            {-112182, 233490, -3120},
            {-112124, 233130, -3136},
            {-112389, 232931, -3096},
            {-112689, 232566, -3072},
            {-112716, 232359, -3072},
            {-112533, 232054, -3080},
            {-112308, 232084, -3104},
            {-112071, 232359, -3136},
            {-111766, 232566, -3160},
            {-111219, 232723, -3224},
            {-110813, 232482, -3256},
            {-110764, 232124, -3256},
            {-111152, 231842, -3224},
            {-111472, 231976, -3200},
            {-111666, 231951, -3168},
            {-111723, 231830, -3168}};

    private final static int[][] RYE_COORDS = {
            {-116664, 255592, -1455},
            {-116600, 256232, -1488},
            {-116520, 257080, -1539},
            {-116392, 257736, -1537},
            {-115032, 257720, -1163},
            {-114408, 257320, -1163},
            {-114392, 258536, -1225},
            {-114680, 259640, -1225},
            {-114376, 260184, -1224}};

    private NpcString currentString = null;
    private int[][] currentCoords = {};
    private int currentState;
    private boolean autounspawn = true;
    private String displayMessageType = "NPC_SAY";
    private long lastMessage = 0;
    private long lastVisit = 0;
    private boolean configured = false;
    private int coordsCovered = 0;

    public TalkinIslandGuides(NpcInstance actor) {
        super(actor);
        currentState = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        NpcInstance actor = getActor();
        if (actor != null) {
            Creature target = actor.getFollowTarget();
            if (target != null)
                target.removeListener(this);
        }
    }

    @Override
    public void onTeleport(Player player, int x, int y, int z, Reflection reflection) {
        playerTeleported();
        Creature target = getActor().getFollowTarget();
        if (target != null)
            target.removeListener(this);
    }

    private void config() {
        if (configured)
            return;

        configured = true;

        NpcInstance actor = getActor();
        Creature target = actor.getFollowTarget();

        if (target == null) {
            stopAITask();
            actor.deleteMe();
            return;
        }

        target.addListener(this);

        int npcId = actor.getNpcId();
        switch (npcId) {
            case SEARCHING_MYST_POWER_SOLDIER:
                currentCoords = SMP_COORDS;
                currentString = SEARCHING_MYST_POWER_STRING;
                break;
            case BACKUP_SEEKERS_ASSASSIN:
                currentCoords = BS_COORDS;
                currentString = BACKUP_SEEKERS_STRING;
                break;
            case GOING_INTO_REAL_WAR_SOLDIER:
                double distLeft = target.getDistance(GRW_COORDS_LEFT[0][0], GRW_COORDS_LEFT[0][1], GRW_COORDS_LEFT[0][2]);
                double distRight = target.getDistance(GRW_COORDS_RIGHT[0][0], GRW_COORDS_RIGHT[0][1], GRW_COORDS_RIGHT[0][2]);
                if (distLeft <= distRight)
                    currentCoords = GRW_COORDS_LEFT;
                else
                    currentCoords = GRW_COORDS_RIGHT;
                currentString = GOING_INTO_REAL_WAR_STRING;
                break;
            case SEEKER_ESCORT_BLOODHOUND:
                currentCoords = SE_COORDS;
                currentString = SEEKER_ESCORT_BLOODHOUND_STRING;
                autounspawn = false;
                displayMessageType = "SCREEN";
                actor.setTitle(target.getName());
                break;
            case RESPECT_YOUR_ELDERS_HANDEMONKEY:
                currentCoords = RYE_COORDS;
                currentString = RESPECT_YOUR_ELDERS_STRING;
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        Creature target = actor.getFollowTarget();
        int npcId = actor.getNpcId();

        if (target == null) {
            stopAITask();
            actor.deleteMe();
        }

        config();

        actor.setRunning();
        if (actor.getDistance(target) < 200 || currentState == 0) {
            lastVisit = System.currentTimeMillis();
            if (currentState == 0)
                ++coordsCovered;
            if (currentState < currentCoords.length) {
                actor.moveToLocation(currentCoords[currentState][0], currentCoords[currentState][1], currentCoords[currentState][2], Rnd.get(0, 50), true);
                if (actor.getDestination() == null) {
                    // TODO: We need to set heading to turn NPC to player. Seems this is not work properly
                    actor.setHeading(target.getHeading() / 2);
                    ++currentState;
                }
            } else {
                if (autounspawn)
                    actor.deleteMe();

                currentString = SEEKER_ESCORT_TELETO_STRING;
                switch (npcId) {
                    case SEEKER_ESCORT_BLOODHOUND:
                        if (coordsCovered >= 2) {
                            // Yeah, this is awesome >_< Very bad to set quest states from another classes, but we need that.
                            QuestState st = target.getPlayer().getQuestState(_10365_SeekerEscort.class);
                            if (st != null)
                                ((_10365_SeekerEscort) st.getQuest()).bloodhoundEscorted(st);
                        }
                        break;
                }
            }
        } else {
            // Remove NPC if player not visit it during 1 minute
            if (System.currentTimeMillis() - lastVisit > 60000) {
                switch (npcId) {
                    case SEEKER_ESCORT_BLOODHOUND:
                        sendMessage(SEEKER_ESCORT_FAIL);
                        break;
                }
                stopAITask();
                actor.deleteMe();
            }
            // Don't spam messages
            if (System.currentTimeMillis() - lastMessage > 2000) {
                sendMessage(currentString);
                lastMessage = System.currentTimeMillis();
            }
        }

        return true;
    }

    private void sendMessage(NpcString message) {
        NpcInstance actor = getActor();
        Creature target = actor.getFollowTarget();

        if (displayMessageType.equals("NPC_SAY"))
            actor.broadcastPacket(new NpcSay(actor, ChatType.NPC_SAY, message, target.getName()));
        else if (displayMessageType.equals("SCREEN"))
            target.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
    }

    public void playerTeleported() {
        /**
         * TODO: We need teleport player to another location.
         * This is strange, but player teleported not to the current 5th area location.
         * He's teleported to upstair, neer teleporter.
         */
        int npcId = getActor().getNpcId();
        switch (npcId) {
            case SEEKER_ESCORT_BLOODHOUND:
                if (coordsCovered >= 1) {
                    currentCoords = SE_COORDS2;
                    currentString = SEEKER_ESCORT_BLOODHOUND_STRING;
                    currentState = 0;
                    autounspawn = true;
                    getActor().teleToLocation(new Location(SE_COORDS2[0][0], SE_COORDS2[0][1], SE_COORDS2[0][2]));
                }
                break;
        }
    }

    /**
     * These NPC does not have random walk
     */
    @Override
    protected boolean randomWalk() {
        return false;
    }
}