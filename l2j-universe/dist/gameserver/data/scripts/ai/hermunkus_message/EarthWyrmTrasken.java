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

import instances.MemoryOfDisaster;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EarthWyrmTrasken extends DefaultAI
{
	/**
	 * Field RHAND_ID. (value is 15280)
	 */
	private static final int RHAND_ID = 15280;
	/**
	 * Field ENRAGED_SKILL_ID. (value is 14505)
	 */
	private static final int ENRAGED_SKILL_ID = 14505;
	/**
	 * Field BODY_STRIKE_SKILL_ID_1. (value is 14337)
	 */
	private static final int BODY_STRIKE_SKILL_ID_1 = 14337;
	/**
	 * Field BODY_STRIKE_SKILL_ID_2. (value is 14338)
	 */
	private static final int BODY_STRIKE_SKILL_ID_2 = 14338;
	
	/**
	 * Constructor for EarthWyrmTrasken.
	 * @param actor NpcInstance
	 */
	public EarthWyrmTrasken(NpcInstance actor)
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
		getActor().setRHandId(RHAND_ID);
		addTimer(1, 50);
	}
	
	/**
	 * Method onEvtTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		Skill sk;
		switch (timerId)
		{
			case 1:
				sk = SkillTable.getInstance().getInfo(ENRAGED_SKILL_ID, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 2:
				sk = SkillTable.getInstance().getInfo(BODY_STRIKE_SKILL_ID_1, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 3:
				sk = SkillTable.getInstance().getInfo(BODY_STRIKE_SKILL_ID_2, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
		}
	}
	
	/**
	 * Method onEvtFinishCasting.
	 * @param skill_id int
	 * @param success boolean
	 */
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if (skill_id == ENRAGED_SKILL_ID)
		{
			final Reflection r = getActor().getReflection();
			if (r instanceof MemoryOfDisaster)
			{
				((MemoryOfDisaster) r).startFinalScene();
			}
			addTimer(2, 50);
		}
		else if (skill_id == BODY_STRIKE_SKILL_ID_1)
		{
			addTimer(3, 50);
		}
	}
}
