package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.MagicSkillUse;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SkillTable;

public class FortuneBug extends DefaultAI {
    private static final int MAX_RADIUS = 500;
    private static final int ItemName_A = 57;
    private static final int ItemName_B_1 = 1881;
    private static final int ItemName_B_2 = 1890;
    private static final int ItemName_B_3 = 1880;
    private static final int ItemName_B_4 = 729;
    private static final Skill s_display_bug_of_fortune1 = SkillTable.getInstance().getInfo(6045, 1);
    private static final Skill s_display_jackpot_firework = SkillTable.getInstance().getInfo(5778, 1);

    private long _nextEat;
    private int i_ai0, i_ai1, i_ai2;

    public FortuneBug(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        addTimer(7778, 1000);
        i_ai0 = i_ai1 = i_ai2 = 0;
    }

    @Override
    protected void onEvtArrived() {
        super.onEvtArrived();
        NpcInstance actor = getActor();
        if (actor == null)
            return;

        ItemInstance closestItem = null;
        if (_nextEat < System.currentTimeMillis()) {
            for (GameObject obj : World.getAroundObjects(actor, 20, 200))
                if (obj.isItem() && ((ItemInstance) obj).isStackable())
                    closestItem = (ItemInstance) obj;

            if (closestItem != null) {
                closestItem.deleteMe();
                actor.altUseSkill(s_display_bug_of_fortune1, actor);
                Functions.npcSayInRange(actor, 600, NpcString.YUMYUM_YUMYUM);

                i_ai0++;
                if (i_ai0 > 1 && i_ai0 <= 10)
                    i_ai1 = 1;
                else if (i_ai0 > 10 && i_ai0 <= 100)
                    i_ai1 = 2;
                else if (i_ai0 > 100 && i_ai0 <= 500)
                    i_ai1 = 3;
                else if (i_ai0 > 500 && i_ai0 <= 1000)
                    i_ai1 = 4;
                if (i_ai0 > 1000)
                    i_ai1 = 5;

                switch (i_ai1) {
                    case 0:
                        i_ai2 = 0;
                        break;
                    case 1:
                        if (Rnd.get(100) < 10)
                            i_ai2 = 2;
                        else if (Rnd.get(100) < 15)
                            i_ai2 = 3;
                        else
                            i_ai2 = 1;
                        break;
                    case 2:
                        if (Rnd.get(100) < 10)
                            i_ai2 = 3;
                        else if (Rnd.get(100) < 15)
                            i_ai2 = 4;
                        else
                            i_ai2 = 2;
                        break;
                    case 3:
                        if (Rnd.get(100) < 10)
                            i_ai2 = 4;
                        else
                            i_ai2 = 3;
                        break;
                    case 4:
                        if (Rnd.get(100) < 10)
                            i_ai2 = 3;
                        else
                            i_ai2 = 4;
                        break;
                }

                _nextEat = System.currentTimeMillis() + 10000;
            }
        }
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null || actor.isDead())
            return true;

        if (!actor.isMoving && _nextEat < System.currentTimeMillis()) {
            ItemInstance closestItem = null;
            for (GameObject obj : World.getAroundObjects(actor, MAX_RADIUS, 200))
                if (obj.isItem() && ((ItemInstance) obj).isStackable())
                    closestItem = (ItemInstance) obj;

            if (closestItem != null)
                actor.moveToLocation(closestItem.getLoc(), 0, true);
        }

        return false;
    }

    @Override
    protected void onEvtDead(Creature killer) {
        super.onEvtDead(killer);
        NpcInstance actor = getActor();
        if (actor == null)
            return;

        if (killer != null) {
            if (i_ai2 == 0)
                Functions.npcSayInRange(actor, 600, NpcString.I_HAVENT_EATEN_ANYTHING_IM_SO_WEAK);
            else
                actor.broadcastPacket(new MagicSkillUse(actor, s_display_jackpot_firework.getId(), 1, s_display_jackpot_firework.getHitTime(), 0));

            int i0, i1;
            switch (i_ai2) {
                case 1:
                    i0 = 695;
                    i1 = 2245;
                    actor.dropItem(killer.getPlayer(), ItemName_A, i0 + Rnd.get(i1 - i0));
                    break;
                case 2:
                    i0 = 3200;
                    i1 = 8400;
                    actor.dropItem(killer.getPlayer(), ItemName_A, i0 + Rnd.get(i1 - i0));
                    break;
                case 3:
                    i0 = 7;
                    i1 = 17;
                    actor.dropItem(killer.getPlayer(), ItemName_B_1, i0 + Rnd.get(i1 - i0));
                    i0 = 1;
                    i1 = 1;
                    actor.dropItem(killer.getPlayer(), ItemName_B_2, i0);
                    i0 = 7;
                    i1 = 17;
                    actor.dropItem(killer.getPlayer(), ItemName_B_3, i0 + Rnd.get(i1 - i0));
                    break;
                case 4:
                    i0 = 15;
                    i1 = 45;
                    actor.dropItem(killer.getPlayer(), ItemName_B_1, i0 + Rnd.get(i1 - i0));
                    i0 = 10;
                    i1 = 20;
                    actor.dropItem(killer.getPlayer(), ItemName_B_2, i0 + Rnd.get(i1 - i0));
                    i0 = 15;
                    i1 = 45;
                    actor.dropItem(killer.getPlayer(), ItemName_B_3, i0 + Rnd.get(i1 - i0));
                    if (Rnd.get(100) < 10)
                        actor.dropItem(killer.getPlayer(), ItemName_B_4, 1);
                    break;
            }
        }
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        NpcInstance actor = getActor();
        if (actor == null)
            return;

        if (timerId == 7778) {
            switch (i_ai0) {
                case 0:
                    Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IF_YOU_HAVE_ITEMS_PLEASE_GIVE_THEM_TO_ME : NpcString.MY_STOMACH_IS_EMPTY);
                    break;
                case 1:
                    Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IM_HUNGRY_IM_HUNGRY : NpcString.IM_STILL_NOT_FULL);
                    break;
                case 2:
                    Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IM_STILL_HUNGRY : NpcString.I_FEEL_A_LITTLE_WOOZY);
                    break;
                case 3:
                    Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.GIVE_ME_SOMETHING_TO_EAT : NpcString.NOW_ITS_TIME_TO_EAT);
                    break;
                case 4:
                    Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.I_ALSO_NEED_A_DESSERT : NpcString.IM_STILL_HUNGRY_);
                    break;
                case 5:
                    Functions.npcSayInRange(actor, 600, NpcString.IM_FULL_NOW_I_DONT_WANT_TO_EAT_ANYMORE);
                    break;
            }
            addTimer(7778, 10000 + Rnd.get(10) * 1000);
        } else
            super.onEvtTimer(timerId, arg1, arg2);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
    }

    @Override
    protected void onEvtAggression(Creature target, int aggro) {
    }
}
