package l2p.gameserver.data.xml.holder;

import gnu.trove.TIntObjectHashMap;
import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.templates.player.ClassData;

public final class ClassDataHolder extends AbstractHolder {
    private static final ClassDataHolder _instance = new ClassDataHolder();
    private final TIntObjectHashMap<ClassData> _classDataList = new TIntObjectHashMap<ClassData>();

    public static ClassDataHolder getInstance() {
        return _instance;
    }

    public void addClassData(ClassData classData) {
        _classDataList.put(classData.getClassId(), classData);
    }

    public ClassData getClassData(int classId) {
        return _classDataList.get(classId);
    }

    public int size() {
        return _classDataList.size();
    }

    public void clear() {
        _classDataList.clear();
    }
}
