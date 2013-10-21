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
package lineage2.gameserver.handler.chat;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.network.serverpackets.components.ChatType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChatHandler extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ChatHandler _instance = new ChatHandler();
	/**
	 * Field _handlers.
	 */
	private final IChatHandler[] _handlers = new IChatHandler[ChatType.VALUES.length];
	
	/**
	 * Method getInstance.
	 * @return ChatHandler
	 */
	public static ChatHandler getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ChatHandler.
	 */
	private ChatHandler()
	{
	}
	
	/**
	 * Method register.
	 * @param chatHandler IChatHandler
	 */
	public void register(IChatHandler chatHandler)
	{
		_handlers[chatHandler.getType().ordinal()] = chatHandler;
	}
	
	/**
	 * Method getHandler.
	 * @param type ChatType
	 * @return IChatHandler
	 */
	public IChatHandler getHandler(ChatType type)
	{
		return _handlers[type.ordinal()];
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _handlers.length;
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
	}
}
