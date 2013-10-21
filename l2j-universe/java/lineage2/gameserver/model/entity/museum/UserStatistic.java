/*package sw.awaken.gameserver.model.entity.museum;

import sw.awaken.gameserver.model.Player;
import sw.awaken.gameserver.network.serverpackets.ExLoadStatUser;

/**
 * @author ALF
 * @data 19.05.2012
 */
/**
 * public class UserStatistic {
 * 
 * public static final UserStatistic getInstance() { return
 * SingletonHolder._instance; }
 * 
 * public void getUserStatistic(Player player) {
 * 
 * ExLoadStatUser stat = new ExLoadStatUser();
 * 
 * stat.addPoints(new UserRankPoints(RankType.ACQUIRED_XP.getId(),
 * player.getExp())); // SP stat.addPoints(new
 * UserRankPoints(RankType.ACQUIRED_ADENA.getId(), player.getAdena())); // Adena
 * stat.addPoints(new UserRankPoints(RankType.PLAY_DURATION.getId(),
 * player.getUptime() / 1000)); // Time stat.addPoints(new
 * UserRankPoints(RankType.PK_COUNT.getId(), player.getPkKills())); // PK kills
 * stat.addPoints(new UserRankPoints(RankType.PVP_COUNT.getId(),
 * player.getPvpKills())); // PVP kills
 * 
 * player.sendPacket(stat); }
 * 
 * @SuppressWarnings("synthetic-access") private static class SingletonHolder {
 *                                       protected static final UserStatistic
 *                                       _instance = new UserStatistic(); }
 * 
 *                                       }
 */
