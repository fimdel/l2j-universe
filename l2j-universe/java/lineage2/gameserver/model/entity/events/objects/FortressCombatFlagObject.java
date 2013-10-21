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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.attachment.FlagItemAttachment;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FortressCombatFlagObject implements SpawnableObject, FlagItemAttachment
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FortressCombatFlagObject.class);
	/**
	 * Field _item.
	 */
	private ItemInstance _item;
	/**
	 * Field _location.
	 */
	private final Location _location;
	/**
	 * Field _event.
	 */
	private GlobalEvent _event;
	
	/**
	 * Constructor for FortressCombatFlagObject.
	 * @param location Location
	 */
	public FortressCombatFlagObject(Location location)
	{
		_location = location;
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		if (_item != null)
		{
			_log.info("FortressCombatFlagObject: can't spawn twice: " + event);
			return;
		}
		_item = ItemFunctions.createItem(9819);
		_item.setAttachment(this);
		_item.dropMe(null, _location);
		_item.setDropTime(0);
		_event = event;
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
		if (_item == null)
		{
			return;
		}
		Player owner = GameObjectsStorage.getPlayer(_item.getOwnerId());
		if (owner != null)
		{
			owner.getInventory().destroyItem(_item);
			owner.sendDisarmMessage(_item);
		}
		_item.setAttachment(null);
		_item.setJdbcState(JdbcEntityState.UPDATED);
		_item.delete();
		_item.deleteMe();
		_item = null;
		_event = null;
	}
	
	/**
	 * Method refreshObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#refreshObject(GlobalEvent)
	 */
	@Override
	public void refreshObject(GlobalEvent event)
	{
	}
	
	/**
	 * Method onLogout.
	 * @param player Player
	 * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#onLogout(Player)
	 */
	@Override
	public void onLogout(Player player)
	{
		onDeath(player, null);
	}
	
	/**
	 * Method onDeath.
	 * @param owner Player
	 * @param killer Creature
	 * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#onDeath(Player, Creature)
	 */
	@Override
	public void onDeath(Player owner, Creature killer)
	{
		owner.getInventory().removeItem(_item);
		_item.setOwnerId(0);
		_item.setJdbcState(JdbcEntityState.UPDATED);
		_item.update();
		owner.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_DROPPED_S1).addItemName(_item.getItemId()));
		_item.dropMe(null, _location);
		_item.setDropTime(0);
	}
	
	/**
	 * Method canPickUp.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.PickableAttachment#canPickUp(Player)
	 */
	@Override
	public boolean canPickUp(Player player)
	{
		if (player.getActiveWeaponFlagAttachment() != null)
		{
			return false;
		}
		FortressSiegeEvent event = player.getEvent(FortressSiegeEvent.class);
		if (event == null)
		{
			return false;
		}
		SiegeClanObject object = event.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if (object == null)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method pickUp.
	 * @param player Player
	 * @see lineage2.gameserver.model.items.attachment.PickableAttachment#pickUp(Player)
	 */
	@Override
	public void pickUp(Player player)
	{
		player.getInventory().equipItem(_item);
		FortressSiegeEvent event = player.getEvent(FortressSiegeEvent.class);
		event.broadcastTo(new SystemMessage2(SystemMsg.C1_HAS_ACQUIRED_THE_FLAG).addName(player), SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
	}
	
	/**
	 * Method canAttack.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#canAttack(Player)
	 */
	@Override
	public boolean canAttack(Player player)
	{
		player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS);
		return false;
	}
	
	/**
	 * Method canCast.
	 * @param player Player
	 * @param skill Skill
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#canCast(Player, Skill)
	 */
	@Override
	public boolean canCast(Player player, Skill skill)
	{
		Skill[] skills = player.getActiveWeaponItem().getAttachedSkills();
		if (!ArrayUtils.contains(skills, skill))
		{
			player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL);
			return false;
		}
		return true;
	}
	
	/**
	 * Method setItem.
	 * @param item ItemInstance
	 * @see lineage2.gameserver.model.items.attachment.ItemAttachment#setItem(ItemInstance)
	 */
	@Override
	public void setItem(ItemInstance item)
	{
	}
	
	/**
	 * Method getEvent.
	 * @return GlobalEvent
	 */
	public GlobalEvent getEvent()
	{
		return _event;
	}
}
