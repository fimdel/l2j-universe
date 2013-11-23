package l2p.gameserver.model.instances;

import l2p.gameserver.model.Player;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class TrainerInstance extends NpcInstance // deprecated?
{
    /**
     *
     */
    private static final long serialVersionUID = -4246814888789247768L;

    public TrainerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public String getHtmlPath(int npcId, int val, Player player) {
        String pom = "";
        if (val == 0)
            pom = "" + npcId;
        else
            pom = npcId + "-" + val;

        return "trainer/" + pom + ".htm";
    }
}
