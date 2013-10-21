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
package lineage2.gameserver.model.entity.olympiad;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadManager extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadManager.class);
	/**
	 * Field _olympiadInstances.
	 */
	private final Map<Integer, OlympiadGame> _olympiadInstances = new ConcurrentHashMap<>();
	
	/**
	 * Method sleep.
	 * @param time long
	 */
	public void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		if (Olympiad.isOlympiadEnd())
		{
			return;
		}
		while (Olympiad.inCompPeriod())
		{
			if (Olympiad._nobles.isEmpty())
			{
				sleep(60000);
				continue;
			}
			while (Olympiad.inCompPeriod())
			{
				if (Olympiad._nonClassBasedRegisters.size() >= Config.NONCLASS_GAME_MIN)
				{
					prepareBattles(CompType.NON_CLASSED, Olympiad._nonClassBasedRegisters);
				}
				for (Map.Entry<Integer, List<Integer>> entry : Olympiad._classBasedRegisters.entrySet())
				{
					if (entry.getValue().size() >= Config.CLASS_GAME_MIN)
					{
						prepareBattles(CompType.CLASSED, entry.getValue());
					}
				}
				sleep(30000);
			}
			sleep(30000);
		}
		Olympiad._classBasedRegisters.clear();
		Olympiad._nonClassBasedRegisters.clear();
		boolean allGamesTerminated = false;
		while (!allGamesTerminated)
		{
			sleep(30000);
			if (_olympiadInstances.isEmpty())
			{
				break;
			}
			allGamesTerminated = true;
			for (OlympiadGame game : _olympiadInstances.values())
			{
				if ((game.getTask() != null) && !game.getTask().isTerminated())
				{
					allGamesTerminated = false;
				}
			}
		}
		_olympiadInstances.clear();
	}
	
	/**
	 * Method prepareBattles.
	 * @param type CompType
	 * @param list List<Integer>
	 */
	private void prepareBattles(CompType type, List<Integer> list)
	{
		for (int i = 0; i < Olympiad.STADIUMS.length; i++)
		{
			try
			{
				if (!Olympiad.STADIUMS[i].isFreeToUse())
				{
					continue;
				}
				if (list.size() < type.getMinSize())
				{
					break;
				}
				OlympiadGame game = new OlympiadGame(i, type, nextOpponents(list, type));
				game.sheduleTask(new OlympiadGameTask(game, BattleStatus.Begining, 0, 1));
				_olympiadInstances.put(i, game);
				Olympiad.STADIUMS[i].setStadiaBusy();
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	/**
	 * Method freeOlympiadInstance.
	 * @param index int
	 */
	public void freeOlympiadInstance(int index)
	{
		_olympiadInstances.remove(index);
		Olympiad.STADIUMS[index].setStadiaFree();
	}
	
	/**
	 * Method getOlympiadInstance.
	 * @param index int
	 * @return OlympiadGame
	 */
	public OlympiadGame getOlympiadInstance(int index)
	{
		return _olympiadInstances.get(index);
	}
	
	/**
	 * Method getOlympiadGames.
	 * @return Map<Integer,OlympiadGame>
	 */
	public Map<Integer, OlympiadGame> getOlympiadGames()
	{
		return _olympiadInstances;
	}
	
	/**
	 * Method nextOpponents.
	 * @param list List<Integer>
	 * @param type CompType
	 * @return List<Integer>
	 */
	private List<Integer> nextOpponents(List<Integer> list, CompType type)
	{
		List<Integer> opponents = new CopyOnWriteArrayList<>();
		Integer noble;
		for (int i = 0; i < type.getMinSize(); i++)
		{
			noble = list.remove(Rnd.get(list.size()));
			opponents.add(noble);
			removeOpponent(noble);
		}
		return opponents;
	}
	
	/**
	 * Method removeOpponent.
	 * @param noble Integer
	 */
	private void removeOpponent(Integer noble)
	{
		Olympiad._classBasedRegisters.removeValue(noble);
		Olympiad._nonClassBasedRegisters.remove(noble);
	}
}
