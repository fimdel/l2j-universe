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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionPlayerCubic extends Condition
{
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for ConditionPlayerCubic.
	 * @param id int
	 */
	public ConditionPlayerCubic(int id)
	{
		_id = id;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		if ((env.target == null) || !env.target.isPlayer())
		{
			return false;
		}
		Player targetPlayer = (Player) env.target;
		if (targetPlayer.getCubic(_id) != null)
		{
			return true;
		}
		int size = (int) targetPlayer.calcStat(Stats.CUBICS_LIMIT, 1);
		if (targetPlayer.getCubics().size() >= size)
		{
			if (env.character == targetPlayer)
			{
				targetPlayer.sendPacket(Msg.CUBIC_SUMMONING_FAILED);
			}
			return false;
		}
		return true;
	}
}
