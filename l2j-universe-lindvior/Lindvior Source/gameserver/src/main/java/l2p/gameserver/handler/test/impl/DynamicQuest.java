/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test.impl;

import l2p.gameserver.instancemanager.DynamicQuestManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;

import java.util.Map;

public class DynamicQuest extends BypassHandler {
    public String[] getBypassList() {
        return new String[]
                {
                        "dynamic_quest_accept", "dynamic_quest_reject", "dynamic_quest_reward", "dynamic_quest_ranking"
                };
    }

    public void onBypassCommand(String command, Player activeChar, Creature target) {
        try {
            Map<String, String> params = parseBypass(command);

            if (params.size() < 3) {
                return;
            }
            String action = params.get("main");
            int questId = Integer.parseInt(params.get("dquest_id"));
            int stepId = Integer.parseInt(params.get("step_id"));

            l2p.gameserver.model.quest.dynamic.DynamicQuest quest = DynamicQuestManager.getInstance().getQuest(questId, stepId);

            if (quest == null) {
                return;
            }
            //	_log.warn("DynamicQuest: " + action);

            switch (action) {
                case "dynamic_quest_accept":
                    quest.addParticipiant(activeChar);
                    break;
                case "dynamic_quest_reject":
                    quest.showDialog(activeChar, "reject");
                    break;
                case "dynamic_quest_ranking":
                    if (quest.isParticipiant(activeChar)) {
                        quest.sendResults(activeChar);
                        quest.sendRewardInfo(activeChar);
                    }
                    break;
                case "dynamic_quest_reward":
                    quest.giveReward(activeChar);
            }

        } catch (Exception e) {
            _log.error("Exception in " + getClass().getSimpleName());
        }
    }
}
