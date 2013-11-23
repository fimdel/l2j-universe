/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills.skillclasses;

import l2p.commons.util.Rnd;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.*;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.tables.FishTable;
import l2p.gameserver.templates.FishTemplate;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.WeaponTemplate;
import l2p.gameserver.templates.item.type.WeaponType;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.PositionUtils;

import java.util.ArrayList;
import java.util.List;

public class FishingSkill extends Skill {
    public FishingSkill(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        Player player = (Player) activeChar;

        if (player.getSkillLevel(SKILL_FISHING_MASTERY) == -1)
            return false;

        if (player.isFishing()) {
            player.stopFishing();
            player.sendPacket(Msg.CANCELS_FISHING);
            return false;
        }

        if (player.isInBoat()) {
            activeChar.sendPacket(Msg.YOU_CANT_FISH_WHILE_YOU_ARE_ON_BOARD);
            return false;
        }

        if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE) {
            activeChar.sendPacket(Msg.YOU_CANNOT_FISH_WHILE_USING_A_RECIPE_BOOK_PRIVATE_MANUFACTURE_OR_PRIVATE_STORE);
            return false;
        }

        if (!player.isInZone(ZoneType.FISHING) || player.isInWater()) {
            player.sendPacket(Msg.YOU_CANT_FISH_HERE);
            return false;
        }

        WeaponTemplate weaponItem = player.getActiveWeaponItem();
        if (weaponItem == null || weaponItem.getItemType() != WeaponType.ROD) {
            //Fishing poles are not installed
            player.sendPacket(Msg.FISHING_POLES_ARE_NOT_INSTALLED);
            return false;
        }

        ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
        if (lure == null || lure.getCount() < 1) {
            player.sendPacket(Msg.BAITS_ARE_NOT_PUT_ON_A_HOOK);
            return false;
        }

        //Вычисляем координаты поплавка
        int rnd = Rnd.get(50) + 150;
        double angle = PositionUtils.convertHeadingToDegree(player.getHeading());
        double radian = Math.toRadians(angle - 90);
        double sin = Math.sin(radian);
        double cos = Math.cos(radian);
        int x1 = -(int) (sin * rnd);
        int y1 = (int) (cos * rnd);
        int x = player.getX() + x1;
        int y = player.getY() + y1;
        //z - уровень карты
        int z = GeoEngine.getHeight(x, y, player.getZ(), player.getGeoIndex()) + 1;

        //Проверяем, что поплавок оказался в воде
        boolean isInWater = false;
        List<Zone> zones = new ArrayList<Zone>();
        World.getZones(zones, new Location(x, y, z), player.getReflection());
        for (Zone zone : zones)
            if (zone.getType() == ZoneType.water) {
                //z - уровень воды
                z = zone.getTerritory().getZmax();
                isInWater = true;
                break;
            }
        zones.clear();

        if (!isInWater) {
            player.sendPacket(Msg.YOU_CANT_FISH_HERE);
            return false;
        }

        player.getFishing().setFishLoc(new Location(x, y, z));

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature caster, List<Creature> targets) {
        if (caster == null || !caster.isPlayer())
            return;

        Player player = (Player) caster;

        ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
        if (lure == null || lure.getCount() < 1) {
            player.sendPacket(Msg.BAITS_ARE_NOT_PUT_ON_A_HOOK);
            return;
        }
        Zone zone = player.getZone(ZoneType.FISHING);
        if (zone == null)
            return;

        int lureId = lure.getItemId();

        int group = l2p.gameserver.model.Fishing.getFishGroup(lure.getItemId());
        int type = l2p.gameserver.model.Fishing.getRandomFishType(lureId);
        int lvl = l2p.gameserver.model.Fishing.getRandomFishLvl(player);

        List<FishTemplate> fishs = FishTable.getInstance().getFish(group, type, lvl);
        if (fishs == null || fishs.size() == 0) {
            player.sendPacket(Msg.SYSTEM_ERROR);
            return;
        }

        if (!player.getInventory().destroyItemByObjectId(player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1L)) {
            player.sendPacket(Msg.NOT_ENOUGH_BAIT);
            return;
        }

        int check = Rnd.get(fishs.size());
        FishTemplate fish = fishs.get(check);

        player.startFishing(fish, lureId);
    }
}
