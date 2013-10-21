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
package npc.model.residences.fortress;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnvoyInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field COND_LEADER. (value is 0)
	 */
	public static final int COND_LEADER = 0;
	/**
	 * Field COND_FAIL. (value is 1)
	 */
	public static final int COND_FAIL = 1;
	/**
	 * Field _castleId.
	 */
	private final int _castleId;
	/**
	 * Field _mainDialog.
	 */
	private final String _mainDialog;
	/**
	 * Field _failDialog.
	 */
	private final String _failDialog;
	/**
	 * Field _successContractDialog.
	 */
	private final String _successContractDialog;
	/**
	 * Field _successIndependentDialog.
	 */
	private final String _successIndependentDialog;
	/**
	 * Field _failContractDialog.
	 */
	private final String _failContractDialog;
	
	/**
	 * Constructor for EnvoyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public EnvoyInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_castleId = template.getAIParams().getInteger("castle_id");
		_mainDialog = template.getAIParams().getString("main_dialog");
		_failDialog = template.getAIParams().getString("fail_dialog");
		_successContractDialog = template.getAIParams().getString("success_contract_dialog");
		_successIndependentDialog = template.getAIParams().getString("success_independent_dialog");
		_failContractDialog = template.getAIParams().getString("fail_contract_dialog");
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
			case COND_LEADER:
				final int castleId,
				state;
				final String fileName;
				if (command.equalsIgnoreCase("yes"))
				{
					Residence castle = ResidenceHolder.getInstance().getResidence(Castle.class, _castleId);
					if (castle.getOwnerId() == 0)
					{
						castleId = -1;
						state = Fortress.NOT_DECIDED;
						fileName = _failContractDialog;
					}
					else
					{
						castleId = castle.getId();
						state = Fortress.CONTRACT_WITH_CASTLE;
						fileName = _successContractDialog;
					}
				}
				else
				{
					castleId = 0;
					state = Fortress.INDEPENDENT;
					fileName = _successIndependentDialog;
				}
				if (state != Fortress.NOT_DECIDED)
				{
					Fortress fortress = getFortress();
					fortress.setFortState(state, castleId);
					fortress.setJdbcState(JdbcEntityState.UPDATED);
					fortress.update();
					FortressSiegeEvent event = fortress.getSiegeEvent();
					event.despawnEnvoy();
				}
				player.sendPacket(new NpcHtmlMessage(player, this, fileName, 0));
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
			case COND_LEADER:
				filename = _mainDialog;
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
		Residence residence = getFortress();
		if (residence == null)
		{
			throw new IllegalArgumentException("Not find fortress: " + getNpcId() + "; loc: " + getLoc());
		}
		Clan residenceOwner = residence.getOwner();
		if ((residenceOwner != null) && (player.getClan() == residenceOwner) && (residenceOwner.getLeaderId() == player.getObjectId()))
		{
			return COND_LEADER;
		}
		return COND_FAIL;
	}
}
