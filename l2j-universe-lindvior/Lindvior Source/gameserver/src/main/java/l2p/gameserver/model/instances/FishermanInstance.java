package l2p.gameserver.model.instances;

import l2p.gameserver.Config;
import l2p.gameserver.instancemanager.games.FishingChampionShipManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author n0nam3
 * @date 08/08/2010 16:22
 */
public class FishermanInstance extends MerchantInstance {
    /**
     *
     */
    private static final long serialVersionUID = 3896294009093378396L;

    public FishermanInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public String getHtmlPath(int npcId, int val, Player player) {
        String pom = "";

        if (val == 0)
            pom = "" + npcId;
        else
            pom = npcId + "-" + val;

        return "fisherman/" + pom + ".htm";
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;
        if (command.equalsIgnoreCase("FishingSkillList"))
            showFishingSkillList(player);
        else if (command.startsWith("FishingChampionship") && Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
            FishingChampionShipManager.getInstance().showChampScreen(player, this);
        else if (command.startsWith("FishingReward") && Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
            FishingChampionShipManager.getInstance().getReward(player);
        else
            super.onBypassFeedback(player, command);
    }
}