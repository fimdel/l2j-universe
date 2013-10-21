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

import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SkillCoolTime;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemSkillsListener implements OnEquipListener
{
	/**
	 * Field _instance.
	 */
	private static final ItemSkillsListener _instance = new ItemSkillsListener();
	
	/**
	 * Method getInstance.
	 * @return ItemSkillsListener
	 */
	public static ItemSkillsListener getInstance()
	{
		return _instance;
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
		Player player = (Player) actor;
		Skill[] itemSkills = null;
		Skill enchant4Skill = null;
		Skill unequipeSkill = null;
		ItemTemplate it = item.getTemplate();
		itemSkills = it.getAttachedSkills();
		enchant4Skill = it.getEnchant4Skill();
		unequipeSkill = it.getUnequipeSkill();
		player.removeTriggers(it);
		if ((itemSkills != null) && (itemSkills.length > 0))
		{
			for (Skill itemSkill : itemSkills)
			{
				if ((itemSkill.getId() >= 26046) && (itemSkill.getId() <= 26048))
				{
					int level = player.getSkillLevel(itemSkill.getId());
					int newlevel = level - 1;
					if (newlevel > 0)
					{
						player.addSkill(SkillTable.getInstance().getInfo(itemSkill.getId(), newlevel), false);
					}
					else
					{
						player.removeSkillById(itemSkill.getId());
					}
				}
				else
				{
					player.removeSkill(itemSkill, false);
				}
			}
		}
		if (enchant4Skill != null)
		{
			player.removeSkill(enchant4Skill, false);
		}
		if (unequipeSkill != null)
		{
			player.doCast(unequipeSkill, player, true);
		}
		if ((itemSkills.length > 0) || (enchant4Skill != null))
		{
			player.sendSkillList();
			player.updateStats();
		}
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
		Player player = (Player) actor;
		Skill[] itemSkills = null;
		Skill enchant4Skill = null;
		ItemTemplate it = item.getTemplate();
		itemSkills = it.getAttachedSkills();
		if (item.getEnchantLevel() >= 4)
		{
			enchant4Skill = it.getEnchant4Skill();
		}
		if ((it.getType2() == ItemTemplate.TYPE2_WEAPON) && (player.getWeaponsExpertisePenalty() > 0))
		{
			return;
		}
		player.addTriggers(it);
		boolean needSendInfo = false;
		if (itemSkills.length > 0)
		{
			for (Skill itemSkill : itemSkills)
			{
				if ((itemSkill.getId() >= 26046) && (itemSkill.getId() <= 26048))
				{
					int level = player.getSkillLevel(itemSkill.getId());
					int newlevel = level;
					if (level > 0)
					{
						if (SkillTable.getInstance().getInfo(itemSkill.getId(), level + 1) != null)
						{
							newlevel = level + 1;
						}
					}
					else
					{
						newlevel = 1;
					}
					if (newlevel != level)
					{
						player.addSkill(SkillTable.getInstance().getInfo(itemSkill.getId(), newlevel), false);
					}
				}
				else if (player.getSkillLevel(itemSkill.getId()) < itemSkill.getLevel())
				{
					player.addSkill(itemSkill, false);
					if (itemSkill.isActive())
					{
						long reuseDelay = Formulas.calcSkillReuseDelay(player, itemSkill);
						reuseDelay = Math.min(reuseDelay, 30000);
						if ((reuseDelay > 0) && !player.isSkillDisabled(itemSkill))
						{
							player.disableSkill(itemSkill, reuseDelay);
							needSendInfo = true;
						}
					}
				}
			}
		}
		if (enchant4Skill != null)
		{
			player.addSkill(enchant4Skill, false);
		}
		if ((itemSkills.length > 0) || (enchant4Skill != null))
		{
			player.sendSkillList();
			player.updateStats();
			if (needSendInfo)
			{
				player.sendPacket(new SkillCoolTime(player));
			}
		}
	}
}
