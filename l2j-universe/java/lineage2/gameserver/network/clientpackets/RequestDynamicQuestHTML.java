package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.dynamic.DynamicQuestController;

public class RequestDynamicQuestHTML extends L2GameClientPacket
{
    private int id;
    private int step;

    @Override
    protected void readImpl()
    {
        id = readD();
        step = readD();
    }

    @Override
    protected void runImpl()
    {
        Player player = getClient().getActiveChar();

        if (player == null)
            return;

        DynamicQuestController.getInstance().requestDynamicQuestHtml(id, step, player);
    }
}