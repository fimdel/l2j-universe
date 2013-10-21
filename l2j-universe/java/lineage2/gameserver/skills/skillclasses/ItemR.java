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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemR extends Skill
{
	/**
	 * Field _item_r1.
	 */
	private final int _item_r1;
	/**
	 * Field _item_r2.
	 */
	private final int _item_r2;
	/**
	 * Field _item_r3.
	 */
	private final int _item_r3;
	/**
	 * Field _item_del.
	 */
	private final int _item_del;
	
	/**
	 * Constructor for ItemR.
	 * @param set StatsSet
	 */
	public ItemR(StatsSet set)
	{
		super(set);
		_item_r1 = set.getInteger("item_r1", 0);
		_item_r2 = set.getInteger("item_r2", 0);
		_item_r3 = set.getInteger("item_r3", 0);
		_item_del = set.getInteger("item_del", 0);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		Player player = (Player) activeChar;
		if (player != null)
		{
			if ((_item_r1 != 0) && (_item_r2 != 0) && (_item_r3 != 0))
			{
				player.getInventory().destroyItemByItemId(_item_del, 1);
				player.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addItemName(_item_del));
				if (Rnd.chance(90))
				{
					player.getInventory().addItem(_item_r1, 1);
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r1));
				}
				else if (Rnd.chance(2))
				{
					player.getInventory().addItem(_item_r3, 1);
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r3));
				}
				else
				{
					player.getInventory().addItem(_item_r2, 1);
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r2));
				}
			}
			else if ((_item_r1 != 0) && (_item_r2 != 0))
			{
				player.getInventory().destroyItemByItemId(_item_del, 1);
				player.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addItemName(_item_del));
				if (Rnd.chance(90))
				{
					player.getInventory().addItem(_item_r1, 1);
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r1));
				}
				else
				{
					player.getInventory().addItem(_item_r2, 1);
					player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r2));
				}
			}
			else
			{
				player.sendMessage("Данный итем не реализован, ожидайте.");
			}
		}
	}
}
