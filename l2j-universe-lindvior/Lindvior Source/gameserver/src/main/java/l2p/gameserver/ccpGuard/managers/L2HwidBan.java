package l2p.gameserver.ccpGuard.managers;

public class L2HwidBan {

    private final int _id;
    private String HWID;

    public L2HwidBan(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public void setHwidBan(String hwid1) {
        HWID = hwid1;
    }

    public String getHwid() {
        return HWID;
    }
}
