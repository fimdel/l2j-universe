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
package ai.seedofinfinity;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AliveTumor extends DefaultAI
{
	/**
	 * Field checkTimer.
	 */
	private long checkTimer = 0;
	/**
	 * Field coffinsCount.
	 */
	private int coffinsCount = 0;
	/**
	 * Field regenCoffins.
	 */
	private static final int[] regenCoffins =
	{
		18706,
		18709,
		18710
	};
	
	/**
	 * Constructor for AliveTumor.
	 * @param actor NpcInstance
	 */
	public AliveTumor(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((checkTimer + 10000) < System.currentTimeMillis())
		{
			checkTimer = System.currentTimeMillis();
			int i = 0;
			for (NpcInstance n : actor.getAroundNpc(400, 300))
			{
				if (ArrayUtils.contains(regenCoffins, n.getNpcId()) && !n.isDead())
				{
					i++;
				}
			}
			if (coffinsCount != i)
			{
				coffinsCount = i;
				coffinsCount = Math.min(coffinsCount, 12);
				if (coffinsCount > 0)
				{
					actor.altOnMagicUseTimer(actor, SkillTable.getInstance().getInfo(5940, coffinsCount));
				}
			}
		}
		return super.thinkActive();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
