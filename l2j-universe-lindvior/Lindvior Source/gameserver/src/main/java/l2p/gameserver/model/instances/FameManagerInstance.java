/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.instances;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowInfoUpdate;
import l2p.gameserver.templates.npc.NpcTemplate;

import java.util.StringTokenizer;

public class FameManagerInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -7856907533631790669L;

    public FameManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken();
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        if (actualCommand.equalsIgnoreCase("PK_Count")) {
            if (player.getFame() >= 500000) {
                if (player.getPkKills() > 0) {
                    player.setFame(player.getFame() - 500000, "PK_Count");
                    player.setPkKills(player.getPkKills() - 1);
                    html.setFile("default/" + getNpcId() + "-okpk.htm");
                } else
                    html.setFile("default/" + getNpcId() + "-nohavepk.htm");
            } else
                html.setFile("default/" + getNpcId() + "-nofame.htm");
            html.replace("%objectId%", String.valueOf(getObjectId()));
            player.sendPacket(html);
        } else if (actualCommand.equalsIgnoreCase("CRP")) {
            if (player.getClan() == null || player.getClassLevel() < 2 || player.getClan().getLevel() < 5)
                html.setFile("default/" + getNpcId() + "-noclancrp.htm");
            else if (player.getFame() < 10000)
                html.setFile("default/" + getNpcId() + "-nofame.htm");
            else {
                player.setFame(player.getFame() - 10000, "CRP");
                player.getClan().incReputation(50, false, "FameManager from " + player.getName());
                player.getClan().broadcastToOnlineMembers(new PledgeShowInfoUpdate(player.getClan()));
                player.sendPacket(Msg.ACQUIRED_50_CLAN_FAME_POINTS);
                html.setFile("default/" + getNpcId() + "-okclancrp.htm");
            }
            html.replace("%objectId%", String.valueOf(getObjectId()));
            player.sendPacket(html);
        } else
            super.onBypassFeedback(player, command);
    }
}