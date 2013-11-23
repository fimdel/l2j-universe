/**
 *
 */
package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.stats.Env;

public class EffectExample extends Effect {
    // Самые важные функции с эффектах:
    // _effected - тот на кого ДЕЙТСВУЕТ эффект
    // getEffected() - тоже самое только другой тип (експерементируйте)
    // getEffector() - тот кто КАСТОВАЛ эффект (надо в основном для эффектов которые кастуются на ПЕТА)

    // Конструктор. Обычно он именно такой
    public EffectExample(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Дальше пишем дейтсвия эффекта "При наложении эффекта"
    }

    @Override
    public boolean onActionTime() {
        // Пишем дейтсвия эффекта "При действии эффекта"
        return false;
    }

    @Override
    public void onExit() {
        super.onExit();
        // Дальше пишем дейтсвия эффекта "При выходе с эффекта"
    }
}