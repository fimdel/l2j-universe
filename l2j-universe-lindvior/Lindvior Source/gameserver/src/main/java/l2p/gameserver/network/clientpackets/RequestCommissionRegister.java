package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии кнопки "Регистрация" в окне регистрации
 */
public class RequestCommissionRegister extends L2GameClientPacket {
    private int objectId;
    private String itemName;
    private long price, count;
    private int days;

    @SuppressWarnings("unused")
    @Override
    protected void readImpl() throws Exception {
        objectId = readD();// ObjectId
        itemName = readS(); // Item Name
        price = readQ();
        count = readQ();
        days = readD(); // 0 - 1 день, 1 - 3 дня, 2 - 5 дней, 3 - 7 дней.
        int unk1 = readD(); // always 0 
        int unk2 = readD(); // always 0
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }

        NpcInstance npc = player.getLastNpc();
        if (npc == null || !npc.isInRangeZ(npc, Creature.INTERACTION_DISTANCE)) {
            return;
        }

        CommissionShopManager.getInstance().registerItem(player, objectId, itemName, price, count, days);

    }
}
