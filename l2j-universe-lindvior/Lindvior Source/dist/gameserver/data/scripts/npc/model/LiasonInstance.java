package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 07.11.12
 * Time: 21:24
 */
public class LiasonInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public LiasonInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    /*  private final static int[][] _mageBuff = new int[][]{ //TODO Реализовать бафф
            // minlevel maxlevel skill skilllevel
            {1, 75, 4322, 1}, // windwalk
            {1, 75, 4323, 1}, // shield
            {1, 75, 5637, 1}, // Magic Barrier 1
            {1, 75, 4328, 1}, // blessthesoul
            {1, 75, 4329, 1}, // acumen
            {1, 75, 4330, 1}, // concentration
            {1, 75, 4331, 1}, // empower
            {16, 34, 4338, 1}, // life cubic
    };

    private final static int[][] _warrBuff = new int[][]{
            // minlevel maxlevel skill
            {1, 75, 4322, 1}, // windwalk
            {1, 75, 4323, 1}, // shield
            {1, 75, 5637, 1}, // Magic Barrier 1
            {1, 75, 4324, 1}, // btb
            {1, 75, 4325, 1}, // vampirerage
            {1, 75, 4326, 1}, // regeneration
            {1, 39, 4327, 1}, // haste 1
            {40, 75, 5632, 1}, // haste 2
            {16, 34, 4338, 1}, // life cubic
    };

    private final static int[][] _summonBuff = new int[][]{
            // minlevel maxlevel skill
            {1, 75, 4322, 1}, // windwalk
            {1, 75, 4323, 1}, // shield
            {1, 75, 5637, 1}, // Magic Barrier 1
            {1, 75, 4324, 1}, // btb
            {1, 75, 4325, 1}, // vampirerage
            {1, 75, 4326, 1}, // regeneration
            {1, 75, 4328, 1}, // blessthesoul
            {1, 75, 4329, 1}, // acumen
            {1, 75, 4330, 1}, // concentration
            {1, 75, 4331, 1}, // empower
            {1, 39, 4327, 1}, // haste 1
            {40, 75, 5632, 1}, // haste 2
    };*/
    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("tele_cruma")) {
            player.teleToLocation(17225, 114173, -3440);
        }
        if (command.equalsIgnoreCase("tele_insolence")) {
            player.teleToLocation(114649, 11115, -5120);
        }
        if (command.equalsIgnoreCase("buff_char")) {
            player.sendMessage("Временно не реализовано.");
        }
        if (command.equalsIgnoreCase("buff_summon")) {
            player.sendMessage("Временно не реализовано.");
        } else
            super.onBypassFeedback(player, command);
    }
}
