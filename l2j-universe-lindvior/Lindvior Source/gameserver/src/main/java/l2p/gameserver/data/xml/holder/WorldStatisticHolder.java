package l2p.gameserver.data.xml.holder;

import l2p.gameserver.model.WorldStatistic.CategoryType;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 29.10.12
 * Time: 16:31
 */
public final class WorldStatisticHolder {
    private final CategoryType _cat;
    private final short _place;
    private final int _objId;
    private final String _name;
    private final long _value;
    private final int _clanCrestId;
    private final boolean _isClanStatistic;

    public WorldStatisticHolder(int catId, int subcatId, short place, int objId, String name, long value) {
        _cat = CategoryType.getCategoryById(catId, subcatId);
        _place = place;
        _objId = objId;
        _name = name;
        _value = value;
        _clanCrestId = 0;
        _isClanStatistic = false;
    }

    public WorldStatisticHolder(int objId, String name, long value, int clanCrestId) {
        _cat = null;
        _place = 0;
        _objId = objId;
        _name = name;
        _value = value;
        _clanCrestId = clanCrestId;
        _isClanStatistic = true;
    }

    public int getObjId() {
        return _objId;
    }

    public String getName() {
        return _name;
    }

    public CategoryType getCategory() {
        return _cat;
    }

    public short getPlace() {
        return _place;
    }

    public long getValue() {
        return _value;
    }

    public int getClanCrestId() {
        return _clanCrestId;
    }

    public boolean isClanStatistic() {
        return _isClanStatistic;
    }
}
