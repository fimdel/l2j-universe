package l2p.gameserver.utils;

import l2p.gameserver.data.xml.holder.InstantZoneHolder;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.CommandChannel;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.templates.InstantZone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @date 2:14/30.06.2011
 */
public class ReflectionUtils {
    /**
     * Использовать акуратно возращает дверь нулевого рефлекта
     *
     * @param id
     * @return
     */
    public static DoorInstance getDoor(int id) {
        return ReflectionManager.DEFAULT.getDoor(id);
    }

    /**
     * Использовать акуратно возращает зону нулевого рефлекта
     *
     * @param name
     * @return
     */
    public static Zone getZone(String name) {
        return ReflectionManager.DEFAULT.getZone(name);
    }

    public static List<Zone> getZonesByType(Zone.ZoneType zoneType) {
        Collection<Zone> zones = ReflectionManager.DEFAULT.getZones();
        if (zones.isEmpty())
            return Collections.emptyList();

        List<Zone> zones2 = new ArrayList<Zone>(5);
        for (Zone z : zones)
            if (z.getType() == zoneType)
                zones2.add(z);

        return zones2;
    }

    public static Reflection enterReflection(Player invoker, int instancedZoneId) {
        InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
        return enterReflection(invoker, new Reflection(), iz);
    }

    public static Reflection enterReflection(Player invoker, Reflection r, int instancedZoneId) {
        InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
        return enterReflection(invoker, r, iz);
    }

    public static Reflection enterReflection(Player invoker, Reflection r, InstantZone iz) {
        r.init(iz);

        if (r.getReturnLoc() == null)
            r.setReturnLoc(invoker.getLoc());

        switch (iz.getEntryType()) {
            case SOLO:
                if (iz.getRemovedItemId() > 0)
                    ItemFunctions.removeItem(invoker, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
                if (iz.getGiveItemId() > 0)
                    ItemFunctions.addItem(invoker, iz.getGiveItemId(), iz.getGiveItemCount(), true);
                if (iz.isDispelBuffs())
                    invoker.dispelBuffs();
                if (iz.getSetReuseUponEntry() && iz.getResetReuse().next(System.currentTimeMillis()) > System.currentTimeMillis())
                    invoker.setInstanceReuse(iz.getId(), System.currentTimeMillis());
                invoker.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
                invoker.teleToLocation(iz.getTeleportCoord(), r);
                break;
            case PARTY:
                Party party = invoker.getParty();

                party.setReflection(r);
                r.setParty(party);

                for (Player member : party.getPartyMembers()) {
                    if (iz.getRemovedItemId() > 0)
                        ItemFunctions.removeItem(member, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
                    if (iz.getGiveItemId() > 0)
                        ItemFunctions.addItem(member, iz.getGiveItemId(), iz.getGiveItemCount(), true);
                    if (iz.isDispelBuffs())
                        member.dispelBuffs();
                    if (iz.getSetReuseUponEntry())
                        member.setInstanceReuse(iz.getId(), System.currentTimeMillis());
                    member.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
                    member.teleToLocation(iz.getTeleportCoord(), r);
                }
                break;
            case COMMAND_CHANNEL:
                Party commparty = invoker.getParty();
                CommandChannel cc = commparty.getCommandChannel();

                cc.setReflection(r);
                r.setCommandChannel(cc);

                for (Player member : cc) {
                    if (iz.getRemovedItemId() > 0)
                        ItemFunctions.removeItem(member, iz.getRemovedItemId(), iz.getRemovedItemCount(), true);
                    if (iz.getGiveItemId() > 0)
                        ItemFunctions.addItem(member, iz.getGiveItemId(), iz.getGiveItemCount(), true);
                    if (iz.isDispelBuffs())
                        member.dispelBuffs();
                    if (iz.getSetReuseUponEntry())
                        member.setInstanceReuse(iz.getId(), System.currentTimeMillis());
                    member.setVar("backCoords", invoker.getLoc().toXYZString(), -1);
                    member.teleToLocation(iz.getTeleportCoord(), r);
                }

                break;
        }

        return r;
    }
}
