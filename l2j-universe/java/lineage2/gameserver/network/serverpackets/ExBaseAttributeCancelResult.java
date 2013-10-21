package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author VISTALL
 */
public class ExBaseAttributeCancelResult extends L2GameServerPacket
{
	private boolean _result;
	private int _objectId;
	private Element _element;

	public ExBaseAttributeCancelResult(boolean result, ItemInstance item, Element element)
	{
		_result = result;
		_objectId = item.getObjectId();
		_element = element;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x76);
		writeD(_result);
		writeD(_objectId);
		writeD(_element.getId());
	}
}