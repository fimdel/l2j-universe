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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeducedInvestigatorInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for SeducedInvestigatorInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SeducedInvestigatorInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(true);
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
		player.sendPacket(new NpcHtmlMessage(player, this, "common/seducedinvestigator.htm", val));
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player player = attacker.getPlayer();
		if (player == null)
		{
			return false;
		}
		if (player.isPlayable())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
