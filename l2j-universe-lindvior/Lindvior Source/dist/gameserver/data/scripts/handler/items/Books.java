/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package handler.items;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.*;

public class Books extends SimpleItemHandler {
    private static final int[] ITEM_IDS = new int[]{5555, 5707, 32777, 32778, 17725};
    private NpcInstance npc;

    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl) {
        int itemId = item.getItemId();

        NpcHtmlMessage htmlMessage = new NpcHtmlMessage(17819);
        htmlMessage.setFile("quests\17725.htm");
        switch (itemId) {
            case 17725:
                player.sendPacket(htmlMessage);
                break;
            case 17819:
                player.sendPacket(new ExNpcQuestHtmlMessage(17819, 10301));
                break;
            case 5555:
                player.sendPacket(new ShowXMasSeal(5555));
                break;
            case 5707:
                player.sendPacket(new SSQStatus(player, 1));
                break;
            case 32777:
                player.sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE, TutorialShowHtml.TYPE_WINDOW));
                break;
            case 32778:
                player.sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_AWAKING, TutorialShowHtml.TYPE_WINDOW));
                break;
            default:
                return false;
        }

        return true;
    }
}

