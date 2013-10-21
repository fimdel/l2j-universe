package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10343_DayOfDestinyDarkElfsFate extends SagasSuperclass implements ScriptFile 
{
	private int questId = 10343;

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public _10343_DayOfDestinyDarkElfsFate()
	{
		super(false);

		StartNPC = 30862;
		StartRace = Race.darkelf;

		init();
	}
	
	@Override
	public int questId()
	{
		return questId;
	}

}