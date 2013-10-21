package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public final class ArcanCatInstance extends NpcInstance
{
	private static final long serialVersionUID = 7456493552671131686L;

	public ArcanCatInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("start_movie"))
		{
			player.showQuestMovie(104);
		}
		else
			super.onBypassFeedback(player, command);
	}
}