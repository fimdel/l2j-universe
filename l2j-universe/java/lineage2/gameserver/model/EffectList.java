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
package lineage2.gameserver.model;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.skills.skillclasses.Transformation;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectList
{
	/**
	 * Field NONE_SLOT_TYPE. (value is -1)
	 */
	public static final int NONE_SLOT_TYPE = -1;
	/**
	 * Field BUFF_SLOT_TYPE. (value is 0)
	 */
	public static final int BUFF_SLOT_TYPE = 0;
	/**
	 * Field MUSIC_SLOT_TYPE. (value is 1)
	 */
	public static final int MUSIC_SLOT_TYPE = 1;
	/**
	 * Field TRIGGER_SLOT_TYPE. (value is 2)
	 */
	public static final int TRIGGER_SLOT_TYPE = 2;
	/**
	 * Field DEBUFF_SLOT_TYPE. (value is 3)
	 */
	public static final int DEBUFF_SLOT_TYPE = 3;
	/**
	 * Field DEBUFF_LIMIT. (value is 8)
	 */
	public static final int DEBUFF_LIMIT = 8;
	/**
	 * Field MUSIC_LIMIT. (value is 12)
	 */
	public static final int MUSIC_LIMIT = 12;
	/**
	 * Field TRIGGER_LIMIT. (value is 12)
	 */
	public static final int TRIGGER_LIMIT = 12;
	/**
	 * Field _actor.
	 */
	private final Creature _actor;
	/**
	 * Field _effects.
	 */
	private List<Effect> _effects;
	/**
	 * Field lock.
	 */
	private final Lock lock = new ReentrantLock();
	
	/**
	 * Constructor for EffectList.
	 * @param owner Creature
	 */
	public EffectList(Creature owner)
	{
		_actor = owner;
	}
	
	/**
	 * Method getEffectsCountForSkill.
	 * @param skill_id int
	 * @return int
	 */
	public int getEffectsCountForSkill(int skill_id)
	{
		if (isEmpty())
		{
			return 0;
		}
		int count = 0;
		for (Effect e : _effects)
		{
			if (e.getSkill().getId() == skill_id)
			{
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Method getEffectByType.
	 * @param et EffectType
	 * @return Effect
	 */
	public Effect getEffectByType(EffectType et)
	{
		if (isEmpty())
		{
			return null;
		}
		for (Effect e : _effects)
		{
			if (e.getEffectType() == et)
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Method getEffectsBySkill.
	 * @param skill Skill
	 * @return List<Effect>
	 */
	public List<Effect> getEffectsBySkill(Skill skill)
	{
		if (skill == null)
		{
			return null;
		}
		return getEffectsBySkillId(skill.getId());
	}
	
	/**
	 * Method getEffectsBySkillId.
	 * @param skillId int
	 * @return List<Effect>
	 */
	public List<Effect> getEffectsBySkillId(int skillId)
	{
		if (isEmpty())
		{
			return null;
		}
		List<Effect> list = new ArrayList<>(2);
		for (Effect e : _effects)
		{
			if (e.getSkill().getId() == skillId)
			{
				list.add(e);
			}
		}
		return list.isEmpty() ? null : list;
	}
	
	/**
	 * Method getEffectByIndexAndType.
	 * @param skillId int
	 * @param type EffectType
	 * @return Effect
	 */
	public Effect getEffectByIndexAndType(int skillId, EffectType type)
	{
		if (isEmpty())
		{
			return null;
		}
		for (Effect e : _effects)
		{
			if ((e.getSkill().getId() == skillId) && (e.getEffectType() == type))
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Method getEffectByStackType.
	 * @param type String
	 * @return Effect
	 */
	public Effect getEffectByStackType(String type)
	{
		if (isEmpty())
		{
			return null;
		}
		for (Effect e : _effects)
		{
			if (e.getStackType().contains(type))
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Method containEffectFromSkills.
	 * @param skillIds int[]
	 * @return boolean
	 */
	public boolean containEffectFromSkills(int[] skillIds)
	{
		if (isEmpty())
		{
			return false;
		}
		int skillId;
		for (Effect e : _effects)
		{
			skillId = e.getSkill().getId();
			if (ArrayUtils.contains(skillIds, skillId))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Method containEffectFromSkill.
	 * @param skillId integer
	 * @param removeEffects boolean
	 * @return boolean
	 */
	public boolean containEffectFromSkillId(int skillId, boolean removeEffects)
	{
		boolean contain = false;
		if (isEmpty())
		{
			return contain;
		}
		for (Effect e : _effects)
		{
			if (skillId == e.getSkill().getId())
			{
				contain = true;
				e.exit();
			}
		}
		return contain;
	}

	/**
	 * Method getAllEffects.
	 * @return List<Effect>
	 */
	public List<Effect> getAllEffects()
	{
		if (isEmpty())
		{
			return Collections.emptyList();
		}
		return new ArrayList<>(_effects);
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return (_effects == null) || _effects.isEmpty();
	}
	
	/**
	 * Method getAllFirstEffects.
	 * @return Effect[]
	 */
	public Effect[] getAllFirstEffects()
	{
		if (isEmpty())
		{
			return Effect.EMPTY_L2EFFECT_ARRAY;
		}
		TIntObjectHashMap<Effect> map = new TIntObjectHashMap<>();
		for (Effect e : _effects)
		{
			map.put(e.getSkill().getId(), e);
		}
		return map.values(new Effect[map.size()]);
	}
	
	/**
	 * Method checkSlotLimit.
	 * @param newEffect Effect
	 */
	private void checkSlotLimit(Effect newEffect)
	{
		if (_effects == null)
		{
			return;
		}
		int slotType = getSlotType(newEffect);
		if (slotType == NONE_SLOT_TYPE)
		{
			return;
		}
		int size = 0;
		TIntArrayList skillIds = new TIntArrayList();
		for (Effect e : _effects)
		{
			if (e.isInUse())
			{
				if (e.getSkill().equals(newEffect.getSkill()))
				{
					return;
				}
				if (!skillIds.contains(e.getSkill().getId()))
				{
					int subType = getSlotType(e);
					if (subType == slotType)
					{
						size++;
						skillIds.add(e.getSkill().getId());
					}
				}
			}
		}
		int limit = 0;
		switch (slotType)
		{
			case BUFF_SLOT_TYPE:
				limit = _actor.getBuffLimit();
				break;
			case MUSIC_SLOT_TYPE:
				limit = MUSIC_LIMIT;
				break;
			case DEBUFF_SLOT_TYPE:
				limit = DEBUFF_LIMIT;
				break;
			case TRIGGER_SLOT_TYPE:
				limit = TRIGGER_LIMIT;
				break;
		}
		if (size < limit)
		{
			return;
		}
		int skillId = 0;
		for (Effect e : _effects)
		{
			if (e.isInUse())
			{
				if (getSlotType(e) == slotType)
				{
					skillId = e.getSkill().getId();
					break;
				}
			}
		}
		if (skillId != 0)
		{
			stopEffect(skillId);
		}
	}
	
	/**
	 * Method getSlotType.
	 * @param e Effect
	 * @return int
	 */
	public static int getSlotType(Effect e)
	{
		if (e.getSkill().isPassive() || e.getSkill().isToggle() || (e.getSkill() instanceof Transformation) || e.getStackType().contains(EffectTemplate.HP_RECOVER_CAST) || (e.getEffectType() == EffectType.Cubic))
		{
			return NONE_SLOT_TYPE;
		}
		else if (e.getSkill().isOffensive())
		{
			return DEBUFF_SLOT_TYPE;
		}
		else if (e.getSkill().isMusic())
		{
			return MUSIC_SLOT_TYPE;
		}
		else if (e.getSkill().isTrigger())
		{
			return TRIGGER_SLOT_TYPE;
		}
		else
		{
			return BUFF_SLOT_TYPE;
		}
	}
	
	/**
	 * Method checkStackType.
	 * @param ef1 EffectTemplate
	 * @param ef2 EffectTemplate
	 * @return boolean
	 */
	public static boolean checkStackType(EffectTemplate ef1, EffectTemplate ef2)
	{
		if (!ef1._stackTypes.contains(EffectTemplate.NO_STACK))
		{
			for (String arg : ef2._stackTypes)
			{
				if (ef1._stackTypes.contains(arg))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method addEffect.
	 * @param effect Effect
	 */
	public void addEffect(Effect effect)
	{
		double hp = _actor.getCurrentHp();
		double mp = _actor.getCurrentMp();
		double cp = _actor.getCurrentCp();
		boolean add = false;
		lock.lock();
		try
		{
			if (_effects == null)
			{
				_effects = new CopyOnWriteArrayList<>();
			}
			if (effect.getStackType().contains(EffectTemplate.NO_STACK))
			{
				for (Effect e : _effects)
				{
					if (!e.isInUse())
					{
						continue;
					}
					if (e.getStackType().contains(EffectTemplate.NO_STACK) && (e.getSkill().getId() == effect.getSkill().getId()) && (e.getEffectType() == effect.getEffectType()))
					{
						if (effect.getTimeLeft() > e.getTimeLeft())
						{
							e.exit();
						}
						else
						{
							return;
						}
					}
				}
			}
			else
			{
				for (Effect e : _effects)
				{
					if (!e.isInUse())
					{
						continue;
					}
					if (!checkStackType(e.getTemplate(), effect.getTemplate()))
					{
						continue;
					}
					if ((e.getSkill().getId() == effect.getSkill().getId()) && (e.getEffectType() != effect.getEffectType()))
					{
						break;
					}
					if (e.getStackOrder() == -1)
					{
						return;
					}
					if (!e.maybeScheduleNext(effect))
					{
						return;
					}
				}
			}
			checkSlotLimit(effect);
			add = _effects.add(effect);
			if (add)
			{
				effect.setInUse(true);
			}
		}
		finally
		{
			lock.unlock();
		}
		if (!add)
		{
			return;
		}
		effect.start();
		for (FuncTemplate ft : effect.getTemplate().getAttachedFuncs())
		{
			if (ft._stat == Stats.MAX_HP)
			{
				_actor.setCurrentHp(hp, false);
			}
			else if (ft._stat == Stats.MAX_MP)
			{
				_actor.setCurrentMp(mp);
			}
			else if (ft._stat == Stats.MAX_CP)
			{
				_actor.setCurrentCp(cp);
			}
		}
		_actor.updateStats();
		_actor.updateEffectIcons();
	}
	
	/**
	 * Method removeEffect.
	 * @param effect Effect
	 */
	public void removeEffect(Effect effect)
	{
		if (effect == null)
		{
			return;
		}
		boolean remove = false;
		lock.lock();
		try
		{
			if (_effects == null)
			{
				return;
			}
			if (!((remove = _effects.remove(effect))))
			{
				return;
			}
		}
		finally
		{
			lock.unlock();
		}
		if (!remove)
		{
			return;
		}
		_actor.updateStats();
		_actor.updateEffectIcons();
	}
	
	/**
	 * Method removeEffect.
	 * @param skillId int
	 */
	public void removeEffect(int skillId)
	{
		if (isEmpty())
		{
			return;
		}
		for (Effect e : _effects)
		{
			if (e.getSkill().getId() == skillId)
			{
				boolean remove = false;
				lock.lock();
				try
				{
					if (_effects == null)
					{
						return;
					}
					if (!((remove = _effects.remove(e))))
					{
						return;
					}
				}
				finally
				{
					lock.unlock();
				}
				if (!remove)
				{
					return;
				}
				_actor.updateStats();
				_actor.updateEffectIcons();
			}
		}
	}
	
	/**
	 * Method stopAllEffects.
	 */
	public void stopAllEffects()
	{
		if (isEmpty())
		{
			return;
		}
		lock.lock();
		try
		{
			for (Effect e : _effects)
			{
				e.exit();
			}
		}
		finally
		{
			lock.unlock();
		}
		_actor.updateStats();
		_actor.updateEffectIcons();
	}
	
	/**
	 * Method stopEffect.
	 * @param skillId int
	 */
	public void stopEffect(int skillId)
	{
		if (isEmpty())
		{
			return;
		}
		for (Effect e : _effects)
		{
			if (e.getSkill().getId() == skillId)
			{
				e.exit();
			}
		}
	}
	
	/**
	 * Method stopEffect.
	 * @param skill Skill
	 */
	public void stopEffect(Skill skill)
	{
		if (skill != null)
		{
			stopEffect(skill.getId());
		}
	}
	
	/**
	 * Method stopEffectByDisplayId.
	 * @param skillId int
	 */
	public void stopEffectByDisplayId(int skillId)
	{
		if (isEmpty())
		{
			return;
		}
		for (Effect e : _effects)
		{
			if (e.getSkill().getDisplayId() == skillId)
			{
				e.exit();
			}
		}
	}
	
	/**
	 * Method stopEffects.
	 * @param type EffectType
	 */
	public void stopEffects(EffectType type)
	{
		if (isEmpty())
		{
			return;
		}
		for (Effect e : _effects)
		{
			if (e.getEffectType() == type)
			{
				e.exit();
			}
		}
	}
	
	/**
	 * Method stopAllSkillEffects.
	 * @param type EffectType
	 */
	public void stopAllSkillEffects(EffectType type)
	{
		if (isEmpty())
		{
			return;
		}
		TIntHashSet skillIds = new TIntHashSet();
		for (Effect e : _effects)
		{
			if (e.getEffectType() == type)
			{
				skillIds.add(e.getSkill().getId());
			}
		}
		for (int skillId : skillIds.toArray())
		{
			stopEffect(skillId);
		}
	}
}
