package lineage2.gameserver.network.serverpackets;

public class ExAlterSkillRequest extends L2GameServerPacket
{
	private static final String _S__FE_113_EXALTERSKILLREQUEST = "[S] FE:113 ExAlterSkillRequest";

	int nextSkillId;
	int currentSkillId;
	int alterTime;

	public ExAlterSkillRequest(int nextSkillId, int currentSkillId, int alterTime)
	{
		this.nextSkillId = nextSkillId;
		this.currentSkillId = currentSkillId;
		this.alterTime = alterTime;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x114);
		// ddd
		writeD(nextSkillId);
		writeD(currentSkillId);
		writeD(alterTime);
	}

	@Override
	public String getType()
	{
		return _S__FE_113_EXALTERSKILLREQUEST;
	}
}
