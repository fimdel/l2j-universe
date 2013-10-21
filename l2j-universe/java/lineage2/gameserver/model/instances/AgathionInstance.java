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
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public class AgathionInstance extends NpcInstance
{
	private static final long serialVersionUID = -3990686488577795700L;
	private final Player _owner;
	final Skill _skill;
	private final int _lifetimeCountdown;
	private ScheduledFuture<?> _targetTask;
	private ScheduledFuture<?> _destroyTask;
	
	public AgathionInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill)
	{
		this(objectId, template, owner, lifetime, skill, owner.getLoc());
	}
	
	public AgathionInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill, Location loc)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		_lifetimeCountdown = lifetime;
		setLevel(owner.getLevel());
		setTitle(owner.getName());
		setLoc(loc);
		setHeading(owner.getHeading());
	}
	
	public Player getOwner()
	{
		return _owner;
	}
	
	private class RemoveAgathion extends RunnableImpl
	{
		
		Player _p;
		
		public RemoveAgathion(Player player)
		{
			_p = player;
		}
		
		@Override
		public void runImpl()
		{
			_p.setAgathion(0);
		}
	}
	
	private static class CastTask extends RunnableImpl
	{
		private final HardReference<NpcInstance> _agatRef;
		
		public CastTask(AgathionInstance agat)
		{
			_agatRef = agat.getRef();
		}
		
		@Override
		public void runImpl()
		{
			AgathionInstance agat = (AgathionInstance) _agatRef.get();
			if (agat == null)
			{
				return;
			}
			Player owner = agat.getOwner();
			if (owner == null)
			{
				return;
			}
			List<Creature> targets = new ArrayList<>(10);
			for (Player target : World.getAroundPlayers(agat, 600, 200))
			{
				if (targets.size() > 10)
				{
					break;
				}
				if (target == owner)
				{
					targets.add(target);
					agat.broadcastPacket(new MagicSkillUse(agat, target, agat._skill.getId(), agat._skill.getLevel(), 0, 0));
				}
				if ((target.getParty() != null) && (owner.getParty() == target.getParty()))
				{
					targets.add(target);
					agat.broadcastPacket(new MagicSkillUse(agat, target, agat._skill.getId(), agat._skill.getLevel(), 0, 0));
				}
			}
			agat.callSkill(agat._skill, targets, true);
		}
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		EffectTaskManager.getInstance().schedule(new RemoveAgathion(_owner), _lifetimeCountdown);
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), _lifetimeCountdown);
		if (_skill != null)
		{
			_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new CastTask(this), 1000L, 5000L);
		}
	}
	
	@Override
	protected void onDelete()
	{
		Player owner = getOwner();
		if (owner != null)
		{
			owner.setTree(false);
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
	
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}
	
	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}
}
