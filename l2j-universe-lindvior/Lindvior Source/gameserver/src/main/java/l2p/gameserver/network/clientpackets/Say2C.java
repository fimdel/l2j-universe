package l2p.gameserver.network.clientpackets;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.ExpressionTree;
import com.graphbuilder.math.VarMap;
import l2p.gameserver.Config;
import l2p.gameserver.cache.ItemInfoCache;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2p.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2p.gameserver.instancemanager.PetitionManager;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.entity.olympiad.OlympiadGame;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.matching.MatchingRoom;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.Say2;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.utils.Log;
import l2p.gameserver.utils.MapUtils;
import l2p.gameserver.utils.Strings;
import l2p.gameserver.utils.Util;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Say2C extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(Say2C.class);

    /**
     * RegExp для кэширования ссылок на предметы, пример ссылки: \b\tType=1 \tID=268484598 \tColor=0 \tUnderline=0 \tTitle=\u001BAdena\u001B\b
     */
    private static final Pattern EX_ITEM_LINK_PATTERN = Pattern.compile("[\b]\tType=[0-9]+[\\s]+\tID=([0-9]+)[\\s]+\tColor=[0-9]+[\\s]+\tUnderline=[0-9]+[\\s]+\tTitle=\u001B(.[^\u001B]*)[^\b]");
    private static final Pattern SKIP_ITEM_LINK_PATTERN = Pattern.compile("[\b]\tType=[0-9]+(.[^\b]*)[\b]");

    private String _text;
    private ChatType _type;
    private String _target;

    @Override
    protected void readImpl() {
        _text = readS(Config.CHAT_MESSAGE_MAX_LEN);
        _type = l2p.commons.lang.ArrayUtils.valid(ChatType.VALUES, readD());
        _target = _type == ChatType.TELL ? readS(Config.CNAME_MAXLEN) : null;
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (_type == null || _text == null || _text.length() == 0) {
            activeChar.sendActionFailed();
            return;
        }

        _text = _text.replaceAll("\\\\n", "\n");

        if (_text.contains("\n")) {
            String[] lines = _text.split("\n");
            _text = StringUtils.EMPTY;
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].trim();
                if (lines[i].length() == 0)
                    continue;
                if (_text.length() > 0)
                    _text += "\n  >";
                _text += lines[i];
            }
        }

        if (_text.length() == 0) {
            activeChar.sendActionFailed();
            return;
        }

        if (_text.startsWith(".")) {
            String fullcmd = _text.substring(1).trim();
            String command = fullcmd.split("\\s+")[0];
            String args = fullcmd.substring(command.length()).trim();

            if (command.length() > 0) {
                // then check for VoicedCommands
                IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
                if (vch != null) {
                    vch.useVoicedCommand(command, activeChar, args);
                    return;
                }
            }
            activeChar.sendMessage(new CustomMessage("common.command404", activeChar));
            return;
        } else if (_text.startsWith("==")) {
            String expression = _text.substring(2);
            Expression expr = null;

            if (!expression.isEmpty()) {
                try {
                    expr = ExpressionTree.parse(expression);
                } catch (ExpressionParseException epe) {

                }

                if (expr != null) {
                    double result;

                    try {
                        VarMap vm = new VarMap();
                        vm.setValue("adena", activeChar.getAdena());
                        result = expr.eval(vm, null);
                        activeChar.sendMessage(expression);
                        activeChar.sendMessage("=" + Util.formatDouble(result, "NaN", false));
                    } catch (Exception e) {

                    }
                }
            }

            return;
        }

        if (Config.CHATFILTER_MIN_LEVEL > 0 && ArrayUtils.contains(Config.CHATFILTER_CHANNELS, _type.ordinal()) && activeChar.getLevel() < Config.CHATFILTER_MIN_LEVEL)
            if (Config.CHATFILTER_WORK_TYPE == 1)
                _type = ChatType.ALL;
            else if (Config.CHATFILTER_WORK_TYPE == 2) {
                activeChar.sendMessage(new CustomMessage("chat.NotHavePermission", activeChar).addNumber(Config.CHATFILTER_MIN_LEVEL));
                return;
            }

        boolean globalchat = _type != ChatType.ALLIANCE && _type != ChatType.CLAN && _type != ChatType.PARTY && _type != ChatType.TELL;

        if (Config.TRADE_CHATS_REPLACE && globalchat)
            for (String s : Config.TRADE_WORDS)
                if (_text.contains(s)) {
                    _type = ChatType.TRADE;
                    break;
                }
        if ((globalchat || ArrayUtils.contains(Config.BAN_CHANNEL_LIST, _type.ordinal())) && activeChar.getNoChannel() != 0) {
            if (activeChar.getNoChannelRemained() > 0 || activeChar.getNoChannel() < 0) {
                if (activeChar.getNoChannel() > 0) {
                    int timeRemained = Math.round(activeChar.getNoChannelRemained() / 60000);
                    activeChar.sendMessage(new CustomMessage("common.ChatBanned", activeChar).addNumber(timeRemained));
                } else
                    activeChar.sendMessage(new CustomMessage("common.ChatBannedPermanently", activeChar));
                activeChar.sendActionFailed();
                return;
            }
            activeChar.updateNoChannel(0);
        }

        if (globalchat)
            if (Config.ABUSEWORD_REPLACE) {
                if (Config.containsAbuseWord(_text)) {
                    _text = Config.ABUSEWORD_REPLACE_STRING;
                    activeChar.sendActionFailed();
                }
            } else if (Config.ABUSEWORD_BANCHAT && Config.containsAbuseWord(_text)) {
                activeChar.sendMessage(new CustomMessage("common.ChatBanned", activeChar).addNumber(Config.ABUSEWORD_BANTIME * 60));
                Log.add(activeChar + ": " + _text, "abuse");
                activeChar.updateNoChannel(Config.ABUSEWORD_BANTIME * 60000);
                activeChar.sendActionFailed();
                return;
            }

        // Кэширование линков предметов
        Matcher m = EX_ITEM_LINK_PATTERN.matcher(_text);
        ItemInstance item;
        int objectId;

        while (m.find()) {
            objectId = Integer.parseInt(m.group(1));
            item = activeChar.getInventory().getItemByObjectId(objectId);

            if (item == null) {
                activeChar.sendActionFailed();
                break;
            }

            ItemInfoCache.getInstance().put(item);
        }

        String translit = activeChar.getVar("translit");
        if (translit != null) {
            //Исключаем из транслитерации ссылки на предметы
            m = SKIP_ITEM_LINK_PATTERN.matcher(_text);
            StringBuilder sb = new StringBuilder();
            int end = 0;
            while (m.find()) {
                sb.append(Strings.fromTranslit(_text.substring(end, end = m.start()), translit.equals("tl") ? 1 : 2));
                sb.append(_text.substring(end, end = m.end()));
            }

            _text = sb.append(Strings.fromTranslit(_text.substring(end, _text.length()), translit.equals("tl") ? 1 : 2)).toString();
        }

        Log.LogChat(_type.name(), activeChar.getName(), _target, _text);

        Say2 cs = new Say2(activeChar.getObjectId(), _type, activeChar.getName(), _text);

        switch (_type) {
            case TELL:
                Player receiver = World.getPlayer(_target);
                if (receiver != null && receiver.isInOfflineMode()) {
                    activeChar.sendMessage("The person is in offline trade mode.");
                    activeChar.sendActionFailed();
                } else if (receiver != null && !receiver.isInBlockList(activeChar) && !receiver.isBlockAll()) {
                    if (!receiver.getMessageRefusal()) {
                        if (activeChar.antiFlood.canTell(receiver.getObjectId(), _text))
                            receiver.sendPacket(cs);

                        cs = new Say2(activeChar.getObjectId(), _type, "->" + receiver.getName(), _text);
                        activeChar.sendPacket(cs);
                    } else
                        activeChar.sendPacket(Msg.THE_PERSON_IS_IN_A_MESSAGE_REFUSAL_MODE);
                } else if (receiver == null)
                    activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_CURRENTLY_LOGGED_IN).addString(_target), ActionFail.STATIC);
                else
                    activeChar.sendPacket(Msg.YOU_HAVE_BEEN_BLOCKED_FROM_THE_CONTACT_YOU_SELECTED, ActionFail.STATIC);
                break;
            case SHOUT:
                if (activeChar.isCursedWeaponEquipped()) {
                    activeChar.sendPacket(Msg.SHOUT_AND_TRADE_CHATING_CANNOT_BE_USED_SHILE_POSSESSING_A_CURSED_WEAPON);
                    return;
                }
                if (activeChar.isInObserverMode()) {
                    activeChar.sendPacket(Msg.YOU_CANNOT_CHAT_LOCALLY_WHILE_OBSERVING);
                    return;
                }

                if (!activeChar.isGM() && !activeChar.antiFlood.canShout(_text)) {
                    activeChar.sendMessage("Shout chat is allowed once per 5 seconds.");
                    return;
                }

                if (Config.GLOBAL_SHOUT)
                    announce(activeChar, cs);
                else
                    shout(activeChar, cs);

                activeChar.sendPacket(cs);
                break;
            case TRADE:
                if (activeChar.isCursedWeaponEquipped()) {
                    activeChar.sendPacket(Msg.SHOUT_AND_TRADE_CHATING_CANNOT_BE_USED_SHILE_POSSESSING_A_CURSED_WEAPON);
                    return;
                }
                if (activeChar.isInObserverMode()) {
                    activeChar.sendPacket(Msg.YOU_CANNOT_CHAT_LOCALLY_WHILE_OBSERVING);
                    return;
                }

                if (!activeChar.isGM() && !activeChar.antiFlood.canTrade(_text)) {
                    activeChar.sendMessage("Trade chat is allowed once per 5 seconds.");
                    return;
                }

                if (Config.GLOBAL_TRADE_CHAT)
                    announce(activeChar, cs);
                else
                    shout(activeChar, cs);

                activeChar.sendPacket(cs);
                break;
            case ALL:
                if (activeChar.isCursedWeaponEquipped())
                    cs = new Say2(activeChar.getObjectId(), _type, activeChar.getTransformationName(), _text);

                List<Player> list = null;

                if (activeChar.isInObserverMode() && activeChar.getObserverRegion() != null && activeChar.getOlympiadObserveGame() != null) {
                    OlympiadGame game = activeChar.getOlympiadObserveGame();
                    if (game != null)
                        list = game.getAllPlayers();
                } else if (activeChar.isInOlympiadMode()) {
                    OlympiadGame game = activeChar.getOlympiadGame();
                    if (game != null)
                        list = game.getAllPlayers();
                } else
                    list = World.getAroundPlayers(activeChar);

                if (list != null)
                    for (Player player : list) {
                        if (player == activeChar || player.getReflection() != activeChar.getReflection() || player.isBlockAll() || player.isInBlockList(activeChar))
                            continue;
                        player.sendPacket(cs);
                    }

                activeChar.sendPacket(cs);
                break;
            case CLAN:
                if (activeChar.getClan() != null)
                    activeChar.getClan().broadcastToOnlineMembers(cs);
                break;
            case ALLIANCE:
                if (activeChar.getClan() != null && activeChar.getClan().getAlliance() != null)
                    activeChar.getClan().getAlliance().broadcastToOnlineMembers(cs);
                break;
            case PARTY:
                if (activeChar.isInParty())
                    activeChar.getParty().broadCast(cs);
                break;
            case PARTY_ROOM:
                MatchingRoom r = activeChar.getMatchingRoom();
                if (r != null && r.getType() == MatchingRoom.PARTY_MATCHING)
                    r.broadCast(cs);
                break;
            case COMMANDCHANNEL_ALL:
                if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel()) {
                    activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
                    return;
                }
                if (activeChar.getParty().getCommandChannel().getChannelLeader() == activeChar)
                    activeChar.getParty().getCommandChannel().broadCast(cs);
                else
                    activeChar.sendPacket(Msg.ONLY_CHANNEL_OPENER_CAN_GIVE_ALL_COMMAND);
                break;
            case COMMANDCHANNEL_COMMANDER:
                if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel()) {
                    activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
                    return;
                }
                if (activeChar.getParty().isLeader(activeChar))
                    activeChar.getParty().getCommandChannel().broadcastToChannelPartyLeaders(cs);
                else
                    activeChar.sendPacket(Msg.ONLY_A_PARTY_LEADER_CAN_ACCESS_THE_COMMAND_CHANNEL);
                break;
            case HERO_VOICE:
                boolean PremiumHeroChat = false;
                if (Config.PREMIUM_HEROCHAT && activeChar.getNetConnection().getBonus() > 1) {
                    long endtime = activeChar.getNetConnection().getBonusExpire();
                    if (endtime >= 0)
                        PremiumHeroChat = true;
                }

                if (activeChar.isHero() || activeChar.getPlayerAccess().CanAnnounce || PremiumHeroChat) {
                    // Ограничение только для героев, гм-мы пускай говорят.
                    if (!activeChar.getPlayerAccess().CanAnnounce)
                        if (!activeChar.antiFlood.canHero(_text)) {
                            activeChar.sendMessage("Hero chat is allowed once per 10 seconds.");
                            return;
                        }
                    for (Player player : GameObjectsStorage.getAllPlayersForIterate())
                        if (!player.isInBlockList(activeChar) && !player.isBlockAll())
                            player.sendPacket(cs);
                }
                break;
            case PETITION_PLAYER:
            case PETITION_GM:
                if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar)) {
                    activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_CURRENTLY_NOT_IN_A_PETITION_CHAT));
                    return;
                }

                PetitionManager.getInstance().sendActivePetitionMessage(activeChar, _text);
                break;
            case BATTLEFIELD:
                if (activeChar.getBattlefieldChatId() == 0)
                    return;

                for (Player player : GameObjectsStorage.getAllPlayersForIterate())
                    if (!player.isInBlockList(activeChar) && !player.isBlockAll() && player.getBattlefieldChatId() == activeChar.getBattlefieldChatId())
                        player.sendPacket(cs);
                break;
            case MPCC_ROOM:
                MatchingRoom r2 = activeChar.getMatchingRoom();
                if (r2 != null && r2.getType() == MatchingRoom.CC_MATCHING)
                    r2.broadCast(cs);
                break;
            default:
                _log.warn("Character " + activeChar.getName() + " used unknown chat type: " + _type.ordinal() + ".");
        }
    }

    private static void shout(Player activeChar, Say2 cs) {
        int rx = MapUtils.regionX(activeChar);
        int ry = MapUtils.regionY(activeChar);
        int offset = Config.SHOUT_OFFSET;

        for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
            if (player == activeChar || activeChar.getReflection() != player.getReflection() || player.isBlockAll() || player.isInBlockList(activeChar))
                continue;

            int tx = MapUtils.regionX(player);
            int ty = MapUtils.regionY(player);

            if (tx >= rx - offset && tx <= rx + offset && ty >= ry - offset && ty <= ry + offset || activeChar.isInRangeZ(player, Config.CHAT_RANGE))
                player.sendPacket(cs);
        }
    }

    private static void announce(Player activeChar, Say2 cs) {
        for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
            if (player == activeChar || activeChar.getReflection() != player.getReflection() || player.isBlockAll() || player.isInBlockList(activeChar))
                continue;

            player.sendPacket(cs);
        }
    }
}
