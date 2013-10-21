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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.ShortCutInit;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.WeaponTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KamaelWeaponExchange extends Skill
{
	/**
	 * Constructor for KamaelWeaponExchange.
	 * @param set StatsSet
	 */
	public KamaelWeaponExchange(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player p = (Player) activeChar;
		if (p.isInStoreMode() || p.isProcessingRequest())
		{
			return false;
		}
		ItemInstance item = activeChar.getActiveWeaponInstance();
		if ((item != null) && (((WeaponTemplate) item.getTemplate()).getKamaelConvert() == 0))
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_CONVERT_THIS_ITEM);
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		final Player player = (Player) activeChar;
		final ItemInstance item = activeChar.getActiveWeaponInstance();
		if (item == null)
		{
			return;
		}
		int itemId = ((WeaponTemplate) item.getTemplate()).getKamaelConvert();
		if (itemId == 0)
		{
			return;
		}
		player.getInventory().unEquipItem(item);
		player.sendPacket(new InventoryUpdate().addRemovedItem(item));
		item.setItemId(itemId);
		player.sendPacket(new ShortCutInit(player));
		for (int shotId : player.getAutoSoulShot())
		{
			player.sendPacket(new ExAutoSoulShot(shotId, true));
		}
		player.sendPacket(new InventoryUpdate().addNewItem(item));
		player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EQUIPPED_YOUR_S1).addItemNameWithAugmentation(item));
		player.getInventory().equipItem(item);
	}
}
