package lineage2.gameserver.model.entity.museum;

/**
 * @author ALF
 */
public class UserRankPoints
{
	private final int _id;
	private long _monthlyPoints;
	private long _totalPoints;

	public UserRankPoints(int id)
	{
		_id = id;
	}

	public UserRankPoints(int id, long totalPoints)
	{
		_id = id;
		_totalPoints = totalPoints;
	}

	public long getMonthlyPoints()
	{
		return _monthlyPoints;
	}

	public void setMonthlyPoints(long _monthlyPoints)
	{
		this._monthlyPoints = _monthlyPoints;
	}

	public long getTotalPoints()
	{
		return _totalPoints;
	}

	public void setTotalPoints(long _totalPoints)
	{
		this._totalPoints = _totalPoints;
	}

	public int getId()
	{
		return _id;
	}

	public void addMonthlyPoints(long count)
	{
		_monthlyPoints += count;
	}

	public void addTotalPoints(long count)
	{
		_totalPoints += count;
	}
}
