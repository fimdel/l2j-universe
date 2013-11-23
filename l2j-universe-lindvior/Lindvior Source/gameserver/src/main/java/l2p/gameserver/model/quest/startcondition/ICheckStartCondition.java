package l2p.gameserver.model.quest.startcondition;

import l2p.gameserver.model.Player;

public interface ICheckStartCondition {
    public boolean checkCondition(Player player);
}
