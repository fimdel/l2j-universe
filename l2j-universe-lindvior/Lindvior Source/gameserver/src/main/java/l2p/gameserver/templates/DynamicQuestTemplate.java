/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2p.gameserver.templates;

import l2p.gameserver.data.xml.holder.StartItemHolder;
import l2p.gameserver.model.Zone;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DynamicQuestTemplate {
    private final int _questId;
    private final int _taskId;
    private final String _questName;
    private final String _questTitle;
    private final String _questPath;
    private final String _spawnGroup;
    private final int _duration;
    private final int _points;
    private final int _nextTaskId;
    private final int _minLevel;
    private final boolean _autostart;
    private final List<Zone> _zones;
    private final Map<String, String> _dialogs;
    private final Map<Integer, Integer> _killPoints;
    private final List<StartItemHolder.StartItem> _rewards;
    private final List<StartItemHolder.StartItem> _eliteRewards;
    private final List<DynamicQuestDate> _startDates;

    public DynamicQuestTemplate(int questId, int taskId, String questName, String questPath, String questTitle, int duration, int minLevel, int points, int nextTaskId, boolean autostart, String spawnGroup, List<Zone> zones, List<DynamicQuestDate> dates, Map<Integer, Integer> killPoints,
                                List<StartItemHolder.StartItem> rewads, List<StartItemHolder.StartItem> eliteRewads, Map<String, String> dialogs) {
        _questId = questId;
        _taskId = taskId;
        _questName = questName;
        _questPath = questPath;
        _questTitle = questTitle;
        _duration = duration;
        _points = points;
        _minLevel = minLevel;
        _nextTaskId = nextTaskId;
        _autostart = autostart;
        _spawnGroup = spawnGroup;
        _zones = zones;
        _killPoints = killPoints;
        _rewards = rewads;
        _eliteRewards = eliteRewads;
        _dialogs = dialogs;
        _startDates = dates;
    }

    public int getPoints() {
        return _points;
    }

    public int getQuestId() {
        return _questId;
    }

    public int getTaskId() {
        return _taskId;
    }

    public String getQuestName() {
        return _questName;
    }

    public Map<Integer, Integer> getAllPoints() {
        return _killPoints;
    }

    public boolean isCampain() {
        return (_zones.isEmpty()) && (!_startDates.isEmpty());
    }

    public boolean isZoneQuest() {
        return (!_zones.isEmpty()) && (_startDates.isEmpty());
    }

    public boolean isAutostart() {
        return _autostart;
    }

    public List<Zone> getAllZones() {
        return _zones;
    }

    public int getKillPoint(int npcId) {
        return _killPoints.containsKey(Integer.valueOf(npcId)) ? _killPoints.get(Integer.valueOf(npcId)).intValue() : 0;
    }

    public String getQuestPath() {
        return _questPath;
    }

    public List<DynamicQuestDate> getStartDates() {
        return _startDates;
    }

    public String getSpawnGroup() {
        return _spawnGroup;
    }

    public int getNextTaskId() {
        return _nextTaskId;
    }

    public List<StartItemHolder.StartItem> getAllRewards() {
        return _rewards;
    }

    public List<StartItemHolder.StartItem> getAllEliteRewards() {
        return _eliteRewards;
    }

    public String getQuestTitle() {
        return _questTitle;
    }

    public int getMinLevel() {
        return _minLevel;
    }

    public String getDialog(String type) {
        return _dialogs.get(type);
    }

    public int getDuration() {
        return _duration;
    }

    public static class DynamicQuestDate {
        private final String _dayOfWeek;
        private final int _hour;
        private final int _minute;

        public DynamicQuestDate(String dayOfWeek, int hours, int minutes) {
            _dayOfWeek = dayOfWeek.toLowerCase();
            _hour = hours;
            _minute = minutes;
        }

        public int getMinutes() {
            return _minute;
        }

        public String getDayOfWeek() {
            return _dayOfWeek;
        }

        public long getNextScheduleTime() {
            int dayOfWeekNumber;
            switch (_dayOfWeek) {
                case "monday":
                case "mon":
                    dayOfWeekNumber = 2;
                    break;
                case "tuesday":
                case "tue":
                    dayOfWeekNumber = 3;
                    break;
                case "wednesday":
                case "wed":
                    dayOfWeekNumber = 4;
                    break;
                case "thursday":
                case "thu":
                    dayOfWeekNumber = 5;
                    break;
                case "friday":
                case "fri":
                    dayOfWeekNumber = 6;
                    break;
                case "saturday":
                case "sat":
                    dayOfWeekNumber = 7;
                    break;
                case "sunday":
                case "sun":
                    dayOfWeekNumber = 1;
                    break;
                default:
                    return 0L;
            }

            Calendar current = Calendar.getInstance();
            current.set(10, _hour);
            current.set(12, _minute);

            int dayDiff = dayOfWeekNumber - current.get(7);

            if (dayDiff > 0) {
                current.add(5, dayDiff);
            } else if (dayDiff < 0) {
                current.add(5, 7 - Math.abs(dayDiff));
            }

            return current.getTimeInMillis();
        }

        public int getHour() {
            return _hour;
        }
    }
}
