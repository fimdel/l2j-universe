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
package lineage2.gameserver.templates;

import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.configuration.ExProperties;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.Zone.ZoneTarget;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZoneTemplate
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _type.
	 */
	private final ZoneType _type;
	/**
	 * Field _territory.
	 */
	private final Territory _territory;
	/**
	 * Field _isEnabled.
	 */
	private final boolean _isEnabled;
	/**
	 * Field _restartPoints.
	 */
	private final List<Location> _restartPoints;
	/**
	 * Field _PKrestartPoints.
	 */
	private final List<Location> _PKrestartPoints;
	/**
	 * Field _restartTime.
	 */
	private final long _restartTime;
	/**
	 * Field _enteringMessageId.
	 */
	private final int _enteringMessageId;
	/**
	 * Field _leavingMessageId.
	 */
	private final int _leavingMessageId;
	/**
	 * Field _affectRace.
	 */
	private final Race _affectRace;
	/**
	 * Field _target.
	 */
	private final ZoneTarget _target;
	/**
	 * Field _skill.
	 */
	private final Skill _skill;
	/**
	 * Field _skillProb.
	 */
	private final int _skillProb;
	/**
	 * Field _initialDelay.
	 */
	private final int _initialDelay;
	/**
	 * Field _unitTick.
	 */
	private final int _unitTick;
	/**
	 * Field _randomTick.
	 */
	private final int _randomTick;
	/**
	 * Field _damageMessageId.
	 */
	private final int _damageMessageId;
	/**
	 * Field _damageOnHP.
	 */
	private final int _damageOnHP;
	/**
	 * Field _damageOnMP.
	 */
	private final int _damageOnMP;
	/**
	 * Field _moveBonus.
	 */
	private final double _moveBonus;
	/**
	 * Field _regenBonusHP.
	 */
	private final double _regenBonusHP;
	/**
	 * Field _regenBonusMP.
	 */
	private final double _regenBonusMP;
	/**
	 * Field _eventId.
	 */
	private final int _eventId;
	/**
	 * Field _blockedActions.
	 */
	private final String[] _blockedActions;
	/**
	 * Field _index.
	 */
	private final int _index;
	/**
	 * Field _taxById.
	 */
	private final int _taxById;
	/**
	 * Field _params.
	 */
	private final StatsSet _params;
	/**
	 * Field _jumpingTrackId.
	 */
	private final int _jumpingTrackId;
	
	/**
	 * Constructor for ZoneTemplate.
	 * @param set StatsSet
	 */
	@SuppressWarnings("unchecked")
	public ZoneTemplate(StatsSet set)
	{
		_name = set.getString("name");
		_type = ZoneType.valueOf(set.getString("type"));
		_territory = (Territory) set.get("territory");
		_enteringMessageId = set.getInteger("entering_message_no", 0);
		_leavingMessageId = set.getInteger("leaving_message_no", 0);
		_target = ZoneTarget.valueOf(set.getString("target", "pc"));
		_affectRace = set.getString("affect_race", "all").equals("all") ? null : Race.valueOf(set.getString("affect_race"));
		String s = set.getString("skill_name", null);
		Skill skill = null;
		if (s != null)
		{
			String[] sk = s.split("[\\s,;]+");
			skill = SkillTable.getInstance().getInfo(Integer.parseInt(sk[0]), Integer.parseInt(sk[1]));
		}
		_skill = skill;
		_skillProb = set.getInteger("skill_prob", 100);
		_initialDelay = set.getInteger("initial_delay", 1);
		_unitTick = set.getInteger("unit_tick", 1);
		_randomTick = set.getInteger("random_time", 0);
		_moveBonus = set.getDouble("move_bonus", 0.);
		_regenBonusHP = set.getDouble("hp_regen_bonus", 0.);
		_regenBonusMP = set.getDouble("mp_regen_bonus", 0.);
		_damageOnHP = set.getInteger("damage_on_hp", 0);
		_damageOnMP = set.getInteger("damage_on_mp", 0);
		_damageMessageId = set.getInteger("message_no", 0);
		_eventId = set.getInteger("eventId", 0);
		_isEnabled = set.getBool("enabled", true);
		_restartPoints = (List<Location>) set.get("restart_points");
		_PKrestartPoints = (List<Location>) set.get("PKrestart_points");
		_restartTime = set.getLong("restart_time", 0L);
		s = (String) set.get("blocked_actions");
		if (s != null)
		{
			_blockedActions = s.split(ExProperties.defaultDelimiter);
		}
		else
		{
			_blockedActions = null;
		}
		_index = set.getInteger("index", 0);
		_taxById = set.getInteger("taxById", 0);
		_jumpingTrackId = set.getInteger("jumping_track", -1);
		_params = set;
	}
	
	/**
	 * Method isEnabled.
	 * @return boolean
	 */
	public boolean isEnabled()
	{
		return _isEnabled;
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
	 * Method getType.
	 * @return ZoneType
	 */
	public ZoneType getType()
	{
		return _type;
	}
	
	/**
	 * Method getTerritory.
	 * @return Territory
	 */
	public Territory getTerritory()
	{
		return _territory;
	}
	
	/**
	 * Method getEnteringMessageId.
	 * @return int
	 */
	public int getEnteringMessageId()
	{
		return _enteringMessageId;
	}
	
	/**
	 * Method getLeavingMessageId.
	 * @return int
	 */
	public int getLeavingMessageId()
	{
		return _leavingMessageId;
	}
	
	/**
	 * Method getZoneSkill.
	 * @return Skill
	 */
	public Skill getZoneSkill()
	{
		return _skill;
	}
	
	/**
	 * Method getSkillProb.
	 * @return int
	 */
	public int getSkillProb()
	{
		return _skillProb;
	}
	
	/**
	 * Method getInitialDelay.
	 * @return int
	 */
	public int getInitialDelay()
	{
		return _initialDelay;
	}
	
	/**
	 * Method getUnitTick.
	 * @return int
	 */
	public int getUnitTick()
	{
		return _unitTick;
	}
	
	/**
	 * Method getRandomTick.
	 * @return int
	 */
	public int getRandomTick()
	{
		return _randomTick;
	}
	
	/**
	 * Method getZoneTarget.
	 * @return ZoneTarget
	 */
	public ZoneTarget getZoneTarget()
	{
		return _target;
	}
	
	/**
	 * Method getAffectRace.
	 * @return Race
	 */
	public Race getAffectRace()
	{
		return _affectRace;
	}
	
	/**
	 * Method getBlockedActions.
	 * @return String[]
	 */
	public String[] getBlockedActions()
	{
		return _blockedActions;
	}
	
	/**
	 * Method getDamageMessageId.
	 * @return int
	 */
	public int getDamageMessageId()
	{
		return _damageMessageId;
	}
	
	/**
	 * Method getDamageOnHP.
	 * @return int
	 */
	public int getDamageOnHP()
	{
		return _damageOnHP;
	}
	
	/**
	 * Method getDamageOnMP.
	 * @return int
	 */
	public int getDamageOnMP()
	{
		return _damageOnMP;
	}
	
	/**
	 * Method getMoveBonus.
	 * @return double
	 */
	public double getMoveBonus()
	{
		return _moveBonus;
	}
	
	/**
	 * Method getRegenBonusHP.
	 * @return double
	 */
	public double getRegenBonusHP()
	{
		return _regenBonusHP;
	}
	
	/**
	 * Method getRegenBonusMP.
	 * @return double
	 */
	public double getRegenBonusMP()
	{
		return _regenBonusMP;
	}
	
	/**
	 * Method getRestartTime.
	 * @return long
	 */
	public long getRestartTime()
	{
		return _restartTime;
	}
	
	/**
	 * Method getRestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getRestartPoints()
	{
		return _restartPoints;
	}
	
	/**
	 * Method getPKRestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getPKRestartPoints()
	{
		return _PKrestartPoints;
	}
	
	/**
	 * Method getIndex.
	 * @return int
	 */
	public int getIndex()
	{
		return _index;
	}
	
	/**
	 * Method getTaxById.
	 * @return int
	 */
	public int getTaxById()
	{
		return _taxById;
	}
	
	/**
	 * Method getEventId.
	 * @return int
	 */
	public int getEventId()
	{
		return _eventId;
	}
	
	/**
	 * Method getJumpTrackId.
	 * @return int
	 */
	public int getJumpTrackId()
	{
		return _jumpingTrackId;
	}
	
	/**
	 * Method getParams.
	 * @return MultiValueSet<String>
	 */
	public MultiValueSet<String> getParams()
	{
		return _params.clone();
	}
}
