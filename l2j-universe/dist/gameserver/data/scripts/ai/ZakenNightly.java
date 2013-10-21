/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZakenNightly extends Fighter
{
	/**
	 * Field doll_blader_b. (value is 29023)
	 */
	private static final int doll_blader_b = 29023;
	/**
	 * Field vale_master_b. (value is 29024)
	 */
	private static final int vale_master_b = 29024;
	/**
	 * Field pirates_zombie_captain_b. (value is 29026)
	 */
	private static final int pirates_zombie_captain_b = 29026;
	/**
	 * Field pirates_zombie_b. (value is 29027)
	 */
	private static final int pirates_zombie_b = 29027;
	/**
	 * Field _locations.
	 */
	static final Location[] _locations = new Location[]
	{
		new Location(55272, 219112, -3496),
		new Location(56296, 218072, -3496),
		new Location(54232, 218072, -3496),
		new Location(54248, 220136, -3496),
		new Location(56296, 220136, -3496),
		new Location(55272, 219112, -3224),
		new Location(56296, 218072, -3224),
		new Location(54232, 218072, -3224),
		new Location(54248, 220136, -3224),
		new Location(56296, 220136, -3224),
		new Location(55272, 219112, -2952),
		new Location(56296, 218072, -2952),
		new Location(54232, 218072, -2952),
		new Location(54248, 220136, -2952),
		new Location(56296, 220136, -2952)
	};
	/**
	 * Field _teleportSelfTimer.
	 */
	private long _teleportSelfTimer = 0L;
	/**
	 * Field _teleportSelfReuse.
	 */
	private static final long _teleportSelfReuse = 30000L;
	/**
	 * Field actor.
	 */
	final NpcInstance actor = getActor();
	/**
	 * Field _stage.
	 */
	private int _stage = 0;
	
	/**
	 * Constructor for ZakenNightly.
	 * @param actor NpcInstance
	 */
	public ZakenNightly(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = Integer.MAX_VALUE >> 1;
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		if ((_teleportSelfTimer + _teleportSelfReuse) < System.currentTimeMillis())
		{
			_teleportSelfTimer = System.currentTimeMillis();
			if (Rnd.chance(20))
			{
				actor.doCast(SkillTable.getInstance().getInfo(4222, 1), actor, false);
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						actor.teleToLocation(_locations[Rnd.get(_locations.length)]);
						actor.getAggroList().clear(true);
					}
				}, 500);
			}
		}
		final double actor_hp_precent = actor.getCurrentHpPercents();
		final Reflection r = actor.getReflection();
		switch (_stage)
		{
			case 0:
				if (actor_hp_precent < 90)
				{
					r.addSpawnWithoutRespawn(pirates_zombie_captain_b, actor.getLoc(), 300);
					_stage++;
				}
				break;
			case 1:
				if (actor_hp_precent < 80)
				{
					r.addSpawnWithoutRespawn(doll_blader_b, actor.getLoc(), 300);
					_stage++;
				}
				break;
			case 2:
				if (actor_hp_precent < 70)
				{
					r.addSpawnWithoutRespawn(vale_master_b, actor.getLoc(), 300);
					r.addSpawnWithoutRespawn(vale_master_b, actor.getLoc(), 300);
					_stage++;
				}
				break;
			case 3:
				if (actor_hp_precent < 60)
				{
					for (int i = 0; i < 5; i++)
					{
						r.addSpawnWithoutRespawn(pirates_zombie_b, actor.getLoc(), 300);
					}
					_stage++;
				}
				break;
			case 4:
				if (actor_hp_precent < 50)
				{
					for (int i = 0; i < 5; i++)
					{
						r.addSpawnWithoutRespawn(doll_blader_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(vale_master_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_captain_b, actor.getLoc(), 300);
					}
					_stage++;
				}
				break;
			case 5:
				if (actor_hp_precent < 40)
				{
					for (int i = 0; i < 6; i++)
					{
						r.addSpawnWithoutRespawn(doll_blader_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(vale_master_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_captain_b, actor.getLoc(), 300);
					}
					_stage++;
				}
				break;
			case 6:
				if (actor_hp_precent < 30)
				{
					for (int i = 0; i < 7; i++)
					{
						r.addSpawnWithoutRespawn(doll_blader_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(vale_master_b, actor.getLoc(), 300);
						r.addSpawnWithoutRespawn(pirates_zombie_captain_b, actor.getLoc(), 300);
					}
					_stage++;
				}
				break;
			default:
				break;
		}
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final Reflection r = actor.getReflection();
		r.setReenterTime(System.currentTimeMillis());
		actor.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, actor.getObjectId(), actor.getLoc()));
		super.onEvtDead(killer);
	}
	
	/**
	 * Method teleportHome.
	 */
	@Override
	protected void teleportHome()
	{
		return;
	}
}
