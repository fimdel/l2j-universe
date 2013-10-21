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
package services.villagemasters;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.VillageMasterInstance;
import lineage2.gameserver.scripts.Functions;

public class Ally extends Functions
{
	/**
	 * Method CheckCreateAlly.
	 */
	public void CheckCreateAlly()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "ally-01.htm";
		if (pl.isClanLeader())
		{
			htmltext = "ally-02.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/ally/" + htmltext);
	}
	
	/**
	 * Method CheckDissolveAlly.
	 */
	public void CheckDissolveAlly()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "ally-01.htm";
		if (pl.isAllyLeader())
		{
			htmltext = "ally-03.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/ally/" + htmltext);
	}
}
