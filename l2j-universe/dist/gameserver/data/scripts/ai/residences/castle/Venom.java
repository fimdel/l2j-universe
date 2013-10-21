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
package ai.residences.castle;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Venom extends Fighter
{
	/**
	 * Constructor for Venom.
	 * @param actor NpcInstance
	 */
	public Venom(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		Functions.npcShout(getActor(), NpcString.WHO_DARES_TO_COVET_THE_THRONE_OF_OUR_CASTLE__LEAVE_IMMEDIATELY_OR_YOU_WILL_PAY_THE_PRICE_OF_YOUR_AUDACITY_WITH_YOUR_VERY_OWN_BLOOD);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		Functions.npcShout(getActor(), NpcString.ITS_NOT_OVER_YET__IT_WONT_BE__OVER__LIKE_THIS__NEVER);
		NpcUtils.spawnSingle(29055, 12589, -49044, -3008, 120000);
	}
}
