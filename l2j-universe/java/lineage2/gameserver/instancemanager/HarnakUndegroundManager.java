package lineage2.gameserver.instancemanager;

import gnu.trove.map.hash.TIntObjectHashMap;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.NpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy
 */
public class HarnakUndegroundManager
{
	private static final Logger _log = LoggerFactory.getLogger(HarnakUndegroundManager.class);
	private static HarnakUndegroundManager _instance;
	private static int secondStageAltarCount;
	private static int thirdStageAltarCount;
	private static int HARNAK = 25772;
	private static int ANGRY_HARNAK = 25774;
	private static int DEMONIC_NOKTUM = 25773;
	private static TIntObjectHashMap<Integer> npcListIdSecond;
	private static TIntObjectHashMap<Integer> npcListIdThird;

	public HarnakUndegroundManager()
	{
		npcListIdSecond = new TIntObjectHashMap<Integer>();
		npcListIdThird = new TIntObjectHashMap<Integer>();
		secondStageAltarCount = 0;
		thirdStageAltarCount = 0;
		_log.info("Harnak Undeground Manager: Loaded");
	}

	public static HarnakUndegroundManager getInstance()
	{
		if (_instance == null)
			_instance = new HarnakUndegroundManager();
		return _instance;
	}

	public boolean addSecondStagePoint(int npcId, final Player player)
	{
		if(requestAddNpcToListSecond(npcId))
		{
			if(secondStageAltarCount < 8)
				secondStageAltarCount++;
			if(secondStageAltarCount == 8)
			{
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if(player.isInParty())
						{
							for(Player players : player.getParty().getPartyMembers())
							{
								if(players.isInRange(player, 1000))
								{
									players.sendPacket(new ExShowScreenMessage(NpcString.ALL_SEALS_HAVE_BEEN_BROKEN_GHOST_OF_HARNAK_WILL_APPEAR_SOON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
									player.broadcastPacket(new Earthquake(players.getLoc(), 50, 4));
								}
							}
						}
						else
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.ALL_SEALS_HAVE_BEEN_BROKEN_GHOST_OF_HARNAK_WILL_APPEAR_SOON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
							player.broadcastPacket(new Earthquake(player.getLoc(), 50, 4));
						}
						secondStageAltarCount = 0;
						NpcUtils.spawnSingle(HARNAK, -114712, 149256, -10800, 47671, 1800000L);
					}
				}, 10000L);
			}
			return true;
		}
		return false;
	}

	public boolean addThirdStagePoint(int npcId, final Player player)
	{
		if(requestAddNpcToListThird(npcId))
		{
			if(thirdStageAltarCount < 8)
				thirdStageAltarCount++;
			if(thirdStageAltarCount == 8)
			{
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						if(player.isInParty())
						{
							for(Player players : player.getParty().getPartyMembers())
							{
								if(players.isInRange(player, 1000))
								{
									players.sendPacket(new ExShowScreenMessage(NpcString.ALL_JAILS_ARE_OPEN_GHOST_OF_HARNAK_WILL_APPEAR_SOON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
									player.broadcastPacket(new Earthquake(player.getLoc(), 50, 4));
								}
							}
						}
						else
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.ALL_JAILS_ARE_OPEN_GHOST_OF_HARNAK_WILL_APPEAR_SOON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
							player.broadcastPacket(new Earthquake(player.getLoc(), 50, 4));
						}
						thirdStageAltarCount = 0;
						NpcUtils.spawnSingle(ANGRY_HARNAK, -114712, 183352, -13820, 49151, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -114696, 182872, -13846, 48112, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -114360, 183016, -13846, 58824, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -114216, 183352, -13846, 1297, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -114360, 183688, -13846, 7738, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -114712, 183816, -13846, 16383, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -115048, 183688, -13846, 26634, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -115192, 183352, -13846, 34065, 1800000L);
						NpcUtils.spawnSingle(DEMONIC_NOKTUM, -115032, 183016, -13846, 40463, 1800000L);
					}
				}, 10000L);
			}
			return true;
		}
		return false;
	}

	public boolean requestAddNpcToListSecond(int npcId)
	{
		if(npcListIdSecond.get(npcId) == null)
		{
			npcListIdSecond.put(npcId, Integer.valueOf(npcId));
			return true;
		}
		return false;
	}

	public void requestRemoveNpcFromListSecond(int npcId)
	{
		npcListIdSecond.remove(npcId);
		if(secondStageAltarCount > 0)
			secondStageAltarCount--;
	}

	public boolean requestAddNpcToListThird(int npcId)
	{
		if(npcListIdThird.get(npcId) == null)
		{
			npcListIdThird.put(npcId, Integer.valueOf(npcId));
			return true;
		}
		return false;
	}

	public void requestRemoveNpcFromListThird(int npcId)
	{
		npcListIdThird.remove(npcId);
		if(thirdStageAltarCount > 0)
			thirdStageAltarCount--;
	}
}