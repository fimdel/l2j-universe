package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.RecipeTemplate;

/**
 * format ddddd
 */
public class RecipeItemMakeInfo extends L2GameServerPacket
{
	private int _id;
	private boolean _isDwarvenRecipe;
	private int _status;
	private int _curMP;
	private int _maxMP;

	public RecipeItemMakeInfo(Player player, RecipeTemplate recipeList, int status)
	{
		_id = recipeList.getId();
		_isDwarvenRecipe = recipeList.isDwarven();
		_status = status;
		_curMP = (int) player.getCurrentMp();
		_maxMP = player.getMaxMp();
		//
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xdd);
		writeD(_id); // ID
		writeD(_isDwarvenRecipe ? 0x00 : 0x01);
		writeD(_curMP);
		writeD(_maxMP);
		writeD(_status);
	}
}