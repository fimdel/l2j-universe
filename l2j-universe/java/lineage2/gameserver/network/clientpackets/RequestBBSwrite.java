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

import lineage2.gameserver.Config;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestBBSwrite extends L2GameClientPacket
{
	/**
	 * Field _url.
	 */
	private String _url;
	/**
	 * Field _arg1.
	 */
	private String _arg1;
	/**
	 * Field _arg2.
	 */
	private String _arg2;
	/**
	 * Field _arg3.
	 */
	private String _arg3;
	/**
	 * Field _arg4.
	 */
	private String _arg4;
	/**
	 * Field _arg5.
	 */
	private String _arg5;
	
	/**
	 * Method readImpl.
	 */
	@Override
	public void readImpl()
	{
		_url = readS();
		_arg1 = readS();
		_arg2 = readS();
		_arg3 = readS();
		_arg4 = readS();
		_arg5 = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		ICommunityBoardHandler handler = CommunityBoardManager.getInstance().getCommunityHandler(_url);
		if (handler != null)
		{
			if (!Config.COMMUNITYBOARD_ENABLED)
			{
				activeChar.sendPacket(new SystemMessage2(SystemMsg.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE));
			}
			else
			{
				handler.onWriteCommand(activeChar, _url, _arg1, _arg2, _arg3, _arg4, _arg5);
			}
		}
	}
}
