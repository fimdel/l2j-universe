package npc.model;

import instances.OctavisNormal;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy
 * 32892 (Tipia)
 * TODO[K]: Hard mode Octavis
 */
public final class TipiaNpcInstance extends NpcInstance
{
	private static final long serialVersionUID = 5984176213940365077L;
	private static final int normalOctavisInstId = 180;
	private static final int hardOctavisInstId = 181;

	public TipiaNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_normaloctavis"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(normalOctavisInstId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(normalOctavisInstId))
				ReflectionUtils.enterReflection(player, new OctavisNormal(), normalOctavisInstId);
		}

		else if(command.equalsIgnoreCase("request_hardoctavis"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(hardOctavisInstId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(hardOctavisInstId))
				ReflectionUtils.enterReflection(player, new OctavisNormal(), hardOctavisInstId);
		}

		else
			super.onBypassFeedback(player, command);
	}
}