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

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectTargetToMe extends Effect
{
	/**
	 * Constructor for EffectTargetToMe.
	 * @param env Env
	 * @param template EffectTemplate
	 * 
	 */

	/**
	 * Field _z. Field _y. Field _x.
	 */
	
	private int _x,_y,_z;
	
	public EffectTargetToMe(Env env, EffectTemplate template)
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
		if(!_effected.isPulledNow())
			_effected.startPulling();
        ThreadPoolManager.getInstance().schedule(new Runnable()
        { 
    	    @Override
            public void run()
            {    			
    			Location flyLoc = _effected.getFlyLocation(getEffector(), getSkill());
    			_effected.abortCast(true, true);
    			if (flyLoc == null)
    				_log.info("EffectTargetToMe Loc null check this!");
    			_effected.broadcastPacket(new FlyToLocation(_effected, flyLoc, getSkill().getFlyType(),getSkill().getFlySpeed()));
    			_x = flyLoc.getX();
    			_y = flyLoc.getY();
    			_z = flyLoc.getZ();
    			_effected.setXYZ(flyLoc.getX(),flyLoc.getY(),flyLoc.getZ());
    			_effected.broadcastPacket(new ValidateLocation(_effected));
            }     

        }, 500L);
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		_effected.setXYZ(_x, _y, _z);
		_effected.broadcastPacket(new ValidateLocation(_effected));
		if(_effected.isPulledNow())
			_effected.stopPulling();
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
