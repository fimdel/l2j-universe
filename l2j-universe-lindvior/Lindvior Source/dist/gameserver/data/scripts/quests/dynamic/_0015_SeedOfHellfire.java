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
package quests.dynamic;

import l2p.gameserver.instancemanager.SoHManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.model.quest.dynamic.DynamicQuest;
import l2p.gameserver.templates.DynamicQuestTemplate;

/**
 * @author Mazaffaka
 */
public class _0015_SeedOfHellfire extends DynamicQuest {
    public _0015_SeedOfHellfire(DynamicQuestTemplate template) {
        super(template);
    }

    @Override
    public void onQuestEnd(Player player, QuestState qs) {
        SoHManager.setCurrentStage(2);
    }
}
