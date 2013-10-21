package instances;


import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.network.serverpackets.components.*;
import lineage2.gameserver.network.serverpackets.*;
import lineage2.gameserver.utils.Location;

/**
 *
 * @author Awakeninger
 *
 */

public class Vullock extends Reflection {
	private static final int Vullock = 29218;
	private static final int VullockSlave = 29219;
	private static final int Golem1 = 23123;
	private static final int Golem2 = 23123;   
	private static final int Golem3 = 23123;
	private static final int Golem4 = 23123;
	private Player player;
	private Location Golem1Loc = new Location(152712,142936,-12762,57343);
	private Location Golem2Loc = new Location(154360,142936,-12762,40959);
	private Location Golem3Loc = new Location(154360,141288,-12762,25029);
	private Location Golem4Loc = new Location(152712,141288,-12762,8740);
    private Location vullockspawn = new Location(153576,142088,-12762,16383);
	private DeathListener _deathListener = new DeathListener();
	private CurrentHpListener _currentHpListener = new CurrentHpListener();
	private static final long BeforeDelay = 60 * 1000L;
	private static final long BeforeDelayVDO = 42 * 1000L;
	//private boolean _lockedTurn = false;
	private static Territory centralRoomPoint = new Territory().add(new Polygon().add(152712,142936).add(154360,142936).add(154360,141288).add(152712,141288).setZmax(-12862).setZmin(-12700));
	
   @Override
   public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
		ThreadPoolManager.getInstance().schedule(new FirstStage(), BeforeDelayVDO);
		ThreadPoolManager.getInstance().schedule(new VullockSpawn(this), BeforeDelay);
		
    }
	
	private class DeathListener implements OnDeathListener {
        @Override
        public void onDeath(Creature self, Creature killer) {
            if (self.isNpc() && self.getNpcId() == Vullock) {
			for (Player p : getPlayers()){
				p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));}
				startCollapseTimer(5 * 60 * 1000L);			
			}
		}
	}
			
	private class FirstStage extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			for(Player player : getPlayers())
				player.showQuestMovie(SceneMovie.si_barlog_opening);			
		}
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if(actor.getNpcId() == Vullock)
			{
				//_log.info("Target - Vullock");
				if(actor == null || actor.isDead())
					return;

				if(actor.getCurrentHp() <= 7850000) // С потолка.
				{	
					//_log.info("HP<75k");
					//actor.setIsInvul(true);
					//_lockedTurn = true;
					ThreadPoolManager.getInstance().schedule(new CaveStage(),0);
					actor.removeListener(_currentHpListener);
				}
			}
		}


	}
	
	public class CaveStage extends RunnableImpl {
        //Reflection _r1;

       // public CaveStage() {
      //      _r1 = r;
       // }
		

        @Override
        public void runImpl() {
		_log.info("Cave Active");
        NpcInstance Slave1 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave1.getAI().Attack(player, true, false);
		NpcInstance Slave2 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave2.getAI().Attack(player, true, false);
		NpcInstance Slave3 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave3.getAI().Attack(player, true, false);
		NpcInstance Slave4 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave4.getAI().Attack(player, true, false);
		NpcInstance Slave5 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave5.getAI().Attack(player, true, false);
		NpcInstance Slave6 = addSpawnWithoutRespawn(VullockSlave, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
		Slave6.getAI().Attack(player, true, false);

        }
    }
	
	public class VullockSpawn extends RunnableImpl {
        Reflection _r;

        public VullockSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
			Location Loc1 = Golem1Loc;
			Location Loc2 = Golem2Loc;
			Location Loc3 = Golem3Loc;
			Location Loc4 = Golem4Loc;
            Location Loc = vullockspawn;
			NpcInstance VullockStay = addSpawnWithoutRespawn(Vullock, Loc, 0);
			VullockStay.addListener(_deathListener);
			VullockStay.addListener(_currentHpListener);
			_r.addSpawnWithoutRespawn(Golem1, Loc1, 0);
			_r.addSpawnWithoutRespawn(Golem2, Loc2, 0);
			_r.addSpawnWithoutRespawn(Golem3, Loc3, 0);
			_r.addSpawnWithoutRespawn(Golem4, Loc4, 0);
        }
    }
}