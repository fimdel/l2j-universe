package l2p.gameserver.listener.game;

import l2p.gameserver.listener.GameListener;

public interface OnShutdownListener extends GameListener {
    public void onShutdown();
}
