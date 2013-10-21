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
package handler.items;

import gnu.trove.set.hash.TIntHashSet;

import java.util.List;

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Spellbooks extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private int[] _itemIds = null;
	
	/**
	 * Constructor for Spellbooks.
	 */
	public Spellbooks()
	{
		final TIntHashSet list = new TIntHashSet();
		final List<SkillLearn> l = SkillAcquireHolder.getInstance().getAllNormalSkillTreeWithForgottenScrolls();
		for (SkillLearn learn : l)
		{
			list.add(learn.getItemId());
		}
		_itemIds = list.toArray();
	}
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		final Player player = (Player) playable;
		if (item.getCount() < 1)
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return false;
		}
		final List<SkillLearn> list = SkillAcquireHolder.getInstance().getSkillLearnListByItemId(player, item.getItemId());
		if (list.isEmpty())
		{
			player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		boolean alreadyHas = true;
		for (SkillLearn learn : list)
		{
			if (player.getSkillLevel(learn.getId()) != learn.getLevel())
			{
				alreadyHas = false;
				break;
			}
		}
		if (alreadyHas)
		{
			player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		boolean wrongLvl = false;
		for (SkillLearn learn : list)
		{
			if (player.getLevel() < learn.getMinLevel())
			{
				wrongLvl = true;
			}
		}
		if (wrongLvl)
		{
			player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		if (!player.consumeItem(item.getItemId(), 1L))
		{
			return false;
		}
		for (SkillLearn skillLearn : list)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillLearn.getId(), skillLearn.getLevel());
			if (skill == null)
			{
				continue;
			}
			player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skill.getId(), skill.getLevel()));
			player.addSkill(skill, true);
		}
		player.updateStats();
		player.sendPacket(new SkillList(player));
		player.broadcastPacket(new MagicSkillUse(player, player, 2790, 1, 1, 0));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
