package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;

/**
 * <p/>
 * sample b9 73 5d 30 49 01 00 00 00 00 00
 * <p/>
 * format dhd (objectid, color, unk)
 * <p/>
 * color -xx -> -9 red<p> -8 -> -6 light-red<p> -5 -> -3 yellow<p> -2 -> 2
 * white<p> 3 -> 5 green<p> 6 -> 8 light-blue<p> 9 -> xx blue<p>
 * <p/>
 * usually the color equals the level difference to the selected target
 */
public class MyTargetSelected extends L2GameServerPacket
{
	final int _color;
	final int _obj;

	public MyTargetSelected(final Player player, final GameObject obj)
	{
		_obj = obj.getObjectId();
		_color = obj.isMonster() ? player.getLevel() - ((MonsterInstance) obj).getLevel() : 0;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xb9);
		writeD(_obj);
		writeH(_color);
		writeD(0x00);
	}
}
