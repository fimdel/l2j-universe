package zones;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;
import instances.Nursery;

public class BirthingRoom implements ScriptFile
{

	private static final String[] zones = {
		"[Birthing_room_0]",
		"[Birthing_room_1]"
	};
	private static ZoneListener _zoneListener;
	private static final int InstanceId = 171;

	private void init()
	{
		_zoneListener = new ZoneListener();

		for(String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{

		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(zone == null)
			{
				return;
			}

			if(cha == null)
			{
				return;
			}

			Player player = cha.getPlayer();

			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(InstanceId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(InstanceId))
			{
				ReflectionUtils.enterReflection(player, new Nursery(), InstanceId);
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}