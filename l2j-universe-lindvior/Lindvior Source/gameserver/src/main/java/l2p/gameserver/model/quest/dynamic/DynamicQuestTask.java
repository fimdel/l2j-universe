/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest.dynamic;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.instancemanager.DynamicQuestManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExDynamicQuest;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;

public class DynamicQuestTask {
    protected static class QuestUpdateProgress extends RunnableImpl {
        private final DynamicQuest _dynamicQuest;

        protected QuestUpdateProgress(DynamicQuest dynamicQuest) {
            _dynamicQuest = dynamicQuest;
        }

        @Override
        public void runImpl() throws Exception {
            for (Player player : _dynamicQuest.getAllParticipiants()) {
                _dynamicQuest.sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);
            }
        }
    }

    protected static class QuestSendPacket extends RunnableImpl {
        private final Player _activeChar;
        private final IStaticPacket _sendPacket;

        public QuestSendPacket(Player activeChar, IStaticPacket packet) {
            _sendPacket = packet;
            _activeChar = activeChar;
        }

        @Override
        public void runImpl() throws Exception {
            _activeChar.sendPacket(_sendPacket);
        }
    }

    protected static class QuestViewResult extends RunnableImpl {
        private final Player _activeChar;
        private final DynamicQuest _dynamicQuest;

        public QuestViewResult(DynamicQuest dynamicQuest, Player activeChar) {
            _activeChar = activeChar;
            _dynamicQuest = dynamicQuest;
        }

        @Override
        public void runImpl() throws Exception {
            _dynamicQuest.sendResults(_activeChar);
            _dynamicQuest.sendProgress(_activeChar, ExDynamicQuest.UpdateAction.ACTION_VIEW_RESULT);
            _activeChar.sendPacket(new ExDynamicQuest(_activeChar, _dynamicQuest.getTemplate().isCampain(), _dynamicQuest.getTemplate().getQuestId(), DynamicQuestManager.getStepId(_dynamicQuest.getTemplate().getTaskId())).endCampain());
        }
    }

    protected static class QuestInviteTask extends RunnableImpl {
        private final DynamicQuest _dynamicQuest;

        public QuestInviteTask(DynamicQuest dynamicQuest) {
            _dynamicQuest = dynamicQuest;
        }

        @Override
        public void runImpl() throws Exception {
            _dynamicQuest.invitePlayers();
        }
    }

    protected static class QuestStopTask extends RunnableImpl {
        private final DynamicQuest _dynamicQuest;

        public QuestStopTask(DynamicQuest dynamicQuest) {
            _dynamicQuest = dynamicQuest;
        }

        @Override
        public void runImpl() throws Exception {
            _dynamicQuest.endQuest(true);
        }
    }

    protected static class QuestStartTask extends RunnableImpl {
        private final DynamicQuest _dynamicQuest;

        public QuestStartTask(DynamicQuest dynamicQuest) {
            _dynamicQuest = dynamicQuest;
        }

        @Override
        public void runImpl() {
            _dynamicQuest.startQuest();
        }
    }
}