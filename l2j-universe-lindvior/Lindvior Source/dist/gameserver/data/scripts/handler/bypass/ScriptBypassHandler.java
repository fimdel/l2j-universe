package handler.bypass;

import l2p.gameserver.handler.test.BypassHolder;
import l2p.gameserver.handler.test.IBypassHandler;
import l2p.gameserver.scripts.ScriptFile;

/**
 * @author VISTALL
 * @date 15:53/12.07.2011
 */
public abstract class ScriptBypassHandler implements ScriptFile, IBypassHandler {
    @Override
    public void onShutdown() {
    }

    @Override
    public void onLoad() {
        BypassHolder.getInstance().registerBypass(this);
    }

    @Override
    public void onReload() {
    }
}
