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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SymbolInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _owner.
	 */
	final Creature _owner;
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
	 * Constructor for SymbolInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Creature
	 * @param skill Skill
	 */
	public SymbolInstance(int objectId, NpcTemplate template, Creature owner, Skill skill)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		setReflection(owner.getReflection());
		setLevel(owner.getLevel());
		setTitle(owner.getName());
	}
	
	/**
	 * Method getOwner.
	 * @return Creature
	 */
	public Creature getOwner()
	{
		return _owner;
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), 120000L);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				List<Creature> targets = new ArrayList<>();
				if (!_skill.isAoE())
				{
					for (Creature target : getAroundCharacters(200, 200))
					{
						if (_skill.checkTarget(_owner, target, null, false, false) == null)
						{
							targets.add(target);
							_skill.useSkill(SymbolInstance.this, targets);
						}
					}
				}
				else
				{
					for (Creature t : getAroundCharacters(_skill.getSkillRadius(), 200))
					{
						if (_skill.checkTarget(_owner, t, null, false, false) == null)
						{
							if (t == _owner && _skill.isOffensive())
								continue;
							targets.add(t);
						}
					}
					broadcastPacket(new MagicSkillUse(SymbolInstance.this, SymbolInstance.this, _skill.getId(), _skill.getLevel(), 0, 0));
					_skill.useSkill(SymbolInstance.this, targets);
				}
			}
		}, 1000L, _skill.getReuseDelay() != 0 ? _skill.getReuseDelay() : 3000L);
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
	public void onActionSelect(final Player player, final boolean forced)
	{
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return null;
	}
}
