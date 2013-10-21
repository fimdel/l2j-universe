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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeleteHateOfMe extends Skill
{
	/**
	 * Constructor for DeleteHateOfMe.
	 * @param set StatsSet
	 */
	public DeleteHateOfMe(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (activeChar.isPlayer() && ((Player) activeChar).isGM())
				{
					activeChar.sendMessage(new CustomMessage("lineage2.gameserver.skills.Formulas.Chance", (Player) activeChar).addString(getName()).addNumber(getActivateRate()));
				}
				if (target.isNpc() && Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					NpcInstance npc = (NpcInstance) target;
					npc.getAggroList().remove(activeChar, true);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				}
				getEffects(activeChar, target, true, false);
			}
		}
	}
}
