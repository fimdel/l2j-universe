/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.model.Player;

/**
 * /**
 *
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии на итем в окне регистрации вещей на продажу.
 *         Отправляет {@link l2p.gameserver.network.serverpackets.commission.ExResponseCommissionInfo}
 */
public class RequestCommissionInfo extends L2GameClientPacket {
    private int itemObjId;

    @Override
    protected void readImpl() throws Exception {
        itemObjId = readD(); // id выбранного итема
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        CommissionShopManager.getInstance().showCommissionInfo(player, itemObjId);
    }
}
