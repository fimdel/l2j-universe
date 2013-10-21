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

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeathPenalty
{
	/**
	 * Field _skillId. (value is 14571)
	 */
	private static final int _skillId = 14571;
	/**
	 * Field _fortuneOfNobleseSkillId. (value is 1325)
	 */
	private static final int _fortuneOfNobleseSkillId = 1325;
	/**
	 * Field _charmOfLuckSkillId. (value is 2168)
	 */
	private static final int _charmOfLuckSkillId = 2168;
	/**
	 * Field _playerRef.
	 */
	private final HardReference<Player> _playerRef;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _hasCharmOfLuck.
	 */
	private boolean _hasCharmOfLuck;
	
	/**
	 * Constructor for DeathPenalty.
	 * @param player Player
	 * @param level int
	 */
	public DeathPenalty(Player player, int level)
	{
		_playerRef = player.getRef();
		_level = level;
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _playerRef.get();
	}
	
	/**
	 * Method getLevel.
	 * @param player Player
	 * @return int
	 */
	public int getLevel(Player player)
	{
		if (_level > 5)
		{
			_level = 5;
		}
		if (_level < 0)
		{
			_level = 0;
		}
		return Config.ALLOW_DEATH_PENALTY_C5 ? _level : 0;
	}
	
	/**
	 * Method getLevelOnSaveDB.
	 * @param player Player
	 * @return int
	 */
	public int getLevelOnSaveDB(Player player)
	{
		if ((player.getEffectList().getEffectsBySkillId(_skillId) != null) && (_level != 0))
		{
			if (_level > 5)
			{
				_level = 5;
			}
			if (_level < 0)
			{
				_level = 0;
			}
			return _level;
		}
		return 0;
	}
	
	/**
	 * Method notifyDead.
	 * @param killer Creature
	 */
	public void notifyDead(Creature killer)
	{
		if (!Config.ALLOW_DEATH_PENALTY_C5)
		{
			return;
		}
		if (_hasCharmOfLuck)
		{
			_hasCharmOfLuck = false;
			return;
		}
		if ((killer == null) || killer.isPlayable())
		{
			return;
		}
		Player player = getPlayer();
		if ((player == null) || (player.getLevel() <= 9))
		{
			return;
		}
		int karmaBonus = player.getKarma() / Config.ALT_DEATH_PENALTY_C5_KARMA_PENALTY;
		if (karmaBonus < 0)
		{
			karmaBonus = 0;
		}
		if (Rnd.chance(Config.ALT_DEATH_PENALTY_C5_CHANCE + karmaBonus))
		{
			addLevel();
		}
	}
	
	/**
	 * Method restore.
	 * @param player Player
	 */
	public void restore(Player player)
	{
		if (player.getEffectList().getEffectsBySkillId(_skillId) != null)
		{
			for (Effect e : player.getEffectList().getAllEffects())
			{
				if (e.getSkill().getId() == _skillId)
				{
					_level = e.getSkill().getLevel();
					e.exit();
				}
			}
		}
		if (!Config.ALLOW_DEATH_PENALTY_C5)
		{
			return;
		}
		if (getLevel(player) > 0)
		{
			Skill skill = SkillTable.getInstance().getInfo(_skillId, _level);
			if (skill == null)
			{
				return;
			}
			skill.getEffects(player, player, false, false);
			player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel(player)));
		}
		player.updateStats();
	}
	
	/**
	 * Method addLevel.
	 */
	public void addLevel()
	{
		Player player = getPlayer();
		if ((player == null) || (getLevel(player) >= 5))
		{
			return;
		}
		if (player.getEffectList().getEffectsBySkillId(_skillId) != null)
		{
			for (Effect e : player.getEffectList().getAllEffects())
			{
				if (e.getSkill().getId() == _skillId)
				{
					_level = e.getSkill().getLevel();
					e.exit();
				}
			}
		}
		_level++;
	}
	
	/**
	 * Method castEffect.
	 * @param player Player
	 */
	public void castEffect(Player player)
	{
		if (getLevel(player) > 0)
		{
			Skill skill = SkillTable.getInstance().getInfo(_skillId, getLevel(player));
			if (skill == null)
			{
				return;
			}
			skill.getEffects(player, player, false, false);
			player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel(player)));
			player.updateStats();
		}
	}
	
	/**
	 * Method reduceLevel.
	 */
	public void reduceLevel()
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		for (Effect e : player.getEffectList().getAllEffects())
		{
			if (e.getSkill().getId() == _skillId)
			{
				_level = e.getSkill().getLevel();
				e.exit();
			}
			else
			{
				return;
			}
		}
		_level--;
		if (getLevel(player) > 0)
		{
			player.broadcastPacket(new MagicSkillUse(player, player, _skillId, getLevel(player), 0, 0));
			player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel(player)));
		}
		else
		{
			player.sendPacket(Msg.THE_DEATH_PENALTY_HAS_BEEN_LIFTED);
		}
		player.updateStats();
	}
	
	/**
	 * Method checkCharmOfLuck.
	 */
	public void checkCharmOfLuck()
	{
		Player player = getPlayer();
		if (player != null)
		{
			for (Effect e : player.getEffectList().getAllEffects())
			{
				if ((e.getSkill().getId() == _charmOfLuckSkillId) || (e.getSkill().getId() == _fortuneOfNobleseSkillId))
				{
					_hasCharmOfLuck = true;
					return;
				}
			}
		}
		_hasCharmOfLuck = false;
	}
}
