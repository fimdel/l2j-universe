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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Skill.AddedSkill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExRedSky;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CursedWeapon
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _transformationName.
	 */
	private String _transformationName;
	/**
	 * Field _skillMaxLevel. Field _itemId.
	 */
	private final int _itemId, _skillMaxLevel;
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _disapearChance. Field _dropRate.
	 */
	private int _dropRate, _disapearChance;
	/**
	 * Field _durationLost. Field _durationMax. Field _durationMin.
	 */
	private int _durationMin, _durationMax, _durationLost;
	/**
	 * Field _transformationTemplateId. Field _transformationId.
	 */
	private int _transformationId, _transformationTemplateId;
	/**
	 * Field _playerPkKills. Field _playerKarma. Field _nbKills. Field _stageKills.
	 */
	private int _stageKills, _nbKills = 0, _playerKarma = 0, _playerPkKills = 0;
	/**
	 * Field _state.
	 */
	private CursedWeaponState _state = CursedWeaponState.NONE;
	/**
	 * Field _loc.
	 */
	private Location _loc = null;
	/**
	 * Field _owner. Field _endTime.
	 */
	private long _endTime = 0, _owner = 0;
	/**
	 * Field _item.
	 */
	private ItemInstance _item = null;
	
	/**
	 * @author Mobius
	 */
	public enum CursedWeaponState
	{
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field ACTIVATED.
		 */
		ACTIVATED,
		/**
		 * Field DROPPED.
		 */
		DROPPED,
	}
	
	/**
	 * Constructor for CursedWeapon.
	 * @param itemId int
	 * @param skillId int
	 * @param name String
	 */
	public CursedWeapon(int itemId, int skillId, String name)
	{
		_name = name;
		_itemId = itemId;
		_skillId = skillId;
		_skillMaxLevel = SkillTable.getInstance().getMaxLevel(_skillId);
	}
	
	/**
	 * Method initWeapon.
	 */
	public void initWeapon()
	{
		zeroOwner();
		setState(CursedWeaponState.NONE);
		_endTime = 0;
		_item = null;
		_nbKills = 0;
	}
	
	/**
	 * Method create.
	 * @param attackable NpcInstance
	 * @param killer Player
	 */
	public void create(NpcInstance attackable, Player killer)
	{
		_item = ItemFunctions.createItem(_itemId);
		if (_item != null)
		{
			zeroOwner();
			setState(CursedWeaponState.DROPPED);
			if (_endTime == 0)
			{
				_endTime = System.currentTimeMillis() + (getRndDuration() * 60000);
			}
			_item.dropToTheGround(attackable, Location.findPointToStay(attackable, 100));
			_loc = _item.getLoc();
			_item.setDropTime(0);
			L2GameServerPacket redSky = new ExRedSky(10);
			L2GameServerPacket eq = new Earthquake(killer.getLoc(), 30, 12);
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.sendPacket(redSky, eq);
			}
		}
	}
	
	/**
	 * Method dropIt.
	 * @param attackable NpcInstance
	 * @param killer Player
	 * @param owner Player
	 * @return boolean
	 */
	public boolean dropIt(NpcInstance attackable, Player killer, Player owner)
	{
		if (Rnd.chance(_disapearChance))
		{
			return false;
		}
		Player player = getOnlineOwner();
		if (player == null)
		{
			if (owner == null)
			{
				return false;
			}
			player = owner;
		}
		ItemInstance oldItem;
		if ((oldItem = player.getInventory().removeItemByItemId(_itemId, 1L)) == null)
		{
			return false;
		}
		player.setKarma(_playerKarma);
		player.setPkKills(_playerPkKills);
		player.setCursedWeaponEquippedId(0);
		player.setTransformation(0);
		player.setTransformationName(null);
		player.validateLocation(0);
		Skill skill = SkillTable.getInstance().getInfo(_skillId, player.getSkillLevel(_skillId));
		if (skill != null)
		{
			for (AddedSkill s : skill.getAddedSkills())
			{
				player.removeSkillById(s.id);
			}
		}
		player.removeSkillById(_skillId);
		player.abortAttack(true, false);
		zeroOwner();
		setState(CursedWeaponState.DROPPED);
		oldItem.dropToTheGround(player, Location.findPointToStay(player, 100));
		_loc = oldItem.getLoc();
		oldItem.setDropTime(0);
		_item = oldItem;
		player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_DROPPED_S1).addItemName(oldItem.getItemId()));
		player.broadcastUserInfo();
		player.broadcastPacket(new Earthquake(player.getLoc(), 30, 12));
		return true;
	}
	
	/**
	 * Method giveSkill.
	 * @param player Player
	 */
	private void giveSkill(Player player)
	{
		for (Skill s : getSkills(player))
		{
			player.addSkill(s, false);
			player._transformationSkills.put(s.getId(), s);
		}
		player.sendSkillList();
	}
	
	/**
	 * Method getSkills.
	 * @param player Player
	 * @return Collection<Skill>
	 */
	private Collection<Skill> getSkills(Player player)
	{
		int level = 1 + (_nbKills / _stageKills);
		if (level > _skillMaxLevel)
		{
			level = _skillMaxLevel;
		}
		Skill skill = SkillTable.getInstance().getInfo(_skillId, level);
		List<Skill> ret = new ArrayList<>();
		ret.add(skill);
		for (AddedSkill s : skill.getAddedSkills())
		{
			ret.add(SkillTable.getInstance().getInfo(s.id, s.level));
		}
		return ret;
	}
	
	/**
	 * Method reActivate.
	 * @return boolean
	 */
	public boolean reActivate()
	{
		if (getTimeLeft() <= 0)
		{
			if (getPlayerId() != 0)
			{
				setState(CursedWeaponState.ACTIVATED);
			}
			return false;
		}
		if (getPlayerId() == 0)
		{
			if ((_loc == null) || ((_item = ItemFunctions.createItem(_itemId)) == null))
			{
				return false;
			}
			_item.dropMe(null, _loc);
			_item.setDropTime(0);
			setState(CursedWeaponState.DROPPED);
		}
		else
		{
			setState(CursedWeaponState.ACTIVATED);
		}
		return true;
	}
	
	/**
	 * Method activate.
	 * @param player Player
	 * @param item ItemInstance
	 */
	public void activate(Player player, ItemInstance item)
	{
		if (isDropped() || (getPlayerId() != player.getObjectId()))
		{
			_playerKarma = player.getKarma();
			_playerPkKills = player.getPkKills();
		}
		setPlayer(player);
		setState(CursedWeaponState.ACTIVATED);
		player.leaveParty();
		if (player.isMounted())
		{
			player.setMount(0, 0, 0);
		}
		_item = item;
		player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, null);
		player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_RHAND, null);
		player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_RHAND, _item);
		player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EQUIPPED_YOUR_S1).addItemName(_item.getItemId()));
		player.setTransformation(0);
		player.setCursedWeaponEquippedId(_itemId);
		player.setTransformation(_transformationId);
		player.setTransformationName(_transformationName);
		player.setTransformationTemplate(_transformationTemplateId);
		player.setPkKills(_nbKills);
		if (_endTime == 0)
		{
			_endTime = System.currentTimeMillis() + (getRndDuration() * 60000);
		}
		giveSkill(player);
		player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
		player.setCurrentCp(player.getMaxCp());
		player.broadcastUserInfo();
	}
	
	/**
	 * Method increaseKills.
	 */
	public void increaseKills()
	{
		Player player = getOnlineOwner();
		if (player == null)
		{
			return;
		}
		_nbKills++;
		player.setPkKills(_nbKills);
		player.updateStats();
		if (((_nbKills % _stageKills) == 0) && (_nbKills <= (_stageKills * (_skillMaxLevel - 1))))
		{
			giveSkill(player);
		}
		_endTime -= _durationLost * 60000;
	}
	
	/**
	 * Method setDisapearChance.
	 * @param disapearChance int
	 */
	public void setDisapearChance(int disapearChance)
	{
		_disapearChance = disapearChance;
	}
	
	/**
	 * Method setDropRate.
	 * @param dropRate int
	 */
	public void setDropRate(int dropRate)
	{
		_dropRate = dropRate;
	}
	
	/**
	 * Method setDurationMin.
	 * @param duration int
	 */
	public void setDurationMin(int duration)
	{
		_durationMin = duration;
	}
	
	/**
	 * Method setDurationMax.
	 * @param duration int
	 */
	public void setDurationMax(int duration)
	{
		_durationMax = duration;
	}
	
	/**
	 * Method setDurationLost.
	 * @param durationLost int
	 */
	public void setDurationLost(int durationLost)
	{
		_durationLost = durationLost;
	}
	
	/**
	 * Method setStageKills.
	 * @param stageKills int
	 */
	public void setStageKills(int stageKills)
	{
		_stageKills = stageKills;
	}
	
	/**
	 * Method setTransformationId.
	 * @param transformationId int
	 */
	public void setTransformationId(int transformationId)
	{
		_transformationId = transformationId;
	}
	
	/**
	 * Method getTransformationId.
	 * @return int
	 */
	public int getTransformationId()
	{
		return _transformationId;
	}
	
	/**
	 * Method setTransformationTemplateId.
	 * @param transformationTemplateId int
	 */
	public void setTransformationTemplateId(int transformationTemplateId)
	{
		_transformationTemplateId = transformationTemplateId;
	}
	
	/**
	 * Method setTransformationName.
	 * @param name String
	 */
	public void setTransformationName(String name)
	{
		_transformationName = name;
	}
	
	/**
	 * Method setNbKills.
	 * @param nbKills int
	 */
	public void setNbKills(int nbKills)
	{
		_nbKills = nbKills;
	}
	
	/**
	 * Method setPlayerId.
	 * @param playerId int
	 */
	public void setPlayerId(int playerId)
	{
		_owner = playerId == 0 ? 0 : GameObjectsStorage.objIdNoStore(playerId);
	}
	
	/**
	 * Method setPlayerKarma.
	 * @param playerKarma int
	 */
	public void setPlayerKarma(int playerKarma)
	{
		_playerKarma = playerKarma;
	}
	
	/**
	 * Method setPlayerPkKills.
	 * @param playerPkKills int
	 */
	public void setPlayerPkKills(int playerPkKills)
	{
		_playerPkKills = playerPkKills;
	}
	
	/**
	 * Method setState.
	 * @param state CursedWeaponState
	 */
	public void setState(CursedWeaponState state)
	{
		_state = state;
	}
	
	/**
	 * Method setEndTime.
	 * @param endTime long
	 */
	public void setEndTime(long endTime)
	{
		_endTime = endTime;
	}
	
	/**
	 * Method setPlayer.
	 * @param player Player
	 */
	public void setPlayer(Player player)
	{
		if (player != null)
		{
			_owner = player.getStoredId();
		}
		else if (_owner != 0)
		{
			setPlayerId(getPlayerId());
		}
	}
	
	/**
	 * Method zeroOwner.
	 */
	private void zeroOwner()
	{
		_owner = 0;
		_playerKarma = 0;
		_playerPkKills = 0;
	}
	
	/**
	 * Method setItem.
	 * @param item ItemInstance
	 */
	public void setItem(ItemInstance item)
	{
		_item = item;
	}
	
	/**
	 * Method setLoc.
	 * @param loc Location
	 */
	public void setLoc(Location loc)
	{
		_loc = loc;
	}
	
	/**
	 * Method getState.
	 * @return CursedWeaponState
	 */
	public CursedWeaponState getState()
	{
		return _state;
	}
	
	/**
	 * Method isActivated.
	 * @return boolean
	 */
	public boolean isActivated()
	{
		return getState() == CursedWeaponState.ACTIVATED;
	}
	
	/**
	 * Method isDropped.
	 * @return boolean
	 */
	public boolean isDropped()
	{
		return getState() == CursedWeaponState.DROPPED;
	}
	
	/**
	 * Method getEndTime.
	 * @return long
	 */
	public long getEndTime()
	{
		return _endTime;
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
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getItem.
	 * @return ItemInstance
	 */
	public ItemInstance getItem()
	{
		return _item;
	}
	
	/**
	 * Method getSkillId.
	 * @return int
	 */
	public int getSkillId()
	{
		return _skillId;
	}
	
	/**
	 * Method getDropRate.
	 * @return int
	 */
	public int getDropRate()
	{
		return _dropRate;
	}
	
	/**
	 * Method getPlayerId.
	 * @return int
	 */
	public int getPlayerId()
	{
		return _owner == 0 ? 0 : GameObjectsStorage.getStoredObjectId(_owner);
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _owner == 0 ? null : GameObjectsStorage.getAsPlayer(_owner);
	}
	
	/**
	 * Method getPlayerKarma.
	 * @return int
	 */
	public int getPlayerKarma()
	{
		return _playerKarma;
	}
	
	/**
	 * Method getPlayerPkKills.
	 * @return int
	 */
	public int getPlayerPkKills()
	{
		return _playerPkKills;
	}
	
	/**
	 * Method getNbKills.
	 * @return int
	 */
	public int getNbKills()
	{
		return _nbKills;
	}
	
	/**
	 * Method getStageKills.
	 * @return int
	 */
	public int getStageKills()
	{
		return _stageKills;
	}
	
	/**
	 * Method getLoc.
	 * @return Location
	 */
	public Location getLoc()
	{
		return _loc;
	}
	
	/**
	 * Method getRndDuration.
	 * @return int
	 */
	public int getRndDuration()
	{
		if (_durationMin > _durationMax)
		{
			_durationMax = 2 * _durationMin;
		}
		return Rnd.get(_durationMin, _durationMax);
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return isActivated() || isDropped();
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return Math.min(1 + (_nbKills / _stageKills), _skillMaxLevel);
	}
	
	/**
	 * Method getTimeLeft.
	 * @return long
	 */
	public long getTimeLeft()
	{
		return _endTime - System.currentTimeMillis();
	}
	
	/**
	 * Method getWorldPosition.
	 * @return Location
	 */
	public Location getWorldPosition()
	{
		if (isActivated())
		{
			Player player = getOnlineOwner();
			if (player != null)
			{
				return player.getLoc();
			}
		}
		else if (isDropped())
		{
			if (_item != null)
			{
				return _item.getLoc();
			}
		}
		return null;
	}
	
	/**
	 * Method getOnlineOwner.
	 * @return Player
	 */
	public Player getOnlineOwner()
	{
		Player player = getPlayer();
		return (player != null) && player.isOnline() ? player : null;
	}
	
	/**
	 * Method isOwned.
	 * @return boolean
	 */
	public boolean isOwned()
	{
		return _owner != 0;
	}
}
