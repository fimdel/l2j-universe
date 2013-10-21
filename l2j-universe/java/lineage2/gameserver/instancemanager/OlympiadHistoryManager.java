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
package lineage2.gameserver.instancemanager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.gameserver.dao.OlympiadHistoryDAO;
import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.olympiad.OlympiadHistory;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.HtmlUtils;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadHistoryManager
{
	/**
	 * Field _instance.
	 */
	private static final OlympiadHistoryManager _instance = new OlympiadHistoryManager();
	/**
	 * Field _historyNew.
	 */
	private final IntObjectMap<List<OlympiadHistory>> _historyNew = new CHashIntObjectMap<>();
	/**
	 * Field _historyOld.
	 */
	private final IntObjectMap<List<OlympiadHistory>> _historyOld = new CHashIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return OlympiadHistoryManager
	 */
	public static OlympiadHistoryManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for OlympiadHistoryManager.
	 */
	OlympiadHistoryManager()
	{
		Map<Boolean, List<OlympiadHistory>> historyList = OlympiadHistoryDAO.getInstance().select();
		for (Map.Entry<Boolean, List<OlympiadHistory>> entry : historyList.entrySet())
		{
			for (OlympiadHistory history : entry.getValue())
			{
				addHistory(entry.getKey(), history);
			}
		}
	}
	
	/**
	 * Method switchData.
	 */
	public void switchData()
	{
		_historyOld.clear();
		_historyOld.putAll(_historyNew);
		_historyNew.clear();
		OlympiadHistoryDAO.getInstance().switchData();
	}
	
	/**
	 * Method saveHistory.
	 * @param history OlympiadHistory
	 */
	public void saveHistory(OlympiadHistory history)
	{
		addHistory(false, history);
		OlympiadHistoryDAO.getInstance().insert(history);
	}
	
	/**
	 * Method addHistory.
	 * @param old boolean
	 * @param history OlympiadHistory
	 */
	public void addHistory(boolean old, OlympiadHistory history)
	{
		IntObjectMap<List<OlympiadHistory>> map = old ? _historyOld : _historyNew;
		addHistory0(map, history.getObjectId1(), history);
		addHistory0(map, history.getObjectId2(), history);
	}
	
	/**
	 * Method addHistory0.
	 * @param map IntObjectMap<List<OlympiadHistory>>
	 * @param objectId int
	 * @param history OlympiadHistory
	 */
	private void addHistory0(IntObjectMap<List<OlympiadHistory>> map, int objectId, OlympiadHistory history)
	{
		List<OlympiadHistory> historySet = map.get(objectId);
		if (historySet == null)
		{
			map.put(objectId, historySet = new CopyOnWriteArrayList<>());
		}
		historySet.add(history);
	}
	
	/**
	 * Method showHistory.
	 * @param player Player
	 * @param targetClassId int
	 * @param page int
	 */
	public void showHistory(Player player, int targetClassId, int page)
	{
		final int perpage = 15;
		Map.Entry<Integer, StatsSet> entry = Hero.getInstance().getHeroStats(targetClassId);
		if (entry == null)
		{
			return;
		}
		List<OlympiadHistory> historyList = _historyOld.get(entry.getKey());
		if (historyList == null)
		{
			historyList = Collections.emptyList();
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, null);
		html.setFile("olympiad/monument_hero_info.htm");
		html.replace("%title%", StringHolder.getInstance().getNotNull(player, "hero.history"));
		int allStatWinner = 0;
		int allStatLoss = 0;
		int allStatTie = 0;
		for (OlympiadHistory h : historyList)
		{
			if (h.getGameStatus() == 0)
			{
				allStatTie++;
			}
			else
			{
				int team = entry.getKey() == h.getObjectId1() ? 1 : 2;
				if (h.getGameStatus() == team)
				{
					allStatWinner++;
				}
				else
				{
					allStatLoss++;
				}
			}
		}
		html.replace("%wins%", String.valueOf(allStatWinner));
		html.replace("%ties%", String.valueOf(allStatTie));
		html.replace("%losses%", String.valueOf(allStatLoss));
		int min = perpage * (page - 1);
		int max = perpage * page;
		int currentWinner = 0;
		int currentLoss = 0;
		int currentTie = 0;
		final StringBuilder b = new StringBuilder(500);
		for (int i = 0; i < historyList.size(); i++)
		{
			OlympiadHistory history = historyList.get(i);
			if (history.getGameStatus() == 0)
			{
				currentTie++;
			}
			else
			{
				int team = entry.getKey() == history.getObjectId1() ? 1 : 2;
				if (history.getGameStatus() == team)
				{
					currentWinner++;
				}
				else
				{
					currentLoss++;
				}
			}
			if (i < min)
			{
				continue;
			}
			if (i >= max)
			{
				break;
			}
			b.append("<tr><td>");
			b.append(history.toString(player, entry.getKey(), currentWinner, currentLoss, currentTie));
			b.append("</td></tr");
		}
		if (min > 0)
		{
			html.replace("%buttprev%", HtmlUtils.PREV_BUTTON);
			html.replace("%prev_bypass%", "_match?class=" + targetClassId + "&page=" + (page - 1));
		}
		else
		{
			html.replace("%buttprev%", StringUtils.EMPTY);
		}
		if (historyList.size() > max)
		{
			html.replace("%buttnext%", HtmlUtils.NEXT_BUTTON);
			html.replace("%prev_bypass%", "_match?class=" + targetClassId + "&page=" + (page + 1));
		}
		else
		{
			html.replace("%buttnext%", StringUtils.EMPTY);
		}
		html.replace("%list%", b.toString());
		player.sendPacket(html);
	}
}
