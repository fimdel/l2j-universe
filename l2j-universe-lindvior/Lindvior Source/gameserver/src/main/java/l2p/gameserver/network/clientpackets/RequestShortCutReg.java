package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.ShortCut;
import l2p.gameserver.network.serverpackets.ShortCutRegister;

public class RequestShortCutReg extends L2GameClientPacket {
    private int _type, _id, _slot, _page, _lvl, _characterType;

    @Override
    protected void readImpl() {
        _type = readD();
        int slot = readD();
        _id = readD();
        _lvl = readD();
        _characterType = readD();

        _slot = slot % 12;
        _page = slot / 12;
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (_page < 0 || _page > ShortCut.PAGE_MAX) {
            activeChar.sendActionFailed();
            return;
        }

        ShortCut shortCut = new ShortCut(_slot, _page, _type, _id, _lvl, _characterType);
        activeChar.sendPacket(new ShortCutRegister(activeChar, shortCut));
        activeChar.registerShortCut(shortCut);
    }
}