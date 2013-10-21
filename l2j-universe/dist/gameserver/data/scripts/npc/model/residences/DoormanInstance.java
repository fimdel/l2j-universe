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
package npc.model.residences;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class DoormanInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field COND_OWNER. (value is 0)
	 */
	protected static final int COND_OWNER = 0;
	/**
	 * Field COND_SIEGE. (value is 1)
	 */
	protected static final int COND_SIEGE = 1;
	/**
	 * Field COND_FAIL. (value is 2)
	 */
	protected static final int COND_FAIL = 2;
	/**
	 * Field _siegeDialog.
	 */
	protected String _siegeDialog;
	/**
	 * Field _mainDialog.
	 */
	protected String _mainDialog;
	/**
	 * Field _failDialog.
	 */
	protected String _failDialog;
	/**
	 * Field _doors.
	 */
	protected int[] _doors;
	
	/**
	 * Constructor for DoormanInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DoormanInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setDialogs();
		_doors = template.getAIParams().getIntegerArray("doors");
	}
	
	/**
	 * Method setDialogs.
	 */
	public void setDialogs()
	{
		_siegeDialog = getTemplate().getAIParams().getString("siege_dialog");
		_mainDialog = getTemplate().getAIParams().getString("main_dialog");
		_failDialog = getTemplate().getAIParams().getString("fail_dialog");
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				if (command.equalsIgnoreCase("openDoors"))
				{
					for (int i : _doors)
					{
						ReflectionUtils.getDoor(i).openMe();
					}
				}
				else if (command.equalsIgnoreCase("closeDoors"))
				{
					for (int i : _doors)
					{
						ReflectionUtils.getDoor(i).closeMe();
					}
				}
				break;
			case COND_SIEGE:
				player.sendPacket(new NpcHtmlMessage(player, this, _siegeDialog, 0));
				break;
			case COND_FAIL:
				player.sendPacket(new NpcHtmlMessage(player, this, _failDialog, 0));
				break;
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		String filename = null;
		int cond = getCond(player);
		switch (cond)
		{
			case COND_OWNER:
				filename = _mainDialog;
				break;
			case COND_SIEGE:
				filename = _siegeDialog;
				break;
			case COND_FAIL:
				filename = _failDialog;
				break;
		}
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
	
	/**
	 * Method getCond.
	 * @param player Player
	 * @return int
	 */
	protected int getCond(Player player)
	{
		Residence residence = getResidence();
		Clan residenceOwner = residence.getOwner();
		if ((residenceOwner != null) && (player.getClan() == residenceOwner) && ((player.getClanPrivileges() & getOpenPriv()) == getOpenPriv()))
		{
			if (residence.getSiegeEvent().isInProgress())
			{
				return COND_SIEGE;
			}
			return COND_OWNER;
		}
		return COND_FAIL;
	}
	
	/**
	 * Method getOpenPriv.
	 * @return int
	 */
	public abstract int getOpenPriv();
	
	/**
	 * Method getResidence.
	 * @return Residence
	 */
	public abstract Residence getResidence();
}
