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
package lineage2.gameserver.model.petition;

import lineage2.gameserver.handler.petition.IPetitionHandler;
import lineage2.gameserver.scripts.Scripts;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetitionSubGroup extends PetitionGroup
{
	/**
	 * Field _handler.
	 */
	private final IPetitionHandler _handler;
	
	/**
	 * Constructor for PetitionSubGroup.
	 * @param id int
	 * @param handler String
	 */
	public PetitionSubGroup(int id, String handler)
	{
		super(id);
		Class<?> clazz = Scripts.getInstance().getClasses().get("handler.petition." + handler);
		try
		{
			_handler = (IPetitionHandler) clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new Error(e);
		}
	}
	
	/**
	 * Method getHandler.
	 * @return IPetitionHandler
	 */
	public IPetitionHandler getHandler()
	{
		return _handler;
	}
}
