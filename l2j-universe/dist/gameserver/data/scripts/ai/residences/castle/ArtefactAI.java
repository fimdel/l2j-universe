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
package ai.residences.castle;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ArtefactAI extends CharacterAI
{
	/**
	 * Constructor for ArtefactAI.
	 * @param actor NpcInstance
	 */
	public ArtefactAI(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		final NpcInstance actor = (NpcInstance) getActor();
		final Player player = attacker.getPlayer();
		if ((attacker == null) || (player == null) || (actor == null))
		{
			return;
		}
		final SiegeEvent<?, ?> siegeEvent1 = actor.getEvent(SiegeEvent.class);
		final SiegeEvent<?, ?> siegeEvent2 = player.getEvent(SiegeEvent.class);
		final SiegeClanObject siegeClan = siegeEvent1.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if ((siegeEvent2 == null) || ((siegeEvent1.equals(siegeEvent2)) && (siegeClan != null)))
		{
			ThreadPoolManager.getInstance().schedule(new notifyGuard(player), 1000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	class notifyGuard extends RunnableImpl
	{
		/**
		 * Field _playerRef.
		 */
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for notifyGuard.
		 * @param attacker Player
		 */
		notifyGuard(Player attacker)
		{
			_playerRef = attacker.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = (NpcInstance) getActor();
			final Player attacker = _playerRef.get();
			if ((attacker == null) || (actor == null))
			{
				return;
			}
			for (NpcInstance npc : actor.getAroundNpc(1500, 200))
			{
				if (npc.isSiegeGuard() && Rnd.chance(20))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
				}
			}
			if ((attacker.getCastingSkill() != null) && (attacker.getCastingSkill().getTargetType() == Skill.SkillTargetType.TARGET_HOLY))
			{
				ThreadPoolManager.getInstance().schedule(this, 10000);
			}
		}
	}
}
