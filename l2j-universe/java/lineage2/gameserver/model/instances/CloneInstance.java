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
package lineage2.gameserver.model.instances;

import java.util.concurrent.ScheduledFuture;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.ClonePlayer;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.player.PlayerTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CloneInstance extends ClonePlayer
{
	/**
	 * Field serialVersionUID. (value is -3990686488577795700)
	 */
	private static final long serialVersionUID = -3990686488577795700L;
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _lifetimeCountdown.
	 */
	private final int _lifetimeCountdown;
	/**
	 * Field _targetTask.
	 */
	private ScheduledFuture<?> _targetTask;
	/**
	 * Field _destroyTask.
	 */
	private ScheduledFuture<?> _destroyTask;
	
	/**
	 * Constructor for TreeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifetime int
	 * @param skill Skill
	 * @param loc Location
	 */
	public CloneInstance(int objectId, PlayerTemplate template, Player owner, int lifetime, Location loc)
	{
		super(objectId, template, owner);
		_owner = owner;
		_lifetimeCountdown = lifetime;
		setLoc(loc);
		setHeading(owner.getHeading());
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), _lifetimeCountdown);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
		}
		_destroyTask = null;
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
		_targetTask = null;
		super.onDelete();
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
	}
	
	/**
	 * Method stopDisappear.
	 */
	protected synchronized void stopDisappear()
	{
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
	}
	
	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		Player owner = getPlayer();
		if (owner == null)
		{
			return;
		}
		if (crit)
		{
			owner.sendPacket(SystemMsg.SUMMONED_MONSTERS_CRITICAL_HIT);
		}
		if (miss)
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addString("Clone of " + owner.getName()));
		}
		else if (!target.isInvul())
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addString("Clone of " + owner.getName()).addName(target).addNumber(damage));
		}
	}
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
		Player owner = getPlayer();
		owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2).addString("Clone of " + owner.getName()).addName(attacker).addNumber((long) damage));
	}
	
	@Override
	public int getLevel()
	{
		return _owner.getLevel();
	}
	
	/**
	 * Method isServitor.
	 * @return boolean
	 */
	@Override
	public boolean isServitor()
	{
		return true;
	}

	/**
	 * Method isServitor.
	 * @return boolean
	 */
	@Override
	public boolean isClone()
	{
		return true;
	}
	
	@Override
	public void onActionSelect(final Player player, boolean forced)
	{
		if(isTargetable())
		{
			super.onActionSelect(player, forced);
		}
	}

	/**
	 * Method getDEX.
	 * @return int
	 */
	@Override
	public int getDEX()
	{
		return _owner.getDEX();
	}
	
	/**
	 * Method getCON.
	 * @return int
	 */
	@Override
	public int getCON()
	{
		return _owner.getCON();
	}
	
	/**
	 * Method getINT.
	 * @return int
	 */
	@Override
	public int getINT()
	{
		return _owner.getINT();
	}
	
	/**
	 * Method getMEN.
	 * @return int
	 */
	@Override
	public int getMEN()
	{
		return _owner.getMEN();
	}
	
	/**
	 * Method getSTR.
	 * @return int
	 */
	@Override
	public int getSTR()
	{
		return _owner.getSTR();
	}
	
	/**
	 * Method getEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getEvasionRate(Creature target)
	{
		return _owner.getEvasionRate(target);
	}
	
	/**
	 * Method getMEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getMEvasionRate(Creature target)
	{
		return _owner.getMEvasionRate(target);
	}
	
	/**
	 * Method getWIT.
	 * @return int
	 */
	@Override
	public int getWIT()
	{
		return _owner.getWIT();
	}

	/**
	 * Method getMAtk.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMAtk(Creature target, Skill skill)
	{
		return _owner.getMAtk(target, skill);
	}
	
	/**
	 * Method getMAtkSpd.
	 * @return int
	 */
	@Override
	public int getMAtkSpd()
	{
		return _owner.getMAtkSpd();
	}
	
	/**
	 * Method getMaxCp.
	 * @return int
	 */
	@Override
	public int getMaxCp()
	{
		return _owner.getMaxCp();
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return _owner.getMaxHp();
	}
	
	/**
	 * Method getMaxMp.
	 * @return int
	 */
	@Override
	public int getMaxMp()
	{
		return _owner.getMaxMp();
	}
	
	/**
	 * Method getMDef.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMDef(Creature target, Skill skill)
	{
		return _owner.getMDef(target, skill);
	}

	/**
	 * Method getMovementSpeedMultiplier.
	 * @return double
	 */
	@Override
	public double getMovementSpeedMultiplier()
	{
		return _owner.getMovementSpeedMultiplier();
	}
	
	/**
	 * Method getRunSpeed.
	 * @return int
	 */
	@Override
	public int getRunSpeed()
	{
		return _owner.getRunSpeed();
	}
	
	/**
	 * Method getCriticalHit.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getCriticalHit(Creature target, Skill skill)
	{
		return _owner.getCriticalHit(target, skill);
	}
	
	/**
	 * Method getWalkSpeed.
	 * @return int
	 */
	@Override
	public int getWalkSpeed()
	{
		return _owner.getWalkSpeed();
	}
	
	/**
	 * Method getSwimRunSpeed.
	 * @return int
	 */
	@Override
	public int getSwimRunSpeed()
	{
		return _owner.getSwimRunSpeed();
	}
	
	/**
	 * Method getSwimWalkSpeed.
	 * @return int
	 */
	@Override
	public int getSwimWalkSpeed()
	{
		return _owner.getSwimWalkSpeed();
	}

	@Override
	public int getPAtk(Creature target)
	{
		return _owner.getPAtk(target);
	}
	
	/**
	 * Method getPAtkSpd.
	 * @return int
	 */
	@Override
	public int getPAtkSpd()
	{
		return _owner.getPAtkSpd();
	}
	
	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPDef(Creature target)
	{
		return _owner.getPDef(target);
	}

}
