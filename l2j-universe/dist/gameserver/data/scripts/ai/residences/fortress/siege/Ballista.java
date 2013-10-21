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
package ai.residences.fortress.siege;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Ballista extends DefaultAI
{
	/**
	 * Field BALLISTA_BOMB_SKILL_ID. (value is 2342)
	 */
	private static final int BALLISTA_BOMB_SKILL_ID = 2342;
	/**
	 * Field _bombsUseCounter.
	 */
	private int _bombsUseCounter;
	
	/**
	 * Constructor for Ballista.
	 * @param actor NpcInstance
	 */
	public Ballista(NpcInstance actor)
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
		if ((caster == null) || (skill.getId() != BALLISTA_BOMB_SKILL_ID))
		{
			return;
		}
		final Player player = caster.getPlayer();
		final FortressSiegeEvent siege = actor.getEvent(FortressSiegeEvent.class);
		final FortressSiegeEvent siege2 = player.getEvent(FortressSiegeEvent.class);
		if ((siege == null) || (!siege.equals(siege2)) || (siege.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) == null))
		{
			return;
		}
		_bombsUseCounter++;
		if (Rnd.chance(20) || (_bombsUseCounter > 4))
		{
			actor.doDie(caster);
		}
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
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_bombsUseCounter = 0;
		super.onEvtDead(killer);
	}
}
