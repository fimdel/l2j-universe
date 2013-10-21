package ai.octavis;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class OctavisRider extends Pointer
{

	public OctavisRider(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 250;

		_points = new Location[] {
				//new Location(181911, 114835, -7678),
				//new Location(182824, 114808, -7906),
				//new Location(182536, 115224, -7836),
				//new Location(182104, 115160, -7735),
				//new Location(181480, 114936, -7702)
				
				new Location(207704, 120792, -10038),
				new Location(207416, 121080, -10038),
				new Location(207016, 121080, -10038),
				new Location(206696, 120776, -10038),
				new Location(206696, 120360, -10038),
				new Location(206984, 120088, -10038),
				new Location(207400, 120072, -10038),
				new Location(207672, 120360, -10038)
				};
	}

}