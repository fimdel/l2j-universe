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
public class DietrichInstance extends _34BossMinionInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for DietrichInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DietrichInstance(int objectId, NpcTemplate template)
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
		return NpcString.SOLDIERS_OF_GUSTAV_GO_FORTH_AND_DESTROY_THE_INVADERS;
	}
	
	/**
	 * Method teleChatSay.
	 * @return NpcString * @see npc.model.residences.clanhall._34SiegeGuard#teleChatSay()
	 */
	@Override
	public NpcString teleChatSay()
	{
		return NpcString.AH_THE_BITTER_TASTE_OF_DEFEAT_I_FEAR_MY_TORMENTS_ARE_NOT_OVER;
	}
}
