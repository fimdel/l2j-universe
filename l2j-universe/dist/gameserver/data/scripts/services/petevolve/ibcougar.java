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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.PetDataTable.L2Pet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ibcougar extends Functions
{
	/**
	 * Field BABY_COUGAR.
	 */
	private static final int BABY_COUGAR = PetDataTable.BABY_COUGAR_ID;
	/**
	 * Field BABY_COUGAR_CHIME.
	 */
	private static final int BABY_COUGAR_CHIME = L2Pet.BABY_COUGAR.getControlItemId();
	/**
	 * Field IN_COUGAR_CHIME.
	 */
	private static final int IN_COUGAR_CHIME = L2Pet.IMPROVED_BABY_COUGAR.getControlItemId();
	
	/**
	 * Method evolve.
	 */
	public void evolve()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		Summon pl_pet = player.getSummonList().getPet();
		if (player.getInventory().getItemByItemId(BABY_COUGAR_CHIME) == null)
		{
			show("scripts/services/petevolve/no_item.htm", player, npc);
			return;
		}
		if ((pl_pet == null) || pl_pet.isDead())
		{
			show("scripts/services/petevolve/evolve_no.htm", player, npc);
			return;
		}
		if (pl_pet.getNpcId() != BABY_COUGAR)
		{
			show("scripts/services/petevolve/no_pet.htm", player, npc);
			return;
		}
		if (pl_pet.getLevel() < 55)
		{
			show("scripts/services/petevolve/no_level.htm", player, npc);
			return;
		}
		int controlItemId = pl_pet.getControlItemObjId();
		player.getSummonList().unsummonPet(false);
		ItemInstance control = player.getInventory().getItemByObjectId(controlItemId);
		control.setItemId(IN_COUGAR_CHIME);
		control.setEnchantLevel(L2Pet.IMPROVED_BABY_COUGAR.getMinLevel());
		control.setJdbcState(JdbcEntityState.UPDATED);
		control.update();
		player.sendItemList(false);
		show("scripts/services/petevolve/yes_pet.htm", player, npc);
	}
}
