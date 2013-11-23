package l2p.gameserver.model.instances;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;

import java.util.StringTokenizer;

public final class MercManagerInstance extends MerchantInstance {
    /**
     *
     */
    private static final long serialVersionUID = -8310451267692462639L;
    private static int COND_ALL_FALSE = 0;
    private static int COND_BUSY_BECAUSE_OF_SIEGE = 1;
    private static int COND_OWNER = 2;

    public MercManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        int condition = validateCondition(player);
        if (condition <= COND_ALL_FALSE || condition == COND_BUSY_BECAUSE_OF_SIEGE)
            return;

        if (condition == COND_OWNER) {
            StringTokenizer st = new StringTokenizer(command, " ");
            String actualCommand = st.nextToken(); // Get actual command

            String val = "";
            if (st.countTokens() >= 1)
                val = st.nextToken();

            if (actualCommand.equalsIgnoreCase("hire")) {
                if (val.equals(""))
                    return;

                showShopWindow(player, Integer.parseInt(val), false);
            } else
                super.onBypassFeedback(player, command);
        }
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        String filename = "castle/mercmanager/mercmanager-no.htm";
        int condition = validateCondition(player);
        if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
            filename = "castle/mercmanager/mercmanager-busy.htm"; // Busy because of siege
        else if (condition == COND_OWNER)
            filename = "castle/mercmanager/mercmanager.htm";
        player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
    }

    private int validateCondition(Player player) {
        if (player.isGM())
            return COND_OWNER;
        if (getCastle() != null && getCastle().getId() > 0)
            if (player.getClan() != null)
                if (getCastle().getSiegeEvent().isInProgress())
                    return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
                else if (getCastle().getOwnerId() == player.getClanId() // Clan owns castle
                        && (player.getClanPrivileges() & Clan.CP_CS_MERCENARIES) == Clan.CP_CS_MERCENARIES) // has merc rights
                    return COND_OWNER; // Owner

        return COND_ALL_FALSE;
    }
}