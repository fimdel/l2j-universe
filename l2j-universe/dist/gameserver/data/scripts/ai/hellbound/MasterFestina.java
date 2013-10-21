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
package ai.hellbound;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MasterFestina extends Fighter
{
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field _mysticSpawnPoints.
	 */
	private static Location[] _mysticSpawnPoints;
	/**
	 * Field _spiritGuardSpawnPoints.
	 */
	private static Location[] _spiritGuardSpawnPoints;
	/**
	 * Field FOUNDRY_MYSTIC_ID. (value is 22387)
	 */
	private final static int FOUNDRY_MYSTIC_ID = 22387;
	/**
	 * Field FOUNDRY_SPIRIT_GUARD_ID. (value is 22389)
	 */
	private final static int FOUNDRY_SPIRIT_GUARD_ID = 22389;
	/**
	 * Field lastFactionNotifyTime.
	 */
	private long lastFactionNotifyTime = 0;
	
	/**
	 * Constructor for MasterFestina.
	 * @param actor NpcInstance
	 */
	public MasterFestina(NpcInstance actor)
	{
		super(actor);
		_zone = ReflectionUtils.getZone("[tully2]");
		_mysticSpawnPoints = new Location[]
		{
			new Location(-11480, 273992, -11768),
			new Location(-11128, 273992, -11864),
			new Location(-10696, 273992, -11936),
			new Location(-12552, 274920, -11752),
			new Location(-12568, 275320, -11864),
			new Location(-12568, 275784, -11936),
			new Location(-13480, 273880, -11752),
			new Location(-13880, 273880, -11864),
			new Location(-14328, 273880, -11936),
			new Location(-12456, 272968, -11752),
			new Location(-12456, 272552, -11864),
			new Location(-12456, 272120, -11936)
		};
		_spiritGuardSpawnPoints = new Location[]
		{
			new Location(-12552, 272168, -11936),
			new Location(-12552, 272520, -11872),
			new Location(-12552, 272984, -11744),
			new Location(-13432, 273960, -11736),
			new Location(-13864, 273960, -11856),
			new Location(-14296, 273976, -11936),
			new Location(-12504, 275736, -11936),
			new Location(-12472, 275288, -11856),
			new Location(-12472, 274888, -11744),
			new Location(-11544, 273912, -11752),
			new Location(-11160, 273912, -11856),
			new Location(-10728, 273896, -11936)
		};
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance actor = getActor();
		for (Location loc : _mysticSpawnPoints)
		{
			MonsterInstance mob = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(FOUNDRY_MYSTIC_ID));
			mob.setSpawnedLoc(loc);
			mob.setReflection(actor.getReflection());
			mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp(), true);
			mob.spawnMe(mob.getSpawnedLoc());
		}
		for (Location loc : _spiritGuardSpawnPoints)
		{
			MonsterInstance mob = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(FOUNDRY_SPIRIT_GUARD_ID));
			mob.setSpawnedLoc(loc);
			mob.setReflection(actor.getReflection());
			mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp(), true);
			mob.spawnMe(mob.getSpawnedLoc());
		}
		setZoneInactive();
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((System.currentTimeMillis() - lastFactionNotifyTime) > _minFactionNotifyInterval)
		{
			lastFactionNotifyTime = System.currentTimeMillis();
			for (NpcInstance npc : actor.getAroundNpc(3000, 500))
			{
				if ((npc.getNpcId() == FOUNDRY_MYSTIC_ID) || (npc.getNpcId() == FOUNDRY_SPIRIT_GUARD_ID))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 100));
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		lastFactionNotifyTime = 0;
		for (NpcInstance npc : actor.getAroundNpc(3000, 500))
		{
			if ((npc.getNpcId() == FOUNDRY_MYSTIC_ID) || (npc.getNpcId() == FOUNDRY_SPIRIT_GUARD_ID))
			{
				npc.deleteMe();
			}
		}
		setZoneActive();
		super.onEvtDead(killer);
	}
	
	/**
	 * Method setZoneActive.
	 */
	private void setZoneActive()
	{
		_zone.setActive(true);
	}
	
	/**
	 * Method setZoneInactive.
	 */
	private void setZoneInactive()
	{
		_zone.setActive(false);
	}
}
