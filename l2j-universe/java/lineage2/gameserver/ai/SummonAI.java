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
package lineage2.gameserver.ai;

import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Summon;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonAI extends PlayableAI
{
	/**
	 * Constructor for SummonAI.
	 * @param actor Summon
	 */
	public SummonAI(Summon actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 */
	@Override
	protected void thinkActive()
	{
		Summon actor = getActor();
		clearNextAction();
		if (actor.isDepressed())
		{
			setAttackTarget(actor.getPlayer());
			changeIntention(CtrlIntention.AI_INTENTION_ATTACK, actor.getPlayer(), null);
			thinkAttack(true);
		}
		else if (actor.isFollowMode())
		{
			changeIntention(CtrlIntention.AI_INTENTION_FOLLOW, actor.getPlayer(), Config.FOLLOW_RANGE);
			thinkFollow();
		}
		else if (!actor.isFollowMode() && getIntention() == AI_INTENTION_ACTIVE)
		{
			actor.setFollowMode(true);
		}
		super.thinkActive();
	}
	
	/**
	 * Method thinkAttack.
	 * @param checkRange boolean
	 */
	@Override
	protected void thinkAttack(boolean checkRange)
	{
		Summon actor = getActor();
		if (actor.isDepressed())
		{
			setAttackTarget(actor.getPlayer());
		}
		super.thinkAttack(checkRange);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		Summon actor = getActor();
		if ((attacker != null) && actor.getPlayer().isDead() && !actor.isDepressed())
		{
			Attack(attacker, false, false);
		}
		if (actor.getPlayer().isDebug())
			actor.getPlayer().sendMessage("SummonAI onEvtAttacked isDefendMode:" + actor.isDefendMode() + " isDepressed:" + actor.isDepressed() + " " + attacker.getName());
		if ((attacker != null) && actor.isDefendMode() && !actor.isDepressed())
		{
			Attack(attacker, false, false);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method getActor.
	 * @return Summon
	 */
	@Override
	public Summon getActor()
	{
		return (Summon) super.getActor();
	}
}
