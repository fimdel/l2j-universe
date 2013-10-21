package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 13:28/01.12.2010
 */
public interface IStaticPacket
{
	L2GameServerPacket packet(Player player);
}
