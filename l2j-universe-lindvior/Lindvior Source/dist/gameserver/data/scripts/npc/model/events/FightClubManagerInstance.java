/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package npc.model.events;

import l2p.gameserver.Config;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.11.12
 * Time: 13:59
 */
public class FightClubManagerInstance extends NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final String HTML_INDEX = "scripts/events/fightclub/index.htm";
    private static final String HTML_ACCEPT = "scripts/events/fightclub/accept.htm";
    private static final String HTML_MAKEBATTLE = "scripts/events/fightclub/makebattle.htm";
    private static final String HTML_INFO = "scripts/events/fightclub/info.htm";
    private static final String HTML_DISABLED = "scripts/events/fightclub/disabled.htm";
    private static final String HTML_LIST = "scripts/events/fightclub/fightslist.htm";
    private static final String HTML_RESULT = "scripts/events/fightclub/result.htm";

    public FightClubManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (!Config.FIGHT_CLUB_ENABLED) {
            showChatWindow(player, HTML_DISABLED, player);
            return;
        }

        if (command.equalsIgnoreCase("index"))
            showChatWindow(player, HTML_INDEX, player);
        else if (command.equalsIgnoreCase("makebattle"))
            player.sendPacket(makeBattleHtml(player));
        else if (command.equalsIgnoreCase("info"))
            showChatWindow(player, HTML_INFO, player);
        else {
            final StringTokenizer st = new StringTokenizer(command, " ");
            String pageName = st.nextToken();
            if (pageName.equalsIgnoreCase("addbattle")) {
                int count = 0;
                try {
                    count = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {
                    sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы не ввели количество, или неправильное число." : "You did not enter the number or wrong number.");
                }
                String itemName = StringUtils.EMPTY;
                if (st.hasMoreTokens()) {
                    itemName = st.nextToken();
                    while (st.hasMoreTokens())
                        itemName += " " + st.nextToken();
                }
                Object[] objects = {player, itemName, count};
                final String respone = (String) callScripts("addApplication", objects);
                if ("OK".equalsIgnoreCase(respone))
                    sendResult(player, player.isLangRus() ? "Выполнено!" : "Completed!", (player.isLangRus() ? "Вы создали заявку на участие.<br>Ваша ставка - <font color=\"LEVEL\">" : "You have created an application for participation.<br>Your bet - <font color=\"LEVEL\">") + String.valueOf(objects[2]) + " " + String.valueOf(objects[1]) + (player.isLangRus() ? "</font><br><center>Удачи!</center>" : "</font><br><center>Good luck!</center>"));
                else if ("NoItems".equalsIgnoreCase(respone))
                    sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "У вас недостаточно или отсутствуют требующиеся предметы!" : "You are not required or missing items!");
                else if ("reg".equalsIgnoreCase(respone))
                    sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы уже зарегистрированы! Если вы хотите изменить ставку, удалите старую регистрацию." : "You are already registered! If you wish to bid, remove the old registration.");
            } else if (pageName.equalsIgnoreCase("delete")) {
                Object[] playerObject = {player};
                if ((Boolean) callScripts("isRegistered", playerObject)) {
                    callScripts("deleteRegistration", playerObject);
                    sendResult(player, player.isLangRus() ? "Выполнено!" : "Completed!", player.isLangRus() ? "<center>Вы удалены из списка регистрации.</center>" : "<center>You are removed from the list of registration.</center>");
                } else
                    sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "<center>Вы не были зарегистрированы на участие.</center>" : "<center>You have not been registered to participate.</center>");
            } else if (pageName.equalsIgnoreCase("openpage"))
                player.sendPacket(makeOpenPage(player, Integer.parseInt(st.nextToken())));
            else if (pageName.equalsIgnoreCase("tryaccept"))
                player.sendPacket(makeAcceptHtml(player, Long.parseLong(st.nextToken())));
            else if (pageName.equalsIgnoreCase("accept"))
                accept(player, Long.parseLong(st.nextToken()));
        }
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        showChatWindow(player, HTML_INDEX);
    }

    private NpcHtmlMessage makeOpenPage(Player player, int pageId) {
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        html.setFile(HTML_LIST);

        StringBuilder sb = new StringBuilder();

        final int count = (Integer) callScripts("getRatesCount", new Object[0]);

        int num = pageId * Config.PLAYERS_PER_PAGE;
        if (num > count)
            num = count;
        if (count > 0) {
            sb.append("<table width=260 border=0 cellspacing=3 cellpadding=3>");
            sb.append("<tr><td align=center valign=top>");

            for (int i = pageId * Config.PLAYERS_PER_PAGE - Config.PLAYERS_PER_PAGE; i < num; i++) {
                Object[] index = {i};
                Rate rate = (Rate) callScripts("getRateByIndex", index);
                sb.append("<table width=250 border=1 cellspacing=5 cellpadding=5>");
                sb.append("<tr>");
                sb.append("<td align=center width=120><a action=\"bypass -h npc_%objectId%_tryaccept ").append(rate.getStoredId()).append("\">");
                sb.append("<td align=center width=120>").append(player.isLangRus() ? "Игрок:" : "Player:").append("</td>");
                sb.append("<td align=center width=120><font color=\"00ff00\">").append(rate.getPlayerName()).append("</font></td>");
                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td align=center width=120>").append(player.isLangRus() ? "Уровень игрока:" : "Level Player:").append("</td>");
                sb.append("<td align=center width=120><font color=\"ff0000\">").append(rate.getPlayerLevel()).append("</font></td>");
                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td align=center width=120>").append(player.isLangRus() ? "Класс:" : "Class:").append("</td>");
                sb.append("<td align=center width=120><font color=\"ff0000\">").append(rate.getPlayerClass()).append("</font></td>");
                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td align=center width=120>").append(player.isLangRus() ? "Награда:" : "Reward:").append("</td>");
                sb.append("<td align=center width=120><font color=\"LEVEL\">").append(rate.getItemCount()).append(" ").append(ItemFunctions.createItem(rate.getItemId()).getTemplate().getName()).append("</font></td>");
                sb.append("</tr>");
                sb.append("</table><br><br><br>");
            }

            sb.append("</td></tr>");
            sb.append("</table><br><br><br>");

            int pg = getPagesCount(count);
            sb.append("Страницы:&nbsp;");
            for (int i = 1; i <= pg; i++)
                if (i == pageId)
                    sb.append(i).append("&nbsp;");
                else
                    sb.append("<a action=\"bypass -h npc_%objectId%_openpage ").append(i).append("\">").append(i).append("</a>&nbsp;");
        } else
            sb.append(player.isLangRus() ? "<br><center>Ставок пока не сделано</center>" : "<br><center>Rates have not yet done</center>");
        html.replace("%data%", sb.toString());

        return html;
    }

    private NpcHtmlMessage makeBattleHtml(Player player) {
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        html.setFile(HTML_MAKEBATTLE);
        html.replace("%items%", (String) callScripts("getItemsList", new Object[0]));

        return html;
    }

    private NpcHtmlMessage makeAcceptHtml(Player player, long storedId) {
        Object[] id = {storedId};
        Rate rate = (Rate) callScripts("getRateByStoredId", id);
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        html.setFile(HTML_ACCEPT);
        html.replace("%name%", rate.getPlayerName());
        html.replace("%class%", rate.getPlayerClass());
        html.replace("%level%", String.valueOf(rate.getPlayerLevel()));
        html.replace("%rate%", rate.getItemCount() + " " + rate.getItemName());
        html.replace("%storedId%", String.valueOf(rate.getStoredId()));
        return html;
    }

    private void accept(Player player, long storedId) {
        final Object[] data = {GameObjectsStorage.getAsPlayer(storedId), player};
        if (player.getStoredId() == storedId) {
            sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы не можете вызвать на бой самого себя." : "You can not call the fight itself.");
            return;
        }
        //TODO: Проверка на айтемы... Берем пример с doStart
        //if(Functions.getItemCount(player, _))
        if ((Boolean) callScripts("requestConfirmation", data))
            sendResult(player, player.isLangRus() ? "Внимание!" : "Attention!", player.isLangRus() ? "Вы отправили запрос сопернику. Если все условия соответствуют, Вас переместят на арену<br><center><font color=\"LEVEL\">Удачи!</font></center><br>" : "You have sent a request to the opponent. If all conditions match, you will move into the arena<br><center><font color=\"LEVEL\">Good luck!</font></center><br>");
    }

    private void sendResult(Player player, String title, String text) {
        NpcHtmlMessage html = new NpcHtmlMessage(player, this);
        html.setFile(HTML_RESULT);
        html.replace("%title%", title);
        html.replace("%text%", text);
        player.sendPacket(html);
    }

    private int getPagesCount(int count) {
        if (count % Config.PLAYERS_PER_PAGE > 0)
            return count / Config.PLAYERS_PER_PAGE + 1;
        return count / Config.PLAYERS_PER_PAGE;
    }

    private Object callScripts(String methodName, Object[] args) {
        return Functions.callScripts("events.FightClub.FightClubManager", methodName, args);
    }

    public static class Rate {
        private String playerName;
        private int playerLevel;
        private String playerClass;
        private int itemId;
        private String itemName;
        private int itemCount;
        private long playerStoredId;

        public Rate(Player player, int itemId, int itemCount) {
            playerName = player.getName();
            playerLevel = player.getLevel();
            playerClass = player.getClassId().name();
            this.itemId = itemId;
            this.itemCount = itemCount;
            itemName = ItemFunctions.createItem(itemId).getTemplate().getName();
            playerStoredId = player.getStoredId();
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getPlayerLevel() {
            return playerLevel;
        }

        public String getPlayerClass() {
            return playerClass;
        }

        public int getItemId() {
            return itemId;
        }

        public int getItemCount() {
            return itemCount;
        }

        public String getItemName() {
            return itemName;
        }

        public long getStoredId() {
            return playerStoredId;
        }
    }
}
