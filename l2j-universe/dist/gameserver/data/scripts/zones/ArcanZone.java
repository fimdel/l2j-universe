package zones;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.instancemanager.ArcanManager;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.network.serverpackets.EventTrigger;

public class ArcanZone implements ScriptFile
{
	private static final String TELEPORT_ZONE_NAME = "[Arcan_0]";
	private static ZoneListener _zoneListener;
	private static int _BLUE = 262001;
	private static int _RED = 262003;

	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
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
				return;

			if(cha == null)
				return;

			if(cha instanceof Player)
			{
				Player player = cha.getPlayer();
				if(ArcanManager.getStage() == _RED)
				{
					player.sendPacket(new EventTrigger(_BLUE, false));
					player.sendPacket(new EventTrigger(_RED, true));
				}
				else
				{
					player.sendPacket(new EventTrigger(_RED, false));
					player.sendPacket(new EventTrigger(_BLUE, true));
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}