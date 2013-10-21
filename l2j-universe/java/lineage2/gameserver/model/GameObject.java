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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventOwner;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.network.serverpackets.DeleteObject;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Events;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class GameObject extends EventOwner
{
	/**
	 * Field serialVersionUID. (value is -950375486287118921)
	 */
	private static final long serialVersionUID = -950375486287118921L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GameObject.class);
	/**
	 * Field EMPTY_L2OBJECT_ARRAY.
	 */
	public final static GameObject[] EMPTY_L2OBJECT_ARRAY = new GameObject[0];
	/**
	 * Field CREATED. (value is 0)
	 */
	protected final static int CREATED = 0;
	/**
	 * Field VISIBLE. (value is 1)
	 */
	protected final static int VISIBLE = 1;
	/**
	 * Field DELETED. (value is -1)
	 */
	protected final static int DELETED = -1;
	/**
	 * Field objectId.
	 */
	protected int objectId;
	/**
	 * Field _x.
	 */
	private int _x;
	/**
	 * Field _y.
	 */
	private int _y;
	/**
	 * Field _z.
	 */
	private int _z;
	/**
	 * Field _reflection.
	 */
	protected Reflection _reflection = ReflectionManager.DEFAULT;
	/**
	 * Field _currentRegion.
	 */
	private WorldRegion _currentRegion;
	/**
	 * Field _state.
	 */
	private final AtomicInteger _state = new AtomicInteger(CREATED);
	
	/**
	 * Constructor for GameObject.
	 */
	protected GameObject()
	{
	}
	
	/**
	 * Constructor for GameObject.
	 * @param objectId int
	 */
	public GameObject(int objectId)
	{
		this.objectId = objectId;
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<? extends GameObject>
	 */
	public HardReference<? extends GameObject> getRef()
	{
		return HardReferences.emptyRef();
	}
	
	/**
	 * Method clearRef.
	 */
	private void clearRef()
	{
		HardReference<? extends GameObject> reference = getRef();
		if (reference != null)
		{
			reference.clear();
		}
	}
	
	/**
	 * Method getReflection.
	 * @return Reflection
	 */
	public Reflection getReflection()
	{
		return _reflection;
	}
	
	/**
	 * Method getReflectionId.
	 * @return int
	 */
	public int getReflectionId()
	{
		return _reflection.getId();
	}
	
	/**
	 * Method getGeoIndex.
	 * @return int
	 */
	public int getGeoIndex()
	{
		return _reflection.getGeoIndex();
	}
	
	/**
	 * Method setReflection.
	 * @param reflection Reflection
	 */
	public void setReflection(Reflection reflection)
	{
		if (_reflection == reflection)
		{
			return;
		}
		boolean respawn = false;
		if (isVisible())
		{
			decayMe();
			respawn = true;
		}
		Reflection r = getReflection();
		if (!r.isDefault())
		{
			r.removeObject(this);
		}
		_reflection = reflection;
		if (!reflection.isDefault())
		{
			reflection.addObject(this);
		}
		if (respawn)
		{
			spawnMe();
		}
	}
	
	/**
	 * Method setReflection.
	 * @param reflectionId int
	 */
	public void setReflection(int reflectionId)
	{
		Reflection r = ReflectionManager.getInstance().get(reflectionId);
		if (r == null)
		{
			Log.debug("Trying to set unavailable reflection: " + reflectionId + " for object: " + this + "!", new Throwable().fillInStackTrace());
			return;
		}
		setReflection(r);
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	@Override
	public final int hashCode()
	{
		return objectId;
	}
	
	/**
	 * Method getObjectId.
	 * @return int
	 */
	public final int getObjectId()
	{
		return objectId;
	}
	
	/**
	 * Method getX.
	 * @return int
	 */
	public int getX()
	{
		return _x;
	}
	
	/**
	 * Method getY.
	 * @return int
	 */
	public int getY()
	{
		return _y;
	}
	
	/**
	 * Method getZ.
	 * @return int
	 */
	public int getZ()
	{
		return _z;
	}
	
	/**
	 * Method getLoc.
	 * @return Location
	 */
	public Location getLoc()
	{
		return new Location(_x, _y, _z, getHeading());
	}
	
	/**
	 * Method getGeoZ.
	 * @param loc Location
	 * @return int
	 */
	public int getGeoZ(Location loc)
	{
		return GeoEngine.getHeight(loc, getGeoIndex());
	}
	
	/**
	 * Method setLoc.
	 * @param loc Location
	 */
	public void setLoc(Location loc)
	{
		setXYZ(loc.x, loc.y, loc.z);
	}
	
	/**
	 * Method setXYZ.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void setXYZ(int x, int y, int z)
	{
		_x = World.validCoordX(x);
		_y = World.validCoordY(y);
		_z = World.validCoordZ(z);
		World.addVisibleObject(this, null);
	}
	
	/**
	 * Method isVisible.
	 * @return boolean
	 */
	public final boolean isVisible()
	{
		return _state.get() == VISIBLE;
	}
	
	/**
	 * Method getInvisibleType.
	 * @return InvisibleType
	 */
	public InvisibleType getInvisibleType()
	{
		return InvisibleType.NONE;
	}
	
	/**
	 * Method isInvisible.
	 * @return boolean
	 */
	public final boolean isInvisible()
	{
		return getInvisibleType() != InvisibleType.NONE;
	}
	
	/**
	 * Method spawnMe.
	 * @param loc Location
	 */
	public void spawnMe(Location loc)
	{
		spawnMe0(loc, null);
	}
	
	/**
	 * Method spawnMe0.
	 * @param loc Location
	 * @param dropper Creature
	 */
	protected void spawnMe0(Location loc, Creature dropper)
	{
		_x = loc.x;
		_y = loc.y;
		_z = getGeoZ(loc);
		spawn0(dropper);
	}
	
	/**
	 * Method spawnMe.
	 */
	public final void spawnMe()
	{
		spawn0(null);
	}
	
	/**
	 * Method spawn0.
	 * @param dropper Creature
	 */
	protected void spawn0(Creature dropper)
	{
		if (!_state.compareAndSet(CREATED, VISIBLE))
		{
			return;
		}
		World.addVisibleObject(this, dropper);
		onSpawn();
	}
	
	/**
	 * Method toggleVisible.
	 */
	public void toggleVisible()
	{
		if (isVisible())
		{
			decayMe();
		}
		else
		{
			spawnMe();
		}
	}
	
	/**
	 * Method onSpawn.
	 */
	protected void onSpawn()
	{
	}
	
	/**
	 * Method decayMe.
	 */
	public final void decayMe()
	{
		if (!_state.compareAndSet(VISIBLE, CREATED))
		{
			return;
		}
		World.removeVisibleObject(this);
		onDespawn();
	}
	
	/**
	 * Method onDespawn.
	 */
	protected void onDespawn()
	{
	}
	
	/**
	 * Method deleteMe.
	 */
	public final void deleteMe()
	{
		decayMe();
		if (!_state.compareAndSet(CREATED, DELETED))
		{
			return;
		}
		onDelete();
	}
	
	/**
	 * Method isDeleted.
	 * @return boolean
	 */
	public final boolean isDeleted()
	{
		return _state.get() == DELETED;
	}
	
	/**
	 * Method onDelete.
	 */
	protected void onDelete()
	{
		Reflection r = getReflection();
		if (!r.isDefault())
		{
			r.removeObject(this);
		}
		clearRef();
	}
	
	/**
	 * Method onForcedAttack.
	 * @param player Player
	 * @param shift boolean
	 */
	public void onForcedAttack(Player player, boolean shift)
	{
		player.sendActionFailed();
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method getL2ClassShortName.
	 * @return String
	 */
	public String getL2ClassShortName()
	{
		return getClass().getSimpleName();
	}
	
	/**
	 * Method getXYDeltaSq.
	 * @param x int
	 * @param y int
	 * @return long
	 */
	public final long getXYDeltaSq(int x, int y)
	{
		long dx = x - getX();
		long dy = y - getY();
		return (dx * dx) + (dy * dy);
	}
	
	/**
	 * Method getXYDeltaSq.
	 * @param loc Location
	 * @return long
	 */
	public final long getXYDeltaSq(Location loc)
	{
		return getXYDeltaSq(loc.x, loc.y);
	}
	
	/**
	 * Method getZDeltaSq.
	 * @param z int
	 * @return long
	 */
	public final long getZDeltaSq(int z)
	{
		long dz = z - getZ();
		return dz * dz;
	}
	
	/**
	 * Method getZDeltaSq.
	 * @param loc Location
	 * @return long
	 */
	public final long getZDeltaSq(Location loc)
	{
		return getZDeltaSq(loc.z);
	}
	
	/**
	 * Method getXYZDeltaSq.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return long
	 */
	public final long getXYZDeltaSq(int x, int y, int z)
	{
		return getXYDeltaSq(x, y) + getZDeltaSq(z);
	}
	
	/**
	 * Method getXYZDeltaSq.
	 * @param loc Location
	 * @return long
	 */
	public final long getXYZDeltaSq(Location loc)
	{
		return getXYDeltaSq(loc.x, loc.y) + getZDeltaSq(loc.z);
	}
	
	/**
	 * Method getDistance.
	 * @param x int
	 * @param y int
	 * @return double
	 */
	public final double getDistance(int x, int y)
	{
		return Math.sqrt(getXYDeltaSq(x, y));
	}
	
	/**
	 * Method getDistance.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return double
	 */
	public final double getDistance(int x, int y, int z)
	{
		return Math.sqrt(getXYZDeltaSq(x, y, z));
	}
	
	/**
	 * Method getDistance.
	 * @param loc Location
	 * @return double
	 */
	public final double getDistance(Location loc)
	{
		return getDistance(loc.x, loc.y, loc.z);
	}
	
	/**
	 * Method isInRange.
	 * @param obj GameObject
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRange(GameObject obj, long range)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj.getReflection() != getReflection())
		{
			return false;
		}
		long dx = Math.abs(obj.getX() - getX());
		if (dx > range)
		{
			return false;
		}
		long dy = Math.abs(obj.getY() - getY());
		if (dy > range)
		{
			return false;
		}
		long dz = Math.abs(obj.getZ() - getZ());
		return (dz <= 1500) && (((dx * dx) + (dy * dy)) <= (range * range));
	}
	
	/**
	 * Method isInRangeZ.
	 * @param obj GameObject
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRangeZ(GameObject obj, long range)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj.getReflection() != getReflection())
		{
			return false;
		}
		long dx = Math.abs(obj.getX() - getX());
		if (dx > range)
		{
			return false;
		}
		long dy = Math.abs(obj.getY() - getY());
		if (dy > range)
		{
			return false;
		}
		long dz = Math.abs(obj.getZ() - getZ());
		return (dz <= range) && (((dx * dx) + (dy * dy) + (dz * dz)) <= (range * range));
	}
	
	/**
	 * Method isInRange.
	 * @param loc Location
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRange(Location loc, long range)
	{
		return isInRangeSq(loc, range * range);
	}
	
	/**
	 * Method isInRangeSq.
	 * @param loc Location
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRangeSq(Location loc, long range)
	{
		return getXYDeltaSq(loc) <= range;
	}
	
	/**
	 * Method isInRangeZ.
	 * @param loc Location
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRangeZ(Location loc, long range)
	{
		return isInRangeZSq(loc, range * range);
	}
	
	/**
	 * Method isInRangeZSq.
	 * @param loc Location
	 * @param range long
	 * @return boolean
	 */
	public final boolean isInRangeZSq(Location loc, long range)
	{
		return getXYZDeltaSq(loc) <= range;
	}
	
	/**
	 * Method getDistance.
	 * @param obj GameObject
	 * @return double
	 */
	public final double getDistance(GameObject obj)
	{
		if (obj == null)
		{
			return 0;
		}
		return Math.sqrt(getXYDeltaSq(obj.getX(), obj.getY()));
	}
	
	/**
	 * Method getDistance3D.
	 * @param obj GameObject
	 * @return double
	 */
	public final double getDistance3D(GameObject obj)
	{
		if (obj == null)
		{
			return 0;
		}
		return Math.sqrt(getXYZDeltaSq(obj.getX(), obj.getY(), obj.getZ()));
	}
	
	/**
	 * Method getRealDistance.
	 * @param obj GameObject
	 * @return double
	 */
	public final double getRealDistance(GameObject obj)
	{
		return getRealDistance3D(obj, true);
	}
	
	/**
	 * Method getRealDistance3D.
	 * @param obj GameObject
	 * @return double
	 */
	public final double getRealDistance3D(GameObject obj)
	{
		return getRealDistance3D(obj, false);
	}
	
	/**
	 * Method getRealDistance3D.
	 * @param obj GameObject
	 * @param ignoreZ boolean
	 * @return double
	 */
	public final double getRealDistance3D(GameObject obj, boolean ignoreZ)
	{
		double distance = ignoreZ ? getDistance(obj) : getDistance3D(obj);
		if (isCreature())
		{
			distance -= ((Creature) this).getTemplate().getCollisionRadius();
		}
		if (obj.isCreature())
		{
			distance -= ((Creature) obj).getTemplate().getCollisionRadius();
		}
		return distance > 0 ? distance : 0;
	}
	
	/**
	 * Method getSqDistance.
	 * @param x int
	 * @param y int
	 * @return long
	 */
	public final long getSqDistance(int x, int y)
	{
		return getXYDeltaSq(x, y);
	}
	
	/**
	 * Method getSqDistance.
	 * @param obj GameObject
	 * @return long
	 */
	public final long getSqDistance(GameObject obj)
	{
		if (obj == null)
		{
			return 0;
		}
		return getXYDeltaSq(obj.getLoc());
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return null;
	}
	
	/**
	 * Method getHeading.
	 * @return int
	 */
	public int getHeading()
	{
		return 0;
	}
	
	/**
	 * Method getMoveSpeed.
	 * @return int
	 */
	public int getMoveSpeed()
	{
		return 0;
	}
	
	/**
	 * Method getCurrentRegion.
	 * @return WorldRegion
	 */
	public WorldRegion getCurrentRegion()
	{
		return _currentRegion;
	}
	
	/**
	 * Method setCurrentRegion.
	 * @param region WorldRegion
	 */
	public void setCurrentRegion(WorldRegion region)
	{
		_currentRegion = region;
	}
	
	/**
	 * Method isInObserverMode.
	 * @return boolean
	 */
	public boolean isInObserverMode()
	{
		return false;
	}
	
	/**
	 * Method isInOlympiadMode.
	 * @return boolean
	 */
	public boolean isInOlympiadMode()
	{
		return false;
	}
	
	/**
	 * Method isInBoat.
	 * @return boolean
	 */
	public boolean isInBoat()
	{
		return false;
	}
	
	/**
	 * Method isInShuttle.
	 * @return boolean
	 */
	public boolean isInShuttle()
	{
		return false;
	}
	
	/**
	 * Method isFlying.
	 * @return boolean
	 */
	public boolean isFlying()
	{
		return false;
	}
	
	/**
	 * Method getColRadius.
	 * @return double
	 */
	public double getColRadius()
	{
		_log.warn("getColRadius called directly from L2Object");
		Thread.dumpStack();
		return 0;
	}
	
	/**
	 * Method getColHeight.
	 * @return double
	 */
	public double getColHeight()
	{
		_log.warn("getColHeight called directly from L2Object");
		Thread.dumpStack();
		return 0;
	}
	
	/**
	 * Method isCreature.
	 * @return boolean
	 */
	public boolean isCreature()
	{
		return false;
	}
	
	/**
	 * Method isPlayable.
	 * @return boolean
	 */
	public boolean isPlayable()
	{
		return false;
	}
	
	/**
	 * Method isPlayer.
	 * @return boolean
	 */
	public boolean isPlayer()
	{
		return false;
	}
	
	/**
	 * Method isPet.
	 * @return boolean
	 */
	public boolean isPet()
	{
		return false;
	}
	
	/**
	 * Method isServitor.
	 * @return boolean
	 */
	public boolean isServitor()
	{
		return false;
	}
	
	/**
	 * Method isClone.
	 * @return boolean
	 */
	public boolean isClone()
	{
		return false;
	}

	/**
	 * Method isNpc.
	 * @return boolean
	 */
	public boolean isNpc()
	{
		return false;
	}
	
	/**
	 * Method isMonster.
	 * @return boolean
	 */
	public boolean isMonster()
	{
		return false;
	}
	
	/**
	 * Method isItem.
	 * @return boolean
	 */
	public boolean isItem()
	{
		return false;
	}
	
	/**
	 * Method isRaid.
	 * @return boolean
	 */
	public boolean isRaid()
	{
		return false;
	}
	
	/**
	 * Method isBoss.
	 * @return boolean
	 */
	public boolean isBoss()
	{
		return false;
	}
	
	/**
	 * Method isTrap.
	 * @return boolean
	 */
	public boolean isTrap()
	{
		return false;
	}
	
	/**
	 * Method isDoor.
	 * @return boolean
	 */
	public boolean isDoor()
	{
		return false;
	}
	
	/**
	 * Method isArtefact.
	 * @return boolean
	 */
	public boolean isArtefact()
	{
		return false;
	}
	
	/**
	 * Method isSiegeGuard.
	 * @return boolean
	 */
	public boolean isSiegeGuard()
	{
		return false;
	}
	
	/**
	 * Method isClanAirShip.
	 * @return boolean
	 */
	public boolean isClanAirShip()
	{
		return false;
	}
	
	/**
	 * Method isAirShip.
	 * @return boolean
	 */
	public boolean isAirShip()
	{
		return false;
	}
	
	/**
	 * Method isBoat.
	 * @return boolean
	 */
	public boolean isBoat()
	{
		return false;
	}
	
	/**
	 * Method isVehicle.
	 * @return boolean
	 */
	public boolean isVehicle()
	{
		return false;
	}
	
	/**
	 * Method isShuttle.
	 * @return boolean
	 */
	public boolean isShuttle()
	{
		return false;
	}
	
	/**
	 * Method isMinion.
	 * @return boolean
	 */
	public boolean isMinion()
	{
		return false;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return getClass().getSimpleName() + ":" + objectId;
	}
	
	/**
	 * Method dump.
	 * @return String
	 */
	public String dump()
	{
		return dump(true);
	}
	
	/**
	 * Method dump.
	 * @param simpleTypes boolean
	 * @return String
	 */
	public String dump(boolean simpleTypes)
	{
		return Util.dumpObject(this, simpleTypes, true, true);
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		return Collections.emptyList();
	}
	
	/**
	 * Method deletePacketList.
	 * @return List<L2GameServerPacket>
	 */
	public List<L2GameServerPacket> deletePacketList()
	{
		return Collections.<L2GameServerPacket> singletonList(new DeleteObject(this));
	}
	
	/**
	 * Method addEvent.
	 * @param event GlobalEvent
	 */
	@Override
	public void addEvent(GlobalEvent event)
	{
		event.onAddEvent(this);
		super.addEvent(event);
	}
	
	/**
	 * Method removeEvent.
	 * @param event GlobalEvent
	 */
	@Override
	public void removeEvent(GlobalEvent event)
	{
		event.onRemoveEvent(this);
		super.removeEvent(event);
	}
	
	/**
	 * Method equals.
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (obj.getClass() != getClass())
		{
			return false;
		}
		return ((GameObject) obj).getObjectId() == getObjectId();
	}

	public void onActionTarget(final Player player, boolean forced)
	{
		player.setTarget(this);
	}

	public void onActionSelect(final Player player, final boolean forced)
	{
		if(Events.onAction(player, this, forced))
		{
			return;
		}

		if(player.getTarget() == this)
		{
			onActionTargeted(player, forced);
		}
		else
		{
			onActionTarget(player, forced);
		}
	}

	public void onActionTargeted(final Player player, boolean forced)
	{
		if(player == this)
		{
			return;
		}
		if(player.isSitting())
		{
			// msg?
			return;
		}
		if(player.isMovementDisabled())
		{
			return;
		}

		if(!player.isInRange(this, Creature.INTERACTION_DISTANCE))
		{
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this, null);
			return;
		}
		onInteract(player);
	}

	public void onInteract(final Player player)
	{
		// dummy
	}

	public boolean isTargetable(final Player player)
	{
		if(player.isInObserverMode())
		{
			return false;
		}

		if(player.isTeleporting())
		{
			return false;
		}

		if(player.isOutOfControl())
		{
			return false;
		}

		if(player.isAlikeDead() && this != player)
		{
			return false;
		}

		if(player.getReflectionId() != getReflectionId())
		{
			return false;
		}

		if(player.isLockedTarget())
		{
			if(player.isClanAirShipDriver())
			{

                /*
                 * 2740 : This action is prohibited while steering.
                 */
				player.sendPacket(new SystemMessage(SystemMessage.THIS_ACTION_IS_PROHIBITED_WHILE_CONTROLLING));
			}

			return false;
		}

		if(player.isFrozen())
		{

            /*
             * 687 : You cannot move while frozen. Please wait.
             */
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_IN_A_FROZEN_STATE_PLEASE_WAIT_A_MOMENT));
			return false;
		}

		if((player.getAggressionTarget() != null) && (player.getAggressionTarget() != this) && !player.getAggressionTarget().isDead())
		{
			return false;
		}
		return true;
	}

	public boolean displayHpBar()
	{
		return false;
	}

}
