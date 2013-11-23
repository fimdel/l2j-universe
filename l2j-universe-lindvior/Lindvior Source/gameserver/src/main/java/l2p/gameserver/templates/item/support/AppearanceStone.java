/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item.support;

import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;

public class AppearanceStone {
    private final int _itemId;
    private final ShapeTargetType[] _targetTypes;
    private final ShapeTargetType _clientTargetType;
    private final ShapeType _type;
    private final ItemGrade[] _grades;
    private final long _cost;
    private final int _extractItemId;
    private final ExItemType[] _itemTypes;

    public AppearanceStone(int itemId, ShapeTargetType[] targetTypes, ShapeType type, ItemGrade[] grades, long cost, int extractItemId, ExItemType[] itemTypes) {
        _itemId = itemId;
        _targetTypes = targetTypes;
        _type = type;
        _grades = grades;
        _cost = cost;
        _extractItemId = extractItemId;
        _itemTypes = itemTypes;

        if (_targetTypes.length > 1)
            _clientTargetType = ShapeTargetType.ALL;
        else
            _clientTargetType = _targetTypes[0];
    }

    public ShapeType getType() {
        return _type;
    }

    public int getItemId() {
        return _itemId;
    }

    public ShapeTargetType[] getTargetTypes() {
        return _targetTypes;
    }

    public ShapeTargetType getClientTargetType() {
        return _clientTargetType;
    }

    public int getExtractItemId() {
        return _extractItemId;
    }

    public long getCost() {
        return _cost;
    }

    public ExItemType[] getItemTypes() {
        return _itemTypes;
    }

    public ItemGrade[] getGrades() {
        return _grades;
    }

    public static enum ShapeTargetType {
        NONE,
        WEAPON,
        ARMOR,
        ACCESSORY,
        ALL
    }

    public static enum ShapeType {
        NONE,
        NORMAL,
        BLESSED,
        FIXED,
        RESTORE
    }
}
