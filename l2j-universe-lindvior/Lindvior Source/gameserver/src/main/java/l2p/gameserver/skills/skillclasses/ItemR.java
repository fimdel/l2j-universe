package l2p.gameserver.skills.skillclasses;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.StatsSet;

import java.util.List;


public class ItemR extends Skill {
    public ItemR(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (activeChar == null)
            return;
        Player player = null;
        if (activeChar instanceof Player)
            player = activeChar.getPlayer();

        if (player != null)
            if (_item_r1 != 0 && _item_r2 != 0 && _item_r3 != 0) {      //For R Grade
                player.getInventory().destroyItemByItemId(_item_del, 1);
                player.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addItemName(_item_del));

                if (Rnd.chance(90)) {
                    player.getInventory().addItem(_item_r1, 1);
                    player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r1));
                } else if (Rnd.chance(2)) {
                    player.getInventory().addItem(_item_r3, 1);
                    player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r3));
                } else {
                    player.getInventory().addItem(_item_r2, 1);
                    player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r2));
                }
            } else if (_item_r1 != 0 && _item_r2 != 0) {              //For D C B A S Grade
                player.getInventory().destroyItemByItemId(_item_del, 1);
                player.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addItemName(_item_del));

                if (Rnd.chance(90)) {
                    player.getInventory().addItem(_item_r1, 1);
                    player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r1));
                } else {
                    player.getInventory().addItem(_item_r2, 1);
                    player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_item_r2));
                }
            } else
                player.sendMessage("Данный итем не реализован, ожидайте.");
    }
}
