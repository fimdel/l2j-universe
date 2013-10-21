package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author ALF Для отправки нативных NpcStringId
 */
public class NpcSayNative extends L2GameServerPacket
{

	private int _id;
	private int _objId;
	private int _msgId;

	public NpcSayNative(NpcInstance npc, int msgId)
	{
		_msgId = msgId;
		_objId = npc.getObjectId();
		_id = npc.getNpcId();
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x30);
		writeD(_objId);
		writeD(0x16);
		writeD(1000000 + _id);
		writeD(_msgId);
	}

}
