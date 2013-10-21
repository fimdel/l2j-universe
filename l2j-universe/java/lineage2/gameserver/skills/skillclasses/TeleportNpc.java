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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleportNpc extends Skill
{
	/**
	 * Constructor for TeleportNpc.
	 * @param set StatsSet
	 */
	public TeleportNpc(StatsSet set)
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
			if ((target != null) && !target.isDead())
			{
				getEffects(activeChar, target, getActivateRate() > 0, false);
				target.abortAttack(true, true);
				target.abortCast(true, true);
				target.stopMove();
				int x = activeChar.getX();
				int y = activeChar.getY();
				int z = activeChar.getZ();
				int h = activeChar.getHeading();
				int range = (int) (activeChar.getColRadius() + target.getColRadius());
				int hyp = (int) Math.sqrt((range * range) / 2);
				if (h < 16384)
				{
					x += hyp;
					y += hyp;
				}
				else if ((h > 16384) && (h <= 32768))
				{
					x -= hyp;
					y += hyp;
				}
				else if ((h < 32768) && (h <= 49152))
				{
					x -= hyp;
					y -= hyp;
				}
				else if (h > 49152)
				{
					x += hyp;
					y -= hyp;
				}
				target.setXYZ(x, y, z);
				target.validateLocation(1);
			}
		}
	}
}
