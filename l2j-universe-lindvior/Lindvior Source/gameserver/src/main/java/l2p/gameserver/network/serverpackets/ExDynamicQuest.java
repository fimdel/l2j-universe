/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.quest.dynamic.DynamicQuest;
import l2p.gameserver.model.quest.dynamic.DynamicQuestResult;

import java.util.List;

public class ExDynamicQuest extends L2GameServerPacket {
    private final Player _player;
    private final boolean _isCampaign;
    private Status _status;
    private final int _campainId;
    private final int _step;
    private UpdateAction _action;
    private int _remainingTime;
    private int _currentTask;
    private int _currentCount;
    private int _totalCount;
    private List<DynamicQuestResult> _participiants;

    public ExDynamicQuest(Player player, boolean isCampain, int campainId, int step) {
        _player = player;
        _isCampaign = isCampain;
        _campainId = campainId;
        _step = step;
    }

    public ExDynamicQuest startCampain() {
        _status = Status.CAMPAIN_START;
        return this;
    }

    public ExDynamicQuest endCampain() {
        _status = Status.CAMPAIN_END;
        return this;
    }

    public ExDynamicQuest setProgressUpdate(UpdateAction action, int remainingTime, int currentTask, int currentCount, int totalCount) {
        _action = action;
        _status = Status.CAMPAIN_PROGRESS;
        _remainingTime = remainingTime;
        _currentTask = currentTask;
        _currentCount = currentCount;
        _totalCount = totalCount;
        return this;
    }

    public ExDynamicQuest setResultsUpdate(List<DynamicQuestResult> participiants, int remainingTime) {
        _participiants = participiants;
        _status = Status.CAMPAIN_STATISTICS;
        _remainingTime = remainingTime;
        return this;
    }

    @Override
    protected void writeImpl() {
        writeEx(0xE9);

        writeC(!_isCampaign);
        writeC(_status.ordinal());
        writeD(_campainId);
        writeD(_step);

        switch (_status.ordinal()) {
            case 1:
                writeC(_action.ordinal());
                writeD(_remainingTime);

                if (!_isCampaign) {
                    if ((_player.getParty() != null) && (_player.getParty() != null)) {
                        writeD(_player.getParty().getMemberCount());
                    } else {
                        writeD(1);
                    }
                }
                int size = 1;
                writeD(size);
                for (int i = 0; i < size; i++) {
                    writeD(_currentTask);
                    writeD(_currentCount);
                    writeD(_totalCount);
                }
                break;
            case 2:
                if (!_isCampaign) {
                    writeD(_remainingTime);
                    writeD(_player.getParty() == null ? 1 : _player.getParty().getMemberCount());

                    if (_participiants != null) {
                        writeD(_participiants.size());
                        for (DynamicQuestResult resultInfo : _participiants) {
                            writeS(resultInfo.getParticipiant());
                            writeD(resultInfo.getContributed(DynamicQuest.ContributionType.CURRENT));
                            writeD(resultInfo.getContributed(DynamicQuest.ContributionType.ADDITIONAL));
                            writeD(resultInfo.getContributed(DynamicQuest.ContributionType.TOTAL));
                        }
                    } else {
                        writeD(0);
                    }

                } else if (_participiants != null) {
                    writeD(_participiants.size());
                    for (DynamicQuestResult resultInfo : _participiants) {
                        writeS(resultInfo.getParticipiant());
                    }
                }
                break;
        }
    }

    public static enum Status {
        CAMPAIN_START, CAMPAIN_END, CAMPAIN_PROGRESS, CAMPAIN_STATISTICS
    }

    public static enum UpdateAction {
        ACTION_PROGRESS, ACTION_GET_REWARD, ACTION_VIEW_RESULT, ACTION_FAIL
    }
}