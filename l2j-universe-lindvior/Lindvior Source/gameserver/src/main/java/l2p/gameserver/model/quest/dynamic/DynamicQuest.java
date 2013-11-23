/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.quest.dynamic;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.StartItemHolder;
import l2p.gameserver.data.xml.holder.StartItemHolder.StartItem;
import l2p.gameserver.instancemanager.DynamicQuestManager;
import l2p.gameserver.instancemanager.SpawnManager;
import l2p.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.ExDynamicQuest;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.DynamicQuestTemplate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Future;

public class DynamicQuest extends Quest {
    private static final Logger _log = Logger.getLogger(DynamicQuest.class);
    private final DynamicQuestTemplate _template;
    private final TIntObjectHashMap<Player> _participiants = new TIntObjectHashMap();

    private boolean _questStarted = false;
    private Future<?> _inviteTask;
    private Future<?> _endTask;
    private Future<?> _updateTask;
    private int _currentPoints = 0;

    private final TIntIntHashMap _pointsByMembers = new TIntIntHashMap();

    private final List<String> _winners = new FastList();
    private final List<String> _eliteWinners = new FastList();

    private List<DynamicQuestResult> _results = new FastList();

    private long _lastResultUpdate = 0L;

    private long _initialTime = 0L;
    private final OnEnterWorld _enterListener;
    private final OnEnterExitZoneListener _zoneListener;

    public DynamicQuest(DynamicQuestTemplate quest) {
        super(2, quest.getTaskId(), quest.getQuestName());

        _template = quest;

        _enterListener = new OnEnterWorld();
        _zoneListener = new OnEnterExitZoneListener();

        addKillId(_template.getAllPoints().keySet());

        init();
    }

