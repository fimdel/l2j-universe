package l2p.commons.dao;

public interface JdbcEntityStats {
    public long getLoadCount();

    public long getInsertCount();

    public long getUpdateCount();

    public long getDeleteCount();
}
