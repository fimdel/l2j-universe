package npc.model;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy
 */
public final class GardenDoorInstance extends NpcInstance
{
	private static final long serialVersionUID = 4953313362819211890L;

	private static final int APHROS_KEY = 17373;

	private static final int APHROS = 25866;	//RAID
	private static final int GUARDIAN = 25776;
	private static final int ANGEL_STATUE_KEEPER = 23038;
	private static final int FOUNTAIN_KEEPER = 23039;
	private static final int GODDESS_STATUE_KEEPER = 23040;
	private static long timeRespwn1 = 0;
	private static long timeRespwn2 = 0;
	private static long timeRespwn3 = 0;
	private static long timeRespwn4 = 0;

	private static final int[] DOORS = {26210041, 26210042, 26210043, 26210044};

	public GardenDoorInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("aphros_door_open"))
		{
			if(timeRespwn1 == 0)
				timeRespwn1 = System.currentTimeMillis();
			if(player.getInventory().getItemByItemId(APHROS_KEY) == null)
				showChatWindow(player, "aphros/aphros_door_nokey.htm");
			else
			{
				if(getNpcId() == 33133 && System.currentTimeMillis() >= timeRespwn1)
				{
					if(Rnd.chance(50))
					{
						showChatWindow(player, "aphros/aphros_door_ok.htm");
						player.getInventory().removeItemByObjectId(APHROS_KEY, 1);
						timeRespwn1 = System.currentTimeMillis() + 14400000;
						NpcUtils.spawnSingle(APHROS, new Location(213732, 115288, -856, 0));
						ThreadPoolManager.getInstance().schedule(new runDoorOpener(), 10000L);
					}
					else
					{
						showChatWindow(player, "aphros/aphros_door_wrongkey.htm");

						for(int i = 0; i < 3; i++)
						{
							NpcInstance guardian = NpcUtils.spawnSingle(GUARDIAN, new Location(player.getX() - Rnd.get(20), player.getY() - Rnd.get(20), player.getZ(), 0), 1800000);
							guardian.getAggroList().addDamageHate(player, 0, 10000);
							guardian.setAggressionTarget(player);
						}
					}
				}
				else
					showChatWindow(player, "aphros/aphros_door_chekedno.htm");
			}
		}
		else if(command.equalsIgnoreCase("garden_door_angel"))
		{
			if(timeRespwn2 == 0)
				timeRespwn2 = System.currentTimeMillis();
			if(System.currentTimeMillis() >= timeRespwn2)
			{
				timeRespwn2 = System.currentTimeMillis() + 14400000;
				NpcInstance angel = NpcUtils.spawnSingle(ANGEL_STATUE_KEEPER, new Location(player.getX() - Rnd.get(20), player.getY() - Rnd.get(20), player.getZ(), 0), 14400000L);
				angel.getAggroList().addDamageHate(player, 0, 10000);
				angel.setAggressionTarget(player);
			}
			else
				showChatWindow(player, "aphros/keeper_no_respawn.htm");
		}
		else if(command.equalsIgnoreCase("garden_door_fountain"))
		{
			if(timeRespwn3 == 0)
				timeRespwn3 = System.currentTimeMillis();
			if(System.currentTimeMillis() >= timeRespwn3)
			{
				timeRespwn3 = System.currentTimeMillis() + 14400000;
				NpcInstance fountain = NpcUtils.spawnSingle(FOUNTAIN_KEEPER, new Location(player.getX() - Rnd.get(20), player.getY() - Rnd.get(20), player.getZ(), 0), 14400000L);
				fountain.getAggroList().addDamageHate(player, 0, 10000);
				fountain.setAggressionTarget(player);
			}
			else
				showChatWindow(player, "aphros/keeper_no_respawn.htm");
		}
		else if(command.equalsIgnoreCase("garden_door_statue"))
		{
			if(timeRespwn4 == 0)
				timeRespwn4 = System.currentTimeMillis();
			if(System.currentTimeMillis() >= timeRespwn4)
			{
				timeRespwn4 = System.currentTimeMillis() + 14400000;
				NpcInstance statue = NpcUtils.spawnSingle(GODDESS_STATUE_KEEPER, new Location(player.getX() - Rnd.get(20), player.getY() - Rnd.get(20), player.getZ(), 0), 14400000L);
				statue.getAggroList().addDamageHate(player, 0, 10000);
				statue.setAggressionTarget(player);
			}
			else
				showChatWindow(player, "aphros/keeper_no_respawn.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	private class runDoorOpener extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			for(int doorID : DOORS)
			{
				DoorInstance door = ReflectionUtils.getDoor(doorID);
				door.openMe();
			}
		}
	}
}