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
package lineage2.gameserver.model.entity.events.actions;

import java.util.List;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.network.serverpackets.PlaySound;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlaySoundAction implements EventAction
{
	/**
	 * Field _range.
	 */
	private final int _range;
	/**
	 * Field _sound.
	 */
	private final String _sound;
	/**
	 * Field _type.
	 */
	private final PlaySound.Type _type;
	
	/**
	 * Constructor for PlaySoundAction.
	 * @param range int
	 * @param s String
	 * @param type PlaySound.Type
	 */
	public PlaySoundAction(int range, String s, PlaySound.Type type)
	{
		_range = range;
		_sound = s;
		_type = type;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		GameObject object = event.getCenterObject();
		PlaySound packet = null;
		if (object != null)
		{
			packet = new PlaySound(_type, _sound, 1, object.getObjectId(), object.getLoc());
		}
		else
		{
			packet = new PlaySound(_type, _sound, 0, 0, 0, 0, 0);
		}
		List<Player> players = event.broadcastPlayers(_range);
		for (Player player : players)
		{
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
}
