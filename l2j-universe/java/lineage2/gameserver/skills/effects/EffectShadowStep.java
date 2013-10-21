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

import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.FlyToLocation.FlyType;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectShadowStep extends Effect
{
	/**
	 * Field _z. Field _y. Field _x.
	 */
	private int _x, _y, _z;
	
	/**
	 * Constructor for EffectKnockBack.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectShadowStep(Env env, EffectTemplate template)
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
		
		int px = _effected.getX();
		int py = _effected.getY();
		
		double ph = Util.convertHeadingToDegree(_effected.getHeading());
		ph += 180;
		if (ph > 360)
		{
			ph -= 360;
		}
		ph = (Math.PI * ph) / 180;
		
		_x = (int) (px + (30 * Math.cos(ph)));
		_y = (int) (py + (30 * Math.sin(ph)));
		_z = _effected.getZ();
		
		Location loc = new Location(_x, _y, _z);
		loc = GeoEngine.moveCheck(_effector.getX(), _effector.getY(), _effector.getZ(), _x, _y, _effector.getGeoIndex());
		
		_x = loc.getX();
		_y = loc.getY();
		_z = loc.getZ();
		
		_effector.broadcastPacket(new FlyToLocation(_effector, loc, FlyType.DUMMY, getSkill().getFlySpeed()));
		_effector.abortAttack(true, false);
		_effector.abortCast(true, false);
		_effector.setXYZ(_x, _y, _z);
		_effector.setHeading(_effected.getHeading());
		_effector.broadcastPacket(new ValidateLocation(_effector));
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
