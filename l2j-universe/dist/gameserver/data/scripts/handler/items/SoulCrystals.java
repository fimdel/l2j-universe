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
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.SoulCrystalHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SetupGauge;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.SoulCrystal;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SoulCrystals extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private final int[] _itemIds;
	
	/**
	 * Constructor for SoulCrystals.
	 */
	public SoulCrystals()
	{
		final TIntHashSet set = new TIntHashSet();
		for (SoulCrystal crystal : SoulCrystalHolder.getInstance().getCrystals())
		{
			set.add(crystal.getItemId());
			set.add(crystal.getNextItemId());
		}
		_itemIds = set.toArray();
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
		final Player player = playable.getPlayer();
		if ((player.getTarget() == null) || !player.getTarget().isMonster())
		{
			player.sendPacket(Msg.INVALID_TARGET, ActionFail.STATIC);
			return false;
		}
		if (player.isImmobilized() || player.isCastingNow())
		{
			player.sendActionFailed();
			return false;
		}
		final MonsterInstance target = (MonsterInstance) player.getTarget();
		if (target.getCurrentHpPercents() >= 50)
		{
			player.sendPacket(Msg.THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_A_SOUL, ActionFail.STATIC);
			return false;
		}
		final int skillHitTime = SkillTable.getInstance().getInfo(2096, 1).getHitTime();
		player.broadcastPacket(new MagicSkillUse(player, player, 2096, 1, skillHitTime, 0));
		player.sendPacket(new SetupGauge(player, SetupGauge.BLUE, skillHitTime));
		player._skillTask = ThreadPoolManager.getInstance().schedule(new CrystalFinalizer(player, target), skillHitTime);
		return true;
	}
	
	/**
	 * @author Mobius
	 */
	static class CrystalFinalizer extends RunnableImpl
	{
		/**
		 * Field _activeChar.
		 */
		private final Player _activeChar;
		/**
		 * Field _target.
		 */
		private final MonsterInstance _target;
		
		/**
		 * Constructor for CrystalFinalizer.
		 * @param activeChar Player
		 * @param target MonsterInstance
		 */
		CrystalFinalizer(Player activeChar, MonsterInstance target)
		{
			_activeChar = activeChar;
			_target = target;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_activeChar.sendActionFailed();
			_activeChar.clearCastVars();
			if (_activeChar.isDead() || _target.isDead())
			{
				return;
			}
			_target.addAbsorber(_activeChar);
		}
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
