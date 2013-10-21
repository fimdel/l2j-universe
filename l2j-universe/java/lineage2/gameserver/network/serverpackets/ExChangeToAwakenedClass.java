package lineage2.gameserver.network.serverpackets;

/**
 * @author Darvin
 * @data 08.06.2012
 *       <p/>
 */
public class ExChangeToAwakenedClass extends L2GameServerPacket
{
	private int classId;

	public ExChangeToAwakenedClass(int classId)
	{
		this.classId = classId;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xFF);
		writeD(classId);
	}
}
