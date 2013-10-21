package npc.model;

import instances.SpezionNormal;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cruel
 */
public final class PortalCubeInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<Integer, Integer> players = new HashMap<Integer, Integer>();
	public PortalCubeInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("register"))
		{
			players.put(player.getObjectId(), player.getObjectId());
		} else if (command.equalsIgnoreCase("exit"))
		{
			for(Player p : ((SpezionNormal) getReflection()).getPlayers()) {
				if (players.get(p.getObjectId()) == null)
					return;
				players.clear();
				((SpezionNormal) getReflection()).SecondRoom();
			}
		}
		else if (command.equalsIgnoreCase("opengate"))
		{
			if (getNpcId() == 32951)
				((SpezionNormal) getReflection()).openGate(26190001);
			else if (getNpcId() == 32952)
				((SpezionNormal) getReflection()).openGate(26190006);
			else if (getNpcId() == 32953)
				((SpezionNormal) getReflection()).openGate(26190005);
		}
		else if (command.equalsIgnoreCase("stage_third"))
		{
				((SpezionNormal) getReflection()).thirdStage();
		}
		else if (command.equalsIgnoreCase("spawn_spezion"))
		{
				((SpezionNormal) getReflection()).spazionSpawn();
		}
		else
			super.onBypassFeedback(player, command);
	}
}