package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.RankPrivs;
import lineage2.gameserver.model.pledge.UnitMember;

public class PledgeReceivePowerInfo extends L2GameServerPacket
{
	private int PowerGrade, privs;
	private String member_name;

	public PledgeReceivePowerInfo(UnitMember member)
	{
		PowerGrade = member.getPowerGrade();
		member_name = member.getName();
		if (member.isClanLeader())
			privs = Clan.CP_ALL;
		else
		{
			RankPrivs temp = member.getClan().getRankPrivs(member.getPowerGrade());
			if (temp != null)
				privs = temp.getPrivs();
			else
				privs = 0;
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x3e);
		writeD(PowerGrade);
		writeS(member_name);
		writeD(privs);
	}
}