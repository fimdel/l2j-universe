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
package events.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ArenaTemplate extends Functions
{
	/**
	 * Field _managerId.
	 */
	protected int _managerId;
	/**
	 * Field _className.
	 */
	protected String _className;
	/**
	 * Field _chatName.
	 */
	protected String _chatName;
	/**
	 * Field _creatorId.
	 */
	protected Long _creatorId;
	/**
	 * Field _status.
	 */
	protected int _status = 0;
	/**
	 * Field _battleType.
	 */
	protected int _battleType = 1;
	/**
	 * Field _team1exp.
	 */
	protected int _team1exp = 0;
	/**
	 * Field _team2exp.
	 */
	protected int _team2exp = 0;
	/**
	 * Field _price.
	 */
	protected int _price = 10000;
	/**
	 * Field _team1count.
	 */
	protected int _team1count = 1;
	/**
	 * Field _team2count.
	 */
	protected int _team2count = 1;
	/**
	 * Field _team1min.
	 */
	protected int _team1min = 1;
	/**
	 * Field _team1max.
	 */
	protected int _team1max = 85;
	/**
	 * Field _team2min.
	 */
	protected int _team2min = 1;
	/**
	 * Field _team2max.
	 */
	protected int _team2max = 85;
	/**
	 * Field _timeToStart.
	 */
	protected int _timeToStart = 10;
	/**
	 * Field _timeOutTask.
	 */
	protected boolean _timeOutTask;
	/**
	 * Field _team1points.
	 */
	protected List<Location> _team1points;
	/**
	 * Field _team2points.
	 */
	protected List<Location> _team2points;
	/**
	 * Field _team1list.
	 */
	protected List<Long> _team1list;
	/**
	 * Field _team2list.
	 */
	protected List<Long> _team2list;
	/**
	 * Field _team1live.
	 */
	protected List<Long> _team1live;
	/**
	 * Field _team2live.
	 */
	protected List<Long> _team2live;
	/**
	 * Field _expToReturn.
	 */
	protected Map<Integer, Integer> _expToReturn;
	/**
	 * Field _classToReturn.
	 */
	protected Map<Integer, Integer> _classToReturn;
	/**
	 * Field _zone.
	 */
	protected Zone _zone;
	/**
	 * Field _zoneListener.
	 */
	protected ZoneListener _zoneListener;
	
	/**
	 * Method onLoad.
	 */
	protected abstract void onLoad();
	
	/**
	 * Method onReload.
	 */
	protected abstract void onReload();
	
	/**
	 * Method template_stop.
	 */
	public void template_stop()
	{
		say("Бой прерван по техниче�?ким причинам, �?тавки возвращены");
		if (_battleType == 1)
		{
			returnAdenaToTeams();
		}
		else if (_battleType == 2)
		{
			returnExpToTeams();
		}
		unParalyzeTeams();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}
	
	/**
	 * Method template_create1.
	 * @param player Player
	 */
	public void template_create1(Player player)
	{
		if (_status > 0)
		{
			show("Дождите�?�? окончани�? бо�?", player);
		}
		else
		{
			show("scripts/events/arena/" + _managerId + "-1.htm", player);
		}
	}
	
	/**
	 * Method template_create2.
	 * @param player Player
	 */
	public void template_create2(Player player)
	{
		if (_status > 0)
		{
			show("Дождите�?�? окончани�? бо�?", player);
		}
		else
		{
			show("scripts/events/arena/" + _managerId + "-2.htm", player);
		}
	}
	
	/**
	 * Method template_register.
	 * @param player Player
	 */
	public void template_register(Player player)
	{
		if (_status > 1)
		{
			show("Дождите�?�? окончани�? бо�?", player);
		}
		else
		{
			show("scripts/events/arena/" + _managerId + "-3.htm", player);
		}
	}
	
	/**
	 * Method template_check1.
	 * @param player Player
	 * @param var String[]
	 */
	public void template_check1(Player player, String[] var)
	{
		if (player.isDead())
		{
			return;
		}
		if (var.length != 8)
		{
			show("�?екорректные данные", player);
			return;
		}
		if (_status > 0)
		{
			show("Дождите�?�? окончани�? бо�?", player);
			return;
		}
		try
		{
			_price = Integer.valueOf(var[0]);
			_team1count = Integer.valueOf(var[1]);
			_team2count = Integer.valueOf(var[2]);
			_team1min = Integer.valueOf(var[3]);
			_team1max = Integer.valueOf(var[4]);
			_team2min = Integer.valueOf(var[5]);
			_team2max = Integer.valueOf(var[6]);
			_timeToStart = Integer.valueOf(var[7]);
		}
		catch (Exception e)
		{
			show("�?екорректные данные", player);
			return;
		}
		if ((_price < 10000) || (_price > 100000000))
		{
			show("�?еправил�?на�? �?тавка", player);
			return;
		}
		if ((_team1count < 1) || (_team1count > 5) || (_team2count < 1) || (_team2count > 5))
		{
			show("�?еправил�?ный размер команды", player);
			return;
		}
		if ((_team1min < 1) || (_team1min > 85) || (_team2min < 1) || (_team2min > 85) || (_team1max < 1) || (_team1max > 85) || (_team2max < 1) || (_team2max > 85) || (_team1min > _team1max) || (_team2min > _team2max))
		{
			show("�?еправил�?ный уровен�?", player);
			return;
		}
		if ((player.getLevel() < _team1min) || (player.getLevel() > _team1max))
		{
			show("�?еправил�?ный уровен�?", player);
			return;
		}
		if ((_timeToStart < 1) || (_timeToStart > 10))
		{
			show("�?еправил�?ное врем�?", player);
			return;
		}
		if (player.getAdena() < _price)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		_battleType = 1;
		_creatorId = player.getStoredId();
		player.reduceAdena(_price, true);
		_status = 1;
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
		_team1list.add(player.getStoredId());
		say(player.getName() + " �?оздал бой " + _team1count + "х" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, �?тавка " + _price + "а, начало через " + _timeToStart + " мин");
		executeTask("events.arena." + _className, "announce", new Object[0], 60000);
	}
	
	/**
	 * Method template_check2.
	 * @param player Player
	 * @param var String[]
	 */
	public void template_check2(Player player, String[] var)
	{
		if (!Config.ALT_ARENA_EXP)
		{
			show("Эта опци�? недо�?тупна", player);
			return;
		}
		if (player.isDead())
		{
			return;
		}
		if (var.length != 7)
		{
			show("�?екорректные данные", player);
			return;
		}
		if (_status > 0)
		{
			show("Дождите�?�? окончани�? бо�?", player);
			return;
		}
		try
		{
			_team1count = Integer.valueOf(var[0]);
			_team2count = Integer.valueOf(var[1]);
			_team1min = Integer.valueOf(var[2]);
			_team1max = Integer.valueOf(var[3]);
			_team2min = Integer.valueOf(var[4]);
			_team2max = Integer.valueOf(var[5]);
			_timeToStart = Integer.valueOf(var[6]);
		}
		catch (Exception e)
		{
			show("�?екорректные данные", player);
			return;
		}
		if ((_team1count < 1) || (_team1count > 5) || (_team2count < 1) || (_team2count > 5))
		{
			show("�?еправил�?ный размер команды", player);
			return;
		}
		if ((_team1min < 1) || (_team1min > 82) || (_team2min < 1) || (_team2min > 82) || (_team1max < 1) || (_team1max > 82) || (_team2max < 1) || (_team2max > 82) || (_team1min > _team1max) || (_team2min > _team2max))
		{
			show("�?еправил�?ный уровен�?", player);
			return;
		}
		if (((player.getLevel() - _team1min) > 10) || ((_team1max - player.getLevel()) > 10) || ((player.getLevel() - _team2min) > 10) || ((_team2max - player.getLevel()) > 10))
		{
			show("Разница в уровн�?х не может быт�? более 10", player);
			return;
		}
		if ((player.getLevel() < _team1min) || (player.getLevel() > _team1max))
		{
			show("�?еправил�?ный уровен�?", player);
			return;
		}
		if ((_timeToStart < 1) || (_timeToStart > 10))
		{
			show("�?еправил�?ное врем�?", player);
			return;
		}
		_battleType = 2;
		_creatorId = player.getStoredId();
		_team1exp = 0;
		_team2exp = 0;
		_expToReturn.clear();
		_classToReturn.clear();
		removeExp(player, 1);
		_status = 1;
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
		_team1list.add(player.getStoredId());
		say(player.getName() + " �?оздал бой " + _team1count + "х" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, �?тавка " + "опыт, начало через " + _timeToStart + " мин");
		executeTask("events.arena." + _className, "announce", new Object[0], 60000);
	}
	
	/**
	 * Method template_register_check.
	 * @param player Player
	 * @param var String[]
	 */
	public void template_register_check(Player player, String[] var)
	{
		if (player.isDead())
		{
			return;
		}
		if (_status > 1)
		{
			show("Дождите�?�? окончани�? бо�?", player);
			return;
		}
		if (var.length != 1)
		{
			show("�?екорректные данные", player);
			return;
		}
		int _regTeam;
		try
		{
			_regTeam = Integer.valueOf(var[0]);
		}
		catch (Exception e)
		{
			show("�?екорректные данные", player);
			return;
		}
		if ((_regTeam != 1) && (_regTeam != 2))
		{
			show("�?еправил�?ный номер команды, введите 1 или 2", player);
			return;
		}
		if (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId()))
		{
			show("Вы уже зареги�?трированы", player);
			return;
		}
		if ((_battleType == 1) && (player.getAdena() < _price))
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		if (_regTeam == 1)
		{
			if ((player.getLevel() < _team1min) || (player.getLevel() > _team1max))
			{
				show("Вы не подходите по уровн�?", player);
				return;
			}
			if (_team1list.size() >= _team1count)
			{
				show("�?оманда 1 переполнена", player);
				return;
			}
			if (_battleType == 1)
			{
				player.reduceAdena(_price, true);
			}
			else if (_battleType == 2)
			{
				removeExp(player, 1);
			}
			_team1list.add(player.getStoredId());
			say(player.getName() + " зареги�?трировал�?�? за 1 команду");
			if ((_team1list.size() >= _team1count) && (_team2list.size() >= _team2count))
			{
				say("�?оманды готовы, �?тарт через 1 минуту.");
				_timeToStart = 1;
			}
		}
		else
		{
			if ((player.getLevel() < _team2min) || (player.getLevel() > _team2max))
			{
				show("Вы не подходите по уровн�?", player);
				return;
			}
			if (_team2list.size() >= _team2count)
			{
				show("�?оманда 2 переполнена", player);
				return;
			}
			if (_battleType == 1)
			{
				player.reduceAdena(_price, true);
			}
			else if (_battleType == 2)
			{
				removeExp(player, 2);
			}
			_team2list.add(player.getStoredId());
			say(player.getName() + " зареги�?трировал�?�? за 2 команду");
			if ((_team1list.size() >= _team1count) && (_team2list.size() >= _team2count))
			{
				say("�?оманды готовы, �?тарт через 1 минуту.");
				_timeToStart = 1;
			}
		}
	}
	
	/**
	 * Method template_announce.
	 */
	public void template_announce()
	{
		Player creator = GameObjectsStorage.getAsPlayer(_creatorId);
		if ((_status != 1) || (creator == null))
		{
			return;
		}
		if (_timeToStart > 1)
		{
			_timeToStart--;
			say(creator.getName() + " �?оздал бой " + _team1count + "х" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, �?тавка " + ((_battleType == 1) ? _price + "а" : "опыт") + ", начало через " + _timeToStart + " мин");
			executeTask("events.arena." + _className, "announce", new Object[0], 60000);
		}
		else if (_team2list.size() > 0)
		{
			say("�?одготовка к бо�?");
			executeTask("events.arena." + _className, "prepare", new Object[0], 5000);
		}
		else
		{
			say("Бой не �?о�?то�?л�?�?, нет противников");
			_status = 0;
			if (_battleType == 1)
			{
				returnAdenaToTeams();
			}
			else if (_battleType == 2)
			{
				returnExpToTeams();
			}
			clearTeams();
		}
	}
	
	/**
	 * Method template_prepare.
	 */
	public void template_prepare()
	{
		if (_status != 1)
		{
			return;
		}
		_status = 2;
		for (Player player : getPlayers(_team1list))
		{
			if (!player.isDead())
			{
				_team1live.add(player.getStoredId());
			}
		}
		for (Player player : getPlayers(_team2list))
		{
			if (!player.isDead())
			{
				_team2live.add(player.getStoredId());
			}
		}
		if (!checkTeams())
		{
			return;
		}
		clearArena();
		paralyzeTeams();
		teleportTeamsToArena();
		say("Бой начнет�?�? через 15 �?екунд");
		executeTask("events.arena." + _className, "start", new Object[0], 15000);
	}
	
	/**
	 * Method template_start.
	 */
	public void template_start()
	{
		if (_status != 2)
		{
			return;
		}
		if (!checkTeams())
		{
			return;
		}
		say("Go!!!");
		unParalyzeTeams();
		_status = 3;
		executeTask("events.arena." + _className, "timeOut", new Object[0], 180000);
		_timeOutTask = true;
	}
	
	/**
	 * Method clearArena.
	 */
	public void clearArena()
	{
		for (Creature cha : _zone.getObjects())
		{
			if (cha.isPlayable())
			{
				cha.teleToLocation(_zone.getSpawn());
			}
		}
	}
	
	/**
	 * Method checkTeams.
	 * @return boolean
	 */
	public boolean checkTeams()
	{
		if (_team1live.isEmpty())
		{
			teamHasLost(1);
			return false;
		}
		else if (_team2live.isEmpty())
		{
			teamHasLost(2);
			return false;
		}
		return true;
	}
	
	/**
	 * Method paralyzeTeams.
	 */
	public void paralyzeTeams()
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		for (Player player : getPlayers(_team1live))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
		}
		for (Player player : getPlayers(_team2live))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
		}
	}
	
	/**
	 * Method unParalyzeTeams.
	 */
	public void unParalyzeTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
		}
		for (Player player : getPlayers(_team2list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
		}
	}
	
	/**
	 * Method teleportTeamsToArena.
	 */
	public void teleportTeamsToArena()
	{
		Integer n = 0;
		for (Player player : getPlayers(_team1live))
		{
			player.teleToLocation(_team1points.get(n));
			player.setTeam(TeamType.BLUE);
			n++;
		}
		n = 0;
		for (Player player : getPlayers(_team2live))
		{
			player.teleToLocation(_team2points.get(n));
			player.setTeam(TeamType.RED);
			n++;
		}
	}
	
	/**
	 * Method playerHasLost.
	 * @param player Player
	 * @return boolean
	 */
	public boolean playerHasLost(Player player)
	{
		_team1live.remove(player.getStoredId());
		_team2live.remove(player.getStoredId());
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
		revengeSkill.getEffects(player, player, false, false);
		return !checkTeams();
	}
	
	/**
	 * Method teamHasLost.
	 * @param team_id Integer
	 */
	public void teamHasLost(Integer team_id)
	{
		if (team_id == 1)
		{
			say("�?оманда 2 победила");
			if (_battleType == 1)
			{
				payAdenaToTeam(2);
			}
			else if (_battleType == 2)
			{
				payExpToTeam(2);
			}
		}
		else
		{
			say("�?оманда 1 победила");
			if (_battleType == 1)
			{
				payAdenaToTeam(1);
			}
			else if (_battleType == 2)
			{
				payExpToTeam(1);
			}
		}
		unParalyzeTeams();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}
	
	/**
	 * Method template_timeOut.
	 */
	public void template_timeOut()
	{
		if (_timeOutTask && (_status == 3))
		{
			say("Врем�? и�?текло, нич�?�?!");
			if (_battleType == 1)
			{
				returnAdenaToTeams();
			}
			else if (_battleType == 2)
			{
				returnExpToTeams();
			}
			unParalyzeTeams();
			clearTeams();
			_status = 0;
			_timeOutTask = false;
		}
	}
	
	/**
	 * Method payAdenaToTeam.
	 * @param team_id Integer
	 */
	public void payAdenaToTeam(Integer team_id)
	{
		if (team_id == 1)
		{
			for (Player player : getPlayers(_team1list))
			{
				player.addAdena(_price + ((_team2list.size() * _price) / _team1list.size()));
			}
		}
		else
		{
			for (Player player : getPlayers(_team2list))
			{
				player.addAdena(_price + ((_team1list.size() * _price) / _team2list.size()));
			}
		}
	}
	
	/**
	 * Method payExpToTeam.
	 * @param team_id Integer
	 */
	public void payExpToTeam(Integer team_id)
	{
		if (team_id == 1)
		{
			for (Player player : getPlayers(_team1list))
			{
				returnExp(player);
				addExp(player, _team2exp / _team1list.size() / 2);
			}
		}
		else
		{
			for (Player player : getPlayers(_team2list))
			{
				returnExp(player);
				addExp(player, _team1exp / _team2list.size() / 2);
			}
		}
	}
	
	/**
	 * Method returnAdenaToTeams.
	 */
	public void returnAdenaToTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.addAdena(_price);
		}
		for (Player player : getPlayers(_team2list))
		{
			player.addAdena(_price);
		}
	}
	
	/**
	 * Method returnExpToTeams.
	 */
	public void returnExpToTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			returnExp(player);
		}
		for (Player player : getPlayers(_team2list))
		{
			returnExp(player);
		}
	}
	
	/**
	 * Method clearTeams.
	 */
	public void clearTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.setTeam(TeamType.NONE);
		}
		for (Player player : getPlayers(_team2list))
		{
			player.setTeam(TeamType.NONE);
		}
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
	}
	
	/**
	 * Method removeExp.
	 * @param player Player
	 * @param team int
	 */
	public void removeExp(Player player, int team)
	{
		int lostExp = Math.round(((Experience.LEVEL[player.getLevel() + 1] - Experience.LEVEL[player.getLevel()]) * 4) / 100);
		player.addExpAndSp(-1 * lostExp, 0);
		_expToReturn.put(player.getObjectId(), lostExp);
		_classToReturn.put(player.getObjectId(), player.getActiveClassId());
		if (team == 1)
		{
			_team1exp += lostExp;
		}
		else if (team == 2)
		{
			_team2exp += lostExp;
		}
	}
	
	/**
	 * Method returnExp.
	 * @param player Player
	 */
	public void returnExp(Player player)
	{
		int addExp = _expToReturn.get(player.getObjectId());
		int classId = _classToReturn.get(player.getObjectId());
		if ((addExp > 0) && (player.getActiveClassId() == classId))
		{
			player.addExpAndSp(addExp, 0);
		}
	}
	
	/**
	 * Method addExp.
	 * @param player Player
	 * @param exp int
	 */
	public void addExp(Player player, int exp)
	{
		int classId = _classToReturn.get(player.getObjectId());
		if (player.getActiveClassId() == classId)
		{
			player.addExpAndSp(exp, 0);
		}
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 */
	protected void onDeath(Creature self, Creature killer)
	{
		if ((_status >= 2) && self.isPlayer() && (_team1list.contains(self.getStoredId()) || _team2list.contains(self.getStoredId())))
		{
			Player player = self.getPlayer();
			Player kplayer = killer.getPlayer();
			if (kplayer != null)
			{
				say(kplayer.getName() + " убил " + player.getName());
				if ((player.getTeam() == kplayer.getTeam()) || (!_team1list.contains(kplayer.getStoredId()) && !_team2list.contains(kplayer.getStoredId())))
				{
					say("�?ару�?ение правил, игрок " + kplayer.getName() + " о�?трафован на " + _price);
					kplayer.reduceAdena(_price, true);
				}
				playerHasLost(player);
			}
			else
			{
				say(player.getName() + " убит");
				playerHasLost(player);
			}
		}
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 */
	protected void onPlayerExit(Player player)
	{
		if ((player != null) && (_status > 0) && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
		{
			switch (_status)
			{
				case 1:
					removePlayer(player);
					say(player.getName() + " ди�?квалифицирован");
					if (player.getStoredId() == _creatorId)
					{
						say("Бой прерван, �?тавки возвращены");
						if (_battleType == 1)
						{
							returnAdenaToTeams();
						}
						else if (_battleType == 2)
						{
							returnExpToTeams();
						}
						unParalyzeTeams();
						clearTeams();
						_status = 0;
						_timeOutTask = false;
					}
					break;
				case 2:
					removePlayer(player);
					say(player.getName() + " ди�?квалифицирован");
					checkTeams();
					break;
				case 3:
					removePlayer(player);
					say(player.getName() + " ди�?квалифицирован");
					checkTeams();
					break;
			}
		}
	}
	
	/**
	 * Method onTeleport.
	 * @param player Player
	 */
	protected void onTeleport(Player player)
	{
		if ((_status > 1) && player.isInZone(_zone))
		{
			onPlayerExit(player);
		}
	}
	
	/**
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((_status >= 2) && (player != null) && !(_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
			{
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, _zone.getSpawn()), 3000);
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((_status >= 2) && (player != null) && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
			{
				double angle = PositionUtils.convertHeadingToDegree(cha.getHeading());
				double radian = Math.toRadians(angle - 90);
				int x = (int) (cha.getX() + (50 * Math.sin(radian)));
				int y = (int) (cha.getY() - (50 * Math.cos(radian)));
				int z = cha.getZ();
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(x, y, z)), 3000);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class TeleportTask extends RunnableImpl
	{
		/**
		 * Field loc.
		 */
		Location loc;
		/**
		 * Field target.
		 */
		Creature target;
		
		/**
		 * Constructor for TeleportTask.
		 * @param target Creature
		 * @param loc Location
		 */
		public TeleportTask(Creature target, Location loc)
		{
			this.target = target;
			this.loc = loc;
			target.block();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			target.unblock();
			target.teleToLocation(loc);
		}
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 */
	private void removePlayer(Player player)
	{
		if (player != null)
		{
			_team1list.remove(player.getStoredId());
			_team2list.remove(player.getStoredId());
			_team1live.remove(player.getStoredId());
			_team2live.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
		}
	}
	
	/**
	 * Method getPlayers.
	 * @param list List<Long>
	 * @return List<Player>
	 */
	private List<Player> getPlayers(List<Long> list)
	{
		List<Player> result = new ArrayList<>();
		for (Long storeId : list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				result.add(player);
			}
		}
		return result;
	}
	
	/**
	 * Method say.
	 * @param text String
	 */
	public void say(String text)
	{
		Say2 cs = new Say2(0, ChatType.SHOUT, "Arena", text);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (!player.isBlockAll() && player.isInRange(_zone.getSpawn(), 4000))
			{
				player.sendPacket(cs);
			}
		}
	}
}
