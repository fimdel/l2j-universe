/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Java Team (c) 2012. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package l2p.gameserver.network.serverpackets;

public class ExChangeMPCost extends L2GameServerPacket {

    @Override
    protected void writeImpl() {

        // 1 и 5 дшка всегджа одинаковы
        writeEx(0xEB);
        writeD(35840);// TODO unknown
        writeF(0x00);// TODO unknown
        writeD(256);   //на NA оффе всегда тут так
        writeD(0x00); //всегда  0
        writeD(35840);
        writeD(0x00);//всегда  0
        writeH(0x00);//всегда  0
    }
}
