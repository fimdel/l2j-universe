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
package lineage2.gameserver.listener.zone.impl;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.entity.residence.ResidenceFunction;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncMul;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ResidenceEnterLeaveListenerImpl implements OnZoneEnterLeaveListener
{
	/**
	 * Field STATIC.
	 */
	public static final OnZoneEnterLeaveListener STATIC = new ResidenceEnterLeaveListenerImpl();
	
	/**
	 * Method onZoneEnter.
	 * @param zone Zone
	 * @param actor Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
	 */
	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		if (!actor.isPlayer())
		{
			return;
		}
		Player player = (Player) actor;
		Residence residence = (Residence) zone.getParams().get("residence");
		if ((residence.getOwner() == null) || (residence.getOwner() != player.getClan()))
		{
			return;
		}
		if (residence.isFunctionActive(ResidenceFunction.RESTORE_HP))
		{
			double value = 1. + (residence.getFunction(ResidenceFunction.RESTORE_HP).getLevel() / 100.);
			player.addStatFunc(new FuncMul(Stats.REGENERATE_HP_RATE, 0x30, residence, value));
		}
		if (residence.isFunctionActive(ResidenceFunction.RESTORE_MP))
		{
			double value = 1. + (residence.getFunction(ResidenceFunction.RESTORE_MP).getLevel() / 100.);
			player.addStatFunc(new FuncMul(Stats.REGENERATE_MP_RATE, 0x30, residence, value));
		}
	}
	
	/**
	 * Method onZoneLeave.
	 * @param zone Zone
	 * @param actor Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
	 */
	@Override
	public void onZoneLeave(Zone zone, Creature actor)
	{
		if (!actor.isPlayer())
		{
			return;
		}
		Residence residence = (Residence) zone.getParams().get("residence");
		actor.removeStatsOwner(residence);
	}
}
