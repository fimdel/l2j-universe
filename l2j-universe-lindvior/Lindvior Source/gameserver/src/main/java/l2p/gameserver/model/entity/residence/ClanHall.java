package l2p.gameserver.model.entity.residence;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.dao.ClanDataDAO;
import l2p.gameserver.dao.ClanHallDAO;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.instancemanager.PlayerMessageStack;
import l2p.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.ItemTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClanHall extends Residence {
    /**
     *
     */
    private static final long serialVersionUID = -5758261358378866022L;

    private static final Logger _log = LoggerFactory.getLogger(ClanHall.class);

    private static final int REWARD_CYCLE = 168; // 1 недели - 7 дней - 168 часов

    private int _auctionLength;
    private long _auctionMinBid;
    private String _auctionDescription = StringUtils.EMPTY;

    private final int _grade;
    private final long _rentalFee;
    private final long _minBid;
    private final long _deposit;

    public ClanHall(StatsSet set) {
        super(set);
        _grade = set.getInteger("grade", 0);
        _rentalFee = set.getInteger("rental_fee", 0);
        _minBid = set.getInteger("min_bid", 0);
        _deposit = set.getInteger("deposit", 0);
    }

    @Override
    public void init() {
        initZone();
        initEvent();

        loadData();
        loadFunctions();
        rewardSkills();

        // если это Аукционный КХ, и есть овнер, и КХ, непродается
        if (getSiegeEvent().getClass() == ClanHallAuctionEvent.class && _owner != null && getAuctionLength() == 0)
            startCycleTask();
    }

    @Override
    public void changeOwner(Clan clan) {
        Clan oldOwner = getOwner();

        if (oldOwner != null && (clan == null || clan.getClanId() != oldOwner.getClanId())) {
            removeSkills();
            oldOwner.setHasHideout(0);

            cancelCycleTask();
        }

        updateOwnerInDB(clan);
        rewardSkills();

        update();

        if (clan == null && getSiegeEvent().getClass() == ClanHallAuctionEvent.class)
            getSiegeEvent().reCalcNextTime(false);
    }

    @Override
    public ResidenceType getType() {
        return ResidenceType.ClanHall;
    }

    @Override
    protected void loadData() {
        _owner = ClanDataDAO.getInstance().getOwner(this);

        ClanHallDAO.getInstance().select(this);
    }

    private void updateOwnerInDB(Clan clan) {
        _owner = clan;

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("UPDATE clan_data SET hasHideout=0 WHERE hasHideout=?");
            statement.setInt(1, getId());
            statement.execute();
            DbUtils.close(statement);

            statement = con.prepareStatement("UPDATE clan_data SET hasHideout=? WHERE clan_id=?");
            statement.setInt(1, getId());
            statement.setInt(2, getOwnerId());
            statement.execute();
            DbUtils.close(statement);

            statement = con.prepareStatement("DELETE FROM residence_functions WHERE id=?");
            statement.setInt(1, getId());
            statement.execute();
            DbUtils.close(statement);

            // Announce to clan memebers
            if (clan != null) {
                clan.setHasHideout(getId()); // Set has hideout flag for new owner
                clan.broadcastClanStatus(false, true, false);
            }
        } catch (Exception e) {
            _log.warn("Exception: updateOwnerInDB(L2Clan clan): " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public int getGrade() {
        return _grade;
    }

    @Override
    public void update() {
        ClanHallDAO.getInstance().update(this);
    }

    public int getAuctionLength() {
        return _auctionLength;
    }

    public void setAuctionLength(int auctionLength) {
        _auctionLength = auctionLength;
    }

    public String getAuctionDescription() {
        return _auctionDescription;
    }

    public void setAuctionDescription(String auctionDescription) {
        _auctionDescription = auctionDescription == null ? StringUtils.EMPTY : auctionDescription;
    }

    public long getAuctionMinBid() {
        return _auctionMinBid;
    }

    public void setAuctionMinBid(long auctionMinBid) {
        _auctionMinBid = auctionMinBid;
    }

    public long getRentalFee() {
        return _rentalFee;
    }

    public long getBaseMinBid() {
        return _minBid;
    }

    public long getDeposit() {
        return _deposit;
    }

    @Override
    public void chanceCycle() {
        super.chanceCycle();

        setPaidCycle(getPaidCycle() + 1);
        if (getPaidCycle() >= REWARD_CYCLE)
            if (_owner.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) > _rentalFee) {
                _owner.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, _rentalFee);
                setPaidCycle(0);
            } else {
                UnitMember member = _owner.getLeader();

                if (member.isOnline())
                    member.getPlayer().sendPacket(SystemMsg.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED);
                else
                    PlayerMessageStack.getInstance().mailto(member.getObjectId(), SystemMsg.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED.packet(null));

                changeOwner(null);
            }
    }
}