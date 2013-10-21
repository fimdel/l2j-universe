package lineage2.gameserver.templates.skill.restoration;

import java.util.ArrayList;
import java.util.List;

public final class RestorationGroup
{
	private final double _chance;
	private final List<RestorationItem> _restorationItems;

	public RestorationGroup(double chance)
	{
		this._chance = chance;
		this._restorationItems = new ArrayList<RestorationItem>();
	}

	public double getChance()
	{
		return this._chance;
	}

	public void addRestorationItem(RestorationItem item)
	{
		this._restorationItems.add(item);
	}

	public List<RestorationItem> getRestorationItems()
	{
		return this._restorationItems;
	}
}