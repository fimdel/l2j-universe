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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionTargetClan extends Condition
{
	/**
	 * Field _test.
	 */
	private final boolean _test;
	
	/**
	 * Constructor for ConditionTargetClan.
	 * @param param String
	 */
	public ConditionTargetClan(String param)
	{
		_test = Boolean.valueOf(param);
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		Creature Char = env.character;
		Creature target = env.target;
		return (Char.getPlayer() != null) && (target.getPlayer() != null) && (((Char.getPlayer().getClanId() != 0) && (Char.getPlayer().getClanId() == target.getPlayer().getClanId() == _test)) || ((Char.getPlayer().getParty() != null) && (Char.getPlayer().getParty() == target.getPlayer().getParty())));
	}
}
