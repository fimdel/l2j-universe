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
package ai.residences.fortress.siege;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import ai.residences.SiegeGuardFighter;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RebelCommander extends SiegeGuardFighter
{
	/**
	 * Constructor for RebelCommander.
	 * @param actor NpcInstance
	 */
	public RebelCommander(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		Functions.npcSay(getActor(), NpcString.DONT_THINK_THAT_ITS_GONNA_END_LIKE_THIS);
	}
}
