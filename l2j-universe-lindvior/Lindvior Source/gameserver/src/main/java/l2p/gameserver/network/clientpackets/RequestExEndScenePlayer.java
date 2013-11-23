/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExStopScenePlayerPacket;
import l2p.gameserver.network.serverpackets.components.SceneMovie;

public class RequestExEndScenePlayer extends L2GameClientPacket {
    private int _movieId;

    @Override
    protected void readImpl() {
        //    _movieId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        if (!activeChar.isInMovie()) {
            activeChar.sendActionFailed();
            return;
        }

        SceneMovie movie = SceneMovie.getMovie(activeChar.getMovieId());
        if ((movie == null) || (!movie.isCancellable())) {
            activeChar.sendActionFailed();
            return;
        }

        activeChar.stopQuestMovie();
        activeChar.sendPacket(new ExStopScenePlayerPacket(movie.getId()));
    }

}