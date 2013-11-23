package npc.model.residences.clanhall;

import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.ClanHall;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.TimeUtils;

/**
 * @author VISTALL
 * @date 18:16/04.03.2011
 */
public class BrakelInstance extends NpcInstance {
    private static final long serialVersionUID = 1L;

    public BrakelInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        ClanHall clanhall = ResidenceHolder.getInstance().getResidence(ClanHall.class, 21);
        if (clanhall == null)
            return;
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        html.setFile("residence2/clanhall/partisan_ordery_brakel001.htm");
        html.replace("%next_siege%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate().getTimeInMillis()));
        player.sendPacket(html);
    }
}
