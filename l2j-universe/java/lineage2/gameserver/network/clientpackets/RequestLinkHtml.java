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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestLinkHtml extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(RequestLinkHtml.class);
	/**
	 * Field _link.
	 */
	private String _link;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_link = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player actor = getClient().getActiveChar();
		if (actor == null)
		{
			return;
		}
		if (_link.contains("..") || !_link.endsWith(".htm"))
		{
			_log.warn("[RequestLinkHtml] hack? link contains prohibited characters: '" + _link + "', skipped");
			return;
		}
		try
		{
			NpcHtmlMessage msg = new NpcHtmlMessage(0);
			msg.setFile("" + _link);
			sendPacket(msg);
		}
		catch (Exception e)
		{
			_log.warn("Bad RequestLinkHtml: ", e);
		}
	}
}
