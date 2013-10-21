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
package lineage2.gameserver;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.utils.MapUtils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Announcements
{
	/**
	 * @author Mobius
	 */
	public class Announce extends RunnableImpl
	{
		/**
		 * Field _task.
		 */
		private Future<?> _task;
		/**
		 * Field _time.
		 */
		private final int _time;
		/**
		 * Field _announce.
		 */
		private final String _announce;
		
		/**
		 * Constructor for Announce.
		 * @param t int
		 * @param announce String
		 */
		public Announce(int t, String announce)
		{
			_time = t;
			_announce = announce;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			announceToAll(_announce);
		}
		
		/**
		 * Method showAnnounce.
		 * @param player Player
		 */
		public void showAnnounce(Player player)
		{
			Say2 cs = new Say2(0, ChatType.ANNOUNCEMENT, player.getName(), _announce);
			player.sendPacket(cs);
		}
		
		/**
		 * Method start.
		 */
		public void start()
		{
			if (_time > 0)
			{
				_task = ThreadPoolManager.getInstance().scheduleAtFixedRate(this, _time * 1000L, _time * 1000L);
			}
		}
		
		/**
		 * Method stop.
		 */
		public void stop()
		{
			if (_task != null)
			{
				_task.cancel(false);
				_task = null;
			}
		}
		
		/**
		 * Method getTime.
		 * @return int
		 */
		public int getTime()
		{
			return _time;
		}
		
		/**
		 * Method getAnnounce.
		 * @return String
		 */
		public String getAnnounce()
		{
			return _announce;
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Announcements.class);
	/**
	 * Field _instance.
	 */
	private static final Announcements _instance = new Announcements();
	
	/**
	 * Method getInstance.
	 * @return Announcements
	 */
	public static final Announcements getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _announcements.
	 */
	private final List<Announce> _announcements = new ArrayList<>();
	
	/**
	 * Constructor for Announcements.
	 */
	private Announcements()
	{
		loadAnnouncements();
	}
	
	/**
	 * Method getAnnouncements.
	 * @return List<Announce>
	 */
	public List<Announce> getAnnouncements()
	{
		return _announcements;
	}
	
	/**
	 * Method loadAnnouncements.
	 */
	public void loadAnnouncements()
	{
		_announcements.clear();
		try
		{
			List<String> lines = Arrays.asList(FileUtils.readFileToString(new File("config/announcements.txt"), "UTF-8").split("\n"));
			for (String line : lines)
			{
				StringTokenizer token = new StringTokenizer(line, "\t");
				if (token.countTokens() > 1)
				{
					addAnnouncement(Integer.parseInt(token.nextToken()), token.nextToken(), false);
				}
				else
				{
					addAnnouncement(0, line, false);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error while loading config/announcements.txt!");
		}
	}
	
	/**
	 * Method showAnnouncements.
	 * @param activeChar Player
	 */
	public void showAnnouncements(Player activeChar)
	{
		for (Announce announce : _announcements)
		{
			announce.showAnnounce(activeChar);
		}
	}
	
	/**
	 * Method addAnnouncement.
	 * @param val int
	 * @param text String
	 * @param save boolean
	 */
	public void addAnnouncement(int val, String text, boolean save)
	{
		Announce announce = new Announce(val, text);
		announce.start();
		_announcements.add(announce);
		if (save)
		{
			saveToDisk();
		}
	}
	
	/**
	 * Method delAnnouncement.
	 * @param line int
	 */
	public void delAnnouncement(int line)
	{
		Announce announce = _announcements.remove(line);
		if (announce != null)
		{
			announce.stop();
		}
		saveToDisk();
	}
	
	/**
	 * Method saveToDisk.
	 */
	private void saveToDisk()
	{
		try
		{
			File f = new File("config/announcements.txt");
			FileWriter writer = new FileWriter(f, false);
			for (Announce announce : _announcements)
			{
				writer.write(announce.getTime() + "\t" + announce.getAnnounce() + "\n");
			}
			writer.close();
		}
		catch (Exception e)
		{
			_log.error("Error while saving config/announcements.txt!", e);
		}
	}
	
	/**
	 * Method announceToAll.
	 * @param text String
	 */
	public void announceToAll(String text)
	{
		announceToAll(text, ChatType.ANNOUNCEMENT);
	}
	
	/**
	 * Method shout.
	 * @param activeChar Creature
	 * @param text String
	 * @param type ChatType
	 */
	public static void shout(Creature activeChar, String text, ChatType type)
	{
		Say2 cs = new Say2(activeChar.getObjectId(), type, activeChar.getName(), text);
		int rx = MapUtils.regionX(activeChar);
		int ry = MapUtils.regionY(activeChar);
		int offset = Config.SHOUT_OFFSET;
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if ((player == activeChar) || (activeChar.getReflection() != player.getReflection()))
			{
				continue;
			}
			int tx = MapUtils.regionX(player);
			int ty = MapUtils.regionY(player);
			if (((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset))) || activeChar.isInRangeZ(player, Config.CHAT_RANGE))
			{
				player.sendPacket(cs);
			}
		}
		activeChar.sendPacket(cs);
	}
	
	/**
	 * Method announceToAll.
	 * @param text String
	 * @param type ChatType
	 */
	public void announceToAll(String text, ChatType type)
	{
		Say2 cs = new Say2(0, type, "", text);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(cs);
		}
	}
	public void announceToAll(IStaticPacket sm) 
        {
			for(Player player : GameObjectsStorage.getAllPlayersForIterate())
				player.sendPacket(sm);
        }
	
	/**
	 * Method announceByCustomMessage.
	 * @param address String
	 * @param replacements String[]
	 */
	public void announceByCustomMessage(String address, String[] replacements)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			announceToPlayerByCustomMessage(player, address, replacements);
		}
	}
	
	/**
	 * Method announceByCustomMessage.
	 * @param address String
	 * @param replacements String[]
	 * @param type ChatType
	 */
	public void announceByCustomMessage(String address, String[] replacements, ChatType type)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			announceToPlayerByCustomMessage(player, address, replacements, type);
		}
	}
	
	/**
	 * Method announceToPlayerByCustomMessage.
	 * @param player Player
	 * @param address String
	 * @param replacements String[]
	 */
	public void announceToPlayerByCustomMessage(Player player, String address, String[] replacements)
	{
		CustomMessage cm = new CustomMessage(address, player);
		if (replacements != null)
		{
			for (String s : replacements)
			{
				cm.addString(s);
			}
		}
		player.sendPacket(new Say2(0, ChatType.ANNOUNCEMENT, "", cm.toString()));
	}
	
	/**
	 * Method announceToPlayerByCustomMessage.
	 * @param player Player
	 * @param address String
	 * @param replacements String[]
	 * @param type ChatType
	 */
	public void announceToPlayerByCustomMessage(Player player, String address, String[] replacements, ChatType type)
	{
		CustomMessage cm = new CustomMessage(address, player);
		if (replacements != null)
		{
			for (String s : replacements)
			{
				cm.addString(s);
			}
		}
		player.sendPacket(new Say2(0, type, "", cm.toString()));
	}
	
	/**
	 * Method announceToAll.
	 * @param sm SystemMessage
	 */
	public void announceToAll(SystemMessage sm)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(sm);
		}
	}
}
