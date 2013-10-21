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
package ai;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WatchmanMonster extends Fighter
{
	/**
	 * Field _lastSearch.
	 */
	private long _lastSearch = 0;
	/**
	 * Field isSearching.
	 */
	private boolean isSearching = false;
	/**
	 * Field _attackerRef.
	 */
	private HardReference<? extends Creature> _attackerRef = HardReferences.emptyRef();
	/**
	 * Field flood.
	 */
	static final String[] flood =
	{
		"I'll be back",
		"You are stronger than expected"
	};
	/**
	 * Field flood2.
	 */
	static final String[] flood2 =
	{
		"Help me!",
		"Alarm! We are under attack!"
	};
	
	/**
	 * Constructor for WatchmanMonster.
	 * @param actor NpcInstance
	 */
	public WatchmanMonster(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(final Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && !actor.getFaction().isNone() && (actor.getCurrentHpPercents() < 50) && (_lastSearch < (System.currentTimeMillis() - 15000)))
		{
			_lastSearch = System.currentTimeMillis();
			_attackerRef = attacker.getRef();
			if (findHelp())
			{
				return;
			}
			Functions.npcSay(actor, "Anyone, help me!");
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method findHelp.
	 * @return boolean
	 */
	private boolean findHelp()
	{
		isSearching = false;
		final NpcInstance actor = getActor();
		final Creature attacker = _attackerRef.get();
		if (attacker == null)
		{
			return false;
		}
		for (final NpcInstance npc : actor.getAroundNpc(1000, 150))
		{
			if (!actor.isDead() && npc.isInFaction(actor) && !npc.isInCombat())
			{
				clearTasks();
				isSearching = true;
				addTaskMove(npc.getLoc(), true);
				Functions.npcSay(actor, flood[Rnd.get(flood.length)]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_lastSearch = 0;
		_attackerRef = HardReferences.emptyRef();
		isSearching = false;
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		final NpcInstance actor = getActor();
		if (isSearching)
		{
			final Creature attacker = _attackerRef.get();
			if (attacker != null)
			{
				Functions.npcSay(actor, flood2[Rnd.get(flood2.length)]);
				notifyFriends(attacker, 100);
			}
			isSearching = false;
			notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
		}
		else
		{
			super.onEvtArrived();
		}
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		if (!isSearching)
		{
			super.onEvtAggression(target, aggro);
		}
	}
}
