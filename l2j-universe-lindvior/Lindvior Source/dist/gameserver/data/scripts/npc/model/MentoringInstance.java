/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.data.xml.holder.MultiSellHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.npc.NpcTemplate;

public class MentoringInstance extends NpcInstance {
    private static final long serialVersionUID = 1L;
    private static int MENTEE_CERTIFICATE = 33800;
    private static int DIPLOMA = 33805;

    public MentoringInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }
        if (command.startsWith("change_certificate")) {
            if (player.getInventory().getCountOf(MENTEE_CERTIFICATE) > 0) {
                player.getInventory().destroyItemByItemId(MENTEE_CERTIFICATE, 1);
                Functions.addItem(player, DIPLOMA, 40);
                showChatWindow(player, "mentoring/mentor_helper.htm");
                return;
            }

            showChatWindow(player, "mentoring/mentor_helper-certificate-no.htm");
            return;
        }

        if (command.startsWith("use_certificate")) {
            if (player.getClassId().isOfLevel(ClassLevel.AWAKED)) {
                MultiSellHolder.getInstance().SeparateAndSend(3358702, player, 0.0);
                return;
            }

            showChatWindow(player, "mentoring/mentor_helper-reward-no.htm");
            return;
        }

        super.onBypassFeedback(player, command);
    }

    @Override
    public String getHtmlPath(int npcId, int val, Player player) {
        if (val == 0)
            return "mentoring/mentor_helper.htm";
        return "mentoring/mentor_helper-" + val + ".htm";
    }
}