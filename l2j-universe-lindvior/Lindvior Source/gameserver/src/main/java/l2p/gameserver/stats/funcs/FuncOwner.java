package l2p.gameserver.stats.funcs;

public interface FuncOwner {
    public boolean isFuncEnabled();

    public boolean overrideLimits();
}