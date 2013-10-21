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
package ai.custom;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GvGBoss extends Fighter
{
	/**
	 * Field phrase1.
	 */
	boolean phrase1 = false;
	/**
	 * Field phrase2.
	 */
	boolean phrase2 = false;
	/**
	 * Field phrase3.
	 */
	boolean phrase3 = false;
	
	/**
	 * Constructor for GvGBoss.
	 * @param actor NpcInstance
	 */
	public GvGBoss(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
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
		if ((actor.getCurrentHpPercents() < 50) && (!phrase1))
		{
			phrase1 = true;
			Functions.npcSay(actor, "Вам не уда�?т�?�? похитит�? �?окровища Герал�?да!");
		}
		else if ((actor.getCurrentHpPercents() < 30) && (!phrase2))
		{
			phrase2 = true;
			Functions.npcSay(actor, "Я тебе череп проломл�?!");
		}
		else if ((actor.getCurrentHpPercents() < 5) && (!phrase3))
		{
			phrase3 = true;
			Functions.npcSay(actor, "Вы в�?е погибнете в �?тра�?ных муках! Уничтожу!");
		}
		super.onEvtAttacked(attacker, damage);
	}
}
