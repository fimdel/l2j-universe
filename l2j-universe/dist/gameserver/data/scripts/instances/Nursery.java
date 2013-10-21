package instances;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

public class Nursery extends Reflection
{
	private int Creature1 = 23033;
	private int Creature2 = 23034;
	private int Creature3 = 23035;
	private int Creature4 = 23036;
	private int Creature5 = 23037;
	private int reward;
	NpcInstance tuy;
	private DeathListener _deathListener = new DeathListener();

	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc())
			{
				if(self.getNpcId() == Creature1 || self.getNpcId() == Creature2 || self.getNpcId() == Creature3 || self.getNpcId() == Creature4 || self.getNpcId() == Creature5)
				{
					int count = reward + Rnd.get(15);
				}
			}
		}
	}
}