package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.items.CrystallizationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALF
 * @modified KilRoy
 */
public class ExGetCrystalizingEstimation extends L2GameServerPacket
{
	/**
	 * Field _items.
	 */
	private List<CrystallizationItem> _items;
	
	/**
	 * Constructor for ExGetCrystalizingEstimation.
	 * @param _items List<CrystallizationItem>
	 */
	public ExGetCrystalizingEstimation(List<CrystallizationItem> _items)
	{
		this._items = _items;
	}

	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xE1);
		writeD(_items.size());
		for (CrystallizationItem i : _items)
		{
			writeD(i.getItemId());
			writeQ(i.getCount());
			writeF(i.getChance());
		}
	}
	
	/**
	 * Method addCrystallizationItem.
	 * @param i CrystallizationItem
	 */
	public void addCrystallizationItem(CrystallizationItem i)
	{
		if (_items == null)
		{
			_items = new ArrayList<>();
		}
		_items.add(i);
	}
}
