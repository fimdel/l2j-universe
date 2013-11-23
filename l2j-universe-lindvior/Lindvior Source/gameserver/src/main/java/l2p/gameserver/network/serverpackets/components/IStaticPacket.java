package l2p.gameserver.network.serverpackets.components;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 13:28/01.12.2010
 */
public interface IStaticPacket {
    L2GameServerPacket packet(Player player);
    //   L2GameServerPacket packet();
}
