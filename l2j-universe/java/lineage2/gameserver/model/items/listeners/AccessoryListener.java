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
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AccessoryListener implements OnEquipListener
{
	/**
	 * Field _instance.
	 */
	private static final AccessoryListener _instance = new AccessoryListener();
	
	/**
	 * Method getInstance.
	 * @return AccessoryListener
	 */
	public static AccessoryListener getInstance()
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
		Player player = (Player) actor;
		if ((item.getBodyPart() == ItemTemplate.SLOT_L_BRACELET) && (item.getTemplate().getAttachedSkills().length > 0))
		{
			int agathionId = player.getAgathionId();
			int transformNpcId = player.getTransformationTemplate();
			for (Skill skill : item.getTemplate().getAttachedSkills())
			{
				if ((agathionId > 0) && (skill.getNpcId() == agathionId))
				{
					player.setAgathion(0);
				}
				if ((skill.getNpcId() == transformNpcId) && (skill.getSkillType() == SkillType.TRANSFORMATION))
				{
					player.setTransformation(0);
				}
			}
		}
		if (item.isAccessory() || item.getTemplate().isTalisman() || item.getTemplate().isBracelet())
		{
			player.sendUserInfo();
		}
		else
		{
			player.broadcastCharInfo();
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
		if (!item.isEquipable())
		{
			return;
		}
		Player player = (Player) actor;
		if (item.isAccessory() || item.getTemplate().isTalisman() || item.getTemplate().isBracelet())
		{
			player.sendUserInfo();
		}
		else
		{
			player.broadcastCharInfo();
		}
	}
}
