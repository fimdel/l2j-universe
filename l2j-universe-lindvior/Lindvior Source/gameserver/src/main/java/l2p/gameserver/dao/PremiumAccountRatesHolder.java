/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class PremiumAccountRatesHolder {
    private static final Logger _log = LoggerFactory.getLogger(PremiumAccountRatesHolder.class);

    public static CopyOnWriteArrayList<PremiumInfo> all_info = new CopyOnWriteArrayList();

    public static void loadLists() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT * FROM premium_account_table");
            rset = statement.executeQuery();
            while (rset.next()) {
                int grp_id = rset.getInt("groupId");
                String grp_name_ru = rset.getString("groupName_ru");
                String grp_name_en = rset.getString("groupName_en");
                double exp = rset.getDouble("exp");
                double sp = rset.getDouble("sp");
                double adena = rset.getDouble("adena");
                double drop = rset.getDouble("drop");
                double spoil = rset.getDouble("spoil");
                double qDrop = rset.getDouble("qdrop");
                double qReward = rset.getDouble("qreward");
                int delay = rset.getInt("delay");
                boolean isHours = rset.getInt("isHours") == 1;
                int itemId = rset.getInt("itemId");
                long itemCount = rset.getLong("itemCount");
                PremiumInfo premium_info = new PremiumInfo(grp_id, grp_name_ru, grp_name_en, exp, sp, adena, drop, spoil, qDrop, qReward, delay, isHours, itemId, itemCount);
                all_info.add(premium_info);
            }

        } catch (Exception e) {
            _log.error("", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
            _log.info("Premium Info: Loaded with " + all_info.size() + " premium options");
        }
    }

    public static CopyOnWriteArrayList<PremiumInfo> getAllAquisions() {
        if (all_info == null)
            return null;
        return all_info;
    }

    public static boolean getIfDelayHours(int groupId) {
        for (PremiumInfo info : all_info) {
            if (info._groupId == groupId) {
                return info._isHours;
            }
        }
        return true;
    }

    public static boolean validateGroup(int groupId, int period, int itemId, int itemCount) {
        boolean validated = false;
        for (PremiumInfo info : all_info) {
            if (info._groupId == groupId) {
                if ((info._delay != period) || (info._itemId != itemId) || (info._itemCount != itemCount)) {
                    return false;
                }
                validated = true;
            }
        }
        if (!validated)
            return false;
        return true;
    }

    public static PremiumInfo getPremiumByGroupId(int groupId) {
        for (PremiumInfo info : all_info) {
            if (info._groupId == groupId) {
                return info;
            }
        }
        return null;
    }

    public static class PremiumInfo {
        public final int _groupId;
        public final String _groupNameRu;
        public final String _groupNameEn;
        public final double _exp;
        public final double _sp;
        public final double _adena;
        public final double _drop;
        public final double _spoil;
        public final double _qdrop;
        public final double _qreward;
        public final int _delay;
        public final boolean _isHours;
        public final int _itemId;
        public final long _itemCount;

        public PremiumInfo(int groupId, String groupNameRu, String groupNameEn, double exp, double sp, double adena, double drop, double spoil, double qdrop, double qreward, int delay, boolean isHours, int itemId, long itemCount) {
            this._groupId = groupId;
            this._groupNameRu = groupNameRu;
            this._groupNameEn = groupNameEn;
            this._exp = exp;
            this._sp = sp;
            this._adena = adena;
            this._drop = drop;
            this._spoil = spoil;
            this._qdrop = qdrop;
            this._qreward = qreward;
            this._delay = delay;
            this._isHours = isHours;
            this._itemId = itemId;
            this._itemCount = itemCount;
        }

        public int getGroupNumber() {
            return this._groupId;
        }

        public String getGroupNameRu() {
            if (this._groupNameRu == null) {
                return "";
            }
            return this._groupNameRu;
        }

        public String getGroupNameEn() {
            if (this._groupNameEn == null)
                return "";
            return this._groupNameEn;
        }

        public double getExp() {
            if (this._exp < 1.0D)
                return 1.0D;
            return this._exp;
        }

        public double getSp() {
            if (this._sp < 1.0D)
                return 1.0D;
            return this._sp;
        }

        public double getAdena() {
            if (this._adena < 1.0D) {
                return 1.0D;
            }
            return this._adena;
        }

        public double getDrop() {
            if (this._drop < 1.0D) {
                return 1.0D;
            }
            return this._drop;
        }

        public double getSpoil() {
            if (this._spoil < 1.0D) {
                return 1.0D;
            }
            return this._spoil;
        }

        public double getQDrop() {
            if (this._qdrop < 1.0D)
                return 1.0D;
            return this._qdrop;
        }

        public double getQReward() {
            if (this._qreward < 1.0D) {
                return 1.0D;
            }
            return this._qreward;
        }

        public int getDays() {
            if (this._delay < 1) {
                return 0;
            }
            return this._delay;
        }

        public boolean isHours() {
            return this._isHours;
        }

        public int getItemId() {
            return this._itemId;
        }

        public long getItemCount() {
            if (this._itemCount <= 0L) {
                return 1L;
            }
            return this._itemCount;
        }
    }
}
