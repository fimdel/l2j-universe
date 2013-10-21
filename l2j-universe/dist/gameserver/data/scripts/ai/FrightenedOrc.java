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
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FrightenedOrc extends Fighter
{
	/**
	 * Field _sayOnAttack.
	 */
	private boolean _sayOnAttack;
	
	/**
	 * Constructor for FrightenedOrc.
	 * @param actor NpcInstance
	 */
	public FrightenedOrc(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		_sayOnAttack = true;
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && Rnd.chance(10) && _sayOnAttack)
		{
			Functions.npcSay(actor, "Don't kill me! If you show mercy I will pay you 10000 adena!");
			_sayOnAttack = false;
		}
		super.onEvtAttacked(attacker, damage);
	}
}
