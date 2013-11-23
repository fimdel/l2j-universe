package l2p.gameserver.model.instances;

import l2p.gameserver.model.Player;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 23:18/12.12.2010
 */
public class BlockInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -2956205438888741014L;
    private boolean _isRed;

    public BlockInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public boolean isRed() {
        return _isRed;
    }

    public void setRed(boolean red) {
        _isRed = red;
        broadcastCharInfo();
    }

    public void changeColor() {
        setRed(!_isRed);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
    }

    @Override
    public boolean isNameAbove() {
        return false;
    }

    @Override
    public int getFormId() {
        return _isRed ? 0x53 : 0;
    }

    @Override
    public boolean isInvul() {
        return true;
    }
}
