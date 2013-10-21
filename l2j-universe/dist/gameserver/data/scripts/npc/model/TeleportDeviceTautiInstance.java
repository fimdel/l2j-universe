package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author KilRoy
 * @reworked Awakeninger
 */
public class TeleportDeviceTautiInstance extends NpcInstance
{
	private static final long serialVersionUID = 8347875591381814256L;
	private boolean accepted = false;
	private static final int KEY_OF_DARKNESS = 34899;
	private static final Location TAUTI_ROOM_HALL = new Location(-149244, 209882, -10199);

	public TeleportDeviceTautiInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_accept_tauti"))
		{
			/*if(player.getParty() == null)
			{
				player.sendPacket(new SystemMessage(SystemMessage.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER));
				return;
			}
			if(player.getParty().getCommandChannel() == null)
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_IN_A_CURRENT_COMMAND_CHANNEL));
				return;
			}
			if(!accepted && !player.getParty().getCommandChannel().isLeaderCommandChannel(player) && !player.getParty().isLeader(player))
			{
				showChatWindow(player, "default/33678-no.htm");
				return;
			}*/
			if(player.getInventory().getItemByItemId(KEY_OF_DARKNESS) != null && !accepted)
			{
				player.getInventory().destroyItemByItemId(KEY_OF_DARKNESS, 1);
				setNpcState(1);
				accepted = true;
				//for(Player party : player.getParty().getPartyMembers())
				//{
					//party.teleToLocation(TAUTI_ROOM_HALL, player.getReflection());
				//}
				for(Player party : GameObjectsStorage.getAllPlayersForIterate())
				{
					party.teleToLocation(TAUTI_ROOM_HALL, player.getReflection());
				}
			}
			else
				showChatWindow(player, "default/33678-nokey.htm");
		}
		else if(command.equalsIgnoreCase("request_tauti"))
		{
			//if(player.getParty().isLeader(player))
			//{
				for(Player party : player.getParty().getPartyMembers())
				{
					party.teleToLocation(TAUTI_ROOM_HALL, player.getReflection());
				}
			//}
			//else
				//player.sendPacket(new SystemMessage(SystemMessage.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER));
		}
		else
			super.onBypassFeedback(player, command);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if(!accepted)
		{
			player.sendPacket(new NpcHtmlMessage(player, this, "default/33678.htm", val));
			return;
		}
		else
		{
			player.sendPacket(new NpcHtmlMessage(player, this, "default/33678-1.htm", val));
			return;
		}
	}
}