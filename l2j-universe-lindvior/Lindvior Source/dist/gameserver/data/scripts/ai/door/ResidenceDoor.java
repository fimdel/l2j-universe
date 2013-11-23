package ai.door;

import l2p.gameserver.ai.DoorAI;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.listener.actor.player.OnAnswerListener;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.Residence;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.ConfirmDlg;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 15:32/11.07.2011
 */
public class ResidenceDoor extends DoorAI {
    public ResidenceDoor(DoorInstance actor) {
        super(actor);
    }

    @Override
    public void onEvtTwiceClick(final Player player) {
        final DoorInstance door = getActor();

        Residence residence = ResidenceHolder.getInstance().getResidence(door.getTemplate().getAIParams().getInteger("residence_id"));
        if (residence.getOwner() != null && player.getClan() != null && player.getClan() == residence.getOwner() && (player.getClanPrivileges() & Clan.CP_CS_ENTRY_EXIT) == Clan.CP_CS_ENTRY_EXIT) {
            SystemMsg msg = door.isOpen() ? SystemMsg.WOULD_YOU_LIKE_TO_CLOSE_THE_GATE : SystemMsg.WOULD_YOU_LIKE_TO_OPEN_THE_GATE;
            player.ask(new ConfirmDlg(msg, 0), new OnAnswerListener() {
                @Override
                public void sayYes() {
                    if (door.isOpen())
                        door.closeMe(player, true);
                    else
                        door.openMe(player, true);
                }

                @Override
                public void sayNo() {
                    //
                }
            });
        }
    }
}
