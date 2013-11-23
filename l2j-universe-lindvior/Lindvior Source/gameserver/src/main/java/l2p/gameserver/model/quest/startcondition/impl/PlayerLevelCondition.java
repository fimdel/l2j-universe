package l2p.gameserver.model.quest.startcondition.impl;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.startcondition.ICheckStartCondition;

public final class PlayerLevelCondition implements ICheckStartCondition {
    private final int min;
    private final int max;

    public PlayerLevelCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public final boolean checkCondition(Player player) {
        return player.getLevel() >= min && player.getLevel() <= max;
    }
}
