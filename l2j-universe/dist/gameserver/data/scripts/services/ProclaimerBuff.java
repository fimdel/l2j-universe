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
package services;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Pandragon
 * @version $Revision: 1.0 $
 */
public class ProclaimerBuff extends Functions
{
	/**
	 * Method getProclaimerBlessing.
	 */
	public void getProclaimerBlessing()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if (player.getKarma() > 0)
		{
			return;
		}
		npc.doCast(SkillTable.getInstance().getInfo(19036, 1), player, true);
	}
}
