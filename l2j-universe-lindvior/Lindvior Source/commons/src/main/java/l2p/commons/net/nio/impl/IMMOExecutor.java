package l2p.commons.net.nio.impl;


public interface IMMOExecutor<T extends MMOClient> {
    public void execute(Runnable r);
}