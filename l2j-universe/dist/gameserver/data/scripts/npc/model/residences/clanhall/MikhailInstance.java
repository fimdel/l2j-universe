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

import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MikhailInstance extends _34BossMinionInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for MikhailInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MikhailInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method spawnChatSay.
	 * @return NpcString
	 */
	@Override
	public NpcString spawnChatSay()
	{
		return NpcString.GLORY_TO_ADEN_THE_KINGDOM_OF_THE_LION_GLORY_TO_SIR_GUSTAV_OUR_IMMORTAL_LORD;
	}
	
	/**
	 * Method teleChatSay.
	 * @return NpcString * @see npc.model.residences.clanhall._34SiegeGuard#teleChatSay()
	 */
	@Override
	public NpcString teleChatSay()
	{
		return NpcString.COULD_IT_BE_THAT_I_HAVE_REACHED_MY_END_I_CANNOT_DIE_WITHOUT_HONOR_WITHOUT_THE_PERMISSION_OF_SIR_GUSTAV;
	}
}
