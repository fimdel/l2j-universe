package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.MenteeMentor;

public class ExMentorList extends L2GameServerPacket
{
	/**
	 * Field _list.
	 */
	private List<MenteeInfo> _list = Collections.emptyList();
	/**
	 * Field _mentor.
	 */
	private final int _mentor;

	/**
	 * Constructor for ExMentorList.
	 * @param player Player
	 */
	public ExMentorList(Player player)
	{
		_mentor = player.getMenteeMentorList().getMentor();
		Map<Integer, MenteeMentor> list = player.getMenteeMentorList().getList();
		_list = new ArrayList<>(list.size());
		for (Map.Entry<Integer, MenteeMentor> entry : list.entrySet())
		{
			MenteeInfo m = new MenteeInfo();
			m.objectId = entry.getKey();
			m.name = entry.getValue().getName();
			m.online = entry.getValue().isOnline();
			m.level = entry.getValue().getLevel();
			m.classId = entry.getValue().getClassId();
			_list.add(m);
		}
	}

	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x121);

		writeD(_mentor == 0 ? 0x01 : 0x02);
		writeD(_list.size());
		for (MenteeInfo entry : _list)
		{
			writeD(entry.objectId);
			writeS(entry.name);
			writeD(entry.classId);
			writeD(entry.level);
			writeD(entry.online);
		}
	}

	private class MenteeInfo
	{
		/**
		 * Constructor for MenteeInfo.
		 */
		public MenteeInfo()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Field name.
		 */
		String name;
		/**
		 * Field objectId.
		 */
		int objectId;
		/**
		 * Field online.
		 */
		boolean online;
		/**
		 * Field level.
		 */
		int level;
		/**
		 * Field classId.
		 */
		int classId;
	}
}
