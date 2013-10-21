package ai;

import java.util.concurrent.ScheduledFuture;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.commons.threading.RunnableImpl;

/**
 * @author cruel
 */
public class SpezionBoss extends Fighter
{
	private ScheduledFuture<?> DeadTask;
	public SpezionBoss(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		NpcInstance actor = getActor();
		DeadTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnMinion(),1000,30000);
		Reflection r = actor.getReflection();
		for(Player p : r.getPlayers())
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		super.onEvtSpawn();
		Skill fp = SkillTable.getInstance().getInfo(14190, 1);
		fp.getEffects(actor, actor, false, false);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		if(DeadTask != null)
			DeadTask.cancel(true);

		super.onEvtDead(killer);
	}
	
	public class SpawnMinion extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			NpcInstance minion = actor.getReflection().addSpawnWithoutRespawn(25780, actor.getLoc(), 250);
			for(Player p : actor.getReflection().getPlayers())
				minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		}
	}
}
