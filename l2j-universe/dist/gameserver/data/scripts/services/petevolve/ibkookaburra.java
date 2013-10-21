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
public class ibkookaburra extends Functions
{
	/**
	 * Field BABY_KOOKABURRA.
	 */
	private static final int BABY_KOOKABURRA = PetDataTable.BABY_KOOKABURRA_ID;
	/**
	 * Field BABY_KOOKABURRA_OCARINA.
	 */
	private static final int BABY_KOOKABURRA_OCARINA = L2Pet.BABY_KOOKABURRA.getControlItemId();
	/**
	 * Field IN_KOOKABURRA_OCARINA.
	 */
	private static final int IN_KOOKABURRA_OCARINA = L2Pet.IMPROVED_BABY_KOOKABURRA.getControlItemId();
	
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
		Summon pet = player.getSummonList().getPet();
		if (player.getInventory().getItemByItemId(BABY_KOOKABURRA_OCARINA) == null)
		{
			show("scripts/services/petevolve/no_item.htm", player, npc);
			return;
		}
		else if ((pet == null) || pet.isDead())
		{
			show("scripts/services/petevolve/evolve_no.htm", player, npc);
			return;
		}
		if (pet.getNpcId() != BABY_KOOKABURRA)
		{
			show("scripts/services/petevolve/no_pet.htm", player, npc);
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && !player.isMageClass())
		{
			show("scripts/services/petevolve/no_class_m.htm", player, npc);
			return;
		}
		if (pet.getLevel() < 55)
		{
			show("scripts/services/petevolve/no_level.htm", player, npc);
			return;
		}
		int controlItemId = pet.getControlItemObjId();
		player.getSummonList().unsummonPet(false);
		ItemInstance control = player.getInventory().getItemByObjectId(controlItemId);
		control.setItemId(IN_KOOKABURRA_OCARINA);
		control.setEnchantLevel(L2Pet.IMPROVED_BABY_KOOKABURRA.getMinLevel());
		control.setJdbcState(JdbcEntityState.UPDATED);
		control.update();
		player.sendItemList(false);
		show("scripts/services/petevolve/yes_pet.htm", player, npc);
	}
}
