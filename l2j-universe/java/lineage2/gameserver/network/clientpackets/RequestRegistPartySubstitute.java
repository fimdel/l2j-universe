package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.party.PartySubstitute;
import lineage2.gameserver.network.serverpackets.ExRegistPartySubstitute;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class RequestRegistPartySubstitute extends L2GameClientPacket
{
	private int _changeCharId;

	@Override
	protected void readImpl()
	{
		_changeCharId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();

		if (player == null)
			return;

		Party p = player.getParty();

		if (p == null)
			return;

		if (!p.isLeader(player))
			return;

		Player chp = GameObjectsStorage.getPlayer(_changeCharId);

		if (!p.containsMember(chp))
			return;

		PartySubstitute.getInstance().addPlayerToReplace(chp);

		player.sendPacket(new ExRegistPartySubstitute(_changeCharId, ExRegistPartySubstitute.REGISTER_OK));

	}
}
