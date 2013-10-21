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

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Bronk_19192 extends Dwarvs
{
	/**
	 * Field TOROCCO_ID. (value is 19198)
	 */
	private static final int TOROCCO_ID = 19198;
	
	/**
	 * Constructor for Bronk_19192.
	 * @param actor NpcInstance
	 */
	public Bronk_19192(NpcInstance actor)
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
		if (event.equalsIgnoreCase("BRONK_1"))
		{
			addTimer(1, 1600);
		}
		else if (event.equalsIgnoreCase("BRONK_2"))
		{
			addTimer(2, 1600);
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
		super.onEvtTimer(timerId, arg1, arg2);
		final Reflection r = getActor().getReflection();
		if (!(r instanceof MemoryOfDisaster))
		{
			return;
		}
		final MemoryOfDisaster ad = (MemoryOfDisaster) r;
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.MM_IM_SEE);
				final List<NpcInstance> list = r.getAllByNpcId(TOROCCO_ID, true);
				if (list.size() > 0)
				{
					final NpcInstance torocco = list.get(0);
					torocco.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "TOROCCO_1");
				}
				break;
			case 2:
				Functions.npcSayInRange(getActor(), 1500, NpcString.THANK_YOU_FOR_THE_REPORT_ROGIN);
				addTimer(3, 1600);
				break;
			case 3:
				Functions.npcSayInRange(getActor(), 1500, NpcString.SOLDIERS_WERE_FIGHTING_A_BATTLE_THAT_CANT_BE_WON);
				addTimer(4, 1600);
				break;
			case 4:
				Functions.npcSayInRange(getActor(), 1500, NpcString.BUT_WE_HAVE_TO_DEFEND_OUR_VILLAGE_SO_WERE_FIGHTING);
				addTimer(5, 1600);
				break;
			case 5:
				Functions.npcSayInRange(getActor(), 1500, NpcString.FOR_THE_FINE_WINES_AND_TREASURES_OF_ADEN);
				addTimer(6, 1600);
				break;
			case 6:
				Functions.npcSayInRange(getActor(), 1500, NpcString.IM_PROUD_OF_EVERY_ONE_OF);
				addTimer(7, 1600);
				addTimer(7, 2400);
				addTimer(7, 3600);
				addTimer(8, 2000);
				break;
			case 7:
				broadCastScriptEvent("SHOUT_ALL_1", 500);
				break;
			case 8:
				ad.spawnTentacles();
				addTimer(9, 8000);
				break;
			case 9:
				Functions.npcSayInRange(getActor(), 1500, NpcString.UGH_IFI_SEE_YOU_IN_THE_SPIRIT_WORLD_FIRST_ROUND_IS_ON_ME);
				addTimer(11, 1000);
				addTimer(10, 1600);
				addTimer(10, 2400);
				addTimer(10, 3600);
				break;
			case 10:
				broadCastScriptEvent("SHOUT_ALL_2", 500);
				break;
			case 11:
				getActor().doDie(getActor());
				break;
		}
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		return false;
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		return false;
	}
}
