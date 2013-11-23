/*package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.npc.NpcTemplate;

public class MammonsInstance extends NpcInstance {

    private static final long serialVersionUID = 1796248576314922526L;

    private static final int ANCIENT_ADENA_ID = 5575;
    private static final int ADENA = 57;
    public static final String MAMMONS_HTML_PATH = "ssmammons/";

    public MammonsInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;
        super.onBypassFeedback(player, command);
        if (command.startsWith("bmarket")) {
            ItemInstance ancientAdena = player.getInventory().getItemByItemId(ANCIENT_ADENA_ID);
            long ancientAdenaAmount = ancientAdena != null ? ancientAdena.getCount() : 0L;
            int val = Integer.parseInt(command.substring(11, 12).trim());
            if (command.length() > 12)
                val = Integer.parseInt(command.substring(11, 13).trim());
            switch (val) {
                case 1:
                    long ancientAdenaConvert = 0L;
                    try {
                        ancientAdenaConvert = Long.parseLong(command.substring(13).trim());
                    } catch (NumberFormatException e) {
                        player.sendMessage(new CustomMessage("common.IntegerAmount", player, new Object[0]));
                        return;
                    } catch (StringIndexOutOfBoundsException e) {
                        player.sendMessage(new CustomMessage("common.IntegerAmount", player, new Object[0]));
                        return;
                    }
                    if (ancientAdenaAmount < ancientAdenaConvert || ancientAdenaConvert < 1L) {
                        player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                        return;
                    }
                    if (player.getInventory().destroyItemByItemId(ANCIENT_ADENA_ID, ancientAdenaConvert)) {
                        player.addAdena(ancientAdenaConvert);
                        player.sendPacket(SystemMessage2.removeItems(ANCIENT_ADENA_ID, ancientAdenaConvert));
                        player.sendPacket(SystemMessage2.obtainItems(ADENA, ancientAdenaConvert, 0));
                    }
                    break;

                default:
                    showChatWindow(player, "blkmrkt.htm", new Object[0]);
                    break;
            }
        }
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        int npcId = getTemplate().npcId;
        String filename = MAMMONS_HTML_PATH;
        switch (npcId) {
            case 31092:
                filename = filename + "blkmrkt.htm";
                break;
            case 31113:
                filename = filename + "merchmamm.htm";
                break;
            case 31126:
                filename = filename + "blksmithmam.htm";
                break;
            case 33511:
                filename = filename + "priestmam.htm";
                break;
            default:
                filename = getHtmlPath(npcId, val, player);
                break;
        }
        player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
    }
}          */
