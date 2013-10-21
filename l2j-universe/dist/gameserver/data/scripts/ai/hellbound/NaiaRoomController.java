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
package ai.hellbound;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.naia.NaiaTowerManager;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaRoomController extends DefaultAI
{
	/**
	 * Constructor for NaiaRoomController.
	 * @param actor NpcInstance
	 */
	public NaiaRoomController(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	public boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		final int npcId = actor.getNpcId();
		if (NaiaTowerManager.isLockedRoom(npcId))
		{
			final List<NpcInstance> _roomMobs = NaiaTowerManager.getRoomMobs(npcId);
			if (_roomMobs == null)
			{
				return false;
			}
			if (!_roomMobs.isEmpty())
			{
				for (NpcInstance npc : _roomMobs)
				{
					if ((npc == null) || !npc.isDead())
					{
						return false;
					}
				}
			}
			switch (npcId)
			{
				case 18494:
				{
					ReflectionUtils.getDoor(18250002).openMe();
					ReflectionUtils.getDoor(18250003).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18495:
				{
					ReflectionUtils.getDoor(18250004).openMe();
					ReflectionUtils.getDoor(18250005).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18496:
				{
					ReflectionUtils.getDoor(18250006).openMe();
					ReflectionUtils.getDoor(18250007).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18497:
				{
					ReflectionUtils.getDoor(18250008).openMe();
					ReflectionUtils.getDoor(18250009).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18498:
				{
					ReflectionUtils.getDoor(18250010).openMe();
					ReflectionUtils.getDoor(18250011).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18499:
				{
					ReflectionUtils.getDoor(18250101).openMe();
					ReflectionUtils.getDoor(18250013).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18500:
				{
					ReflectionUtils.getDoor(18250014).openMe();
					ReflectionUtils.getDoor(18250015).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18501:
				{
					ReflectionUtils.getDoor(18250102).openMe();
					ReflectionUtils.getDoor(18250017).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18502:
				{
					ReflectionUtils.getDoor(18250018).openMe();
					ReflectionUtils.getDoor(18250019).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18503:
				{
					ReflectionUtils.getDoor(18250103).openMe();
					ReflectionUtils.getDoor(18250021).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18504:
				{
					ReflectionUtils.getDoor(18250022).openMe();
					ReflectionUtils.getDoor(18250023).openMe();
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				case 18505:
				{
					ReflectionUtils.getDoor(18250024).openMe();
					ThreadPoolManager.getInstance().schedule(new LastDoorClose(), 300 * 1000L);
					NaiaTowerManager.unlockRoom(npcId);
					NaiaTowerManager.removeRoomMobs(npcId);
					break;
				}
				default:
					break;
			}
		}
		return true;
	}
	
	/**
	 * @author Mobius
	 */
	static private class LastDoorClose extends RunnableImpl
	{
		/**
		 * Constructor for LastDoorClose.
		 */
		LastDoorClose()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			ReflectionUtils.getDoor(18250024).closeMe();
		}
	}
}
