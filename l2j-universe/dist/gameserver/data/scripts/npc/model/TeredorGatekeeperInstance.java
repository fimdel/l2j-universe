package npc.model;

import instances.TeredorCavern;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

public final class TeredorGatekeeperInstance extends NpcInstance
{

	private static final long serialVersionUID = 6518350180076969631L;
	private static final int teredorInstanceId = 160;

	public TeredorGatekeeperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("teredorenterinst"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(teredorInstanceId))
					player.teleToLocation(r.getTeleportLoc(), r);
			}
			else if(player.canEnterInstance(teredorInstanceId))
				ReflectionUtils.enterReflection(player, new TeredorCavern(),teredorInstanceId);
		}
		else
			super.onBypassFeedback(player, command);
	}
}