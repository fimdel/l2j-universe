package l2p.gameserver.handler.chat;

import l2p.gameserver.network.serverpackets.components.ChatType;

/**
 * @author VISTALL
 * @date 18:16/12.03.2011
 */
public interface IChatHandler {
    void say();

    ChatType getType();
}
