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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DarkElves extends DefaultAI
{
	/**
	 * Field TEXT.
	 */
	private static final NpcString[] TEXT =
	{
		NpcString.GAH_SHILEN_WHY_MUST_YOU_MAKE_US_SUFFER,
		NpcString.SHILEN_ABANDONED_US_IT_IS_OUR_TIME_TO_DIE,
		NpcString.WITH_OUR_SACRIFICE_WILL_WE_FULLFILL_THE_PROPHECY,
		NpcString.BLOODY_RAIN_PLAGUE_DEATH_SHE_IS_NEAR,
		NpcString.ARHHHH,
		NpcString.WE_OFFER_OUR_BLOOD_AS_A_SACRIFICE_SHILEN_SEE_US,
		NpcString.WILL_DARK_ELVES_BE_FORGOTTEN_AFTER_WHAT_WE_HAVE_DONE,
		NpcString.UNBELIEVERS_RUN_DEATH_WILL_FOLLOW_YOU,
		NpcString.I_CURSE_OUR_BLOOD_I_DESPISE_WHAT_WE_ARE_SHILEN
	};
	
	/**
	 * Constructor for DarkElves.
	 * @param actor NpcInstance
	 */
	public DarkElves(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if (event.equalsIgnoreCase("START_DIE"))
		{
			addTimer(1, 2500);
		}
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
		if (timerId == 1)
		{
			if (Rnd.chance(50))
			{
				Functions.npcSayInRange(getActor(), 1000, TEXT[Rnd.get(TEXT.length)]);
			}
			final Reflection r = getActor().getReflection();
			if (r instanceof MemoryOfDisaster)
			{
				((MemoryOfDisaster) r).dieNextElf();
			}
			getActor().doDie(getActor());
		}
	}
}
