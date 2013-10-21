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
package lineage2.gameserver.scripts;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Scripts.ScriptClassAndMethod;
import lineage2.gameserver.utils.Strings;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Events
{
	/**
	 * Method onAction.
	 * @param player Player
	 * @param obj GameObject
	 * @param shift boolean
	 * @return boolean
	 */
	public static boolean onAction(Player player, GameObject obj, boolean shift)
	{
		if (shift)
		{
			if (player.getVarB("noShift"))
			{
				return false;
			}
			ScriptClassAndMethod handler = Scripts.onActionShift.get(obj.getL2ClassShortName());
			if ((handler == null) && obj.isNpc())
			{
				handler = Scripts.onActionShift.get("NpcInstance");
			}
			if ((handler == null) && obj.isPet())
			{
				handler = Scripts.onActionShift.get("PetInstance");
			}
			if (handler == null)
			{
				return false;
			}
			return Strings.parseBoolean(Scripts.getInstance().callScripts(player, handler.className, handler.methodName, new Object[]
			{
				player,
				obj
			}));
		}
		ScriptClassAndMethod handler = Scripts.onAction.get(obj.getL2ClassShortName());
		if ((handler == null) && obj.isDoor())
		{
			handler = Scripts.onAction.get("DoorInstance");
		}
		if (handler == null)
		{
			return false;
		}
		return Strings.parseBoolean(Scripts.getInstance().callScripts(player, handler.className, handler.methodName, new Object[]
		{
			player,
			obj
		}));
	}
}
