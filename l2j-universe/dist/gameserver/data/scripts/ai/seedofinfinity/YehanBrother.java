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
package ai.seedofinfinity;

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class YehanBrother extends Fighter
{
	/**
	 * Field _spawnTimer.
	 */
	private long _spawnTimer = 0;
	/**
	 * Field _minions.
	 */
	private static final int[] _minions = ArrayUtils.createAscendingArray(22509, 22512);
	
	/**
	 * Constructor for YehanBrother.
	 * @param actor NpcInstance
	 */
	public YehanBrother(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		_spawnTimer = System.currentTimeMillis();
	}
	
	/**
	 * Method getBrother.
	 * @return NpcInstance
	 */
	private NpcInstance getBrother()
	{
		final NpcInstance actor = getActor();
		int brotherId = 0;
		if (actor.getNpcId() == 25665)
		{
			brotherId = 25666;
		}
		else if (actor.getNpcId() == 25666)
		{
			brotherId = 25665;
		}
		for (NpcInstance npc : actor.getReflection().getNpcs())
		{
			if (npc.getNpcId() == brotherId)
			{
				return npc;
			}
		}
		return null;
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance actor = getActor();
		final NpcInstance brother = getBrother();
		if (!brother.isDead() && !actor.isInRange(brother, 300))
		{
			actor.altOnMagicUseTimer(getActor(), SkillTable.getInstance().getInfo(6371, 1));
		}
		else
		{
			removeInvul(actor);
		}
		if ((_spawnTimer + 40000) < System.currentTimeMillis())
		{
			_spawnTimer = System.currentTimeMillis();
			final NpcInstance mob = actor.getReflection().addSpawnWithoutRespawn(_minions[Rnd.get(_minions.length)], Location.findAroundPosition(actor, 300), 0);
			mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor.getAggressionTarget(), 1000);
		}
		super.thinkAttack();
	}
	
	/**
	 * Method removeInvul.
	 * @param npc NpcInstance
	 */
	private void removeInvul(NpcInstance npc)
	{
		for (Effect e : npc.getEffectList().getAllEffects())
		{
			if (e.getSkill().getId() == 6371)
			{
				e.exit();
			}
		}
	}
}
