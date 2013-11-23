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
 *         Приходит при нажатия вкладки "Регистрация", запращивает список вещей, которые можно положить в коммиссионный магазин
 *         Отправляет {@link l2p.gameserver.network.serverpackets.commission.ExResponseCommissionItemList}
 */
public class RequestCommissionRegistrableItemList extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
        // Do nothing
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        CommissionShopManager.getInstance().showRegistrableItems(player);
    }
}
