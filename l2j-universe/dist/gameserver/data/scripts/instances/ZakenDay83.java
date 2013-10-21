package instances;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 *
 * @author pchayka
 */

public class ZakenDay83 extends Reflection
{
	private static final int Anchor = 32468;
	private static final int UltraDayZaken = 29181;
	private static Location[] zakenTp = { new Location(55272, 219080, -2952), new Location(55272, 219080, -3224), new Location(55272, 219080, -3496), };
	private static Location zakenSpawn = new Location(55048, 216808, -3772);
	private DeathListener _deathListener = new DeathListener();
	private long _savedTime;

	@Override
	protected void onCreate()
	{
		super.onCreate();
		addSpawnWithoutRespawn(Anchor, zakenTp[Rnd.get(zakenTp.length)], 0);
		NpcInstance zaken = addSpawnWithoutRespawn(UltraDayZaken, zakenSpawn, 0);
		zaken.addListener(_deathListener);
		zaken.setIsInvul(true);
		_savedTime = System.currentTimeMillis();
	}

	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcString.ELAPSED_TIME));
	}

	@Override
	public void onPlayerExit(Player player)
	{
		super.onPlayerExit(player);
		player.sendPacket(new ExSendUIEvent(player, 1, 1, 0, 0));
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc() && self.getNpcId() == UltraDayZaken)
			{
				long _timePassed = System.currentTimeMillis() - _savedTime;
				for(Player p : getPlayers())
					if(_timePassed < 5 * 60 * 1000)
					{
						if(Rnd.chance(50))
							ItemFunctions.addItem(p, 15763, 1, true);
					}
					else if(_timePassed < 10 * 60 * 1000)
					{
						if(Rnd.chance(30))
							ItemFunctions.addItem(p, 15764, 1, true);
					}
					else if(_timePassed < 15 * 60 * 1000)
						if(Rnd.chance(25))
							ItemFunctions.addItem(p, 15763, 1, true);
				for(Player p : getPlayers())
					p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
			}
		}
	}
}