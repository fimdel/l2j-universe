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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.ClonePlayer;
import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClonePlayerAI extends PlayableAI
{
	/**
	 * Constructor for ClonePlayerAI.
	 * @param actor ClonePlayer
	 */
	public ClonePlayerAI(ClonePlayer actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 */
	@Override
	protected void thinkActive()
	{
		ClonePlayer actor = getActor();
		clearNextAction();
		if (actor.isFollowMode())
		{
			changeIntention(CtrlIntention.AI_INTENTION_FOLLOW, actor.getPlayer(), Config.FOLLOW_RANGE);
			thinkFollow();
		}
		super.thinkActive();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		ClonePlayer actor = getActor();
		if ((attacker != null) && actor.getPlayer().isDead())
		{
			Attack(attacker, false, false);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method getActor.
	 * @return ClonePlayer
	 */
	@Override
	public ClonePlayer getActor()
	{
		return (ClonePlayer) super.getActor();
	}
}
