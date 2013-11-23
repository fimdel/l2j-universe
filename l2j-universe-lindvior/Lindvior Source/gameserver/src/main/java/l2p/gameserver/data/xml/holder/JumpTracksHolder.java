package l2p.gameserver.data.xml.holder;

import gnu.trove.TIntObjectHashMap;
import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.templates.jump.JumpTrack;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public final class JumpTracksHolder extends AbstractHolder {
    private static final JumpTracksHolder _instance = new JumpTracksHolder();

    private TIntObjectHashMap<JumpTrack> _jumpingTracks = new TIntObjectHashMap<JumpTrack>();

    public static JumpTracksHolder getInstance() {
        return _instance;
    }

    public void addTrack(JumpTrack track) {
        _jumpingTracks.put(track.getId(), track);
    }

    public JumpTrack getTrack(int id) {
        return _jumpingTracks.get(id);
    }

    @Override
    public int size() {
        return _jumpingTracks.size();
    }

    @Override
    public void clear() {
        _jumpingTracks.clear();
    }
}
