package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;

/**
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии на какую либо категорию/подкатегорию в списке продаж,
 *         либо при нажатии кнопки "Поиск"
 *         Отправляет пакет ExResponseCommissionList, с параметром type = 3 (ALL_ITEMS)
 */
public class RequestCommissionList extends L2GameClientPacket {
    private int listType;
    private int category;
    private int rareType;
    private int itemGrade;
    private String searchName;

    @Override
    protected void readImpl() throws Exception {
        // type
        // 1 приходит, если нажата определенная категория: Оружие, броня, итд.
        // 2 приходит, если выбрана подкатегория.
        listType = readD();
        // Category.
        // Если предыдущее 1, то определяет тип отображаемой категории. Оружие = 0, доспехи = 1, итд.
        // Если предыдущее 2, значит была нажата определенная категория, и здесь приходит тип этой под-категории.
        category = readD();

        rareType = readD(); // -1 = все, 0 = обычный, 1 = редкий
        itemGrade = readD(); // -1 = все, 0 = без ранга, 1 = D .... 10 = R99
        searchName = readS(); // имя, заданное в строке поиска.
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        NpcInstance npc = player.getLastNpc();
        if (npc == null || !npc.isInRangeZ(npc, Creature.INTERACTION_DISTANCE)) {
            return;
        }

        CommissionShopManager.getInstance().showItems(listType, category, rareType, itemGrade, searchName, player);
    }
}
