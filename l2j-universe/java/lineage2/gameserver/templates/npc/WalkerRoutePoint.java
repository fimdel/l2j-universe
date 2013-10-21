package lineage2.gameserver.templates.npc;

import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

public class WalkerRoutePoint
{
	private final Location _loc;
	private final NpcString _phrase;
	private final int _socialActionId;
	private final int _delay;
	private final boolean _running;

	public WalkerRoutePoint(Location loc, NpcString phrase, int socialActionId, int delay, boolean running)
	{
		_loc = loc;
		_phrase = phrase;
		_socialActionId = socialActionId;
		_delay = delay;
		_running = running;
	}

	public Location getLocation()
	{
		return _loc;
	}

	public NpcString getPhrase()
	{
		return _phrase;
	}

	public int getSocialActionId()
	{
		return _socialActionId;
	}

	public int getDelay()
	{
		return _delay;
	}

	public boolean isRunning()
	{
		return _running;
	}
}