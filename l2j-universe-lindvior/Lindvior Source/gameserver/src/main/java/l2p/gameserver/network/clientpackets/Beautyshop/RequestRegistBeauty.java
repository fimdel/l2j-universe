/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.clientpackets.Beautyshop;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.Beautyshop.ExResponseBeautyRegistResetPacket;

public class RequestRegistBeauty extends L2GameClientPacket {

    private int face, hairstyle, haircolor;

    @Override
    protected void readImpl() throws Exception {
        face = readD();    //face
        hairstyle = readD();     //hairStyle
        haircolor = readD();    //hairColor
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 0));
        //    activeChar.setFace(face);
        //    activeChar.setHairStyle(hairstyle);
        //     activeChar.setHairColor(haircolor);
        //     activeChar.broadcastUserInfo(true);
    }
}
