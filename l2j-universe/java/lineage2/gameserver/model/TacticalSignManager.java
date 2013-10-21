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
package lineage2.gameserver.model;

import java.util.Map;

import lineage2.gameserver.network.serverpackets.ExTacticalSign;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TacticalSignManager
{
	/**
	 * Method setTacticalSign.
	 * @param player Player
	 * @param target GameObject
	 * @param signId int
	 */
	public static void setTacticalSign(Player player, GameObject target, int signId)
	{
		if ((player.getTarget() == null) || (player.getParty() == null))
		{
			return;
		}
		player.getParty().getTacticalSignsList().put(signId, target);
		for (Player partyChar : player.getParty())
		{
			partyChar.sendPacket(new ExTacticalSign(target.getObjectId(), signId));
		}
	}
	
	/**
	 * Method getTargetOnTacticalSign.
	 * @param player Player
	 * @param signId int
	 */
	public static void getTargetOnTacticalSign(Player player, int signId)
	{
		if (player.getParty() != null)
		{
			GameObject newTarget = player.getParty().getTacticalSignsList().get(signId);
			player.setTarget(newTarget);
		}
	}
	
	/**
	 * Method getSignOnTarget.
	 * @param target GameObject
	 * @param party Party
	 * @return int
	 */
	public static int getSignOnTarget(GameObject target, Party party)
	{
		if (party != null)
		{
			for (Map.Entry<Integer, GameObject> entry : party.getTacticalSignsList().entrySet())
			{
				if (entry.getValue() == target)
				{
					return entry.getKey();
				}
			}
		}
		return 0;
	}
	
	/**
	 * Method getTargetOnSign.
	 * @param signId int
	 * @param party Party
	 * @return GameObject
	 */
	public static GameObject getTargetOnSign(int signId, Party party)
	{
		return party != null ? party.getTacticalSignsList().get(signId) : null;
	}
}
