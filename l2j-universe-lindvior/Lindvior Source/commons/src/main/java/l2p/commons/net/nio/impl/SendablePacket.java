package l2p.commons.net.nio.impl;

import java.nio.ByteBuffer;

public abstract class SendablePacket<T extends MMOClient> extends l2p.commons.net.nio.SendablePacket<T> {
    @Override
    protected ByteBuffer getByteBuffer() {
        return ((SelectorThread) Thread.currentThread()).getWriteBuffer();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getClient() {
        return (T) ((SelectorThread) Thread.currentThread()).getWriteClient();
    }

    protected abstract boolean write();
}