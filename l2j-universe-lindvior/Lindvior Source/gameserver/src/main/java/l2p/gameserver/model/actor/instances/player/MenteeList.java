/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.actor.instances.player;

import l2p.gameserver.dao.MentoringDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.serverpackets.ExMentorList;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Mentoring;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:41
 */
public class MenteeList {
    private List<Mentee> menteeInfo = new ArrayList<Mentee>();
    private Player mentor;

    public MenteeList(Player mentor) {
        this.mentor = mentor;
    }

    public void remove(String name, boolean isMentor, boolean notify) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        int objectId = removeMentee(name);
        if ((objectId > 0) && (notify)) {
            Player otherSideMentee = World.getPlayer(name);

            if (otherSideMentee != null) {
                otherSideMentee.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString(isMentor ? name : this.mentor.getName()));
                Mentoring.removeEffectsFromPlayer(otherSideMentee);
            }
            this.mentor.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString(isMentor ? name : this.mentor.getName()));
        }
    }

    public String toString() {
        return "MenteeList[owner=" + this.mentor.getName() + "]";
    }

    public void notify(boolean online) {
        for (Mentee mentee : this.menteeInfo) {
            Player menteePlayer = World.getPlayer(mentee.getObjectId());
            if (menteePlayer != null) {
                Mentee thisMentee = menteePlayer.getMenteeList().checkInList(this.mentor.getObjectId());
                if (thisMentee != null) {
                    thisMentee.update(this.mentor, online);

                    if (online) {
                        menteePlayer.sendPacket(new SystemMessage2(mentee.isMentor() ? SystemMsg.YOU_MENTEE_S1_HAS_CONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_CONNECTED).addString(this.mentor.getName()));
                    } else {
                        menteePlayer.sendPacket(new SystemMessage2(mentee.isMentor() ? SystemMsg.YOU_MENTEE_S1_HAS_DISCONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_DISCONNECTED).addString(this.mentor.getName()));
                    }

                    menteePlayer.sendPacket(new ExMentorList(menteePlayer));
                    mentee.update(menteePlayer, online);
                }
            }
        }
    }

    public Mentee getMenteeById(int id) {
        for (Mentee info : this.menteeInfo) {
            if (info.getObjectId() == id)
                return info;
        }
        return null;
    }

    public void restore() {
        this.menteeInfo = MentoringDAO.getInstance().selectMenteeList(this.mentor);
    }

    public int getMentor() {
        for (Mentee menteeInfo : getMentees()) {
            if (menteeInfo.isMentor())
                return menteeInfo.getObjectId();
        }
        return 0;
    }

    public List<Mentee> getMentees() {
        return this.menteeInfo;
    }

    public boolean whoIsOnline(boolean login) {
        for (Mentee mentee : this.menteeInfo) {
            Player menteePlayer = World.getPlayer(mentee.getObjectId());
            if (menteePlayer != null) {
                Mentee thisMentee = menteePlayer.getMenteeList().checkInList(this.mentor.getObjectId());
                if (thisMentee != null) {
                    thisMentee.update(this.mentor, login);

                    if (menteePlayer.isOnline())
                        return true;
                }
            }
        }
        return false;
    }

    public void addMentee(Player mentee) {
        if (this.menteeInfo.size() < 3) {
            this.menteeInfo.add(new Mentee(mentee, false));
            MentoringDAO.getInstance().insert(this.mentor, mentee);
        }
    }

    public void addMentor(Player mentor) {
        if (this.menteeInfo.size() == 0) {
            this.menteeInfo.add(new Mentee(mentor, true));
            Mentoring.addSkillsToMentor(mentor);
        }
    }

    private int removeMentee(String name) {
        if (name == null) {
            return 0;
        }
        Mentee removedMentee = null;
        for (Mentee entry : this.menteeInfo) {
            if (name.equalsIgnoreCase(entry.getName())) {
                removedMentee = entry;
                break;
            }
        }
        if (removedMentee != null) {
            this.menteeInfo.remove(removedMentee);
            MentoringDAO.getInstance().delete(this.mentor.getObjectId(), removedMentee.getObjectId());
            return removedMentee.getObjectId();
        }

        return 0;
    }

    public Mentee checkInList(int objectId) {
        for (Mentee mentee : this.menteeInfo) {
            if (mentee.getObjectId() == objectId)
                return mentee;
        }
        return null;
    }
}
