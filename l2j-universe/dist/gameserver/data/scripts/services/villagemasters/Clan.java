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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Clan extends Functions
{
	/**
	 * Method CheckCreateClan.
	 */
	public void CheckCreateClan()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "clan-02.htm";
		if (pl.getLevel() <= 9)
		{
			htmltext = "clan-06.htm";
		}
		else if (pl.isClanLeader())
		{
			htmltext = "clan-07.htm";
		}
		else if (pl.getClan() != null)
		{
			htmltext = "clan-09.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/clan/" + htmltext);
	}
	
	/**
	 * Method CheckDissolveClan.
	 */
	public void CheckDissolveClan()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		String htmltext = "clan-01.htm";
		if (pl.isClanLeader())
		{
			htmltext = "clan-04.htm";
		}
		else if (pl.getClan() != null)
		{
			htmltext = "clan-08.htm";
		}
		else
		{
			htmltext = "clan-11.htm";
		}
		((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/clan/" + htmltext);
	}
}
