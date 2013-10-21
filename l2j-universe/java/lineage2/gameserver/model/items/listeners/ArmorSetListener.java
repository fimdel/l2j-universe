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
package lineage2.gameserver.model.items.listeners;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ArmorSetsHolder;
import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.ArmorSet;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ArmorSetListener implements OnEquipListener
{
	/**
	 * Field _instance.
	 */
	private static final ArmorSetListener _instance = new ArmorSetListener();
	
	/**
	 * Method getInstance.
	 * @return ArmorSetListener
	 */
	public static ArmorSetListener getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method onEquip.
	 * @param slot int
	 * @param item ItemInstance
	 * @param actor Playable
	 * @see lineage2.gameserver.listener.inventory.OnEquipListener#onEquip(int, ItemInstance, Playable)
	 */
	@Override
	public void onEquip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
		{
			return;
		}
		Player player = (Player) actor;
		ArmorSet armorSet = ArmorSetsHolder.getInstance().getArmorSet(item.getItemId());
		if (armorSet == null)
		{
			return;
		}
		boolean update = false;
		if (armorSet.containItem(slot, item.getItemId()))
		{
			List<Skill> skills = armorSet.getSkills(armorSet.getEquipedSetPartsCount(player));
			for (Skill skill : skills)
			{
				player.addSkill(skill, false);
				update = true;
			}
			if (armorSet.containAll(player))
			{
				if (armorSet.containShield(player))
				{
					skills = armorSet.getShieldSkills();
					for (Skill skill : skills)
					{
						player.addSkill(skill, false);
						update = true;
					}
				}
				if (armorSet.isEnchanted6(player))
				{
					skills = armorSet.getEnchant6skills();
					for (Skill skill : skills)
					{
						player.addSkill(skill, false);
						update = true;
					}
				}
			}
		}
		else if (armorSet.containShield(item.getItemId()) && armorSet.containAll(player))
		{
			List<Skill> skills = armorSet.getShieldSkills();
			for (Skill skill : skills)
			{
				player.addSkill(skill, false);
				update = true;
			}
		}
		if (update)
		{
			player.sendPacket(new SkillList(player));
			player.updateStats();
		}
	}
	
	/**
	 * Method onUnequip.
	 * @param slot int
	 * @param item ItemInstance
	 * @param actor Playable
	 * @see lineage2.gameserver.listener.inventory.OnEquipListener#onUnequip(int, ItemInstance, Playable)
	 */
	@Override
	public void onUnequip(int slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
		{
			return;
		}
		Player player = (Player) actor;
		boolean remove = false;
		List<Skill> removeSkillId1 = new ArrayList<>();
		List<Skill> removeSkillId2 = new ArrayList<>();
		List<Skill> removeSkillId3 = new ArrayList<>();
		ArmorSet armorSet = ArmorSetsHolder.getInstance().getArmorSet(item.getItemId());
		if (armorSet == null)
		{
			return;
		}
		if (armorSet.containItem(slot, item.getItemId()))
		{
			remove = true;
			removeSkillId1 = armorSet.getSkillsToRemove();
			removeSkillId2 = armorSet.getShieldSkills();
			removeSkillId3 = armorSet.getEnchant6skills();
		}
		else if (armorSet.containShield(item.getItemId()))
		{
			remove = true;
			removeSkillId2 = armorSet.getShieldSkills();
		}
		boolean update = false;
		if (remove)
		{
			for (Skill skill : removeSkillId1)
			{
				player.removeSkill(skill, false);
				update = true;
			}
			for (Skill skill : removeSkillId2)
			{
				player.removeSkill(skill);
				update = true;
			}
			for (Skill skill : removeSkillId3)
			{
				player.removeSkill(skill);
				update = true;
			}
		}
		List<Skill> skills = armorSet.getSkills(armorSet.getEquipedSetPartsCount(player));
		for (Skill skill : skills)
		{
			player.addSkill(skill, false);
			update = true;
		}
		if (update)
		{
			if (!player.getInventory().isRefresh)
			{
				if (!player.getOpenCloak() && (player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_BACK, null) != null))
				{
					player.sendPacket(SystemMsg.YOUR_CLOAK_HAS_BEEN_UNEQUIPPED_BECAUSE_YOUR_ARMOR_SET_IS_NO_LONGER_COMPLETE);
				}
			}
			player.sendPacket(new SkillList(player));
			player.updateStats();
		}
	}
}
