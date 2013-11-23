package l2p.gameserver.network.clientpackets;

import l2p.gameserver.handler.usercommands.IUserCommandHandler;
import l2p.gameserver.handler.usercommands.UserCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.components.CustomMessage;

/**
 * format:  cd
 * Пример пакета по команде /loc:
 * AA 00 00 00 00
 */
public class BypassUserCmd extends L2GameClientPacket {
    private int _command;

    @Override
    protected void readImpl() {
        _command = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        IUserCommandHandler handler = UserCommandHandler.getInstance().getUserCommandHandler(_command);

        if (handler == null)
            activeChar.sendMessage(new CustomMessage("common.S1NotImplemented", activeChar).addString(String.valueOf(_command)));
        else
            handler.useUserCommand(_command, activeChar);
    }
}