package l2p.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.commons.data.xml.AbstractHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mazaffaka
 */
public class StartItemHolder extends AbstractHolder {
    private static StartItemHolder _instance = new StartItemHolder();
    private final TIntObjectHashMap<ArrayList<StartItem>> _startItemTemplate;

    private StartItemHolder() {
        this._startItemTemplate = new TIntObjectHashMap<ArrayList<StartItem>>();
    }

    @Override
    public void clear() {
        this._startItemTemplate.clear();
    }

    public static StartItemHolder getInstance() {
        return _instance;
    }

    @Override
    public int size() {
        return this._startItemTemplate.size();
    }

    public List<StartItem> getStartItems(int classId) {
        return this._startItemTemplate.get(classId) != null ? (ArrayList<StartItem>) this._startItemTemplate.get(classId) : new ArrayList<StartItem>(0);
    }

    public void addStartItem(StartItem item, int classId) {
        if (!this._startItemTemplate.containsKey(classId)) {
            this._startItemTemplate.put(classId, new ArrayList<StartItem>());
        }
        this._startItemTemplate.get(classId).add(item);
    }

    public static class StartItem {
        public int id;
        public long count;
        public boolean equipped = false;
        public boolean warehouse = false;
    }
}
