package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.party.PartySubstitute;

public class PartySmallWindowAdd extends L2GameServerPacket
{
	private int leaderId;
	private int distribution;
	private final PartySmallWindowAll.PartySmallWindowMemberInfo member;
	private int replace;

	public PartySmallWindowAdd(Player player, Player member, int _distribution)
	{
		leaderId = player.getObjectId();
		distribution = _distribution;
		this.member = new PartySmallWindowAll.PartySmallWindowMemberInfo(member);
		replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x4F);
		writeD(leaderId);
		writeD(distribution);
		writeD(member._id);
		writeS(member._name);
		writeD(member.curCp);
		writeD(member.maxCp);
		writeD(member.vitality);
		writeD(member.curHp);
		writeD(member.maxHp);
		writeD(member.curMp);
		writeD(member.maxMp);
		writeD(member.level);
		writeD(member.class_id);
		writeD(0x00);// writeD(0x01); ??
		writeD(member.race_id);
		writeD(0x00);// Hide name
		writeD(0x00);// unknown
		writeD(replace);// Идет ли поиск замены игроку
	}
}