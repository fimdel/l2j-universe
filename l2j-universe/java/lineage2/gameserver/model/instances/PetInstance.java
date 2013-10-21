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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Future;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.PetData;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.BaseStats;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PetInventory;
import lineage2.gameserver.model.items.attachment.FlagItemAttachment;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetInstance extends Summon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PetInstance.class);
	/**
	 * Field DELUXE_FOOD_FOR_STRIDER. (value is 5169)
	 */
	private static final int DELUXE_FOOD_FOR_STRIDER = 5169;
	
	/**
	 * @author Mobius
	 */
	class FeedTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player owner = getPlayer();
			while ((getCurrentFed() <= (0.55 * getMaxFed())) && tryFeed())
			{
			}
			if (PetDataTable.isVitaminPet(getNpcId()) && (getCurrentFed() <= 0))
			{
				deleteMe();
			}
			else if (getCurrentFed() <= (0.10 * getMaxFed()))
			{
				owner.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2PetInstance.UnSummonHungryPet", owner));
				unSummon();
				return;
			}
			setCurrentFed(getCurrentFed() - 5);
			sendStatusUpdate();
			startFeed(isInCombat());
		}
	}
	
	/**
	 * Field _controlItemObjId.
	 */
	private final int _controlItemObjId;
	/**
	 * Field _curFed.
	 */
	private int _curFed;
	/**
	 * Field _data.
	 */
	protected PetData _data;
	/**
	 * Field _feedTask.
	 */
	private Future<?> _feedTask;
	/**
	 * Field _inventory.
	 */
	protected PetInventory _inventory;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _respawned.
	 */
	private boolean _respawned;
	/**
	 * Field lostExp.
	 */
	private int lostExp;
	
	/**
	 * Method restore.
	 * @param control ItemInstance
	 * @param template NpcTemplate
	 * @param owner Player
	 * @return PetInstance
	 */
	public static final PetInstance restore(ItemInstance control, NpcTemplate template, Player owner)
	{
		PetInstance pet = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT objId, name, level, curHp, curMp, exp, sp, fed FROM pets WHERE item_obj_id=?");
			statement.setInt(1, control.getObjectId());
			rset = statement.executeQuery();
			if (!rset.next())
			{
				if (PetDataTable.isBabyPet(template.getNpcId()) || PetDataTable.isImprovedBabyPet(template.getNpcId()))
				{
					pet = new PetBabyInstance(IdFactory.getInstance().getNextId(), template, owner, control);
				}
				else
				{
					pet = new PetInstance(IdFactory.getInstance().getNextId(), template, owner, control);
				}
				return pet;
			}
			if (PetDataTable.isBabyPet(template.getNpcId()) || PetDataTable.isImprovedBabyPet(template.getNpcId()))
			{
				pet = new PetBabyInstance(rset.getInt("objId"), template, owner, control, rset.getInt("level"), rset.getLong("exp"));
			}
			else
			{
				pet = new PetInstance(rset.getInt("objId"), template, owner, control, rset.getInt("level"), rset.getLong("exp"));
			}
			pet.setRespawned(true);
			String name = rset.getString("name");
			pet.setName((name == null) || name.isEmpty() ? template.name : name);
			pet.setCurrentHpMp(rset.getDouble("curHp"), rset.getInt("curMp"), true);
			pet.setCurrentCp(pet.getMaxCp());
			pet.setSp(rset.getInt("sp"));
			pet.setCurrentFed(rset.getInt("fed"));
		}
		catch (Exception e)
		{
			_log.error("Could not restore Pet data from item: " + control + "!", e);
			return null;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return pet;
	}
	
	/**
	 * Constructor for PetInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param control ItemInstance
	 */
	public PetInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control)
	{
		this(objectId, template, owner, control, 0, 0);
	}
	
	/**
	 * Constructor for PetInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param control ItemInstance
	 * @param currentLevel int
	 * @param exp long
	 */
	public PetInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control, int currentLevel, long exp)
	{
		super(objectId, template, owner);
		_controlItemObjId = control.getObjectId();
		_exp = exp;
		_level = control.getEnchantLevel();
		if (_level <= 0)
		{
			if (template.npcId == PetDataTable.SIN_EATER_ID)
			{
				_level = owner.getLevel();
			}
			else
			{
				_level = template.level;
			}
			_exp = getExpForThisLevel();
		}
		int minLevel = PetDataTable.getMinLevel(template.npcId);
		if (_level < minLevel)
		{
			_level = minLevel;
		}
		if (_exp < getExpForThisLevel())
		{
			_exp = getExpForThisLevel();
		}
		while ((_exp >= getExpForNextLevel()) && (_level < Experience.getMaxLevel()))
		{
			_level++;
		}
		while ((_exp < getExpForThisLevel()) && (_level > minLevel))
		{
			_level--;
		}
		if (PetDataTable.isVitaminPet(template.npcId))
		{
			_level = owner.getLevel();
			_exp = getExpForNextLevel();
		}
		_data = PetDataTable.getInstance().getInfo(template.npcId, _level);
		_inventory = new PetInventory(this);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		startFeed(false);
	}
	
	/**
	 * Method onDespawn.
	 */
	@Override
	protected void onDespawn()
	{
		super.onSpawn();
		stopFeed();
	}
	
	/**
	 * Method tryFeedItem.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean tryFeedItem(ItemInstance item)
	{
		if (item == null)
		{
			return false;
		}
		boolean deluxFood = PetDataTable.isStrider(getNpcId()) && (item.getItemId() == DELUXE_FOOD_FOR_STRIDER);
		if ((getFoodId() != item.getItemId()) && !deluxFood)
		{
			return false;
		}
		int newFed = Math.min(getMaxFed(), getCurrentFed() + Math.max((getMaxFed() * getAddFed() * (deluxFood ? 2 : 1)) / 100, 1));
		if (getCurrentFed() != newFed)
		{
			if (getInventory().destroyItem(item, 1L))
			{
				getPlayer().sendPacket(new SystemMessage(SystemMessage.PET_TOOK_S1_BECAUSE_HE_WAS_HUNGRY).addItemName(item.getItemId()));
				setCurrentFed(newFed);
				sendStatusUpdate();
			}
		}
		return true;
	}
	
	/**
	 * Method tryFeed.
	 * @return boolean
	 */
	public boolean tryFeed()
	{
		ItemInstance food = getInventory().getItemByItemId(getFoodId());
		if ((food == null) && PetDataTable.isStrider(getNpcId()))
		{
			food = getInventory().getItemByItemId(DELUXE_FOOD_FOR_STRIDER);
		}
		return tryFeedItem(food);
	}
	
	/**
	 * Method addExpAndSp.
	 * @param addToExp long
	 * @param addToSp long
	 */
	@Override
	public void addExpAndSp(long addToExp, long addToSp)
	{
		Player owner = getPlayer();
		if (PetDataTable.isVitaminPet(getNpcId()))
		{
			return;
		}
		_exp += addToExp;
		_sp += addToSp;
		if (_exp > getMaxExp())
		{
			_exp = getMaxExp();
		}
		if ((addToExp > 0) || (addToSp > 0))
		{
			owner.sendPacket(new SystemMessage(SystemMessage.THE_PET_ACQUIRED_EXPERIENCE_POINTS_OF_S1).addNumber(addToExp));
		}
		int old_level = _level;
		while ((_exp >= getExpForNextLevel()) && (_level < Experience.getMaxLevel()))
		{
			_level++;
		}
		while ((_exp < getExpForThisLevel()) && (_level > getMinLevel()))
		{
			_level--;
		}
		if (old_level < _level)
		{
			owner.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2PetInstance.PetLevelUp", owner).addNumber(_level));
			broadcastPacket(new SocialAction(getObjectId(), SocialAction.LEVEL_UP));
			setCurrentHpMp(getMaxHp(), getMaxMp());
		}
		if (old_level != _level)
		{
			updateControlItem();
			updateData();
		}
		if ((addToExp > 0) || (addToSp > 0))
		{
			sendStatusUpdate();
		}
	}
	
	/**
	 * Method consumeItem.
	 * @param itemConsumeId int
	 * @param itemCount long
	 * @return boolean
	 */
	@Override
	public boolean consumeItem(int itemConsumeId, long itemCount)
	{
		return getInventory().destroyItemByItemId(itemConsumeId, itemCount);
	}
	
	/**
	 * Method deathPenalty.
	 */
	private void deathPenalty()
	{
		if (isInZoneBattle())
		{
			return;
		}
		int lvl = getLevel();
		double percentLost = (-0.07 * lvl) + 6.5;
		lostExp = (int) Math.round(((getExpForNextLevel() - getExpForThisLevel()) * percentLost) / 100);
		addExpAndSp(-lostExp, 0);
	}
	
	/**
	 * Method destroyControlItem.
	 */
	private void destroyControlItem()
	{
		Player owner = getPlayer();
		if (getControlItemObjId() == 0)
		{
			return;
		}
		if (!owner.getInventory().destroyItemByObjectId(getControlItemObjId(), 1L))
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?");
			statement.setInt(1, getControlItemObjId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("could not delete pet:" + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		Player owner = getPlayer();
		owner.sendPacket(Msg.THE_PET_HAS_BEEN_KILLED_IF_YOU_DO_NOT_RESURRECT_IT_WITHIN_24_HOURS_THE_PETS_BODY_WILL_DISAPPEAR_ALONG_WITH_ALL_THE_PETS_ITEMS);
		startDecay(86400000L);
		if (PetDataTable.isVitaminPet(getNpcId()))
		{
			return;
		}
		stopFeed();
		deathPenalty();
	}
	
	/**
	 * Method doPickupItem.
	 * @param object GameObject
	 */
	@Override
	public void doPickupItem(GameObject object)
	{
		Player owner = getPlayer();
		stopMove();
		if (!object.isItem())
		{
			return;
		}
		ItemInstance item = (ItemInstance) object;
		if (item.isCursed())
		{
			owner.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_FAILED_TO_PICK_UP_S1).addItemName(item.getItemId()));
			return;
		}
		synchronized (item)
		{
			if (!item.isVisible())
			{
				return;
			}
			if (item.isHerb())
			{
				Skill[] skills = item.getTemplate().getAttachedSkills();
				if (skills.length > 0)
				{
					for (Skill skill : skills)
					{
						altUseSkill(skill, this);
					}
				}
				item.deleteMe();
				return;
			}
			if (!getInventory().validateWeight(item))
			{
				sendPacket(Msg.EXCEEDED_PET_INVENTORYS_WEIGHT_LIMIT);
				return;
			}
			if (!getInventory().validateCapacity(item))
			{
				sendPacket(Msg.DUE_TO_THE_VOLUME_LIMIT_OF_THE_PETS_INVENTORY_NO_MORE_ITEMS_CAN_BE_PLACED_THERE);
				return;
			}
			if (!item.getTemplate().getHandler().pickupItem(this, item))
			{
				return;
			}
			FlagItemAttachment attachment = item.getAttachment() instanceof FlagItemAttachment ? (FlagItemAttachment) item.getAttachment() : null;
			if (attachment != null)
			{
				return;
			}
			item.pickupMe();
		}
		if ((owner.getParty() == null) || (owner.getParty().getLootDistribution() == Party.ITEM_LOOTER))
		{
			getInventory().addItem(item);
			sendChanges();
		}
		else
		{
			owner.getParty().distributeItem(owner, item, null);
		}
		broadcastPickUpMsg(item);
	}
	
	/**
	 * Method doRevive.
	 * @param percent double
	 */
	public void doRevive(double percent)
	{
		restoreExp(percent);
		doRevive();
	}
	
	/**
	 * Method doRevive.
	 */
	@Override
	public void doRevive()
	{
		stopDecay();
		super.doRevive();
		startFeed(false);
		setRunning();
	}
	
	/**
	 * Method getAccuracy.
	 * @return int
	 */
	@Override
	public int getAccuracy()
	{
		return (int) calcStat(Stats.ACCURACY_COMBAT, _data.getAccuracy(), null, null);
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
	 * Method getControlItem.
	 * @return ItemInstance
	 */
	public ItemInstance getControlItem()
	{
		Player owner = getPlayer();
		if (owner == null)
		{
			return null;
		}
		int item_obj_id = getControlItemObjId();
		if (item_obj_id == 0)
		{
			return null;
		}
		return owner.getInventory().getItemByObjectId(item_obj_id);
	}
	
	/**
	 * Method getControlItemObjId.
	 * @return int
	 */
	@Override
	public int getControlItemObjId()
	{
		return _controlItemObjId;
	}
	
	/**
	 * Method getCriticalHit.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getCriticalHit(Creature target, Skill skill)
	{
		return (int) calcStat(Stats.CRITICAL_BASE, _data.getCritical(), target, skill);
	}
	
	/**
	 * Method getCurrentFed.
	 * @return int
	 */
	@Override
	public int getCurrentFed()
	{
		return _curFed;
	}
	
	/**
	 * Method getEvasionRate.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getEvasionRate(Creature target)
	{
		return (int) calcStat(Stats.EVASION_RATE, _data.getEvasion(), target, null);
	}
	
	/**
	 * Method getExpForNextLevel.
	 * @return long
	 */
	@Override
	public long getExpForNextLevel()
	{
		return PetDataTable.getInstance().getInfo(getNpcId(), _level + 1).getExp();
	}
	
	/**
	 * Method getExpForThisLevel.
	 * @return long
	 */
	@Override
	public long getExpForThisLevel()
	{
		return PetDataTable.getInstance().getInfo(getNpcId(), _level).getExp();
	}
	
	/**
	 * Method getFoodId.
	 * @return int
	 */
	public int getFoodId()
	{
		return _data.getFoodId();
	}
	
	/**
	 * Method getAddFed.
	 * @return int
	 */
	public int getAddFed()
	{
		return _data.getAddFed();
	}
	
	/**
	 * Method getInventory.
	 * @return PetInventory
	 */
	@Override
	public PetInventory getInventory()
	{
		return _inventory;
	}
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	@Override
	public long getWearedMask()
	{
		return _inventory.getWearedMask();
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public final int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method setLevel.
	 * @param level int
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	
	/**
	 * Method getLevelMod.
	 * @return double
	 */
	@Override
	public double getLevelMod()
	{
		return (89. + getLevel()) / 100.0;
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _data.getMinLevel();
	}
	
	/**
	 * Method getMaxExp.
	 * @return long
	 */
	public long getMaxExp()
	{
		return PetDataTable.getInstance().getInfo(getNpcId(), Experience.getMaxLevel()).getExp();
	}
	
	/**
	 * Method getMaxFed.
	 * @return int
	 */
	@Override
	public int getMaxFed()
	{
		return _data.getFeedMax();
	}
	
	/**
	 * Method getMaxLoad.
	 * @return int
	 */
	@Override
	public int getMaxLoad()
	{
		return (int) calcStat(Stats.MAX_LOAD, _data.getMaxLoad(), null, null);
	}
	
	/**
	 * Method getInventoryLimit.
	 * @return int
	 */
	@Override
	public int getInventoryLimit()
	{
		return Config.ALT_PET_INVENTORY_LIMIT;
	}
	
	/**
	 * Method getMaxHp.
	 * @return int
	 */
	@Override
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, _data.getHP(), null, null);
	}
	
	/**
	 * Method getMaxMp.
	 * @return int
	 */
	@Override
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, _data.getMP(), null, null);
	}
	
	/**
	 * Method getPAtk.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPAtk(Creature target)
	{
		double mod = BaseStats.STR.calcBonus(this) * getLevelMod();
		return (int) calcStat(Stats.POWER_ATTACK, _data.getPAtk() / mod, target, null);
	}
	
	/**
	 * Method getPDef.
	 * @param target Creature
	 * @return int
	 */
	@Override
	public int getPDef(Creature target)
	{
		double mod = getLevelMod();
		return (int) calcStat(Stats.POWER_DEFENCE, _data.getPDef() / mod, target, null);
	}
	
	/**
	 * Method getMAtk.
	 * @param target Creature
	 * @param skill Skill
	 * @return int
	 */
	@Override
	public int getMAtk(Creature target, Skill skill)
	{
		double ib = BaseStats.INT.calcBonus(this);
		double lvlb = getLevelMod();
		double mod = lvlb * lvlb * ib * ib;
		return (int) calcStat(Stats.MAGIC_ATTACK, _data.getMAtk() / mod, target, skill);
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
		double mod = BaseStats.MEN.calcBonus(this) * getLevelMod();
		return (int) calcStat(Stats.MAGIC_DEFENCE, _data.getMDef() / mod, target, skill);
	}
	
	/**
	 * Method getPAtkSpd.
	 * @return int
	 */
	@Override
	public int getPAtkSpd()
	{
		return (int) calcStat(Stats.POWER_ATTACK_SPEED, calcStat(Stats.ATK_BASE, _data.getAtkSpeed(), null, null), null, null);
	}
	
	/**
	 * Method getMAtkSpd.
	 * @return int
	 */
	@Override
	public int getMAtkSpd()
	{
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, _data.getCastSpeed(), null, null);
	}
	
	/**
	 * Method getRunSpeed.
	 * @return int
	 */
	@Override
	public int getRunSpeed()
	{
		return getSpeed(_data.getSpeed());
	}
	
	/**
	 * Method getSoulshotConsumeCount.
	 * @return int
	 */
	@Override
	public int getSoulshotConsumeCount()
	{
		return PetDataTable.getSoulshots(getNpcId());
	}
	
	/**
	 * Method getSpiritshotConsumeCount.
	 * @return int
	 */
	@Override
	public int getSpiritshotConsumeCount()
	{
		return PetDataTable.getSpiritshots(getNpcId());
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
	 * Method getSkillLevel.
	 * @param skillId int
	 * @return int
	 */
	public int getSkillLevel(int skillId)
	{
		if ((_skills == null) || (_skills.get(skillId) == null))
		{
			return -1;
		}
		int lvl = getLevel();
		return lvl > 70 ? 7 + ((lvl - 70) / 5) : lvl / 10;
	}
	
	/**
	 * Method getSummonType.
	 * @return int
	 */
	@Override
	public int getSummonType()
	{
		return 2;
	}
	
	/**
	 * Method getTemplate.
	 * @return NpcTemplate
	 */
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) _template;
	}
	
	/**
	 * Method isMountable.
	 * @return boolean
	 */
	@Override
	public boolean isMountable()
	{
		return _data.isMountable();
	}
	
	/**
	 * Method isRespawned.
	 * @return boolean
	 */
	public boolean isRespawned()
	{
		return _respawned;
	}
	
	/**
	 * Method restoreExp.
	 * @param percent double
	 */
	public void restoreExp(double percent)
	{
		if (lostExp != 0)
		{
			addExpAndSp((long) ((lostExp * percent) / 100.), 0);
			lostExp = 0;
		}
	}
	
	/**
	 * Method setCurrentFed.
	 * @param num int
	 */
	public void setCurrentFed(int num)
	{
		_curFed = Math.min(getMaxFed(), Math.max(0, num));
	}
	
	/**
	 * Method setRespawned.
	 * @param respawned boolean
	 */
	public void setRespawned(boolean respawned)
	{
		_respawned = respawned;
	}
	
	/**
	 * Method setSp.
	 * @param sp int
	 */
	@Override
	public void setSp(int sp)
	{
		_sp = sp;
	}
	
	/**
	 * Method startFeed.
	 * @param battleFeed boolean
	 */
	public void startFeed(boolean battleFeed)
	{
		boolean first = _feedTask == null;
		stopFeed();
		if (!isDead())
		{
			int feedTime;
			if (PetDataTable.isVitaminPet(getNpcId()))
			{
				feedTime = 10000;
			}
			else
			{
				feedTime = Math.max(first ? 15000 : 1000, 60000 / (battleFeed ? _data.getFeedBattle() : _data.getFeedNormal()));
			}
			_feedTask = ThreadPoolManager.getInstance().schedule(new FeedTask(), feedTime);
		}
	}
	
	/**
	 * Method stopFeed.
	 */
	private void stopFeed()
	{
		if (_feedTask != null)
		{
			_feedTask.cancel(false);
			_feedTask = null;
		}
	}
	
	/**
	 * Method store.
	 */
	public void store()
	{
		if ((getControlItemObjId() == 0) || (_exp == 0))
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			String req;
			if (!isRespawned())
			{
				req = "INSERT INTO pets (name,level,curHp,curMp,exp,sp,fed,objId,item_obj_id) VALUES (?,?,?,?,?,?,?,?,?)";
			}
			else
			{
				req = "UPDATE pets SET name=?,level=?,curHp=?,curMp=?,exp=?,sp=?,fed=?,objId=? WHERE item_obj_id = ?";
			}
			statement = con.prepareStatement(req);
			statement.setString(1, getName().equalsIgnoreCase(getTemplate().name) ? "" : getName());
			statement.setInt(2, _level);
			statement.setDouble(3, getCurrentHp());
			statement.setDouble(4, getCurrentMp());
			statement.setLong(5, _exp);
			statement.setLong(6, _sp);
			statement.setInt(7, _curFed);
			statement.setInt(8, getObjectId());
			statement.setInt(9, _controlItemObjId);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error("Could not store pet data!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		_respawned = true;
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		super.onDelete();
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	protected void onDecay()
	{
		getInventory().store();
		destroyControlItem();
		super.onDecay();
	}
	
	/**
	 * Method unSummon.
	 */
	@Override
	public void unSummon()
	{
		stopFeed();
		getInventory().store();
		store();
		super.unSummon();
	}
	
	/**
	 * Method getSummonPoint.
	 * @return int
	 */
	@Override
	public int getSummonPoint()
	{
		return 0;
	}
	
	/**
	 * Method updateControlItem.
	 */
	public void updateControlItem()
	{
		ItemInstance controlItem = getControlItem();
		if (controlItem == null)
		{
			return;
		}
		controlItem.setEnchantLevel(_level);
		controlItem.setCustomType2(isDefaultName() ? 0 : 1);
		controlItem.setJdbcState(JdbcEntityState.UPDATED);
		controlItem.update();
		Player owner = getPlayer();
		owner.sendPacket(new InventoryUpdate().addModifiedItem(controlItem));
	}
	
	/**
	 * Method updateData.
	 */
	private void updateData()
	{
		_data = PetDataTable.getInstance().getInfo(getTemplate().npcId, _level);
	}
	
	/**
	 * Method getExpPenalty.
	 * @return double
	 */
	@Override
	public double getExpPenalty()
	{
		return PetDataTable.getExpPenalty(getTemplate().npcId);
	}
	
	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		Player owner = getPlayer();
		if (crit)
		{
			owner.sendPacket(SystemMsg.SUMMONED_MONSTERS_CRITICAL_HIT);
		}
		if (miss)
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(this));
		}
		else
		{
			owner.sendPacket(new SystemMessage(SystemMessage.THE_PET_GAVE_DAMAGE_OF_S1).addNumber(damage));
		}
	}
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
		Player owner = getPlayer();
		if (!isDead())
		{
			SystemMessage sm = new SystemMessage(SystemMessage.THE_PET_RECEIVED_DAMAGE_OF_S2_CAUSED_BY_S1);
			if (attacker.isNpc())
			{
				sm.addNpcName(((NpcInstance) attacker).getTemplate().npcId);
			}
			else
			{
				sm.addString(attacker.getName());
			}
			sm.addNumber((long) damage);
			owner.sendPacket(sm);
		}
	}
	
	/**
	 * Method getFormId.
	 * @return int
	 */
	@Override
	public int getFormId()
	{
		switch (getNpcId())
		{
			case PetDataTable.GREAT_WOLF_ID:
			case PetDataTable.WGREAT_WOLF_ID:
			case PetDataTable.FENRIR_WOLF_ID:
			case PetDataTable.WFENRIR_WOLF_ID:
				if (getLevel() >= 70)
				{
					return 3;
				}
				else if (getLevel() >= 65)
				{
					return 2;
				}
				else if (getLevel() >= 60)
				{
					return 1;
				}
				break;
		}
		return 0;
	}
	
	/**
	 * Method isPet.
	 * @return boolean
	 */
	@Override
	public boolean isPet()
	{
		return true;
	}
	
	/**
	 * Method isDefaultName.
	 * @return boolean
	 */
	public boolean isDefaultName()
	{
		return StringUtils.isEmpty(_name) || getName().equalsIgnoreCase(getTemplate().name);
	}
	
	/**
	 * Method getSummonSkillId.
	 * @return int
	 */
	@Override
	public int getSummonSkillId()
	{
		return 0;
	}
	
	/**
	 * Method getSummonSkillLvl.
	 * @return int
	 */
	@Override
	public int getSummonSkillLvl()
	{
		return 0;
	}
}
