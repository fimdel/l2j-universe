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
import lineage2.gameserver.model.entity.residence.ResidenceType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionPlayerResidence extends Condition
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _type.
	 */
	private final ResidenceType _type;
	
	/**
	 * Constructor for ConditionPlayerResidence.
	 * @param id int
	 * @param type ResidenceType
	 */
	public ConditionPlayerResidence(int id, ResidenceType type)
	{
		_id = id;
		_type = type;
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
		Player player = (Player) env.character;
		Clan clan = player.getClan();
		if (clan == null)
		{
			return false;
		}
		int residenceId = clan.getResidenceId(_type);
		return _id > 0 ? residenceId == _id : residenceId > 0;
	}
}
