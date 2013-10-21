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
package ai.residences.clanhall;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.entity.events.objects.ZoneObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.NpcUtils;
import npc.model.residences.clanhall.RainbowGourdInstance;
import npc.model.residences.clanhall.RainbowYetiInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowYeti extends CharacterAI
{
	/**
	 * @author Mobius
	 */
	private static class ZoneDeactive extends RunnableImpl
	{
		/**
		 * Field _zone.
		 */
		private final ZoneObject _zone;
		
		/**
		 * Constructor for ZoneDeactive.
		 * @param zone ZoneObject
		 */
		ZoneDeactive(ZoneObject zone)
		{
			_zone = zone;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_zone.setActive(false);
		}
	}
	
	/**
	 * Constructor for RainbowYeti.
	 * @param actor NpcInstance
	 */
	public RainbowYeti(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param character Creature
	 */
	@Override
	public void onEvtSeeSpell(Skill skill, Creature character)
	{
		final RainbowYetiInstance actor = (RainbowYetiInstance) getActor();
		final ClanHallMiniGameEvent miniGameEvent = actor.getEvent(ClanHallMiniGameEvent.class);
		if (miniGameEvent == null)
		{
			return;
		}
		if (!character.isPlayer())
		{
			return;
		}
		
		final Player player = character.getPlayer();
		
		CMGSiegeClanObject siegeClan = null;
		final List<CMGSiegeClanObject> attackers = miniGameEvent.getObjects(SiegeEvent.ATTACKERS);
		for (CMGSiegeClanObject $ : attackers)
		{
			if ($.isParticle(player))
			{
				siegeClan = $;
			}
		}
		
		if (siegeClan == null)
		{
			return;
		}
		
		final int index = attackers.indexOf(siegeClan);
		int warIndex = Integer.MIN_VALUE;
		
		RainbowGourdInstance gourdInstance = null;
		RainbowGourdInstance gourdInstance2 = null;
		switch (skill.getId())
		{
			case 2240: // nectar
				if (Rnd.chance(90))
				{
					gourdInstance = getGourd(index);
					if (gourdInstance == null)
					{
						return;
					}
					
					gourdInstance.doDecrease(player);
				}
				else
				{
					actor.addMob(NpcUtils.spawnSingle(35592, actor.getX() + 10, actor.getY() + 10, actor.getZ(), 0));
				}
				break;
			case 2241: // mineral water
				warIndex = rndEx(attackers.size(), index);
				if (warIndex == Integer.MIN_VALUE)
				{
					return;
				}
				
				gourdInstance2 = getGourd(warIndex);
				if (gourdInstance2 == null)
				{
					return;
				}
				gourdInstance2.doHeal();
				break;
			case 2242: // water
				warIndex = rndEx(attackers.size(), index);
				if (warIndex == Integer.MIN_VALUE)
				{
					return;
				}
				
				gourdInstance = getGourd(index);
				gourdInstance2 = getGourd(warIndex);
				if ((gourdInstance2 == null) || (gourdInstance == null))
				{
					return;
				}
				
				gourdInstance.doSwitch(gourdInstance2);
				break;
			case 2243: // sulfur
				warIndex = rndEx(attackers.size(), index);
				if (warIndex == Integer.MIN_VALUE)
				{
					return;
				}
				
				final ZoneObject zone = miniGameEvent.getFirstObject("zone_" + warIndex);
				if (zone == null)
				{
					return;
				}
				zone.setActive(true);
				ThreadPoolManager.getInstance().schedule(new ZoneDeactive(zone), 60000L);
				break;
		}
	}
	
	/**
	 * Method getGourd.
	 * @param index int
	 * @return RainbowGourdInstance
	 */
	private RainbowGourdInstance getGourd(int index)
	{
		final ClanHallMiniGameEvent miniGameEvent = getActor().getEvent(ClanHallMiniGameEvent.class);
		
		final SpawnExObject spawnEx = miniGameEvent.getFirstObject("arena_" + index);
		
		return (RainbowGourdInstance) spawnEx.getSpawns().get(1).getFirstSpawned();
	}
	
	/**
	 * Method rndEx.
	 * @param size int
	 * @param ex int
	 * @return int
	 */
	private int rndEx(int size, int ex)
	{
		int rnd = Integer.MIN_VALUE;
		for (int i = 0; i < Byte.MAX_VALUE; i++)
		{
			rnd = Rnd.get(size);
			if (rnd != ex)
			{
				break;
			}
		}
		
		return rnd;
	}
}
