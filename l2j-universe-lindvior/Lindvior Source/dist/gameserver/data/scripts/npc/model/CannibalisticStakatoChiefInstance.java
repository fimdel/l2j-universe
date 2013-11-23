package npc.model;

import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.RaidBossInstance;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.npc.NpcTemplate;

public class CannibalisticStakatoChiefInstance extends RaidBossInstance {
    /**
     *
     */
    private static final long serialVersionUID = -8341801084021751166L;
    private static final int ITEMS[] = {14833, 14834};

    public CannibalisticStakatoChiefInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    protected void onDeath(Creature killer) {
        super.onDeath(killer);
        if (killer == null)
            return;
        Creature topdam = getAggroList().getTopDamager();
        if (topdam == null)
            topdam = killer;
        Player pc = topdam.getPlayer();
        if (pc == null)
            return;
        Party party = pc.getParty();
        int itemId;
        if (party != null) {
            for (Player partyMember : party.getPartyMembers())
                if (partyMember != null && pc.isInRange(partyMember, Config.ALT_PARTY_DISTRIBUTION_RANGE)) {
                    itemId = ITEMS[Rnd.get(ITEMS.length)];
                    partyMember.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(itemId));
                    partyMember.getInventory().addItem(itemId, 1);
                }
        } else {
            itemId = ITEMS[Rnd.get(ITEMS.length)];
            pc.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(itemId));
            pc.getInventory().addItem(itemId, 1);
        }
    }
}