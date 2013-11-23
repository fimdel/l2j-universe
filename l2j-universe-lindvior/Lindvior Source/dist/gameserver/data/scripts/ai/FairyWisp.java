package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 16.10.12
 * Time: 22:40
 * <p/>
 * При входе в зону агра (600) юзает хил, а потом умирает.
 * Wisp (32915) лечит по ХП, Large Wisp (32916) лечит по ХП и МП.
 * Не понятно что делают Red Wisp и Weak Wisp по оффу, поэтому прописываем им АИ, но не лечим.
 * АИ прописываем для старта таймера на убийство моба.
 */
public class FairyWisp extends DefaultAI {
    private final Skill healSkill = SkillTable.getInstance().getInfo(14064, 1);
    private final Skill healSkillBig = SkillTable.getInstance().getInfo(14065, 1);

    public FairyWisp(NpcInstance actor) {
        super(actor);

        actor.setAggroRange(600);

        MAX_PURSUE_RANGE = 0;
    }

    @Override
    protected void onEvtAggression(Creature attacker, int aggro) {
        super.onEvtAggression(attacker, aggro);

        if (!getActor().isCastingNow()) {
            createNewTask();
        }
    }

    @Override
    protected boolean createNewTask() {
        clearTasks();

        Creature target;

        if ((target = prepareTarget()) == null) {
            return false;
        }

        NpcInstance actor = getActor();

        if (actor.isDead()) {
            return false;
        }

        switch (actor.getNpcId()) {
            case 32915:
                addTaskCast(target, healSkill);
                break;
            case 32916:
                addTaskCast(target, healSkillBig);
                break;
        }

        addTimer(103, 3000);    // Таймер на убийство моба. 3 секунды, т.к. надо времян а каст скилла.
        return false;
    }

    @Override
    protected void onEvtTimer(int timerId, Object arg1, Object arg2) {
        NpcInstance actor = getActor();

        if (actor == null) {
            return;
        }

        if (timerId == 103) {
            actor.doDie(null);
        }
    }
}
