package lineage2.gameserver.model.items.etcitems;

import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author kick
 */
public class AppearanceStone
{
	public static enum ShapeType
	{
		NONE,
		NORMAL,
		BLESSED,
		FIXED,
		RESTORE
	}

	public static enum ShapeTargetType
	{
		NONE,
		WEAPON,
		ARMOR,
		ACCESSORY,
		ALL
	}

	private final int _itemId;
	private final ShapeTargetType[] _targetTypes;
	private final ShapeTargetType _clientTargetType;
	private final ShapeType _type;
	private final Grade[] _grades;
	private final long _cost;
	private final int _extractItemId;
	private final ExItemType[] _itemTypes;

	public AppearanceStone(int itemId, ShapeTargetType[] targetTypes, ShapeType type, Grade[] grades, long cost, int extractItemId, ExItemType[] itemTypes)
	{
		_itemId = itemId;
		_targetTypes = targetTypes;
		_type = type;
		_grades = grades;
		_cost = cost;
		_extractItemId = extractItemId;
		_itemTypes = itemTypes;

		if(_targetTypes.length > 1)
		{
			_clientTargetType = ShapeTargetType.ALL;
		}
		else
		{
			_clientTargetType = _targetTypes[0];
		}
	}

	public int getItemId()
	{
		return _itemId;
	}

	public ShapeTargetType[] getTargetTypes()
	{
		return _targetTypes;
	}

	public ShapeTargetType getClientTargetType()
	{
		return _clientTargetType;
	}

	public ShapeType getType()
	{
		return _type;
	}

	public Grade[] getGrades()
	{
		return _grades;
	}

	public long getCost()
	{
		return _cost;
	}

	public int getExtractItemId()
	{
		return _extractItemId;
	}

	public ExItemType[] getItemTypes()
	{
		return _itemTypes;
	}
}
