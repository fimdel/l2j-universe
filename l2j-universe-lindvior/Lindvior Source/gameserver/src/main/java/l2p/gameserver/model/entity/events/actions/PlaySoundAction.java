package l2p.gameserver.model.entity.events.actions;

import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.EventAction;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.network.serverpackets.PlaySound;

import java.util.List;

/**
 * @author VISTALL
 * @date 16:25/06.01.2011
 */
public class PlaySoundAction implements EventAction {
    private int _range;
    private String _sound;
    private PlaySound.Type _type;

    public PlaySoundAction(int range, String s, PlaySound.Type type) {
        _range = range;
        _sound = s;
        _type = type;
    }

    @Override
    public void call(GlobalEvent event) {
        GameObject object = event.getCenterObject();
        PlaySound packet = null;
        if (object != null)
            packet = new PlaySound(_type, _sound, 1, object.getObjectId(), object.getLoc());
        else
            packet = new PlaySound(_type, _sound, 0, 0, 0, 0, 0);

        List<Player> players = event.broadcastPlayers(_range);
        for (Player player : players)
            if (player != null)
                player.sendPacket(packet);
    }
}
