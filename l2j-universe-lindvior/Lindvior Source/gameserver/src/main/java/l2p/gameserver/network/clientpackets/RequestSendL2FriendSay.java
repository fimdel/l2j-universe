package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.L2FriendSay;
import l2p.gameserver.utils.Log;

/**
 * Recieve Private (Friend) Message
 * Format: c SS
 * S: Message
 * S: Receiving Player
 */
public class RequestSendL2FriendSay extends L2GameClientPacket {
    private String _message;
    private String _reciever;

    @Override
    protected void readImpl() {
        _message = readS(2048);
        _reciever = readS(16);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.getNoChannel() != 0) {
            if (activeChar.getNoChannelRemained() > 0 || activeChar.getNoChannel() < 0) {
                activeChar.sendPacket(Msg.CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_BECOME_EVEN_LONGER);
                return;
            }
            activeChar.updateNoChannel(0);
        }

        Player targetPlayer = World.getPlayer(_reciever);
        if (targetPlayer == null) {
            activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_ONLINE);
            return;
        }

        if (targetPlayer.isBlockAll()) {
            activeChar.sendPacket(Msg.THE_PERSON_IS_IN_A_MESSAGE_REFUSAL_MODE);
            return;
        }

        if (!activeChar.getFriendList().getList().containsKey(targetPlayer.getObjectId()))
            return;

        Log.LogChat("FRIENDTELL", activeChar.getName(), _reciever, _message);

        L2FriendSay frm = new L2FriendSay(activeChar.getName(), _reciever, _message);
        targetPlayer.sendPacket(frm);
    }
}