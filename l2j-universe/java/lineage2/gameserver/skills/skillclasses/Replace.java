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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Replace extends Skill
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Replace.class);
	
	/**
	 * Constructor for Replace.
	 * @param set StatsSet
	 */
	public Replace(StatsSet set)
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
		if (!(activeChar instanceof Player))
		{
			return;
		}
		Player activePlayer = activeChar.getPlayer();
		_log.info("" + targets.size());
		for (Creature target : targets)
		{
			if ((target != null) && !target.isDead())
			{
				Location loc_pet = target.getLoc();
				Location loc_cha = activePlayer.getLoc();
				activePlayer.teleToLocation(loc_pet);
				target.teleToLocation(loc_cha);
			}
		}
	}
}
