package ai.harnak_4pf;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SkillTable;

import java.util.List;

public class SourceOfPower extends DefaultAI {
    private static final int SKILL_ID = 14625;
    private static final int LIGHT_HEAL_ID = 14736;
    private static final NpcString MSG1 = NpcString.I_HERMUNKUS_GIVE_MY_POWER_TO_THOSE_WHO_FIGHT_FOR_ME;
    private static final NpcString MSG2 = NpcString.THOUGH_SMALL_THIS_POWER_WILL_HELP_YOU_GREATLY;
    private boolean controlNpc;
    private boolean useLightHeal;
    private boolean firstCast;

    public SourceOfPower(NpcInstance actor) {
        super(actor);
        controlNpc = actor.getParameter("ControlNpc", false);
        useLightHeal = actor.getParameter("useLightHeal", false);
        firstCast = true;
    }

    @Override
    protected boolean thinkActive() {
        List<Player> players = World.getAroundPlayers(getActor(), 300, 300);

        if (!players.isEmpty()) {
            Player p = players.get(0);
            Skill skill;
            if (!useLightHeal) {
                skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
                addTaskCast(p, skill);
                if (firstCast) {
                    addTimer(1, 7000);
                    firstCast = false;
                    getActor().broadcastPacket(new ExShowScreenMessage(MSG1,
                            10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE,
                            1, true, 0));
                    if (controlNpc) {
                        Functions.npcSayToPlayer(getActor(), p, NpcString.RRECEIVE_THIS_POWER_FROM_THE_ANCIENT_GIANT, ChatType.TELL);
                        Functions.npcSayToPlayer(getActor(), p, NpcString.USE_THIS_NEW_POWER_WHEN_THE_TIME_IS_RIGHT, ChatType.TELL);
                    }
                } else {
                    getActor().broadcastPacket(new ExShowScreenMessage(MSG2,
                            10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE,
                            1, true, 0));
                }
            } else { // useLightHeal = true
                if (firstCast) {
                    addTimer(2, 100);
                    firstCast = false;
                }
            }
        }
        return super.thinkActive();
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        super.onEvtTimer(timerId, arg1, arg2);
        if (!isActive())
            return;
        if (timerId == 1) {
            getActor().deleteMe();
        } else if (timerId == 2) {
            List<Player> players = World.getAroundPlayers(getActor(), 500, 300);
            if (!players.isEmpty()) {
                Player p = players.get(0);
                Skill skill = SkillTable.getInstance().getInfo(LIGHT_HEAL_ID, 1);
                addTaskCast(p, skill);
                addTimer(2, 3500);
            }
        }
    }
}
