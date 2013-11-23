/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model;

import l2p.commons.collections.JoinedIterator;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcFriendInstance;
import l2p.gameserver.model.matching.MatchingRoom;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.company.MPCC.ExMPCCClose;
import l2p.gameserver.network.serverpackets.company.MPCC.ExMPCCOpen;
import l2p.gameserver.network.serverpackets.company.MPCC.ExMPCCPartyInfoUpdate;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandChannel implements PlayerGroup {
    public static final int STRATEGY_GUIDE_ID = 8871;
    public static final int CLAN_IMPERIUM_ID = 391;

    private final List<Party> _commandChannelParties = new CopyOnWriteArrayList<Party>();
    private Player _commandChannelLeader;
    private int _commandChannelLvl;
    private Reflection _reflection;

    private MatchingRoom _matchingRoom;

    /**
     * Creates a New Command Channel and Add the Leaders party to the CC
     *
     * @param leader
     */
    public CommandChannel(Player leader) {
        _commandChannelLeader = leader;
        _commandChannelParties.add(leader.getParty());
        _commandChannelLvl = leader.getParty().getLevel();
        leader.getParty().setCommandChannel(this);
        broadCast(ExMPCCOpen.STATIC);
    }

    /**
     * Adds a Party to the Command Channel
     *
     * @param party
     */
    public void addParty(Party party) {
        broadCast(new ExMPCCPartyInfoUpdate(party, 1));
        _commandChannelParties.add(party);
        refreshLevel();
        party.setCommandChannel(this);

        for (Player $member : party) {
            $member.sendPacket(ExMPCCOpen.STATIC);
            if (_matchingRoom != null)
                _matchingRoom.broadcastPlayerUpdate($member);
        }
    }

    /**
     * Removes a Party from the Command Channel
     *
     * @param party
     */
    public void removeParty(Party party) {
        _commandChannelParties.remove(party);
        refreshLevel();
        party.setCommandChannel(null);
        party.broadCast(ExMPCCClose.STATIC);
        Reflection reflection = getReflection();
        if (reflection != null)
            for (Player player : party.getPartyMembers())
                player.teleToLocation(reflection.getReturnLoc(), 0);

        if (_commandChannelParties.size() < 2)
            disbandChannel();
        else
            for (Player $member : party) {
                $member.sendPacket(new ExMPCCPartyInfoUpdate(party, 0));
                if (_matchingRoom != null)
                    _matchingRoom.broadcastPlayerUpdate($member);
            }
    }

    /**
     * Распускает Command Channel
     */
    public void disbandChannel() {
        broadCast(Msg.THE_COMMAND_CHANNEL_HAS_BEEN_DISBANDED);
        for (Party party : _commandChannelParties) {
            party.setCommandChannel(null);
            party.broadCast(ExMPCCClose.STATIC);
            if (isInReflection())
                party.broadCast(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
        }
        Reflection reflection = getReflection();
        if (reflection != null) {
            reflection.startCollapseTimer(60000L);
            setReflection(null);
        }

        if (_matchingRoom != null)
            _matchingRoom.disband();
        _commandChannelParties.clear();
        _commandChannelLeader = null;
    }

    /**
     * @return overall count members of the Command Channel
     */
    public int getMemberCount() {
        int count = 0;
        for (Party party : _commandChannelParties)
            count += party.getMemberCount();
        return count;
    }

    /**
     * Broadcast packet to every channel member
     *
     * @param gsp
     */
    @Override
    public void broadCast(IStaticPacket... gsp) {
        for (Party party : _commandChannelParties)
            party.broadCast(gsp);
    }

    /**
     * Broadcast packet to every party leader of command channel
     */
    public void broadcastToChannelPartyLeaders(L2GameServerPacket gsp) {
        for (Party party : _commandChannelParties) {
            Player leader = party.getPartyLeader();
            if (leader != null)
                leader.sendPacket(gsp);
        }
    }

    /**
     * @return list of Parties in Command Channel
     */
    public List<Party> getParties() {
        return _commandChannelParties;
    }

    @Deprecated
    public List<Player> getMembers() {
        List<Player> members = new ArrayList<Player>(_commandChannelParties.size());
        for (Party party : getParties())
            members.addAll(party.getPartyMembers());
        return members;
    }

    @Override
    public Iterator<Player> iterator() {
        List<Iterator<Player>> iterators = new ArrayList<Iterator<Player>>(_commandChannelParties.size());
        for (Party p : getParties())
            iterators.add(p.getPartyMembers().iterator());
        return new JoinedIterator<Player>(iterators);
    }

    /**
     * @return Level of CC
     */
    public int getLevel() {
        return _commandChannelLvl;
    }

    /**
     * @param newLeader the leader of the Command Channel
     */
    public void setChannelLeader(Player newLeader) {
        _commandChannelLeader = newLeader;
        broadCast(new SystemMessage(SystemMessage.COMMAND_CHANNEL_AUTHORITY_HAS_BEEN_TRANSFERRED_TO_S1).addString(newLeader.getName()));
    }

    /**
     * @return the leader of the Command Channel
     */
    public Player getChannelLeader() {
        return _commandChannelLeader;
    }

    /**
     * true if player is Command Channel leader
     */
    public boolean isLeaderCommandChannel(Player player) {
        return getChannelLeader() == player;
    }

    /**
     * Queen Ant, Core, Orfen, Zaken: MemberCount > 36<br>
     * Baium: MemberCount > 56<br>
     * Antharas: MemberCount > 225<br>
     * Valakas: MemberCount > 99<br>
     * normal RaidBoss: MemberCount > 18
     *
     * @param npc
     * @return true if proper condition for RaidWar
     */
    public boolean meetRaidWarCondition(NpcFriendInstance npc) {
        if (!npc.isRaid())
            return false;
        int npcId = npc.getNpcId();
        switch (npcId) {
            case 29001: // Queen Ant
            case 29006: // Core
            case 29014: // Orfen
            case 29022: // Zaken
                return getMemberCount() > 36;
            case 29020: // Baium
                return getMemberCount() > 56;
            case 29019: // Antharas
                return getMemberCount() > 225;
            case 29028: // Valakas
                return getMemberCount() > 99;
            default: // normal Raidboss
                return getMemberCount() > 18;
        }
    }

    private void refreshLevel() {
        _commandChannelLvl = 0;
        for (Party pty : _commandChannelParties)
            if (pty.getLevel() > _commandChannelLvl)
                _commandChannelLvl = pty.getLevel();
    }

    public boolean isInReflection() {
        return _reflection != null;
    }

    public void setReflection(Reflection reflection) {
        _reflection = reflection;
    }

    public Reflection getReflection() {
        return _reflection;
    }

    /**
     * Проверяет возможность создания командного канала
     */
    public static boolean checkAuthority(Player creator) {
        // CC могут создавать только лидеры партий, состоящие в клане ранком не ниже барона
        if (creator.getClan() == null || !creator.isInParty() || !creator.getParty().isLeader(creator) || creator.getPledgeClass() < Player.RANK_BARON) {
            creator.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
            return false;
        }

        // CC можно создать, если есть клановый скилл Clan Imperium
        boolean haveSkill = creator.getSkillLevel(CLAN_IMPERIUM_ID) > 0;

        // Ищем Strategy Guide в инвентаре
        boolean haveItem = creator.getInventory().getItemByItemId(STRATEGY_GUIDE_ID) != null;

        if (!haveSkill && !haveItem) {
            creator.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
            return false;
        }

        return true;
    }

    public MatchingRoom getMatchingRoom() {
        return _matchingRoom;
    }

    public void setMatchingRoom(MatchingRoom matchingRoom) {
        _matchingRoom = matchingRoom;
    }
}