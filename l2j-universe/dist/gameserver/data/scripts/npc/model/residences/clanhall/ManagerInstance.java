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
package npc.model.residences.clanhall;

import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.AgitDecoInfo;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.templates.npc.NpcTemplate;
import npc.model.residences.ResidenceManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManagerInstance extends ResidenceManager
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for ManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method getResidence.
	 * @return Residence
	 */
	@Override
	protected Residence getResidence()
	{
		return getClanHall();
	}
	
	/**
	 * Method decoPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket decoPacket()
	{
		ClanHall clanHall = getClanHall();
		if (clanHall != null)
		{
			return new AgitDecoInfo(clanHall);
		}
		return null;
	}
	
	/**
	 * Method getPrivUseFunctions.
	 * @return int
	 */
	@Override
	protected int getPrivUseFunctions()
	{
		return Clan.CP_CH_USE_FUNCTIONS;
	}
	
	/**
	 * Method getPrivSetFunctions.
	 * @return int
	 */
	@Override
	protected int getPrivSetFunctions()
	{
		return Clan.CP_CH_SET_FUNCTIONS;
	}
	
	/**
	 * Method getPrivDismiss.
	 * @return int
	 */
	@Override
	protected int getPrivDismiss()
	{
		return Clan.CP_CH_DISMISS;
	}
	
	/**
	 * Method getPrivDoors.
	 * @return int
	 */
	@Override
	protected int getPrivDoors()
	{
		return Clan.CP_CH_ENTRY_EXIT;
	}
}
