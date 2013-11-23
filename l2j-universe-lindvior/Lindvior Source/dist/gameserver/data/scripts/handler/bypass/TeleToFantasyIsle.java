package handler.bypass;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.utils.Location;

/**
 * @author VISTALL
 * @date 16:01/12.07.2011
 */
public class TeleToFantasyIsle extends ScriptBypassHandler {
    public static final Location[] POINTS =
            {
                    new Location(-60695, -56896, -2032), new Location(-59716, -55920, -2032), new Location(-58752, -56896, -2032), new Location(-59716, -57864, -2032)
            };

    @Override
    public String[] getBypassList() {
        return new String[]
                {
                        "teleToFantasyIsle"
                };
    }

    @Override
    public void onBypassCommand(String command, Player activeChar, Creature target) {
        if ((target == null) || (!target.isNpc())) {
            return;
        }
        activeChar.teleToLocation(Rnd.get(POINTS));
    }
}
