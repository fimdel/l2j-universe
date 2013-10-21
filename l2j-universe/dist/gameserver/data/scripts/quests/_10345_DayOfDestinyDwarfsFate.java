package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10345_DayOfDestinyDwarfsFate extends SagasSuperclass implements ScriptFile 
{
	private int questId = 10345;
	
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
	
	public _10345_DayOfDestinyDwarfsFate()
	{
		super(false);

		StartNPC = 30847;
		StartRace = Race.dwarf;
		init();
		
	}

	@Override
	public int questId()
	{
		return questId;
	}
	
}

