package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.ShortCut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShortCutInit extends ShortCutPacket {
    private List<ShortcutInfo> _shortCuts = Collections.emptyList();

    public ShortCutInit(Player pl) {
        Collection<ShortCut> shortCuts = pl.getAllShortCuts();
        _shortCuts = new ArrayList<ShortcutInfo>(shortCuts.size());
        for (ShortCut shortCut : shortCuts)
            _shortCuts.add(convert(pl, shortCut));
    }

    @Override
    protected final void writeImpl() {
        writeC(0x45);
        writeD(_shortCuts.size());

        for (final ShortcutInfo sc : _shortCuts)
            sc.write(this);
    }
}