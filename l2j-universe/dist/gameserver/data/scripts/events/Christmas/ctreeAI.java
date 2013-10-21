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
package events.Christmas;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ctreeAI extends DefaultAI
{
	/**
	 * Constructor for ctreeAI.
	 * @param actor NpcInstance
	 */
	public ctreeAI(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return true;
		}
		final int skillId = 2139;
		for (Player player : World.getAroundPlayers(actor, 200, 200))
		{
			if ((player != null) && !player.isInZonePeace() && (player.getEffectList().getEffectsBySkillId(skillId) == null))
			{
				actor.doCast(SkillTable.getInstance().getInfo(skillId, 1), player, true);
			}
		}
		return false;
	}
	
	/**
	 * Method randomAnimation.
	 * @return boolean
	 */
	@Override
	protected boolean randomAnimation()
	{
		return false;
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
}
