package ai.HarnakUndeground;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;

public class Hermuncus extends DefaultAI
{
	private final boolean LAST_SPAWN;

	public Hermuncus(NpcInstance actor)
	{
		super(actor);
		LAST_SPAWN = actor.getParameter("lastSpawn", false);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if(!LAST_SPAWN)
			getActor().setNpcState(1);
	}

	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
		if(ask == 10338 && reply == 2)
		{
			player.teleToLocation(-114962, 226564, -2864, ReflectionManager.DEFAULT);
			player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_VIEW);
		}
	}
}
