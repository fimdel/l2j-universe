package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExShapeShiftingResult;

public class RequestExCancelShapeShiftingItem extends L2GameClientPacket
{
    @Override
    protected void runImpl()
    {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.setAppearanceStone(null);
        player.setAppearanceExtractItem(null);
        player.sendPacket(ExShapeShiftingResult.FAIL);
    }

    @Override
    protected void readImpl()
    {
    }
}
