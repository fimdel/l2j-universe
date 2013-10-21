package lineage2.gameserver.model.worldstatistics;

/**
 * @author Дмитрий
 * @date 10.10.12 20:51
 */
public class CharacterStatistic
{
	private int objId;
	private String name;
	private CharacterStatisticElement statisticElement;
	private int clanObjId = 0;
	private boolean clanCrestId;

	public CharacterStatistic(int objId, String name, CharacterStatisticElement statisticElement)
	{
		this.objId = objId;
		this.name = name;
		this.statisticElement = statisticElement;
	}

	public int getObjId()
	{
		return objId;
	}

	public String getName()
	{
		return name;
	}

	public long getValue()
	{
		return statisticElement.getValue();
	}

	public int getClanObjId()
	{
		return clanObjId;
	}

	public boolean getClanCrestId()
	{
		return clanCrestId;
	}

}
