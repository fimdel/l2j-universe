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
package ai.adept;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Adept extends DefaultAI
{
	/**
	 * Field _points.
	 */
	protected Location[] _points;
	/**
	 * Field _lastPoint.
	 */
	private int _lastPoint = 0;
	
	/**
	 * Constructor for Adept.
	 * @param actor NpcInstance
	 */
	public Adept(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			startMoveTask();
		}
		return true;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		if (Rnd.chance(30))
		{
			sayRndMsg();
		}
		super.onEvtArrived();
	}
	
	/**
	 * Method startMoveTask.
	 */
	private void startMoveTask()
	{
		_lastPoint++;
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
		}
		addTaskMove(_points[_lastPoint], false);
		doTask();
	}
	
	/**
	 * Method sayRndMsg.
	 */
	private void sayRndMsg()
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		NpcString ns;
		switch (Rnd.get(7))
		{
			case 1:
				ns = NpcString.CLOUDS_OF_BLOOD_ARE_GATHERING_SOON_IT_WILL_START_TO_RAIN_THE_RAIN_OF_CRIMSON_BLOOD;
				break;
			case 2:
				ns = NpcString.WHILE_THE_FOOLISH_LIGHT_WAS_ASLEEP_THE_DARKNESS_WILL_AWAKEN_FIRST_UH;
				break;
			case 3:
				ns = NpcString.IT_IS_THE_DEEPEST_DARKNESS_WITH_ITS_ARRIVAL_THE_WORLD_WILL_SOON_DIE;
				break;
			case 4:
				ns = NpcString.DEATH_IS_JUST_A_NEW_BEGINNING_HUHU_FEAR_NOT;
				break;
			case 5:
				ns = NpcString.AHH_BEAUTIFUL_GODDES_OF_DEATH_COVER_OVER_THE_FILTH_OF_THOS_WORLD_YOUR_DARKNESS;
				break;
			case 6:
				ns = NpcString.THE_GODDESS_RESURRECTION_HAS_ALREADY_BEGUN_HUHU_INSIGNIFICANT_CREATURES_LIKE_YOU_CAN_DO_NOTHING;
				break;
			default:
				ns = NpcString.A_BLACK_MOON_NOW_DO_YOU_UNDERSTAND_THAT_HE_HAS_OPENED_HIS_EYES;
				break;
		}
		Functions.npcSay(actor, ns);
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
