package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.NpcString;

public class ExSendUIEvent extends NpcStringContainer
{
	private int _objectId;
	private int _isHide;
	private int _isIncrease;
	private int _startTime;
	private int _endTime;

	public ExSendUIEvent(Player player, int isHide, int isIncrease, int startTime, int endTime, String... params)
	{
		this(player, isHide, isIncrease, startTime, endTime, NpcString.NONE, params);
	}

	/*
	 * public ExSendUIEvent(Player player, boolean isHide, boolean isIncrease, int startTime, int endTime, NpcString npcString, String... params) {
	 * super(npcString, params); _objectId = player.getObjectId(); _isHide = isHide; _isIncrease = isIncrease; _startTime = startTime; _endTime =
	 * endTime; }
	 */

	public ExSendUIEvent(Player player, int isHide, int isIncrease, int startTime, int endTime, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_isHide = isHide;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x8F);
		writeD(_objectId);
		writeD(_isHide); // ID timer (any style)
		writeD(0x00); // unknown
		writeD(0x00); // unknown
		writeS(String.valueOf(_isIncrease)); // Name indicator
		writeS(String.valueOf(_startTime / 60)); // timer starting minute(s)
		writeS(String.valueOf(_startTime % 60)); // timer starting second(s)
		writeS(String.valueOf(_endTime / 60)); // timer length minute(s) (timer
		                                       // will disappear 10 seconds
		                                       // before it ends)
		writeS(String.valueOf(_endTime % 60)); // timer length second(s) (timer
		                                       // will disappear 10 seconds
		                                       // before it ends)
		writeElements();
	}
}