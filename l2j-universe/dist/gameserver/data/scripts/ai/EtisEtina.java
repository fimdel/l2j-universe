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

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EtisEtina extends Fighter
{
	/**
	 * Field summonsReleased.
	 */
	private boolean summonsReleased = false;
	/**
	 * Field summon1.
	 */
	private NpcInstance summon1;
	/**
	 * Field summon2.
	 */
	private NpcInstance summon2;
	
	/**
	 * Constructor for EtisEtina.
	 * @param actor NpcInstance
	 */
	public EtisEtina(NpcInstance actor)
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
		if ((actor.getCurrentHpPercents() < 70) && !summonsReleased)
		{
			summonsReleased = true;
			summon1 = NpcUtils.spawnSingle(18950, Location.findAroundPosition(actor, 150), actor.getReflection());
			summon2 = NpcUtils.spawnSingle(18951, Location.findAroundPosition(actor, 150), actor.getReflection());
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
		if ((summon1 != null) && !summon1.isDead())
		{
			summon1.decayMe();
		}
		if ((summon2 != null) && !summon2.isDead())
		{
			summon2.decayMe();
		}
		super.onEvtDead(killer);
	}
}
