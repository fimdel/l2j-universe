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
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestTargetCanceld extends L2GameClientPacket
{
	/**
	 * Field _unselect.
	 */
	private int _unselect;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_unselect = readH();
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
		if (activeChar.isLockedTarget())
		{
			if (activeChar.isClanAirShipDriver())
			{
				activeChar.sendPacket(SystemMsg.THIS_ACTION_IS_PROHIBITED_WHILE_STEERING);
			}
			activeChar.sendActionFailed();
			return;
		}
		if (_unselect == 0)
		{
			if (activeChar.isCastingNow())
			{
				Skill skill = activeChar.getCastingSkill();
				activeChar.abortCast((skill != null) && (skill.isHandler() || (skill.getHitTime() > 1000)), false);
			}
			else if (activeChar.getTarget() != null)
			{
				activeChar.setTarget(null);
			}
		}
		else if (activeChar.getTarget() != null)
		{
			activeChar.setTarget(null);
		}
	}
}
