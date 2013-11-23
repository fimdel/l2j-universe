package l2p.gameserver.network.serverpackets;

public class ExNewSkillToLearnByLevelUp extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExNewSkillToLearnByLevelUp();

    public ExNewSkillToLearnByLevelUp() {
        //
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xFC);
    }
}
