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
package l2p.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.templates.DynamicQuestTemplate;

public class DynamicQuestHolder extends AbstractHolder {
    private static final DynamicQuestHolder _instance = new DynamicQuestHolder();

    private final TIntObjectHashMap<DynamicQuestTemplate> _dynamicQuests = new TIntObjectHashMap();

    @Override
    public void clear() {
        this._dynamicQuests.clear();
    }

    public static DynamicQuestHolder getInstance() {
        return _instance;
    }

    @Override
    public int size() {
        return this._dynamicQuests.size();
    }

    public void addDynamicQuest(int questId, DynamicQuestTemplate dynamicQuest) {
        this._dynamicQuests.put(questId, dynamicQuest);
    }

    public DynamicQuestTemplate getDynamicQuest(int taskId) {
        for (DynamicQuestTemplate template : getAllDynamicQuests()) {
            if (template.getTaskId() == taskId) {
                return template;
            }
        }
        return null;
    }

    public DynamicQuestTemplate[] getAllDynamicQuests() {
        return this._dynamicQuests.values(new DynamicQuestTemplate[this._dynamicQuests.size()]);
    }
}
