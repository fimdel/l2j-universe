package zones;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ResidenceSide;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.network.serverpackets.ExCastleState;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.utils.ReflectionUtils;

public class SiegePiece implements ScriptFile
{
	private static ZoneListener _zoneListener;
	private ResidenceSide _side;
	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();

		for (Zone zone : ReflectionUtils.getZonesByType(ZoneType.peace_zone))
			zone.addListener(_zoneListener);
	}
	
	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
	
	public void broadcastPacket(int value, boolean b)
	{
		L2GameServerPacket trigger = new EventTrigger(value, b);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			player.sendPacket(trigger);

	}
	
	public void broadcastSend(Castle castle)
	{
		L2GameServerPacket trigger = new ExCastleState(castle);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			player.sendPacket(trigger);

	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (zone.getParams() == null || !cha.isPlayer())
				return;

			Castle castle = ResidenceHolder.getInstance().getResidence(zone.getTemplate().getIndex());

			if (castle != null)
			{	
				if(_side.ordinal()==1 || _side.ordinal()==0)
				{
				((Player) cha).sendPacket(new ExCastleState(castle));
				broadcastPacket(_side.ordinal(),true);
				broadcastSend(castle);
				Announcements.getInstance().announceToAll(new ExCastleState(castle));
				//broadcastPacket(_side.ordinal(),false);
				}
				else
				{
				((Player) cha).sendPacket(new ExCastleState(castle));
				broadcastPacket(_side.ordinal(),true);
				broadcastSend(castle);
				Announcements.getInstance().announceToAll(new ExCastleState(castle));
				//broadcastPacket(_side.ordinal(),false);
				}
				
				//return;
			}

				//Announcements.getInstance().announceToAll(new ExCastleState(castle));
		}	
			
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{}
	}
}