    private void init() {
        if (_template.isCampain()) {
            for (DynamicQuestTemplate.DynamicQuestDate startDate : _template.getStartDates()) {
                long scheduleTime = startDate.getNextScheduleTime() - System.currentTimeMillis();

                if (scheduleTime <= 0L) {
                    startQuest();
                } else {
                    _initialTime = (System.currentTimeMillis() + scheduleTime);

                    ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestStartTask(this), scheduleTime);
                }

            }

        } else if ((_template.isZoneQuest()) && (_template.isAutostart())) {
            startQuest();
        }
    }

    public DynamicQuestTemplate getTemplate() {
        return _template;
    }

    public boolean isStarted() {
        return _questStarted;
    }

    public void giveReward(Player player) {
        if ((!_winners.contains(player.getName())) && (!_eliteWinners.contains(player.getName()))) {
            return;
        }
        List<StartItem> rewards = _eliteWinners.contains(player.getName()) ? _template.getAllEliteRewards() : _template.getAllRewards();

        for (StartItemHolder.StartItem item : rewards) {
            player.getInventory().addItem(item.id, item.count);
        }
        showDialog(player, "finish");

        _winners.remove(player.getName());
        _eliteWinners.remove(player.getName());
    }

    public List<StartItemHolder.StartItem> getRewards() {
        return _template.getAllRewards();
    }

    public void onQuestEnd(Player player, QuestState qs) {
    }

    public void startQuest() {
        if (_questStarted) {
            return;
        }
        if (_template.getSpawnGroup() != null) {
            SpawnManager.getInstance().spawn(_template.getSpawnGroup());
        }
        for (Zone zone : _template.getAllZones()) {
            zone.addListener(_zoneListener);
        }
        CharListenerList.addGlobal(_enterListener);

        _lastResultUpdate = 0L;
        _results.clear();
        _participiants.clear();
        _winners.clear();
        _eliteWinners.clear();
        _currentPoints = 0;
        _pointsByMembers.clear();

        _questStarted = true;

        if (_template.isCampain()) {
            if (_inviteTask == null) {
                _inviteTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DynamicQuestTask.QuestInviteTask(this), 0L, 300000L);
            }
        } else {
            invitePlayers();
        }
        _initialTime = (System.currentTimeMillis() + (_template.getDuration() * 60 * 1000));
        if ((_endTask == null) && (_updateTask == null)) {
            _endTask = ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestStopTask(this), _template.getDuration() * 60 * 1000);
        }
    }

    public void endQuest(final boolean startNextTask) {
        if (!_questStarted) {
            return;
        }
        _questStarted = false;

        boolean isFailed = false;

        if (_currentPoints < _template.getPoints()) {
            isFailed = true;
        }
        if (_template.getSpawnGroup() != null) {
            SpawnManager.getInstance().despawn(_template.getSpawnGroup());
        }
        for (Zone zone : _template.getAllZones()) {
            zone.removeListener(_zoneListener);
        }
        CharListenerList.removeGlobal(_enterListener);

        int minElitePoints = 0;

        Integer[] points = ArrayUtils.toObject(_pointsByMembers.values());

        Arrays.sort(points, Collections.reverseOrder());

        if (points.length > 0) {
            minElitePoints = (points.length >= 7 ? points[6] : points[(points.length - 1)]).intValue();
        }
        for (Player player : getAllParticipiants()) {
            if (isFailed) {
                onQuestFailed(player, player.getQuestState(_template.getQuestName()));
                showDialog(player, "failed");
            } else {
                int participiantPoints = _pointsByMembers.containsKey(player.getObjectId()) ? _pointsByMembers.get(player.getObjectId()) : 0;
                onQuestEnd(player, player.getQuestState(_template.getQuestName()));
                if ((participiantPoints >= minElitePoints) && (minElitePoints > 0)) {
                    if (!showDialog(player, "elite_reward")) {
                        giveReward(player);
                    }
                    _eliteWinners.add(player.getName());
                } else {
                    if (!showDialog(player, "reward")) {
                        giveReward(player);
                    }
                    _winners.add(player.getName());
                }
            }

            if (!isFailed) {
                sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_GET_REWARD);

                if (_template.isCampain()) {
                    ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestViewResult(this, player), 180000L);
                }
            } else if (!_template.isCampain()) {
                sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_FAIL);
            }
        }
        if ((isFailed) && (_template.isCampain())) {
            for (Player player : GameObjectsStorage.getAllPlayers()) {
                if (player.getLevel() >= _template.getMinLevel()) {
                    sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_FAIL);

                    ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestSendPacket(player, new ExDynamicQuest(player, _template.isCampain(), _template.getQuestId(), DynamicQuestManager.getStepId(_template.getTaskId())).endCampain()), 300000L);
                }
            }
        }
        if (_inviteTask != null) {
            _inviteTask.cancel(true);
            _inviteTask = null;
        }

        if (_endTask != null) {
            _endTask.cancel(true);
            _endTask = null;
        }

        if (_updateTask != null) {
            _updateTask.cancel(true);
            _updateTask = null;
        }

        if (!_template.isCampain()) {
            if (!isFailed) {
                int nextTaskId = _template.getNextTaskId();

                if (nextTaskId > 0) {
                    final DynamicQuest quest = DynamicQuestManager.getInstance().getQuestByTaskId(nextTaskId);
                    if (quest != null) {
                        _initialTime = (System.currentTimeMillis() + 180000L);
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                for (Player player : DynamicQuest.this.getAllParticipiants()) {
                                    player.sendPacket(new ExDynamicQuest(player, DynamicQuest.this._template.isCampain(), DynamicQuest.this._template.getQuestId(), DynamicQuestManager.getStepId(DynamicQuest.this._template.getTaskId())).endCampain());
                                }
                                if (startNextTask) {
                                    ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestStartTask(quest), 5500L);
                                }
                            }
                        }, 180000L);
                    }

                }

            } else {
                _initialTime = (System.currentTimeMillis() + 180000L);
                ThreadPoolManager.getInstance().schedule(new DynamicQuestTask.QuestStartTask(this), 180000L);
            }
        }
    }

    public void sendProgress(Player player, ExDynamicQuest.UpdateAction updateAction) {
        player.sendPacket(new ExDynamicQuest(player, _template.isCampain(), getQuestId(), DynamicQuestManager.getStepId(_template.getTaskId())).setProgressUpdate(updateAction, getRemainingTime(), _template.getTaskId(), _currentPoints, _template.getPoints()));
    }

    public void sendResults(Player player) {
        List memberList = new ArrayList();

        memberList.addAll(_winners);
        memberList.addAll(_eliteWinners);

        long currentTime = System.currentTimeMillis();

        if ((_lastResultUpdate == 0L) || ((_lastResultUpdate + 5000L) <= currentTime)) {
            _lastResultUpdate = currentTime;
            _results = new FastList();

            if (!_template.isCampain()) {
                Map unsorted = new HashMap();
                List passedParticipiants = new ArrayList();

                for (Player participiant : getAllParticipiants()) {
                    if (participiant != null) {
                        int objectId = participiant.getObjectId();

                        if (!passedParticipiants.contains(Integer.valueOf(objectId))) {
                            passedParticipiants.add(Integer.valueOf(objectId));

                            Party party = participiant.getParty();

                            if ((party != null) || (participiant.isGM())) {
                                String leader = party != null ? party.getPartyLeader().getName() : participiant.getName();

                                int currentContributed = _pointsByMembers.containsKey(objectId) ? _pointsByMembers.get(objectId) : 0;
                                int additionalContributed = 0;

                                if (unsorted.containsKey(leader)) {
                                    DynamicQuestResult currentResult = (DynamicQuestResult) unsorted.get(leader);
                                    currentResult.setContributed(ContributionType.CURRENT, currentContributed + currentResult.getContributed(ContributionType.CURRENT));
                                    currentResult.setContributed(ContributionType.ADDITIONAL, additionalContributed + currentResult.getContributed(ContributionType.ADDITIONAL));
                                    currentResult.setContributed(ContributionType.TOTAL, currentContributed + currentResult.getContributed(ContributionType.TOTAL));
                                } else {
                                    DynamicQuestResult currentResult = new DynamicQuestResult(leader, currentContributed, additionalContributed, currentContributed);
                                    unsorted.put(leader, currentResult);
                                    _results.add(currentResult);
                                }
                            }
                        }
                    }
                }
            } else {
                int currentContributed = _pointsByMembers.containsKey(player.getObjectId()) ? _pointsByMembers.get(player.getObjectId()) : 0;
                int additionalContributed = 0;

                _results.add(new DynamicQuestResult(player.getName(), currentContributed, additionalContributed, currentContributed));
            }

            Collections.sort(_results);
        }

        player.sendPacket(new ExDynamicQuest(player, _template.isCampain(), _template.getQuestId(), DynamicQuestManager.getStepId(_template.getTaskId())).setResultsUpdate(_results, getRemainingTime()));
    }

    public List<String> getWinners() {
        return _winners;
    }

    public int getQuestId() {
        return _template.getQuestId();
    }

    public void invitePlayers() {
        if (!_questStarted) {
            return;
        }
        if (_template.isCampain()) {
            for (Player player : GameObjectsStorage.getAllPlayers()) {
                if ((player.getLevel() >= _template.getMinLevel()) && (!_participiants.containsKey(player.getObjectId()))) {
                    player.sendPacket(new ExDynamicQuest(player, _template.isCampain(), _template.getQuestId(), DynamicQuestManager.getStepId(_template.getTaskId())).startCampain());
                }
            }
        } else {
            for (Zone zone : _template.getAllZones()) {
                for (Player player : zone.getInsidePlayers()) {
                    if ((player.getParty() != null) || (player.isGM())) {
                        player.sendPacket(new ExDynamicQuest(player, _template.isCampain(), _template.getQuestId(), DynamicQuestManager.getStepId(_template.getTaskId())).startCampain());
                        if (DynamicQuestManager.getStepId(_template.getTaskId()) != 1) {
                            sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);
                        }
                        addParticipiant(player);
                    }
                }
            }
        }
    }

    public void onQuestFailed(Player player, QuestState qs) {
    }

    public void onRewardReceived(Player player, QuestState qs) {
    }

    public void addParticipiant(Player player) {
        if (!_questStarted) {
            return;
        }
        DynamicQuestState st = newQuestState(player);
        player.setQuestState(st);
        st.startQuest();

        _participiants.put(player.getObjectId(), player);

        sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);

        showDialog(player, "accept");
    }

    public boolean isParticipiant(Player player) {
        return (_participiants.containsKey(player.getObjectId())) || (_winners.contains(player.getName())) || (_eliteWinners.contains(player.getName()));
    }

    public Player[] getAllParticipiants() {
        return _participiants.values(new Player[_participiants.size()]);
    }

    public void sendRewardInfo(Player player) {
        if (getEliteWinners().contains(player.getName())) {
            showDialog(player, "elite_reward");
        } else {
            showDialog(player, "reward");
        }
    }

    public int getRemainingTime() {
        return Math.max(0, (int) ((_initialTime - System.currentTimeMillis()) / 1000L));
    }

    public List<String> getEliteWinners() {
        return _eliteWinners;
    }

    public int getCollectedPoints() {
        return _currentPoints;
    }

    @Override
    public String onKill(NpcInstance npc, QuestState st) {
        if (!_questStarted) {
            return null;
        }
        if (!_participiants.containsKey(st.getPlayer().getObjectId())) {
            return "";
        }
        try {
            int points = _template.getKillPoint(npc.getNpcId());

            if ((DynamicQuestManager.hasGmBonus()) && (st.getPlayer().isGM())) {
                points *= _template.getPoints() / 10;
            }
            if (!_template.isCampain()) {
                Party party = st.getPlayer().getParty();
                if (party != null) {
                    for (Player partyMember : party.getPartyMembers()) {
                        if (partyMember.getObjectId() != st.getPlayer().getObjectId()) {
                            int memberPoints = points;
                            if (_pointsByMembers.containsKey(partyMember.getObjectId())) {
                                memberPoints += _pointsByMembers.get(partyMember.getObjectId());
                            }
                            _pointsByMembers.put(partyMember.getObjectId(), memberPoints);
                        }
                    }
                    points *= party.getMemberCount();
                }
            }

            if (points > 0) {
                _currentPoints += points;

                if (_pointsByMembers.containsKey(st.getPlayer().getObjectId())) {
                    points += _pointsByMembers.get(st.getPlayer().getObjectId());
                }
                _currentPoints = Math.min(_currentPoints, _template.getPoints());

                _pointsByMembers.put(st.getPlayer().getObjectId(), points);
            }

            if (_currentPoints >= _template.getPoints()) {
                for (Player player : getAllParticipiants()) {
                    sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);
                }
                endQuest(true);
            }
        } catch (Exception e) {
            _log.error("Dynamic Quest onKill event failed: Quest=" + _template.getQuestName(), e);
        }

        return null;
    }

    public boolean showDialog(Player player, String type) {
        String html = HtmCache.getInstance().getNotNull("dynamic_quest/" + _template.getQuestPath() + "/" + _template.getDialog(type) + ".htm", player);

        if (html == null) {
            return false;
        }
        NpcHtmlMessage htmlMessage = new NpcHtmlMessage(0);

        htmlMessage.setHtml(html);

        player.sendPacket(htmlMessage);

        return true;
    }

    @Override
    public DynamicQuestState newQuestState(Player player) {
        return new DynamicQuestState(this, player);
    }

    public static enum ContributionType {
        CURRENT, ADDITIONAL, TOTAL;
    }

    public class OnEnterWorld implements OnPlayerEnterListener {
        public OnEnterWorld() {
        }

        @Override
        public void onPlayerEnter(Player player) {
            if (!DynamicQuest.this._questStarted) {
                return;
            }
            if (DynamicQuest.this._participiants.containsKey(player.getObjectId())) {
                DynamicQuest.this.sendProgress(player, ExDynamicQuest.UpdateAction.ACTION_PROGRESS);
            }
        }
    }

    public class OnEnterExitZoneListener implements OnZoneEnterLeaveListener {
        public OnEnterExitZoneListener() {
        }

        @Override
        public void onZoneEnter(Zone zone, Creature actor) {
            if (!DynamicQuest.this._questStarted) {
                return;
            }
            if (actor.isPlayer()) {
                actor.sendPacket(new ExDynamicQuest((Player) actor, DynamicQuest.this._template.isCampain(), DynamicQuest.this._template.getQuestId(), DynamicQuestManager.getStepId(DynamicQuest.this._template.getTaskId())).startCampain());

                if (!DynamicQuest.this._template.isCampain()) {
                    DynamicQuest.this.addParticipiant((Player) actor);
                }
            }
        }

        @Override
        public void onZoneLeave(Zone zone, Creature actor) {
            if (!DynamicQuest.this._questStarted) {
                return;
            }
            if (actor.isPlayer()) {
                if (!DynamicQuest.this._template.isCampain()) {
                    DynamicQuest.this.sendProgress((Player) actor, ExDynamicQuest.UpdateAction.ACTION_FAIL);
                    actor.sendPacket(new ExDynamicQuest((Player) actor, DynamicQuest.this._template.isCampain(), DynamicQuest.this._template.getQuestId(), DynamicQuestManager.getStepId(DynamicQuest.this._template.getTaskId())).endCampain());

                    if (DynamicQuest.this._participiants.containsKey(actor.getObjectId())) {
                        DynamicQuest.this._participiants.remove(actor.getObjectId());
                    }
                }
            }
        }
    }
}
