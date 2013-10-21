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
package lineage2.gameserver.utils;

import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.map.hash.TIntLongHashMap;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AntiFlood
{
	/**
	 * Field _recentReceivers.
	 */
	private final TIntLongHashMap _recentReceivers = new TIntLongHashMap();
	/**
	 * Field _lastSent.
	 */
	private long _lastSent = 0L;
	/**
	 * Field _lastText.
	 */
	private String _lastText = StringUtils.EMPTY;
	/**
	 * Field _lastHeroTime.
	 */
	private long _lastHeroTime;
	/**
	 * Field _lastTradeTime.
	 */
	private long _lastTradeTime;
	/**
	 * Field _lastShoutTime.
	 */
	private long _lastShoutTime;
	/**
	 * Field _lastMailTime.
	 */
	private long _lastMailTime;
	
	/**
	 * Method canTrade.
	 * @param text String
	 * @return boolean
	 */
	public boolean canTrade(String text)
	{
		long currentMillis = System.currentTimeMillis();
		if ((currentMillis - _lastTradeTime) < 5000L)
		{
			return false;
		}
		_lastTradeTime = currentMillis;
		return true;
	}
	
	/**
	 * Method canShout.
	 * @param text String
	 * @return boolean
	 */
	public boolean canShout(String text)
	{
		long currentMillis = System.currentTimeMillis();
		if ((currentMillis - _lastShoutTime) < 5000L)
		{
			return false;
		}
		_lastShoutTime = currentMillis;
		return true;
	}
	
	/**
	 * Method canHero.
	 * @param text String
	 * @return boolean
	 */
	public boolean canHero(String text)
	{
		long currentMillis = System.currentTimeMillis();
		if ((currentMillis - _lastHeroTime) < 10000L)
		{
			return false;
		}
		_lastHeroTime = currentMillis;
		return true;
	}
	
	/**
	 * Method canMail.
	 * @return boolean
	 */
	public boolean canMail()
	{
		long currentMillis = System.currentTimeMillis();
		if ((currentMillis - _lastMailTime) < 10000L)
		{
			return false;
		}
		_lastMailTime = currentMillis;
		return true;
	}
	
	/**
	 * Method canTell.
	 * @param charId int
	 * @param text String
	 * @return boolean
	 */
	public boolean canTell(int charId, String text)
	{
		long currentMillis = System.currentTimeMillis();
		long lastSent;
		TIntLongIterator itr = _recentReceivers.iterator();
		int recent = 0;
		while (itr.hasNext())
		{
			itr.advance();
			lastSent = itr.value();
			if ((currentMillis - lastSent) < (text.equalsIgnoreCase(_lastText) ? 600000L : 60000L))
			{
				recent++;
			}
			else
			{
				itr.remove();
			}
		}
		lastSent = _recentReceivers.put(charId, currentMillis);
		long delay = 333L;
		if (recent > 3)
		{
			lastSent = _lastSent;
			delay = (recent - 3) * 3333L;
		}
		_lastText = text;
		_lastSent = currentMillis;
		return (currentMillis - lastSent) > delay;
	}
}
