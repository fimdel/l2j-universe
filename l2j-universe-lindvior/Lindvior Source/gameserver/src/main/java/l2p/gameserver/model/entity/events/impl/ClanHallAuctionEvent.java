package l2p.gameserver.model.entity.events.impl;

import l2p.commons.collections.MultiValueSet;
import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.dao.SiegeClanDAO;
import l2p.gameserver.instancemanager.PlayerMessageStack;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.actions.StartStopAction;
import l2p.gameserver.model.entity.events.objects.AuctionSiegeClanObject;
import l2p.gameserver.model.entity.events.objects.SiegeClanObject;
import l2p.gameserver.model.entity.residence.ClanHall;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.tables.ClanTable;
import l2p.gameserver.templates.item.ItemTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author VISTALL
 * @date 15:24/14.02.2011
 */
public class ClanHallAuctionEvent extends SiegeEvent<ClanHall, AuctionSiegeClanObject> {
    private Calendar _endSiegeDate = Calendar.getInstance();

    public ClanHallAuctionEvent(MultiValueSet<String> set) {
        super(set);
    }

    @Override
    public void reCalcNextTime(boolean onStart) {
        clearActions();
        _onTimeActions.clear();

        Clan owner = getResidence().getOwner();

        _endSiegeDate.setTimeInMillis(0);
        // первый старт
        if (getResidence().getAuctionLength() == 0 && owner == null) {
            getResidence().getSiegeDate().setTimeInMillis(System.currentTimeMillis());
            getResidence().getSiegeDate().set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            getResidence().getSiegeDate().set(Calendar.HOUR_OF_DAY, 15);
            getResidence().getSiegeDate().set(Calendar.MINUTE, 0);
            getResidence().getSiegeDate().set(Calendar.SECOND, 0);
            getResidence().getSiegeDate().set(Calendar.MILLISECOND, 0);

            getResidence().setAuctionLength(7);
            getResidence().setAuctionMinBid(getResidence().getBaseMinBid());
            getResidence().setJdbcState(JdbcEntityState.UPDATED);
            getResidence().update();

            _onTimeActions.clear();
            addOnTimeAction(0, new StartStopAction(EVENT, true));
            addOnTimeAction(getResidence().getAuctionLength() * 86400, new StartStopAction(EVENT, false));

            _endSiegeDate.setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis() + getResidence().getAuctionLength() * 86400000L);

            registerActions();
        } else if (getResidence().getAuctionLength() == 0 && owner != null) {
            // КХ куплен
        } else {
            long endDate = getResidence().getSiegeDate().getTimeInMillis() + getResidence().getAuctionLength() * 86400000L;
            // дата окончания далека от текущей деты
            if (endDate <= System.currentTimeMillis())
                getResidence().getSiegeDate().setTimeInMillis(System.currentTimeMillis());

            _endSiegeDate.setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis() + getResidence().getAuctionLength() * 86400000L);

            _onTimeActions.clear();
            addOnTimeAction(0, new StartStopAction(EVENT, true));
            addOnTimeAction(getResidence().getAuctionLength() * 86400, new StartStopAction(EVENT, false));

            registerActions();
        }
    }

    @Override
    public void stopEvent(boolean step) {
        List<AuctionSiegeClanObject> siegeClanObjects = removeObjects(ATTACKERS);
        // сортуруем с Макс к мин
        AuctionSiegeClanObject[] clans = siegeClanObjects.toArray(new AuctionSiegeClanObject[siegeClanObjects.size()]);
        Arrays.sort(clans, SiegeClanObject.SiegeClanComparatorImpl.getInstance());

        Clan oldOwner = getResidence().getOwner();
        AuctionSiegeClanObject winnerSiegeClan = clans.length > 0 ? clans[0] : null;

        // если есть победитель(тоисть больше 1 клана)
        if (winnerSiegeClan != null) {
            // розсылаем мессагу, возращаем всем деньги
            SystemMessage2 msg = new SystemMessage2(SystemMsg.THE_CLAN_HALL_WHICH_WAS_PUT_UP_FOR_AUCTION_HAS_BEEN_AWARDED_TO_S1_CLAN).addString(winnerSiegeClan.getClan().getName());
            for (AuctionSiegeClanObject $siegeClan : siegeClanObjects) {
                Player player = $siegeClan.getClan().getLeader().getPlayer();
                if (player != null)
                    player.sendPacket(msg);
                else
                    PlayerMessageStack.getInstance().mailto($siegeClan.getClan().getLeaderId(), msg);

                if ($siegeClan != winnerSiegeClan) {
                    long returnBid = $siegeClan.getParam() - (long) ($siegeClan.getParam() * 0.1);

                    $siegeClan.getClan().getWarehouse().addItem(ItemTemplate.ITEM_ID_ADENA, returnBid);
                }
            }

            SiegeClanDAO.getInstance().delete(getResidence());

            // если был овнер, возращаем депозит
            if (oldOwner != null)
                oldOwner.getWarehouse().addItem(ItemTemplate.ITEM_ID_ADENA, getResidence().getDeposit());

            getResidence().setAuctionLength(0);
            getResidence().setAuctionMinBid(0);
            getResidence().setAuctionDescription(StringUtils.EMPTY);
            getResidence().getSiegeDate().setTimeInMillis(0);
            getResidence().getLastSiegeDate().setTimeInMillis(0);
            getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
            getResidence().setJdbcState(JdbcEntityState.UPDATED);

            getResidence().changeOwner(winnerSiegeClan.getClan());
            getResidence().startCycleTask();
        } else {
            if (oldOwner != null) {
                Player player = oldOwner.getLeader().getPlayer();
                if (player != null)
                    player.sendPacket(SystemMsg.THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED);
                else
                    PlayerMessageStack.getInstance().mailto(oldOwner.getLeaderId(), SystemMsg.THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED.packet(null));
            }
        }

        super.stopEvent(step);
    }

    @Override
    public boolean isParticle(Player player) {
        return false;
    }

    @Override
    public AuctionSiegeClanObject newSiegeClan(String type, int clanId, long param, long date) {
        Clan clan = ClanTable.getInstance().getClan(clanId);
        return clan == null ? null : new AuctionSiegeClanObject(type, clan, param, date);
    }

    public Calendar getEndSiegeDate() {
        return _endSiegeDate;
    }
}
