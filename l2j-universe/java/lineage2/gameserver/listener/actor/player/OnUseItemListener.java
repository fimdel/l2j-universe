package lineage2.gameserver.listener.actor.player;

import lineage2.gameserver.listener.PlayerListener;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;

public interface OnUseItemListener extends PlayerListener
{
	public void onUseItem(Player player, GameObject target, ItemInstance item);
}