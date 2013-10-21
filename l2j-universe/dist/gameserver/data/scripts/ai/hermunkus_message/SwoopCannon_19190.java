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
package ai.hermunkus_message;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SwoopCannon_19190 extends DefaultAI
{
	/**
	 * Field SKILL_ID. (value is 16023)
	 */
	private static final int SKILL_ID = 16023;
	
	/**
	 * Constructor for SwoopCannon_19190.
	 * @param actor NpcInstance
	 */
	public SwoopCannon_19190(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(1, 1000 + Rnd.get(500));
	}
	
	/**
	 * Method onEvtTimer.
	 * @param timer_id int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtTimer(int timer_id, Object arg1, Object arg2)
	{
		super.onEvtTimer(timer_id, arg1, arg2);
		if (timer_id == 1)
		{
			if (!isActive())
			{
				return;
			}
			final Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			addTaskBuff(getActor(), skill);
			addTimer(1, skill.getHitTime() + 10000);
		}
	}
}
