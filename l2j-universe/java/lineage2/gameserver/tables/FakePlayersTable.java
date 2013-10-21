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
package lineage2.gameserver.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;

import lineage2.commons.collections.GArray;
import lineage2.commons.collections.GCArray;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectsStorage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FakePlayersTable
{
	/**
	 * @author Mobius
	 */
	public class Task implements Runnable
	{
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			try
			{
				if ((_activeFakePlayers.size() < ((Math.max(0, GameObjectsStorage.getAllPlayersCount() - GameObjectsStorage.getAllOfflineCount()) * Config.FAKE_PLAYERS_PERCENT) / 100)) && (_activeFakePlayers.size() < _fakePlayers.length))
				{
					if (Rnd.chance(10))
					{
						String player = _fakePlayers[Rnd.get(_fakePlayers.length)];
						if ((player != null) && !_activeFakePlayers.contains(player))
						{
							_activeFakePlayers.add(player);
						}
					}
				}
				else if (_activeFakePlayers.size() > 0)
				{
					_activeFakePlayers.remove(Rnd.get(_activeFakePlayers.size()));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = Logger.getLogger(FakePlayersTable.class.getName());
	/**
	 * Field _fakePlayers.
	 */
	static String[] _fakePlayers;
	/**
	 * Field _activeFakePlayers.
	 */
	static GCArray<String> _activeFakePlayers = new GCArray<>();
	/**
	 * Field _instance.
	 */
	private static FakePlayersTable _instance;
	
	/**
	 * Method getInstance.
	 * @return FakePlayersTable
	 */
	public static FakePlayersTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new FakePlayersTable();
		}
		return _instance;
	}
	
	/**
	 * Constructor for FakePlayersTable.
	 */
	public FakePlayersTable()
	{
		if (Config.ALLOW_FAKE_PLAYERS)
		{
			parseData();
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new Task(), 180000, 1000);
		}
	}
	
	/**
	 * Method parseData.
	 */
	private void parseData()
	{
		LineNumberReader lnr = null;
		try
		{
			File doorData = new File(Config.FAKE_PLAYERS_LIST);
			lnr = new LineNumberReader(new BufferedReader(new FileReader(doorData)));
			String line;
			GArray<String> players_list = new GArray<>();
			while ((line = lnr.readLine()) != null)
			{
				if ((line.trim().length() == 0) || ((line.length() > 0) && (line.charAt(0) == '#')))
				{
					continue;
				}
				players_list.add(line);
			}
			_fakePlayers = players_list.toArray(new String[players_list.size()]);
			_log.config("FakePlayersTable: Loaded " + _fakePlayers.length + " Fake Players.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (lnr != null)
				{
					lnr.close();
				}
			}
			catch (Exception e1)
			{
			}
		}
	}
	
	/**
	 * Method getFakePlayersCount.
	 * @return int
	 */
	public static int getFakePlayersCount()
	{
		return _activeFakePlayers.size();
	}
	
	/**
	 * Method getActiveFakePlayers.
	 * @return GCArray<String>
	 */
	public static GCArray<String> getActiveFakePlayers()
	{
		return _activeFakePlayers;
	}
}
