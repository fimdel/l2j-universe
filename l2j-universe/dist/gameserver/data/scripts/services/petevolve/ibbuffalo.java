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
public class ibbuffalo extends Functions
{
	/**
	 * Field BABY_BUFFALO.
	 */
	private static final int BABY_BUFFALO = PetDataTable.BABY_BUFFALO_ID;
	/**
	 * Field BABY_BUFFALO_PANPIPE.
	 */
	private static final int BABY_BUFFALO_PANPIPE = L2Pet.BABY_BUFFALO.getControlItemId();
	/**
	 * Field IN_BABY_BUFFALO_NECKLACE.
	 */
	private static final int IN_BABY_BUFFALO_NECKLACE = L2Pet.IMPROVED_BABY_BUFFALO.getControlItemId();
	
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
		if (player.getInventory().getItemByItemId(BABY_BUFFALO_PANPIPE) == null)
		{
			show("scripts/services/petevolve/no_item.htm", player, npc);
			return;
		}
		if ((pl_pet == null) || pl_pet.isDead())
		{
			show("scripts/services/petevolve/evolve_no.htm", player, npc);
			return;
		}
		if (pl_pet.getNpcId() != BABY_BUFFALO)
		{
			show("scripts/services/petevolve/no_pet.htm", player, npc);
			return;
		}
		if (Config.ALT_IMPROVED_PETS_LIMITED_USE && player.isMageClass())
		{
			show("scripts/services/petevolve/no_class_w.htm", player, npc);
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
		control.setItemId(IN_BABY_BUFFALO_NECKLACE);
		control.setEnchantLevel(L2Pet.IMPROVED_BABY_BUFFALO.getMinLevel());
		control.setJdbcState(JdbcEntityState.UPDATED);
		control.update();
		player.sendItemList(false);
		show("scripts/services/petevolve/yes_pet.htm", player, npc);
	}
}
