/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

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

// эффект для юза 2 скила после уза 1. не стал делать скил клас или же писать новую переменную так проще.
// эффект добавляется в оба скила
//пример:
/*
      <skill id="1929" levels="1" name="Tyrr Force">
        <set name="icon" val="icon.icon_tir"/>
		<set name="class_level" val="5"/>
		<set name="operateType" val="OP_TOGGLE"/>
		<set name="skillRadius" val="1000"/>
		<set name="target" val="TARGET_PARTY"/>
		<set name="skillType" val="CONT"/>
		<for>
			<effect name="AddTrigerSkillUse" count="0x7fffffff" time="5" val="10" stackTypes="Siegel_Aura" stackOrder="1">
				<def name="UseTrigerSkillId" val="1930" />
			</effect>
		</for>
	</skill>
    <skill id="1930" levels="1" name="force tyuranta">
		<set name="icon" val="icon.icon_tir"/>
		<set name="class_level" val="5"/>
		<set name="operateType" val="OP_ACTIVE"/>
		<set name="skillRadius" val="1000"/>
		<set name="target" val="TARGET_ONE"/>
		<set name="skillType" val="CONT"/>
		<for>
			<effect name="AddTrigerSkillUse" count="0x7fffffff" time="5" val="10" stackTypes="Tyrr_Aura" stackOrder="1">
				<mul stat="rCritRate" order="0x30" val="1.01">
					<not>
						<player classId="140"/>
					</not>
				</mul>
				<def name="TrigerSkillId" val="1929" />
			</effect>
		</for>
	</skill>
 */
public class EffectAddTrigerSkillUse extends Effect {

    private int UseTrigerSkillId;
    private int TrigerSkillId;
    private ScheduledFuture<?> startEffectTask;

    public EffectAddTrigerSkillUse(Env env, EffectTemplate template) {
        super(env, template);
        UseTrigerSkillId = template.getParam().getInteger("UseTrigerSkillId", -1); // вставлять в тригерный скилл
        TrigerSkillId = template.getParam().getInteger("TrigerSkillId", -1);    // вставлять в главный скилл
    }

    @Override
    public void onStart() {
        super.onStart();

        // Контроллирующий скилл
        if (UseTrigerSkillId > 0) {
            startEffectTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
                @Override
                public void runImpl() throws Exception {
                    updateUseTrigerSkillId();
                }
            }, 500 + Rnd.get(4000));
        }
    }

    private void updateUseTrigerSkillId() {
        Player effector = (Player) getEffector();
        Skill forceSkill = SkillTable.getInstance().getInfo(UseTrigerSkillId, 1);
        Skill auraSkill = getSkill();

        if (effector == null || forceSkill == null || auraSkill == null)
            return;
        List<Creature> targets = forceSkill.getTargets(effector, getEffected(), false);
        for (Creature target : targets) {
            if (target.getEffectList().getEffectsBySkillId(UseTrigerSkillId) == null) {
                forceSkill.getEffects(effector, target, false, false);
            }
        }
    }

    @Override
    public void onExit() {
        super.onExit();

        if (startEffectTask != null)
            startEffectTask.cancel(false);
    }

    @Override
    public boolean onActionTime() {
        if (getEffected().isDead())
            return false;
        // Контроллирующий скилл
        if (UseTrigerSkillId > 0) {
            updateUseTrigerSkillId();
        } else if (TrigerSkillId > 0) {
            Player effector = (Player) getEffector();

            if (effector == null || effector.getEffectList().getEffectsBySkillId(TrigerSkillId) == null)
                return false;
        }
        return true;
    }
}
