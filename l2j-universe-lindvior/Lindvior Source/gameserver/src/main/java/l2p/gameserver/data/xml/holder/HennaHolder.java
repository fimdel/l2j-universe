package l2p.gameserver.data.xml.holder;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.templates.Henna;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date 9:03/06.12.2010
 */
public final class HennaHolder extends AbstractHolder {
    private static final HennaHolder _instance = new HennaHolder();

    private TIntObjectHashMap<Henna> _hennas = new TIntObjectHashMap<Henna>();

    public static HennaHolder getInstance() {
        return _instance;
    }

    public void addHenna(Henna h) {
        _hennas.put(h.getSymbolId(), h);
    }

    public Henna getHenna(int symbolId) {
        return _hennas.get(symbolId);
    }

    public List<Henna> generateList(Player player) {
        List<Henna> list = new ArrayList<Henna>();
        for (TIntObjectIterator<Henna> iterator = _hennas.iterator(); iterator.hasNext(); ) {
            iterator.advance();
            Henna h = iterator.value();
            if (h.isForThisClass(player))
                list.add(h);
        }

        return list;
    }

    @Override
    public int size() {
        return _hennas.size();
    }

    @Override
    public void clear() {
        _hennas.clear();
    }
}
