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
package lineage2.gameserver.model.entity.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.logging.LoggerObject;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.event.OnStartStopListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.events.objects.InitableObject;
import lineage2.gameserver.model.entity.events.objects.SpawnableObject;
import lineage2.gameserver.model.entity.events.objects.ZoneObject;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.taskmanager.actionrunner.ActionRunner;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.TimeUtils;

import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class GlobalEvent extends LoggerObject
{
	/**
	 * @author Mobius
	 */
	private class ListenerListImpl extends ListenerList<GlobalEvent>
	{
		/**
		 * Constructor for ListenerListImpl.
		 */
		public ListenerListImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onStart.
		 */
		public void onStart()
		{
			for (Listener<GlobalEvent> listener : getListeners())
			{
				if (OnStartStopListener.class.isInstance(listener))
				{
					((OnStartStopListener) listener).onStart(GlobalEvent.this);
				}
			}
		}
		
		/**
		 * Method onStop.
		 */
		public void onStop()
		{
			for (Listener<GlobalEvent> listener : getListeners())
			{
				if (OnStartStopListener.class.isInstance(listener))
				{
					((OnStartStopListener) listener).onStop(GlobalEvent.this);
				}
			}
		}
	}
	
	/**
	 * Field EVENT. (value is ""event"")
	 */
	public static final String EVENT = "event";
	/**
	 * Field _onTimeActions.
	 */
	protected final IntObjectMap<List<EventAction>> _onTimeActions = new TreeIntObjectMap<>();
	/**
	 * Field _onStartActions.
	 */
	protected final List<EventAction> _onStartActions = new ArrayList<>(0);
	/**
	 * Field _onStopActions.
	 */
	protected final List<EventAction> _onStopActions = new ArrayList<>(0);
	/**
	 * Field _onInitActions.
	 */
	protected final List<EventAction> _onInitActions = new ArrayList<>(0);
	/**
	 * Field _objects.
	 */
	protected final Map<String, List<Serializable>> _objects = new HashMap<>(0);
	/**
	 * Field _id.
	 */
	protected final int _id;
	/**
	 * Field _name.
	 */
	protected final String _name;
	/**
	 * Field _timerName.
	 */
	protected final String _timerName;
	/**
	 * Field _listenerList.
	 */
	protected final ListenerListImpl _listenerList = new ListenerListImpl();
	/**
	 * Field _banishedItems.
	 */
	protected IntObjectMap<ItemInstance> _banishedItems = Containers.emptyIntObjectMap();
	
	/**
	 * Constructor for GlobalEvent.
	 * @param set MultiValueSet<String>
	 */
	protected GlobalEvent(MultiValueSet<String> set)
	{
		this(set.getInteger("id"), set.getString("name"));
	}
	
	/**
	 * Constructor for GlobalEvent.
	 * @param id int
	 * @param name String
	 */
	protected GlobalEvent(int id, String name)
	{
		_id = id;
		_name = name;
		_timerName = id + "_" + name.toLowerCase().replace(" ", "_");
	}
	
	/**
	 * Method initEvent.
	 */
	public void initEvent()
	{
		callActions(_onInitActions);
		reCalcNextTime(true);
		printInfo();
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		callActions(_onStartActions);
		_listenerList.onStart();
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		callActions(_onStopActions);
		_listenerList.onStop();
	}
	
	/**
	 * Method printInfo.
	 */
	protected void printInfo()
	{
		info(getName() + " time - " + TimeUtils.toSimpleFormat(startTimeMillis()));
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getId() + ";" + getName() + "]";
	}
	
	/**
	 * Method callActions.
	 * @param actions List<EventAction>
	 */
	protected void callActions(List<EventAction> actions)
	{
		for (EventAction action : actions)
		{
			action.call(this);
		}
	}
	
	/**
	 * Method addOnStartActions.
	 * @param start List<EventAction>
	 */
	public void addOnStartActions(List<EventAction> start)
	{
		_onStartActions.addAll(start);
	}
	
	/**
	 * Method addOnStopActions.
	 * @param start List<EventAction>
	 */
	public void addOnStopActions(List<EventAction> start)
	{
		_onStopActions.addAll(start);
	}
	
	/**
	 * Method addOnInitActions.
	 * @param start List<EventAction>
	 */
	public void addOnInitActions(List<EventAction> start)
	{
		_onInitActions.addAll(start);
	}
	
	/**
	 * Method addOnTimeAction.
	 * @param time int
	 * @param action EventAction
	 */
	public void addOnTimeAction(int time, EventAction action)
	{
		List<EventAction> list = _onTimeActions.get(time);
		if (list != null)
		{
			list.add(action);
		}
		else
		{
			List<EventAction> actions = new ArrayList<>(1);
			actions.add(action);
			_onTimeActions.put(time, actions);
		}
	}
	
	/**
	 * Method addOnTimeActions.
	 * @param time int
	 * @param actions List<EventAction>
	 */
	public void addOnTimeActions(int time, List<EventAction> actions)
	{
		if (actions.isEmpty())
		{
			return;
		}
		List<EventAction> list = _onTimeActions.get(time);
		if (list != null)
		{
			list.addAll(actions);
		}
		else
		{
			_onTimeActions.put(time, new ArrayList<>(actions));
		}
	}
	
	/**
	 * Method timeActions.
	 * @param time int
	 */
	public void timeActions(int time)
	{
		List<EventAction> actions = _onTimeActions.get(time);
		if (actions == null)
		{
			info("Undefined time : " + time);
			return;
		}
		callActions(actions);
	}
	
	/**
	 * Method timeActions.
	 * @return int[]
	 */
	public int[] timeActions()
	{
		return _onTimeActions.keySet().toArray();
	}
	
	/**
	 * Method registerActions.
	 */
	public void registerActions()
	{
		long t = startTimeMillis();
		if (t == 0)
		{
			return;
		}
		for (int key : _onTimeActions.keySet().toArray())
		{
			ActionRunner.getInstance().register(t + (key * 1000L), new EventWrapper(_timerName, this, key));
		}
	}
	
	/**
	 * Method clearActions.
	 */
	public void clearActions()
	{
		ActionRunner.getInstance().clear(_timerName);
	}
	
	/**
	 * Method getObjects.
	 * @param name String
	 * @return List<O>
	 */
	@SuppressWarnings("unchecked")
	public <O extends Serializable> List<O> getObjects(String name)
	{
		List<Serializable> objects = _objects.get(name);
		return objects == null ? Collections.<O> emptyList() : (List<O>) objects;
	}
	
	/**
	 * Method getFirstObject.
	 * @param name String
	 * @return O
	 */
	public <O extends Serializable> O getFirstObject(String name)
	{
		List<O> objects = getObjects(name);
		return objects.size() > 0 ? objects.get(0) : null;
	}
	
	/**
	 * Method addObject.
	 * @param name String
	 * @param object Serializable
	 */
	public void addObject(String name, Serializable object)
	{
		if (object == null)
		{
			return;
		}
		List<Serializable> list = _objects.get(name);
		if (list != null)
		{
			list.add(object);
		}
		else
		{
			list = new CopyOnWriteArrayList<>();
			list.add(object);
			_objects.put(name, list);
		}
	}
	
	/**
	 * Method removeObject.
	 * @param name String
	 * @param o Serializable
	 */
	public void removeObject(String name, Serializable o)
	{
		if (o == null)
		{
			return;
		}
		List<Serializable> list = _objects.get(name);
		if (list != null)
		{
			list.remove(o);
		}
	}
	
	/**
	 * Method removeObjects.
	 * @param name String
	 * @return List<O>
	 */
	@SuppressWarnings("unchecked")
	public <O extends Serializable> List<O> removeObjects(String name)
	{
		List<Serializable> objects = _objects.remove(name);
		return objects == null ? Collections.<O> emptyList() : (List<O>) objects;
	}
	
	/**
	 * Method addObjects.
	 * @param name String
	 * @param objects List<? extends Serializable>
	 */
	@SuppressWarnings("unchecked")
	public void addObjects(String name, List<? extends Serializable> objects)
	{
		if (objects.isEmpty())
		{
			return;
		}
		List<Serializable> list = _objects.get(name);
		if (list != null)
		{
			list.addAll(objects);
		}
		else
		{
			_objects.put(name, (List<Serializable>) objects);
		}
	}
	
	/**
	 * Method getObjects.
	 * @return Map<String,List<Serializable>>
	 */
	public Map<String, List<Serializable>> getObjects()
	{
		return _objects;
	}
	
	/**
	 * Method spawnAction.
	 * @param name String
	 * @param spawn boolean
	 */
	public void spawnAction(String name, boolean spawn)
	{
		List<Serializable> objects = getObjects(name);
		if (objects.isEmpty())
		{
			info("Undefined objects: " + name);
			return;
		}
		for (Serializable object : objects)
		{
			if (object instanceof SpawnableObject)
			{
				if (spawn)
				{
					((SpawnableObject) object).spawnObject(this);
				}
				else
				{
					((SpawnableObject) object).despawnObject(this);
				}
			}
		}
	}
	
	/**
	 * Method doorAction.
	 * @param name String
	 * @param open boolean
	 */
	public void doorAction(String name, boolean open)
	{
		List<Serializable> objects = getObjects(name);
		if (objects.isEmpty())
		{
			info("Undefined objects: " + name);
			return;
		}
		for (Serializable object : objects)
		{
			if (object instanceof DoorObject)
			{
				if (open)
				{
					((DoorObject) object).open(this);
				}
				else
				{
					((DoorObject) object).close(this);
				}
			}
		}
	}
	
	/**
	 * Method zoneAction.
	 * @param name String
	 * @param active boolean
	 */
	public void zoneAction(String name, boolean active)
	{
		List<Serializable> objects = getObjects(name);
		if (objects.isEmpty())
		{
			info("Undefined objects: " + name);
			return;
		}
		for (Serializable object : objects)
		{
			if (object instanceof ZoneObject)
			{
				((ZoneObject) object).setActive(active, this);
			}
		}
	}
	
	/**
	 * Method initAction.
	 * @param name String
	 */
	public void initAction(String name)
	{
		List<Serializable> objects = getObjects(name);
		if (objects.isEmpty())
		{
			info("Undefined objects: " + name);
			return;
		}
		for (Serializable object : objects)
		{
			if (object instanceof InitableObject)
			{
				((InitableObject) object).initObject(this);
			}
		}
	}
	
	/**
	 * Method action.
	 * @param name String
	 * @param start boolean
	 */
	public void action(String name, boolean start)
	{
		if (name.equalsIgnoreCase(EVENT))
		{
			if (start)
			{
				startEvent();
			}
			else
			{
				stopEvent();
			}
		}
	}
	
	/**
	 * Method refreshAction.
	 * @param name String
	 */
	public void refreshAction(String name)
	{
		List<Serializable> objects = getObjects(name);
		if (objects.isEmpty())
		{
			info("Undefined objects: " + name);
			return;
		}
		for (Serializable object : objects)
		{
			if (object instanceof SpawnableObject)
			{
				((SpawnableObject) object).refreshObject(this);
			}
		}
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	public abstract void reCalcNextTime(boolean onInit);
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	protected abstract long startTimeMillis();
	
	/**
	 * Method broadcastToWorld.
	 * @param packet IStaticPacket
	 */
	public void broadcastToWorld(IStaticPacket packet)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method broadcastToWorld.
	 * @param packet L2GameServerPacket
	 */
	public void broadcastToWorld(L2GameServerPacket packet)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getCenterObject.
	 * @return GameObject
	 */
	public GameObject getCenterObject()
	{
		return null;
	}
	
	/**
	 * Method getReflection.
	 * @return Reflection
	 */
	public Reflection getReflection()
	{
		return ReflectionManager.DEFAULT;
	}
	
	/**
	 * Method getRelation.
	 * @param thisPlayer Player
	 * @param target Player
	 * @param oldRelation int
	 * @return int
	 */
	public int getRelation(Player thisPlayer, Player target, int oldRelation)
	{
		return oldRelation;
	}
	
	/**
	 * Method getUserRelation.
	 * @param thisPlayer Player
	 * @param oldRelation int
	 * @return int
	 */
	public int getUserRelation(Player thisPlayer, int oldRelation)
	{
		return oldRelation;
	}
	
	/**
	 * Method checkRestartLocs.
	 * @param player Player
	 * @param r Map<RestartType,Boolean>
	 */
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
	}
	
	/**
	 * Method getRestartLoc.
	 * @param player Player
	 * @param type RestartType
	 * @return Location
	 */
	public Location getRestartLoc(Player player, RestartType type)
	{
		return null;
	}
	
	/**
	 * Method canAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return boolean
	 */
	public boolean canAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		return false;
	}
	
	/**
	 * Method checkForAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return SystemMsg
	 */
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		return null;
	}
	
	/**
	 * Method isInProgress.
	 * @return boolean
	 */
	public boolean isInProgress()
	{
		return false;
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isParticle(Player player)
	{
		return false;
	}
	
	/**
	 * Method announce.
	 * @param a int
	 */
	public void announce(int a)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method teleportPlayers.
	 * @param teleportWho String
	 */
	public void teleportPlayers(String teleportWho)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method ifVar.
	 * @param name String
	 * @return boolean
	 */
	public boolean ifVar(String name)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method itemObtainPlayers.
	 * @return List<Player>
	 */
	public List<Player> itemObtainPlayers()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method giveItem.
	 * @param player Player
	 * @param itemId int
	 * @param count long
	 */
	public void giveItem(Player player, int itemId, long count)
	{
		switch (itemId)
		{
			case ItemTemplate.ITEM_ID_FAME:
				player.setFame(player.getFame() + (int) count, toString());
				break;
			default:
				Functions.addItem(player, itemId, count);
				break;
		}
	}
	
	/**
	 * Method broadcastPlayers.
	 * @param range int
	 * @return List<Player>
	 */
	public List<Player> broadcastPlayers(int range)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method canRessurect.
	 * @param resurrectPlayer Player
	 * @param creature Creature
	 * @param force boolean
	 * @return boolean
	 */
	public boolean canRessurect(Player resurrectPlayer, Creature creature, boolean force)
	{
		return true;
	}
	
	/**
	 * Method onAddEvent.
	 * @param o GameObject
	 */
	public void onAddEvent(GameObject o)
	{
	}
	
	/**
	 * Method onRemoveEvent.
	 * @param o GameObject
	 */
	public void onRemoveEvent(GameObject o)
	{
	}
	
	/**
	 * Method addBanishItem.
	 * @param item ItemInstance
	 */
	public void addBanishItem(ItemInstance item)
	{
		if (_banishedItems.isEmpty())
		{
			_banishedItems = new CHashIntObjectMap<>();
		}
		_banishedItems.put(item.getObjectId(), item);
	}
	
	/**
	 * Method removeBanishItems.
	 */
	public void removeBanishItems()
	{
		Iterator<IntObjectMap.Entry<ItemInstance>> iterator = _banishedItems.entrySet().iterator();
		while (iterator.hasNext())
		{
			IntObjectMap.Entry<ItemInstance> entry = iterator.next();
			iterator.remove();
			ItemInstance item = ItemsDAO.getInstance().load(entry.getKey());
			if (item != null)
			{
				if (item.getOwnerId() > 0)
				{
					GameObject object = GameObjectsStorage.findObject(item.getOwnerId());
					if ((object != null) && object.isPlayable())
					{
						((Playable) object).getInventory().destroyItem(item);
						object.getPlayer().sendPacket(SystemMessage2.removeItems(item));
					}
				}
				item.delete();
			}
			else
			{
				item = entry.getValue();
			}
			item.deleteMe();
		}
	}
	
	/**
	 * Method addListener.
	 * @param l Listener<GlobalEvent>
	 */
	public void addListener(Listener<GlobalEvent> l)
	{
		_listenerList.add(l);
	}
	
	/**
	 * Method removeListener.
	 * @param l Listener<GlobalEvent>
	 */
	public void removeListener(Listener<GlobalEvent> l)
	{
		_listenerList.remove(l);
	}
	
	/**
	 * Method cloneTo.
	 * @param e GlobalEvent
	 */
	public void cloneTo(GlobalEvent e)
	{
		for (EventAction a : _onInitActions)
		{
			e._onInitActions.add(a);
		}
		for (EventAction a : _onStartActions)
		{
			e._onStartActions.add(a);
		}
		for (EventAction a : _onStopActions)
		{
			e._onStopActions.add(a);
		}
		for (IntObjectMap.Entry<List<EventAction>> entry : _onTimeActions.entrySet())
		{
			e.addOnTimeActions(entry.getKey(), entry.getValue());
		}
	}
}
