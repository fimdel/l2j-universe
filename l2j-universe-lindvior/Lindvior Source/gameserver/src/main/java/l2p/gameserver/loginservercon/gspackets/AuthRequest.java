package l2p.gameserver.loginservercon.gspackets;

import l2p.commons.net.AdvancedIp;
import l2p.gameserver.Config;
import l2p.gameserver.GameServer;
import l2p.gameserver.loginservercon.SendablePacket;

public class AuthRequest extends SendablePacket
{
	@Override
	protected void writeImpl()
	{
		writeC(0x00);
		writeD(GameServer.LOGIN_SERVER_PROTOCOL);
		writeC(Config.REQUEST_ID);
		writeC(Config.ACCEPT_ALTERNATE_ID ? 0x01 : 0x00);
		writeD(Config.LOGIN_SERVER_SERVER_TYPE);
		writeD(Config.LOGIN_SERVER_AGE_LIMIT);
		writeC(Config.LOGIN_SERVER_GM_ONLY ? 0x01 : 0x00);
		writeC(Config.LOGIN_SERVER_BRACKETS ? 0x01 : 0x00);
		writeC(Config.LOGIN_SERVER_IS_PVP ? 0x01 : 0x00);
		writeS(Config.EXTERNAL_HOSTNAME);
		writeS(Config.INTERNAL_HOSTNAME);
		writeH(Config.PORTS_GAME.length);
		for(int PORT_GAME : Config.PORTS_GAME)
		{
			writeH(PORT_GAME);
		}
		writeD(Config.MAXIMUM_ONLINE_USERS);

		System.out.println(Config.advancedIps.size());
		// Advanced Ip
		writeD(Config.advancedIps.size());
		for(AdvancedIp ip : Config.advancedIps)
		{
			writeS(ip.ipAdress);
			writeS(ip.ipMask);
			writeS(ip.bitMask);
		}
	}
}