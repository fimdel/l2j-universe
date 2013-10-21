package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.CharSelectionInfo;

/**
 * @author : Ragnarok
 * @date : 22.01.12 11:44
 *       <p/>
 *       dddd dd
 */
public class ExLoginVitalityEffectInfo extends L2GameServerPacket
{

	private CharSelectionInfo charInfo;

	public ExLoginVitalityEffectInfo(CharSelectionInfo charInfo)
	{
		this.charInfo = charInfo;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x11F);

		writeD(charInfo.getVitalityPoints() == 0 ? 0 : (int) (Config.ALT_VITALITY_RATE * 100));
		                                                                                        // bonus
		writeD(5); // TODO: Remaining items count

		// Unknown
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}
