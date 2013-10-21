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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestSkillList extends L2GameClientPacket
{
	/**
	 * Field _C__50_REQUESTSKILLLIST. (value is ""[C] 50 RequestSkillList"")
	 */
	private static final String _C__50_REQUESTSKILLLIST = "[C] 50 RequestSkillList";
	
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
		Player cha = getClient().getActiveChar();
		if (cha != null)
		{
			cha.sendSkillList();
		}
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	@Override
	public String getType()
	{
		return _C__50_REQUESTSKILLLIST;
	}
}
