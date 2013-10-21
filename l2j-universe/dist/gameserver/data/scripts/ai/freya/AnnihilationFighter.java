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
package ai.freya;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AnnihilationFighter extends Fighter
{
	/**
	 * Constructor for AnnihilationFighter.
	 * @param actor NpcInstance
	 */
	public AnnihilationFighter(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (Rnd.chance(5))
		{
			NpcUtils.spawnSingle(18839, Location.findPointToStay(getActor(), 40, 120), getActor().getReflection());
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method canSeeInSilentMove.
	 * @param target Playable
	 * @return boolean
	 */
	@Override
	public boolean canSeeInSilentMove(Playable target)
	{
		return true;
	}
	
	/**
	 * Method canSeeInHide.
	 * @param target Playable
	 * @return boolean
	 */
	@Override
	public boolean canSeeInHide(Playable target)
	{
		return true;
	}
}
