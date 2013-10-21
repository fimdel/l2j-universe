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
package ai.hellbound;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SteelCitadelKeymaster extends Fighter
{
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	/**
	 * Field AMASKARI_ID. (value is 22449)
	 */
	private static final int AMASKARI_ID = 22449;
	
	/**
	 * Constructor for SteelCitadelKeymaster.
	 * @param actor NpcInstance
	 */
	public SteelCitadelKeymaster(NpcInstance actor)
	{
		super(actor);
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
		if (actor.isDead())
		{
			return;
		}
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			Functions.npcSay(actor, "You have done well in finding me, but I cannot just hand you the key!");
			for (NpcInstance npc : World.getAroundNpc(actor))
			{
				if ((npc.getNpcId() == AMASKARI_ID) && (npc.getReflectionId() == actor.getReflectionId()) && !npc.isDead())
				{
					npc.teleToLocation(Location.findPointToStay(actor, 150, 200));
					break;
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
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
