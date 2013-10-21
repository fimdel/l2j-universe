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
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaLock extends Fighter
{
	/**
	 * Field _attacked.
	 */
	private static boolean _attacked = false;
	/**
	 * Field _entranceactive.
	 */
	private static boolean _entranceactive = false;
	
	/**
	 * Constructor for NaiaLock.
	 * @param actor NpcInstance
	 */
	public NaiaLock(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		_entranceactive = true;
		Functions.npcShout(actor, "The lock has been removed from the Controller device");
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final NpcInstance actor = getActor();
		_entranceactive = false;
		Functions.npcShout(actor, "The lock has been put on the Controller device");
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		return false;
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
		if (!_attacked)
		{
			for (int i = 0; i < 4; i++)
			{
				try
				{
					SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(18493));
					sp.setLoc(Location.findPointToStay(actor, 150, 250));
					sp.setReflection(actor.getReflection());
					sp.doSpawn(true);
					sp.stopRespawn();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			_attacked = true;
		}
	}
	
	/**
	 * Method isEntranceActive.
	 * @return boolean
	 */
	public static boolean isEntranceActive()
	{
		return _entranceactive;
	}
}
