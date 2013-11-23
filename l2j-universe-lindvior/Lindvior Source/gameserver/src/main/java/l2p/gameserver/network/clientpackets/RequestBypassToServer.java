/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import gnu.trove.TIntArrayList;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.MultiSellHolder;
import l2p.gameserver.handler.admincommands.AdminCommandHandler;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.handler.test.BypassHolder;
import l2p.gameserver.handler.test.IBypassHandler;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2p.gameserver.instancemanager.OlympiadHistoryManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Hero;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.instances.OlympiadManagerInstance;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.scripts.Scripts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestBypassToServer extends L2GameClientPacket {
    // Format: cS
    private static final Logger _log = LoggerFactory.getLogger(RequestBypassToServer.class);

    private static final TIntArrayList allowed_buff = new TIntArrayList(new int[]
            {
                    1032, 1033, 1035, 1036, 1040,
                    1044, 1045, 1048, 1059, 1062,
                    1068, 1073, 1077, 1078, 1085,
                    1086, 1087, 1182, 1189, 1191,
                    1204, 1240, 1242, 1243, 1257,
                    1268, 1284, 1303, 1304, 1307,
                    1352, 1353, 1354, 1355, 1356,
                    1357, 1363, 1388, 1389, 1392,
                    1393, 1397, 1413, 1460, 1461,
                    1499, 1500, 1501, 1502, 1503,
                    1504, 1519, 4699, 4700, 4702,
                    4703, 264, 265, 266, 267,
                    268, 269, 270, 271, 272,
                    273, 274, 275, 276, 277,
                    304, 305, 306, 307, 308,
                    309, 310, 311, 349, 363,
                    364, 365, 366, 529, 530,
                    764, 825, 826, 827, 828,
                    829, 830, 1043, 1259, 1542,
                    1548, 1549, 914, 915, 6429,
                    11517, 11518, 11519, 11520, 11521,
                    11522, 11523, 11524, 11525, 11529,
                    11530, 11532, 11570, 11571, 1586,
                    1588, 1590, 1592, 1594, 1596,
                    1599, 11566, 11565, 11567, 1364
            });
    private String _command;

    @Override
    protected void readImpl() {
        _command = readS();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if ((activeChar == null) || (_command.isEmpty())) {
            return;
        }
        if (_command.startsWith("_bbsbuff;buff;")) {
            StringTokenizer buffOne = new StringTokenizer(_command, ";");
            buffOne.nextToken();
            buffOne.nextToken();
            int BuffIdUse = Integer.parseInt(buffOne.nextToken());
            int BuffLvL = Integer.parseInt(buffOne.nextToken());
            if (!allowed_buff.contains(BuffIdUse)) {
                activeChar.sendMessage("You banned and disconnected!");
                _log.warn("Player: " + activeChar + " used not allow buff: " + BuffIdUse + " lvl " + BuffLvL + " - Player: " + activeChar + " BANNED!!! Packet");
                activeChar.setAccessLevel(-100); // banned (Access Level - 100)
                activeChar.kick(); // Kick player to server
                return;
            }
        }
        try {
            NpcInstance npc = activeChar.getLastNpc();
            GameObject target = activeChar.getTarget();
            if ((npc == null) && (target != null) && target.isNpc()) {
                npc = (NpcInstance) target;
            }

            if (_command.startsWith("admin_")) {
                AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, _command);
            } else if (_command.equals("come_here") && activeChar.isGM()) {
                comeHere(getClient());
            } else if (_command.startsWith("player_help ")) {
                playerHelp(activeChar, _command.substring(12));
            } else if (_command.startsWith("scripts_")) {
                String command = _command.substring(8).trim();
                String[] word = command.split("\\s+");
                String[] args = command.substring(word[0].length()).trim().split("\\s+");
                String[] path = word[0].split(":");
                if (path.length != 2) {
                    _log.warn("Bad Script bypass!");
                    return;
                }

                Map<String, Object> variables = null;
                if (npc != null) {
                    variables = new HashMap<String, Object>(1);
                    variables.put("npc", npc.getRef());
                }

                if (word.length == 1) {
                    Scripts.getInstance().callScripts(activeChar, path[0], path[1], variables);
                } else {
                    Scripts.getInstance().callScripts(activeChar, path[0], path[1], new Object[]
                            {
                                    args
                            }, variables);
                }
            } else if (_command.startsWith("user_")) {
                String command = _command.substring(5).trim();
                String word = command.split("\\s+")[0];
                String args = command.substring(word.length()).trim();
                IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(word);

                if (vch != null) {
                    vch.useVoicedCommand(word, activeChar, args);
                } else {
                    _log.warn("Unknow voiced command '" + word + "'");
                }
            } else if (_command.startsWith("npc_")) {
                int endOfId = _command.indexOf('_', 5);
                String id;
                if (endOfId > 0) {
                    id = _command.substring(4, endOfId);
                } else {
                    id = _command.substring(4);
                }
                GameObject object = activeChar.getVisibleObject(Integer.parseInt(id));
                if ((object != null) && object.isNpc() && (endOfId > 0) && activeChar.isInRange(object.getLoc(), Creature.INTERACTION_DISTANCE)) {
                    activeChar.setLastNpc((NpcInstance) object);
                    ((NpcInstance) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
                }
            } else if (_command.startsWith("_olympiad?")) {
                String[] ar = _command.replace("_olympiad?", "").split("&");
                String firstVal = ar[0].split("=")[1];
                String secondVal = ar[1].split("=")[1];

                if (firstVal.equalsIgnoreCase("move_op_field")) {
                    if (!Config.ENABLE_OLYMPIAD_SPECTATING) {
                        return;
                    }

                    // Переход в просмотр олимпа разрешен только от менеджера или с арены.
                    if (((activeChar.getLastNpc() instanceof OlympiadManagerInstance) && activeChar.getLastNpc().isInRange(activeChar, Creature.INTERACTION_DISTANCE)) || (activeChar.getOlympiadObserveGame() != null)) {
                        Olympiad.addSpectator(Integer.parseInt(secondVal) - 1, activeChar);
                    }
                }
            } else if (_command.startsWith("_diary")) {
                String params = _command.substring(_command.indexOf("?") + 1);
                StringTokenizer st = new StringTokenizer(params, "&");
                int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
                int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
                int heroid = Hero.getInstance().getHeroByClass(heroclass);
                if (heroid > 0) {
                    Hero.getInstance().showHeroDiary(activeChar, heroclass, heroid, heropage);
                }
            } else if (_command.startsWith("_match")) {
                String params = _command.substring(_command.indexOf("?") + 1);
                StringTokenizer st = new StringTokenizer(params, "&");
                int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
                int heropage = Integer.parseInt(st.nextToken().split("=")[1]);

                OlympiadHistoryManager.getInstance().showHistory(activeChar, heroclass, heropage);
            } else if (_command.startsWith("manor_menu_select?")) // Navigate throught Manor windows
            {
                GameObject object = activeChar.getTarget();
                if ((object != null) && object.isNpc()) {
                    ((NpcInstance) object).onBypassFeedback(activeChar, _command);
                }
            } else if (_command.startsWith("multisell ")) {
                MultiSellHolder.getInstance().SeparateAndSend(Integer.parseInt(_command.substring(10)), activeChar, 0);
            } else if (_command.startsWith("Quest ")) {
                String p = _command.substring(6).trim();
                int idx = p.indexOf(' ');
                if (idx < 0) {
                    activeChar.processQuestEvent(p, "", npc);
                } else {
                    activeChar.processQuestEvent(p.substring(0, idx), p.substring(idx).trim(), npc);
                }
            } else if (_command.startsWith("menu_select?")) {
                String params = _command.substring(_command.indexOf("?") + 1);
                StringTokenizer st = new StringTokenizer(params, "&");
                int ask = Integer.parseInt(st.nextToken().split("=")[1]);
                int reply = Integer.parseInt(st.nextToken().split("=")[1]);
                if (npc != null) {
                    npc.onMenuSelect(activeChar, ask, reply);
                }
            } else if (_command.startsWith("_bbs")) {
                ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(this._command);
                if (handler != null) {
                    handler.onBypassCommand(activeChar, this._command);
                } else {
                    activeChar.sendPacket(SystemMessageId.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE);
                }
            } else if (_command.startsWith("_")) {
                ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(this._command);
                if (handler != null) {
                    handler.onBypassCommand(activeChar, this._command);
                } else {
                    activeChar.sendPacket(SystemMessageId.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE);
                }
            } else {
                IBypassHandler handler = BypassHolder.getInstance().getHandler(this._command);
                if (handler != null) {
                    handler.onBypassCommand(this._command, activeChar, npc);
                } else {
                    _log.warn(getClient() + " sent not handled RequestBypassToServer: [" + this._command + "]");
                }
            }
        } catch (Exception e) {
            // _log.error("", e);
            String st = "Bad RequestBypassToServer: " + _command;
            GameObject target = activeChar.getTarget();
            if ((target != null) && target.isNpc()) {
                st = st + " via NPC #" + ((NpcInstance) target).getNpcId();
            }
            _log.error(st, e);
        }
    }

    private static void comeHere(GameClient client) {
        GameObject obj = client.getActiveChar().getTarget();
        if ((obj != null) && obj.isNpc()) {
            NpcInstance temp = (NpcInstance) obj;
            Player activeChar = client.getActiveChar();
            temp.setTarget(activeChar);
            temp.moveToLocation(activeChar.getLoc(), 0, true);
        }
    }

    private static void playerHelp(Player activeChar, String path) {
        NpcHtmlMessage html = new NpcHtmlMessage(5);
        html.setFile(path);
        activeChar.sendPacket(html);
    }
}