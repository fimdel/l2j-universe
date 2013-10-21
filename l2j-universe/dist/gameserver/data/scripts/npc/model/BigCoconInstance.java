package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Awakeninger
 * @created 15.09.2012
 * @reworked 20.11.2012
 */

public final class BigCoconInstance extends NpcInstance {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int[] small = {22867, 22875, 22883, 22891, 22899, 22907 };
	private final int[] big = { 22868, 22876, 22884, 22892, 22900, 22908  };
    public BigCoconInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;
		if (getNpcId() == 32920){
			if(command.startsWith("non_strong_mass_attack")){
					NpcUtils.spawnSingle(small[Rnd.get(small.length)], Location.coordsRandomize(getLoc(), 1, 1), getReflection());
					this.doDie(null);
					return;
			}
			if(command.startsWith("strong_mass_attack")){
					NpcUtils.spawnSingle(big[Rnd.get(big.length)], Location.coordsRandomize(getLoc(), 1, 1), getReflection());
					this.doDie(null);
					return;
			}
		} else
            super.onBypassFeedback(player, command);
    }
}