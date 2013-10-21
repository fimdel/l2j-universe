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

import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.events.objects.DuelSnapshotObject;
import lineage2.gameserver.network.serverpackets.ExDuelAskStart;
import lineage2.gameserver.network.serverpackets.ExDuelEnd;
import lineage2.gameserver.network.serverpackets.ExDuelReady;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerVsPlayerDuelEvent extends DuelEvent
{
	/**
	 * Constructor for PlayerVsPlayerDuelEvent.
	 * @param set MultiValueSet<String>
	 */
	public PlayerVsPlayerDuelEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Constructor for PlayerVsPlayerDuelEvent.
	 * @param id int
	 * @param name String
	 */
	protected PlayerVsPlayerDuelEvent(int id, String name)
	{
		super(id, name);
	}
	
	/**
	 * Method canDuel.
	 * @param player Player
	 * @param target Player
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean canDuel(Player player, Player target, boolean first)
	{
		IStaticPacket sm = canDuel0(player, target);
		if (sm != null)
		{
			player.sendPacket(sm);
			return false;
		}
		sm = canDuel0(target, player);
		if (sm != null)
		{
			player.sendPacket(SystemMsg.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
			return false;
		}
		return true;
	}
	
	/**
	 * Method askDuel.
	 * @param player Player
	 * @param target Player
	 */
	@Override
	public void askDuel(Player player, Player target)
	{
		Request request = new Request(Request.L2RequestType.DUEL, player, target).setTimeout(10000L);
		request.set("duelType", 0);
		player.setRequest(request);
		target.setRequest(request);
		player.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_BEEN_CHALLENGED_TO_A_DUEL).addName(target));
		target.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_CHALLENGED_YOU_TO_A_DUEL).addName(player), new ExDuelAskStart(player.getName(), 0));
	}
	
	/**
	 * Method createDuel.
	 * @param player Player
	 * @param target Player
	 */
	@Override
	public void createDuel(Player player, Player target)
	{
		PlayerVsPlayerDuelEvent duelEvent = new PlayerVsPlayerDuelEvent(getDuelType(), player.getObjectId() + "_" + target.getObjectId() + "_duel");
		cloneTo(duelEvent);
		duelEvent.addObject(BLUE_TEAM, new DuelSnapshotObject(player, TeamType.BLUE));
		duelEvent.addObject(RED_TEAM, new DuelSnapshotObject(target, TeamType.RED));
		duelEvent.sendPacket(new ExDuelReady(this));
		duelEvent.reCalcNextTime(false);
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public void stopEvent()
	{
		clearActions();
		updatePlayers(false, false);
		for (DuelSnapshotObject d : this)
		{
			d.getPlayer().sendPacket(new ExDuelEnd(this));
			GameObject target = d.getPlayer().getTarget();
			if (target != null)
			{
				d.getPlayer().getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, target);
			}
		}
		switch (_winner)
		{
			case NONE:
				sendPacket(SystemMsg.THE_DUEL_HAS_ENDED_IN_A_TIE);
				break;
			case RED:
			case BLUE:
				List<DuelSnapshotObject> winners = getObjects(_winner.name());
				List<DuelSnapshotObject> lossers = getObjects(_winner.revert().name());
				sendPacket(new SystemMessage2(SystemMsg.C1_HAS_WON_THE_DUEL).addName(winners.get(0).getPlayer()));
				for (DuelSnapshotObject d : lossers)
				{
					d.getPlayer().broadcastPacket(new SocialAction(d.getPlayer().getObjectId(), SocialAction.BOW));
				}
				break;
		}
		removeObjects(RED_TEAM);
		removeObjects(BLUE_TEAM);
	}
	
	/**
	 * Method onDie.
	 * @param player Player
	 */
	@Override
	public void onDie(Player player)
	{
		TeamType team = player.getTeam();
		if ((team == TeamType.NONE) || _aborted)
		{
			return;
		}
		boolean allDead = true;
		List<DuelSnapshotObject> objs = getObjects(team.name());
		for (DuelSnapshotObject obj : objs)
		{
			if (obj.getPlayer() == player)
			{
				obj.setDead();
			}
			if (!obj.isDead())
			{
				allDead = false;
			}
		}
		if (allDead)
		{
			_winner = team.revert();
			stopEvent();
		}
	}
	
	/**
	 * Method getDuelType.
	 * @return int
	 */
	@Override
	public int getDuelType()
	{
		return 0;
	}
	
	/**
	 * Method playerExit.
	 * @param player Player
	 */
	@Override
	public void playerExit(Player player)
	{
		if ((_winner != TeamType.NONE) || _aborted)
		{
			return;
		}
		_winner = player.getTeam().revert();
		_aborted = false;
		stopEvent();
	}
	
	/**
	 * Method packetSurrender.
	 * @param player Player
	 */
	@Override
	public void packetSurrender(Player player)
	{
		playerExit(player);
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return System.currentTimeMillis() + 5000L;
	}
}
