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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeerFlouros extends Mystic
{
	/**
	 * Field _hpCount.
	 */
	private int _hpCount = 0;
	/**
	 * Field MOB. (value is 18560)
	 */
	private static final int MOB = 18560;
	/**
	 * Field MOBS_COUNT. (value is 2)
	 */
	private static final int MOBS_COUNT = 2;
	/**
	 * Field _hps.
	 */
	private static final int[] _hps =
	{
		80,
		60,
		40,
		30,
		20,
		10,
		5,
		-5
	};
	
	/**
	 * Constructor for SeerFlouros.
	 * @param actor NpcInstance
	 */
	public SeerFlouros(NpcInstance actor)
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
		if (!actor.isDead())
		{
			if (actor.getCurrentHpPercents() < _hps[_hpCount])
			{
				spawnMobs(attacker);
				_hpCount++;
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method spawnMobs.
	 * @param attacker Creature
	 */
	private void spawnMobs(Creature attacker)
	{
		final NpcInstance actor = getActor();
		for (int i = 0; i < MOBS_COUNT; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(MOB));
				sp.setLoc(Location.findPointToStay(actor, 100, 120));
				sp.setReflection(actor.getReflection());
				NpcInstance npc = sp.doSpawn(true);
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_hpCount = 0;
		super.onEvtDead(killer);
	}
}
