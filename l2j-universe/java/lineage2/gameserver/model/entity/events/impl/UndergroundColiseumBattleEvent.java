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
package lineage2.gameserver.model.entity.events.impl;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class UndergroundColiseumBattleEvent extends GlobalEvent
{
	/**
	 * Constructor for UndergroundColiseumBattleEvent.
	 * @param player1 Player
	 * @param player2 Player
	 */
	protected UndergroundColiseumBattleEvent(Player player1, Player player2)
	{
		super(0, player1.getObjectId() + "_" + player2.getObjectId());
	}
	
	/**
	 * Method announce.
	 * @param val int
	 */
	@Override
	public void announce(int val)
	{
		switch (val)
		{
			case -180:
			case -120:
			case -60:
				break;
		}
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		registerActions();
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return System.currentTimeMillis() + 180000L;
	}
}
