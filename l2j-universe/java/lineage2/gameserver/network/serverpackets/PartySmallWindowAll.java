package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.party.PartySubstitute;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class PartySmallWindowAll extends L2GameServerPacket
{
	private int leaderId, loot;
	private List<PartySmallWindowMemberInfo> members = new ArrayList<PartySmallWindowMemberInfo>();

	public PartySmallWindowAll(Party party, Player exclude)
	{
		leaderId = party.getPartyLeader().getObjectId();
		loot = party.getLootDistribution();

		for (Player member : party.getPartyMembers())
			if (member != exclude)
				members.add(new PartySmallWindowMemberInfo(member));
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x4E);
		writeD(leaderId); // c3 party leader id
		writeD(loot); // c3 party loot type (0,1,2,....)
		writeD(members.size());
		for (PartySmallWindowMemberInfo member : members)
		{
			writeD(member._id);
			writeS(member._name);
			writeD(member.curCp);
			writeD(member.maxCp);
			writeD(member.curHp);
			writeD(member.maxHp);
			writeD(member.curMp);
			writeD(member.maxMp);
			writeD(member.vitality);
			writeD(member.level);
			writeD(member.class_id);
			writeD(0x00);// writeD(0x01);
			writeD(member.race_id);
			writeD(0x00); // Hide Name
			writeD(0x00);
			writeD(member.replace);
			writeD(member._pets.size());
			for (Summon pet : member._pets)
			{
				writeD(pet.getObjectId());
				writeD(pet.getNpcId() + 1000000);
				writeD(pet.getSummonType());
				writeS(pet.getName());
				writeD((int) pet.getCurrentHp());
				writeD(pet.getMaxHp());
				writeD((int) pet.getCurrentMp());
				writeD(pet.getMaxMp());
				writeD(pet.getLevel());
			}
		}
	}

	public static class PartySmallWindowMemberInfo
	{
		public String _name, pet_Name;
		public int _id, curCp, maxCp, curHp, maxHp, curMp, maxMp, level, class_id, race_id;
		public int vitality;
		public Collection<Summon> _pets;
		public int replace;

		public PartySmallWindowMemberInfo(Player member)
		{
			_name = member.getName();
			_id = member.getObjectId();
			curCp = (int) member.getCurrentCp();
			maxCp = member.getMaxCp();
			vitality = member.getVitality();
			curHp = (int) member.getCurrentHp();
			maxHp = member.getMaxHp();
			curMp = (int) member.getCurrentMp();
			maxMp = member.getMaxMp();
			level = member.getLevel();
			class_id = member.getClassId().getId();
			race_id = member.getRace().ordinal();
			_pets = member.getPets();
			replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
		}
	}
}