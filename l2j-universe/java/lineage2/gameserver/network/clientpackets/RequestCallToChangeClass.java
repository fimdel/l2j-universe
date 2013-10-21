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
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.network.serverpackets.ExCallToChangeClass;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestCallToChangeClass extends L2GameClientPacket
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
		if (!Config.AWAKING_FREE)
		{
			Player player = getClient().getActiveChar();
			if (player == null)
			{
				return;
			}
			if (player.getVarB("GermunkusUSM"))
			{
				return;
			}
			int _cId = 0;
			for (ClassId Cl : ClassId.VALUES)
			{
				if ((Cl.level() == 5) && player.getClassId().childOf(Cl))
				{
					_cId = Cl.getId();
					break;
				}
			}
			if (player.isDead())
			{
				sendPacket(new ExShowScreenMessage(NpcString.YOU_CANNOT_TELEPORT_WHILE_YOU_ARE_DEAD, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, ExShowScreenMessage.STRING_TYPE, -1, false), new ExCallToChangeClass(_cId, false));
				return;
			}
			player.processQuestEvent("_10338_SeizeYourDestiny", "MemoryOfDisaster", null);
		}
		else
		{
			Player player = getClient().getActiveChar();
			if (player == null)
			{
				return;
			}
			if (player.getLevel() < 85)
			{
				return;
			}
			if (player.getClassId().level() < 3)
			{
				return;
			}
			AwakingManager.getInstance().SendReqToAwaking(player);
		}
	}
}
