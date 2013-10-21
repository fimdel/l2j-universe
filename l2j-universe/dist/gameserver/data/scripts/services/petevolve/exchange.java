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
package services.petevolve;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.PetDataTable.L2Pet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class exchange extends Functions
{
	/**
	 * Field PEticketB. (value is 7583)
	 */
	private static final int PEticketB = 7583;
	/**
	 * Field PEticketC. (value is 7584)
	 */
	private static final int PEticketC = 7584;
	/**
	 * Field PEticketK. (value is 7585)
	 */
	private static final int PEticketK = 7585;
	/**
	 * Field BbuffaloP. (value is 6648)
	 */
	private static final int BbuffaloP = 6648;
	/**
	 * Field BcougarC. (value is 6649)
	 */
	private static final int BcougarC = 6649;
	/**
	 * Field BkookaburraO. (value is 6650)
	 */
	private static final int BkookaburraO = 6650;
	
	/**
	 * Method exch_1.
	 */
	public void exch_1()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (getItemCount(player, PEticketB) >= 1)
		{
			removeItem(player, PEticketB, 1);
			addItem(player, BbuffaloP, 1);
			return;
		}
		show("scripts/services/petevolve/exchange_no.htm", player);
	}
	
	/**
	 * Method exch_2.
	 */
	public void exch_2()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (getItemCount(player, PEticketC) >= 1)
		{
			removeItem(player, PEticketC, 1);
			addItem(player, BcougarC, 1);
			return;
		}
		show("scripts/services/petevolve/exchange_no.htm", player);
	}
	
	/**
	 * Method exch_3.
	 */
	public void exch_3()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (getItemCount(player, PEticketK) >= 1)
		{
			removeItem(player, PEticketK, 1);
			addItem(player, BkookaburraO, 1);
			return;
		}
		show("scripts/services/petevolve/exchange_no.htm", player);
	}
	
	/**
	 * Method showBabyPetExchange.
	 */
	public void showBabyPetExchange()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXCHANGE_BABY_PET_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.SERVICES_EXCHANGE_BABY_PET_ITEM);
		String out = "";
		out += "<html><body>Вы можете в л�?бое врем�? обмен�?т�? ва�?его Improved Baby пета на другой вид, без потери опыта. �?ет при �?том должен быт�? вызван.";
		out += "<br>Стоимо�?т�? обмена: " + Util.formatAdena(Config.SERVICES_EXCHANGE_BABY_PET_PRICE) + " " + item.getName();
		out += "<br><button width=250 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h scripts_services.petevolve.exchange:exToCougar\" value=\"�?бмен�?т�? на Improved Cougar\">";
		out += "<br1><button width=250 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h scripts_services.petevolve.exchange:exToBuffalo\" value=\"�?бмен�?т�? на Improved Buffalo\">";
		out += "<br1><button width=250 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h scripts_services.petevolve.exchange:exToKookaburra\" value=\"�?бмен�?т�? на Improved Kookaburra\">";
		out += "</body></html>";
		show(out, player);
	}
	
	/**
	 * Method showErasePetName.
	 */
	public void showErasePetName()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_CHANGE_PET_NAME_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_PET_NAME_ITEM);
		String out = "";
		out += "<html><body>Вы можете обнулит�? им�? у пета, дл�? того чтобы назначит�? новое. �?ет при �?том должен быт�? вызван.";
		out += "<br>Стоимо�?т�? обнулени�?: " + Util.formatAdena(Config.SERVICES_CHANGE_PET_NAME_PRICE) + " " + item.getName();
		out += "<br><button width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h scripts_services.petevolve.exchange:erasePetName\" value=\"�?бнулит�? им�?\">";
		out += "</body></html>";
		show(out, player);
	}
	
	/**
	 * Method erasePetName.
	 */
	public void erasePetName()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_CHANGE_PET_NAME_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		Summon pl_pet = player.getSummonList().getPet();
		if ((pl_pet == null) || !pl_pet.isPet())
		{
			show("�?итомец должен быт�? вызван.", player);
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_CHANGE_PET_NAME_ITEM, Config.SERVICES_CHANGE_PET_NAME_PRICE))
		{
			pl_pet.setName(pl_pet.getTemplate().name);
			pl_pet.broadcastCharInfo();
			PetInstance _pet = (PetInstance) pl_pet;
			ItemInstance control = _pet.getControlItem();
			if (control != null)
			{
				control.setCustomType2(1);
				control.setJdbcState(JdbcEntityState.UPDATED);
				control.update();
				player.sendPacket(new InventoryUpdate().addModifiedItem(control));
			}
			show("�?м�? �?терто.", player);
		}
		else if (Config.SERVICES_CHANGE_PET_NAME_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	/**
	 * Method exToCougar.
	 */
	public void exToCougar()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXCHANGE_BABY_PET_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		Summon pl_pet = player.getSummonList().getPet();
		if ((pl_pet == null) || pl_pet.isDead() || !((pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_BUFFALO_ID) || (pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_KOOKABURRA_ID)))
		{
			show("�?ет должен быт�? вызван.", player);
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_EXCHANGE_BABY_PET_ITEM, Config.SERVICES_EXCHANGE_BABY_PET_PRICE))
		{
			ItemInstance control = player.getInventory().getItemByObjectId(pl_pet.getControlItemObjId());
			control.setItemId(L2Pet.IMPROVED_BABY_COUGAR.getControlItemId());
			control.setJdbcState(JdbcEntityState.UPDATED);
			control.update();
			player.sendPacket(new InventoryUpdate().addModifiedItem(control));
			player.getSummonList().unsummonPet(false);
			show("�?ет изменен.", player);
		}
		else if (Config.SERVICES_EXCHANGE_BABY_PET_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	/**
	 * Method exToBuffalo.
	 */
	public void exToBuffalo()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXCHANGE_BABY_PET_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		Summon pl_pet = player.getSummonList().getPet();
		if ((pl_pet == null) || pl_pet.isDead() || !((pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_COUGAR_ID) || (pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_KOOKABURRA_ID)))
		{
			show("�?ет должен быт�? вызван.", player);
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && player.isMageClass())
		{
			show("Этот пет тол�?ко дл�? воинов.", player);
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_EXCHANGE_BABY_PET_ITEM, Config.SERVICES_EXCHANGE_BABY_PET_PRICE))
		{
			ItemInstance control = player.getInventory().getItemByObjectId(pl_pet.getControlItemObjId());
			control.setItemId(L2Pet.IMPROVED_BABY_BUFFALO.getControlItemId());
			control/**
			 * Method exToKookaburra.
			 */
			.setJdbcState(JdbcEntityState.UPDATED);
			control.update();
			player.sendPacket(new InventoryUpdate().addModifiedItem(control));
			player.getSummonList().unsummonPet(false);
			show("�?ет изменен.", player);
		}
		else if (Config.SERVICES_EXCHANGE_BABY_PET_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	public void exToKookaburra()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXCHANGE_BABY_PET_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		Summon pl_pet = player.getSummonList().getPet();
		if ((pl_pet == null) || pl_pet.isDead() || !((pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_BUFFALO_ID) || (pl_pet.getNpcId() == PetDataTable.IMPROVED_BABY_COUGAR_ID)))
		{
			show("�?ет должен быт�? вызван.", player);
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && !player.isMageClass())
		{
			show("Этот пет тол�?ко дл�? магов.", player);
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_EXCHANGE_BABY_PET_ITEM, Config.SERVICES_EXCHANGE_BABY_PET_PRICE))
		{
			ItemInstance control = player.getInventory().getItemByObjectId(pl_pet.getControlItemObjId());
			control.setItemId(L2Pet.IMPROVED_BABY_KOOKABURRA.getControlItemId());
			control.setJdbcState(JdbcEntityState.UPDATED);
			control.update();
			player.sendPacket(new InventoryUpdate().addModifiedItem(control));
			player.getSummonList().unsummonPet(false);
			show("�?ет изменен.", player);
		}
		else if (Config.SERVICES_EXCHANGE_BABY_PET_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	/**
	 * Method DialogAppend_30731.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30731(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30827.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30827(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30828.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30828(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30829.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30829(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30830.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30830(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30831.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30831(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30869.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30869(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31067.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_31067(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31265.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_31265(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31309.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_31309(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31954.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_31954(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method getHtmlAppends.
	 * @param val Integer
	 * @return String
	 */
	private static String getHtmlAppends(Integer val)
	{
		String ret = "";
		if (val != 0)
		{
			return ret;
		}
		if (Config.SERVICES_CHANGE_PET_NAME_ENABLED)
		{
			ret = "<br>[scripts_services.petevolve.exchange:showErasePetName|�?бнулит�? им�? у пета]";
		}
		if (Config.SERVICES_EXCHANGE_BABY_PET_ENABLED)
		{
			ret += "<br>[scripts_services.petevolve.exchange:showBabyPetExchange|�?бмен�?т�? Improved Baby пета]";
		}
		return ret;
	}
}
