package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;

public class ExVitalityEffectInfo extends L2GameServerPacket
{
	private int points;
	private int expBonus;

	public ExVitalityEffectInfo(Player player)
	{
		points = player.getVitality();
		expBonus = (player.getVitality() == 0 ? 0 : (int) (Config.ALT_VITALITY_RATE * 100));
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x11E);

		writeD(points);
		writeD(expBonus);
		writeD(5);// TODO: Remaining items count
		writeD(5);// TODO: Remaining items count
	}
}