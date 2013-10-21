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
package ai.door;

import lineage2.gameserver.ai.DoorAI;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.ConfirmDlg;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ResidenceDoor extends DoorAI
{
	/**
	 * Constructor for ResidenceDoor.
	 * @param actor DoorInstance
	 */
	public ResidenceDoor(DoorInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtTwiceClick.
	 * @param player Player
	 */
	@Override
	public void onEvtTwiceClick(final Player player)
	{
		final DoorInstance door = getActor();
		final Residence residence = ResidenceHolder.getInstance().getResidence(door.getTemplate().getAIParams().getInteger("residence_id"));
		if ((residence.getOwner() != null) && (player.getClan() != null) && (player.getClan().equals(residence.getOwner())) && ((player.getClanPrivileges() & Clan.CP_CS_ENTRY_EXIT) == Clan.CP_CS_ENTRY_EXIT))
		{
			final SystemMsg msg = door.isOpen() ? SystemMsg.WOULD_YOU_LIKE_TO_CLOSE_THE_GATE : SystemMsg.WOULD_YOU_LIKE_TO_OPEN_THE_GATE;
			player.ask(new ConfirmDlg(msg, 0), new OnAnswerListener()
			{
				@Override
				public void sayYes()
				{
					if (door.isOpen())
					{
						door.closeMe(player, true);
					}
					else
					{
						door.openMe(player, true);
					}
				}
				
				@Override
				public void sayNo()
				{
					// empty method
				}
			});
		}
	}
}
