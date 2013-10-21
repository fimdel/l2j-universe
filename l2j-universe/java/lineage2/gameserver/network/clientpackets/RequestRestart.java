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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.GameClient.GameClientState;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.CharacterSelectionInfo;
import lineage2.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;
import lineage2.gameserver.network.serverpackets.RestartResponse;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRestart extends L2GameClientPacket
{
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isInObserverMode())
		{
			activeChar.sendPacket(Msg.OBSERVERS_CANNOT_PARTICIPATE, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}
		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RESTART_WHILE_IN_COMBAT, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_ANYTHING_ELSE_WHILE_FISHING, RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}
		if (activeChar.isBlocked() && !activeChar.isFlying())
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestRestart.OutOfControl", activeChar));
			activeChar.sendPacket(RestartResponse.FAIL, ActionFail.STATIC);
			return;
		}
		if (getClient() != null)
		{
			getClient().setState(GameClientState.AUTHED);
		}
		activeChar.restart();
		CharacterSelectionInfo cl = new CharacterSelectionInfo(getClient().getLogin(), getClient().getSessionKey().playOkID1);
		ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(cl.getCharInfo());
		sendPacket(RestartResponse.OK, cl, vl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}
