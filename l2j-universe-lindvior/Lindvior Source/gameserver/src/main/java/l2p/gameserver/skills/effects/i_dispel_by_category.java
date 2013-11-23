/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.effects;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

import java.util.List;


public class i_dispel_by_category extends Effect {
    private final AbnormalCategory _abnormalCategory;
    private final int _dispelChance;
    private final int _maxCount;

    public i_dispel_by_category(Env env, EffectTemplate template) {
        super(env, template);

        _abnormalCategory = template.getParam().getEnum("abnormal_category", AbnormalCategory.class);
        _dispelChance = template.getParam().getInteger("dispel_chance", 100);
        _maxCount = template.getParam().getInteger("max_count", 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (_dispelChance == 0 || _maxCount == 0) {
            return;
        }
        int dispelledCount = 0;
        List<Effect> effects = getEffected().getEffectList().getAllEffects();
        if (_abnormalCategory == AbnormalCategory.slot_debuff) {
            for (Effect effect : effects) {
                if ((effect.isOffensive()) &&
                        (effect.getSkill().isCancelable()) &&
                        (effect.getSkill().getMagicLevel() > 0)) {
                    if (dispelledCount >= _maxCount) {
                        break;
                    }
                    if (Rnd.chance(_dispelChance)) {
                        effect.exit();
                        dispelledCount++;
                    }
                }
            }
        } else if (_abnormalCategory == AbnormalCategory.slot_buff) {
            for (Effect effect : effects) {
                if (!effect.isOffensive() &&
                        effect.getSkill().isCancelable() &&
                        effect.getSkill().getMagicLevel() > 0) {
                    if (dispelledCount >= _maxCount) {
                        break;
                    }
                    if (Rnd.chance(_dispelChance)) {
                        effect.exit();
                        dispelledCount++;
                    }
                }
            }
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onActionTime() {
        return false;
    }

    private static enum AbnormalCategory {
        slot_buff,
        slot_debuff
    }
}