package lineage2.gameserver.templates.skill.restoration;

import lineage2.commons.util.Rnd;

public final class RestorationItem
{
	private final int _id;
	private final int _minCount;
	private final int _maxCount;

	public RestorationItem(int id, int minCount, int maxCount)
	{
		this._id = id;
		this._minCount = minCount;
		this._maxCount = maxCount;
	}

	public int getId()
	{
		return this._id;
	}

	public int getMinCount()
	{
		return this._minCount;
	}

	public int getMaxCount()
	{
		return this._maxCount;
	}

	public int getRandomCount()
	{
		return Rnd.get(this._minCount, this._maxCount);
	}
}