package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.network.serverpackets.ExIsCharNameCreatable;
import l2p.gameserver.utils.Util;

public class RequestCharacterNameCreatable extends L2GameClientPacket {
    private static final String _C__D0_B0_REQUESTCHARACTERNAMECREATABLE = "[C] D0:B0 RequestCharacterNameCreatable";
    private String _nickname;

    @Override
    protected void readImpl() {
        _nickname = readS();
    }

    @Override
    protected void runImpl() {
        if (CharacterDAO.getInstance().accountCharNumber(getClient().getLogin()) >= 8) {
            sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_TOO_MANY_CHARACTERS));
            return;
        }
        if (!Util.isMatchingRegexp(_nickname, Config.CNAME_TEMPLATE)) {
            sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_16_ENG_CHARS));
            return;
        }
        if (CharacterDAO.getInstance().getObjectIdByName(_nickname) > 0) {
            sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_NAME_ALREADY_EXISTS));
            return;
        }

        sendPacket(new ExIsCharNameCreatable(ExIsCharNameCreatable.REASON_CREATION_OK));
    }

    @Override
    public String getType() {
        return _C__D0_B0_REQUESTCHARACTERNAMECREATABLE;
    }
}
