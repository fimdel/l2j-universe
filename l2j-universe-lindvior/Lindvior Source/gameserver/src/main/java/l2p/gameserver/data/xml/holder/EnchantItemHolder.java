/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data.xml.holder;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.templates.item.support.AppearanceStone;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public class EnchantItemHolder extends AbstractHolder {
    private static EnchantItemHolder _instance = new EnchantItemHolder();

    private IntObjectMap<AppearanceStone> _appearanceStones = new HashIntObjectMap<AppearanceStone>();

    public void clear() {
        _appearanceStones.clear();
    }

    public static EnchantItemHolder getInstance() {
        return _instance;
    }

    public int size() {
        return _appearanceStones.size();
    }

    public void addAppearanceStone(AppearanceStone appearanceStone) {
        _appearanceStones.put(appearanceStone.getItemId(), appearanceStone);
    }

    public AppearanceStone getAppearanceStone(int id) {
        return _appearanceStones.get(id);
    }

    public int[] getAppearanceStones() {
        return _appearanceStones.keySet().toArray();
    }
}
