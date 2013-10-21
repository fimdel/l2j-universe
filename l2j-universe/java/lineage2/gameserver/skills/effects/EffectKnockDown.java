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

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExAlterSkillRequest;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.FlyToLocation.FlyType;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectKnockDown extends Effect
{
	/**
	 * Field _z. Field _y. Field _x.
	 */
	private int _x, _y, _z;


	private static TIntIntHashMap _ChainKnockSkills = new TIntIntHashMap(8);
	
	private static TIntIntHashMap _ChainedTemporalReplace = new TIntIntHashMap(8);
	/**
	 * Constructor for EffectKnockDown.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectKnockDown(Env env, EffectTemplate template)
	{
		super(env, template);
		_ChainKnockSkills.clear();
		_ChainedTemporalReplace.clear();
		_ChainKnockSkills.put(139, 10250);
		_ChainKnockSkills.put(140, 10500);
		_ChainKnockSkills.put(141, 10750);
		_ChainKnockSkills.put(142, 11000);
		_ChainKnockSkills.put(143, 11249);
		_ChainKnockSkills.put(144, 11750);
		_ChainKnockSkills.put(145, 11500);
		_ChainKnockSkills.put(146, 12000);
		_ChainedTemporalReplace.put(10250, 10009);
		_ChainedTemporalReplace.put(10500, 10258);
		_ChainedTemporalReplace.put(10750, 10508);
		_ChainedTemporalReplace.put(11000, 10760);
		_ChainedTemporalReplace.put(11249, 11011);
		_ChainedTemporalReplace.put(11750, 11510);
		_ChainedTemporalReplace.put(11500, 11273);
		_ChainedTemporalReplace.put(12000, 11766);
	}

	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (_effected.isAirBinded() || _effected.isKnockedDown())
		{
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
		Location playerLoc = _effected.getLoc();
		Location tagetLoc = getEffector().getLoc();
		double distance = playerLoc.distance(tagetLoc);
		if ((distance > 2000) || (distance < 1))
		{
			return;
		}
		double dx = tagetLoc.x - playerLoc.x;
		double dy = tagetLoc.y - playerLoc.y;
		double dz = tagetLoc.z - playerLoc.z;
		int offset = Math.min((int) distance + getSkill().getFlyRadius(), 1400);
		offset = (int) (offset + Math.abs(dz));
		if (offset < 5)
		{
			offset = 5;
		}
		double sin = dy / distance;
		double cos = dx / distance;
		_x = (tagetLoc.x - (int) (offset * cos));
		_y = (tagetLoc.y - (int) (offset * sin));
		_z = tagetLoc.z;
		Location loc = new Location(_x, _y, _z);
		loc = GeoEngine.moveCheck(tagetLoc.x, tagetLoc.y, tagetLoc.z, _x, _y, _effected.getGeoIndex());
		if(!_effected.isKnockedDown())
			_effected.startKnockingdown();
		for(Player playerNearEffected : World.getAroundPlayers(_effected, 1200, 400))//Need to check: When the target has been hitted by another Knock Down skill, don't trigger chain skill
		{
			if(playerNearEffected.getTarget() == _effected && playerNearEffected.isAwaking())
			{
				int chainSkill = _ChainKnockSkills.get(playerNearEffected.getClassId().getId());
				int temporalReplaceSkill = _ChainedTemporalReplace.get(chainSkill);
				playerNearEffected.sendPacket(new ExAlterSkillRequest(chainSkill,temporalReplaceSkill,3));	
			}
		}
		if (loc == null)
			_log.info("EffectKnockDown Loc null check this!");
		_effected.broadcastPacket(new FlyToLocation(_effected, loc, FlyType.PUSH_DOWN_HORIZONTAL, getSkill().getFlySpeed()));//need to check: if the effected is already knocked down, move the target again?
		_effected.abortAttack(true, true);
		_effected.abortCast(true, true);
		_effected.setXYZ(loc.getX(), loc.getY(), loc.getZ());
		_effected.broadcastPacket(new ValidateLocation(_effected));
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if(_effected.isKnockedDown())
			_effected.stopKnockingdown();
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
