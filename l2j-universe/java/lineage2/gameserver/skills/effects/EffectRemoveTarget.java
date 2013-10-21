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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectRemoveTarget extends Effect
{
	/**
	 * Field _doStopTarget.
	 */
	private final boolean _doStopTarget;
	
	/**
	 * Constructor for EffectRemoveTarget.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectRemoveTarget(Env env, EffectTemplate template)
	{
		super(env, template);
		_doStopTarget = template.getParam().getBool("doStopTarget", false);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		return Rnd.chance(_template.chance(100));
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		if (getEffected().getAI() instanceof DefaultAI)
		{
			((DefaultAI) getEffected().getAI()).setGlobalAggro(System.currentTimeMillis() + 3000L);
		}
		getEffected().setTarget(null);
		if (_doStopTarget)
		{
			getEffected().stopMove();
		}
		getEffected().abortAttack(true, true);
		getEffected().abortCast(true, true);
		getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, getEffector());
	}
	
	/**
	 * Method isHidden.
	 * @return boolean
	 */
	@Override
	public boolean isHidden()
	{
		return true;
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
