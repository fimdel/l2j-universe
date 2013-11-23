/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.Config;
import l2p.gameserver.dao.AccountBonusDAO;
import l2p.gameserver.dao.PremiumAccountRatesHolder;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.database.mysql;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.instancemanager.AwakingManager;
import l2p.gameserver.instancemanager.QuestManager;
import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.gspackets.BonusRequest;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.actor.instances.player.Bonus;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.base.ClassLevel;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Log;
import l2p.gameserver.utils.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommunityQuests implements ScriptFile, ICommunityBoardHandler {

    private static final String[][] COLORS = {
            // BGR		 RGB	   ENG_N	RUS_N
            {"00D7FF", "FFD700", "Gold", "Золотой"},
            {"FFFF00", "00FFFF", "Light Blue", "Голубой"},
            {"97F8FC", "FCF897", "Lemon Yellow", "Желтый"},
            {"FA9AEE", "EE9AFA", "Lilac", "Сиреневый"},
            {"FF5D93", "935DFF", "Cobalt Violet", "Фиолетовый"},
            {"00FCA0", "A0FC00", "Mint Green", "Светло-зеленый"},
            {"A0A601", "01A6A0", "Peacock Green", "Темно-зеленый"},
            {"E16941", "4169E1", "Royal Blue", "Королевский синий"},
            {"486295", "956248", "Chocolate", "Шоколадный"},
            {"999999", "999999", "Silver", "Серебряный"}};

    private static final int[] class_levels = {20, 40, 76, 85};
    private static final int Ocupation1_Adena = Config.PROFF_1_COST;
    private static final int Ocupation2_Adena = Config.PROFF_2_COST;
    private static final int Ocupation3_COST_ITEM = Config.PROFF_3_COST_ITEM;
    private static final int Ocupation4_COST_ITEM = Config.PROFF_4_COST_ITEM;
    private static final int Subclasses_ITEM_ID = Config.SUB_CLASS_ITEM_ID;
    private static final int Subclasses_COST_ITEM = Config.SUB_CLASS_COST_ITEM;
    private static final int COST_ITEM_ID_NOBLESS = Config.NOBLESS_ITEM_ID;
    private static final int Nobless_COST_ITEM = Config.NOBLESS_COST_ITEM;
    private static final int PK_KARMA_ITEM_ID = Config.PK_KARMA_ITEM_ID;
    private static final int PK_KARMA_ITEM_COUNT = Config.PK_KARMA_ITEM_COUNT;
    private static final int PK_KARMA_REDUCE = Config.PK_KARMA_REDUCE;

    @Override
    public void onLoad() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().registerHandler(this);
    }

    @Override
    public void onReload() {
        if (Config.COMMUNITYBOARD_ENABLED)
            CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]
                {
                        "_cbbsquestsmain",
                        "_cbbsquestsocupation",
                        "_cbbsquestssubclasses",
                        "_cbbsquestsnoble",
                        "_cbbsquestsdecreasepk",
                        "_cbbsquestsbuysp",
                        "_cbbsquestschangepetnick",
                        "_cbbsquestsexpendinventory",
                        "_cbbsquestsexpendwarehouse",
                        "_cbbsquestsexpendclanwh",
                        "_cbbsquestschangenaneclan",
                        "_cbbsquests2changesex",
                        "_cbbsserviceshair",
                        "_cbbsserviceschangehair",
                        "_cbbsservicesnamecolor",
                        "_cbbsserviceschangenamecolor",
                        "_cbbsservicesbuypa",
                        "_bbsgeneratepalist",
                        "_cbbsquestschangenick"
                };
    }

    @Override
    public void onBypassCommand(Player player, String bypass) {
        if (!CommunityBoard.checkPlayer(player)) {
            if (player.isLangRus()) {
                player.sendMessage("Не соблюдены условия для использование данной функции");
                return;
            } else {
                player.sendMessage("You are not allowed to use this action in you current stance");
                return;
            }
        }

        String html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/quests.htm", player);
        String content = "";
        if (bypass.startsWith("_cbbsquestsmain"))
            content = html(player);
        StringTokenizer st2 = new StringTokenizer(bypass, "_");
        String cmd = st2.nextToken();
        if (bypass.startsWith("_cbbsserviceshair")) {
            html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/change_hair.htm", player);

            String buttons = "";
            int hairStyle = player.getHairStyle();
            if (player.getSex() == 0) {
                switch (hairStyle) {
                    case 5:
                        buttons += "<tr><td><button value=\"%type% G\" action=\"bypass _cbbsserviceschangehair:6\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                        break;
                    case 6:
                        buttons += "<tr><td><button value=\"%type% F\" action=\"bypass _cbbsserviceschangehair:5\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                        break;
                    default:
                        buttons += "<tr><td><button value=\"%type% F\" action=\"bypass _cbbsserviceschangehair:5\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                        buttons += "<tr><td><button value=\"%type% G\" action=\"bypass _cbbsserviceschangehair:6\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                        break;
                }
            } else {
                if (player.getRace() == Race.kamael) {
                    switch (hairStyle) {
                        case 8:
                            buttons += "<tr><td><button value=\"%type% I\" action=\"bypass _cbbsserviceschangehair:9\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                        case 9:
                            buttons += "<tr><td><button value=\"%type% H\" action=\"bypass _cbbsserviceschangehair:8\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                        default:
                            buttons += "<tr><td><button value=\"%type% H\" action=\"bypass _cbbsserviceschangehair:8\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            buttons += "<tr><td><button value=\"%type% I\" action=\"bypass _cbbsserviceschangehair:9\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                    }
                } else {
                    switch (hairStyle) {
                        case 7:
                            buttons += "<tr><td><button value=\"%type% I\" action=\"bypass _cbbsserviceschangehair:8\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                        case 8:
                            buttons += "<tr><td><button value=\"%type% H\" action=\"bypass _cbbsserviceschangehair:7\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                        default:
                            buttons += "<tr><td><button value=\"%type% H\" action=\"bypass _cbbsserviceschangehair:7\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            buttons += "<tr><td><button value=\"%type% I\" action=\"bypass _cbbsserviceschangehair:8\" width=250 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                            break;
                    }
                }
            }

            if (player.isLangRus())
                buttons = buttons.replace("%type%", "Тип");
            else
                buttons = buttons.replace("%type%", "Type");

            html = html.replace("<?buttons?>", buttons);
            ShowBoard.separateAndSend(html, player);
            return;
        } else if (bypass.startsWith("_cbbsserviceschangehair")) {
            if (Functions.getItemCount(player, Config.BBS_HAIRSTYLE_ITEM_ID) < Config.BBS_HAIRSTYLE_ITEM_COUNT) {
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
                onBypassCommand(player, "_cbbsserviceshair");
                return;
            }
            Functions.removeItem(player, Config.BBS_HAIRSTYLE_ITEM_ID, Config.BBS_HAIRSTYLE_ITEM_COUNT);

            String[] b = bypass.split(":");
            int type = Integer.parseInt(b[1]);
            player.setHairStyle(type);
            int hairStyle = player.getHairStyle();
            if ((player.getSex() == 0 && (hairStyle == 5 || hairStyle == 6)) ||
                    (player.getSex() == 1 && player.getRace() != Race.kamael && (hairStyle == 7 || hairStyle == 8)) ||
                    (player.getSex() == 1 && player.getRace() == Race.kamael && (hairStyle == 8 || hairStyle == 9)))
                player.setHairColor(0);
            player.broadcastPacket(new MagicSkillUse(player, player, 23128, 1, 1, 0));
            player.broadcastCharInfo();
            onBypassCommand(player, "_cbbsserviceshair");
            return;
        } else if (bypass.startsWith("_cbbsservicesnamecolor")) {
            html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/change_name_color.htm", player);

            String buttons = "";
            int i = 1;
            for (String[] color : COLORS) {
                buttons += "<tr>";
                String color_rgb = color[1];
                buttons += "<td width=25></td><td><font color=\"" + color_rgb + "\">|||||||||||||||||||||||||||||||||||||</font></td><td width=25></td></tr>";
                if (i % 2 == 0)
                    buttons += "<tr><td></td></tr>\n<?color_" + i + "?>";
                i++;
            }
            i = 1;
            for (String[] color : COLORS) {
                String color_str = "";
                color_str += "<tr>";
                String color_bgr = color[0];
                String color_name = player.isLangRus() ? color[3] : color[2];
                color_str += "<td align=center><button value=\"%color% " + color_name + "\" action=\"bypass _cbbsserviceschangenamecolor:" + color_bgr + "\" width=250 height=25 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                if (i % 2 == 0) {
                    color_str += "<tr><td></td></tr>";
                    buttons = buttons.replace("<?colors_" + i + "?>", color_str);
                } else {
                    color_str += "<?colors_" + (i + 1) + "?>";
                    buttons = buttons.replace("<?color_" + (i + 1) + "?>", color_str);
                }
                i++;
            }

            if (player.isLangRus())
                buttons = buttons.replace("%color%", "Цвет:");
            else
                buttons = buttons.replace("%color%", "Color:");

            html = html.replace("<?buttons?>", buttons);
            ShowBoard.separateAndSend(html, player);
            return;
        } else if (bypass.startsWith("_cbbsserviceschangenamecolor")) {
            String[] b = bypass.split(":");
            int color = Integer.decode("0x" + b[1]);
            if (color != player.getNameColor()) {
                if (Integer.decode("0xFFFFFF") != color) {
                    if (Functions.getItemCount(player, Config.BBS_NAME_COLOR_ITEM_ID) < Config.BBS_NAME_COLOR_ITEM_COUNT) {
                        player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
                        onBypassCommand(player, "_cbbsservicesnamecolor");
                        return;
                    }
                }
                Functions.removeItem(player, Config.BBS_NAME_COLOR_ITEM_ID, Config.BBS_NAME_COLOR_ITEM_COUNT);
                player.setNameColor(color);
                player.broadcastPacket(new MagicSkillUse(player, player, 23128, 1, 1, 0));
                player.broadcastUserInfo(true);
            }
            onBypassCommand(player, "_cbbsservicesnamecolor");
            return;
        } else if (bypass.startsWith("_bbsgeneratepalist")) {
            html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa.htm", player);
            CopyOnWriteArrayList list = PremiumAccountRatesHolder.getAllAquisions();

            if (list == null || list.isEmpty()) {
                html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa-no.htm", player);
                ShowBoard.separateAndSend(html, player);
                return;
            }
            String subContent = "";
            if (player.isLangRus())
                subContent += "Доступные схемы:";
            else
                subContent += "Avaliable schemes<br>";

            for (PremiumAccountRatesHolder.PremiumInfo info2 : PremiumAccountRatesHolder.getAllAquisions()) {
                int _type = info2.getGroupNumber();
                //type is the default!!!
                if (_type == 1)
                    continue;
                String _groupName_ru = info2.getGroupNameRu();
                String _groupName_en = info2.getGroupNameEn();
                double _xp = info2.getExp();
                double _sp = info2.getSp();
                double _adena = info2.getAdena();
                double _drop = info2.getDrop();
                double _spoil = info2.getSpoil();
                double _qDrop = info2.getQDrop();
                double _qReward = info2.getQReward();
                long _time = info2.getDays();
                String delay = info2.isHours() ? player.isLangRus() ? "Часов" : "Hours" : player.isLangRus() ? "Дней" : "Days";

                if (player.isLangRus())
                    subContent += "Схема " + _groupName_ru + " - Бонусы - EXP x" + _xp + ", SP x" + _sp + ",Adena x" + _adena + ", Drop x" + _drop + ", Spoil x" + _spoil + ", QuestDrop x" + _qDrop + ", QuestReward x" + _qReward + " На " + _time + " " + delay + "<br>";
                else
                    subContent += "Scheme " + _groupName_en + " - Bonuses - EXP x" + _xp + ", SP x" + _sp + ",Adena x" + _adena + ", Drop x" + _drop + ", Spoil x" + _spoil + ", QuestDrop x" + _qDrop + ", QuestReward x" + _qReward + " for " + _time + " " + delay + "<br>";
            }

            String content2 = "";
            for (PremiumAccountRatesHolder.PremiumInfo info : PremiumAccountRatesHolder.getAllAquisions()) {
                int _type = info.getGroupNumber();
                //type is the default!!!
                if (_type == 1)
                    continue;
                String _groupName_ru = info.getGroupNameRu();
                String _groupName_en = info.getGroupNameEn();
                int _days = info.getDays();
                int _itemId = info.getItemId();
                String _itemName = getItemName(_itemId, true);
                long _itemCount = info.getItemCount();
                String delay = info.isHours() ? player.isLangRus() ? "Часов" : "Hours" : player.isLangRus() ? "Дней" : "Days";

                if (player.isLangRus())
                    content2 += "<tr><td><button value=\"Купить [Схема " + _groupName_ru + "] на " + _days + " " + delay + " [" + _itemCount + " " + _itemName + "]\" action=\"bypass _cbbsservicesbuypa_" + _type + "_" + _days + "_" + _itemId + "_" + _itemCount + "\" width=300 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
                else
                    content2 += "<tr><td><button value=\"Buy [Scheme " + _groupName_en + "] for " + _days + " " + delay + " [" + _itemCount + " " + _itemName + "]\" action=\"bypass _cbbsservicesbuypa_" + _type + "_" + _days + "_" + _itemId + "_" + _itemCount + "\" width=300 height=35 back=\"l2ui_ct1.button_df_small_down\" fore=\"l2ui_ct1.button_df_small\"></td></tr>";
            }
            html = html.replaceFirst("%ganerated_schemes%", subContent);
            html = html.replaceFirst("%ganerated_list%", content2);

            ShowBoard.separateAndSend(html, player);
            return;

        } else if (bypass.startsWith("_cbbsservicesbuypa")) {
            if (Config.SERVICES_RATE_TYPE == Bonus.NO_BONUS)
                html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa-no.htm", player);
            else if (Config.SERVICES_RATE_TYPE == Bonus.BONUS_GLOBAL_ON_LOGINSERVER && LoginServerCommunication.getInstance().isShutdown())
                html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa-login.htm", player);
            if (player.getBonus().getBonusExpire() > 0) {
                html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa-already.htm", player);
                html = html.replace("%date_expire%", String.valueOf(new Timestamp(player.getBonus().getBonusExpire() * 1000)));
            } else {
                //html = HtmCache.getInstance().getNotNull("scripts/services/community/pages/buy_pa.htm", player);

                int bonus = Integer.parseInt(st2.nextToken());
                int period = Integer.parseInt(st2.nextToken());
                int itemId = Integer.parseInt(st2.nextToken());
                int itemCount = Integer.parseInt(st2.nextToken());
                if (Functions.getItemCount(player, itemId) >= itemCount && PremiumAccountRatesHolder.validateGroup(bonus, period, itemId, itemCount)) {
                    int startTime = (int) (System.currentTimeMillis() / 1000);
                    if (player.getNetConnection().getBonus() >= 1.) {
                        int endTime = player.getNetConnection().getBonusExpire();
                        if (endTime >= System.currentTimeMillis() / 1000L)
                            startTime = endTime;
                    }
                    boolean isHours = PremiumAccountRatesHolder.getIfDelayHours(bonus);
                    int bonusExpire;

                    if (isHours)
                        bonusExpire = startTime + period * 60 * 60;
                    else
                        bonusExpire = startTime + period * 24 * 60 * 60;

                    switch (Config.SERVICES_RATE_TYPE) {
                        case Bonus.BONUS_GLOBAL_ON_LOGINSERVER:
                            LoginServerCommunication.getInstance().sendPacket(new BonusRequest(player.getAccountName(), bonus, bonusExpire));
                            break;
                        case Bonus.BONUS_GLOBAL_ON_GAMESERVER:
                            AccountBonusDAO.getInstance().insert(player.getAccountName(), bonus, bonusExpire);
                            break;
                    }

                    player.getNetConnection().setBonus(bonus);
                    player.getNetConnection().setBonusExpire(bonusExpire);

                    player.stopBonusTask();
                    player.startBonusTask();

                    if (player.getParty() != null)
                        player.getParty().recalculatePartyData();

                    Functions.removeItem(player, itemId, itemCount);
                    player.broadcastPacket(new MagicSkillUse(player, player, 23128, 1, 1, 0));
                    player.sendPacket(new ExBR_PremiumState(player, true));
                } else
                    player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
            }
            onBypassCommand(player, "_bbsgeneratepalist");
            //ShowBoard.separateAndSend(html, player);
            return;
        } else {
            StringTokenizer bf = new StringTokenizer(bypass, " ");
            bf.nextToken();
            String[] arg = new String[0];
            while (bf.hasMoreTokens())
                arg = ArrayUtils.add(arg, bf.nextToken());

            if (bypass.startsWith("_cbbsquestsocupation"))
                content = getOcupation(arg, player);
            else if (bypass.startsWith("_cbbsquestssubclasses"))
                content = getSubs(arg, player);
            else if (bypass.startsWith("_cbbsquestsnoble"))
                content = getNobless(arg, player);
            else if (bypass.startsWith("_cbbsquestsdecreasepk"))
                content = decreasePK(player);
            else if (bypass.startsWith("_cbbsquestsbuysp"))
                content = buySP(arg, player);


            else if (bypass.startsWith("_cbbsquestschange")) {
                StringTokenizer st = new StringTokenizer(bypass, "_");
                st.nextToken();
                if (!st.hasMoreTokens()) {
                    html(player);
                    return;
                }
                String bmTemp = st.nextToken();
                String bmName = bmTemp.replaceAll(" ", "");
                if (bmName.equals(" ") || bmName.isEmpty() || !Util.isMatchingRegexp(bmName, Config.CNAME_TEMPLATE) || mysql.simple_get_int("count(*)", "characters", "`char_name` like '" + bmName + "'") > 0) {
                    if (player.isLangRus())
                        player.sendMessage("Вы не внесли имя!");
                    else
                        player.sendMessage("Please insert new name!");
                    html(player);
                    return;
                }
                if (bypass.startsWith("_cbbsquestschangenick"))
                    content = setNewNick(player, bmName, 1);
                else if (bypass.startsWith("_cbbsquestschangenaneclan"))
                    content = setNewNick(player, bmName, 2);
                else if (bypass.startsWith("_cbbsquestschangepetnick"))
                    content = setNewNick(player, bmName, 3);

            } else if (bypass.startsWith("_cbbsquestsexpendinventory"))
                content = ExpandInventory(player);
            else if (bypass.startsWith("_cbbsquestsexpendwarehouse"))
                content = ExpandWareHouse(player);
            else if (bypass.startsWith("_cbbsquestsexpendclanwh"))
                content = ExpandClanWH(player);

            else if (bypass.startsWith("_cbbsquests2changesex"))
                content = DoChangeSex(player);
        }
        html = html.replace("%content%", content);
        ShowBoard.separateAndSend(html, player);
    }

    public String DoChangeSex(Player player) {
        int item_need = Config.CHANGE_SEX_ITEM_ID;
        int item_count = Config.CHANGE_SEX_ITEM_COUNT;

        if (!checkHaveItem(player, item_need, item_count))
            return html(player);

        Functions.removeItem(player, item_need, item_count);

        player.changeSex();
        player.sendMessage("Поздравляем ваш пол изменился!");
        player.broadcastUserInfo(true);

        return html(player);
    }

    public String ExpandClanWH(Player player) {
        if (player.getClan() == null) {
            player.sendMessage("You must be in clan.");
            return html(player);
        }

        int item_need = Config.EXPAND_CLAN_WH_ITEM_ID;
        int item_count = Config.EXPAND_CLAN_WH_ITEM_COUNT;
        if (!checkHaveItem(player, item_need, item_count))
            return html(player);

        Functions.removeItem(player, item_need, item_count);
        if (player.getClan().getWhBonus() == -1 && Config.CLAN_WH_VALUE == 1)
            player.getClan().setWhBonus(Config.CLAN_WH_VALUE);
        else
            player.getClan().setWhBonus(player.getClan().getWhBonus() + Config.CLAN_WH_VALUE);

        player.sendMessage("Warehouse capacity is now " + (Config.WAREHOUSE_SLOTS_CLAN + player.getClan().getWhBonus()));
        return html(player);
    }

    public String ExpandWareHouse(Player player) {
        if (player.getWarehouseLimit() >= Config.SERVICES_EXPAND_INVENTORY_MAX) {
            player.sendMessage("Already max count.");
            return html(player);
        }

        int item_need = Config.EXPAND_WH_ITEM_ID;
        int item_count = Config.EXPAND_WH_ITEM_COUNT;
        if (!checkHaveItem(player, item_need, item_count))
            return html(player);

        Functions.removeItem(player, item_need, item_count);
        player.setExpandWarehouse(player.getExpandWarehouse() + Config.EXPEND_WH_VALUE);
        player.setVar("ExpandWarehouse", String.valueOf(player.getExpandWarehouse()), -1);
        player.sendMessage("Warehouse capacity is now " + player.getWarehouseLimit());
        return html(player);
    }

    public String ExpandInventory(Player player) {
        if (player.getInventoryLimit() >= Config.SERVICES_EXPAND_INVENTORY_MAX) {
            player.sendMessage("Already max count.");
            return html(player);
        }

        int item_need = Config.EXPAND_INVENTORY_ITEM_ID;
        int item_count = Config.EXPAND_INVENTORY_ITEM_COUNT;
        if (!checkHaveItem(player, item_need, item_count))
            return html(player);

        Functions.removeItem(player, item_need, item_count);
        player.setExpandInventory(player.getExpandInventory() + Config.EXPAND_INV_VALUE);
        player.setVar("ExpandInventory", String.valueOf(player.getExpandInventory()), -1);
        player.sendMessage("Inventory capacity is now " + player.getInventoryLimit());
        return html(player);
    }

    public String setNewNick(Player player, String newName, int type) {
        if (type == 2)
            if (player.getClan() == null || !player.isClanLeader())
                return html(player);
        if (type == 3) {
            if (player.getSummonList().getServitors().size() == 0)
                return html(player);
        }
        int item_need = 0;
        int item_count = 0;
        switch (type) {
            case 1:
                item_need = Config.CHANGE_NICK_ITEM_ID;
                item_count = Config.CHANGE_NICK_ITEM_COUNT;
                if (!checkHaveItem(player, item_need, item_count))
                    return html(player);
                Functions.removeItem(player, item_need, item_count);
                player.reName(newName, true);
                break;
            case 2:
                item_need = Config.CHANGE_NAME_CLAN_ITEM_ID;
                item_count = Config.CHANGE_NAME_CLAN_ITEM_COUNT;
                if (!checkHaveItem(player, item_need, item_count))
                    return html(player);
                Functions.removeItem(player, item_need, item_count);
                player.getClan().getSubUnit(Clan.SUBUNIT_MAIN_CLAN).setName(newName, true);
                player.getClan().updateClanInDB();
                player.getClan().broadcastClanStatus(true, true, true);
                break;
            case 3:
                PetInstance pet = (PetInstance) player.getPet();
                item_need = Config.CHANGE_NICK_PET_ITEM_ID;
                item_count = Config.CHANGE_NICK_PET_ITEM_COUNT;
                if (!checkHaveItem(player, item_need, item_count))
                    return html(player);
                Functions.removeItem(player, item_need, item_count);
                pet.setName(newName);
                pet.broadcastCharInfo();
                pet.updateControlItem();
                break;
            default:
                return html(player);

        }
        return html(player);

    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }

    private String html(Player player) {
        String result = "";
        if (Config.ALLOW_OCCUPATION)
            result += tableOcupation(player);
        if (Config.ALLOW_SUB_CLASSES)
            result += tableSubclasses(player);
        if (Config.ALLOW_NOBLESS)
            result += tableNobless(player);
        if (Config.ALLOW_SP_ADD)
            result += tableSP(player);
        if (Config.ALLOW_KARMA_PK)
            result += tableKarmaPK(player);
        //new
        if (Config.ALLOW_CHANGE_NAME)
            result += tableChangeNickPC(player);
        if (Config.ALLOW_CHANCE_PET_NAME)
            result += tableChangeNickPet(player);
        if (Config.ALLOW_CHANGE_CLAN_NAME)
            result += tableChangeClanName(player);
        if (Config.ALLOW_EXPEND_INVENTORY)
            result += tableExpendInventory(player);
        if (Config.ALLOW_EXPEND_WAREHOUSE)
            result += tableExpendWarehouse(player);
        if (Config.ALLOW_EXPEND_CLAN_WH)
            result += tableExpendClanWh(player);
        if (Config.ALLOW_SEX_CHANGE && player.getRace() != Race.kamael)
            result += tableChangeSex(player);
        if (Config.ALLOW_HAIR_STYLE_CHANGE)
            result += tableHairStyleChange(player);
        if (Config.ALLOW_COLOR_NICK_CHANGE)
            result += tableColorChange(player);
        return result;
    }

    //new impl
    private static String tableChangeNickPC(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 19), 280, 22, "bypass _cbbsquestschangenick_ $newfname1") + "</td>";
        result += "<td><edit var=\"newfname1\" width=150></td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableChangeNickPet(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 20), 280, 22, "bypass _cbbsquestschangepetnick_ $newfname2") + "</td>";
        result += "<td><edit var=\"newfname2\" width=150></td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableExpendInventory(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 21), 280, "_cbbsquestsexpendinventory", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableExpendWarehouse(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 22), 280, "_cbbsquestsexpendwarehouse", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableExpendClanWh(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 23), 280, "_cbbsquestsexpendclanwh", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableChangeSex(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 50), 280, "_cbbsquests2changesex", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableHairStyleChange(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 51), 280, "_cbbsserviceshair", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableColorChange(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 52), 280, "_cbbsservicesnamecolor", 1) + "</td>";
        result += "</tr></table><br><br>";
        return result;
    }

    private static String tableChangeClanName(Player player) {
        String result = "<table><tr><td>" + htmlButton(localize(player, 24), 280, 22, "bypass _cbbsquestschangenaneclan_ $newfname3") + "</td>";
        result += "<td><edit var=\"newfname3\" width=150></td>";
        result += "</tr></table><br><br>";
        return result;
    }

    public static String htmlButton(String value, int width, int height, String function, Object... args) {
        String action = "bypass " + function;
        for (Object arg : args)
            action += " " + arg.toString();
        return HtmlUtils.htmlButton(value, action, width, height);
    }

    public static String htmlButton(String value, int width, int height, String function) {
        return HtmlUtils.htmlButton(value, function, width, height);
    }

    public static String htmlButton(String value, int width, String function, Object... args) {
        return htmlButton(value, width, 22, function, args);
    }

    private static final void CompleteQuest(String name, Player player) {
        Quest _quest = QuestManager.getQuest2(name);
        QuestState qs = player.getQuestState(_quest.getName());

        if (qs == null)
            qs = _quest.newQuestState(player, Quest.COMPLETED);
        qs.setState(Quest.COMPLETED);
    }

    private static boolean checkHaveItem(Player player, int itemId, long count) {
        if (Functions.getItemCount(player, itemId) < count) {
            if (itemId == 57)
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
            else
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
            return false;
        }
        return true;
    }

    /**
     * *****************************************************************************
     */

    private static ArrayList<ClassId> getAvailClasses(ClassId playerClass) {
        ArrayList<ClassId> result = new ArrayList<ClassId>();
        for (ClassId _class : ClassId.values()) {

            if (_class.getClassLevel().ordinal() == playerClass.getClassLevel().ordinal() + 1 && _class.childOf(playerClass) && _class != ClassId.INSPECTOR)
                result.add(_class);
        }
        return result;
    }

    private String getOcupation(String[] var, Player player) {
        ClassId playerClass = player.getClassId();
        if (playerClass.isOfLevel(ClassLevel.AWAKED))
            return html(player);

        int need_level = class_levels[playerClass.getClassLevel().ordinal()];
        if (player.getLevel() < need_level)
            return html(player);

        int RequestClass = Integer.parseInt(var[0]);
        ClassId RequestClassId = null;
        ArrayList<ClassId> avail_classes = getAvailClasses(playerClass);
        for (ClassId _class : avail_classes)
            if (_class.getId() == RequestClass) {
                RequestClassId = _class;
                break;
            }
        if (RequestClassId == null && RequestClass != -500)
            return html(player);

        int need_item_id = 0;
        int need_item_count = 0;

        switch (playerClass.getClassLevel()) {
            case NONE:
                need_item_id = Config.OCCUPATION1_COST_ITEM;
                need_item_count = Ocupation1_Adena;
                break;
            case FIRST:
                need_item_id = Config.OCCUPATION2_COST_ITEM;
                need_item_count = Ocupation2_Adena;
                break;
            case SECOND:
                need_item_id = Config.OCCUPATION3_COST_ITEM;
                need_item_count = Ocupation3_COST_ITEM;
                break;
            case THIRD:
                need_item_id = Ocupation4_COST_ITEM;
                need_item_count = Config.OCCUPATION4_COST_ITEM;
                break;
        }

        if (need_item_id == 0 || need_item_count == 0)
            return html(player);

        if (need_item_id == -300) {
            if (!reducePoints(player, need_item_count))
                return html(player);
        } else {
            if (!checkHaveItem(player, need_item_id, need_item_count))
                return html(player);

            Functions.removeItem(player, need_item_id, need_item_count);
        }
        //Log.add("QUEST\tСмена професии " + playerClass.getId() + " -> " + RequestClassId.getId() + " за " + need_item_id + ":" + need_item_count, "service_quests", player);

        player.sendPacket(new SystemMessage(SystemMessage.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS));

        if (RequestClass == -500) {
            int _reqClass = -1;
            for (ClassId cid : ClassId.VALUES) {
                if (cid.childOf(player.getClassId()) && cid.getClassLevel().ordinal() == player.getClassId().getClassLevel().ordinal() + 1)
                    _reqClass = cid.getId();
            }
            if (_reqClass == -1)
                player.sendMessage("Something gone wrong, please contact administrator!");

            ItemFunctions.addItem(player, AwakingManager.SCROLL_OF_AFTERLIFE, 1, true);
            player.teleToLocation(AwakingManager.TELEPORT_LOC);
        } else
            player.setClassId(RequestClass, false);
        player.broadcastUserInfo(true);

        return html(player);
    }

    private static String tableOcupation(Player player) {
        ClassId playerClass = player.getClassId();
        String playerClassName = getClassIdSysstring(player, playerClass.getId());
        String result = localize(player, 3) + ": <font color=LEVEL>" + playerClassName + "</font>";

        if (playerClass.isOfLevel(ClassLevel.AWAKED))
            return result + "<br>";

        int need_level = class_levels[player.getClassLevel()];
        if (player.getLevel() < need_level)
            return result + "<br1>" + localize(player, 4) + ": " + need_level + "<br>";

        ArrayList<ClassId> avail_classes = getAvailClasses(playerClass);
        if (avail_classes.size() == 0 && playerClass.getClassLevel() != ClassLevel.THIRD)
            return result + "<br>";

        result += "<table><tr>";

        if (playerClass.getClassLevel() == ClassLevel.THIRD) {
            if (Config.ALLOW_FOURTH_OCCUPATION)
                result += "<td>" + htmlButton(localize(player, 99, "Awakening", Ocupation4_COST_ITEM), 250, "_cbbsquestsocupation", -500) + "</td>";

        }

        for (ClassId _class : avail_classes) {
            String _className = getClassIdSysstring(player, _class.getId());
            switch (playerClass.getClassLevel()) {
                case NONE:
                    if (Config.ALLOW_FIRST_OCCUPATION)
                        result += "<td>" + htmlButton(localize(player, 5, _className, Ocupation1_Adena), 250, "_cbbsquestsocupation", _class.getId()) + "</td>";
                    break;
                case FIRST:
                    if (Config.ALLOW_SECOND_OCCUPATION)
                        result += "<td>" + htmlButton(localize(player, 18, _className, Ocupation2_Adena), 250, "_cbbsquestsocupation", _class.getId()) + "</td>";
                    break;
                case SECOND:
                    if (Config.ALLOW_THIRD_OCCUPATION)
                        result += "<td>" + htmlButton(localize(player, 6, _className, Ocupation3_COST_ITEM), 300, "_cbbsquestsocupation", _class.getId(), 1) + "</td>";
                    break;
            }
        }
        result += "</tr></table><br><br>";

        return result;
    }

    /**
     * *****************************************************************************
     */

    public String getSubs(String[] var, Player player) {
        if (player.getLevel() < 75)
            return localize(player, 7);

        int need_item_id = Subclasses_ITEM_ID;
        int need_item_count = Subclasses_COST_ITEM;

        if (need_item_id == -300) {
            if (!reducePoints(player, need_item_count))
                return html(player);
        } else {
            if (!checkHaveItem(player, need_item_id, need_item_count))
                return html(player);

            Functions.removeItem(player, need_item_id, need_item_count);
        }
        Log.add("QUEST\tАктивация сабклассов за " + need_item_id + ":" + need_item_count, "service_quests", player);

        CompleteQuest("_234_FatesWhisper", player);
        CompleteQuest(player.getRace() == Race.kamael ? "_236_SeedsOfChaos" : "_235_MimirsElixir", player);

        return html(player);
    }

    private static String tableSubclasses(Player player) {
        QuestState qs = player.getQuestState("_234_FatesWhisper");
        if (qs == null || !qs.isCompleted())
            return player.getLevel() < 75 ? localize(player, 7) : "<table><tr><td>" + htmlButton(localize(player, 8), 280, "_cbbsquestssubclasses", 0) + "</td></tr></table><br><br>";

        qs = player.getQuestState(player.getRace() == Race.kamael ? "_236_SeedsOfChaos" : "_235_MimirsElixir");
        if (qs == null || !qs.isCompleted())
            return player.getLevel() < 75 ? localize(player, 7) : "<table><tr><td>" + htmlButton(localize(player, 8), 280, "_cbbsquestssubclasses", 0) + "</td></tr></table><br><br>";

        return localize(player, 9) + "<br>";
    }

    /**
     * *****************************************************************************
     */

    public String getNobless(String[] var, Player player) {
        if (player.isNoble())
            return localize(player, 11);

        if (player.getSubLevel() < 75)
            return localize(player, 10);

        int need_item_id = COST_ITEM_ID_NOBLESS;
        int need_item_count = Nobless_COST_ITEM;

        if (need_item_id == -300) {
            if (!reducePoints(player, need_item_count))
                return html(player);
        } else {
            if (!checkHaveItem(player, need_item_id, need_item_count))
                return html(player);

            Functions.removeItem(player, need_item_id, need_item_count);
        }
        Log.add("QUEST\tПолучение дворянства за " + need_item_id + ":" + need_item_count, "service_quests", player);

        Olympiad.addNoble(player);
        player.setNoble(true);
        player.updatePledgeClass();
        player.sendPacket(new SkillList(player));
        player.broadcastPacket(new SocialAction(player.getObjectId(), 16));
        player.broadcastUserInfo(true);

        return html(player);
    }

    private static String tableNobless(Player player) {
        if (player.isNoble())
            return localize(player, 11);

        if (player.getSubLevel() < 75)
            return localize(player, 10);

        return "<table><tr><td>" + htmlButton(localize(player, 13), 280, "_cbbsquestsnoble", 1) + "</td></tr></table><br><br>";
    }

    /**
     * *****************************************************************************
     */

    public String decreasePK(Player player) {
        if (player.isCursedWeaponEquipped())
            return "";

        if (PK_KARMA_ITEM_ID == -300) {
            if (!reducePoints(player, 10)) {
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
                return html(player);
            }
        } else {
            if (Functions.getItemCount(player, PK_KARMA_ITEM_ID) < PK_KARMA_ITEM_COUNT) {
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
                return html(player);
            }

            Functions.removeItem(player, PK_KARMA_ITEM_ID, PK_KARMA_ITEM_COUNT);
        }
        Log.add("QUEST\tИскупление грехов за 1 COL: " + player.getPkKills() + " PK и " + player.getKarma() + " кармы", "service_quests", player);
        if (player.getKarma() > 0)
            player.setKarma(0);
        else
            player.setPkKills(player.getPkKills() - PK_KARMA_REDUCE);
        player.sendUserInfo(true);

        return html(player);
    }

    private static String tableKarmaPK(Player player) {
        if (player.isCursedWeaponEquipped())
            return "";

        String result = "";
        if (player.getPkKills() > 0) {
            result += player.getPkKills() + " PK";
            if (player.getKarma() > 0)
                result += " & " + player.getKarma() + " karma";
        } else if (player.getKarma() > 0)
            result += player.getKarma() + " karma";
        else
            return "";

        return "<table><tr><td><font color=FF0000>" + result + "</font></td><td>" + htmlButton(localize(player, 2), 250, "_cbbsquestsdecreasepk") + "</td></tr></table><br><br>";
    }

    /**
     * *****************************************************************************
     */

    public String buySP(String[] var, Player player) {
        if (player.getSp() + Config.SERVICE_SP_ADD > Integer.MAX_VALUE) {
            player.sendMessage(localize(player, 17));
            return html(player);
        }

        int need_item_id = Config.SERVICE_SP_ITEM_ID;
        int need_item_count = Config.SERVICE_SP_ITEM_COUNT;

        if (need_item_id == -300) {
            if (!reducePoints(player, need_item_count)) {
                player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
                return html(player);
            }
        } else {
            if (!checkHaveItem(player, need_item_id, need_item_count))
                return html(player);

            Functions.removeItem(player, need_item_id, need_item_count);
        }

        player.addExpAndSp(1, Config.SERVICE_SP_ADD);
        player.sendUserInfo(true);

        return html(player);
    }

    private static String tableSP(Player player) {
        return "<table><tr><td>" + htmlButton(localize(player, 15), 260, "_cbbsquestsbuysp", 0) + "</td></tr></table><br><br>";
    }

    /**
     * *****************************************************************************
     */

    public static String localize(Player player, int ID, Object... args) {
        boolean ru = player.isLangRus();
        switch (ID) {
            case 2:
                return ru ? "-" + PK_KARMA_REDUCE + " PK или очистить карму за " + PK_KARMA_ITEM_COUNT + " " + getItemName(PK_KARMA_ITEM_ID, true) + " " : "-" + PK_KARMA_REDUCE + " PK or clear karma for " + PK_KARMA_ITEM_COUNT + " " + getItemName(PK_KARMA_ITEM_ID, false) + "";
            case 3:
                return ru ? "Ваша текущая профессия" : "Your current occupation";
            case 4:
                return ru ? "Для получения следующей профессии вы должны достичь уровня" : "To get your's next occupation you should reach level";
            case 5:
                return ru ? "Стать " + (String) args[0] + " за " + (Integer) args[1] + " " + getItemName(Config.OCCUPATION1_COST_ITEM, true) + "" : "Become " + (String) args[0] + " (" + (Integer) args[1] + " " + getItemName(Config.OCCUPATION1_COST_ITEM, false) + ")";
            case 6:
                return ru ? "Стать " + (String) args[0] + " за " + (Integer) args[1] + " " + getItemName(Config.OCCUPATION3_COST_ITEM, true) + "" : "Become " + (String) args[0] + " (" + (Integer) args[1] + " " + getItemName(Config.OCCUPATION3_COST_ITEM, false) + ")";
            case 7:
                return ru ? "Для активации сабклассов вы должны достичь 75-ого уровня<br>" : "To activate subclasses you should reach level 75<br>";
            case 8:
                return ru ? "Активировать сабклассы за " + Subclasses_COST_ITEM + " " + getItemName(Subclasses_ITEM_ID, true) + "" : "Activate subclasses for " + Subclasses_COST_ITEM + " " + getItemName(Subclasses_ITEM_ID, false) + "";
            case 9:
                return ru ? "У вашего персонажа активна возможность получения сабклассов. Посетите Великого Магистра." : "Visit Grand Master to manage subclasses.";
            case 10:
                return ru ? "Что бы стать дворянином вы должны прокачать сабкласс до 75 уровня.<br>" : "To become Noblesse your subclass should reach level 75.<br>";
            case 11:
                return ru ? "Ваш персонаж является Дворянином.<br>" : "You are already Noblesse.<br>";
            case 13:
                return ru ? "Получить дворянство за " + Nobless_COST_ITEM + " " + getItemName(COST_ITEM_ID_NOBLESS, true) + "" : "Become Noblesse (" + Nobless_COST_ITEM + " " + getItemName(COST_ITEM_ID_NOBLESS, false) + ")";
            case 15:
                //SERVICE_SP_ITEM_ID
                return ru ? "Купить " + Config.SERVICE_SP_ADD + " SP за " + Config.SERVICE_SP_ITEM_COUNT + " " + getItemName(Config.SERVICE_SP_ITEM_ID, true) + "" : "Buy " + Config.SERVICE_SP_ADD + " SP (" + Config.SERVICE_SP_ITEM_COUNT + " " + getItemName(Config.SERVICE_SP_ITEM_ID, false) + ")";
            case 16:
                return ru ? "У вас и так очень много SP, приходите в другой раз" : "You have a lot of SP, come back next time";
            case 18:
                return ru ? "Стать " + (String) args[0] + " за " + (Integer) args[1] + " " + getItemName(Config.OCCUPATION2_COST_ITEM, true) + "" : "Become " + (String) args[0] + " (" + (Integer) args[1] + " " + getItemName(Config.OCCUPATION1_COST_ITEM, false) + ")";
            case 19:
                return ru ? "Изменить ник персонажа за " + Config.CHANGE_NICK_ITEM_COUNT + " " + getItemName(Config.CHANGE_NICK_ITEM_ID, true) + "" : "Change character name for " + Config.CHANGE_NICK_ITEM_COUNT + " " + getItemName(Config.CHANGE_NICK_ITEM_ID, true) + "";
            case 20:
                return ru ? "Изменить ник питомца за " + Config.CHANGE_NICK_PET_ITEM_COUNT + " " + getItemName(Config.CHANGE_NICK_PET_ITEM_ID, true) + "" : "Change pet name for " + Config.CHANGE_NICK_PET_ITEM_COUNT + " " + getItemName(Config.CHANGE_NICK_PET_ITEM_ID, true) + "";
            case 21:
                return ru ? "Расширить инвентарь за " + Config.EXPAND_INVENTORY_ITEM_COUNT + " " + getItemName(Config.EXPAND_INVENTORY_ITEM_ID, true) + "" : "Expend inventory for " + Config.EXPAND_INVENTORY_ITEM_COUNT + " " + getItemName(Config.EXPAND_INVENTORY_ITEM_ID, true) + "";
            case 22:
                return ru ? "Расширить склад за " + Config.EXPAND_WH_ITEM_COUNT + " " + getItemName(Config.EXPAND_WH_ITEM_ID, true) + "" : "Expend warehouse for " + Config.EXPAND_WH_ITEM_COUNT + " " + getItemName(Config.EXPAND_WH_ITEM_ID, true) + "";
            case 23:
                return ru ? "Расширить склад клана за " + Config.EXPAND_CLAN_WH_ITEM_COUNT + " " + getItemName(Config.EXPAND_CLAN_WH_ITEM_ID, true) + "" : "Expend clan warehouse for " + Config.EXPAND_CLAN_WH_ITEM_COUNT + " " + getItemName(Config.EXPAND_CLAN_WH_ITEM_ID, true) + "";
            case 24:
                return ru ? "Изменить название клана за " + Config.CHANGE_NAME_CLAN_ITEM_COUNT + " " + getItemName(Config.CHANGE_NAME_CLAN_ITEM_ID, true) + "" : "Change clan name for " + Config.CHANGE_NAME_CLAN_ITEM_COUNT + " " + getItemName(Config.CHANGE_NAME_CLAN_ITEM_ID, true) + "";
            case 50:
                return ru ? "Изменить пол за " + Config.CHANGE_SEX_ITEM_COUNT + " " + getItemName(Config.CHANGE_SEX_ITEM_ID, true) + "" : "Change sex for " + Config.CHANGE_SEX_ITEM_COUNT + " " + getItemName(Config.CHANGE_SEX_ITEM_ID, true) + "";
            case 51:
                return ru ? "Изменить прическу за " + Config.BBS_HAIRSTYLE_ITEM_COUNT + " " + getItemName(Config.BBS_HAIRSTYLE_ITEM_ID, true) + "" : "Change hairstyle for " + Config.BBS_HAIRSTYLE_ITEM_COUNT + " " + getItemName(Config.BBS_HAIRSTYLE_ITEM_ID, true) + "";
            case 52:
                return ru ? "Изменить цвет ника " + Config.BBS_NAME_COLOR_ITEM_COUNT + " " + getItemName(Config.BBS_NAME_COLOR_ITEM_ID, true) + "" : "Change nick color for " + Config.BBS_NAME_COLOR_ITEM_COUNT + " " + getItemName(Config.BBS_NAME_COLOR_ITEM_ID, true) + "";
            case 99:
                return ru ? "Пройти путь пробуждение за " + Config.OCCUPATION4_COST_ITEM + " " + getItemName(Ocupation4_COST_ITEM, true) + "" : "Complete the path of awakening for " + Config.OCCUPATION4_COST_ITEM + " " + getItemName(Ocupation4_COST_ITEM, true) + "";

        }
        return "Unknown localize String - " + ID;
    }

    private static String getClassIdSysstring(Player player, int classId) {
        if (player.isLangRus()) {
            switch (classId) {
                case 0:
                    return "Воин";
                case 1:
                    return "Воитель";
                case 2:
                    return "Гладиатор";
                case 3:
                    return "Копейщик";
                case 4:
                    return "Рыцарь";
                case 5:
                    return "Паладин";
                case 6:
                    return "Мститель";
                case 7:
                    return "Разбойник";
                case 8:
                    return "Искатель Сокровищ";
                case 9:
                    return "Стрелок";
                case 10:
                    return "Мистик";
                case 11:
                    return "Маг";
                case 12:
                    return "Властитель Огня";
                case 13:
                    return "Некромант";
                case 14:
                    return "Колдун";
                case 15:
                    return "Клерик";
                case 16:
                    return "Епископ";
                case 17:
                    return "Проповедник";
                case 18:
                    return "Светлый Воин";
                case 19:
                    return "Светлый Рыцарь";
                case 20:
                    return "Рыцарь Евы";
                case 21:
                    return "Менестрель";
                case 22:
                    return "Разведчик";
                case 23:
                    return "Следопыт";
                case 24:
                    return "Серебряный Рейнджер";
                case 25:
                    return "Светлый Мистик";
                case 26:
                    return "Светлый Маг";
                case 27:
                    return "Певец Заклинаний";
                case 28:
                    return "Последователь Стихий";
                case 29:
                    return "Оракул Евы";
                case 30:
                    return "Мудрец Евы";
                case 31:
                    return "Темный Воин";
                case 32:
                    return "Темный Рыцарь";
                case 33:
                    return "Рыцарь Шилен";
                case 34:
                    return "Танцор Смерти";
                case 35:
                    return "Ассасин";
                case 36:
                    return "Странник Бездны";
                case 37:
                    return "Призрачный Рейнджер";
                case 38:
                    return "Темный Мистик";
                case 39:
                    return "Темный Маг";
                case 40:
                    return "Заклинатель Ветра";
                case 41:
                    return "Последователь Тьмы";
                case 42:
                    return "Оракул Шилен";
                case 43:
                    return "Мудрец Шилен";
                case 44:
                    return "Боец";
                case 45:
                    return "Налетчик";
                case 46:
                    return "Разрушитель";
                case 47:
                    return "Монах";
                case 48:
                    return "Отшельник";
                case 49:
                    return "Адепт";
                case 50:
                    return "Шаман";
                case 51:
                    return "Верховный Шаман";
                case 52:
                    return "Вестник Войны";
                case 53:
                    return "Подмастерье";
                case 54:
                    return "Собиратель";
                case 55:
                    return "Охотник за Наградой";
                case 56:
                    return "Ремесленник";
                case 57:
                    return "Кузнец";
                case 88:
                    return "Дуэлист";
                case 89:
                    return "Полководец";
                case 90:
                    return "Рыцарь Феникса";
                case 91:
                    return "Рыцарь Ада";
                case 92:
                    return "Снайпер";
                case 93:
                    return "Авантюрист";
                case 94:
                    return "Архимаг";
                case 95:
                    return "Пожиратель Душ";
                case 96:
                    return "Чернокнижник";
                case 97:
                    return "Кардинал";
                case 98:
                    return "Апостол";
                case 99:
                    return "Храмовник Евы";
                case 100:
                    return "Виртуоз";
                case 101:
                    return "Странник Ветра";
                case 102:
                    return "Страж Лунного Света";
                case 103:
                    return "Магистр Магии";
                case 104:
                    return "Мастер Стихий";
                case 105:
                    return "Жрец Евы";
                case 106:
                    return "Храмовник Шилен";
                case 107:
                    return "Призрачный Танцор";
                case 108:
                    return "Призрачный Охотник";
                case 109:
                    return "Страж Теней";
                case 110:
                    return "Повелитель Бури";
                case 111:
                    return "Владыка Теней";
                case 112:
                    return "Жрец Шилен";
                case 113:
                    return "Титан";
                case 114:
                    return "Аватар";
                case 115:
                    return "Деспот";
                case 116:
                    return "Глас Судьбы";
                case 117:
                    return "Кладоискатель";
                case 118:
                    return "Мастер";
                case 123:
                    return "Грешник";
                case 124:
                    return "Грешница";
                case 125:
                    return "Солдат";
                case 126:
                    return "Надзиратель";
                case 127:
                    return "Берсерк";
                case 128:
                    return "Палач";
                case 129:
                    return "Палач";
                case 130:
                    return "Арбалетчик";
                case 131:
                    return "Каратель";
                case 132:
                    return "Инквизитор";
                case 133:
                    return "Инквизитор";
                case 134:
                    return "Диверсант";
                case 135:
                    return "Инспектор";
                case 136:
                    return "Арбитр";
                case 139:
                    return "Рыцарь Сигеля";
                case 140:
                    return "Воин Тира";
                case 141:
                    return "Разбойник Одала";
                case 142:
                    return "Лучник Еура";
                case 143:
                    return "Волшебник Фео";
                case 144:
                    return "Заклинатель Иса";
                case 145:
                    return "Призыватель Веньо";
                case 146:
                    return "Целитель Альгиза";
                default:
                    return "Неизвесно";
            }
        } else {
            switch (classId) {
                case 0:
                    return "Human Fighter";
                case 1:
                    return "WARRIOR";
                case 2:
                    return "GLADIATOR";
                case 3:
                    return "WARLORD";
                case 4:
                    return "Human Knight";
                case 5:
                    return "PALADIN";
                case 6:
                    return "Dark Avenger";
                case 7:
                    return "ROGUE";
                case 8:
                    return "Treasure Hunter";
                case 9:
                    return "HAWKEYE";
                case 10:
                    return "Human Mystic";
                case 11:
                    return "Human Wizard";
                case 12:
                    return "Sorcerer";
                case 13:
                    return "NECROMANCER";
                case 14:
                    return "WARLOCK";
                case 15:
                    return "CLERIC";
                case 16:
                    return "BISHOP";
                case 17:
                    return "PROPHET";
                case 18:
                    return "Elven Fighter";
                case 19:
                    return "Elven Knight";
                case 20:
                    return "Temple Knight";
                case 21:
                    return "Sword Singer";
                case 22:
                    return "Elven Scout";
                case 23:
                    return "Plains Walker";
                case 24:
                    return "Silver Ranger";
                case 25:
                    return "Elven Mystic";
                case 26:
                    return "Elven Wizard";
                case 27:
                    return "Spellsinger";
                case 28:
                    return "Elemental Summoner";
                case 29:
                    return "Elven Oracle";
                case 30:
                    return "Elven Elder";
                case 31:
                    return "Dark Fighter";
                case 32:
                    return "Palus Knight";
                case 33:
                    return "Shillien Knight";
                case 34:
                    return "Bladedancer";
                case 35:
                    return "Assassin";
                case 36:
                    return "Abyss Walker";
                case 37:
                    return "Phantom Ranger";
                case 38:
                    return "Dark Mystic";
                case 39:
                    return "Dark Wizard";
                case 40:
                    return "Spellhowler";
                case 41:
                    return "Phantom Summoner";
                case 42:
                    return "Shillien Oracle";
                case 43:
                    return "Shillien Elder";
                case 44:
                    return "Orc Fighter";
                case 45:
                    return "Orc Raider";
                case 46:
                    return "Destroyer";
                case 47:
                    return "Monk";
                case 48:
                    return "Tyrant";
                case 49:
                    return "Orc Mystic";
                case 50:
                    return "Orc Shaman";
                case 51:
                    return "Overlord";
                case 52:
                    return "Warcryer";
                case 53:
                    return "Dwarven Fighter";
                case 54:
                    return "Scavenger";
                case 55:
                    return "Bounty Hunter";
                case 56:
                    return "Artisan";
                case 57:
                    return "Warsmith";
                case 88:
                    return "Duelist";
                case 89:
                    return "Dreadnought";
                case 90:
                    return "Phoenix Knight";
                case 91:
                    return "Hell Knight";
                case 92:
                    return "Sagittarius";
                case 93:
                    return "Adventurer";
                case 94:
                    return "Archmage";
                case 95:
                    return "Soultaker";
                case 96:
                    return "Arcana Lord";
                case 97:
                    return "Cardinal";
                case 98:
                    return "Hierophant";
                case 99:
                    return "Eva's Templar";
                case 100:
                    return "Sword Muse";
                case 101:
                    return "Wind Rider";
                case 102:
                    return "Moonlight Sentinel";
                case 103:
                    return "Mystic Muse";
                case 104:
                    return "Elemental Master";
                case 105:
                    return "Eva's Saint";
                case 106:
                    return "Shillien Templar";
                case 107:
                    return "Spectral Dancer";
                case 108:
                    return "Ghost Hunter";
                case 109:
                    return "Ghost Sentinel";
                case 110:
                    return "Storm Screamer";
                case 111:
                    return "Spectral Master";
                case 112:
                    return "Shillien Saint";
                case 113:
                    return "Titan";
                case 114:
                    return "Grand Khavatari";
                case 115:
                    return "Dominator";
                case 116:
                    return "Doom Cryer";
                case 117:
                    return "Fortune Seeker";
                case 118:
                    return "Maestro";
                case 123:
                    return "kamael Soldier";
                case 124:
                    return "kamael Soldier";
                case 125:
                    return "Trooper";
                case 126:
                    return "Warder";
                case 127:
                    return "Berserker";
                case 128:
                    return "Soul Breaker";
                case 129:
                    return "Soul Breaker";
                case 130:
                    return "Arbalester";
                case 131:
                    return "Doombringer";
                case 132:
                    return "Soul Hound";
                case 133:
                    return "Soul Hound";
                case 134:
                    return "Trickster";
                case 135:
                    return "Inspector";
                case 136:
                    return "Judicator";
                case 139:
                    return "Sigel Knight";
                case 140:
                    return "Tyrr Warrior";
                case 141:
                    return "Othell Rogue";
                case 142:
                    return "Yul Archer";
                case 143:
                    return "Feoh Wizard";
                case 144:
                    return "Iss Enchanter";
                case 145:
                    return "Wynn Summoner";
                case 146:
                    return "Aeore Healer";
                default:
                    return "None";
            }
        }
    }

    private static boolean reducePoints(Player player, int count) {
        if (player == null || player.getPcBangPoints() < count)
            return false;

        player.setPcBangPoints(player.getPcBangPoints() - count);
        player.sendPacket(new ExPCCafePointInfo(player, 0, 1, 2, 12));
        return true;
    }

    private static String getItemName(int itemId, boolean isRus) {
        ItemTemplate itemWanted = ItemHolder.getInstance().getTemplate(itemId);
        if (itemWanted == null)
            return "";
        return itemWanted.getName();
    }
}
