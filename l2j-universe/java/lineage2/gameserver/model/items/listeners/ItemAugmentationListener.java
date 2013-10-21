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

import lineage2.gameserver.data.xml.holder.OptionDataHolder;
import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SkillCoolTime;
import lineage2.gameserver.templates.OptionDataTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemAugmentationListener implements OnEquipListener
{
	/**
	 * Field _instance.
	 */
	private static final ItemAugmentationListener _instance = new ItemAugmentationListener();
	
	/**
	 * Method getInstance.
	 * @return ItemAugmentationListener
	 */
	public static ItemAugmentationListener getInstance()
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
		if (!item.isEquipable())
		{
			return;
		}
		if (!item.isAugmented())
		{
			return;
		}
		Player player = actor.getPlayer();
		int stats[] = new int[2];
		stats[0] = 0x0000FFFF & item.getAugmentationId();
		stats[1] = item.getAugmentationId() >> 16;
		boolean sendList = false;
		for (int i : stats)
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
			{
				continue;
			}
			player.removeStatsOwner(template);
			for (Skill skill : template.getSkills())
			{
				sendList = true;
				player.removeSkill(skill);
			}
			player.removeTriggers(template);
		}
		if (sendList)
		{
			player.sendSkillList();
		}
		player.updateStats();
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
		if (!item.isAugmented())
		{
			return;
		}
		Player player = actor.getPlayer();
		if (player.getExpertisePenalty(item) > 0)
		{
			return;
		}
		int stats[] = new int[2];
		stats[0] = 0x0000FFFF & item.getAugmentationId();
		stats[1] = item.getAugmentationId() >> 16;
		boolean sendList = false;
		boolean sendReuseList = false;
		for (int i : stats)
		{
			OptionDataTemplate template = OptionDataHolder.getInstance().getTemplate(i);
			if (template == null)
			{
				continue;
			}
			player.addStatFuncs(template.getStatFuncs(template));
			for (Skill skill : template.getSkills())
			{
				sendList = true;
				player.addSkill(skill);
				if (player.isSkillDisabled(skill))
				{
					sendReuseList = true;
				}
			}
			player.addTriggers(template);
		}
		if (sendList)
		{
			player.sendSkillList();
		}
		if (sendReuseList)
		{
			player.sendPacket(new SkillCoolTime(player));
		}
		player.updateStats();
	}
}
