package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10342_DayOfDestinyElvenFate extends SagasSuperclass implements ScriptFile 
{
	private int questId = 10342;
	
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

	public _10342_DayOfDestinyElvenFate()
	{
		super(false);

		StartNPC = 30856;
		StartRace = Race.elf;

		init();
	}
	
	@Override
	public int questId()
	{
		return questId;
	}

}
