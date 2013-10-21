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
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.actions.StartStopAction;
import lineage2.gameserver.model.entity.events.impl.BoatWayEvent;
import lineage2.gameserver.model.entity.events.objects.BoatPoint;
import lineage2.gameserver.model.instances.ControlKeyInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.DeleteObject;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.AirshipDock;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanAirShip extends AirShip
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field MAINTENANCE_DELAY.
	 */
	private static final long MAINTENANCE_DELAY = 60 * 1000L;
	/**
	 * Field MAX_FUEL. (value is 600)
	 */
	public static final int MAX_FUEL = 600;
	/**
	 * Field _currentFuel.
	 */
	private int _currentFuel;
	/**
	 * Field _dock.
	 */
	private AirshipDock _dock;
	/**
	 * Field _platform.
	 */
	private AirshipDock.AirshipPlatform _platform;
	/**
	 * Field _driverRef.
	 */
	private HardReference<Player> _driverRef = HardReferences.emptyRef();
	/**
	 * Field _controlKey.
	 */
	private final GameObject _controlKey = new ControlKeyInstance();
	/**
	 * Field _clan.
	 */
	private final Clan _clan;
	/**
	 * Field _customMove.
	 */
	private boolean _customMove;
	/**
	 * Field _deleteTask.
	 */
	private Future<?> _deleteTask = null;
	
	/**
	 * Constructor for ClanAirShip.
	 * @param clan Clan
	 */
	public ClanAirShip(Clan clan)
	{
		super(IdFactory.getInstance().getNextId(), BoatHolder.TEMPLATE);
		BoatHolder.getInstance().addBoat(this);
		_clan = clan;
		_clan.setAirship(this);
		_currentFuel = clan.getAirshipFuel();
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		_controlKey.spawnMe(getLoc());
	}
	
	/**
	 * Method updatePeopleInTheBoat.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	@Override
	protected void updatePeopleInTheBoat(int x, int y, int z)
	{
		super.updatePeopleInTheBoat(x, y, z);
		_controlKey.setXYZ(x, y, z);
	}
	
	/**
	 * Method oustPlayer.
	 * @param player Player
	 * @param loc Location
	 * @param teleport boolean
	 */
	@Override
	public void oustPlayer(Player player, Location loc, boolean teleport)
	{
		if (player == getDriver())
		{
			setDriver(null);
		}
		super.oustPlayer(player, loc, teleport);
	}
	
	/**
	 * Method startDepartTask.
	 */
	public void startDepartTask()
	{
		BoatWayEvent arrivalWay = new BoatWayEvent(this);
		BoatWayEvent departWay = new BoatWayEvent(this);
		for (BoatPoint p : _platform.getArrivalPoints())
		{
			arrivalWay.addObject(BoatWayEvent.BOAT_POINTS, p);
		}
		for (BoatPoint p : _platform.getDepartPoints())
		{
			departWay.addObject(BoatWayEvent.BOAT_POINTS, p);
		}
		arrivalWay.addOnTimeAction(0, new StartStopAction(StartStopAction.EVENT, true));
		departWay.addOnTimeAction(300, new StartStopAction(StartStopAction.EVENT, true));
		setWay(0, arrivalWay);
		setWay(1, departWay);
		arrivalWay.reCalcNextTime(false);
	}
	
	/**
	 * Method startArrivalTask.
	 */
	public void startArrivalTask()
	{
		if (_deleteTask != null)
		{
			_deleteTask.cancel(true);
			_deleteTask = null;
		}
		for (Player player : _players)
		{
			player.showQuestMovie(_platform.getOustMovie());
			oustPlayer(player, getReturnLoc(), true);
		}
		deleteMe();
	}
	
	/**
	 * Method addTeleportPoint.
	 * @param player Player
	 * @param id int
	 */
	public void addTeleportPoint(Player player, int id)
	{
		if (isMoving || !isDocked())
		{
			return;
		}
		if (id == 0)
		{
			getCurrentWay().clearActions();
			getCurrentWay().startEvent();
		}
		else
		{
			BoatPoint point = getDock().getTeleportList().get(id);
			if (getCurrentFuel() < point.getFuel())
			{
				player.sendPacket(SystemMsg.YOUR_SHIP_CANNOT_TELEPORT_BECAUSE_IT_DOES_NOT_HAVE_ENOUGH_FUEL_FOR_THE_TRIP);
				return;
			}
			setCurrentFuel(getCurrentFuel() - point.getFuel());
			getCurrentWay().clearActions();
			getCurrentWay().addObject(BoatWayEvent.BOAT_POINTS, point);
			getCurrentWay().startEvent();
		}
	}
	
	/**
	 * Method trajetEnded.
	 * @param oust boolean
	 */
	@Override
	public void trajetEnded(boolean oust)
	{
		_runState = 0;
		if (_fromHome == 0)
		{
			_fromHome = 1;
			getCurrentWay().reCalcNextTime(false);
		}
		else
		{
			_customMove = true;
			_deleteTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new FuelAndDeleteTask(), MAINTENANCE_DELAY, MAINTENANCE_DELAY);
		}
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	public void onEvtArrived()
	{
		if (!_customMove)
		{
			getCurrentWay().moveNext();
		}
	}
	
	/**
	 * Method setDriver.
	 * @param player Player
	 */
	public void setDriver(Player player)
	{
		if (player != null)
		{
			if (_clan != player.getClan())
			{
				return;
			}
			if (player.getTargetId() != _controlKey.getObjectId())
			{
				player.sendPacket(SystemMsg.YOU_MUST_TARGET_THE_ONE_YOU_WISH_TO_CONTROL);
				return;
			}
			final int x = player.getInBoatPosition().x - 0x16e;
			final int y = player.getInBoatPosition().y;
			final int z = player.getInBoatPosition().z - 0x6b;
			if (((x * x) + (y * y) + (z * z)) > 2500)
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_BECAUSE_YOU_ARE_TOO_FAR);
				return;
			}
			if (player.getTransformation() != 0)
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_TRANSFORMED);
				return;
			}
			if (player.isParalyzed())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_YOU_ARE_PETRIFIED);
				return;
			}
			if (player.isDead() || player.isFakeDeath())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHEN_YOU_ARE_DEAD);
				return;
			}
			if (player.isFishing())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_FISHING);
				return;
			}
			if (player.isInCombat())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_BATTLE);
				return;
			}
			if (player.isInDuel())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_DUEL);
				return;
			}
			if (player.isSitting())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_IN_A_SITTING_POSITION);
				return;
			}
			if (player.isCastingNow())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_USING_A_SKILL);
				return;
			}
			if (player.isCursedWeaponEquipped())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_A_CURSED_WEAPON_IS_EQUIPPED);
				return;
			}
			if (player.getActiveWeaponFlagAttachment() != null)
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_CONTROL_THE_HELM_WHILE_HOLDING_A_FLAG);
				return;
			}
			_driverRef = player.getRef();
			player.setLockedTarget(true);
			player.unEquipWeapon();
			player.broadcastCharInfo();
		}
		else
		{
			Player oldDriver = getDriver();
			_driverRef = HardReferences.emptyRef();
			if (oldDriver != null)
			{
				oldDriver.setLockedTarget(false);
				oldDriver.broadcastCharInfo();
			}
		}
		broadcastCharInfo();
	}
	
	/**
	 * Method setCurrentFuel.
	 * @param fuel int
	 */
	public void setCurrentFuel(int fuel)
	{
		final int old = _currentFuel;
		_currentFuel = fuel;
		if (_currentFuel <= 0)
		{
			_currentFuel = 0;
			setMoveSpeed(150);
			setRotationSpeed(1000);
		}
		else if (_currentFuel > MAX_FUEL)
		{
			_currentFuel = MAX_FUEL;
		}
		if ((_currentFuel == 0) && (old > 0))
		{
			broadcastPacketToPassengers(SystemMsg.THE_AIRSHIPS_FUEL_EP_HAS_RUN_OUT);
		}
		else if (_currentFuel < 40)
		{
			broadcastPacketToPassengers(SystemMsg.THE_AIRSHIPS_FUEL_EP_WILL_SOON_RUN_OUT);
		}
		broadcastCharInfo();
	}
	
	/**
	 * Method getCurrentFuel.
	 * @return int
	 */
	public int getCurrentFuel()
	{
		return _currentFuel;
	}
	
	/**
	 * Method getMaxFuel.
	 * @return int
	 */
	public int getMaxFuel()
	{
		return MAX_FUEL;
	}
	
	/**
	 * Method getDriver.
	 * @return Player
	 */
	public Player getDriver()
	{
		return _driverRef.get();
	}
	
	/**
	 * Method getControlKey.
	 * @return GameObject
	 */
	public GameObject getControlKey()
	{
		return _controlKey;
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		_clan.setAirship(null);
		_clan.setAirshipFuel(_currentFuel);
		_clan.updateClanInDB();
		IdFactory.getInstance().releaseId(_controlKey.getObjectId());
		BoatHolder.getInstance().removeBoat(this);
		super.onDelete();
	}
	
	/**
	 * Method getReturnLoc.
	 * @return Location
	 */
	@Override
	public Location getReturnLoc()
	{
		return _platform == null ? null : _platform.getOustLoc();
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return _clan;
	}
	
	/**
	 * Method setDock.
	 * @param dockId AirshipDock
	 */
	public void setDock(AirshipDock dockId)
	{
		_dock = dockId;
	}
	
	/**
	 * Method setPlatform.
	 * @param platformId AirshipDock.AirshipPlatform
	 */
	public void setPlatform(AirshipDock.AirshipPlatform platformId)
	{
		_platform = platformId;
	}
	
	/**
	 * Method getDock.
	 * @return AirshipDock
	 */
	public AirshipDock getDock()
	{
		return _dock;
	}
	
	/**
	 * Method isCustomMove.
	 * @return boolean
	 */
	public boolean isCustomMove()
	{
		return _customMove;
	}
	
	/**
	 * Method isDocked.
	 * @return boolean
	 */
	@Override
	public boolean isDocked()
	{
		return (_dock != null) && !isMoving;
	}
	
	/**
	 * Method isClanAirShip.
	 * @return boolean
	 */
	@Override
	public boolean isClanAirShip()
	{
		return true;
	}
	
	/**
	 * Method deletePacketList.
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> deletePacketList()
	{
		List<L2GameServerPacket> list = new ArrayList<>(2);
		list.add(new DeleteObject(_controlKey));
		list.add(new DeleteObject(this));
		return list;
	}
	
	/**
	 * @author Mobius
	 */
	private class FuelAndDeleteTask extends RunnableImpl
	{
		/**
		 * Constructor for FuelAndDeleteTask.
		 */
		public FuelAndDeleteTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			boolean empty = true;
			for (Player player : _players)
			{
				if (player.isOnline())
				{
					empty = false;
				}
			}
			if (empty)
			{
				deleteMe();
			}
			else
			{
				setCurrentFuel(getCurrentFuel() - 10);
			}
		}
	}
}
