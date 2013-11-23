/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.actor.instances.player;

import l2p.commons.lang.reference.HardReference;
import l2p.commons.lang.reference.HardReferences;
import l2p.gameserver.model.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.07.12
 * Time: 0:37
 */
public class Mentee {
    private final int _objectId;
    private String _name;
    private int _classId;
    private int _level;
    private boolean _isMentor;
    private HardReference<Player> _playerRef = HardReferences.emptyRef();

    public Mentee(Player player, boolean isMentor) {
        this._objectId = player.getObjectId();
        this._isMentor = isMentor;
        update(player, true);
    }

    public Mentee(Player player) {
        this._objectId = player.getObjectId();
        update(player, true);
    }

    public Mentee(ResultSet rset)
            throws SQLException {
        this._objectId = rset.getInt("charid");
        this._name = rset.getString("c.char_name");
        this._classId = rset.getInt("s.class_id");
        this._level = rset.getInt("s.level");
        this._isMentor = (this._classId > 138);
    }

    public String toString() {
        return "MenteeInfo{_objectId=" + this._objectId + ", _name='" + this._name + '\'' + ", _classId=" + this._classId + ", _level=" + this._level + ", _isMentor=" + this._isMentor + ", _playerRef=" + this._playerRef + '}';
    }

    public String getName() {
        Player player = getPlayer();
        return player == null ? this._name : player.getName();
    }

    public void update(Player player, boolean set) {
        this._level = player.getLevel();
        this._name = player.getName();
        this._classId = player.getActiveClassId();

        _playerRef = set ? player.getRef() : _playerRef;
    }

    public int getLevel() {
        Player player = getPlayer();
        return player == null ? this._level : player.getLevel();
    }

    public int getClassId() {
        Player player = getPlayer();
        return player == null ? this._classId : player.getActiveClassId();
    }

    public boolean isOnline() {
        Player player = (Player) this._playerRef.get();
        return (player != null) && (!player.isInOfflineMode());
    }

    public Player getPlayer() {
        Player player = (Player) this._playerRef.get();
        return (player != null) && (!player.isInOfflineMode()) ? player : null;
    }

    public int getObjectId() {
        return this._objectId;
    }

    public boolean isMentor() {
        return this._isMentor;
    }
}
