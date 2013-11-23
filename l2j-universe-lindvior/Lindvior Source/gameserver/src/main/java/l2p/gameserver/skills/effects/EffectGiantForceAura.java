package l2p.gameserver.skills.effects;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 30.06.12
 * Time: 9:26
 */
public class EffectGiantForceAura extends Effect {
    private int forceSkillId;
    private int auraSkillId;
    private ScheduledFuture<?> startEffectTask;

    public EffectGiantForceAura(Env env, EffectTemplate template) {
        super(env, template);
        forceSkillId = template.getParam().getInteger("forceSkillId", -1);
        auraSkillId = template.getParam().getInteger("auraSkillId", -1);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Контроллирующий скилл
        if (forceSkillId > 0) {
            startEffectTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
                @Override
                public void runImpl() throws Exception {
                    updateAura();
                }
            }, 500 + Rnd.get(4000));
        }
    }

    @Override
    public void onExit() {
        super.onExit();

        if (startEffectTask != null)
            startEffectTask.cancel(false);
    }

    private void updateAura() {
        Player effector = (Player) getEffector();
        Skill forceSkill = SkillTable.getInstance().getInfo(forceSkillId, 1);
        Skill auraSkill = getSkill();

        if (effector == null || forceSkill == null || auraSkill == null)
            return;
        List<Creature> targets = forceSkill.getTargets(effector, getEffected(), false);
        for (Creature target : targets) {
            if (target.getEffectList().getEffectsBySkillId(forceSkillId) == null) {
                forceSkill.getEffects(effector, target, false, false);
            }
        }
    }

    @Override
    public boolean onActionTime() {
        if (getEffected().isDead())
            return false;
        // Контроллирующий скилл
        if (forceSkillId > 0) {
            updateAura();
        } else if (auraSkillId > 0) {
            Player effector = (Player) getEffector();

            if (effector == null || effector.getEffectList().getEffectsBySkillId(auraSkillId) == null)
                return false;
        }
        return true;
    }
}
