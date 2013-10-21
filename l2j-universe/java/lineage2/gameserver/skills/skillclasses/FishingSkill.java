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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.FishTable;
import lineage2.gameserver.templates.FishTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishingSkill extends Skill
{
	/**
	 * Constructor for FishingSkill.
	 * @param set StatsSet
	 */
	public FishingSkill(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = (Player) activeChar;
		if (player.getSkillLevel(SKILL_FISHING_MASTERY) == -1)
		{
			return false;
		}
		if (player.isFishing())
		{
			player.stopFishing();
			player.sendPacket(Msg.CANCELS_FISHING);
			return false;
		}
		if (player.isInBoat())
		{
			activeChar.sendPacket(Msg.YOU_CANT_FISH_WHILE_YOU_ARE_ON_BOARD);
			return false;
		}
		if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_FISH_WHILE_USING_A_RECIPE_BOOK_PRIVATE_MANUFACTURE_OR_PRIVATE_STORE);
			return false;
		}
		if (!player.isInZone(ZoneType.FISHING) || player.isInWater())
		{
			player.sendPacket(Msg.YOU_CANT_FISH_HERE);
			return false;
		}
		WeaponTemplate weaponItem = player.getActiveWeaponItem();
		if ((weaponItem == null) || (weaponItem.getItemType() != WeaponType.ROD))
		{
			player.sendPacket(Msg.FISHING_POLES_ARE_NOT_INSTALLED);
			return false;
		}
		ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if ((lure == null) || (lure.getCount() < 1))
		{
			player.sendPacket(Msg.BAITS_ARE_NOT_PUT_ON_A_HOOK);
			return false;
		}
		int rnd = Rnd.get(50) + 150;
		double angle = PositionUtils.convertHeadingToDegree(player.getHeading());
		double radian = Math.toRadians(angle - 90);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);
		int x1 = -(int) (sin * rnd);
		int y1 = (int) (cos * rnd);
		int x = player.getX() + x1;
		int y = player.getY() + y1;
		int z = GeoEngine.getHeight(x, y, player.getZ(), player.getGeoIndex()) + 1;
		boolean isInWater = false;
		LazyArrayList<Zone> zones = LazyArrayList.newInstance();
		World.getZones(zones, new Location(x, y, z), player.getReflection());
		for (Zone zone : zones)
		{
			if (zone.getType() == ZoneType.FISHING)
			{
				z = zone.getTerritory().getZmax();
				isInWater = true;
				break;
			}
		}
		LazyArrayList.recycle(zones);
		if (!isInWater)
		{
			player.sendPacket(Msg.YOU_CANT_FISH_HERE);
			return false;
		}
		player.getFishing().setFishLoc(new Location(x, y, z));
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param caster Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature caster, List<Creature> targets)
	{
		if ((caster == null) || !caster.isPlayer())
		{
			return;
		}
		Player player = (Player) caster;
		ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if ((lure == null) || (lure.getCount() < 1))
		{
			player.sendPacket(Msg.BAITS_ARE_NOT_PUT_ON_A_HOOK);
			return;
		}
		Zone zone = player.getZone(ZoneType.FISHING);
		if (zone == null)
		{
			return;
		}
		zone.getParams().getInteger("distribution_id");
		int lureId = lure.getItemId();
		int group = lineage2.gameserver.model.Fishing.getFishGroup(lure.getItemId());
		int type = lineage2.gameserver.model.Fishing.getRandomFishType(lureId);
		int lvl = lineage2.gameserver.model.Fishing.getRandomFishLvl(player);
		List<FishTemplate> fishs = FishTable.getInstance().getFish(group, type, lvl);
		if ((fishs == null) || (fishs.size() == 0))
		{
			player.sendPacket(Msg.SYSTEM_ERROR);
			return;
		}
		if (!player.getInventory().destroyItemByObjectId(player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1L))
		{
			player.sendPacket(Msg.NOT_ENOUGH_BAIT);
			return;
		}
		int check = Rnd.get(fishs.size());
		FishTemplate fish = fishs.get(check);
		player.startFishing(fish, lureId);
	}
}
