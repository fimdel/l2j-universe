package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;
import instances.FortunaInstance;

public final class IzaelManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fortunaId = 179;

	public IzaelManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equalsIgnoreCase("enter"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(fortunaId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if (player.canEnterInstance(fortunaId))
			{
				ReflectionUtils.enterReflection(player, new FortunaInstance(), fortunaId);
			}
		}
		super.onBypassFeedback(player, command);
	}
}