package instances;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.utils.Location;

/**
 *
 * @author Awakeninger
 */

public class AltarShilen extends Reflection {
    private static final int StartNPC1 = 32788;
	private static final int StartNPC2 = 32789;
	private static final int KeyStage1 = 23131;
	private static final int BossStage1 = 25857;
	//private static final int DoorEnter1 = 25180001;
	private static final int EndStage1 = 19123;
	private static final int KeyStage2 = 23138;
	private static final int BossStage2 = 25858;
	private Location NPC1Loc = new Location(179576, 13592, -7420);
	private Location NPC2Loc = new Location(179576, 13592, -9852);
	private Location KeyStage1Loc = new Location(179656, 18535, -8157);
	private Location KeyStage2Loc = new Location(179656, 13528, -8157);
	private Location Boss1Loc = new Location(178152, 14856, -8341, 16383);
	private Location Boss2Loc = new Location(178152, 14856, -10768, 16383);
	private Location End1Loc = new Location(178152, 13576, -8045);
	private Location End2Loc = new Location(178152, 13576, -10476);
	
	
	private static final long BeforeDelay = 0 * 1000L;
	@Override
   public void onPlayerEnter(Player player) {
        super.onPlayerEnter(player);
		ThreadPoolManager.getInstance().schedule(new CannonSpawn(this), BeforeDelay);
	}
	
	@Override
    public void onPlayerExit(Player player) {
        super.onPlayerExit(player);
    }
	
	public class CannonSpawn extends RunnableImpl {
        Reflection _r;

        public CannonSpawn(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
			Location Npc1Loc = NPC1Loc;
			Location Key1 = KeyStage1Loc;
			Location Boss1L = Boss1Loc;
			Location Altar1 = End1Loc;
			Location Npc2Loc = NPC2Loc;
			Location Key2 = KeyStage2Loc;
			Location Boss2L = Boss2Loc;
			Location Altar2 = End2Loc;
			_r.addSpawnWithoutRespawn(StartNPC1, Npc1Loc, 0);
			_r.addSpawnWithoutRespawn(KeyStage1, Key1, 0);
			_r.addSpawnWithoutRespawn(BossStage1, Boss1L, 0);
			_r.addSpawnWithoutRespawn(EndStage1, Altar1, 0);
			_r.addSpawnWithoutRespawn(StartNPC2, Npc2Loc, 0);
			_r.addSpawnWithoutRespawn(KeyStage2, Key2, 0);
			_r.addSpawnWithoutRespawn(BossStage2, Boss2L, 0);
			_r.addSpawnWithoutRespawn(EndStage1, Altar2, 0);
        }
    }

}