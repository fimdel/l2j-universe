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
package lineage2.gameserver.model.instances;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.geometry.Shape;
import lineage2.commons.listener.Listener;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DoorAI;
import lineage2.gameserver.geodata.GeoCollision;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.listener.actor.door.OnOpenCloseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.StaticObject;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;

/**
 */
public final class DoorInstance extends Creature implements GeoCollision
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void onInteract(final Player player)
	{
		getAI().onEvtTwiceClick(player);
	}
	/**
	 * @author Mobius
	 */
	private class AutoOpenClose extends RunnableImpl
	{
		/**
		 * Field _open.
		 */
		private final boolean _open;
		
		/**
		 * Constructor for AutoOpenClose.
		 * @param open boolean
		 */
		public AutoOpenClose(boolean open)
		{
			_open = open;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_open)
			{
				openMe(null, true);
			}
			else
			{
				closeMe(null, true);
			}
		}
	}
	
	/**
	 * Field _open.
	 */
	private boolean _open = true;
	/**
	 * Field _geoOpen.
	 */
	private boolean _geoOpen = true;
	/**
	 * Field _openLock.
	 */
	private final Lock _openLock = new ReentrantLock();
	/**
	 * Field _upgradeHp.
	 */
	private int _upgradeHp;
	/**
	 * Field _geoAround.
	 */
	private byte[][] _geoAround;
	/**
	 * Field _autoActionTask.
	 */
	protected ScheduledFuture<?> _autoActionTask;
	
	/**
	 * Constructor for DoorInstance.
	 * @param objectId int
	 * @param template DoorTemplate
	 */
	public DoorInstance(int objectId, DoorTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method isUnlockable.
	 * @return boolean
	 */
	public boolean isUnlockable()
	{
		return getTemplate().isUnlockable();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return getTemplate().getName();
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public int getLevel()
	{
		return 1;
	}
	
	/**
	 * Method getDoorId.
	 * @return int
	 */
	public int getDoorId()
	{
		return getTemplate().getNpcId();
	}
	
	/**
	 * Method isOpen.
	 * @return boolean
	 */
	public boolean isOpen()
	{
		return _open;
	}
	
	/**
	 * Method setOpen.
	 * @param open boolean
	 * @return boolean
	 */
	protected boolean setOpen(boolean open)
	{
		if (_open == open)
		{
			return false;
		}
		_open = open;
		return true;
	}
	
	/**
	 * Method scheduleAutoAction.
	 * @param open boolean
	 * @param actionDelay long
	 */
	public void scheduleAutoAction(boolean open, long actionDelay)
	{
		if (_autoActionTask != null)
		{
			_autoActionTask.cancel(false);
			_autoActionTask = null;
		}
		_autoActionTask = ThreadPoolManager.getInstance().schedule(new AutoOpenClose(open), actionDelay);
	}
	
	/**
	 * Method getDamage.
	 * @return int
	 */
	public int getDamage()
	{
		int dmg = 6 - (int) Math.ceil(getCurrentHpRatio() * 6);
		return Math.max(0, Math.min(6, dmg));
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return isAttackable(attacker);
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		if ((attacker == null) || isOpen())
		{
			return false;
		}
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		switch (getDoorType())
		{
			case WALL:
				if (!attacker.isServitor() || (siegeEvent == null) || !siegeEvent.containsSiegeSummon((SummonInstance) attacker))
				{
					return false;
				}
				break;
			case DOOR:
				Player player = attacker.getPlayer();
				if (player == null)
				{
					return false;
				}
				if (siegeEvent != null)
				{
					if (siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, player.getClan()) != null)
					{
						return false;
					}
				}
				break;
		}
		return !isInvul();
	}
	
	/**
	 * Method sendChanges.
	 */
	@Override
	public void sendChanges()
	{
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

	@Override
	public DoorAI getAI()
	{
		if(_ai == null)
		{
			synchronized(this)
			{
				if(_ai == null)
				{
					_ai = getTemplate().getNewAI(this);
				}
			}
		}

		return (DoorAI) _ai;
	}

	/**
	 * Method broadcastStatusUpdate.
	 */
	@Override
	public void broadcastStatusUpdate()
	{
		for (Player player : World.getAroundPlayers(this))
		{
			if (player != null)
			{
				player.sendPacket(new StaticObject(this, player));
			}
		}
	}
	
	/**
	 * Method openMe.
	 * @return boolean
	 */
	public boolean openMe()
	{
		return openMe(null, true);
	}
	
	/**
	 * Method openMe.
	 * @param opener Player
	 * @param autoClose boolean
	 * @return boolean
	 */
	public boolean openMe(Player opener, boolean autoClose)
	{
		_openLock.lock();
		try
		{
			if (!setOpen(true))
			{
				return false;
			}
			setGeoOpen(true);
		}
		finally
		{
			_openLock.unlock();
		}
		broadcastStatusUpdate();
		if (autoClose && (getTemplate().getCloseTime() > 0))
		{
			scheduleAutoAction(false, getTemplate().getCloseTime() * 1000L);
		}
		getAI().onEvtOpen(opener);
		for (Listener<Creature> l : getListeners().getListeners())
		{
			if (l instanceof OnOpenCloseListener)
			{
				((OnOpenCloseListener) l).onOpen(this);
			}
		}
		return true;
	}
	
	/**
	 * Method closeMe.
	 * @return boolean
	 */
	public boolean closeMe()
	{
		return closeMe(null, true);
	}
	
	/**
	 * Method closeMe.
	 * @param closer Player
	 * @param autoOpen boolean
	 * @return boolean
	 */
	public boolean closeMe(Player closer, boolean autoOpen)
	{
		if (isDead())
		{
			return false;
		}
		_openLock.lock();
		try
		{
			if (!setOpen(false))
			{
				return false;
			}
			setGeoOpen(false);
		}
		finally
		{
			_openLock.unlock();
		}
		broadcastStatusUpdate();
		if (autoOpen && (getTemplate().getOpenTime() > 0))
		{
			long openDelay = getTemplate().getOpenTime() * 1000L;
			if (getTemplate().getRandomTime() > 0)
			{
				openDelay += Rnd.get(0, getTemplate().getRandomTime()) * 1000L;
			}
			scheduleAutoAction(true, openDelay);
		}
		getAI().onEvtClose(closer);
		for (Listener<Creature> l : getListeners().getListeners())
		{
			if (l instanceof OnOpenCloseListener)
			{
				((OnOpenCloseListener) l).onClose(this);
			}
		}
		return true;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "[Door " + getDoorId() + "]";
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		_openLock.lock();
		try
		{
			setGeoOpen(true);
		}
		finally
		{
			_openLock.unlock();
		}
		super.onDeath(killer);
	}
	
	/**
	 * Method onRevive.
	 */
	@Override
	protected void onRevive()
	{
		super.onRevive();
		_openLock.lock();
		try
		{
			if (!isOpen())
			{
				setGeoOpen(false);
			}
		}
		finally
		{
			_openLock.unlock();
		}
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		setCurrentHpMp(getMaxHp(), getMaxMp(), true);
		closeMe(null, true);
	}
	
	/**
	 * Method onDespawn.
	 */
	@Override
	protected void onDespawn()
	{
		if (_autoActionTask != null)
		{
			_autoActionTask.cancel(false);
			_autoActionTask = null;
		}
		super.onDespawn();
	}
	
	/**
	 * Method isHPVisible.
	 * @return boolean
	 */
	public boolean isHPVisible()
	{
		return getTemplate().isHPVisible();
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return super.getMaxHp() + _upgradeHp;
	}
	
	/**
	 * Method setUpgradeHp.
	 * @param hp int
	 */
	public void setUpgradeHp(int hp)
	{
		_upgradeHp = hp;
	}
	
	/**
	 * Method getUpgradeHp.
	 * @return int
	 */
	public int getUpgradeHp()
	{
		return _upgradeHp;
	}
	
	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPDef(Creature target)
	{
		return super.getPDef(target);
	}
	
	/**
	 * Method getMDef.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMDef(Creature target, Skill skill)
	{
		return super.getMDef(target, skill);
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		if (!getTemplate().isHPVisible())
		{
			return true;
		}
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		if ((siegeEvent != null) && siegeEvent.isInProgress())
		{
			return false;
		}
		return super.isInvul();
	}
	
	/**
	 * Method setGeoOpen.
	 * @param open boolean
	 * @return boolean
	 */
	protected boolean setGeoOpen(boolean open)
	{
		if (_geoOpen == open)
		{
			return false;
		}
		_geoOpen = open;
		if (Config.ALLOW_GEODATA)
		{
			if (open)
			{
				GeoEngine.removeGeoCollision(this, getGeoIndex());
			}
			else
			{
				GeoEngine.applyGeoCollision(this, getGeoIndex());
			}
		}
		return true;
	}
	
	/**
	 * Method isMovementDisabled.
	 * @return boolean
	 */
	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}
	
	/**
	 * Method isActionsDisabled.
	 * @return boolean
	 */
	@Override
	public boolean isActionsDisabled()
	{
		return true;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	/**
	 * Method isConcrete.
	 * @return boolean * @see lineage2.gameserver.geodata.GeoCollision#isConcrete()
	 */
	@Override
	public boolean isConcrete()
	{
		return true;
	}
	
	/**
	 * Method isHealBlocked.
	 * @return boolean
	 */
	@Override
	public boolean isHealBlocked()
	{
		return true;
	}
	
	/**
	 * Method isEffectImmune.
	 * @return boolean
	 */
	@Override
	public boolean isEffectImmune()
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
		return Collections.<L2GameServerPacket> singletonList(new StaticObject(this, forPlayer));
	}
	
	/**
	 * Method isDoor.
	 * @return boolean
	 */
	@Override
	public boolean isDoor()
	{
		return true;
	}
	
	/**
	 * Method getShape.
	 * @return Shape * @see lineage2.gameserver.geodata.GeoCollision#getShape()
	 */
	@Override
	public Shape getShape()
	{
		return getTemplate().getPolygon();
	}
	
	/**
	 * Method getGeoAround.
	 * @return byte[][] * @see lineage2.gameserver.geodata.GeoCollision#getGeoAround()
	 */
	@Override
	public byte[][] getGeoAround()
	{
		return _geoAround;
	}
	
	/**
	 * Method setGeoAround.
	 * @param geo byte[][]
	 * @see lineage2.gameserver.geodata.GeoCollision#setGeoAround(byte[][])
	 */
	@Override
	public void setGeoAround(byte[][] geo)
	{
		_geoAround = geo;
	}
	
	/**
	 * Method getTemplate.
	 * @return DoorTemplate
	 */
	@Override
	public DoorTemplate getTemplate()
	{
		return (DoorTemplate) super.getTemplate();
	}
	
	/**
	 * Method getDoorType.
	 * @return DoorTemplate.DoorType
	 */
	public DoorTemplate.DoorType getDoorType()
	{
		return getTemplate().getDoorType();
	}
	
	/**
	 * Method getKey.
	 * @return int
	 */
	public int getKey()
	{
		return getTemplate().getKey();
	}

	@Override
	public boolean displayHpBar()
	{
		return getTemplate().isHPVisible();
	}
}
