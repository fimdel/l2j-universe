package l2p.gameserver.templates.player;

public class StatAttributes {
    private int _int = 0;
    private int _str = 0;
    private int _con = 0;
    private int _men = 0;
    private int _dex = 0;
    private int _wit = 0;

    public StatAttributes(int _int, int str, int con, int men, int dex, int wit) {
        this._int = _int;
        _str = str;
        _con = con;
        _men = men;
        _dex = dex;
        _wit = wit;
    }

    public int getINT() {
        return _int;
    }

    public int getSTR() {
        return _str;
    }

    public int getCON() {
        return _con;
    }

    public int getMEN() {
        return _men;
    }

    public int getDEX() {
        return _dex;
    }

    public int getWIT() {
        return _wit;
    }
}