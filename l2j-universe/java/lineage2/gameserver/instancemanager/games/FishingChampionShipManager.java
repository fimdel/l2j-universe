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
package lineage2.gameserver.instancemanager.games;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishingChampionShipManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FishingChampionShipManager.class);
	/**
	 * Field _instance.
	 */
	private static final FishingChampionShipManager _instance = new FishingChampionShipManager();
	
	/**
	 * Method getInstance.
	 * @return FishingChampionShipManager
	 */
	public static final FishingChampionShipManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _enddate.
	 */
	long _enddate = 0;
	/**
	 * Field _playersName.
	 */
	private final List<String> _playersName = new ArrayList<>();
	/**
	 * Field _fishLength.
	 */
	private final List<String> _fishLength = new ArrayList<>();
	/**
	 * Field _winPlayersName.
	 */
	private final List<String> _winPlayersName = new ArrayList<>();
	/**
	 * Field _winFishLength.
	 */
	private final List<String> _winFishLength = new ArrayList<>();
	/**
	 * Field _tmpPlayers.
	 */
	final List<Fisher> _tmpPlayers = new ArrayList<>();
	/**
	 * Field _winPlayers.
	 */
	final List<Fisher> _winPlayers = new ArrayList<>();
	/**
	 * Field _minFishLength.
	 */
	private double _minFishLength = 0;
	/**
	 * Field _needRefresh.
	 */
	boolean _needRefresh = true;
	
	/**
	 * Constructor for FishingChampionShipManager.
	 */
	private FishingChampionShipManager()
	{
		restoreData();
		refreshWinResult();
		recalculateMinLength();
		if (_enddate <= System.currentTimeMillis())
		{
			_enddate = System.currentTimeMillis();
			new finishChamp().run();
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(new finishChamp(), _enddate - System.currentTimeMillis());
		}
	}
	
	/**
	 * Method setEndOfChamp.
	 */
	void setEndOfChamp()
	{
		Calendar finishtime = Calendar.getInstance();
		finishtime.setTimeInMillis(_enddate);
		finishtime.set(Calendar.MINUTE, 0);
		finishtime.set(Calendar.SECOND, 0);
		finishtime.add(Calendar.DAY_OF_MONTH, 6);
		finishtime.set(Calendar.DAY_OF_WEEK, 3);
		finishtime.set(Calendar.HOUR_OF_DAY, 19);
		_enddate = finishtime.getTimeInMillis();
	}
	
	/**
	 * Method restoreData.
	 */
	private void restoreData()
	{
		_enddate = ServerVariables.getLong("fishChampionshipEnd", 0);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `PlayerName`, `fishLength`, `rewarded` FROM fishing_championship");
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				int rewarded = rs.getInt("rewarded");
				if (rewarded == 0)
				{
					_tmpPlayers.add(new Fisher(rs.getString("PlayerName"), rs.getDouble("fishLength"), 0));
				}
				if (rewarded > 0)
				{
					_winPlayers.add(new Fisher(rs.getString("PlayerName"), rs.getDouble("fishLength"), rewarded));
				}
			}
			rs.close();
		}
		catch (SQLException e)
		{
			_log.warn("Exception: can't get fishing championship info: " + e.getMessage());
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method newFish.
	 * @param pl Player
	 * @param lureId int
	 */
	public synchronized void newFish(Player pl, int lureId)
	{
		if (!Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			return;
		}
		double p1 = Rnd.get(60, 80);
		if ((p1 < 90) && (lureId > 8484) && (lureId < 8486))
		{
			long diff = Math.round(90 - p1);
			if (diff > 1)
			{
				p1 += Rnd.get(1, diff);
			}
		}
		double len = (Rnd.get(100, 999) / 1000.) + p1;
		if (_tmpPlayers.size() < 5)
		{
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getName().equalsIgnoreCase(pl.getName()))
				{
					if (fisher.getLength() < len)
					{
						fisher.setLength(len);
						pl.sendMessage(new CustomMessage("lineage2.gameserver.instancemanager.games.FishingChampionShipManager.ResultImproveOn", pl));
						recalculateMinLength();
					}
					return;
				}
			}
			_tmpPlayers.add(new Fisher(pl.getName(), len, 0));
			pl.sendMessage(new CustomMessage("lineage2.gameserver.instancemanager.games.FishingChampionShipManager.YouInAPrizeList", pl));
			recalculateMinLength();
		}
		else if (_minFishLength < len)
		{
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getName().equalsIgnoreCase(pl.getName()))
				{
					if (fisher.getLength() < len)
					{
						fisher.setLength(len);
						pl.sendMessage(new CustomMessage("lineage2.gameserver.instancemanager.games.FishingChampionShipManager.ResultImproveOn", pl));
						recalculateMinLength();
					}
					return;
				}
			}
			Fisher minFisher = null;
			double minLen = 99999.;
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getLength() < minLen)
				{
					minFisher = fisher;
					minLen = minFisher.getLength();
				}
			}
			_tmpPlayers.remove(minFisher);
			_tmpPlayers.add(new Fisher(pl.getName(), len, 0));
			pl.sendMessage(new CustomMessage("lineage2.gameserver.instancemanager.games.FishingChampionShipManager.YouInAPrizeList", pl));
			recalculateMinLength();
		}
	}
	
	/**
	 * Method recalculateMinLength.
	 */
	private void recalculateMinLength()
	{
		double minLen = 99999.;
		for (Fisher fisher : _tmpPlayers)
		{
			if (fisher.getLength() < minLen)
			{
				minLen = fisher.getLength();
			}
		}
		_minFishLength = minLen;
	}
	
	/**
	 * Method getTimeRemaining.
	 * @return long
	 */
	public long getTimeRemaining()
	{
		return (_enddate - System.currentTimeMillis()) / 60000;
	}
	
	/**
	 * Method getWinnerName.
	 * @param par int
	 * @return String
	 */
	public String getWinnerName(int par)
	{
		if (_winPlayersName.size() >= par)
		{
			return _winPlayersName.get(par - 1);
		}
		return "—";
	}
	
	/**
	 * Method getCurrentName.
	 * @param par int ng
	 * @return String
	 */
	
	public String getCurrentName(int par)
	{
		if (_playersName.size() >= par)
		{
			return _playersName.get(par - 1);
		}
		return "—";
	}
	
	/**
	 * Method getFishLength.
	 * @param par inrin * @return Stringg
	 */
	
	public String getFishLength(int par)
	{
		if (_winFishLength.size() >= par)
		{
			return _winFishLength.get(par - 1);
		}
		return "0";
	}
	
	/**
	 * Method getCurrentFishLength.
	 * @param par inrin * @return Stringg
	 */
	
	public String getCurrentFishLength(int par)
	{
		if (_fishLength.size() >= par)
		{
			return _fishLength.get(par - 1);
		}
		return "0";
	}
	
	/**
	 * Method getReward.
	 * @param pl Player
	 */
	
	public void getReward(Player pl)
	{
		String filename = "fisherman/championship/getReward.htm";
		NpcHtmlMessage html = new NpcHtmlMessage(pl.getObjectId());
		html.setFile(filename);
		pl.sendPacket(html);
		for (Fisher fisher : _winPlayers)
		{
			if (fisher._name.equalsIgnoreCase(pl.getName()))
			{
				if (fisher.getRewardType() != 2)
				{
					int rewardCnt = 0;
					for (int x = 0; x < _winPlayersName.size(); x++)
					{
						if (_winPlayersName.get(x).equalsIgnoreCase(pl.getName()))
						{
							switch (x)
							{
								case 0:
									rewardCnt = Config.ALT_FISH_CHAMPIONSHIP_REWARD_1;
									break;
								case 1:
									rewardCnt = Config.ALT_FISH_CHAMPIONSHIP_REWARD_2;
									break;
								case 2:
									rewardCnt = Config.ALT_FISH_CHAMPIONSHIP_REWARD_3;
									break;
								case 3:
									rewardCnt = Config.ALT_FISH_CHAMPIONSHIP_REWARD_4;
									break;
								case 4:
									rewardCnt = Config.ALT_FISH_CHAMPIONSHIP_REWARD_5;
									break;
							}
						}
					}
					fisher.setRewardType(2);
					if (rewardCnt > 0)
					{
						SystemMessage smsg = new SystemMessage(SystemMessage.YOU_HAVE_EARNED_S2_S1S).addItemName(Config.ALT_FISH_CHAMPIONSHIP_REWARD_ITEM).addNumber(rewardCnt);
						pl.sendPacket(smsg);
						pl.getInventory().addItem(Config.ALT_FISH_CHAMPIONSHIP_REWARD_ITEM, rewardCnt);
						pl.sendItemList(false);
					}
				}
			}
		}
	}
	
	/**
	 * Method showMidResult.
	 * @param pl Player
	 */
	
	public void showMidResult(Player pl)
	{
		if (_needRefresh)
		{
			refreshResult();
			ThreadPoolManager.getInstance().schedule(new needRefresh(), 60000);
		}
		NpcHtmlMessage html = new NpcHtmlMessage(pl.getObjectId());
		String filename = "fisherman/championship/MidResult.htm";
		html.setFile(filename);
		String str = null;
		for (int x = 1; x <= 5; x++)
		{
			str += "<tr><td width=70 align=center>" + x + " Position:" + "</td>";
			str += "<td width=110 align=center>" + getCurrentName(x) + "</td>";
			str += "<td width=80 align=center>" + getCurrentFishLength(x) + "</td></tr>";
		}
		html.replace("%TABLE%", str);
		html.replace("%prizeItem%", ItemHolder.getInstance().getTemplate(Config.ALT_FISH_CHAMPIONSHIP_REWARD_ITEM).getName());
		html.replace("%prizeFirst%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_1));
		html.replace("%prizeTwo%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_2));
		html.replace("%prizeThree%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_3));
		html.replace("%prizeFour%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_4));
		html.replace("%prizeFive%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_5));
		pl.sendPacket(html);
	}
	
	/**
	 * Method showChampScreen.
	 * @param pl Player
	 * @param npc NpcInstance
	 */
	
	public void showChampScreen(Player pl, NpcInstance npc)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(pl.getObjectId());
		String filename = "fisherman/championship/champScreen.htm";
		html.setFile(filename);
		String str = null;
		for (int x = 1; x <= 5; x++)
		{
			str += "<tr><td width=70 align=center>" + x + " Position:" + "</td>";
			str += "<td width=110 align=center>" + getWinnerName(x) + "</td>";
			str += "<td width=80 align=center>" + getFishLength(x) + "</td></tr>";
		}
		html.replace("%TABLE%", str);
		html.replace("%prizeItem%", ItemHolder.getInstance().getTemplate(Config.ALT_FISH_CHAMPIONSHIP_REWARD_ITEM).getName());
		html.replace("%prizeFirst%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_1));
		html.replace("%prizeTwo%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_2));
		html.replace("%prizeThree%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_3));
		html.replace("%prizeFour%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_4));
		html.replace("%prizeFive%", String.valueOf(Config.ALT_FISH_CHAMPIONSHIP_REWARD_5));
		html.replace("%refresh%", String.valueOf(getTimeRemaining()));
		html.replace("%objectId%", String.valueOf(npc.getObjectId()));
		pl.sendPacket(html);
	}
	
	/**
	 * Method shutdown.
	 */
	
	public void shutdown()
	{
		ServerVariables.set("fishChampionshipEnd", _enddate);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM fishing_championship");
			statement.execute();
			statement.close();
			for (Fisher fisher : _winPlayers)
			{
				statement = con.prepareStatement("INSERT INTO fishing_championship(PlayerName,fishLength,rewarded) VALUES (?,?,?)");
				statement.setString(1, fisher.getName());
				statement.setDouble(2, fisher.getLength());
				statement.setInt(3, fisher.getRewardType());
				statement.execute();
				statement.close();
			}
			for (Fisher fisher : _tmpPlayers)
			{
				statement = con.prepareStatement("INSERT INTO fishing_championship(PlayerName,fishLength,rewarded) VALUES (?,?,?)");
				statement.setString(1, fisher.getName());
				statement.setDouble(2, fisher.getLength());
				statement.setInt(3, 0);
				statement.execute();
				statement.close();
			}
		}
		catch (SQLException e)
		{
			_log.warn("Exception: can't update player vitality: " + e.getMessage());
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method refreshResult.
	 */
	
	private synchronized void refreshResult()
	{
		_needRefresh = false;
		_playersName.clear();
		_fishLength.clear();
		Fisher fisher1 = null;
		Fisher fisher2 = null;
		for (int x = 0; x <= (_tmpPlayers.size() - 1); x++)
		{
			for (int y = 0; y <= (_tmpPlayers.size() - 2); y++)
			{
				fisher1 = _tmpPlayers.get(y);
				fisher2 = _tmpPlayers.get(y + 1);
				if (fisher1.getLength() < fisher2.getLength())
				{
					_tmpPlayers.set(y, fisher2);
					_tmpPlayers.set(y + 1, fisher1);
				}
			}
		}
		for (int x = 0; x <= (_tmpPlayers.size() - 1); x++)
		{
			_playersName.add(_tmpPlayers.get(x)._name);
			_fishLength.add(String.valueOf(_tmpPlayers.get(x).getLength()));
		}
	}
	
	/**
	 * Method refreshWinResult.
	 */
	
	void refreshWinResult()
	{
		_winPlayersName.clear();
		_winFishLength.clear();
		Fisher fisher1 = null;
		Fisher fisher2 = null;
		for (int x = 0; x <= (_winPlayers.size() - 1); x++)
		{
			for (int y = 0; y <= (_winPlayers.size() - 2); y++)
			{
				fisher1 = _winPlayers.get(y);
				fisher2 = _winPlayers.get(y + 1);
				if (fisher1.getLength() < fisher2.getLength())
				{
					_winPlayers.set(y, fisher2);
					_winPlayers.set(y + 1, fisher1);
				}
			}
		}
		for (int x = 0; x <= (_winPlayers.size() - 1); x++)
		{
			_winPlayersName.add(_winPlayers.get(x)._name);
			_winFishLength.add(String.valueOf(_winPlayers.get(x).getLength()));
		}
	}/*
	 * * @author Mobius
	 */
	
	private class finishChamp extends RunnableImpl
	/**
	 * Constructor for finishChamp.
	 */
	/**
	 * Constructor for finishChamp.
	 */
	{
		public finishChamp()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		
		@Override
		public void runImpl()
		{
			_winPlayers.clear();
			for (Fisher fisher : _tmpPlayers)
			{
				fisher.setRewardType(1);
				_winPlayers.add(fisher);
			}
			_tmpPlayers.clear();
			refreshWinResult();
			setEndOfChamp();
			shutdown();
			_log.info("Fishing Championship Manager : start new event period.");
			ThreadPoolManager.getInstance().schedule(new finishChamp(), _enddate - System.currentTimeMillis());
		}
	}/*
	 * * @author Mobius
	 */
	
	private class needRefresh extends RunnableImpl
	/**
	 * Constructor for needRefresh.
	 */
	/**
	 * Constructor for needRefresh.
	 */
	{
		public needRefresh()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		
		@Override
		public void runImpl()
		{
			_needRefresh = true;
		}
	}/*
	 * * @author Mobius
	 */
	
	private class Fisher
	/**
	 * Field _length.
	 */
	/**
	 * Field _length.
	 */
	{
		private double _length = 0./**
		 * Field _name.
		 */
		/**
		 * Field _name.
		 */
		;
		String _name/**
		 * Field _reward.
		 */
		/**
		 * Field _reward.
		 */
		;
		private int _reward = 0;
		
		/**
		 * Constructor for Fisher.
		 * @param name String
		 * @param length double
		 * @param rewardType int
		 */
		
		public Fisher(String name, double length, int rewardType)
		{
			setName(name);
			setLength(length);
			setRewardType(rewardType);
		}
		
		/**
		 * Method setLength.
		 * @param value double
		 */
		
		public void setLength(double value)
		{
			_length = value;
		}
		
		/**
		 * Method setName.
		 * @param value String
		 */
		
		public void setName(String value)
		{
			_name = value;
		}
		
		/**
		 * Method setRewardType.
		 * @param value int
		 */
		
		public void setRewardType(int value)
		{
			_reward = value;
		}
		
		/**
		 * Method getName.ring * @return String
		 */
		
		public String getName()
		{
			return _name;
		}
		
		/**
		 * Method getRewardType. int * @return int
		 */
		
		public int getRewardType()
		{
			return _reward;
		}
		
		/**
		 * Method getLength.uble * @return double
		 */
		
		public double getLength()
		{
			return _length;
		}
	}
}
