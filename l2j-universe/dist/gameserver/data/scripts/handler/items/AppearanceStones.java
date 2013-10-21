package handler.items;

import lineage2.gameserver.data.xml.holder.EnchantItemHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExChooseShapeShiftingItem;

public class AppearanceStones extends ScriptItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return false;
		}

		Player player = (Player) playable;

		if(player.getAppearanceStone() != null)
		{
			return false;
		}

		player.setAppearanceStone(item);
		player.sendPacket(new ExChooseShapeShiftingItem(EnchantItemHolder.getInstance().getAppearanceStone(item.getItemId())));
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return EnchantItemHolder.getInstance().getAppearanceStones();
	}
}
