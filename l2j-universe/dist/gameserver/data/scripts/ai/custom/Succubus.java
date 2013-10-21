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
package ai.custom;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Succubus extends Fighter
{
	/**
	 * Field SAY_TEXT.
	 */
	private static final NpcString[] SAY_TEXT = new NpcString[]
	{
		NpcString.HAHAHA_YOU_DARE_TO_DISRUPT_ME_I_WILL_BE_YOUR_NIGHTMARE_FROM_WHICH_YOU_CAN_NEVER_AWAKEN,
		NpcString.YOU_DARE_ATTACK_ME_I_WILL_FILL_YOUR_NIGHTMARES_WHITH_BLOOD,
		NpcString.I_CANNOT_LET_YOU_STOP_THE_WRAITH_OF_SHILEN,
		NpcString.ANSUCUBUS,
		NpcString.HALT_YOURNIGHTMARES_WILL_FILL_YOU_WITH_DREAD,
		NpcString.YOU_WONT_GET_AWAY,
		NpcString.HOW_ALL__THAT_POWER_REMOED
	};
	
	/**
	 * Constructor for Succubus.
	 * @param actor NpcInstance
	 */
	public Succubus(NpcInstance actor)
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
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		if (Rnd.chance(25))
		{
			Functions.npcSay(actor, SAY_TEXT[Rnd.get(SAY_TEXT.length)]);
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
		final NpcInstance actor = getActor();
		SkillTable.getInstance().getInfo(14975, 1).getEffects(killer, killer, false, false);
		SkillTable.getInstance().getInfo(14976, 1).getEffects(killer, killer, false, false);
		SkillTable.getInstance().getInfo(14977, 1).getEffects(killer, killer, false, false);
		if (Rnd.chance(25))
		{
			Functions.npcSay(actor, NpcString.TO_THINK_THAT_I_COULD_FAIL_IMPOSSIBLE);
		}
		else
		{
			Functions.npcSay(actor, NpcString.SHILEN_I_HAVE_FAILED);
		}
		super.onEvtDead(killer);
		getActor().endDecayTask();
	}
}
