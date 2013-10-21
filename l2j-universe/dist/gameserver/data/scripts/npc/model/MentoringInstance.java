package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author vegax
 */
public class MentoringInstance extends NpcInstance {
    private static final long serialVersionUID = 907679679965868534L;

    private int MENTEE_CERTIFICATE = 33800;
    private int DIPLOMA = 33805;

    public MentoringInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;
        if (command.startsWith("changediploma")) {
            if(player.isAwaking() && (player.getLevel() >= 85)) 
			{
				if(ItemFunctions.getItemCount(player, MENTEE_CERTIFICATE) == 1)
				{
					ItemFunctions.removeItem(player, MENTEE_CERTIFICATE, 1, true);
					ItemFunctions.addItem(player, DIPLOMA, 40, true);
				} 
				else
				{
                    showChatWindow(player, "mentoring/menthelper-no-diploma.htm");
				}
			}
			else
			{
                showChatWindow(player, "mentoring/menthelper-no-diploma.htm");
			}
        } else
            super.onBypassFeedback(player, command);
    }
}