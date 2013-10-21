package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * 
 * @author KilRoyS
 * 33560, 33561, 33562, 33563, 33564, 33565, 33566, 33567, 33568, 33569 - Hermunkus teleport NPC
 */
public class HermunkusInstance extends NpcInstance
{
	private static final long serialVersionUID = 6626658591714594410L;

	public HermunkusInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;
		
		if(command.startsWith("awakingteleport"))
		{
			if(player.isAwaking())
			{
				if((player.getLevel() < 90) && (player.getLevel() >= 85))
					showChatWindow(player, "teleporter/hermunkus-85.htm");
				if(player.getLevel() >= 90)
					showChatWindow(player, "teleporter/hermunkus-90.htm");
			}
			else
				showChatWindow(player, "teleporter/hermunkus-noawakening.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}
}