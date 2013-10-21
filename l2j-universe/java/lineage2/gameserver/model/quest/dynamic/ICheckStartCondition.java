package lineage2.gameserver.model.quest.dynamic;

import lineage2.gameserver.model.Player;

public interface ICheckStartCondition
{
	public boolean checkCondition(Player player);
}