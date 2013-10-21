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
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExGMViewQuestItemList;
import lineage2.gameserver.network.serverpackets.GMHennaInfo;
import lineage2.gameserver.network.serverpackets.GMViewCharacterInfo;
import lineage2.gameserver.network.serverpackets.GMViewItemList;
import lineage2.gameserver.network.serverpackets.GMViewPledgeInfo;
import lineage2.gameserver.network.serverpackets.GMViewQuestInfo;
import lineage2.gameserver.network.serverpackets.GMViewSkillInfo;
import lineage2.gameserver.network.serverpackets.GMViewWarehouseWithdrawList;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestGMCommand extends L2GameClientPacket
{
	/**
	 * Field _targetName.
	 */
	private String _targetName;
	/**
	 * Field _command.
	 */
	private int _command;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetName = readS();
		_command = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		Player target = World.getPlayer(_targetName);
		if ((player == null) || (target == null))
		{
			return;
		}
		if (!player.getPlayerAccess().CanViewChar)
		{
			return;
		}
		switch (_command)
		{
			case 1:
				player.sendPacket(new GMViewCharacterInfo(target));
				player.sendPacket(new GMHennaInfo(target));
				break;
			case 2:
				if (target.getClan() != null)
				{
					player.sendPacket(new GMViewPledgeInfo(target));
				}
				break;
			case 3:
				player.sendPacket(new GMViewSkillInfo(target));
				break;
			case 4:
				player.sendPacket(new GMViewQuestInfo(target));
				break;
			case 5:
				ItemInstance[] items = target.getInventory().getItems();
				int questSize = 0;
				for (ItemInstance item : items)
				{
					if (item.getTemplate().isQuest())
					{
						questSize++;
					}
				}
				player.sendPacket(new GMViewItemList(target, items, items.length - questSize));
				player.sendPacket(new ExGMViewQuestItemList(target, items, questSize));
				player.sendPacket(new GMHennaInfo(target));
				break;
			case 6:
				player.sendPacket(new GMViewWarehouseWithdrawList(target));
				break;
		}
	}
}
