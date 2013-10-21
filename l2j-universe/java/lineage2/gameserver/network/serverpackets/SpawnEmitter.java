package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * Этот пакет отвечает за анимацию высасывания душ из трупов
 * 
 * @author SYS
 */
public class SpawnEmitter extends L2GameServerPacket
{
	private int _monsterObjId;
	private int _playerObjId;

	public SpawnEmitter(NpcInstance monster, Player player)
	{
		_playerObjId = player.getObjectId();
		_monsterObjId = monster.getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		// ddd
		writeEx(0x5d);

		writeD(_monsterObjId);
		writeD(_playerObjId);
		writeD(0x00); // unk
	}
}