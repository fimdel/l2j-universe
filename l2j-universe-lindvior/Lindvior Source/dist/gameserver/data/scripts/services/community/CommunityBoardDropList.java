/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services.community;

import l2p.gameserver.Config;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.handler.bbs.CommunityBoardHandler;
import l2p.gameserver.handler.bbs.ICommunityBoardHandler;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Spawner;
import l2p.gameserver.model.base.Experience;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.instances.RaidBossInstance;
import l2p.gameserver.model.reward.RewardData;
import l2p.gameserver.model.reward.RewardGroup;
import l2p.gameserver.model.reward.RewardList;
import l2p.gameserver.model.reward.RewardType;
import l2p.gameserver.network.serverpackets.ShowBoard;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.HtmlUtils;
import l2p.gameserver.utils.Language;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class CommunityBoardDropList implements ScriptFile, ICommunityBoardHandler {
    private static CommunityBoardDropList _Instance = null;
    private static final NumberFormat pf = NumberFormat.getPercentInstance(Locale.ENGLISH);
    private static final NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
    private static final Map<Integer, String> list = new HashMap<Integer, String>();
    private static final Map<Integer, String> list2 = new HashMap<Integer, String>();
    private static final Map<Integer, String> list3 = new HashMap<Integer, String>();
    private String val1 = "";
    private String val2 = "";
    private String val3 = "";
    private String val4 = "";

    static {
        pf.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(2);
    }

    public static CommunityBoardDropList getInstance() {
        if (_Instance == null)
            _Instance = new CommunityBoardDropList();
        return _Instance;
    }

    @Override
    public void onLoad() {
        CommunityBoardHandler.getInstance().registerHandler(this);
    }

    @Override
    public void onReload() {
        CommunityBoardHandler.getInstance().removeHandler(this);
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public String[] getBypassCommands() {
        return new String[]{
                "_bbsdroplist",
                "_bbsrewardspage",
                "_bbsdropnpcnamepage",
                "_bbsdroppage",
                "_bbsdrop",
                "_bbsmonster",
                "_bbsdropid",
                "_bbsdropname",
                "_bbsdropnpcid",
                "_bbsdropnpcname",
                "_bbsrewards",
                "_bbsrewardradar"};
    }

    @Override
    public void onBypassCommand(Player activeChar, String command) {
        StringTokenizer st = new StringTokenizer(command, " ");
        String cmd = st.nextToken();
        val1 = "";
        val2 = "";
        val3 = "";
        val4 = "";
        if (st.countTokens() == 1)
            val1 = st.nextToken();
        else if (st.countTokens() == 2) {
            val1 = st.nextToken();
            val2 = st.nextToken();
        } else if (st.countTokens() == 3) {
            val1 = st.nextToken();
            val2 = st.nextToken();
            val3 = st.nextToken();
        } else if (st.countTokens() == 4) {
            val1 = st.nextToken();
            val2 = st.nextToken();
            val3 = st.nextToken();
            val4 = st.nextToken();
        }

        if (cmd.equalsIgnoreCase("_bbsdroplist")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/list.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdrop")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/drop.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsmonster")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/monster.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdropid")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewards.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            if (!val1.equals("")) {
                generateNpcList(activeChar, Integer.parseInt(val1));
                String str = list.get(1);
                StringBuilder result2 = new StringBuilder();
                content = content.replace("<?rewards?>", str);


                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result2.append("\" action=\"bypass _bbsdroppage " + 2 + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");

                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
                result2.append("\" action=\"bypass _bbsdrop" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");
                content = content.replace("<?pagenext?>", list.get(2) != null ? result2.toString() : "");
            }

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdropname")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewards.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());

            if (!val1.equals("")) {
                String str1 = null;

                if (!val1.equals(""))
                    str1 = val1;

                if (!val2.equals(""))
                    str1 = val1 + " " + val2;

                if (!val3.equals(""))
                    str1 = val1 + " " + val2 + " " + val3;

                if (!val4.equals(""))
                    str1 = val1 + " " + val2 + " " + val3 + " " + val4;

                generateNpcList(activeChar, str1);
                String str = list.get(1);
                StringBuilder result2 = new StringBuilder();
                content = content.replace("<?rewards?>", str);

                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result2.append("\" action=\"bypass _bbsdroppage " + 2 + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");

                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
                result2.append("\" action=\"bypass _bbsdrop" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");
                content = content.replace("<?pagenext?>", result2.toString());
            }

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdroppage")) {
            int page = Integer.parseInt(val1);
            int backpage = page - 1;
            int nextpage = page + 1;

            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewardspage.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            String str = list.get(page);
            StringBuilder result = new StringBuilder();
            content = content.replace("<?rewards?>", str);

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsdrop" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
            String strback = list.get(backpage);
            if (strback != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Предыдущая страница" : "Previous Page");
                result.append("\" action=\"bypass _bbsdroppage ").append(backpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pageback?>", result.length() > 0 ? result.toString() : "");

            result = new StringBuilder();

            String strnext = list.get(nextpage);
            if (strnext != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result.append("\" action=\"bypass _bbsdroppage ").append(nextpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pagenext?>", result.length() > 0 ? result.toString() : "");

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdropnpcid")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/mobsid.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            content = content.replace("<?mobs?>", generateDropListAll(activeChar, Integer.parseInt(val1)));

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdropnpcname")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/mobs.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            if (!val1.equals("")) {
                String str1 = null;

                if (!val1.equals(""))
                    str1 = val1;

                if (!val2.equals(""))
                    str1 = val1 + " " + val2;

                if (!val3.equals(""))
                    str1 = val1 + " " + val2 + " " + val3;

                if (!val4.equals(""))
                    str1 = val1 + " " + val2 + " " + val3 + " " + val4;

                generateDropListAll(activeChar, str1);
                String str = list2.get(1);
                StringBuilder result2 = new StringBuilder();
                content = content.replace("<?mobs?>", str);


                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result2.append("\" action=\"bypass _bbsdropnpcnamepage " + 2 + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");

                result2.append("<center><table width=690>");
                result2.append("<tr>");
                result2.append("<td WIDTH=690 align=center valign=top>");
                result2.append("<center><button value=\"");
                result2.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
                result2.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result2.append("</td>");
                result2.append("</tr>");
                result2.append("</table></center>");
                content = content.replace("<?pagenext?>", list2.get(2) != null ? result2.toString() : "");
            }

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsdropnpcnamepage")) {
            int page = Integer.parseInt(val1);
            int backpage = page - 1;
            int nextpage = page + 1;

            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/mobspage.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            String str = list2.get(page);
            StringBuilder result = new StringBuilder();
            content = content.replace("<?mobs?>", str);

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            String strback = list2.get(backpage);
            if (strback != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Предыдущая страница" : "Previous Page");
                result.append("\" action=\"bypass _bbsdropnpcnamepage ").append(backpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pageback?>", result.length() > 0 ? result.toString() : "");

            result = new StringBuilder();

            String strnext = list2.get(nextpage);
            if (strnext != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result.append("\" action=\"bypass _bbsdropnpcnamepage ").append(nextpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pagenext?>", result.length() > 0 ? result.toString() : "");

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsrewards")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewards.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            RewardType type = RewardType.valueOf(val2);
            if (!val1.equals("")) {
                generateDropListAll(activeChar, Integer.parseInt(val1), type);
                String str = list3.get(1);
                StringBuilder result = new StringBuilder();
                content = content.replace("<?rewards?>", str);

                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result.append("\" action=\"bypass _bbsrewardspage " + 2 + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");

                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
                result.append("\" action=\"bypass _bbsdroplist" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
                content = content.replace("<?pagenext?>", list3.get(2) != null ? result.toString() : "");
            }

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsrewardspage")) {
            int page = Integer.parseInt(val1);
            int backpage = page - 1;
            int nextpage = page + 1;

            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewardspage.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            String str = list3.get(page);
            StringBuilder result = new StringBuilder();
            content = content.replace("<?rewards?>", str);

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsdroplist" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            String strback = list3.get(backpage);
            if (strback != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Предыдущая страница" : "Previous Page");
                result.append("\" action=\"bypass _bbsrewardspage ").append(backpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pageback?>", result.length() > 0 ? result.toString() : "");

            result = new StringBuilder();

            String strnext = list3.get(nextpage);
            if (strnext != null) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                result.append(activeChar.getLanguage() == Language.RUSSIAN ? "Следущая страница" : "Next page");
                result.append("\" action=\"bypass _bbsrewardspage ").append(nextpage).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            content = content.replace("<?pagenext?>", result.length() > 0 ? result.toString() : "");

            ShowBoard.separateAndSend(content, activeChar);
        } else if (cmd.equalsIgnoreCase("_bbsrewardradar")) {
            String content = HtmCache.getInstance().getNullable("scripts/services/community/" + Config.BBS_FOLDER + "/wiki/rewards/rewards.htm", activeChar);

            content = content.replace("<?cb_name?>", Config.COMMUNITYBOARD_NAME);
            content = content.replace("<?cb_copy?>", Config.COMMUNITYBOARD_COPY);
            content = content.replace("%bbs_menu%", HtmCache.getInstance().getNotNull("scripts/services/community/" + Config.BBS_FOLDER + "/bbs_menu.htm", activeChar).toString());


            content = content.replace("<?rewards?>", generateDropListAll(activeChar, Integer.parseInt(val1)));
            content = content.replace("<?pagenext?>", "");

            ShowBoard.separateAndSend(content, activeChar);

            NpcInstance npc = GameObjectsStorage.getByNpcId(Integer.parseInt(val1));
            if (npc != null) {
                Spawner sp = npc.getSpawn();
                if (sp != null)
                    for (NpcInstance spawn : sp.getAllSpawned())
                        if (spawn != null) {
                            activeChar.addRadar(spawn.getSpawnedLoc().getX(), spawn.getSpawnedLoc().getY(), spawn.getSpawnedLoc().getZ());
                            activeChar.addRadarWithMap(spawn.getSpawnedLoc().getX(), spawn.getSpawnedLoc().getY(), spawn.getSpawnedLoc().getZ());
                            break;
                        }
            }
        }
    }

    /**
     * - Генерирует список всех монстров по ID предмета
     *
     * @param player
     * @param id
     * @return
     */
    private void generateNpcList(Player player, int id) {
        list.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int count2 = 0;
        int page = 0;


        for (NpcTemplate npc : NpcHolder.getInstance().getAll())
            if (npc != null) {
                boolean next = false;

                if (npc.getRewards() == null || npc.getRewards().isEmpty() || npc.getRewards().size() == 0)
                    continue;

                for (Map.Entry<RewardType, RewardList> entry : npc.getRewards().entrySet()) {
                    if (next)
                        break;

                    for (RewardGroup group : entry.getValue()) {
                        if (next)
                            break;

                        if (group != null)
                            for (RewardData dat : group.getItems())
                                if (dat.getItem().getItemId() == id) {
                                    result.append("<center><table width=690>");
                                    result.append("<tr>");
                                    result.append("<td WIDTH=690 align=center valign=top>");
                                    result.append("<center><button value=\"");
                                    result.append(npc.getName());
                                    result.append("\" action=\"bypass _bbsdropnpcid ").append(npc.getNpcId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"><br1>").append(" Level: ").append(npc.level).append("</center>");
                                    result.append("</td>");
                                    result.append("</tr>");
                                    result.append("</table></center>");
                                    count++;
                                    count2++;
                                    if (count == 5) {
                                        count = 0;
                                        page++;
                                        list.put(page, result.toString());
                                        result = new StringBuilder();
                                    }

                                    next = true;
                                    break;
                                }
                    }
                }
            }

        if (count2 < 5 && count > 0) {
            page++;
            list.put(page, result.toString());
            return;
        }

        if (list.isEmpty() || list == null || list.size() == 0) {
            page++;

            result = new StringBuilder();

            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Предмет не найден</font></center></td></tr></table><br>" : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Item not found</font></center></td></tr></table><br>");
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsdrop" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            list.put(page, result.toString());
            return;
        }

        list.put(page, result.toString());
    }

    private void generateNpcList(Player player, String name) {
        list.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int count2 = 0;
        int page = 0;

        Language lang = player.getLanguage();

        for (NpcTemplate npc : NpcHolder.getInstance().getAll())
            if (npc != null) {
                boolean next = false;

                if (npc.getRewards() == null || npc.getRewards().isEmpty() || npc.getRewards().size() == 0)
                    continue;

                for (Map.Entry<RewardType, RewardList> entry : npc.getRewards().entrySet()) {
                    if (next)
                        break;

                    for (RewardGroup group : entry.getValue()) {
                        if (next)
                            break;

                        if (group != null)
                            for (RewardData dat : group.getItems())
                                if (dat.getItem().getName() != null && (dat.getItem().getName() == name || val2.equals("") ? dat.getItem().getName().startsWith(name) : dat.getItem().getName().contains(name) || dat.getItem().getName().equals(name) || dat.getItem().getName().equalsIgnoreCase(name))) {
                                    result.append("<center><table width=690>");
                                    result.append("<tr>");
                                    result.append("<td WIDTH=690 align=center valign=top>");
                                    result.append("<center><button value=\"");
                                    result.append(npc.getName());
                                    result.append("\" action=\"bypass _bbsdropnpcid ").append(npc.getNpcId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"><br1>").append(" Level: ").append(npc.level).append("</center>");
                                    result.append("</td>");
                                    result.append("</tr>");
                                    result.append("</table></center>");
                                    count++;
                                    count2++;
                                    if (count == 5) {
                                        count = 0;
                                        page++;
                                        list.put(page, result.toString());
                                        result = new StringBuilder();
                                    }

                                    next = true;
                                    break;
                                }
                    }
                }
            }

        if (count2 < 5 && count > 0) {
            page++;
            list.put(page, result.toString());
            return;
        }

        if (list.isEmpty() || list == null || list.size() == 0) {
            page++;

            result = new StringBuilder();

            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Предмет не найден</font></center></td></tr></table><br>" : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Item not found</font></center></td></tr></table><br>");
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(lang == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsdrop" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
            list.put(page, result.toString());
            return;
        }

        list.put(page, result.toString());
    }

    private String generateDropListAll(Player player, int id) {
        StringBuilder result = new StringBuilder();


        NpcInstance npc = GameObjectsStorage.getByNpcId(id);
        if (npc != null) {
            if (npc.getTemplate().getRewardList(RewardType.RATED_GROUPED) != null && !npc.getTemplate().getRewardList(RewardType.RATED_GROUPED).isEmpty()) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                if (player.getLanguage() == Language.RUSSIAN)
                    result.append("Главная награда");
                else
                    result.append("Home reward");
                result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.RATED_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if (npc.getTemplate().getRewardList(RewardType.NOT_RATED_NOT_GROUPED) != null && !npc.getTemplate().getRewardList(RewardType.NOT_RATED_NOT_GROUPED).isEmpty()) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                if (player.getLanguage() == Language.RUSSIAN)
                    result.append("Дополнительная награда");
                else
                    result.append("More reward");
                result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.NOT_RATED_NOT_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if (npc.getTemplate().getRewardList(RewardType.NOT_RATED_GROUPED) != null && !npc.getTemplate().getRewardList(RewardType.NOT_RATED_GROUPED).isEmpty()) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                if (player.getLanguage() == Language.RUSSIAN)
                    result.append("Хербы");
                else
                    result.append("Herbs");
                result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.NOT_RATED_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            if (npc.getTemplate().getRewardList(RewardType.SWEEP) != null && !npc.getTemplate().getRewardList(RewardType.SWEEP).isEmpty()) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><button value=\"");
                if (player.getLanguage() == Language.RUSSIAN)
                    result.append("Спойл");
                else
                    result.append("Spoil");
                result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.SWEEP).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
            }

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            if (player.getLanguage() == Language.RUSSIAN)
                result.append("Поставить радар");
            else
                result.append("Place the Radar");
            result.append("\" action=\"bypass _bbsrewardradar ").append(npc.getNpcId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        } else {
            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Монстер не найден</font></center></td></tr></table><br>" : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Monster not found</font></center></td></tr></table><br>");

            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><button value=\"");
            result.append(player.getLanguage() == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");
        }

        return result.toString();
    }

    private void generateDropListAll(Player player, String name) {
        list2.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int count2 = 0;
        int page = 0;

        Language lang = player.getLanguage();

        for (NpcTemplate npc : NpcHolder.getInstance().getAll())
            if (npc != null && (npc.getName() == name || val2.equals("") ? npc.getName().startsWith(name) : npc.getName().contains(name) || npc.getName().equals(name) || npc.getName().equalsIgnoreCase(name))) {
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><font color=\"b09979\">").append(npc.getName()).append("</font></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");

                if (npc.getRewardList(RewardType.RATED_GROUPED) != null && !npc.getRewardList(RewardType.RATED_GROUPED).isEmpty()) {
                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<center><br><br><button value=\"");
                    if (lang == Language.RUSSIAN)
                        result.append("Главная награда");
                    else
                        result.append("Home reward");
                    result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.RATED_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                }

                if (npc.getRewardList(RewardType.NOT_RATED_NOT_GROUPED) != null && !npc.getRewardList(RewardType.NOT_RATED_NOT_GROUPED).isEmpty()) {
                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<center><br><br><button value=\"");
                    if (lang == Language.RUSSIAN)
                        result.append("Дополнительная награда");
                    else
                        result.append("More reward");
                    result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.NOT_RATED_NOT_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                }

                if (npc.getRewardList(RewardType.NOT_RATED_GROUPED) != null && !npc.getRewardList(RewardType.NOT_RATED_GROUPED).isEmpty()) {
                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<center><br><br><button value=\"");
                    if (lang == Language.RUSSIAN)
                        result.append("Хербы");
                    else
                        result.append("Herbs");
                    result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.NOT_RATED_GROUPED).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                }

                if (npc.getRewardList(RewardType.SWEEP) != null && !npc.getRewardList(RewardType.SWEEP).isEmpty()) {
                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<center><br><br><button value=\"");
                    if (lang == Language.RUSSIAN)
                        result.append("Спойл");
                    else
                        result.append("Spoil");
                    result.append("\" action=\"bypass _bbsrewards ").append(npc.getNpcId()).append(" ").append(RewardType.SWEEP).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                }
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><br><br><button value=\"");
                if (lang == Language.RUSSIAN)
                    result.append("Поставить радар");
                else
                    result.append("Place the Radar");
                result.append("\" action=\"bypass _bbsrewardradar ").append(npc.getNpcId()).append("\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Watch_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
                result.append("<center><table width=690>");
                result.append("<tr>");
                result.append("<td WIDTH=690 align=center valign=top>");
                result.append("<center><br><br><button value=\"");
                result.append(lang == Language.RUSSIAN ? "Назад" : "Back");
                result.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
                result.append("</td>");
                result.append("</tr>");
                result.append("</table></center>");
                count++;
                count2++;
                if (count == 5) {
                    count = 0;
                    page++;
                    list2.put(page, result.toString());
                    result = new StringBuilder();
                }
            }

        if (count2 < 5 && count > 0) {
            page++;
            list2.put(page, result.toString());
            return;
        }

        if (list2.isEmpty() || list2 == null || list2.size() == 0) {
            page++;
            result = new StringBuilder();

            result.append(player.getLanguage() == Language.RUSSIAN ? "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Монстер не найден</font></center></td></tr></table><br>" : "<table width=690><tr><td width=690><center><font name=\"hs12\" color=\"FF0000\">Monster not found</font></center></td></tr></table><br>");
            result.append("<center><table width=690>");
            result.append("<tr>");
            result.append("<td WIDTH=690 align=center valign=top>");
            result.append("<center><br><br><button value=\"");
            result.append(lang == Language.RUSSIAN ? "Назад" : "Back");
            result.append("\" action=\"bypass _bbsmonster" + "\" width=200 height=29 back=\"L2UI_CT1.OlympiadWnd_DF_Back_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\"></center>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table></center>");

            list2.put(page, result.toString());
            return;
        }

        page++;
        list2.put(page, result.toString());
    }

    private void generateDropListAll(Player player, int id, RewardType type) {
        list3.clear();
        NpcInstance npc = GameObjectsStorage.getByNpcId(id);
        if (npc != null) {
            final int diff = npc.calculateLevelDiffForDrop(player.isInParty() ? player.getParty().getLevel() : player.getLevel());
            double mod = npc.calcStat(Stats.REWARD_MULTIPLIER, 1., player, null);
            mod *= Experience.penaltyModifier(diff, 9);

            switch (type) {
                case RATED_GROUPED:
                    generateDropList(player, npc, mod);
                    break;
                case NOT_RATED_GROUPED:
                    generateDropListContinue(player, npc, mod);
                    break;
                case NOT_RATED_NOT_GROUPED:
                    generateDropListHerbs(player, npc, mod);
                    break;
                case SWEEP:
                    generateSpoilList(player, npc, mod);
                    break;
            }
        }
    }

    /**
     * - Генерирует список всех предметов у монстра
     *
     * @param player
     * @param id
     * @return
     */
    private void generateDropList(Player player, NpcInstance npc, double mod) {
        list3.clear();
        int page = 0;

        if (npc != null)
            for (RewardGroup g : npc.getTemplate().getRewardList(RewardType.RATED_GROUPED)) {
                StringBuilder result = new StringBuilder();

                double gchance = g.getChance();
                double gmod = mod;
                double grate;
                double gmult;

                double rateDrop = npc instanceof RaidBossInstance ? Config.RATE_DROP_RAIDBOSS : npc.isSiegeGuard() ? Config.RATE_DROP_SIEGE_GUARD : Config.RATE_DROP_ITEMS * player.getRateItems();
                double rateAdena = Config.RATE_DROP_ADENA * player.getRateAdena();

                if (g.isAdena()) {
                    if (rateAdena == 0)
                        continue;

                    grate = rateAdena;

                    if (gmod > 5) {
                        gmod *= g.getChance() / RewardList.MAX_CHANCE;
                        gchance = RewardList.MAX_CHANCE;
                    }

                    grate *= gmod;
                } else {
                    if (rateDrop == 0)
                        continue;

                    grate = rateDrop;

                    if (g.notRate())
                        grate = Math.min(gmod, 1.0);
                    else
                        grate *= gmod;
                }

                gmult = Math.ceil(grate);

                for (RewardData d : g.getItems()) {
                    double imult = d.notRate() ? 1.0 : gmult;
                    String icon = d.getItem().getIcon();
                    if (icon == null || icon.equals(StringUtils.EMPTY))
                        icon = "icon.etc_question_mark_i00";

                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<table border=0 cellspacing=4 cellpadding=3>");
                    result.append("<tr>");
                    result.append("<td FIXWIDTH=50 align=right valign=top>");
                    result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
                    result.append("</td>");
                    result.append("<td FIXWIDTH=671 align=left valign=top>");
                    result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1><font color=\"LEVEL\">").append(player.getLanguage() == Language.RUSSIAN ? "Шанс выпадения:</font> " : "Drop chance:</font><font color=\"b09979\">[Min: ").append(Math.round(d.getMinDrop() * (g.isAdena() ? gmult : 1.0))).append("| Max: ").append(Math.round(d.getMaxDrop() * imult)).append("]&nbsp;");
                    result.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("<table border=0 cellspacing=0 cellpadding=0>");
                    result.append("<tr>");
                    result.append("<td width=690>");
                    result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                }

                page++;
                list3.put(page, result.toString());
            }
    }

    public static void generateDropListContinue(Player player, NpcInstance npc, double mod) {
        list3.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int page = 0;

        if (npc != null)
            for (RewardGroup g : npc.getTemplate().getRewardList(RewardType.NOT_RATED_GROUPED)) {
                double gchance = g.getChance();

                if (player.getLanguage() == Language.RUSSIAN)
                    result.append("<table><tr><td width=170><font color=\"a2a0a2\">Общий шанс группы: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td></tr><table>");
                else
                    result.append("<table><tr><td width=170><font color=\"a2a0a2\">The overall chance of: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td></tr><table>");

                for (RewardData d : g.getItems()) {
                    String icon = d.getItem().getIcon();
                    if (icon == null || icon.equals(StringUtils.EMPTY))
                        icon = "icon.etc_question_mark_i00";

                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<table border=0 cellspacing=4 cellpadding=3>");
                    result.append("<tr>");
                    result.append("<td FIXWIDTH=50 align=right valign=top>");
                    result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
                    result.append("</td>");
                    result.append("<td FIXWIDTH=671 align=left valign=top>");
                    result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1><font color=\"LEVEL\">").append(player.getLanguage() == Language.RUSSIAN ? "Шанс выпадения:</font> " : "Drop chance:</font><font color=\"b09979\">[Min: ").append(Math.round(d.getMinDrop())).append("| Max: ").append(Math.round(d.getMaxDrop())).append("]&nbsp;");
                    result.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("<table border=0 cellspacing=0 cellpadding=0>");
                    result.append("<tr>");
                    result.append("<td width=690>");
                    result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");

                    count++;
                    if (count == 5) {
                        count = 0;
                        page++;
                        list3.put(page, result.toString());
                        result = new StringBuilder();

                        if (player.getLanguage() == Language.RUSSIAN)
                            result.append("<table><tr><td width=170><font color=\"a2a0a2\">Общий шанс группы: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td></tr></table>");
                        else
                            result.append("<table><tr><td width=170><font color=\"a2a0a2\">The overall chance of: </font><font color=\"b09979\">").append(pf.format(gchance / RewardList.MAX_CHANCE)).append("</font></td></tr></table>");
                    }
                }
            }
        page++;
        list3.put(page, result.toString());
    }

    public static void generateDropListHerbs(Player player, NpcInstance npc, double mod) {
        list3.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int page = 0;

        if (npc != null)
            for (RewardGroup g : npc.getTemplate().getRewardList(RewardType.NOT_RATED_NOT_GROUPED)) {
                double grate;
                double gmult;

                grate = 1;

                if (g.notRate())
                    grate = Math.min(mod, 1.0);
                else
                    grate *= mod;

                gmult = Math.ceil(grate);

                for (RewardData d : g.getItems()) {
                    double imult = d.notRate() ? 1.0 : gmult;
                    String icon = d.getItem().getIcon();
                    if (icon == null || icon.equals(StringUtils.EMPTY))
                        icon = "icon.etc_question_mark_i00";

                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<table border=0 cellspacing=4 cellpadding=3>");
                    result.append("<tr>");
                    result.append("<td FIXWIDTH=50 align=right valign=top>");
                    result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
                    result.append("</td>");
                    result.append("<td FIXWIDTH=671 align=left valign=top>");
                    result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1><font color=\"LEVEL\">").append(player.getLanguage() == Language.RUSSIAN ? "Шанс выпадения:</font> " : "Drop chance:</font><font color=\"b09979\">[Min: ").append(Math.round(d.getMaxDrop() * imult)).append("| Max: ").append(Math.round(d.getMaxDrop() * imult)).append("]&nbsp;");
                    result.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("<table border=0 cellspacing=0 cellpadding=0>");
                    result.append("<tr>");
                    result.append("<td width=690>");
                    result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");
                    count++;
                    if (count == 5) {
                        count = 0;
                        page++;
                        list3.put(page, result.toString());
                        result = new StringBuilder();

                        if (player.getLanguage() == Language.RUSSIAN)
                            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Список хербов:</font></td></tr></table></td></tr></table>");
                        else
                            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Herbs list:</font></td></tr></table></td></tr></table>");
                    }
                }
            }
        page++;
        list3.put(page, result.toString());
    }

    public static void generateSpoilList(Player player, NpcInstance npc, double mod) {
        list3.clear();
        StringBuilder result = new StringBuilder();
        int count = 0;
        int page = 0;

        if (player.getLanguage() == Language.RUSSIAN)
            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Список спойла:</font></td></tr></table></td></tr></table>");
        else
            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Spoil list:</font></td></tr></table></td></tr></table>");

        if (npc != null)
            for (RewardGroup g : npc.getTemplate().getRewardList(RewardType.SWEEP)) {
                double grate;
                double gmult;

                grate = Config.RATE_DROP_SPOIL * player.getRateSpoil();

                if (g.notRate())
                    grate = Math.min(mod, 1.0);
                else
                    grate *= mod;

                gmult = Math.ceil(grate);

                for (RewardData d : g.getItems()) {
                    double imult = d.notRate() ? 1.0 : gmult;
                    String icon = d.getItem().getIcon();
                    if (icon == null || icon.equals(StringUtils.EMPTY))
                        icon = "icon.etc_question_mark_i00";

                    result.append("<center><table width=690>");
                    result.append("<tr>");
                    result.append("<td WIDTH=690 align=center valign=top>");
                    result.append("<table border=0 cellspacing=4 cellpadding=3>");
                    result.append("<tr>");
                    result.append("<td FIXWIDTH=50 align=right valign=top>");
                    result.append("<img src=\"").append(icon).append("\" width=32 height=32>");
                    result.append("</td>");
                    result.append("<td FIXWIDTH=671 align=left valign=top>");
                    result.append("<font color=\"0099FF\">").append(player.getLanguage() == Language.RUSSIAN ? "Название предмета:</font> " : "Item name:</font> ").append(HtmlUtils.htmlItemName(d.getItemId())).append("<br1><font color=\"LEVEL\">").append(player.getLanguage() == Language.RUSSIAN ? "Шанс выпадения:</font> " : "Drop chance:</font><font color=\"b09979\">[Min: ").append(d.getMinDrop()).append("| Max: ").append(Math.round(d.getMaxDrop() * imult)).append("]&nbsp;");
                    result.append(pf.format(d.getChance() / RewardList.MAX_CHANCE)).append("</font></td></tr>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("<table border=0 cellspacing=0 cellpadding=0>");
                    result.append("<tr>");
                    result.append("<td width=690>");
                    result.append("<img src=\"l2ui.squaregray\" width=\"690\" height=\"1\">");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table>");
                    result.append("</td>");
                    result.append("</tr>");
                    result.append("</table></center>");

                    count++;
                    if (count == 5) {
                        count = 0;
                        page++;
                        list3.put(page, result.toString());
                        result = new StringBuilder();

                        if (player.getLanguage() == Language.RUSSIAN)
                            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Список спойла:</font></td></tr></table></td></tr></table>");
                        else
                            result.append("<table><tr><td><table width=270 border=0><tr><td><font color=\"aaccff\">Spoil list:</font></td></tr></table></td></tr></table>");

                    }
                }
            }

        page++;
        list3.put(page, result.toString());
    }

    @Override
    public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5) {
    }
}