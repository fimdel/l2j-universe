package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;

import java.util.ArrayList;

/**
 * @author Darvin
 */
public class ExMenteeSearch extends L2GameServerPacket {
    ArrayList<Player> mentees;
    int page, playersInPage;
    private GameObject obj;

    public ExMenteeSearch(int _page, int minLevel, int maxLevel) {
        mentees = new ArrayList<Player>();
        page = _page;
        playersInPage = 64;
        for (Player player : World.getAroundPlayers(obj))
            if (player.getLevel() >= minLevel && player.getLevel() <= maxLevel)
                mentees.add(player);
    }

    @Override
    protected void writeImpl() {
        writeEx(0x122);
        writeD(page);
        if (!mentees.isEmpty()) {
            writeD(mentees.size());
            writeD(mentees.size() % playersInPage);
            int i = 1;
            for (Player player : mentees) {
                if (i <= playersInPage * page && i > playersInPage * (page - 1)) {
                    writeS(player.getName());
                    writeD(player.getClassId().getId());
                    writeD(player.getLevel());
                }
            }
        } else {
            writeD(0x00);
            writeD(0x00);
        }
    }

    @Override
    public String getType() {
        return null;
    }

}
