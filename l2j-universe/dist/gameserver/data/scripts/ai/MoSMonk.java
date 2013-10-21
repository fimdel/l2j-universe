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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MoSMonk extends Fighter
{
	/**
	 * Constructor for MoSMonk.
	 * @param actor NpcInstance
	 */
	public MoSMonk(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		getActor();
		if ((getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) && Rnd.chance(20))
		{
			Functions.npcSayCustomMessage(getActor(), "scripts.ai.MoSMonk.onIntentionAttack");
		}
		super.onIntentionAttack(target);
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if (target.getActiveWeaponInstance() == null)
		{
			return false;
		}
		return super.checkAggression(target);
	}
}
