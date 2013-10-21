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

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Sandstorm extends DefaultAI
{
	/**
	 * Field AGGRO_RANGE. (value is 200)
	 */
	private static final int AGGRO_RANGE = 200;
	/**
	 * Field SKILL1.
	 */
	private static final Skill SKILL1 = SkillTable.getInstance().getInfo(5435, 1);
	/**
	 * Field SKILL2.
	 */
	private static final Skill SKILL2 = SkillTable.getInstance().getInfo(5494, 1);
	/**
	 * Field lastThrow.
	 */
	private long lastThrow = 0;
	
	/**
	 * Constructor for Sandstorm.
	 * @param actor NpcInstance
	 */
	public Sandstorm(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((lastThrow + 5000) < System.currentTimeMillis())
		{
			for (Playable target : World.getAroundPlayables(actor, AGGRO_RANGE, AGGRO_RANGE))
			{
				if ((target != null) && !target.isAlikeDead() && !target.isInvul() && target.isVisible() && GeoEngine.canSeeTarget(actor, target, false))
				{
					actor.doCast(SKILL1, target, true);
					actor.doCast(SKILL2, target, true);
					lastThrow = System.currentTimeMillis();
					break;
				}
			}
		}
		return super.thinkActive();
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		// empty method
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		// empty method
	}
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked_member Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		final NpcInstance actor = getActor();
		final Location sloc = actor.getSpawnedLoc();
		final Location pos = Location.findPointToStay(actor, sloc, 150, 300);
		if (GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
		{
			actor.setRunning();
			addTaskMove(pos, false);
		}
		return true;
	}
}
