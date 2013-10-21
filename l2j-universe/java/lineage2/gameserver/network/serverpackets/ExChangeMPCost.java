/*
 * Copyright Java Team (c) 2012. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package lineage2.gameserver.network.serverpackets;

public class ExChangeMPCost extends L2GameServerPacket
{
	private int unk1;
	private double unk2;

	public ExChangeMPCost(int unk1, double unk2)
	{
		this.unk1 = unk1;
		this.unk2 = unk2;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xEB);
		writeD(unk1);// TODO unknown
		writeF(unk2);// TODO unknown
	}
}
