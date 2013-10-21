package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10344_DayOfDestinyOrcsFate extends SagasSuperclass implements ScriptFile 
{
	private int questId = 10344;

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

	public _10344_DayOfDestinyOrcsFate()
	{
		super(false);

		StartNPC = 30865;
		StartRace = Race.orc;

		init();
	}
	
	@Override
	public int questId()
	{
		return questId;
	}

}
