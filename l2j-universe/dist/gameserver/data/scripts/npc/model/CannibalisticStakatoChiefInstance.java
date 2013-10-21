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
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CannibalisticStakatoChiefInstance extends RaidBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ITEMS.
	 */
	private static final int ITEMS[] =
	{
		14833,
		14834
	};
	
	/**
	 * Constructor for CannibalisticStakatoChiefInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CannibalisticStakatoChiefInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (killer == null)
		{
			return;
		}
		Creature topdam = getAggroList().getTopDamager();
		if (topdam == null)
		{
			topdam = killer;
		}
		Player pc = topdam.getPlayer();
		if (pc == null)
		{
			return;
		}
		Party party = pc.getParty();
		int itemId;
		if (party != null)
		{
			for (Player partyMember : party.getPartyMembers())
			{
				if ((partyMember != null) && pc.isInRange(partyMember, Config.ALT_PARTY_DISTRIBUTION_RANGE))
				{
					itemId = ITEMS[Rnd.get(ITEMS.length)];
					partyMember.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(itemId));
					partyMember.getInventory().addItem(itemId, 1);
				}
			}
		}
		else
		{
			itemId = ITEMS[Rnd.get(ITEMS.length)];
			pc.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(itemId));
			pc.getInventory().addItem(itemId, 1);
		}
	}
}
