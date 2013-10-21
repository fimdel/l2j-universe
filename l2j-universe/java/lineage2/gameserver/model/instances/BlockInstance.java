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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BlockInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _isRed.
	 */
	private boolean _isRed;
	
	/**
	 * Constructor for BlockInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BlockInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method isRed.
	 * @return boolean
	 */
	public boolean isRed()
	{
		return _isRed;
	}
	
	/**
	 * Method setRed.
	 * @param red boolean
	 */
	public void setRed(boolean red)
	{
		_isRed = red;
		broadcastCharInfo();
	}
	
	/**
	 * Method changeColor.
	 */
	public void changeColor()
	{
		setRed(!_isRed);
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
	 * Method isNameAbove.
	 * @return boolean
	 */
	@Override
	public boolean isNameAbove()
	{
		return false;
	}
	
	/**
	 * Method getFormId.
	 * @return int
	 */
	@Override
	public int getFormId()
	{
		return _isRed ? 0x53 : 0;
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
}
