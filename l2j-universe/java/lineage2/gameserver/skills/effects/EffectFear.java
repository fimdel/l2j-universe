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

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.SummonInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectFear extends Effect
{
	/**
	 * Field FEAR_RANGE. (value is 900.0)
	 */
	public static final double FEAR_RANGE = 900;
	
	/**
	 * Constructor for EffectFear.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectFear(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (_effected.isFearImmune())
		{
			getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		Player player = _effected.getPlayer();
		if (player != null)
		{
			SiegeEvent<?, ?> siegeEvent = player.getEvent(SiegeEvent.class);
			if (_effected.isServitor() && (siegeEvent != null) && siegeEvent.containsSiegeSummon((SummonInstance) _effected))
			{
				getEffector().sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
				return false;
			}
		}
		if (_effected.isInZonePeace())
		{
			getEffector().sendPacket(Msg.YOU_MAY_NOT_ATTACK_IN_A_PEACEFUL_ZONE);
			return false;
		}
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (!_effected.startFear())
		{
			_effected.abortAttack(true, true);
			_effected.abortCast(true, true);
			_effected.stopMove();
		}
		onActionTime();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopFear();
		_effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		final double angle = Math.toRadians(PositionUtils.calculateAngleFrom(_effector, _effected));
		final int oldX = _effected.getX();
		final int oldY = _effected.getY();
		final int x = oldX + (int) (FEAR_RANGE * Math.cos(angle));
		final int y = oldY + (int) (FEAR_RANGE * Math.sin(angle));
		_effected.setRunning();
		_effected.moveToLocation(GeoEngine.moveCheck(oldX, oldY, _effected.getZ(), x, y, _effected.getGeoIndex()), 0, false);
		return true;
	}
}
