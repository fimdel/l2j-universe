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

import java.util.Iterator;
import java.util.List;

import lineage2.commons.collections.JoinedIterator;
import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.DuelSnapshotObject;
import lineage2.gameserver.network.serverpackets.ExDuelStart;
import lineage2.gameserver.network.serverpackets.ExDuelUpdateUserInfo;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class DuelEvent extends GlobalEvent implements Iterable<DuelSnapshotObject>
{
	/**
	 * @author Mobius
	 */
	private class OnPlayerExitListenerImpl implements OnPlayerExitListener
	{
		/**
		 * Constructor for OnPlayerExitListenerImpl.
		 */
		public OnPlayerExitListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onPlayerExit.
		 * @param player Player
		 * @see lineage2.gameserver.listener.actor.player.OnPlayerExitListener#onPlayerExit(Player)
		 */
		@Override
		public void onPlayerExit(Player player)
		{
			playerExit(player);
		}
	}
	
	/**
	 * Field RED_TEAM.
	 */
	public static final String RED_TEAM = TeamType.RED.name();
	/**
	 * Field BLUE_TEAM.
	 */
	public static final String BLUE_TEAM = TeamType.BLUE.name();
	
	/**
	 * Field _playerExitListener.
	 */
	protected OnPlayerExitListener _playerExitListener = new OnPlayerExitListenerImpl();
	/**
	 * Field _winner.
	 */
	protected TeamType _winner = TeamType.NONE;
	/**
	 * Field _aborted.
	 */
	protected boolean _aborted;
	
	/**
	 * Constructor for DuelEvent.
	 * @param set MultiValueSet<String>
	 */
	public DuelEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Constructor for DuelEvent.
	 * @param id int
	 * @param name String
	 */
	protected DuelEvent(int id, String name)
	{
		super(id, name);
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	public void initEvent()
	{
	}
	
	/**
	 * Method canDuel.
	 * @param player Player
	 * @param target Player
	 * @param first boolean
	 * @return boolean
	 */
	public abstract boolean canDuel(Player player, Player target, boolean first);
	
	/**
	 * Method askDuel.
	 * @param player Player
	 * @param target Player
	 */
	public abstract void askDuel(Player player, Player target);
	
	/**
	 * Method createDuel.
	 * @param player Player
	 * @param target Player
	 */
	public abstract void createDuel(Player player, Player target);
	
	/**
	 * Method playerExit.
	 * @param player Player
	 */
	public abstract void playerExit(Player player);
	
	/**
	 * Method packetSurrender.
	 * @param player Player
	 */
	public abstract void packetSurrender(Player player);
	
	/**
	 * Method onDie.
	 * @param player Player
	 */
	public abstract void onDie(Player player);
	
	/**
	 * Method getDuelType.
	 * @return int
	 */
	public abstract int getDuelType();
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		updatePlayers(true, false);
		
		sendPackets(new ExDuelStart(this), PlaySound.B04_S01, SystemMsg.LET_THE_DUEL_BEGIN);
		
		for (DuelSnapshotObject $snapshot : this)
		{
			sendPacket(new ExDuelUpdateUserInfo($snapshot.getPlayer()), $snapshot.getTeam().revert().name());
		}
	}
	
	/**
	 * Method sendPacket.
	 * @param packet IStaticPacket
	 * @param ar String[]
	 */
	public void sendPacket(IStaticPacket packet, String... ar)
	{
		for (String a : ar)
		{
			List<DuelSnapshotObject> objs = getObjects(a);
			
			for (DuelSnapshotObject obj : objs)
			{
				obj.getPlayer().sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method sendPacket.
	 * @param packet IStaticPacket
	 */
	public void sendPacket(IStaticPacket packet)
	{
		sendPackets(packet);
	}
	
	/**
	 * Method sendPackets.
	 * @param packet IStaticPacket[]
	 */
	public void sendPackets(IStaticPacket... packet)
	{
		for (DuelSnapshotObject d : this)
		{
			d.getPlayer().sendPacket(packet);
		}
	}
	
	/**
	 * Method abortDuel.
	 * @param player Player
	 */
	public void abortDuel(Player player)
	{
		_aborted = true;
		_winner = TeamType.NONE;
		
		stopEvent();
	}
	
	/**
	 * Method canDuel0.
	 * @param requestor Player
	 * @param target Player
	 * @return IStaticPacket
	 */
	protected IStaticPacket canDuel0(Player requestor, Player target)
	{
		IStaticPacket packet = null;
		if (target.isInCombat())
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_ENGAGED_IN_BATTLE).addName(target);
		}
		else if (target.isDead() || target.isAlikeDead() || (target.getCurrentHpPercents() < 50) || (target.getCurrentMpPercents() < 50) || (target.getCurrentCpPercents() < 50))
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1S_HP_OR_MP_IS_BELOW_50).addName(target);
		}
		else if (target.getEvent(DuelEvent.class) != null)
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_ALREADY_ENGAGED_IN_A_DUEL).addName(target);
		}
		else if ((target.getEvent(ClanHallSiegeEvent.class) != null) || (target.getEvent(ClanHallNpcSiegeEvent.class) != null))
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_A_CLAN_HALL_WAR).addName(target);
		}
		else if (target.getEvent(SiegeEvent.class) != null)
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_A_SIEGE_WAR).addName(target);
		}
		else if (target.isInOlympiadMode())
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_PARTICIPATING_IN_THE_OLYMPIAD).addName(target);
		}
		else if (target.isCursedWeaponEquipped() || (target.isChaotic()) || (target.getPvpFlag() > 0))
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_IN_A_CHAOTIC_STATE).addName(target);
		}
		else if (target.isInStoreMode())
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE).addName(target);
		}
		else if (target.isMounted() || target.isInBoat())
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_RIDING_A_BOAT_STEED_OR_STRIDER).addName(target);
		}
		else if (target.isFishing())
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_FISHING).addName(target);
		}
		else if (target.isInCombatZone() || target.isInPeaceZone() || target.isInWater() || target.isInZone(Zone.ZoneType.no_restart))
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_MAKE_A_CHALLENGE_TO_A_DUEL_BECAUSE_C1_IS_CURRENTLY_IN_A_DUELPROHIBITED_AREA_PEACEFUL_ZONE__SEVEN_SIGNS_ZONE__NEAR_WATER__RESTART_PROHIBITED_AREA).addName(target);
		}
		else if (!requestor.isInRangeZ(target, 1200))
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_C1_IS_TOO_FAR_AWAY).addName(target);
		}
		else if (target.getTransformation() != 0)
		{
			packet = new SystemMessage2(SystemMsg.C1_CANNOT_DUEL_BECAUSE_C1_IS_CURRENTLY_POLYMORPHED).addName(target);
		}
		return packet;
	}
	
	/**
	 * Method updatePlayers.
	 * @param start boolean
	 * @param teleport boolean
	 */
	protected void updatePlayers(boolean start, boolean teleport)
	{
		for (DuelSnapshotObject $snapshot : this)
		{
			if (teleport)
			{
				$snapshot.teleport();
			}
			else
			{
				if (start)
				{
					$snapshot.getPlayer().addEvent(this);
					$snapshot.getPlayer().setTeam($snapshot.getTeam());
				}
				else
				{
					$snapshot.getPlayer().removeEvent(this);
					$snapshot.restore(_aborted);
					
					$snapshot.getPlayer().setTeam(TeamType.NONE);
				}
			}
		}
	}
	
	/**
	 * Method checkForAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if ((target.getTeam() == TeamType.NONE) || (attacker.getTeam() == TeamType.NONE) || (target.getTeam() == attacker.getTeam()))
		{
			return SystemMsg.INVALID_TARGET;
		}
		
		DuelEvent duelEvent = target.getEvent(DuelEvent.class);
		if ((duelEvent == null) || (duelEvent != this))
		{
			return SystemMsg.INVALID_TARGET;
		}
		
		return null;
	}
	
	/**
	 * Method canAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return boolean
	 */
	@Override
	public boolean canAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if ((target.getTeam() == TeamType.NONE) || (attacker.getTeam() == TeamType.NONE) || (target.getTeam() == attacker.getTeam()))
		{
			return false;
		}
		
		DuelEvent duelEvent = target.getEvent(DuelEvent.class);
		return !((duelEvent == null) || (duelEvent != this));
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
			o.getPlayer().addListener(_playerExitListener);
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
			o.getPlayer().removeListener(_playerExitListener);
		}
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<DuelSnapshotObject> * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<DuelSnapshotObject> iterator()
	{
		List<DuelSnapshotObject> blue = getObjects(BLUE_TEAM);
		List<DuelSnapshotObject> red = getObjects(RED_TEAM);
		return new JoinedIterator<>(blue.iterator(), red.iterator());
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		registerActions();
	}
	
	/**
	 * Method announce.
	 * @param i int
	 */
	@Override
	public void announce(int i)
	{
		sendPacket(new SystemMessage2(SystemMsg.THE_DUEL_WILL_BEGIN_IN_S1_SECONDS).addInteger(i));
	}
}
