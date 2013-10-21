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
package ai.seedofdestruction;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TiatsTrap extends DefaultAI
{
	/**
	 * Field holdTraps.
	 */
	private static final int[] holdTraps =
	{
		18720,
		18721,
		18722,
		18723,
		18724,
		18725,
		18726,
		18727,
		18728
	};
	/**
	 * Field damageTraps.
	 */
	private static final int[] damageTraps =
	{
		18737,
		18738,
		18739,
		18740,
		18741,
		18742,
		18743,
		18744,
		18745,
		18746,
		18747,
		18748,
		18749,
		18750,
		18751,
		18752,
		18753,
		18754,
		18755,
		18756,
		18757,
		18758,
		18759,
		18760,
		18761,
		18762,
		18763,
		18764,
		18765,
		18766,
		18767,
		18768,
		18769,
		18770,
		18771,
		18772,
		18773,
		18774
	};
	/**
	 * Field stunTraps.
	 */
	private static final int[] stunTraps =
	{
		18729,
		18730,
		18731,
		18732,
		18733,
		18734,
		18735,
		18736
	};
	
	/**
	 * Constructor for TiatsTrap.
	 * @param actor NpcInstance
	 */
	public TiatsTrap(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
		actor.startDamageBlocked();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (!actor.getAroundCharacters(200, 150).isEmpty())
		{
			Skill skill = null;
			if (ArrayUtils.contains(holdTraps, actor.getNpcId()))
			{
				skill = SkillTable.getInstance().getInfo(4186, 9);
			}
			else if (ArrayUtils.contains(damageTraps, actor.getNpcId()))
			{
				skill = SkillTable.getInstance().getInfo(5311, 9);
			}
			else if (ArrayUtils.contains(stunTraps, actor.getNpcId()))
			{
				skill = SkillTable.getInstance().getInfo(4072, 10);
			}
			else
			{
				return false;
			}
			actor.doCast(skill, actor, true);
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					getActor().doDie(null);
				}
			}, 5000);
			return true;
		}
		return true;
	}
}
