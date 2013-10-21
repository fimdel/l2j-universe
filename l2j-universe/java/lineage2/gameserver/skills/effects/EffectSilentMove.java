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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectSilentMove extends Effect
{
	/**
	 * Constructor for EffectSilentMove.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectSilentMove(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (_effected.isPlayable())
		{
			((Playable) _effected).startSilentMoving();
		}
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if (_effected.isPlayable())
		{
			((Playable) _effected).stopSilentMoving();
		}
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (_effected.isDead())
		{
			return false;
		}
		if (!getSkill().isToggle())
		{
			return false;
		}
		double manaDam = calc();
		if (manaDam > _effected.getCurrentMp())
		{
			_effected.sendPacket(Msg.NOT_ENOUGH_MP);
			_effected.sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(getSkill().getId(), getSkill().getDisplayLevel()));
			return false;
		}
		_effected.reduceCurrentMp(manaDam, null);
		return true;
	}
}
