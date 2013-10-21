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
package lineage2.gameserver.model.actor.listener;

import lineage2.commons.listener.Listener;
import lineage2.gameserver.listener.actor.npc.OnDecayListener;
import lineage2.gameserver.listener.actor.npc.OnSpawnListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcListenerList extends CharListenerList
{
	/**
	 * Constructor for NpcListenerList.
	 * @param actor NpcInstance
	 */
	public NpcListenerList(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method getActor.
	 * @return NpcInstance
	 */
	@Override
	public NpcInstance getActor()
	{
		return (NpcInstance) actor;
	}
	
	/**
	 * Method onSpawn.
	 */
	public void onSpawn()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnSpawnListener.class.isInstance(listener))
				{
					((OnSpawnListener) listener).onSpawn(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnSpawnListener.class.isInstance(listener))
				{
					((OnSpawnListener) listener).onSpawn(getActor());
				}
			}
		}
	}
	
	/**
	 * Method onDecay.
	 */
	public void onDecay()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnDecayListener.class.isInstance(listener))
				{
					((OnDecayListener) listener).onDecay(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnDecayListener.class.isInstance(listener))
				{
					((OnDecayListener) listener).onDecay(getActor());
				}
			}
		}
	}
}
