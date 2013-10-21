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
package ai;

import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.listener.game.OnDayNightChangeListener;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NightAgressionMystic extends Mystic
{
	/**
	 * Constructor for NightAgressionMystic.
	 * @param actor NpcInstance
	 */
	public NightAgressionMystic(NpcInstance actor)
	{
		super(actor);
		GameTimeController.getInstance().addListener(new NightAgressionDayNightListener());
	}
	
	/**
	 * @author Mobius
	 */
	private class NightAgressionDayNightListener implements OnDayNightChangeListener
	{
		/**
		 * Constructor for NightAgressionDayNightListener.
		 */
		NightAgressionDayNightListener()
		{
			if (GameTimeController.getInstance().isNowNight())
			{
				onNight();
			}
			else
			{
				onDay();
			}
		}
		
		/**
		 * Method onDay.
		 * @see lineage2.gameserver.listener.game.OnDayNightChangeListener#onDay()
		 */
		@Override
		public void onDay()
		{
			getActor().setAggroRange(0);
		}
		
		/**
		 * Method onNight.
		 * @see lineage2.gameserver.listener.game.OnDayNightChangeListener#onNight()
		 */
		@Override
		public void onNight()
		{
			getActor().setAggroRange(-1);
		}
	}
}
