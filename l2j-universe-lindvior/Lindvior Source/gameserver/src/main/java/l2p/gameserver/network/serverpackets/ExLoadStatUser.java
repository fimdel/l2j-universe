package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.WorldStatistic.CharacterStatisticElement;

import java.util.List;

/**
 * @author Darvin
 */
public class ExLoadStatUser extends L2GameServerPacket {
    private List<CharacterStatisticElement> list;

    public ExLoadStatUser(List<CharacterStatisticElement> list) {
        this.list = list;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x101);
        writeD(list.size());
        for (CharacterStatisticElement stat : list) {
            writeD(stat.getCategoryType().getClientId());
            writeD(stat.getCategoryType().getSubcat());
            writeQ(stat.getMonthlyValue());
            writeQ(stat.getValue());
        }
    }
}