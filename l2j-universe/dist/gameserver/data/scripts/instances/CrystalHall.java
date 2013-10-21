package instances;

import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 *
 * @author Awakeninger
 */

public class CrystalHall extends Reflection {
    private static final int RB1 = 25881;
	private static final int RB2 = 25881;
	private static final int Cannon1 = 19008;
	private static final int Cannon2 = 19008;   
	private static final int Cannon3 = 19008;
	private static final int Cannon4 = 19008; 
	private static final int Cannon5 = 19008;
	private static final int Cannon6 = 19008;   
	private static final int Cannon7 = 19008;
	private static final int Cannon8 = 19009;
	private static final int Exchanger = 33388;
	private static final int DoorOutside = 24220005;
	private static final int DoorInside = 24220006;
	private long _savedTime;
	private Location Cannon1Loc = new Location(143144, 145832, -12061);
	private Location Cannon2Loc = new Location(141912, 144200, -11949);
	private Location Cannon3Loc = new Location(143368, 143768, -11976);
	private Location Cannon4Loc = new Location(145544, 143746, -11841);
	private Location Cannon5Loc = new Location(147544, 144872, -12251);
	private Location Cannon6Loc = new Location(148952, 145224, -12326);
	private Location Cannon7Loc = new Location(148152, 146136, -12305);
	private Location Cannon8Loc = new Location(149096, 146872, -12369);
    private Location RB1Loc = new Location(152984, 145960, -12609, 15640);
	private Location RB2Loc = new Location(152536, 145960, -12609, 15640);
	private DeathListener _deathListener = new DeathListener();
	@Override
   public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
		_savedTime = System.currentTimeMillis();
		player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcString.ELAPSED_TIME));
		NpcInstance can8 = addSpawnWithoutRespawn(Cannon8, Cannon8Loc, 0);
		can8.addListener(_deathListener);
		NpcInstance can1 = addSpawnWithoutRespawn(Cannon1, Cannon1Loc, 0);
		can1.addListener(_deathListener);
		NpcInstance can2 = addSpawnWithoutRespawn(Cannon2, Cannon2Loc, 0);
		can2.addListener(_deathListener);
		NpcInstance can3 = addSpawnWithoutRespawn(Cannon3, Cannon3Loc, 0);
		can3.addListener(_deathListener);
		NpcInstance can4 = addSpawnWithoutRespawn(Cannon4, Cannon4Loc, 0);
		can4.addListener(_deathListener);
		NpcInstance can5 = addSpawnWithoutRespawn(Cannon5, Cannon5Loc, 0);
		can5.addListener(_deathListener);
		NpcInstance can6 = addSpawnWithoutRespawn(Cannon6, Cannon6Loc, 0);
		can6.addListener(_deathListener);
		NpcInstance can7 = addSpawnWithoutRespawn(Cannon7, Cannon7Loc, 0);
		can7.addListener(_deathListener);
		NpcInstance RB1N = addSpawnWithoutRespawn(RB1, RB1Loc, 0);
		RB1N.addListener(_deathListener);
		NpcInstance RB2N = addSpawnWithoutRespawn(RB2, RB2Loc, 0);
		RB2N.addListener(_deathListener);
    }
	
	
	private class DeathListener implements OnDeathListener {
        @Override
        public void onDeath(Creature self, Creature killer) {
            if (self.isNpc() && self.getNpcId() == Cannon1) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 1));}
			}else if (self.isNpc() && self.getNpcId() == Cannon2) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 2));}
			}else if (self.isNpc() && self.getNpcId() == Cannon3) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 3));}
			}else if (self.isNpc() && self.getNpcId() == Cannon4) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 4));}
			}else if (self.isNpc() && self.getNpcId() == Cannon5) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 5));}
			}else if (self.isNpc() && self.getNpcId() == Cannon6) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 6));}
			}else if (self.isNpc() && self.getNpcId() == Cannon7) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, 7));}
			}else if (self.isNpc() && self.getNpcId() == Cannon8) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExShowScreenMessage(NpcString.Success_destroying_open_door, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true));}
				getDoor(DoorOutside).openMe();
				getDoor(DoorInside).openMe();
			}else if (self.isNpc() && self.getNpcId() == RB1 && self.getNpcId() == RB2) {
			for (Player p : getPlayers()){
				p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
				p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));}
				startCollapseTimer(5 * 60 * 1000L);	
				addSpawnWithoutRespawn(Exchanger, RB2Loc, 0);
			}
		}
	}
}