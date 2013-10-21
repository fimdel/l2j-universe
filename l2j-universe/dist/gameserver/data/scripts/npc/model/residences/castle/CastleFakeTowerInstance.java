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
package npc.model.residences.castle;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleFakeTowerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for CastleFakeTowerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CastleFakeTowerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param player Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature player)
	{
		return false;
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
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param filename String
	 * @param replace Object[]
	 */
	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}
	
	/**
	 * Method hasRandomAnimation.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return true;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
}
