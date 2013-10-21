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
package lineage2.gameserver.listener.actor.player.impl;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Scripts;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ScriptAnswerListener implements OnAnswerListener
{
	/**
	 * Field _playerRef.
	 */
	private final HardReference<Player> _playerRef;
	/**
	 * Field _scriptName.
	 */
	private final String _scriptName;
	/**
	 * Field _arg.
	 */
	private final Object[] _arg;
	
	/**
	 * Constructor for ScriptAnswerListener.
	 * @param player Player
	 * @param scriptName String
	 * @param arg Object[]
	 */
	public ScriptAnswerListener(Player player, String scriptName, Object[] arg)
	{
		_scriptName = scriptName;
		_arg = arg;
		_playerRef = player.getRef();
	}
	
	/**
	 * Method sayYes.
	 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayYes()
	 */
	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if (player == null)
		{
			return;
		}
		Scripts.getInstance().callScripts(player, _scriptName.split(":")[0], _scriptName.split(":")[1], _arg);
	}
	
	/**
	 * Method sayNo.
	 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayNo()
	 */
	@Override
	public void sayNo()
	{
	}
}
