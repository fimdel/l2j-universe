package l2p.gameserver.instancemanager;

import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExTacticalSign;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 08.09.12
 * Time: 3:42
 */
public class TacticalSignManager {
    /*
     * Присваиваем метку таргету
     */
    public static void setTacticalSign(Player player, GameObject target, int signId) {
        if (player.getTarget() == null || player.getParty() == null) {
            return;
        }

        player.getParty().getTacticalSignsList().put(signId, target);
        for (Player partyChar : player.getParty())
            partyChar.sendPacket(new ExTacticalSign(target.getObjectId(), signId));
    }

    /*
     * Берём в таргет объект с меткой
     */
    public static void getTargetOnTacticalSign(Player player, int signId) {
        if (player.getParty() != null) {
            GameObject newTarget = player.getParty().getTacticalSignsList().get(signId);
            player.setTarget(newTarget);
        }

    }

    /*
    * Возвращает номер метки по objectId таргета
    */
    public static int getSignOnTarget(GameObject target, Party party) {
        if (party != null)
            for (Map.Entry<Integer, GameObject> entry : party.getTacticalSignsList().entrySet())
                if (entry.getValue() == target)
                    return entry.getKey();
        return 0;
    }

    /*
    * Возвращает objectId таргета по номеру метки
    */
    public static GameObject getTargetOnSign(int signId, Party party) {
        return party != null ? party.getTacticalSignsList().get(signId) : null;
    }
}
