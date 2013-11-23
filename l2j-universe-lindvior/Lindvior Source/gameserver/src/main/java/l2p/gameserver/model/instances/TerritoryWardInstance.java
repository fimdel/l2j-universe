package l2p.gameserver.model.instances;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.DominionSiegeEvent;
import l2p.gameserver.model.entity.events.objects.TerritoryWardObject;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 16:38/11.04.2011
 */
public class TerritoryWardInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 5238061197304584367L;
    private final TerritoryWardObject _territoryWard;

    public TerritoryWardInstance(int objectId, NpcTemplate template, TerritoryWardObject territoryWardObject) {
        super(objectId, template);
        setHasChatWindow(false);
        _territoryWard = territoryWardObject;
    }

    @Override
    public void onDeath(Creature killer) {
        super.onDeath(killer);
        Player player = killer.getPlayer();
        if (player == null)
            return;

        if (_territoryWard.canPickUp(player)) {
            _territoryWard.pickUp(player);
            decayMe();
        }
    }

    @Override
    protected void onDecay() {
        decayMe();

        _spawnAnimation = 2;
    }

    @Override
    public boolean isAttackable(Creature attacker) {
        return isAutoAttackable(attacker);
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        DominionSiegeEvent siegeEvent = getEvent(DominionSiegeEvent.class);
        if (siegeEvent == null)
            return false;
        DominionSiegeEvent siegeEvent2 = attacker.getEvent(DominionSiegeEvent.class);
        if (siegeEvent2 == null)
            return false;
        if (siegeEvent == siegeEvent2)
            return false;
        if (siegeEvent2.getResidence().getOwner() != attacker.getClan())
            return false;
        return true;
    }

    @Override
    public boolean isInvul() {
        return false;
    }

    @Override
    public Clan getClan() {
        return null;
    }
}
