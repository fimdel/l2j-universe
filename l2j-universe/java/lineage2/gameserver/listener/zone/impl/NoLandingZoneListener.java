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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.PetDataTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NoLandingZoneListener implements OnZoneEnterLeaveListener
{
	/**
	 * Field STATIC.
	 */
	public static final OnZoneEnterLeaveListener STATIC = new NoLandingZoneListener();
	
	/**
	 * Method onZoneEnter.
	 * @param zone Zone
	 * @param actor Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
	 */
	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		Player player = actor.getPlayer();
		if (player != null)
		{
			if (player.isFlying() && (player.getMountNpcId() == PetDataTable.WYVERN_ID))
			{
				Residence residence = ResidenceHolder.getInstance().getResidence(zone.getParams().getInteger("residence", 0));
				if ((residence != null) && (player.getClan() != null) && (residence.getOwner() == player.getClan()))
				{
				}
				else
				{
					player.stopMove();
					player.sendPacket(SystemMsg.THIS_AREA_CANNOT_BE_ENTERED_WHILE_MOUNTED_ATOP_OF_A_WYVERN);
					player.setMount(0, 0, 0);
				}
			}
		}
	}
	
	/**
	 * Method onZoneLeave.
	 * @param zone Zone
	 * @param cha Creature
	 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
	 */
	@Override
	public void onZoneLeave(Zone zone, Creature cha)
	{
	}
}
