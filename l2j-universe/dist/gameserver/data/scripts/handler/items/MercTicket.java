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

import java.util.Collection;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.dao.CastleHiredGuardDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.support.MerchantGuard;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.PositionUtils;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MercTicket extends ScriptItemHandler
{
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
		return false;
	}
	
	/**
	 * Method dropItem.
	 * @param player Player
	 * @param item ItemInstance
	 * @param count long
	 * @param loc Location
	 * @see lineage2.gameserver.handler.items.IItemHandler#dropItem(Player, ItemInstance, long, Location)
	 */
	@Override
	public void dropItem(Player player, ItemInstance item, long count, Location loc)
	{
		if (!player.hasPrivilege(Privilege.CS_FS_MERCENARIES) || (player.getClan().getCastle() == 0))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_POSITION_MERCENARIES, ActionFail.STATIC);
			return;
		}
		
		final Castle castle = player.getCastle();
		final MerchantGuard guard = castle.getMerchantGuard(item.getItemId());
		if ((guard == null) || !castle.checkIfInZone(loc, ReflectionManager.DEFAULT) || player.isActionBlocked(Zone.BLOCKED_ACTION_DROP_MERCHANT_GUARD))
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_POSITION_MERCENARIES_HERE, ActionFail.STATIC);
			return;
		}
		
		if (castle.getSiegeEvent().isInProgress())
		{
			player.sendPacket(SystemMsg.A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS, ActionFail.STATIC);
			return;
		}
		
		int countOfGuard = 0;
		for (ItemInstance $item : castle.getSpawnMerchantTickets())
		{
			if (PositionUtils.getDistance($item.getLoc(), loc) < 200)
			{
				player.sendPacket(SystemMsg.POSITIONING_CANNOT_BE_DONE_HERE_BECAUSE_THE_DISTANCE_BETWEEN_MERCENARIES_IS_TOO_SHORT, ActionFail.STATIC);
				return;
			}
			if ($item.getItemId() == guard.getItemId())
			{
				countOfGuard++;
			}
		}
		
		if (countOfGuard >= guard.getMax())
		{
			player.sendPacket(SystemMsg.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE, ActionFail.STATIC);
			return;
		}
		
		item = player.getInventory().removeItemByObjectId(item.getObjectId(), 1);
		if (item == null)
		{
			player.sendActionFailed();
			return;
		}
		
		Log.LogItem(player, Log.Drop, item);
		
		item.dropToTheGround(player, loc);
		player.disableDrop(1000);
		
		player.sendChanges();
		
		item.delete();
		item.setJdbcState(JdbcEntityState.STORED);
		
		castle.getSpawnMerchantTickets().add(item);
		CastleHiredGuardDAO.getInstance().insert(castle, item.getItemId(), item.getLoc());
	}
	
	/**
	 * Method pickupItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#pickupItem(Playable, ItemInstance)
	 */
	@Override
	public boolean pickupItem(Playable playable, ItemInstance item)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		
		final Player player = (Player) playable;
		if (!player.hasPrivilege(Privilege.CS_FS_MERCENARIES) || (player.getClan().getCastle() == 0))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_CANCEL_MERCENARY_POSITIONING);
			return false;
		}
		
		final Castle castle = player.getCastle();
		if (!castle.getSpawnMerchantTickets().contains(item))
		{
			player.sendPacket(SystemMsg.THIS_IS_NOT_A_MERCENARY_OF_A_CASTLE_THAT_YOU_OWN_AND_SO_YOU_CANNOT_CANCEL_ITS_POSITIONING);
			return false;
		}
		
		if (castle.getSiegeEvent().isInProgress())
		{
			player.sendPacket(SystemMsg.A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS, ActionFail.STATIC);
			return false;
		}
		castle.getSpawnMerchantTickets().remove(item);
		CastleHiredGuardDAO.getInstance().delete(castle, item);
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		final IntSet set = new HashIntSet(100);
		final Collection<Castle> castles = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castles)
		{
			set.addAll(c.getMerchantGuards().keySet());
		}
		return set.toArray();
	}
}