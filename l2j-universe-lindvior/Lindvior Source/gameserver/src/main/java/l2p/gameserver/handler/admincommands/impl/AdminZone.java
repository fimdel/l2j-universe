package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.instancemanager.MapRegionManager;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.templates.mapregion.DomainArea;

import java.util.ArrayList;
import java.util.List;

public class AdminZone implements IAdminCommandHandler {
    private static enum Commands {
        admin_zone_check,
        admin_region,
        admin_pos,
        admin_vis_count,
        admin_domain
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (activeChar == null || !activeChar.getPlayerAccess().CanTeleport)
            return false;

        switch (command) {
            case admin_zone_check: {
                activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
                activeChar.sendMessage("Zone list:");
                List<Zone> zones = new ArrayList<Zone>();
                World.getZones(zones, activeChar.getLoc(), activeChar.getReflection());
                for (Zone zone : zones)
                    activeChar.sendMessage(zone.getType().toString() + ", name: " + zone.getName() + ", state: " + (zone.isActive() ? "active" : "not active") + ", inside: " + zone.checkIfInZone(activeChar) + "/" + zone.checkIfInZone(activeChar.getX(), activeChar.getY(), activeChar.getZ()));

                break;
            }
            case admin_region: {
                activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
                activeChar.sendMessage("Objects list:");
                for (GameObject o : activeChar.getCurrentRegion())
                    if (o != null)
                        activeChar.sendMessage(o.toString());
                break;
            }
            case admin_vis_count: {
                activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
                activeChar.sendMessage("Players count: " + World.getAroundPlayers(activeChar).size());
                break;
            }
            case admin_pos: {
                String pos = activeChar.getX() + ", " + activeChar.getY() + ", " + activeChar.getZ() + ", " + activeChar.getHeading() + " Geo [" + (activeChar.getX() - World.MAP_MIN_X >> 4) + ", " + (activeChar.getY() - World.MAP_MIN_Y >> 4) + "] Ref " + activeChar.getReflectionId();
                activeChar.sendMessage("Pos: " + pos);
                break;
            }
            case admin_domain: {
                DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, activeChar);
                Castle castle = domain != null ? ResidenceHolder.getInstance().getResidence(Castle.class, domain.getId()) : null;
                if (castle != null)
                    activeChar.sendMessage("Domain: " + castle.getName());
                else
                    activeChar.sendMessage("Domain: Unknown");
            }
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}
