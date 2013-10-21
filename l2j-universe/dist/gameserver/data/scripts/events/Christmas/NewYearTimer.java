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
package events.Christmas;

import java.util.Calendar;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NewYearTimer implements ScriptFile
{
	/**
	 * Field instance.
	 */
	static NewYearTimer instance;
	
	/**
	 * Method getInstance.
	 * @return NewYearTimer
	 */
	public static NewYearTimer getInstance()
	{
		if (instance == null)
		{
			new NewYearTimer();
		}
		return instance;
	}
	
	/**
	 * Constructor for NewYearTimer.
	 */
	public NewYearTimer()
	{
		if (instance != null)
		{
			return;
		}
		instance = this;
		if (!isActive())
		{
			return;
		}
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		while (getDelay(c) < 0)
		{
			c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
		}
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("С Новым, " + c.get(Calendar.YEAR) + ", Годом!!!"), getDelay(c));
		c.add(Calendar.SECOND, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("1"), getDelay(c));
		c.add(Calendar.SECOND, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("2"), getDelay(c));
		c.add(Calendar.SECOND, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("3"), getDelay(c));
		c.add(Calendar.SECOND, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("4"), getDelay(c));
		c.add(Calendar.SECOND, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("5"), getDelay(c));
	}
	
	/**
	 * Method getDelay.
	 * @param c Calendar
	 * @return long
	 */
	private long getDelay(Calendar c)
	{
		return c.getTime().getTime() - System.currentTimeMillis();
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		// empty method
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		// empty method
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return ServerVariables.getString("Christmas", "off").equalsIgnoreCase("on");
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
	
	/**
	 * @author Mobius
	 */
	static private class NewYearAnnouncer extends RunnableImpl
	{
		/**
		 * Field message.
		 */
		private final String message;
		
		/**
		 * Constructor for NewYearAnnouncer.
		 * @param message String
		 */
		NewYearAnnouncer(String message)
		{
			this.message = message;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Announcements.getInstance().announceToAll(message);
			if (message.length() == 1)
			{
				return;
			}
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				Skill skill = SkillTable.getInstance().getInfo(3266, 1);
				MagicSkillUse msu = new MagicSkillUse(player, player, 3266, 1, skill.getHitTime(), 0);
				player.broadcastPacket(msu);
			}
			instance = null;
			new NewYearTimer();
		}
	}
}
