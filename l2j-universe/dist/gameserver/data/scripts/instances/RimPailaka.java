package instances;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.Location;

/**
 * ����� ������������ Rim Pailaka - Rune
 *
 * @author pchayka
 */

public class RimPailaka extends Reflection
{
	private static final int SeducedKnight = 36562;
	private static final int SeducedRanger = 36563;
	private static final int SeducedMage = 36564;
	private static final int SeducedWarrior = 36565;
	private static final int KanadisGuide1 = 25659;
	private static final int KanadisGuide2 = 25660;
	private static final int KanadisGuide3 = 25661;
	private static final int KanadisFollower1 = 25662;
	private static final int KanadisFollower2 = 25663;
	private static final int KanadisFollower3 = 25664;
	private static final long initdelay = 30 * 1000L;
	private static final long firstwavedelay = 120 * 1000L;
	private static final long secondwavedelay = 480 * 1000L; // 8 ����� ����� ������ �����
	private static final long thirdwavedelay = 480 * 1000L; // 16 ����� ����� ������ �����

	private ScheduledFuture<?> initTask;
	private ScheduledFuture<?> firstwaveTask;
	private ScheduledFuture<?> secondWaveTask;
	private ScheduledFuture<?> thirdWaveTask;

	public RimPailaka()
	{
		super();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		ThreadPoolManager.getInstance().schedule(new CollapseTimer(10), (getInstancedZone().getTimelimit() - 10) * 60 * 1000L);
		initTask = ThreadPoolManager.getInstance().schedule(new InvestigatorsSpawn(), initdelay);
		firstwaveTask = ThreadPoolManager.getInstance().schedule(new FirstWave(), firstwavedelay);
	}

	public class InvestigatorsSpawn extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			Location ranger = new Location(49192, -12232, -9384, 0);
			Location mage = new Location(49192, -12456, -9392, 0);
			Location warrior = new Location(49192, -11992, -9392, 0);
			Location knight = new Location(49384, -12232, -9384, 0);
			addSpawnWithoutRespawn(SeducedKnight, knight, 0);
			addSpawnWithoutRespawn(SeducedRanger, ranger, 0);
			addSpawnWithoutRespawn(SeducedMage, mage, 0);
			addSpawnWithoutRespawn(SeducedWarrior, warrior, 0);
		}
	}

	public class FirstWave extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			List<Player> who = getPlayers();
			if(who != null && !who.isEmpty())
				for(Player player : who)
					player.sendPacket(new ExShowScreenMessage("First stage begins!", 3000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));

			Location bossnminions = new Location(50536, -12232, -9384, 32768);
			addSpawnWithoutRespawn(KanadisGuide1, bossnminions, 0);
			for(int i = 0; i < 10; i++)
				addSpawnWithoutRespawn(KanadisFollower1, bossnminions, 400);
			secondWaveTask = ThreadPoolManager.getInstance().schedule(new SecondWave(), secondwavedelay);
		}
	}

	public class SecondWave extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			List<Player> who = getPlayers();
			if(who != null && !who.isEmpty())
				for(Player player : who)
					player.sendPacket(new ExShowScreenMessage("Second stage begins!", 3000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));

			Location bossnminions = new Location(50536, -12232, -9384, 32768);
			addSpawnWithoutRespawn(KanadisGuide2, bossnminions, 0);
			for(int i = 0; i < 10; i++)
				addSpawnWithoutRespawn(KanadisFollower2, bossnminions, 400);
			thirdWaveTask = ThreadPoolManager.getInstance().schedule(new ThirdWave(), thirdwavedelay);
		}
	}

	public class ThirdWave extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			List<Player> who = getPlayers();
			if(who != null && !who.isEmpty())
				for(Player player : who)
					player.sendPacket(new ExShowScreenMessage("Third stage begins!", 3000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));

			Location bossnminions = new Location(50536, -12232, -9384, 32768);
			addSpawnWithoutRespawn(KanadisGuide3, bossnminions, 100);
			addSpawnWithoutRespawn(KanadisGuide3, bossnminions, 100);
			for(int i = 0; i < 10; i++)
				addSpawnWithoutRespawn(KanadisFollower3, bossnminions, 400);
		}
	}

	public class CollapseTimer extends RunnableImpl
	{
		private int _minutes = 0;

		public CollapseTimer(int minutes)
		{
			_minutes = minutes;
		}

		@Override
		public void runImpl() throws Exception
		{
			List<Player> who = getPlayers();
			if(who != null && !who.isEmpty())
				for(Player player : who)
					player.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(_minutes));
		}
	}

	@Override
	public void onCollapse()
	{
		if(initTask != null)
			initTask.cancel(true);
		if(firstwaveTask != null)
			firstwaveTask.cancel(true);
		if(secondWaveTask != null)
			secondWaveTask.cancel(true);
		if(thirdWaveTask != null)
			thirdWaveTask.cancel(true);

		super.onCollapse();
	}
}