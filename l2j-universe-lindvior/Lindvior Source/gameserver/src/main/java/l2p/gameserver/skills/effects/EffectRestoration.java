package l2p.gameserver.skills.effects;

import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Playable;
import l2p.gameserver.stats.Env;
import l2p.gameserver.utils.ItemFunctions;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 30.06.12
 * Time: 9:32
 */
public class EffectRestoration extends Effect {
    private final int itemId;
    private final long count;

    public EffectRestoration(Env env, EffectTemplate template) {
        super(env, template);
        String item = getTemplate().getParam().getString("Item");
        itemId = Integer.parseInt(item.split(":")[0]);
        count = Long.parseLong(item.split(":")[1]);

    }

    @Override
    public void onStart() {
        super.onStart();
        ItemFunctions.addItem((Playable) getEffected(), itemId, count, true);
    }

    @Override
    protected boolean onActionTime() {
        return false;
    }
}
