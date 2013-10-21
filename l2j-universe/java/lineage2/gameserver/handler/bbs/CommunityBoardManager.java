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
package lineage2.gameserver.handler.bbs;

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.Config;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommunityBoardManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CommunityBoardManager.class);
	/**
	 * Field _instance.
	 */
	private static final CommunityBoardManager _instance = new CommunityBoardManager();
	/**
	 * Field _handlers.
	 */
	private final Map<String, ICommunityBoardHandler> _handlers = new HashMap<>();
	/**
	 * Field _properties.
	 */
	private final StatsSet _properties = new StatsSet();
	
	/**
	 * Method getInstance.
	 * @return CommunityBoardManager
	 */
	public static CommunityBoardManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for CommunityBoardManager.
	 */
	private CommunityBoardManager()
	{
	}
	
	/**
	 * Method registerHandler.
	 * @param commHandler ICommunityBoardHandler
	 */
	public void registerHandler(ICommunityBoardHandler commHandler)
	{
		for (String bypass : commHandler.getBypassCommands())
		{
			if (_handlers.containsKey(bypass))
			{
				_log.warn("CommunityBoard: dublicate bypass registered! First handler: " + _handlers.get(bypass).getClass().getSimpleName() + " second: " + commHandler.getClass().getSimpleName());
			}
			_handlers.put(bypass, commHandler);
		}
	}
	
	/**
	 * Method removeHandler.
	 * @param handler ICommunityBoardHandler
	 */
	public void removeHandler(ICommunityBoardHandler handler)
	{
		for (String bypass : handler.getBypassCommands())
		{
			_handlers.remove(bypass);
		}
		_log.info("CommunityBoard: " + handler.getClass().getSimpleName() + " unloaded.");
	}
	
	/**
	 * Method getCommunityHandler.
	 * @param bypass String
	 * @return ICommunityBoardHandler
	 */
	public ICommunityBoardHandler getCommunityHandler(String bypass)
	{
		if (!Config.COMMUNITYBOARD_ENABLED || _handlers.isEmpty())
		{
			return null;
		}
		for (Map.Entry<String, ICommunityBoardHandler> entry : _handlers.entrySet())
		{
			if (bypass.contains(entry.getKey()))
			{
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Method setProperty.
	 * @param name String
	 * @param val String
	 */
	public void setProperty(String name, String val)
	{
		_properties.set(name, val);
	}
	
	/**
	 * Method setProperty.
	 * @param name String
	 * @param val int
	 */
	public void setProperty(String name, int val)
	{
		_properties.set(name, val);
	}
	
	/**
	 * Method getIntProperty.
	 * @param name String
	 * @return int
	 */
	public int getIntProperty(String name)
	{
		return _properties.getInteger(name, 0);
	}
}
