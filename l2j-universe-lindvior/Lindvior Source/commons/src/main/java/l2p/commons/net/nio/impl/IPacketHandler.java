package l2p.commons.net.nio.impl;

import java.nio.ByteBuffer;

public interface IPacketHandler<T extends MMOClient> {
    public ReceivablePacket<T> handlePacket(ByteBuffer buf, T client);
}