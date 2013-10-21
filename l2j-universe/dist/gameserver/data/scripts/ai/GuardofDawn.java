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
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardofDawn extends DefaultAI
{
	/**
	 * Field _aggrorange. (value is 150)
	 */
	private static final int _aggrorange = 150;
	/**
	 * Field _skill.
	 */
	private static final Skill _skill = SkillTable.getInstance().getInfo(5978, 1);
	/**
	 * Field _locStart.
	 */
	private Location _locStart = null;
	/**
	 * Field _locEnd.
	 */
	private Location _locEnd = null;
	/**
	 * Field _locTele.
	 */
	private Location _locTele = null;
	/**
	 * Field moveToEnd.
	 */
	private boolean moveToEnd = true;
	/**
	 * Field noCheckPlayers.
	 */
	boolean noCheckPlayers = false;
	
	/**
	 * Constructor for GuardofDawn.
	 * @param actor NpcInstance
	 * @param locationEnd Location
	 * @param telePoint Location
	 */
	public GuardofDawn(NpcInstance actor, Location locationEnd, Location telePoint)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 200;
		setStartPoint(actor.getSpawnedLoc());
		setEndPoint(locationEnd);
		setTelePoint(telePoint);
	}
	
	/**
	 * @author Mobius
	 */
	public class Teleportation extends RunnableImpl
	{
		/**
		 * Field _telePoint.
		 */
		Location _telePoint = null;
		/**
		 * Field _target.
		 */
		Playable _target = null;
		
		/**
		 * Constructor for Teleportation.
		 * @param telePoint Location
		 * @param target Playable
		 */
		public Teleportation(Location telePoint, Playable target)
		{
			_telePoint = telePoint;
			_target = target;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_target.teleToLocation(_telePoint);
			noCheckPlayers = false;
		}
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (!noCheckPlayers)
		{
			checkAroundPlayers(actor);
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		moveToEnd = !moveToEnd;
		if (!moveToEnd)
		{
			addTaskMove(getEndPoint(), true);
		}
		else
		{
			addTaskMove(getStartPoint(), true);
		}
		doTask();
		return true;
	}
	
	/**
	 * Method checkAroundPlayers.
	 * @param actor NpcInstance
	 * @return boolean
	 */
	private boolean checkAroundPlayers(NpcInstance actor)
	{
		for (Playable target : World.getAroundPlayables(actor, _aggrorange, _aggrorange))
		{
			if ((target != null) && target.isPlayer() && !target.isInvul() && GeoEngine.canSeeTarget(actor, target, false))
			{
				actor.doCast(_skill, target, true);
				Functions.npcSay(actor, "Intruder! Protect the Priests of Dawn!");
				noCheckPlayers = true;
				ThreadPoolManager.getInstance().schedule(new Teleportation(getTelePoint(), target), 3000);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method setStartPoint.
	 * @param loc Location
	 */
	private void setStartPoint(Location loc)
	{
		_locStart = loc;
	}
	
	/**
	 * Method setEndPoint.
	 * @param loc Location
	 */
	private void setEndPoint(Location loc)
	{
		_locEnd = loc;
	}
	
	/**
	 * Method setTelePoint.
	 * @param loc Location
	 */
	private void setTelePoint(Location loc)
	{
		_locTele = loc;
	}
	
	/**
	 * Method getStartPoint.
	 * @return Location
	 */
	private Location getStartPoint()
	{
		return _locStart;
	}
	
	/**
	 * Method getEndPoint.
	 * @return Location
	 */
	private Location getEndPoint()
	{
		return _locEnd;
	}
	
	/**
	 * Method getTelePoint.
	 * @return Location
	 */
	private Location getTelePoint()
	{
		return _locTele;
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
}
