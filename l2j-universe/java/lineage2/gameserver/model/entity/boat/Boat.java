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
package lineage2.gameserver.model.entity.boat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.gameserver.ai.BoatAI;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.BoatWayEvent;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Boat extends Creature
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _moveSpeed.
	 */
	private int _moveSpeed;
	/**
	 * Field _rotationSpeed.
	 */
	private int _rotationSpeed;
	/**
	 * Field _fromHome.
	 */
	protected int _fromHome;
	/**
	 * Field _runState.
	 */
	protected int _runState;
	/**
	 * Field _ways.
	 */
	private final BoatWayEvent[] _ways = new BoatWayEvent[2];
	/**
	 * Field _players.
	 */
	protected final Set<Player> _players = new CopyOnWriteArraySet<>();
	
	/**
	 * Constructor for Boat.
	 * @param objectId int
	 * @param template CharTemplate
	 */
	public Boat(int objectId, CharTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		_fromHome = 1;
		getCurrentWay().reCalcNextTime(false);
	}
	
	/**
	 * Method setXYZ.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param MoveTask boolean
	 */
	@Override
	public void setXYZ(int x, int y, int z, boolean MoveTask)
	{
		super.setXYZ(x, y, z, MoveTask);
		updatePeopleInTheBoat(x, y, z);
	}
	
	/**
	 * Method onEvtArrived.
	 */
	public void onEvtArrived()
	{
		getCurrentWay().moveNext();
	}
	
	/**
	 * Method updatePeopleInTheBoat.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	protected void updatePeopleInTheBoat(int x, int y, int z)
	{
		for (Player player : _players)
		{
			if (player != null)
			{
				player.setXYZ(x, y, z, true);
			}
			if (isShuttle())
			{
				for (Summon summon : player.getSummonList())
				{
					summon.setXYZ(x, y, z, true);
				}
			}
		}
	}
	
	/**
	 * Method addPlayer.
	 * @param player Player
	 * @param boatLoc Location
	 */
	public void addPlayer(Player player, Location boatLoc)
	{
		synchronized (_players)
		{
			_players.add(player);
			player.setBoat(this);
			player.setLoc(getLoc(), true);
			player.setInBoatPosition(boatLoc);
			player.broadcastPacket(getOnPacket(player, boatLoc));
			if (isShuttle())
			{
				for (Summon summon : player.getSummonList())
				{
					summon.setBoat(this);
					summon.moveToLocation(getLoc(), 0, false);
					summon.broadcastPacket(getOnPacket(summon, boatLoc));
				}
			}
		}
	}
	
	/**
	 * Method moveInBoat.
	 * @param playable Playable
	 * @param ori Location
	 * @param loc Location
	 */
	public void moveInBoat(Playable playable, Location ori, Location loc)
	{
		if (!playable.isPlayer())
		{
			return;
		}
		Player player = playable.getPlayer();
		if (player.getSummonList().size() > 0)
		{
			player.sendPacket(SystemMsg.YOU_SHOULD_RELEASE_YOUR_PET_OR_SERVITOR_SO_THAT_IT_DOES_NOT_FALL_OFF_OF_THE_BOAT_AND_DROWN, ActionFail.STATIC);
			return;
		}
		if (player.getTransformation() != 0)
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_BOARD_A_SHIP_WHILE_YOU_ARE_POLYMORPHED, ActionFail.STATIC);
			return;
		}
		if (player.isMovementDisabled() || player.isSitting())
		{
			player.sendActionFailed();
			return;
		}
		if (!player.isInBoat())
		{
			player.setBoat(this);
		}
		loc.h = PositionUtils.getHeadingTo(ori, loc);
		player.setInBoatPosition(loc);
		player.broadcastPacket(inMovePacket(player, ori, loc));
	}
	
	/**
	 * Method trajetEnded.
	 * @param oust boolean
	 */
	public void trajetEnded(boolean oust)
	{
		_runState = 0;
		_fromHome = _fromHome == 1 ? 0 : 1;
		L2GameServerPacket checkLocation = checkLocationPacket();
		if (checkLocation != null)
		{
			broadcastPacket(infoPacket(), checkLocation);
		}
		if (oust)
		{
			oustPlayers();
			getCurrentWay().reCalcNextTime(false);
		}
	}
	
	/**
	 * Method teleportShip.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void teleportShip(int x, int y, int z)
	{
		if (isMoving)
		{
			stopMove(false);
		}
		for (Player player : _players)
		{
			player.teleToLocation(x, y, z);
		}
		setHeading(calcHeading(x, y));
		setXYZ(x, y, z, true);
		getCurrentWay().moveNext();
	}
	
	/**
	 * Method oustPlayer.
	 * @param player Player
	 * @param loc Location
	 * @param teleport boolean
	 */
	public void oustPlayer(Player player, Location loc, boolean teleport)
	{
		synchronized (_players)
		{
			player.setStablePoint(null);
			player.setBoat(null);
			player.setInBoatPosition(null);
			player.broadcastPacket(getOffPacket(player, loc));
			if (teleport)
			{
				player.teleToLocation(loc);
			}
			if (isShuttle())
			{
				for (Summon summon : player.getSummonList())
				{
					summon.setBoat(null);
					summon.setInBoatPosition(null);
					summon.broadcastPacket(getOffPacket(summon, loc));
				}
			}
			_players.remove(player);
		}
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 */
	public void removePlayer(Player player)
	{
		synchronized (_players)
		{
			_players.remove(player);
		}
	}
	
	/**
	 * Method broadcastPacketToPassengers.
	 * @param packet IStaticPacket
	 */
	public void broadcastPacketToPassengers(IStaticPacket packet)
	{
		for (Player player : _players)
		{
			player.sendPacket(packet);
		}
	}
	
	/**
	 * Method infoPacket.
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket infoPacket();
	
	/**
	 * Method movePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public abstract L2GameServerPacket movePacket();
	
	/**
	 * Method inMovePacket.
	 * @param player Player
	 * @param src Location
	 * @param desc Location
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket inMovePacket(Player player, Location src, Location desc);
	
	/**
	 * Method stopMovePacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public abstract L2GameServerPacket stopMovePacket();
	
	/**
	 * Method inStopMovePacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket inStopMovePacket(Player player);
	
	/**
	 * Method startPacket.
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket startPacket();
	
	/**
	 * Method validateLocationPacket.
	 * @param player Player
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket validateLocationPacket(Player player);
	
	/**
	 * Method checkLocationPacket.
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket checkLocationPacket();
	
	/**
	 * Method getOnPacket.
	 * @param playable Playable
	 * @param location Location
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket getOnPacket(Playable playable, Location location);
	
	/**
	 * Method getOffPacket.
	 * @param playable Playable
	 * @param location Location
	 * @return L2GameServerPacket
	 */
	public abstract L2GameServerPacket getOffPacket(Playable playable, Location location);
	
	/**
	 * Method oustPlayers.
	 */
	public abstract void oustPlayers();
	
	/**
	 * Method getAI.
	 * @return CharacterAI
	 */
	@Override
	public CharacterAI getAI()
	{
		if (_ai == null)
		{
			_ai = new BoatAI(this);
		}
		return _ai;
	}
	
	/**
	 * Method broadcastCharInfo.
	 */
	@Override
	public void broadcastCharInfo()
	{
		broadcastPacket(infoPacket());
	}
	
	/**
	 * Method broadcastPacket.
	 * @param packets L2GameServerPacket[]
	 */
	@Override
	public void broadcastPacket(L2GameServerPacket... packets)
	{
		List<Player> players = new ArrayList<>();
		players.addAll(_players);
		players.addAll(World.getAroundPlayers(this));
		for (Player player : players)
		{
			if (player != null)
			{
				player.sendPacket(packets);
			}
		}
	}
	
	/**
	 * Method validateLocation.
	 * @param broadcast int
	 */
	@Override
	public void validateLocation(int broadcast)
	{
	}
	
	/**
	 * Method sendChanges.
	 */
	@Override
	public void sendChanges()
	{
	}
	
	/**
	 * Method getMoveSpeed.
	 * @return int
	 */
	@Override
	public int getMoveSpeed()
	{
		return _moveSpeed;
	}
	
	/**
	 * Method getRunSpeed.
	 * @return int
	 */
	@Override
	public int getRunSpeed()
	{
		return _moveSpeed;
	}
	
	/**
	 * Method getActiveWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	/**
	 * Method getActiveWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		return null;
	}
	
	/**
	 * Method getSecondaryWeaponInstance.
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	/**
	 * Method getSecondaryWeaponItem.
	 * @return WeaponTemplate
	 */
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		return null;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public int getLevel()
	{
		return 0;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method getRunState.
	 * @return int
	 */
	public int getRunState()
	{
		return _runState;
	}
	
	/**
	 * Method setRunState.
	 * @param runState int
	 */
	public void setRunState(int runState)
	{
		_runState = runState;
	}
	
	/**
	 * Method setMoveSpeed.
	 * @param moveSpeed int
	 */
	public void setMoveSpeed(int moveSpeed)
	{
		_moveSpeed = moveSpeed;
	}
	
	/**
	 * Method setRotationSpeed.
	 * @param rotationSpeed int
	 */
	public void setRotationSpeed(int rotationSpeed)
	{
		_rotationSpeed = rotationSpeed;
	}
	
	/**
	 * Method getRotationSpeed.
	 * @return int
	 */
	public int getRotationSpeed()
	{
		return _rotationSpeed;
	}
	
	/**
	 * Method getCurrentWay.
	 * @return BoatWayEvent
	 */
	public BoatWayEvent getCurrentWay()
	{
		return _ways[_fromHome];
	}
	
	/**
	 * Method setWay.
	 * @param id int
	 * @param v BoatWayEvent
	 */
	public void setWay(int id, BoatWayEvent v)
	{
		_ways[id] = v;
	}
	
	/**
	 * Method getPlayers.
	 * @return Set<Player>
	 */
	public Set<Player> getPlayers()
	{
		return _players;
	}
	
	/**
	 * Method isDocked.
	 * @return boolean
	 */
	public boolean isDocked()
	{
		return _runState == 0;
	}
	
	/**
	 * Method getReturnLoc.
	 * @return Location
	 */
	public Location getReturnLoc()
	{
		return getCurrentWay().getReturnLoc();
	}
	
	/**
	 * Method isBoat.
	 * @return boolean
	 */
	@Override
	public boolean isBoat()
	{
		return true;
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isMoving)
		{
			return Collections.singletonList(infoPacket());
		}
		List<L2GameServerPacket> list = new ArrayList<>(2);
		list.add(infoPacket());
		list.add(movePacket());
		return list;
	}
	
	/**
	 * Method getBoatId.
	 * @return int
	 */
	public int getBoatId()
	{
		return getObjectId();
	}
}
