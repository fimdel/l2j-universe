package npc.model;

import instances.TautiNormal;

import lineage2.gameserver.instancemanager.SoHManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy
 * FIXME[K] - Deprecated method getMembers()
 */
public class SizrakInstance extends NpcInstance
{
	private static final long serialVersionUID = -5672768757660962094L;
	private static final int normalTautiInstanceId = 218;
	private static final Location TAUTI_ROOM_TELEPORT = new Location(-147262, 211318, -10040);

	public SizrakInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.startsWith("request_tauti_normal_battle"))
		{
			if(SoHManager.getCurrentStage() != 2)
			{
				showChatWindow(player, "tauti/sofa_sizraku002h.htm");
				return;
			}
			if(player.getParty() == null)
			{
				player.sendPacket(new SystemMessage(SystemMessage.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER));
				return;
			}
			if(player.getParty().getCommandChannel() == null)
			{
				showChatWindow(player, "tauti/sofa_sizraku002e.htm");
				return;
			}
			if(!player.getParty().getCommandChannel().isLeaderCommandChannel(player))
			{
				showChatWindow(player, "tauti/sofa_sizraku002d.htm");
				return;
			}
			if(player.getParty().getCommandChannel().getMemberCount() > 35)
			{
				showChatWindow(player, "tauti/sofa_sizraku002c.htm");
				return;
			}
			for(Player commandChannel : player.getParty().getCommandChannel().getMembers())
			{
				if(commandChannel.getLevel() < 97)
					showChatWindow(player, "tauti/sofa_sizraku002b.htm");
				return;
			}

			Reflection reflection = player.getActiveReflection();
			if(reflection != null)
			{
				if(player.canReenterInstance(normalTautiInstanceId))
					showChatWindow(player, "tauti/sofa_sizraku002g.htm");
			}
			else if(player.canEnterInstance(normalTautiInstanceId))
			{
				ReflectionUtils.enterReflection(player, new TautiNormal(), normalTautiInstanceId);
				showChatWindow(player, "tauti/sofa_sizraku002a.htm");
			}
		}
		if(command.startsWith("reenter_tauti_normal_battle"))
		{
			Reflection reflection = player.getActiveReflection();
			if(reflection != null)
			{
				if(player.canReenterInstance(normalTautiInstanceId))
				{
					TautiNormal instance = (TautiNormal) reflection;
					if(instance.getInstanceStage() == 2)
						player.teleToLocation(TAUTI_ROOM_TELEPORT, reflection);
					else
						player.teleToLocation(reflection.getTeleportLoc(), reflection);
					showChatWindow(player, "tauti/sofa_sizraku002f.htm");
				}
			}
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		player.sendPacket(new NpcHtmlMessage(player, this, "tauti/sofa_sizraku001.htm", val));
		return;
	}
}