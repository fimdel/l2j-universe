package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.SkillCoolTime;

public class RequestSkillCoolTime extends L2GameClientPacket {
    @Override
    protected void readImpl() {

    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.sendPacket(new SkillCoolTime(player));
    }
}