package lineage2.gameserver.network.serverpackets;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExChangeAttributeInfo extends L2GameServerPacket
{
	/**
	 * Field _attribute.
	 */
	private int _attribute = -1;
	/**
	 * Field _ObjectIdStone.
	 */
	private final int _ObjectIdStone;
	
	/**
	 * Constructor for ExChangeAttributeInfo.
	 * @param att int
	 * @param ObjectIdStone int
	 */
	public ExChangeAttributeInfo(int att, int ObjectIdStone)
	{
		switch (att)
		{
			case 0:
				_attribute = -2;
				break;
			case 1:
				_attribute = -3;
				break;
			case 2:
				_attribute = -5;
				break;
			case 3:
				_attribute = -9;
				break;
			case 4:
				_attribute = -17;
				break;
			case 5:
				_attribute = -33;
		}
		_ObjectIdStone = ObjectIdStone;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x119);
		writeD(_ObjectIdStone);
		writeD(_attribute);
	}
}