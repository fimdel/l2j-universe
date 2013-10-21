package lineage2.gameserver.templates.skill.restoration;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;

public final class RestorationInfo
{
	private final int _skillId;
	private final int _skillLvl;
	private final int _itemConsumeId;
	private final int _itemConsumeCount;
	private final List<RestorationGroup> _restorationGroups;

	public RestorationInfo(int skillId, int skillLvl, int itemConsumeId, int itemConsumeCount)
	{
		this._skillId = skillId;
		this._skillLvl = skillLvl;
		this._itemConsumeId = itemConsumeId;
		this._itemConsumeCount = itemConsumeCount;
		this._restorationGroups = new ArrayList<RestorationGroup>();
	}

	public int getSkillId()
	{
		return this._skillId;
	}

	public int getSkillLvl()
	{
		return this._skillLvl;
	}

	public int getItemConsumeId()
	{
		return this._itemConsumeId;
	}

	public int getItemConsumeCount()
	{
		return this._itemConsumeCount;
	}

	public void addRestorationGroup(RestorationGroup group)
	{
		this._restorationGroups.add(group);
	}

	public List<RestorationItem> getRandomGroupItems()
	{
		double chancesAmount = 0.0D;
		for (RestorationGroup group : this._restorationGroups)
		{
			chancesAmount += group.getChance();
		}
		if (Rnd.chance(chancesAmount))
		{
			double chanceMod = (100.0D - chancesAmount) / this._restorationGroups.size();
			List<RestorationGroup> successGroups = new ArrayList<RestorationGroup>();
			int tryCount = 0;
			while (successGroups.isEmpty())
			{
				tryCount++;
				for (RestorationGroup group : this._restorationGroups)
				{
					if (tryCount % 10 == 0)
						chanceMod += 1.0D;
					if (Rnd.chance(group.getChance() + chanceMod))
						successGroups.add(group);
				}
			}
			RestorationGroup[] groupsArray = successGroups.toArray(new RestorationGroup[successGroups.size()]);
			return groupsArray[Rnd.get(groupsArray.length)].getRestorationItems();
		}
		return new ArrayList<RestorationItem>(0);
	}
}