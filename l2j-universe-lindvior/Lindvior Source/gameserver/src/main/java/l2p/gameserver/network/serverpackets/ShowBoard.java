/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.ServerPacket;
import l2p.gameserver.utils.Strings;

import java.util.List;

public class ShowBoard extends L2GameServerPacket {
    private final StringBuilder _htmlCode;
    private String _addFav = "";
    public static final String TYPE_101 = "101";
    public static final String TYPE_102 = "102";
    public static final String TYPE_103 = "103";
    public static final String TYPE_104 = "104";

    public ShowBoard(String htmlCode, String id, Player player) {
        this(htmlCode, id);
        if (player.getSessionVar("add_fav") != null) {
            this._addFav = "bypass _bbsaddfav_List";
        }
    }

    public ShowBoard(String htmlCode, String id) {
        this._htmlCode = Strings.startAppend(500, new String[]
                {
                        id, "\b", htmlCode
                });
    }

    public ShowBoard(List<String> arg) {
        this._htmlCode = Strings.startAppend(500, new String[]
                {
                        "1002\b"
                });
        for (String str : arg) {
            Strings.append(this._htmlCode, new String[]
                    {
                            str, " \b"
                    });
        }
    }

    public static void separateAndSend(String html, Player cha) {
        if (html == null) {
            return;
        }
        if (html.length() < 8180) {
            cha.sendPacket(new ShowBoard(html, "101", cha));
            cha.sendPacket(new ShowBoard(null, "102", cha));
            cha.sendPacket(new ShowBoard(null, "103", cha));
        } else if (html.length() < 16360) {
            cha.sendPacket(new ShowBoard(html.substring(0, 8180), "101", cha));
            cha.sendPacket(new ShowBoard(html.substring(8180, html.length()), "102", cha));
            cha.sendPacket(new ShowBoard(null, "103", cha));
        } else if (html.length() < 24540) {
            cha.sendPacket(new ShowBoard(html.substring(0, 8180), "101", cha));
            cha.sendPacket(new ShowBoard(html.substring(8180, 16360), "102", cha));
            cha.sendPacket(new ShowBoard(html.substring(16360, html.length()), "103", cha));
        }
    }

    @Override
    protected final void writeImpl() {
        writeEx(ServerPacket.ShowBoard);

        writeC(1);
        writeS("bypass _bbshome");
        writeS("bypass _bbsgetfav");
        writeS("bypass _bbsloc");
        writeS("bypass _bbsclan");
        writeS("bypass _bbsmemo");
        writeS("bypass _bbsmail");
        writeS("bypass _bbsfriends");
        writeS(this._addFav);

        writeS(this._htmlCode.length() < 8192 ? this._htmlCode.toString() : "<html><body>Html is too long!</body></html>");
    }
}