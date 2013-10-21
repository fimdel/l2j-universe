package npc.model;

import instances.SpezionNormal;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author cruel
 */
public final class SpezionNpcInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpezionNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("normal_spezion"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(159))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(159))
				ReflectionUtils.enterReflection(player, new SpezionNormal(), 159);
		}
		else
			super.onBypassFeedback(player, command);
	}
}