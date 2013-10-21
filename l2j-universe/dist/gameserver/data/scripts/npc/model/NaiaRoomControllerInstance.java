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
package npc.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.geometry.Rectangle;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.naia.NaiaTowerManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaRoomControllerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _room1territory.
	 */
	private static final Territory _room1territory = new Territory().add(new Rectangle(-46652, 245576, -45735, 246648).setZmin(-9175).setZmax(-9075));
	/**
	 * Field _room3territory.
	 */
	private static final Territory _room3territory = new Territory().add(new Rectangle(-52088, 245667, -51159, 246609).setZmin(-10037).setZmax(-9837));
	/**
	 * Field _room5territory.
	 */
	private static final Territory _room5territory = new Territory().add(new Rectangle(-46652, 245596, -45737, 246626).setZmin(-10032).setZmax(-9832));
	/**
	 * Field _room6territory.
	 */
	private static final Territory _room6territory = new Territory().add(new Rectangle(-49220, 247903, -48647, 248543).setZmin(-10027).setZmax(-9827));
	/**
	 * Field _room7territory.
	 */
	private static final Territory _room7territory = new Territory().add(new Rectangle(-52068, 245575, -51195, 246617).setZmin(-10896).setZmax(-10696));
	/**
	 * Field _room8territory.
	 */
	private static final Territory _room8territory = new Territory().add(new Rectangle(-49284, 243788, -48592, 244408).setZmin(-10892).setZmax(-10692));
	/**
	 * Field _room9territory.
	 */
	private static final Territory _room9territory = new Territory().add(new Rectangle(-46679, 245661, -45771, 246614).setZmin(-11756).setZmax(-11556));
	/**
	 * Field _room10territory.
	 */
	private static final Territory _room10territory = new Territory().add(new Rectangle(-49252, 247894, -48587, 248519).setZmin(-11757).setZmax(-11757));
	/**
	 * Field _room11territory.
	 */
	private static final Territory _room11territory = new Territory().add(new Rectangle(-52080, 245665, -51174, 246660).setZmin(-12619).setZmax(-12419));
	/**
	 * Field _room12territory.
	 */
	private static final Territory _room12territory = new Territory().add(new Rectangle(-48732, 243186, -47752, 244097).setZmin(-13423).setZmax(-13223));
	/**
	 * Field _roomMobList.
	 */
	private static List<NpcInstance> _roomMobList;
	/**
	 * Field _room2locs.
	 */
	private static final Location[] _room2locs =
	{
		new Location(-48146, 249597, -9124, -16280),
		new Location(-48144, 248711, -9124, 16368),
		new Location(-48704, 249597, -9104, -16380),
		new Location(-49219, 249596, -9104, -16400),
		new Location(-49715, 249601, -9104, -16360),
		new Location(-49714, 248696, -9104, 15932),
		new Location(-49225, 248710, -9104, 16512),
		new Location(-48705, 248708, -9104, 16576),
	};
	/**
	 * Field _room4locs.
	 */
	private static final Location[] _room4locs =
	{
		new Location(-49754, 243866, -9968, -16328),
		new Location(-49754, 242940, -9968, 16336),
		new Location(-48733, 243858, -9968, -16208),
		new Location(-48745, 242936, -9968, 16320),
		new Location(-49264, 242946, -9968, 16312),
		new Location(-49268, 243869, -9968, -16448),
		new Location(-48186, 242934, -9968, 16576),
		new Location(-48185, 243855, -9968, -16448),
	};
	
	/**
	 * Constructor for NaiaRoomControllerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public NaiaRoomControllerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		Location kickLoc = new Location(17656, 244328, 11595);
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("challengeroom"))
		{
			if (!NaiaTowerManager.isLegalGroup(player))
			{
				if (player.isInParty())
				{
					for (Player member : player.getParty().getPartyMembers())
					{
						member.teleToLocation(kickLoc);
					}
					return;
				}
				player.teleToLocation(kickLoc);
				return;
			}
			int npcId = getNpcId();
			if (NaiaTowerManager.isRoomDone(npcId, player))
			{
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Ingenious Contraption:<br><br>The room is already challenged."));
				return;
			}
			switch (npcId)
			{
				case 18494:
				{
					ReflectionUtils.getDoor(18250001).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22393, 3, _room1territory, npcId);
					spawnToRoom(22394, 3, _room1territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					break;
				}
				case 18495:
				{
					ReflectionUtils.getDoor(18250002).closeMe();
					ReflectionUtils.getDoor(18250003).closeMe();
					_roomMobList = new ArrayList<>();
					for (Location _room2loc : _room2locs)
					{
						spawnExactToRoom(22439, _room2loc, npcId);
					}
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18496:
				{
					ReflectionUtils.getDoor(18250004).closeMe();
					ReflectionUtils.getDoor(18250005).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22441, 2, _room3territory, npcId);
					spawnToRoom(22442, 2, _room3territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18497:
				{
					ReflectionUtils.getDoor(18250006).closeMe();
					ReflectionUtils.getDoor(18250007).closeMe();
					_roomMobList = new ArrayList<>();
					for (Location _room4loc : _room4locs)
					{
						spawnExactToRoom(22440, _room4loc, npcId);
					}
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18498:
				{
					ReflectionUtils.getDoor(18250008).closeMe();
					ReflectionUtils.getDoor(18250009).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22411, 2, _room5territory, npcId);
					spawnToRoom(22393, 2, _room5territory, npcId);
					spawnToRoom(22394, 2, _room5territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18499:
				{
					ReflectionUtils.getDoor(18250010).closeMe();
					ReflectionUtils.getDoor(18250011).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22395, 2, _room6territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18500:
				{
					ReflectionUtils.getDoor(18250101).closeMe();
					ReflectionUtils.getDoor(18250013).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22393, 3, _room7territory, npcId);
					spawnToRoom(22394, 3, _room7territory, npcId);
					spawnToRoom(22412, 1, _room7territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18501:
				{
					ReflectionUtils.getDoor(18250014).closeMe();
					ReflectionUtils.getDoor(18250015).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22395, 2, _room8territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18502:
				{
					ReflectionUtils.getDoor(18250102).closeMe();
					ReflectionUtils.getDoor(18250017).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22441, 2, _room9territory, npcId);
					spawnToRoom(22442, 3, _room9territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18503:
				{
					ReflectionUtils.getDoor(18250018).closeMe();
					ReflectionUtils.getDoor(18250019).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22395, 2, _room10territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18504:
				{
					ReflectionUtils.getDoor(18250103).closeMe();
					ReflectionUtils.getDoor(18250021).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(22413, 6, _room11territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.updateGroupTimer(player);
					break;
				}
				case 18505:
				{
					ReflectionUtils.getDoor(18250022).closeMe();
					ReflectionUtils.getDoor(18250023).closeMe();
					_roomMobList = new ArrayList<>();
					spawnToRoom(18490, 12, _room12territory, npcId);
					NaiaTowerManager.lockRoom(npcId);
					NaiaTowerManager.addRoomDone(npcId, player);
					NaiaTowerManager.addMobsToRoom(npcId, _roomMobList);
					NaiaTowerManager.removeGroupTimer(player);
					break;
				}
				default:
					break;
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method spawnToRoom.
	 * @param mobId int
	 * @param count int
	 * @param territory Territory
	 * @param roomId int
	 */
	private void spawnToRoom(int mobId, int count, Territory territory, int roomId)
	{
		for (int i = 0; i < count; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(mobId);
				sp.setLoc(Territory.getRandomLoc(territory).setH(Rnd.get(65535)));
				sp.doSpawn(true);
				sp.stopRespawn();
				_roomMobList.add(sp.getLastSpawn());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method spawnExactToRoom.
	 * @param mobId int
	 * @param loc Location
	 * @param roomId int
	 */
	private void spawnExactToRoom(int mobId, Location loc, int roomId)
	{
		try
		{
			SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(mobId));
			sp.setLoc(loc);
			sp.doSpawn(true);
			sp.stopRespawn();
			_roomMobList.add(sp.getLastSpawn());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		return "default/18494.htm";
	}
}
