package lineage2.gameserver.model.worldstatistics;

/**
 * @author Дмитрий
 * @date 10.10.12 20:52
 */
public class CharacterStatisticElement
{
	private CategoryType categoryType;
	private long value;
	private long monthlyValue;

	public CharacterStatisticElement(CategoryType type, long value, long monthlyValue)
	{
		categoryType = type;
		this.value = value;
		this.monthlyValue = monthlyValue;
	}

	public CharacterStatisticElement(CategoryType type, long value)
	{
		this(type, value, 0L);
	}

	public CategoryType getCategoryType()
	{
		return categoryType;
	}

	public long getValue()
	{
		return value;
	}

	public long getMonthlyValue()
	{
		return monthlyValue;
	}
}
