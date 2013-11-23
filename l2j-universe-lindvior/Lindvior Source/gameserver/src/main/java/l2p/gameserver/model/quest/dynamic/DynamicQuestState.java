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
package l2p.gameserver.model.quest.dynamic;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class DynamicQuestState extends QuestState {
    public DynamicQuestState(Quest quest, Player player) {
        super(quest, player, 0);
    }

    public DynamicQuestState(Quest quest, Player player, byte state) {
        super(quest, player, state);
    }

    @Override
    public Object setState(int state) {
        return _state = state;
    }

    @Override
    public void startQuest() {
        playSound(Quest.SOUND_ACCEPT);
        setState(Quest.STARTED);
        setCond(1);
    }
}
