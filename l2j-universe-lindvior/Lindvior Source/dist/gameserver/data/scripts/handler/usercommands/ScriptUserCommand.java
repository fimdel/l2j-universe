package handler.usercommands;

import l2p.gameserver.handler.usercommands.IUserCommandHandler;
import l2p.gameserver.handler.usercommands.UserCommandHandler;
import l2p.gameserver.scripts.ScriptFile;

/**
 * @author VISTALL
 * @date 16:53/24.06.2011
 */
public abstract class ScriptUserCommand implements IUserCommandHandler, ScriptFile {
    @Override
    public void onLoad() {
        UserCommandHandler.getInstance().registerUserCommandHandler(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onShutdown() {

    }
}