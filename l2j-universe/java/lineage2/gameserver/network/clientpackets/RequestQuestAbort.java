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

import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestQuestAbort extends L2GameClientPacket
{
	/**
	 * Field _questID.
	 */
	private int _questID;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_questID = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		Quest quest = QuestManager.getQuest(_questID);
		if ((activeChar == null) || (quest == null))
		{
			return;
		}
		if (!quest.canAbortByPacket())
		{
			return;
		}
		QuestState qs = activeChar.getQuestState(quest.getClass());
		if ((qs != null) && !qs.isCompleted())
		{
			qs.abortQuest();
		}
	}
}
