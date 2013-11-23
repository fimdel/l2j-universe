package l2p.gameserver.handler.admincommands.impl;

import l2p.commons.dbutils.DbUtils;
import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminTeleport implements IAdminCommandHandler {
    private static enum Commands {
        admin_show_moves,
        admin_show_moves_other,
        admin_show_teleport,
        admin_teleport_to_character,
        admin_teleportto,
        admin_teleport_to,
        admin_move_to,
        admin_moveto,
        admin_teleport,
        admin_teleport_character,
        admin_recall,
        admin_walk,
        admin_recall_npc,
        admin_gonorth,
        admin_gosouth,
        admin_goeast,
        admin_gowest,
        admin_goup,
        admin_godown,
        admin_tele,
        admin_teleto,
        admin_tele_to,
        admin_instant_move,
        admin_tonpc,
        admin_to_npc,
        admin_toobject,
        admin_setref,
        admin_getref
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        Commands command = (Commands) comm;

        if (!activeChar.getPlayerAccess().CanTeleport)
            return false;

        switch (command) {
            case admin_show_moves:
                activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/teleports.htm"));
                break;
            case admin_show_moves_other:
                activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/tele/other.htm"));
                break;
            case admin_show_teleport:
                showTeleportCharWindow(activeChar);
                break;
            case admin_teleport_to_character:
                teleportToCharacter(activeChar, activeChar.getTarget());
                break;
            case admin_teleport_to:
            case admin_teleportto:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //teleportto charName");
                    return false;
                }
                String chaName = Util.joinStrings(" ", wordList, 1);
                Player cha = GameObjectsStorage.getPlayer(chaName);
                if (cha == null) {
                    activeChar.sendMessage("Player '" + chaName + "' not found in world");
                    return false;
                }
                teleportToCharacter(activeChar, cha);
                break;
            case admin_move_to:
            case admin_moveto:
            case admin_teleport:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //teleport x y z [ref]");
                    return false;
                }
                teleportTo(activeChar, activeChar, Util.joinStrings(" ", wordList, 1, 3), ArrayUtils.valid(wordList, 4) != null && !ArrayUtils.valid(wordList, 4).isEmpty() ? Integer.parseInt(wordList[4]) : 0);
                break;
            case admin_walk:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //walk x y z");
                    return false;
                }
                try {
                    activeChar.moveToLocation(Location.parseLoc(Util.joinStrings(" ", wordList, 1)), 0, true);
                } catch (IllegalArgumentException e) {
                    activeChar.sendMessage("USAGE: //walk x y z");
                    return false;
                }
                break;
            case admin_gonorth:
            case admin_gosouth:
            case admin_goeast:
            case admin_gowest:
            case admin_goup:
            case admin_godown:
                int val = wordList.length < 2 ? 150 : Integer.parseInt(wordList[1]);
                int x = activeChar.getX();
                int y = activeChar.getY();
                int z = activeChar.getZ();
                if (command == Commands.admin_goup)
                    z += val;
                else if (command == Commands.admin_godown)
                    z -= val;
                else if (command == Commands.admin_goeast)
                    x += val;
                else if (command == Commands.admin_gowest)
                    x -= val;
                else if (command == Commands.admin_gosouth)
                    y += val;
                else if (command == Commands.admin_gonorth)
                    y -= val;

                activeChar.teleToLocation(x, y, z);
                showTeleportWindow(activeChar);
                break;
            case admin_tele:
                showTeleportWindow(activeChar);
                break;
            case admin_teleto:
            case admin_tele_to:
            case admin_instant_move:
                if (wordList.length > 1 && wordList[1].equalsIgnoreCase("r"))
                    activeChar.setTeleMode(2);
                else if (wordList.length > 1 && wordList[1].equalsIgnoreCase("end"))
                    activeChar.setTeleMode(0);
                else
                    activeChar.setTeleMode(1);
                break;
            case admin_tonpc:
            case admin_to_npc:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //tonpc npcId|npcName");
                    return false;
                }
                String npcName = Util.joinStrings(" ", wordList, 1);
                NpcInstance npc;
                try {
                    if ((npc = GameObjectsStorage.getByNpcId(Integer.parseInt(npcName))) != null) {
                        teleportToCharacter(activeChar, npc);
                        return true;
                    }
                } catch (Exception e) {
                }
                if ((npc = GameObjectsStorage.getNpc(npcName)) != null) {
                    teleportToCharacter(activeChar, npc);
                    return true;
                }
                activeChar.sendMessage("Npc " + npcName + " not found");
                break;
            case admin_toobject:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //toobject objectId");
                    return false;
                }
                Integer target = Integer.parseInt(wordList[1]);
                GameObject obj;
                if ((obj = GameObjectsStorage.findObject(target)) != null) {
                    teleportToCharacter(activeChar, obj);
                    return true;
                }
                activeChar.sendMessage("Object " + target + " not found");
                break;
            default:
                break;
        }

        if (!activeChar.getPlayerAccess().CanEditChar)
            return false;

        switch (command) {
            case admin_teleport_character:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //teleport_character x y z");
                    return false;
                }
                teleportCharacter(activeChar, Util.joinStrings(" ", wordList, 1));
                showTeleportCharWindow(activeChar);
                break;
            case admin_recall:
                if (wordList.length < 2) {
                    activeChar.sendMessage("USAGE: //recall charName");
                    return false;
                }
                String targetName = Util.joinStrings(" ", wordList, 1);
                Player recall_player = GameObjectsStorage.getPlayer(targetName);
                if (recall_player != null) {
                    teleportTo(activeChar, recall_player, activeChar.getLoc(), activeChar.getReflectionId());
                    return true;
                }
                int obj_id = CharacterDAO.getInstance().getObjectIdByName(targetName);
                if (obj_id > 0) {
                    teleportCharacter_offline(obj_id, activeChar.getLoc());
                    activeChar.sendMessage(targetName + " is offline. Offline teleport used...");
                } else
                    activeChar.sendMessage("->" + targetName + "<- is incorrect.");
                break;
            case admin_setref: {
                if (wordList.length < 2) {
                    activeChar.sendMessage("Usage: //setref <reflection>");
                    return false;
                }

                int ref_id = Integer.parseInt(wordList[1]);
                if (ref_id != 0 && ReflectionManager.getInstance().get(ref_id) == null) {
                    activeChar.sendMessage("Reflection <" + ref_id + "> not found.");
                    return false;
                }

                GameObject target = activeChar;
                GameObject obj = activeChar.getTarget();
                if (obj != null)
                    target = obj;

                target.setReflection(ref_id);
                target.decayMe();
                target.spawnMe();
                break;
            }
            case admin_getref:
                if (wordList.length < 2) {
                    activeChar.sendMessage("Usage: //getref <char_name>");
                    return false;
                }
                Player cha = GameObjectsStorage.getPlayer(wordList[1]);
                if (cha == null) {
                    activeChar.sendMessage("Player '" + wordList[1] + "' not found in world");
                    return false;
                }
                activeChar.sendMessage("Player '" + wordList[1] + "' in reflection: " + activeChar.getReflectionId() + ", name: " + activeChar.getReflection().getName());
                break;
            default:
                break;
        }

        if (!activeChar.getPlayerAccess().CanEditNPC)
            return false;

        switch (command) {
            case admin_recall_npc:
                recallNPC(activeChar);
                break;
            default:
                break;
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }

    private void showTeleportWindow(Player activeChar) {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>Teleport Menu</title>");
        replyMSG.append("<body>");

        replyMSG.append("<br>");
        replyMSG.append("<center><table>");

        replyMSG.append("<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"North\" action=\"bypass -h admin_gonorth\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Up\" action=\"bypass -h admin_goup\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
        replyMSG.append("<tr><td><button value=\"West\" action=\"bypass -h admin_gowest\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"East\" action=\"bypass -h admin_goeast\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
        replyMSG.append("<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"South\" action=\"bypass -h admin_gosouth\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
        replyMSG.append("<td><button value=\"Down\" action=\"bypass -h admin_godown\" width=70 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");

        replyMSG.append("</table></center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void showTeleportCharWindow(Player activeChar) {
        GameObject target = activeChar.getTarget();
        Player player = null;
        if (target.isPlayer())
            player = (Player) target;
        else {
            activeChar.sendPacket(Msg.INVALID_TARGET);
            return;
        }
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>Teleport Character</title>");
        replyMSG.append("<body>");
        replyMSG.append("The character you will teleport is " + player.getName() + ".");
        replyMSG.append("<br>");

        replyMSG.append("Co-ordinate x");
        replyMSG.append("<edit var=\"char_cord_x\" width=110>");
        replyMSG.append("Co-ordinate y");
        replyMSG.append("<edit var=\"char_cord_y\" width=110>");
        replyMSG.append("Co-ordinate z");
        replyMSG.append("<edit var=\"char_cord_z\" width=110>");
        replyMSG.append("<button value=\"Teleport\" action=\"bypass -h admin_teleport_character $char_cord_x $char_cord_y $char_cord_z\" width=60 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
        replyMSG.append("<button value=\"Teleport near you\" action=\"bypass -h admin_teleport_character " + activeChar.getX() + " " + activeChar.getY() + " " + activeChar.getZ() + "\" width=115 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
        replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void teleportTo(Player activeChar, Player target, String Cords, int ref) {
        try {
            teleportTo(activeChar, target, Location.parseLoc(Cords), ref);
        } catch (IllegalArgumentException e) {
            activeChar.sendMessage("You must define 3 coordinates required to teleport");
            return;
        }
    }

    private void teleportTo(Player activeChar, Player target, Location loc, int ref) {
        if (!target.equals(activeChar))
            target.sendMessage("Admin is teleporting you.");

        target.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
        target.teleToLocation(loc, ref);

        if (target.equals(activeChar))
            activeChar.sendMessage("You have been teleported to " + loc + ", reflection id: " + ref);
    }

    private void teleportCharacter(Player activeChar, String Cords) {
        GameObject target = activeChar.getTarget();
        if (target == null || !target.isPlayer()) {
            activeChar.sendPacket(Msg.INVALID_TARGET);
            return;
        }
        if (target.getObjectId() == activeChar.getObjectId()) {
            activeChar.sendMessage("You cannot teleport yourself.");
            return;
        }
        teleportTo(activeChar, (Player) target, Cords, activeChar.getReflectionId());
    }

    private void teleportCharacter_offline(int obj_id, Location loc) {
        if (obj_id == 0)
            return;

        Connection con = null;
        PreparedStatement st = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            st = con.prepareStatement("UPDATE characters SET x=?,y=?,z=? WHERE obj_Id=? LIMIT 1");
            st.setInt(1, loc.x);
            st.setInt(2, loc.y);
            st.setInt(3, loc.z);
            st.setInt(4, obj_id);
            st.executeUpdate();
        } catch (Exception e) {

        } finally {
            DbUtils.closeQuietly(con, st);
        }
    }

    private void teleportToCharacter(Player activeChar, GameObject target) {
        if (target == null)
            return;

        activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
        activeChar.teleToLocation(target.getLoc(), target.getReflectionId());

        activeChar.sendMessage("You have teleported to " + target);
    }

    private void recallNPC(Player activeChar) {
        GameObject obj = activeChar.getTarget();
        if (obj != null && obj.isNpc()) {
            obj.setLoc(activeChar.getLoc());
            ((NpcInstance) obj).broadcastCharInfo();
            activeChar.sendMessage("You teleported npc " + obj.getName() + " to " + activeChar.getLoc().toString() + ".");
        } else
            activeChar.sendMessage("Target is't npc.");
    }
}
