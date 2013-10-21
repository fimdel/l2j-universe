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
public class ConditionPlayerState extends Condition
{
	/**
	 * @author Mobius
	 */
	public enum CheckPlayerState
	{
		/**
		 * Field RESTING.
		 */
		RESTING,
		/**
		 * Field MOVING.
		 */
		MOVING,
		/**
		 * Field RUNNING.
		 */
		RUNNING,
		/**
		 * Field STANDING.
		 */
		STANDING,
		/**
		 * Field FLYING.
		 */
		FLYING,
		/**
		 * Field FLYING_TRANSFORM.
		 */
		FLYING_TRANSFORM
	}
	
	/**
	 * Field _check.
	 */
	private final CheckPlayerState _check;
	/**
	 * Field _required.
	 */
	private final boolean _required;
	
	/**
	 * Constructor for ConditionPlayerState.
	 * @param check CheckPlayerState
	 * @param required boolean
	 */
	public ConditionPlayerState(CheckPlayerState check, boolean required)
	{
		_check = check;
		_required = required;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		switch (_check)
		{
			case RESTING:
				if (env.character.isPlayer())
				{
					return ((Player) env.character).isSitting() == _required;
				}
				return !_required;
			case MOVING:
				return env.character.isMoving == _required;
			case RUNNING:
				return (env.character.isMoving && env.character.isRunning()) == _required;
			case STANDING:
				if (env.character.isPlayer())
				{
					return (((Player) env.character).isSitting() != _required) && (env.character.isMoving != _required);
				}
				return env.character.isMoving != _required;
			case FLYING:
				if (env.character.isPlayer())
				{
					return env.character.isFlying() == _required;
				}
				return !_required;
			case FLYING_TRANSFORM:
				if (env.character.isPlayer())
				{
					return ((Player) env.character).isInFlyingTransform() == _required;
				}
				return !_required;
		}
		return !_required;
	}
}
