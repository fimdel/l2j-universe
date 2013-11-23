package l2p.gameserver.network.serverpackets;

import l2p.gameserver.Config;
import l2p.gameserver.model.CharSelectionInfo;

/**
 * @author : Ragnarok
 * @date : 22.01.12 11:44
 * <p/>
 * dddd dd
 */
public class ExLoginVitalityEffectInfo extends L2GameServerPacket {

    private final CharSelectionInfo charInfo;

    public ExLoginVitalityEffectInfo(CharSelectionInfo charInfo) {
        this.charInfo = charInfo;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x11F);

        writeD(charInfo.getVitalityPoints() == 0 ? 0 : (int) (Config.ALT_VITALITY_RATE_EXP * 100)); // Exp
        // bonus
        writeD(5); // TODO: Remaining items count

        // Unknown
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
    }
}
