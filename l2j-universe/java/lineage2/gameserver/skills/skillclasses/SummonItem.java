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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonItem extends Skill
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _minId.
	 */
	private final int _minId;
	/**
	 * Field _maxId.
	 */
	private final int _maxId;
	/**
	 * Field _minCount.
	 */
	private final long _minCount;
	/**
	 * Field _maxCount.
	 */
	private final long _maxCount;
	
	/**
	 * Constructor for SummonItem.
	 * @param set StatsSet
	 */
	public SummonItem(final StatsSet set)
	{
		super(set);
		_itemId = set.getInteger("SummonItemId", 0);
		_minId = set.getInteger("SummonMinId", 0);
		_maxId = set.getInteger("SummonMaxId", _minId);
		_minCount = set.getLong("SummonMinCount");
		_maxCount = set.getLong("SummonMaxCount", _minCount);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(final Creature activeChar, final List<Creature> targets)
	{
		if (!activeChar.isPlayable())
		{
			return;
		}
		for (Creature target : targets)
		{
			if (target != null)
			{
				int itemId = _minId > 0 ? Rnd.get(_minId, _maxId) : _itemId;
				long count = Rnd.get(_minCount, _maxCount);
				ItemFunctions.addItem((Playable) activeChar, itemId, count, true);
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
	}
}
