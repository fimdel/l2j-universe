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
import lineage2.gameserver.ai.Ranger;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeluLizardmanSpecialAgent extends Ranger
{
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	
	/**
	 * Constructor for DeluLizardmanSpecialAgent.
	 * @param actor NpcInstance
	 */
	public DeluLizardmanSpecialAgent(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		_firstTimeAttacked = true;
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
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			if (Rnd.chance(25))
			{
				Functions.npcSay(actor, "How dare you interrupt our fight! Hey guys, help!");
			}
		}
		else if (Rnd.chance(10))
		{
			Functions.npcSay(actor, "Hey! Were having a duel here!");
		}
		super.onEvtAttacked(attacker, damage);
	}
}
