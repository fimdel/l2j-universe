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
package lineage2.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.EffectList;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectTemplate extends StatTemplate
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EffectTemplate.class);
	/**
	 * Field EMPTY_ARRAY.
	 */
	public static final EffectTemplate[] EMPTY_ARRAY = new EffectTemplate[0];
	/**
	 * Field NO_STACK.
	 */
	public static final String NO_STACK = "none".intern();
	/**
	 * Field HP_RECOVER_CAST.
	 */
	public static final String HP_RECOVER_CAST = "HpRecoverCast".intern();
	/**
	 * Field _attachCond.
	 */
	public Condition _attachCond;
	/**
	 * Field _value.
	 */
	public final double _value;
	/**
	 * Field _count.
	 */
	public final int _count;
	/**
	 * Field _period.
	 */
	public final long _period;
	/**
	 * Field _abnormalEffect.
	 */
	public AbnormalEffect _abnormalEffect;
	/**
	 * Field _abnormalEffect2.
	 */
	public AbnormalEffect _abnormalEffect2;
	/**
	 * Field _abnormalEffect3.
	 */
	public AbnormalEffect _abnormalEffect3;
	/**
	 * Field _effectType.
	 */
	public final EffectType _effectType;
	/**
	 * Field _stackTypes.
	 */
	public List<String> _stackTypes = new ArrayList<>();
	/**
	 * Field _stackOrder.
	 */
	public final int _stackOrder;
	/**
	 * Field _displayId.
	 */
	public final int _displayId;
	/**
	 * Field _displayLevel.
	 */
	public final int _displayLevel;
	/**
	 * Field _applyOnCaster.
	 */
	public final boolean _applyOnCaster;
	/**
	 * Field _applyOnSummon.
	 */
	public final boolean _applyOnSummon;
	/**
	 * Field _cancelOnAction.
	 */
	public final boolean _cancelOnAction;
	/**
	 * Field _cancelOnAttacked.
	 */
	public final boolean _cancelOnAttacked;
	/**
	 * Field _isReflectable.
	 */
	public final boolean _isReflectable;
	/**
	 * Field _isSaveable.
	 */
	private final Boolean _isSaveable;
	/**
	 * Field _isCancelable.
	 */
	private final Boolean _isCancelable;
	/**
	 * Field _isOffensive.
	 */
	private final Boolean _isOffensive;
	/**
	 * Field _skillToCast.
	 */
	protected final int _skillToCast;
	/**
	 * Field _skillToCastLevel.
	 */
	protected final int _skillToCastLevel;
	/**
	 * Field _paramSet.
	 */
	private final StatsSet _paramSet;
	/**
	 * Field _chance.
	 */
	private final int _chance;
	
	/**
	 * Constructor for EffectTemplate.
	 * @param set StatsSet
	 */
	public EffectTemplate(StatsSet set)
	{
		_value = set.getDouble("value");
		_count = set.getInteger("count", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("count", 1);
		_period = Math.min(Integer.MAX_VALUE, 1000 * (set.getInteger("time", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("time", 1)));
		_abnormalEffect = set.getEnum("abnormal", AbnormalEffect.class);
		_abnormalEffect2 = set.getEnum("abnormal2", AbnormalEffect.class);
		_abnormalEffect3 = set.getEnum("abnormal3", AbnormalEffect.class);
		String args[] = set.getString("stackType", NO_STACK).split(";", -1);
		for (String arg : args)
		{
			_stackTypes.add(arg);
		}
		_stackOrder = set.getInteger("stackOrder", _stackTypes.contains(NO_STACK) ? 1 : 0);
		_applyOnCaster = set.getBool("applyOnCaster", false);
		_applyOnSummon = set.getBool("applyOnSummon", true);
		_cancelOnAction = set.getBool("cancelOnAction", false);
		_cancelOnAttacked = set.getBool("cancelOnAttacked", false);
		_isReflectable = set.getBool("isReflectable", true);
		_isSaveable = set.isSet("isSaveable") ? set.getBool("isSaveable") : null;
		_isCancelable = set.isSet("isCancelable") ? set.getBool("isCancelable") : null;
		_isOffensive = set.isSet("isOffensive") ? set.getBool("isOffensive") : null;
		_displayId = set.getInteger("displayId", 0);
		_displayLevel = set.getInteger("displayLevel", 0);
		_effectType = set.getEnum("name", EffectType.class);
		_chance = set.getInteger("chance", Integer.MAX_VALUE);
		_skillToCast = set.getInteger("skillToCast", 0);
		_skillToCastLevel = set.getInteger("skillToCastLevel", 0);
		_paramSet = set;
	}
	
	/**
	 * Method getEffect.
	 * @param env Env
	 * @return Effect
	 */
	public Effect getEffect(Env env)
	{
		if ((_attachCond != null) && !_attachCond.test(env))
		{
			return null;
		}
		try
		{
			return _effectType.makeEffect(env, this);
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		return null;
	}
	
	/**
	 * Method attachCond.
	 * @param c Condition
	 */
	public void attachCond(Condition c)
	{
		_attachCond = c;
	}
	
	/**
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method getPeriod.
	 * @return long
	 */
	public long getPeriod()
	{
		return _period;
	}
	
	/**
	 * Method getEffectType.
	 * @return EffectType
	 */
	public EffectType getEffectType()
	{
		return _effectType;
	}
	
	/**
	 * Method getSameByStackType.
	 * @param list List<Effect>
	 * @return Effect
	 */
	public Effect getSameByStackType(List<Effect> list)
	{
		for (Effect ef : list)
		{
			if ((ef != null) && EffectList.checkStackType(ef.getTemplate(), this))
			{
				return ef;
			}
		}
		return null;
	}
	
	/**
	 * Method getSameByStackType.
	 * @param list EffectList
	 * @return Effect
	 */
	public Effect getSameByStackType(EffectList list)
	{
		return getSameByStackType(list.getAllEffects());
	}
	
	/**
	 * Method getSameByStackType.
	 * @param actor Creature
	 * @return Effect
	 */
	public Effect getSameByStackType(Creature actor)
	{
		return getSameByStackType(actor.getEffectList().getAllEffects());
	}
	
	/**
	 * Method getParam.
	 * @return StatsSet
	 */
	public StatsSet getParam()
	{
		return _paramSet;
	}
	
	/**
	 * Method chance.
	 * @param val int
	 * @return int
	 */
	public int chance(int val)
	{
		return _chance == Integer.MAX_VALUE ? val : _chance;
	}
	
	/**
	 * Method isSaveable.
	 * @param def boolean
	 * @return boolean
	 */
	public boolean isSaveable(boolean def)
	{
		return _isSaveable != null ? _isSaveable.booleanValue() : def;
	}
	
	/**
	 * Method isCancelable.
	 * @param def boolean
	 * @return boolean
	 */
	public boolean isCancelable(boolean def)
	{
		return _isCancelable != null ? _isCancelable.booleanValue() : def;
	}
	
	/**
	 * Method isOffensive.
	 * @param def boolean
	 * @return boolean
	 */
	public boolean isOffensive(boolean def)
	{
		return _isOffensive != null ? _isOffensive.booleanValue() : def;
	}
}
