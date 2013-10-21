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

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.EffectList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PetBabyInstance extends PetInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PetBabyInstance.class);
	/**
	 * Field _actionTask.
	 */
	Future<?> _actionTask;
	/**
	 * Field _buffEnabled.
	 */
	private boolean _buffEnabled = true;
	
	/**
	 * Constructor for PetBabyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param control ItemInstance
	 * @param _currentLevel int
	 * @param exp long
	 */
	public PetBabyInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control, int _currentLevel, long exp)
	{
		super(objectId, template, owner, control, _currentLevel, exp);
	}
	
	/**
	 * Constructor for PetBabyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param control ItemInstance
	 */
	public PetBabyInstance(int objectId, NpcTemplate template, Player owner, ItemInstance control)
	{
		super(objectId, template, owner, control);
	}
	
	/**
	 * Field HealTrick. (value is 4717)
	 */
	private static final int HealTrick = 4717;
	/**
	 * Field GreaterHealTrick. (value is 4718)
	 */
	private static final int GreaterHealTrick = 4718;
	/**
	 * Field GreaterHeal. (value is 5195)
	 */
	private static final int GreaterHeal = 5195;
	/**
	 * Field BattleHeal. (value is 5590)
	 */
	private static final int BattleHeal = 5590;
	/**
	 * Field Recharge. (value is 5200)
	 */
	private static final int Recharge = 5200;
	
	/**
	 * @author Mobius
	 */
	class ActionTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Skill skill = onActionTask();
			_actionTask = ThreadPoolManager.getInstance().schedule(new ActionTask(), skill == null ? 1000 : ((skill.getHitTime() * 333) / Math.max(getMAtkSpd(), 1)) - 100);
		}
	}
	
	/**
	 * Method getBuffs.
	 * @return Skill[]
	 */
	public Skill[] getBuffs()
	{
		switch (getNpcId())
		{
			case PetDataTable.IMPROVED_BABY_COUGAR_ID:
				return COUGAR_BUFFS[getBuffLevel()];
			case PetDataTable.IMPROVED_BABY_BUFFALO_ID:
				return BUFFALO_BUFFS[getBuffLevel()];
			case PetDataTable.IMPROVED_BABY_KOOKABURRA_ID:
				return KOOKABURRA_BUFFS[getBuffLevel()];
			case PetDataTable.FAIRY_PRINCESS_ID:
				return FAIRY_PRINCESS_BUFFS[getBuffLevel()];
			default:
				return Skill.EMPTY_ARRAY;
		}
	}
	
	/**
	 * Method onActionTask.
	 * @return Skill
	 */
	public Skill onActionTask()
	{
		try
		{
			Player owner = getPlayer();
			if (!owner.isDead() && !owner.isInvul() && !isCastingNow())
			{
				if (getEffectList().getEffectsCountForSkill(5753) > 0)
				{
					return null;
				}
				if (getEffectList().getEffectsCountForSkill(5771) > 0)
				{
					return null;
				}
				boolean improved = PetDataTable.isImprovedBabyPet(getNpcId());
				Skill skill = null;
				if (!Config.ALT_PET_HEAL_BATTLE_ONLY || owner.isInCombat())
				{
					double curHp = owner.getCurrentHpPercents();
					if ((curHp < 90) && Rnd.chance((100 - curHp) / 3))
					{
						if (curHp < 33)
						{
							skill = SkillTable.getInstance().getInfo(improved ? BattleHeal : GreaterHealTrick, getHealLevel());
						}
						else if (getNpcId() != PetDataTable.IMPROVED_BABY_KOOKABURRA_ID)
						{
							skill = SkillTable.getInstance().getInfo(improved ? GreaterHeal : HealTrick, getHealLevel());
						}
					}
					if ((skill == null) && (getNpcId() == PetDataTable.IMPROVED_BABY_KOOKABURRA_ID))
					{
						double curMp = owner.getCurrentMpPercents();
						if ((curMp < 66) && Rnd.chance((100 - curMp) / 2))
						{
							skill = SkillTable.getInstance().getInfo(Recharge, getRechargeLevel());
						}
					}
					if ((skill != null) && skill.checkCondition(PetBabyInstance.this, owner, false, !isFollowMode(), true))
					{
						setTarget(owner);
						getAI().Cast(skill, owner, false, !isFollowMode());
						return skill;
					}
				}
				if (!improved || owner.isInOfflineMode() || (owner.getEffectList().getEffectsCountForSkill(5771) > 0))
				{
					return null;
				}
				outer:
				for (Skill buff : getBuffs())
				{
					if (getCurrentMp() < buff.getMpConsume2())
					{
						continue;
					}
					for (Effect ef : owner.getEffectList().getAllEffects())
					{
						if (checkEffect(ef, buff))
						{
							continue outer;
						}
					}
					if (buff.checkCondition(PetBabyInstance.this, owner, false, !isFollowMode(), true))
					{
						setTarget(owner);
						getAI().Cast(buff, owner, false, !isFollowMode());
						return buff;
					}
					return null;
				}
			}
		}
		catch (Throwable e)
		{
			_log.warn("Pet [#" + getNpcId() + "] a buff task error has occurred: " + e);
			_log.error("", e);
		}
		return null;
	}
	
	/**
	 * Method checkEffect.
	 * @param ef Effect
	 * @param skill Skill
	 * @return boolean
	 */
	private boolean checkEffect(Effect ef, Skill skill)
	{
		if ((ef == null) || !ef.isInUse() || !EffectList.checkStackType(ef.getTemplate(), skill.getEffectTemplates()[0]))
		{
			return false;
		}
		if (ef.getStackOrder() < skill.getEffectTemplates()[0]._stackOrder)
		{
			return false;
		}
		if (ef.getTimeLeft() > 10)
		{
			return true;
		}
		if (ef.getNext() != null)
		{
			return checkEffect(ef.getNext(), skill);
		}
		return false;
	}
	
	/**
	 * Method stopBuffTask.
	 */
	public synchronized void stopBuffTask()
	{
		if (_actionTask != null)
		{
			_actionTask.cancel(false);
			_actionTask = null;
		}
	}
	
	/**
	 * Method startBuffTask.
	 */
	public synchronized void startBuffTask()
	{
		if (_actionTask != null)
		{
			stopBuffTask();
		}
		if ((_actionTask == null) && !isDead())
		{
			_actionTask = ThreadPoolManager.getInstance().schedule(new ActionTask(), 5000);
		}
	}
	
	/**
	 * Method isBuffEnabled.
	 * @return boolean
	 */
	public boolean isBuffEnabled()
	{
		return _buffEnabled;
	}
	
	/**
	 * Method triggerBuff.
	 */
	public void triggerBuff()
	{
		_buffEnabled = !_buffEnabled;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		stopBuffTask();
		super.onDeath(killer);
	}
	
	/**
	 * Method doRevive.
	 */
	@Override
	public void doRevive()
	{
		super.doRevive();
		startBuffTask();
	}
	
	/**
	 * Method unSummon.
	 */
	@Override
	public void unSummon()
	{
		stopBuffTask();
		super.unSummon();
	}
	
	/**
	 * Method getHealLevel.
	 * @return int
	 */
	public int getHealLevel()
	{
		return Math.min(Math.max((getLevel() - getMinLevel()) / ((80 - getMinLevel()) / 12), 1), 12);
	}
	
	/**
	 * Method getRechargeLevel.
	 * @return int
	 */
	public int getRechargeLevel()
	{
		return Math.min(Math.max((getLevel() - getMinLevel()) / ((80 - getMinLevel()) / 8), 1), 8);
	}
	
	/**
	 * Method getBuffLevel.
	 * @return int
	 */
	public int getBuffLevel()
	{
		if (getNpcId() == PetDataTable.FAIRY_PRINCESS_ID)
		{
			return Math.min(Math.max((getLevel() - getMinLevel()) / ((80 - getMinLevel()) / 3), 0), 3);
		}
		return Math.min(Math.max((getLevel() - 55) / 5, 0), 3);
	}
	
	/**
	 * Method getSoulshotConsumeCount.
	 * @return int
	 */
	@Override
	public int getSoulshotConsumeCount()
	{
		return 1;
	}
	
	/**
	 * Method getSpiritshotConsumeCount.
	 * @return int
	 */
	@Override
	public int getSpiritshotConsumeCount()
	{
		return 1;
	}
	
	/**
	 * Field Pet_Haste. (value is 5186)
	 */
	private static final int Pet_Haste = 5186;
	/**
	 * Field Pet_Vampiric_Rage. (value is 5187)
	 */
	private static final int Pet_Vampiric_Rage = 5187;
	/**
	 * Field Pet_Regeneration. (value is 5188)
	 */
	@SuppressWarnings("unused")
	private static final int Pet_Regeneration = 5188;
	/**
	 * Field Pet_Blessed_Body. (value is 5189)
	 */
	private static final int Pet_Blessed_Body = 5189;
	/**
	 * Field Pet_Blessed_Soul. (value is 5190)
	 */
	private static final int Pet_Blessed_Soul = 5190;
	/**
	 * Field Pet_Guidance. (value is 5191)
	 */
	private static final int Pet_Guidance = 5191;
	/**
	 * Field Pet_Wind_Walk. (value is 5192)
	 */
	@SuppressWarnings("unused")
	private static final int Pet_Wind_Walk = 5192;
	/**
	 * Field Pet_Acumen. (value is 5193)
	 */
	private static final int Pet_Acumen = 5193;
	/**
	 * Field Pet_Empower. (value is 5194)
	 */
	private static final int Pet_Empower = 5194;
	/**
	 * Field Pet_Concentration. (value is 5201)
	 */
	private static final int Pet_Concentration = 5201;
	/**
	 * Field Pet_Might. (value is 5586)
	 */
	private static final int Pet_Might = 5586;
	/**
	 * Field Pet_Shield. (value is 5587)
	 */
	private static final int Pet_Shield = 5587;
	/**
	 * Field Pet_Focus. (value is 5588)
	 */
	private static final int Pet_Focus = 5588;
	/**
	 * Field Pet_Death_Wisper. (value is 5589)
	 */
	private static final int Pet_Death_Wisper = 5589;
	/**
	 * Field CurseGloom. (value is 5199) Field Slow. (value is 5198) Field Hex. (value is 5197) Field WindShackle. (value is 5196)
	 */
	@SuppressWarnings("unused")
	private static final int WindShackle = 5196, Hex = 5197, Slow = 5198, CurseGloom = 5199;
	/**
	 * Field COUGAR_BUFFS.
	 */
	private static final Skill[][] COUGAR_BUFFS =
	{
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Might, 3)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Haste, 2)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Haste, 2),
			SkillTable.getInstance().getInfo(Pet_Vampiric_Rage, 4),
			SkillTable.getInstance().getInfo(Pet_Focus, 3)
		}
	};
	/**
	 * Field BUFFALO_BUFFS.
	 */
	private static final Skill[][] BUFFALO_BUFFS =
	{
		{
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Guidance, 3),
		},
		{
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Guidance, 3),
			SkillTable.getInstance().getInfo(Pet_Vampiric_Rage, 4),
			SkillTable.getInstance().getInfo(Pet_Haste, 2)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Might, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Guidance, 3),
			SkillTable.getInstance().getInfo(Pet_Vampiric_Rage, 4),
			SkillTable.getInstance().getInfo(Pet_Haste, 2),
			SkillTable.getInstance().getInfo(Pet_Focus, 3),
			SkillTable.getInstance().getInfo(Pet_Death_Wisper, 3)
		}
	};
	/**
	 * Field KOOKABURRA_BUFFS.
	 */
	private static final Skill[][] KOOKABURRA_BUFFS =
	{
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Concentration, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Concentration, 6)
		}
	};
	/**
	 * Field FAIRY_PRINCESS_BUFFS.
	 */
	private static final Skill[][] FAIRY_PRINCESS_BUFFS =
	{
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Concentration, 6)
		},
		{
			SkillTable.getInstance().getInfo(Pet_Empower, 3),
			SkillTable.getInstance().getInfo(Pet_Blessed_Soul, 6),
			SkillTable.getInstance().getInfo(Pet_Blessed_Body, 6),
			SkillTable.getInstance().getInfo(Pet_Shield, 3),
			SkillTable.getInstance().getInfo(Pet_Acumen, 3),
			SkillTable.getInstance().getInfo(Pet_Concentration, 6)
		}
	};
}
