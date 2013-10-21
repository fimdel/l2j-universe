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

import lineage2.commons.text.PrintfFormat;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PurpleManedHorse extends Functions implements ScriptFile
{
	/**
	 * Field Enabled.
	 */
	private static boolean Enabled = false;
	/**
	 * Field MultiSellID. (value is -1001)
	 */
	private static final int MultiSellID = -1001;
	/**
	 * Field dlg.
	 */
	private static final PrintfFormat dlg = new PrintfFormat("<br>[npc_%%objectId%%_Multisell %d|%s]");
	/**
	 * Field list.
	 */
	private static MultiSellListContainer list;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.SERVICES_SELLPETS.isEmpty())
		{
			return;
		}
		String[] SELLPETS = Config.SERVICES_SELLPETS.split(";");
		if (SELLPETS.length == 0)
		{
			return;
		}
		list = new MultiSellListContainer();
		list.setNoTax(true);
		list.setShowAll(true);
		list.setKeepEnchant(false);
		list.setNoKey(true);
		int entId = 1;
		for (String SELLPET : SELLPETS)
		{
			MultiSellEntry e = MultiSellHolder.parseEntryFromStr(SELLPET);
			if (e != null)
			{
				e.setEntryId(entId++);
				list.addEntry(e);
			}
		}
		if (list.getEntries().size() == 0)
		{
			return;
		}
		Enabled = true;
		loadMultiSell();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method loadMultiSell.
	 */
	private static void loadMultiSell()
	{
		MultiSellHolder.getInstance().addMultiSellListContainer(MultiSellID, list);
	}
	
	/**
	 * Method PetManagersDialogAppend.
	 * @param val Integer
	 * @return String
	 */
	public String PetManagersDialogAppend(Integer val)
	{
		if ((val == 0) && Enabled)
		{
			return dlg.sprintf(new Object[]
			{
				MultiSellID,
				"Buy New Pets"
			});
		}
		return "";
	}
	
	/**
	 * Method DialogAppend_30731.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30731(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30827.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30827(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30828.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30828(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30829.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30829(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30830.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30830(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30831.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30831(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_30869.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30869(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_31067.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31067(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_31265.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31265(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_31309.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31309(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	/**
	 * Method DialogAppend_31954.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31954(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
}
