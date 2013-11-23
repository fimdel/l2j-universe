/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;

public class BloodyDisciple extends Fighter {
    private static final int[] buffId = {14975, 14976, 14977};

    public BloodyDisciple(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(Creature killer) {
        if (killer.isPlayable()) {
            Player player = killer.getPlayer();
            for (int buff : buffId) {
                Skill skill = SkillTable.getInstance().getInfo(buff, 1);
                for (EffectTemplate et : skill.getEffectTemplates()) {
                    Env env = new Env(player, player, skill);
                    Effect effect = et.getEffect(env);
                    player.getEffectList().addEffect(effect);
                }
            }
        }
        super.onEvtDead(killer);
    }
}