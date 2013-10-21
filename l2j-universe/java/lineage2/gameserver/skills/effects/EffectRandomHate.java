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
package lineage2.gameserver.skills.effects;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectRandomHate extends Effect
{
	/**
	 * Constructor for EffectRandomHate.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectRandomHate(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		return getEffected().isMonster() && Rnd.chance(_template.chance(100));
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		MonsterInstance monster = (MonsterInstance) getEffected();
		Creature mostHated = monster.getAggroList().getMostHated();
		if (mostHated == null)
		{
			return;
		}
		AggroList.AggroInfo mostAggroInfo = monster.getAggroList().get(mostHated);
		List<Creature> hateList = monster.getAggroList().getHateList();
		hateList.remove(mostHated);
		if (!hateList.isEmpty())
		{
			AggroList.AggroInfo newAggroInfo = monster.getAggroList().get(hateList.get(Rnd.get(hateList.size())));
			final int oldHate = newAggroInfo.hate;
			newAggroInfo.hate = mostAggroInfo.hate;
			mostAggroInfo.hate = oldHate;
		}
	}
	
	/**
	 * Method isHidden.
	 * @return boolean
	 */
	@Override
	public boolean isHidden()
	{
		return true;
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
