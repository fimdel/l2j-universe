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
package ai.hermunkus_message;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Teredor_19172 extends Fighter
{
	/**
	 * Field ATTACK_IDS.
	 */
	private static final int[] ATTACK_IDS =
	{
		19189,
		19191,
		19192,
		19193,
		19196,
		19197,
		19198,
		19199,
		19200,
		19201,
		19202,
		19203,
		19204,
		19205,
		19206,
		19207,
		19208,
		19209,
		19210,
		19211,
		19212,
		19213,
		19214,
		19215
	};
	
	/**
	 * Constructor for Teredor_19172.
	 * @param actor NpcInstance
	 */
	public Teredor_19172(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		final NpcInstance actor = getActor();
		if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			final AggroList.AggroInfo ai = actor.getAggroList().get(target);
			return (ai != null) && (ai.hate > 0);
		}
		return target.isPlayable() || ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
		{
			return false;
		}
		return super.checkAggression(target);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		broadCastScriptEvent("TEREDOR_DIE", 500);
	}
}
