package l2p.gameserver.stats.conditions;

import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.stats.Env;

public class ConditionZoneType extends Condition {
    private final ZoneType _zoneType;

    public ConditionZoneType(String zoneType) {
        _zoneType = ZoneType.valueOf(zoneType);
    }

    @Override
    protected boolean testImpl(Env env) {
        if (!env.character.isPlayer())
            return false;
        return env.character.isInZone(_zoneType);
    }
}