package l2p.gameserver.taskmanager.tasks;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.network.serverpackets.ExRegistPartySubstitute;
import l2p.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.11.12
 * Time: 21:22
 */
public class PartySubstituteTask extends RunnableImpl {
    @Override
    public void runImpl() throws Exception {
        ConcurrentMap<Player, Integer> _wPlayers = PartySubstituteManager.getInstance().getWaitingPlayer();
        Set<Player> _wPartys = PartySubstituteManager.getInstance().getWaitingParty();

        Set<Map.Entry<Player, Integer>> sets = _wPlayers.entrySet();

        for (Map.Entry<Player, Integer> e : sets) {
            Player p = e.getKey();

            if (e.getValue() > 4) {
                PartySubstituteManager.getInstance().removePlayerReplace(p);
                p.getParty().getPartyLeader().sendPacket(new ExRegistPartySubstitute(p.getObjectId(), ExRegistPartySubstitute.REGISTER_TIMEOUT));
                continue;
            }

            for (Player pp : _wPartys) {
                if (PartySubstituteManager.getInstance().isGoodPlayer(p, pp)) {
                    if (pp.isProcessingRequest())
                        continue;

                    new Request(Request.L2RequestType.SUBSTITUTE, p, pp).setTimeout(10000L);
                    pp.sendPacket(new ExRegistWaitingSubstituteOk(p.getParty(), p));

                    break;
                }
            }
            PartySubstituteManager.getInstance().updatePlayerToReplace(p, e.getValue() + 1);
        }
    }

}
