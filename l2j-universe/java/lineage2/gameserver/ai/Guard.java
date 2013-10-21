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

import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Guard extends Fighter
{
	/**
	 * Constructor for Guard.
	 * @param actor NpcInstance
	 */
	public Guard(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean canAttackCharacter(Creature target)
	{
		NpcInstance actor = getActor();
		if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			AggroInfo ai = actor.getAggroList().get(target);
			return (ai != null) && (ai.hate > 0);
		}
		return target.isMonster() || target.isPlayable();
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isPlayable())
		{
			if ((target.getKarma() >= 0) || (actor.getParameter("evilGuard", false) && (target.getPvpFlag() > 0)))
			{
				return false;
			}
		}
		if (target.isNpc())
		{
			if (((NpcInstance) target).isInFaction(actor))
			{
				return false;
			}
			if (target.isMonster())
			{
				if (!((MonsterInstance) target).isAggressive())
				{
					return false;
				}
			}
		}
		return super.checkAggression(target);
	}
	
	/**
	 * Method getMaxAttackTimeout.
	 * @return int
	 */
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
