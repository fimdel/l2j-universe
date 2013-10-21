package lineage2.gameserver.network.serverpackets;

import javolution.util.FastList;
import lineage2.gameserver.model.Player;

public class ExBR_ExtraUserInfo extends L2GameServerPacket
{
	private int _objectId;
	private FastList<Integer> _effect;

	public ExBR_ExtraUserInfo(Player cha)
	{
		_objectId = cha.getObjectId();
		_effect = cha.getAveList();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xDB);
		writeD(_objectId); // object id of player
		
		if (_effect != null)
		{
			writeD(_effect.size());
			for (int i : _effect)
			{
				writeD(i);
			}
		}
		else
		{
			writeD(0x00);
		}

		writeC(0);
	}
}