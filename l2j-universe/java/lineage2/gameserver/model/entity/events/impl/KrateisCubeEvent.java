/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity.events.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.KrateisCubePlayerObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExPVPMatchCCMyRecord;
import lineage2.gameserver.network.serverpackets.ExPVPMatchCCRecord;
import lineage2.gameserver.network.serverpackets.ExPVPMatchCCRetire;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisCubeEvent extends GlobalEvent
{
	/**
	 * @author Mobius
	 */
	private class Listeners implements OnKillListener, OnPlayerExitListener, OnTeleportListener
	{
		/**
		 * Constructor for Listeners.
		 */
		public Listeners()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onKill.
		 * @param actor Creature
		 * @param victim Creature
		 * @see lineage2.gameserver.listener.actor.OnKillListener#onKill(Creature, Creature)
		 */
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if (!victim.isPlayer())
			{
				return;
			}
			
			KrateisCubeEvent cubeEvent2 = victim.getEvent(KrateisCubeEvent.class);
			if (cubeEvent2 != KrateisCubeEvent.this)
			{
				return;
			}
			
			KrateisCubePlayerObject winnerPlayer = getParticlePlayer((Player) actor);
			
			winnerPlayer.setPoints(winnerPlayer.getPoints() + 5);
			updatePoints(winnerPlayer);
			
			KrateisCubePlayerObject looserPlayer = getParticlePlayer((Player) victim);
			
			looserPlayer.startRessurectTask();
		}
		
		/**
		 * Method ignorePetOrSummon.
		 * @return boolean * @see lineage2.gameserver.listener.actor.OnKillListener#ignorePetOrSummon()
		 */
		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
		
		/**
		 * Method onPlayerExit.
		 * @param player Player
		 * @see lineage2.gameserver.listener.actor.player.OnPlayerExitListener#onPlayerExit(Player)
		 */
		@Override
		public void onPlayerExit(Player player)
		{
			exitCube(player, false);
		}
		
		/**
		 * Method onTeleport.
		 * @param player Player
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param reflection Reflection
		 * @see lineage2.gameserver.listener.actor.player.OnTeleportListener#onTeleport(Player, int, int, int, Reflection)
		 */
		@Override
		public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
		{
			List<Location> waitLocs = getObjects(WAIT_LOCS);
			for (Location loc : waitLocs)
			{
				if ((loc.x == x) && (loc.y == y))
				{
					return;
				}
			}
			
			waitLocs = getObjects(TELEPORT_LOCS);
			
			for (Location loc : waitLocs)
			{
				if ((loc.x == x) && (loc.y == y))
				{
					return;
				}
			}
			
			exitCube(player, false);
		}
	}
	
	/**
	 * Field DATE_PATTERN.
	 */
	private static final SchedulingPattern DATE_PATTERN = new SchedulingPattern("0,30 * * * *");
	/**
	 * Field RETURN_LOC.
	 */
	private static final Location RETURN_LOC = new Location(-70381, -70937, -1428);
	/**
	 * Field SKILL_IDS.
	 */
	private static final int[] SKILL_IDS =
	{
		1086,
		1204,
		1059,
		1085,
		1078,
		1068,
		1240,
		1077,
		1242,
		1062,
		5739
	};
	/**
	 * Field SKILL_LEVEL.
	 */
	private static final int[] SKILL_LEVEL =
	{
		2,
		2,
		3,
		3,
		6,
		3,
		3,
		3,
		3,
		2,
		1
	};
	
	/**
	 * Field PARTICLE_PLAYERS. (value is ""particle_players"")
	 */
	public static final String PARTICLE_PLAYERS = "particle_players";
	/**
	 * Field REGISTERED_PLAYERS. (value is ""registered_players"")
	 */
	public static final String REGISTERED_PLAYERS = "registered_players";
	/**
	 * Field WAIT_LOCS. (value is ""wait_locs"")
	 */
	public static final String WAIT_LOCS = "wait_locs";
	/**
	 * Field TELEPORT_LOCS. (value is ""teleport_locs"")
	 */
	public static final String TELEPORT_LOCS = "teleport_locs";
	/**
	 * Field PREPARE. (value is ""prepare"")
	 */
	public static final String PREPARE = "prepare";
	
	/**
	 * Field _minLevel.
	 */
	private final int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private final int _maxLevel;
	
	/**
	 * Field _calendar.
	 */
	private final Calendar _calendar = Calendar.getInstance();
	
	/**
	 * Field _runnerEvent.
	 */
	private KrateisCubeRunnerEvent _runnerEvent;
	/**
	 * Field _listeners.
	 */
	private final Listeners _listeners = new Listeners();
	
	/**
	 * Constructor for KrateisCubeEvent.
	 * @param set MultiValueSet<String>
	 */
	public KrateisCubeEvent(MultiValueSet<String> set)
	{
		super(set);
		_minLevel = set.getInteger("min_level");
		_maxLevel = set.getInteger("max_level");
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	public void initEvent()
	{
		_runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 2);
		
		super.initEvent();
	}
	
	/**
	 * Method prepare.
	 */
	public void prepare()
	{
		NpcInstance npc = _runnerEvent.getNpc();
		List<KrateisCubePlayerObject> registeredPlayers = removeObjects(REGISTERED_PLAYERS);
		List<Location> waitLocs = getObjects(WAIT_LOCS);
		for (KrateisCubePlayerObject k : registeredPlayers)
		{
			if (npc.getDistance(k.getPlayer()) > 800)
			{
				continue;
			}
			
			addObject(PARTICLE_PLAYERS, k);
			
			Player player = k.getPlayer();
			
			player.teleToLocation(Rnd.get(waitLocs), ReflectionManager.DEFAULT);
		}
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		super.startEvent();
		
		List<KrateisCubePlayerObject> players = getObjects(PARTICLE_PLAYERS);
		List<Location> teleportLocs = getObjects(TELEPORT_LOCS);
		
		for (int i = 0; i < players.size(); i++)
		{
			KrateisCubePlayerObject k = players.get(i);
			Player player = k.getPlayer();
			
			player.getEffectList().stopAllEffects();
			
			giveEffects(player);
			
			player.teleToLocation(teleportLocs.get(i));
			player.addEvent(this);
			
			player.sendPacket(new ExPVPMatchCCMyRecord(k), SystemMsg.THE_MATCH_HAS_STARTED);
		}
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public void stopEvent()
	{
		super.stopEvent();
		reCalcNextTime(false);
		
		double dif = 0.05;
		int pos = 0;
		
		List<KrateisCubePlayerObject> players = removeObjects(PARTICLE_PLAYERS);
		for (KrateisCubePlayerObject krateisPlayer : players)
		{
			Player player = krateisPlayer.getPlayer();
			pos++;
			if (krateisPlayer.getPoints() >= 10)
			{
				int count = (int) (krateisPlayer.getPoints() * dif * (1.0 + ((players.size() / pos) * 0.04)));
				dif -= 0.0016;
				if (count > 0)
				{
					Functions.addItem(player, 13067, count);
					
					int exp = count * 2880;
					int sp = count * 288;
					player.addExpAndSp(exp, sp);
				}
			}
			
			player.removeEvent(this);
			
			player.sendPacket(ExPVPMatchCCRetire.STATIC, SystemMsg.END_MATCH);
			player.teleToLocation(RETURN_LOC);
		}
	}
	
	/**
	 * Method giveEffects.
	 * @param player Player
	 */
	private void giveEffects(Player player)
	{
		player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
		player.setCurrentCp(player.getMaxCp());
		
		for (int j = 0; j < SKILL_IDS.length; j++)
		{
			Skill skill = SkillTable.getInstance().getInfo(SKILL_IDS[j], SKILL_LEVEL[j]);
			if (skill != null)
			{
				skill.getEffects(player, player, false, false);
			}
		}
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		
		_calendar.setTimeInMillis(DATE_PATTERN.next(System.currentTimeMillis()));
		
		registerActions();
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return _calendar.getTimeInMillis();
	}
	
	/**
	 * Method canRessurect.
	 * @param resurrectPlayer Player
	 * @param creature Creature
	 * @param force boolean
	 * @return boolean
	 */
	@Override
	public boolean canRessurect(Player resurrectPlayer, Creature creature, boolean force)
	{
		resurrectPlayer.sendPacket(SystemMsg.INVALID_TARGET);
		return false;
	}
	
	/**
	 * Method getRegisteredPlayer.
	 * @param player Player
	 * @return KrateisCubePlayerObject
	 */
	public KrateisCubePlayerObject getRegisteredPlayer(Player player)
	{
		List<KrateisCubePlayerObject> registeredPlayers = getObjects(REGISTERED_PLAYERS);
		for (KrateisCubePlayerObject p : registeredPlayers)
		{
			if (p.getPlayer() == player)
			{
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Method getParticlePlayer.
	 * @param player Player
	 * @return KrateisCubePlayerObject
	 */
	public KrateisCubePlayerObject getParticlePlayer(Player player)
	{
		List<KrateisCubePlayerObject> registeredPlayers = getObjects(PARTICLE_PLAYERS);
		for (KrateisCubePlayerObject p : registeredPlayers)
		{
			if (p.getPlayer() == player)
			{
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Method showRank.
	 * @param player Player
	 */
	public void showRank(Player player)
	{
		KrateisCubePlayerObject particlePlayer = getParticlePlayer(player);
		if ((particlePlayer == null) || particlePlayer.isShowRank())
		{
			return;
		}
		
		particlePlayer.setShowRank(true);
		
		player.sendPacket(new ExPVPMatchCCRecord(this));
	}
	
	/**
	 * Method closeRank.
	 * @param player Player
	 */
	public void closeRank(Player player)
	{
		KrateisCubePlayerObject particlePlayer = getParticlePlayer(player);
		if ((particlePlayer == null) || !particlePlayer.isShowRank())
		{
			return;
		}
		
		particlePlayer.setShowRank(false);
	}
	
	/**
	 * Method updatePoints.
	 * @param k KrateisCubePlayerObject
	 */
	public void updatePoints(KrateisCubePlayerObject k)
	{
		k.getPlayer().sendPacket(new ExPVPMatchCCMyRecord(k));
		
		final ExPVPMatchCCRecord p = new ExPVPMatchCCRecord(this);
		
		List<KrateisCubePlayerObject> players = getObjects(PARTICLE_PLAYERS);
		for (KrateisCubePlayerObject $player : players)
		{
			if ($player.isShowRank())
			{
				$player.getPlayer().sendPacket(p);
			}
		}
	}
	
	/**
	 * Method getSortedPlayers.
	 * @return KrateisCubePlayerObject[]
	 */
	public KrateisCubePlayerObject[] getSortedPlayers()
	{
		List<KrateisCubePlayerObject> players = getObjects(PARTICLE_PLAYERS);
		KrateisCubePlayerObject[] array = players.toArray(new KrateisCubePlayerObject[players.size()]);
		ArrayUtils.eqSort(array);
		return array;
	}
	
	/**
	 * Method exitCube.
	 * @param player Player
	 * @param teleport boolean
	 */
	public void exitCube(Player player, boolean teleport)
	{
		KrateisCubePlayerObject krateisCubePlayer = getParticlePlayer(player);
		krateisCubePlayer.stopRessurectTask();
		
		getObjects(PARTICLE_PLAYERS).remove(krateisCubePlayer);
		
		player.sendPacket(ExPVPMatchCCRetire.STATIC);
		player.removeEvent(this);
		
		if (teleport)
		{
			player.teleToLocation(RETURN_LOC);
		}
	}
	
	/**
	 * Method announce.
	 * @param a int
	 */
	@Override
	public void announce(int a)
	{
		IStaticPacket p = null;
		if (a > 0)
		{
			p = new SystemMessage2(SystemMsg.S1_SECONDS_TO_GAME_END).addInteger(a);
		}
		else
		{
			p = new SystemMessage2(SystemMsg.THE_MATCH_WILL_START_IN_S1_SECONDS).addInteger(-a);
		}
		
		List<KrateisCubePlayerObject> players = getObjects(PARTICLE_PLAYERS);
		for (KrateisCubePlayerObject $player : players)
		{
			$player.getPlayer().sendPacket(p);
		}
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		return getParticlePlayer(player) != null;
	}
	
	/**
	 * Method onAddEvent.
	 * @param o GameObject
	 */
	@Override
	public void onAddEvent(GameObject o)
	{
		if (o.isPlayer())
		{
			o.getPlayer().addListener(_listeners);
		}
	}
	
	/**
	 * Method onRemoveEvent.
	 * @param o GameObject
	 */
	@Override
	public void onRemoveEvent(GameObject o)
	{
		if (o.isPlayer())
		{
			o.getPlayer().removeListener(_listeners);
		}
	}
	
	/**
	 * Method action.
	 * @param name String
	 * @param start boolean
	 */
	@Override
	public void action(String name, boolean start)
	{
		if (name.equalsIgnoreCase(PREPARE))
		{
			prepare();
		}
		else
		{
			super.action(name, start);
		}
	}
	
	/**
	 * Method checkRestartLocs.
	 * @param player Player
	 * @param r Map<RestartType,Boolean>
	 */
	@Override
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		r.clear();
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	/**
	 * Method isInProgress.
	 * @return boolean
	 */
	@Override
	public boolean isInProgress()
	{
		return _runnerEvent.isInProgress();
	}
	
	/**
	 * Method isRegistrationOver.
	 * @return boolean
	 */
	public boolean isRegistrationOver()
	{
		return _runnerEvent.isRegistrationOver();
	}
}
