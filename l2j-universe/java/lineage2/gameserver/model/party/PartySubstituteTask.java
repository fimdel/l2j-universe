package lineage2.gameserver.model.party;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.ExRegistPartySubstitute;
import lineage2.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class PartySubstituteTask extends RunnableImpl
{
	@Override
	public void runImpl() throws Exception
	{
		ConcurrentMap<Player, Integer> _wPlayers = PartySubstitute.getInstance().getWaitingPlayer();
		Set<Player> _wPartys = PartySubstitute.getInstance().getWaitingParty();

		Set<Entry<Player, Integer>> sets = _wPlayers.entrySet();

		for (Entry<Player, Integer> e : sets)
		{
			Player p = e.getKey();

			if (e.getValue() > 4)
			{
				PartySubstitute.getInstance().removePlayerReplace(p);
				p.getParty().getPartyLeader().sendPacket(new ExRegistPartySubstitute(p.getObjectId(), ExRegistPartySubstitute.REGISTER_TIMEOUT));
				continue;
			}

			for (Player pp : _wPartys)
			{
				if (PartySubstitute.getInstance().isGoodPlayer(p, pp))
				{
					if (pp.isProcessingRequest())
						continue;

					new Request(L2RequestType.SUBSTITUTE, p, pp).setTimeout(10000L);
					pp.sendPacket(new ExRegistWaitingSubstituteOk(p.getParty(), p));

					break;
				}
			}
			PartySubstitute.getInstance().updatePlayerToReplace(p, e.getValue() + 1);
		}
	}

}
