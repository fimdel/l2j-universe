package lineage2.gameserver.model.party;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnPlayerPartyInviteListener;
import lineage2.gameserver.listener.actor.player.OnPlayerPartyLeaveListener;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.ClassType2;
import lineage2.gameserver.network.serverpackets.ExWaitWaitingSubStituteInfo;
import lineage2.gameserver.network.serverpackets.PartySmallWindowUpdate;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class PartySubstitute
{
	private static Set<Player> _waitingParty = new HashSet<Player>();

	private static ConcurrentMap<Player, Integer> _waitingPlayer = new ConcurrentHashMap<Player, Integer>();

	public static final long TIME_OUT = 300000;

	public static final long ACCEPT_TIME_OUT = 180000;

	private static final long REFRASH_TIME = 60000;

	private static final int LEVEL_DIFF = 5;

	private static final int SKILL_INVUL_ID = 1750;

	private final SubstituteListeners _listener;

	public PartySubstitute()
	{
		_listener = new SubstituteListeners();
		CharListenerList.addGlobal(_listener);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new PartySubstituteTask(), REFRASH_TIME, REFRASH_TIME);
	}

	public void addPlayerToParty(Player p)
	{
		_waitingParty.add(p);
		p.sendPacket(new ExWaitWaitingSubStituteInfo(ExWaitWaitingSubStituteInfo.WAITING_OK));
		p.sendPacket(SystemMsg.STARTED_SEARCHING_THE_PARTY);
	}

	public boolean isPlayerToParty(Player p)
	{
		return _waitingParty.contains(p);
	}

	public void removePlayerFromParty(Player p)
	{
		_waitingParty.remove(p);
		p.sendPacket(new ExWaitWaitingSubStituteInfo(ExWaitWaitingSubStituteInfo.WAITING_CANCEL));
		p.sendPacket(SystemMsg.STOPPED_SEARCHING_THE_PARTY);
	}

	public void addPlayerToReplace(Player p)
	{
		_waitingPlayer.put(p, 0);
		p.getParty().broadCast(new PartySmallWindowUpdate(p));
	}

	public void updatePlayerToReplace(Player p, int i)
	{
		_waitingPlayer.put(p, i);
	}

	public boolean isPlayerToReplace(Player p)
	{
		return _waitingPlayer.containsKey(p);
	}

	public void removePlayerReplace(Player p)
	{
		_waitingPlayer.remove(p);
	}

	private class SubstituteListeners implements OnPlayerExitListener, OnPlayerPartyLeaveListener, OnPlayerPartyInviteListener
	{
		@Override
		public void onPlayerExit(Player player)
		{
			_waitingParty.remove(player);
			_waitingPlayer.remove(player);
		}

		@Override
		public void onPartyLeave(Player player)
		{
			removePlayerReplace(player);
		}

		@Override
		public void onPartyInvite(Player player)
		{
			synchronized (this)
			{
				removePlayerFromParty(player);
			}
		}
	}

	public void doReplace(Player replaceWho, Player replaceTo)
	{
		if (replaceWho == null || replaceTo == null)
			return;

		Party p = replaceWho.getParty();

		if (p == null)
			return;

		Player leader = p.getPartyLeader();

		if (p.getMemberCount() == Party.MAX_SIZE)
		{
			p.removePartyMember(replaceWho, true);
			p.addPartyMember(replaceTo);
		}
		else
		{
			p.addPartyMember(replaceTo);
			p.removePartyMember(replaceWho, true);
		}

		Location loc = leader.getLoc();

		replaceTo.setLoc(loc);

		Skill skill = SkillTable.getInstance().getInfo(SKILL_INVUL_ID, 1);

		for (Player pp : p.getPartyMembers())
		{
			for (EffectTemplate et : skill.getEffectTemplates())
			{
				Env env = new Env(pp, pp, skill);
				Effect effect = et.getEffect(env);
				effect.setPeriod(10000);
				pp.getEffectList().addEffect(effect);
			}
		}
	}

	public boolean isGoodPlayer(Player a, Player b)
	{
		int plvl = a.getLevel();
		int tlvl = b.getLevel();

		int d_lvl = Math.abs(plvl - tlvl);

		if (d_lvl > LEVEL_DIFF)
			return false;

		ClassType2 ca = a.getClassId().getType2();
		ClassType2 cb = b.getClassId().getType2();

		if (ca != null && cb != null)
			if (ca == cb)
				return false;

		return true;
	}

	public Set<Player> getWaitingParty()
	{
		return _waitingParty;
	}

	public ConcurrentMap<Player, Integer> getWaitingPlayer()
	{
		return _waitingPlayer;
	}

	public static PartySubstitute getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final PartySubstitute _instance = new PartySubstitute();
	}
}
