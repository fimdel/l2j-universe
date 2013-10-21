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
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Pronghorn extends Fighter
{
	/**
	 * Field _mobsNotSpawned.
	 */
	private boolean _mobsNotSpawned = true;
	/**
	 * Field MOBS. (value is 22087)
	 */
	private static final int MOBS = 22087;
	/**
	 * Field MOBS_COUNT. (value is 4)
	 */
	private static final int MOBS_COUNT = 4;
	
	/**
	 * Constructor for Pronghorn.
	 * @param actor NpcInstance
	 */
	public Pronghorn(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		if (skill.isMagic())
		{
			return;
		}
		if (_mobsNotSpawned)
		{
			_mobsNotSpawned = false;
			for (int i = 0; i < MOBS_COUNT; i++)
			{
				try
				{
					SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(MOBS));
					sp.setLoc(Location.findPointToStay(actor, 100, 120));
					NpcInstance npc = sp.doSpawn(true);
					if (caster.isPet() || caster.isServitor())
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster, Rnd.get(2, 100));
					}
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster.getPlayer(), Rnd.get(1, 100));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
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
		_mobsNotSpawned = true;
		super.onEvtDead(killer);
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return _mobsNotSpawned;
	}
}
