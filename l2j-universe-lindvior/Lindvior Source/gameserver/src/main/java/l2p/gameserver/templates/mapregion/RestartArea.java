package l2p.gameserver.templates.mapregion;

import l2p.gameserver.model.Territory;
import l2p.gameserver.model.base.Race;

import java.util.Map;

public class RestartArea implements RegionData {
    private final Territory _territory;
    private final Map<Race, RestartPoint> _restarts;

    public RestartArea(Territory territory, Map<Race, RestartPoint> restarts) {
        _territory = territory;
        _restarts = restarts;
    }

    @Override
    public Territory getTerritory() {
        return _territory;
    }

    public Map<Race, RestartPoint> getRestartPoint() {
        return _restarts;
    }
}
