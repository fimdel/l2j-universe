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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionPlayerClassId extends Condition
{
	/**
	 * Field _classIds.
	 */
	private final int[] _classIds;
	
	/**
	 * Constructor for ConditionPlayerClassId.
	 * @param ids String[]
	 */
	public ConditionPlayerClassId(String[] ids)
	{
		_classIds = new int[ids.length];
		for (int i = 0; i < ids.length; i++)
		{
			_classIds[i] = Integer.parseInt(ids[i]);
		}
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		if (!env.character.isPlayer())
		{
			return false;
		}
		int playerClassId = ((Player) env.character).getActiveClassId();
		for (int id : _classIds)
		{
			if (playerClassId == id)
			{
				return true;
			}
		}
		return false;
	}
}
