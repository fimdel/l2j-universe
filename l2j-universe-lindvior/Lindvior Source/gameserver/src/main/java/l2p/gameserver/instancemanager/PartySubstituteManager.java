/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.instancemanager;

import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.listener.actor.player.OnPlayerExitListener;
import l2p.gameserver.listener.actor.player.OnPlayerPartyInviteListener;
import l2p.gameserver.listener.actor.player.OnPlayerPartyLeaveListener;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.actor.listener.PlayerListenerList;
import l2p.gameserver.model.base.ClassType2;
import l2p.gameserver.network.serverpackets.ExWaitWaitingSubStituteInfo;
import l2p.gameserver.network.serverpackets.company.party.PartySmallWindowUpdate;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.taskmanager.tasks.PartySubstituteTask;
import l2p.gameserver.utils.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.11.12
 * Time: 21:18
 */
public class PartySubstituteManager {
    // Игроки, которые ищут пати.
    private static Set<Player> _waitingParty = new HashSet<Player>();

    // Пати, которые изут замену игру. В наборе - игроки, которые ищут себе замену
    private static ConcurrentMap<Player, Integer> _waitingPlayer = new ConcurrentHashMap<Player, Integer>();

    // Время, по которому замена игрока в пати останавливается. 5 минут.
    public static final long TIME_OUT = 300000;

    // Время, по которому запрос на замену игрока в пати истекает. 3 минуты.
    public static final long ACCEPT_TIME_OUT = 180000;

    // Время обновления всех связей. По умолчанию 1 минута.
    private static final long REFRASH_TIME = 60000;

    // Максимальная разница между ЛвЛом чара, подходящего для замены
    private static final int LEVEL_DIFF = 5;

    // Скил Неуязвимость
    private static final int SKILL_INVUL_ID = 1750;

    private final SubstituteListeners _listener;

    public PartySubstituteManager() {
        _listener = new SubstituteListeners();
        PlayerListenerList.addGlobal(_listener);
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new PartySubstituteTask(), REFRASH_TIME, REFRASH_TIME);
    }

    // Добавление игрока к списку игроков, которые ищут пати
    public void addPlayerToParty(Player p) {
        _waitingParty.add(p);
        p.sendPacket(new ExWaitWaitingSubStituteInfo(ExWaitWaitingSubStituteInfo.WAITING_OK));
        p.sendPacket(SystemMsg.STARTED_SEARCHING_THE_PARTY);
    }

    public boolean isPlayerToParty(Player p) {
        return _waitingParty.contains(p);
    }

    public void removePlayerFromParty(Player p) {
        _waitingParty.remove(p);
        p.sendPacket(new ExWaitWaitingSubStituteInfo(ExWaitWaitingSubStituteInfo.WAITING_CANCEL));
        p.sendPacket(SystemMsg.STOPPED_SEARCHING_THE_PARTY);
    }

    // Добавляет игрока в список, которому необходимо найти замену
    public void addPlayerToReplace(Player p) {
        _waitingPlayer.put(p, 0);
        // Показываем всем иконку, о том, что чару ищется замена
        p.getParty().broadCast(new PartySmallWindowUpdate(p));
    }

    public void updatePlayerToReplace(Player p, int i) {
        _waitingPlayer.put(p, i);
    }

    public boolean isPlayerToReplace(Player p) {
        return _waitingPlayer.containsKey(p);
    }

    public void removePlayerReplace(Player p) {
        _waitingPlayer.remove(p);
    }

    private class SubstituteListeners implements OnPlayerExitListener, OnPlayerPartyLeaveListener, OnPlayerPartyInviteListener {
        @Override
        public void onPlayerExit(Player player) {
            _waitingParty.remove(player);
            _waitingPlayer.remove(player);
        }

        @Override
        public void onPartyLeave(Player player) {
            removePlayerReplace(player);
        }

        @Override
        public void onPartyInvite(Player player) {
            synchronized (this) {
                removePlayerFromParty(player);
            }
        }
    }

    public void doReplace(Player replaceWho, Player replaceTo) {
        if (replaceWho == null || replaceTo == null)
            return;

        Party p = replaceWho.getParty();

        if (p == null)
            return;

        Player leader = p.getPartyLeader();

        // Фишка, дабы не распалось пати, когда пати состоит из 2 чел
        if (p.getMemberCount() == Party.MAX_SIZE) {
            p.removePartyMember(replaceWho, true);
            p.addPartyMember(replaceTo);
        } else {
            p.addPartyMember(replaceTo);
            p.removePartyMember(replaceWho, true);
        }

        Location loc = leader.getLoc();

        replaceTo.setLoc(loc);

        Skill skill = SkillTable.getInstance().getInfo(SKILL_INVUL_ID, 1);

        for (Player pp : p.getPartyMembers()) {
            for (EffectTemplate et : skill.getEffectTemplates()) {
                Env env = new Env(pp, pp, skill);
                Effect effect = et.getEffect(env);
                effect.setPeriod(10000);
                pp.getEffectList().addEffect(effect);
            }
        }

        Skill skill1 = SkillTable.getInstance().getInfo(19036, 1);

        for (Player pp : p.getPartyMembers()) {
            for (EffectTemplate et : skill.getEffectTemplates()) {
                Env env = new Env(pp, pp, skill1);
                Effect effect = et.getEffect(env);
                effect.setPeriod(10000);
                pp.getEffectList().addEffect(effect);
            }
        }
    }

    public boolean isGoodPlayer(Player a, Player b) {
        int plvl = a.getLevel();
        int tlvl = b.getLevel();

        int d_lvl = Math.abs(plvl - tlvl);

        if (d_lvl > LEVEL_DIFF)
            return false;

        ClassType2 ca = a.getClassId().getType2();
        ClassType2 cb = b.getClassId().getType2();

        if (ca != null && cb != null)
            if (ca == cb)
                return false;

        // TODO: Проверка на виталити? оО

        return true;
    }

    public Set<Player> getWaitingParty() {
        return _waitingParty;
    }

    public ConcurrentMap<Player, Integer> getWaitingPlayer() {
        return _waitingPlayer;
    }

    public static PartySubstituteManager getInstance() {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder {
        protected static final PartySubstituteManager _instance = new PartySubstituteManager();
    }
}
