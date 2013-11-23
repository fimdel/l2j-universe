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
package l2p.gameserver.instancemanager;

import gnu.trove.TIntObjectHashMap;
import l2p.gameserver.data.xml.holder.DynamicQuestHolder;
import l2p.gameserver.model.quest.dynamic.DynamicQuest;
import l2p.gameserver.scripts.Scripts;
import l2p.gameserver.templates.DynamicQuestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DynamicQuestManager {
    private static final Logger _log = LoggerFactory.getLogger(DynamicQuestManager.class);
    protected static DynamicQuestManager _instance;
    private final TIntObjectHashMap<DynamicQuest> _activeQuests = new TIntObjectHashMap<DynamicQuest>();

    private static boolean _gmPointBonus = false;

    public static DynamicQuestManager getInstance() {
        return _instance == null ? (DynamicQuestManager._instance = new DynamicQuestManager()) : _instance;
    }

    public void init() {
        _log.info("Initializing Dynamic Quest Manager");
        for (DynamicQuestTemplate template : DynamicQuestHolder.getInstance().getAllDynamicQuests()) {
            Class clazz = Scripts.getInstance().getClasses().get("quests.dynamic." + template.getQuestPath());
            if (clazz != null) {
                try {
                    Constructor constructor = clazz.getConstructor(new Class[]
                            {
                                    DynamicQuestTemplate.class
                            });
                    if (constructor != null) {
                        addQuest((DynamicQuest) constructor.newInstance(template));
                    }
                } catch (Exception ignored) {
                }
            } else {
                addQuest(new DynamicQuest(template));
            }
        }
    }

    public DynamicQuest getQuest(int questId, int stepId) {
        return _activeQuests.get(getTaskId(questId, stepId));
    }

    public static int getTaskId(int questId, int stepId) {
        return (questId * 100) + stepId;
    }

    public static boolean hasGmBonus() {
        return _gmPointBonus;
    }

    public static int getStepId(int taskId) {
        return taskId % 100;
    }

    public DynamicQuest getQuestByTaskId(int taskId) {
        if (_activeQuests.containsKey(taskId)) {
            return _activeQuests.get(taskId);
        }
        return null;
    }

    public void addQuest(DynamicQuest quest) {
        _activeQuests.put(quest.getTemplate().getTaskId(), quest);
    }

    public static void setGmPointBonus(boolean value) {
        _gmPointBonus = value;
    }

    public DynamicQuest[] getAllQuests() {
        return _activeQuests.getValues(new DynamicQuest[_activeQuests.size()]);
    }
}
