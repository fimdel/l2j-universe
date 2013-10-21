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
package npc.model.residences.fortress.siege;

import java.util.List;

import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import ai.residences.fortress.siege.MercenaryCaption;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MercenaryCaptionInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private class DoorDeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DoorDeathListener.
		 */
		public DoorDeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param door Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature door, Creature killer)
		{
			if (isDead())
			{
				return;
			}
			FortressSiegeEvent event = door.getEvent(FortressSiegeEvent.class);
			if (event == null)
			{
				return;
			}
			Functions.npcShout(MercenaryCaptionInstance.this, NpcString.WE_HAVE_BROKEN_THROUGH_THE_GATE_DESTROY_THE_ENCAMPMENT_AND_MOVE_TO_THE_COMMAND_POST);
			List<DoorObject> objects = event.getObjects(FortressSiegeEvent.ENTER_DOORS);
			for (DoorObject d : objects)
			{
				d.open(event);
			}
			((MercenaryCaption) getAI()).startMove(true);
		}
	}
	
	/**
	 * Field _doorDeathListener.
	 */
	private final DoorDeathListener _doorDeathListener = new DoorDeathListener();
	
	/**
	 * Constructor for MercenaryCaptionInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MercenaryCaptionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Fortress f = getFortress();
		FortressSiegeEvent event = f.getSiegeEvent();
		List<DoorObject> objects = event.getObjects(FortressSiegeEvent.ENTER_DOORS);
		for (DoorObject d : objects)
		{
			d.getDoor().addListener(_doorDeathListener);
		}
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isAutoAttackable(attacker);
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		FortressSiegeEvent event = getEvent(FortressSiegeEvent.class);
		if (event == null)
		{
			return false;
		}
		SiegeClanObject object = event.getSiegeClan(SiegeEvent.DEFENDERS, attacker.getClan());
		if (object == null)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		super.onDeath(killer);
		Functions.npcShout(this, NpcString.THE_GODS_HAVE_FORSAKEN_US__RETREAT);
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	public void onDecay()
	{
		super.onDecay();
		Fortress f = getFortress();
		FortressSiegeEvent event = f.getSiegeEvent();
		List<DoorObject> objects = event.getObjects(FortressSiegeEvent.ENTER_DOORS);
		for (DoorObject d : objects)
		{
			d.getDoor().removeListener(_doorDeathListener);
		}
	}
}
