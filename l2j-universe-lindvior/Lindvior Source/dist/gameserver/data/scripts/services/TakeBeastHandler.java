package services;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

public class TakeBeastHandler extends Functions {
    private final int BEAST_WHIP = 15473;

    public void show() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (player.getLevel() < 82)
            htmltext = npc.getNpcId() + "-1.htm";
        else if (Functions.getItemCount(player, BEAST_WHIP) > 0)
            htmltext = npc.getNpcId() + "-2.htm";
        else {
            Functions.addItem(player, BEAST_WHIP, 1);
            htmltext = npc.getNpcId() + "-3.htm";
        }

        npc.showChatWindow(player, "default/" + htmltext);
    }
}
