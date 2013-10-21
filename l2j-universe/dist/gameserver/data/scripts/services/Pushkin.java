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
package services;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Pushkin extends Functions
{
	/**
	 * Method DialogAppend_30300.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30300(Integer val)
	{
		if ((val != 0) || (!Config.ALT_SIMPLE_SIGNS && !Config.ALT_BS_CRYSTALLIZE))
		{
			return "";
		}
		StringBuilder append = new StringBuilder();
		if (Config.ALT_SIMPLE_SIGNS)
		{
			append.append("<br><center>Seven Signs options:</center><br>");
			append.append("<center>[npc_%objectId%_Multisell 3112605|Manufacture an S-grade sword]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112606|Bestow the special S-grade weapon some abilities]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112607|Release the S-grade armor seal]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112608|Release the S-grade accessory seal]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112609|Manufacture an A-grade sword]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112610|Bestow the special A-grade weapon some abilities]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112611|Release the A-grade armor seal]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112612|Release the A-grade accessory seal]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112613|Seal the A-grade armor again]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112601|Remove the special abilities from a weapon]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112602|Upgrade weapon]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112603|Make an even exchange of weapons]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3112604|Complete a Foundation Item]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 3111301|Buy Something]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 400|Exchange Seal Stones]</center><br1>");
			append.append("<center>[npc_%objectId%_Multisell 500|Purchase consumable items]</center><br1>");
		}
		if (Config.ALT_BS_CRYSTALLIZE)
		{
			append.append("<br1>[scripts_services.Pushkin:doCrystallize|Crystallize]");
		}
		return append.toString();
	}
	
	/**
	 * Method DialogAppend_30086.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30086(Integer val)
	{
		return DialogAppend_30300(val);
	}
	
	/**
	 * Method DialogAppend_30098.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30098(Integer val)
	{
		if ((val != 0) || !Config.ALT_ALLOW_TATTOO)
		{
			return "";
		}
		return "<br>[npc_%objectId%_Multisell 6500|Buy tattoo]";
	}
	
	/**
	 * Method doCrystallize.
	 */
	public void doCrystallize()
	{
		Player player = getSelf();
		NpcInstance merchant = player.getLastNpc();
		Castle castle = merchant != null ? merchant.getCastle(player) : null;
		MultiSellListContainer list = new MultiSellListContainer();
		list.setShowAll(false);
		list.setKeepEnchant(true);
		list.setNoTax(false);
		int entry = 0;
		final Inventory inv = player.getInventory();
		for (final ItemInstance itm : inv.getItems())
		{
			if (itm.canBeCrystallized(player))
			{
				final ItemTemplate crystal = ItemHolder.getInstance().getTemplate(itm.getTemplate().getCrystalType().cry);
				MultiSellEntry possibleEntry = new MultiSellEntry(++entry, crystal.getItemId(), itm.getTemplate().getCrystalCount(), 0);
				possibleEntry.addIngredient(new MultiSellIngredient(itm.getItemId(), 1, itm.getEnchantLevel()));
				possibleEntry.addIngredient(new MultiSellIngredient(ItemTemplate.ITEM_ID_ADENA, Math.round(itm.getTemplate().getCrystalCount() * crystal.getReferencePrice() * 0.05), 0));
				list.addEntry(possibleEntry);
			}
		}
		MultiSellHolder.getInstance().SeparateAndSend(list, player, castle == null ? 0. : castle.getTaxRate());
	}
}
