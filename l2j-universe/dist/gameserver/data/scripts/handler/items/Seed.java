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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.ChestInstance;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.mapregion.DomainArea;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Seed extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static int[] _itemIds = {};
	
	/**
	 * Constructor for Seed.
	 */
	public Seed()
	{
		_itemIds = new int[Manor.getInstance().getAllSeeds().size()];
		int id = 0;
		for (Integer s : Manor.getInstance().getAllSeeds().keySet())
		{
			_itemIds[id++] = s.shortValue();
		}
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
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		final Player player = (Player) playable;
		if (playable.getTarget() == null)
		{
			player.sendActionFailed();
			return false;
		}
		if (!player.getTarget().isMonster() || (player.getTarget() instanceof RaidBossInstance) || ((player.getTarget() instanceof MinionInstance) && (((MinionInstance) player.getTarget()).getLeader() instanceof RaidBossInstance)) || (player.getTarget() instanceof ChestInstance) || ((((MonsterInstance) playable.getTarget()).getChampion() > 0) && !item.isAltSeed()))
		{
			player.sendPacket(SystemMsg.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			return false;
		}
		final MonsterInstance target = (MonsterInstance) playable.getTarget();
		if (target == null)
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (target.isDead())
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		if (target.isSeeded())
		{
			player.sendPacket(SystemMsg.THE_SEED_HAS_BEEN_SOWN);
			return false;
		}
		final int seedId = item.getItemId();
		if ((seedId == 0) || (player.getInventory().getItemByItemId(item.getItemId()) == null))
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return false;
		}
		final DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, player);
		final int castleId = domain == null ? 0 : domain.getId();
		if (Manor.getInstance().getCastleIdForSeed(seedId) != castleId)
		{
			player.sendPacket(SystemMsg.THIS_SEED_MAY_NOT_BE_SOWN_HERE);
			return false;
		}
		final Skill skill = SkillTable.getInstance().getInfo(2097, 1);
		if (skill == null)
		{
			player.sendActionFailed();
			return false;
		}
		if (skill.checkCondition(player, target, false, false, true))
		{
			player.setUseSeed(seedId);
			player.getAI().Cast(skill, target);
		}
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}
}
