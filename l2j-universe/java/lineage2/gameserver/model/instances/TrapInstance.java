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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.SkillTargetType;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TrapInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private static class CastTask extends RunnableImpl
	{
		/**
		 * Field _trapRef.
		 */
		private final HardReference<NpcInstance> _trapRef;
		
		/**
		 * Constructor for CastTask.
		 * @param trap TrapInstance
		 */
		public CastTask(TrapInstance trap)
		{
			_trapRef = trap.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			TrapInstance trap = (TrapInstance) _trapRef.get();
			if (trap == null)
			{
				return;
			}
			Creature owner = trap.getOwner();
			if (owner == null)
			{
				return;
			}
			if (trap._skill == null)
			{
				System.out.println("ERROR IN TRAP SKILL");
				trap.deleteMe();
				return;
			}
			for (Creature target : trap.getAroundCharacters(200, 200))
			{
				if (target != owner)
				{
					if (trap._skill.checkTarget(owner, target, null, false, false) == null)
					{
						List<Creature> targets = new ArrayList<>();
						if (trap._skill.getTargetType() != SkillTargetType.TARGET_AREA)
						{
							targets.add(target);
						}
						else
						{
							for (Creature t : trap.getAroundCharacters(trap._skill.getSkillRadius(), 128))
							{
								if (trap._skill.checkTarget(owner, t, null, false, false) == null)
								{
									targets.add(target);
								}
							}
						}
						trap._skill.useSkill(trap, targets);
						if (target.isPlayer())
						{
							target.sendMessage(new CustomMessage("common.Trap", target.getPlayer()));
						}
						trap.deleteMe();
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Field _ownerRef.
	 */
	private final HardReference<? extends Creature> _ownerRef;
	/**
	 * Field _skill.
	 */
	final Skill _skill;
	/**
	 * Field _targetTask.
	 */
	private ScheduledFuture<?> _targetTask;
	/**
	 * Field _destroyTask.
	 */
	private ScheduledFuture<?> _destroyTask;
	/**
	 * Field _detected.
	 */
	private boolean _detected;
	
	/**
	 * Constructor for TrapInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Creature
	 * @param skill Skill
	 */
	public TrapInstance(int objectId, NpcTemplate template, Creature owner, Skill skill)
	{
		this(objectId, template, owner, skill, owner.getLoc());
	}
	
	/**
	 * Constructor for TrapInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Creature
	 * @param skill Skill
	 * @param loc Location
	 */
	public TrapInstance(int objectId, NpcTemplate template, Creature owner, Skill skill, Location loc)
	{
		super(objectId, template);
		_ownerRef = owner.getRef();
		_skill = skill;
		setReflection(owner.getReflection());
		setLevel(owner.getLevel());
		setTitle(owner.getName());
		setLoc(loc);
	}
	
	/**
	 * Method isTrap.
	 * @return boolean
	 */
	@Override
	public boolean isTrap()
	{
		return true;
	}
	
	/**
	 * Method getOwner.
	 * @return Creature
	 */
	public Creature getOwner()
	{
		return _ownerRef.get();
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), 120000L);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new CastTask(this), 250L, 250L);
	}
	
	/**
	 * Method broadcastCharInfo.
	 */
	@Override
	public void broadcastCharInfo()
	{
		if (!isDetected())
		{
			return;
		}
		super.broadcastCharInfo();
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		Creature owner = getOwner();
		if ((owner != null) && owner.isPlayer())
		{
			((Player) owner).removeTrap(this);
		}
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
	 * Method isDetected.
	 * @return boolean
	 */
	public boolean isDetected()
	{
		return _detected;
	}
	
	/**
	 * Method setDetected.
	 * @param detected boolean
	 */
	public void setDetected(boolean detected)
	{
		_detected = detected;
	}
	
	/**
	 * Method getPAtk.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPAtk(Creature target)
	{
		Creature owner = getOwner();
		return owner == null ? 0 : owner.getPAtk(target);
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
		Creature owner = getOwner();
		return owner == null ? 0 : owner.getMAtk(target, skill);
	}
	
	/**
	 * Method hasRandomAnimation.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return true;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param filename String
	 * @param replace Object[]
	 */
	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}
	
	@Override
	public void onActionTargeted(final Player player, boolean forced)
	{
	}

	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isDetected() && (getOwner() != forPlayer))
		{
			return Collections.emptyList();
		}
		return Collections.<L2GameServerPacket> singletonList(new NpcInfo(this, forPlayer));
	}
}
