package l2p.gameserver.instancemanager;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.utils.Log;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class L2TopManager {
    private static final String SELECT_PLAYER_OBJID = "SELECT obj_Id FROM characters WHERE char_name=?";
    private static final String SELECT_CHARACTER_MMOTOP_DATA = "SELECT * FROM character_l2top_votes WHERE id=? AND date=? AND multipler=?";
    private static final String INSERT_L2TOP_DATA = "INSERT INTO character_l2top_votes (date, id, nick, multipler) values (?,?,?,?)";
    private static final String DELETE_L2TOP_DATA = "DELETE FROM character_l2top_votes WHERE date<?";
    private static final String SELECT_MULTIPLER_L2TOP_DATA = "SELECT multipler FROM character_l2top_votes WHERE id=? AND has_reward=0";
    private static final String UPDATE_L2TOP_DATA = "UPDATE character_l2top_votes SET has_reward=1 WHERE id=?";

    private final static String voteWeb = Config.DATAPACK_ROOT + "/data/l2top_vote-web.txt";
    private final static String voteSms = Config.DATAPACK_ROOT + "/data/l2top_vote-sms.txt";

    private static L2TopManager _instance;

    public static L2TopManager getInstance() {
        if (_instance == null && Config.L2_TOP_MANAGER_ENABLED)
            _instance = new L2TopManager();
        return _instance;
    }

    public L2TopManager() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new ConnectAndUpdate(), Config.L2_TOP_MANAGER_INTERVAL, Config.L2_TOP_MANAGER_INTERVAL);
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Clean(), Config.L2_TOP_MANAGER_INTERVAL, Config.L2_TOP_MANAGER_INTERVAL);
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new GiveReward(), Config.L2_TOP_MANAGER_INTERVAL, Config.L2_TOP_MANAGER_INTERVAL);
    }

    private void update() {
        String out_sms = getPage(Config.L2_TOP_SMS_ADDRESS);
        String out_web = getPage(Config.L2_TOP_WEB_ADDRESS);

        File sms = new File(voteSms);
        File web = new File(voteWeb);
        FileWriter SaveWeb = null;
        FileWriter SaveSms = null;

        try {
            SaveSms = new FileWriter(sms);
            SaveSms.write(out_sms);
            SaveWeb = new FileWriter(web);
            SaveWeb.write(out_web);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (SaveSms != null)
                    SaveSms.close();
                if (SaveWeb != null)
                    SaveWeb.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private static String getPage(String address) {
        StringBuffer buf = new StringBuffer();
        Socket s;
        try {
            s = new Socket("l2top.ru", 80);

            s.setSoTimeout(30000);
            String request = "GET " + address + " HTTP/1.1\r\n" + "User-Agent: http:\\" + Config.EXTERNAL_HOSTNAME + " server\r\n" + "Host: http:\\" + Config.EXTERNAL_HOSTNAME + " \r\n" + "Accept: */*\r\n" + "Connection: close\r\n" + "\r\n";
            s.getOutputStream().write(request.getBytes());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "Cp1251"));

            for (String line = in.readLine(); line != null; line = in.readLine()) {
                buf.append(line);
                buf.append("\r\n");
            }
            s.close();
        } catch (Exception e) {
            buf.append("Connection error");
        }
        return buf.toString();
    }

    private void parse(boolean sms) {
        String nick = "";
        try {
            @SuppressWarnings("resource")
            BufferedReader in = new BufferedReader(new FileReader(sms ? voteSms : voteWeb));
            String line = in.readLine();
            while (line != null) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                if (line.startsWith("" + year))
                    try {
                        StringTokenizer st = new StringTokenizer(line, "- :\t");
                        if (st.countTokens() == 7) {
                            cal.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MONTH, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MILLISECOND, 0);
                            nick = st.nextToken();
                        } else {
                            cal.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MONTH, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));
                            cal.set(Calendar.MILLISECOND, 0);
                            st.nextToken();
                            nick = st.nextToken();
                        }
                        int mult = 1;
                        if (sms)
                            mult = Integer.parseInt(new StringBuffer(st.nextToken()).delete(0, 1).toString());
                        if (cal.getTimeInMillis() + Config.L2_TOP_SAVE_DAYS * 86400 > System.currentTimeMillis())
                            checkAndSaveFromDb(cal.getTimeInMillis(), nick, mult);
                    } catch (NoSuchElementException nsee) {
                        continue;
                    }
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void clean() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -Config.L2_TOP_SAVE_DAYS);
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(DELETE_L2TOP_DATA);
            statement.setLong(1, cal.getTimeInMillis());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    private synchronized void checkAndSaveFromDb(long date, String nick, int mult) {
        Connection con = null;
        PreparedStatement selectObjectStatement = null, selectL2topStatement = null, insertStatement = null;
        ResultSet rsetObject = null, rsetL2top = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            selectObjectStatement = con.prepareStatement(SELECT_PLAYER_OBJID);
            selectObjectStatement.setString(1, nick);
            rsetObject = selectObjectStatement.executeQuery();
            int objId = 0;
            if (rsetObject.next())
                objId = rsetObject.getInt("obj_Id");
            if (objId > 0) {
                selectL2topStatement = con.prepareStatement(SELECT_CHARACTER_MMOTOP_DATA);
                selectL2topStatement.setInt(1, objId);
                selectL2topStatement.setLong(2, date);
                selectL2topStatement.setInt(3, mult);
                rsetL2top = selectL2topStatement.executeQuery();
                if (!rsetL2top.next()) {
                    insertStatement = con.prepareStatement(INSERT_L2TOP_DATA);
                    insertStatement.setLong(1, date);
                    insertStatement.setInt(2, objId);
                    insertStatement.setString(3, nick);
                    insertStatement.setInt(4, mult);
                    insertStatement.execute();
                    insertStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, selectObjectStatement, rsetObject);
            DbUtils.closeQuietly(con, selectL2topStatement, rsetL2top);
            DbUtils.closeQuietly(con, insertStatement);
        }
    }

    private synchronized void giveReward() {
        Connection con = null;
        PreparedStatement selectMultStatement = null, updateStatement = null;
        ResultSet rsetMult = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            for (Player player : GameObjectsStorage.getAllPlayers()) {
                int objId = player.getObjectId();
                int mult = 0;
                selectMultStatement = con.prepareStatement(SELECT_MULTIPLER_L2TOP_DATA);
                selectMultStatement.setInt(1, objId);
                rsetMult = selectMultStatement.executeQuery();
                while (rsetMult.next())
                    mult += rsetMult.getInt("multipler");

                updateStatement = con.prepareStatement(UPDATE_L2TOP_DATA);
                updateStatement.setInt(1, objId);
                updateStatement.executeUpdate();
                if (mult > 0) {
                    if (player.isLangRus())
                        player.sendMessage("Спасибо за Ваш голос в рейтинге L2Top. C наилучшими пожеланиями " + Config.L2_TOP_SERVER_ADDRESS);
                    else
                        player.sendMessage("Thank you for your vote in L2Top raiting. Best regards " + Config.L2_TOP_SERVER_ADDRESS);
                    for (int i = 0; i < Config.L2_TOP_REWARD.length; i += 2)
                        if (Config.L2_TOP_REWARD[i] == -100) // PC Bang
                        {
                            player.addPcBangPoints(Config.L2_TOP_REWARD[i + 1] * mult, false);
                            Log.add(player.getName() + " | " + player.getObjectId() + " | L2Top reward item ID | " + Config.L2_TOP_REWARD[i] + " | L2Top reward count | " + Config.L2_TOP_REWARD[i + 1] * mult + " |", "l2top");
                        } else if (Config.L2_TOP_REWARD[i] == -200) // Clan reputation
                        {
                            if (player.getClan() != null) {
                                player.getClan().incReputation(Config.L2_TOP_REWARD[i + 1] * mult, false, "L2Top");
                                Log.add(player.getName() + " | " + player.getObjectId() + " | L2Top reward item ID | " + Config.L2_TOP_REWARD[i] + " | L2Top reward count | " + Config.L2_TOP_REWARD[i + 1] * mult + " |", "l2top");
                            } else {
                                player.getInventory().addItem(Config.L2_TOP_REWARD_NO_CLAN[i], Config.L2_TOP_REWARD_NO_CLAN[i + 1] * mult);
                                Log.add(player.getName() + " | " + player.getObjectId() + " | L2Top reward item ID | " + Config.L2_TOP_REWARD_NO_CLAN[i] + " | L2Top reward count | " + Config.L2_TOP_REWARD_NO_CLAN[i + 1] * mult + " |", "l2top");
                            }
                        } else if (Config.L2_TOP_REWARD[i] == -300) // Fame
                        {
                            player.setFame(player.getFame() + Config.L2_TOP_REWARD[i + 1] * mult, "L2Top");
                            Log.add(player.getName() + " | " + player.getObjectId() + " | L2Top reward item ID | " + Config.L2_TOP_REWARD[i] + " | L2Top reward count | " + Config.L2_TOP_REWARD[i + 1] * mult + " |", "l2top");
                        } else {
                            player.getInventory().addItem(Config.L2_TOP_REWARD[i], Config.L2_TOP_REWARD[i + 1] * mult);
                            Log.add(player.getName() + " | " + player.getObjectId() + " | L2Top reward item ID | " + Config.L2_TOP_REWARD[i] + " | L2Top reward count | " + Config.L2_TOP_REWARD[i + 1] * mult + " |", "l2top");
                        }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, selectMultStatement, rsetMult);
            DbUtils.closeQuietly(con, updateStatement);
        }
    }

    private class ConnectAndUpdate implements Runnable {
        @Override
        public void run() {
            update();
            parse(true);
            parse(false);
        }
    }

    private class Clean implements Runnable {
        @Override
        public void run() {
            clean();
        }
    }

    private class GiveReward implements Runnable {
        @Override
        public void run() {
            giveReward();
        }
    }
}
