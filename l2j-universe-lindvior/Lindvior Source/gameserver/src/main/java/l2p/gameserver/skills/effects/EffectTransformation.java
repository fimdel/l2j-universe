package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.skills.skillclasses.Transformation;
import l2p.gameserver.stats.Env;

public final class EffectTransformation extends Effect {
    private final boolean isFlyingTransform;

    public EffectTransformation(Env env, EffectTemplate template) {
        super(env, template);
        int id = (int) template._value;
        isFlyingTransform = template.getParam().getBool("isFlyingTransform", id == 8 || id == 9 || id == 260); // TODO сделать через параметр
    }

    @Override
    public boolean checkCondition() {
        if (isFlyingTransform && _effected.getX() > -166168)
            return false;
        return super.checkCondition();
    }

    @Override
    public void onStart() {
        super.onStart();

        _effected.setTransformationTemplate(getSkill().getNpcId());
        if (getSkill() instanceof Transformation)
            _effected.setTransformationName(((Transformation) getSkill()).transformationName);

        int id = (int) calc();

        if (_effected.isPlayer()) {
            Player player = (Player) _effected;

            if (isFlyingTransform) {
                boolean isVisible = player.isVisible();
                player.getSummonList().unsummonAll();
                player.decayMe();
                player.setFlying(true);
                player.setLoc(player.getLoc().changeZ(300)); // Немного поднимаем чара над землей

                player.setTransformation(id);
                if (isVisible)
                    player.spawnMe();
            } else
                player.setTransformation(id);
        } else
            _effected.setTransformation(id);

    }

    @Override
    public void onExit() {
        super.onExit();

        if (_effected.isPlayer()) {
            Player player = (Player) _effected;

            if (getSkill() instanceof Transformation)
                player.setTransformationName(null);

            if (isFlyingTransform) {
                boolean isVisible = player.isVisible();
                player.decayMe();
                player.setFlying(false);
                player.setLoc(player.getLoc().correctGeoZ());
                player.setTransformation(0);
                if (isVisible)
                    player.spawnMe();
            } else
                player.setTransformation(0);
        } else
            _effected.setTransformation(0);
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}