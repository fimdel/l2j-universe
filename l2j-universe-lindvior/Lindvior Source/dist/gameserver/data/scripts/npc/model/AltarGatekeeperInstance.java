/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExSendUIEvent;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class AltarGatekeeperInstance extends NpcInstance {
    private static final int DoorEnter1 = 25180001;
    private static final int DoorEnter2 = 25180002;
    private static final int DoorEnter3 = 25180003;
    private static final int DoorEnter4 = 25180004;
    private static final int DoorEnter5 = 25180005;
    private static final int DoorEnter6 = 25180006;
    private static final int DoorEnter7 = 25180007;
    private long _savedTime;
    //DoorInstance _door1 = getReflection().getDoor(DoorEnter1);


    public AltarGatekeeperInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("start1")) {
            _savedTime = System.currentTimeMillis();
            DoorInstance a_door1 = getReflection().getDoor(DoorEnter1);

            player.sendPacket(new ExSendUIEvent(player, 0, 0, 15 * 60, 0, NpcString.ELAPSED_TIME));
            a_door1.openMe();
        } else if (command.startsWith("start2")) {
            _savedTime = System.currentTimeMillis();
            DoorInstance a_door2 = getReflection().getDoor(DoorEnter2);

            player.sendPacket(new ExSendUIEvent(player, 0, 0, 10 * 60, 0, NpcString.ELAPSED_TIME));
            a_door2.openMe();
        } else if (command.startsWith("start3")) {
            _savedTime = System.currentTimeMillis(); // ToDo


            DoorInstance a_door1 = getReflection().getDoor(DoorEnter1);
            a_door1.openMe();
        } else
            super.onBypassFeedback(player, command);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... replace) {
        NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());

        if (player.getVar("Altar1") != null) {
            htmlMessage.setFile("default/32798-1.htm");
        }
        if (player.getVar("Altar2") != null) {
            htmlMessage.setFile("default/32798-2.htm");
        }
        if (player.getVar("Altar3") != null) {
            htmlMessage.setFile("default/32798-3.htm");
        }

        player.sendPacket(htmlMessage);
    }
}