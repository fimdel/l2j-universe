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
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии вкладки "Регистрация", запрашивает список вещей, находящихся в коммиссионном магазине
 *         Отправляет {@link l2p.gameserver.network.serverpackets.commission.ExResponseCommissionList}
 */
public class RequestCommissionRegisteredItem extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
        // Trigger
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        CommissionShopManager.getInstance().showPlayerRegisteredItems(player);
    }
}
