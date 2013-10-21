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
package ai.isle_of_prayer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IsleOfPrayerFighter extends Fighter
{
	/**
	 * Field _penaltyMobsNotSpawned.
	 */
	private boolean _penaltyMobsNotSpawned = true;
	/**
	 * Field PENALTY_MOBS.
	 */
	private static final int[] PENALTY_MOBS =
	{
		18364,
		18365,
		18366
	};
	/**
	 * Field YELLOW_CRYSTAL. (value is 9593)
	 */
	private static final int YELLOW_CRYSTAL = 9593;
	/**
	 * Field GREEN_CRYSTAL. (value is 9594)
	 */
	private static final int GREEN_CRYSTAL = 9594;
	
	/**
	 * Constructor for IsleOfPrayerFighter.
	 * @param actor NpcInstance
	 */
	public IsleOfPrayerFighter(NpcInstance actor)
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
		if (_penaltyMobsNotSpawned && attacker.isPlayable() && (attacker.getPlayer() != null))
		{
			final Party party = attacker.getPlayer().getParty();
			if ((party != null) && (party.getMemberCount() > 2))
			{
				_penaltyMobsNotSpawned = false;
				for (int i = 0; i < 2; i++)
				{
					MonsterInstance npc = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(PENALTY_MOBS[Rnd.get(PENALTY_MOBS.length)]));
					npc.setSpawnedLoc(((MonsterInstance) actor).getMinionPosition());
					npc.setReflection(actor.getReflection());
					npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
					npc.spawnMe(npc.getSpawnedLoc());
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 100));
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
		_penaltyMobsNotSpawned = true;
		if (killer != null)
		{
			final Player player = killer.getPlayer();
			if (player != null)
			{
				final NpcInstance actor = getActor();
				switch (actor.getNpcId())
				{
					case 22259:
						if (Rnd.chance(26))
						{
							actor.dropItem(player, YELLOW_CRYSTAL, 1);
						}
						break;
					case 22263:
						if (Rnd.chance(14))
						{
							actor.dropItem(player, GREEN_CRYSTAL, 1);
						}
						break;
				}
			}
		}
		super.onEvtDead(killer);
	}
}
