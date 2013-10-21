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
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NihilInvaderChest extends DefaultAI
{
	/**
	 * Field _firstLevelItems.
	 */
	private static final int[] _firstLevelItems =
	{
		4039,
		4040,
		4041,
		4042,
		4043,
		4044
	};
	/**
	 * Field _secondLevelItems.
	 */
	private static final int[] _secondLevelItems =
	{
		9628,
		9629,
		9630
	};
	
	/**
	 * Constructor for NihilInvaderChest.
	 * @param actor NpcInstance
	 */
	public NihilInvaderChest(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
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
		if (actor.getNpcId() == 18820)
		{
			if (Rnd.chance(40))
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 2025, 1, 0, 10));
				actor.dropItem(attacker.getPlayer(), _firstLevelItems[Rnd.get(0, _firstLevelItems.length - 1)], Rnd.get(10, 20));
				actor.doDie(null);
			}
		}
		else if (actor.getNpcId() == 18823)
		{
			if (Rnd.chance(40))
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 2025, 1, 0, 10));
				actor.dropItem(attacker.getPlayer(), _secondLevelItems[Rnd.get(0, _secondLevelItems.length - 1)], Rnd.get(10, 20));
				actor.doDie(null);
			}
		}
		for (NpcInstance npc : actor.getReflection().getNpcs())
		{
			if (npc.getNpcId() == actor.getNpcId())
			{
				npc.deleteMe();
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
