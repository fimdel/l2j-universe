package lineage2.gameserver.network.serverpackets;

/**
 * Created by IntelliJ IDEA. User: Darvin Date: 04.03.12 Time: 23:16
 */
public class ExBR_BuyProductResult extends L2GameServerPacket
{
	private int code;

	public ExBR_BuyProductResult(int code)
	{
		this.code = code;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xCC);
		writeD(code);
	}
}
