/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test.impl;

import l2p.gameserver.data.xml.holder.BuyListHolder;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.instancemanager.CastleManorManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.instances.MerchantInstance;
import l2p.gameserver.model.items.TradeItem;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.templates.manor.SeedProduction;

import java.util.List;
import java.util.Map;

public class ManorMenuSelect extends BypassHandler {
    public String[] getBypassList() {
        return new String[]{"manor_menu_select"};
    }

    public void onBypassCommand(String command, Player activeChar, Creature target) {
        if (CastleManorManager.getInstance().isUnderMaintenance()) {
            activeChar.sendPacket(ActionFail.STATIC, SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
            return;
        }

        Map params = parseBypass(command);

        if ((params.size() < 4) || (target == null) || (!target.isNpc())) {
            return;
        }
        MerchantInstance npc = (MerchantInstance) target;

        int ask = Integer.parseInt((String) params.get("ask"));
        int state = Integer.parseInt((String) params.get("state"));
        int time = Integer.parseInt((String) params.get("time"));

        Castle castle = npc.getCastle();
        int castleId;
        if (state == -1) {
            castleId = castle.getId();
        } else {
            castleId = state;
        }
        switch (ask) {
            case 1:
                if (castleId != castle.getId()) {
                    activeChar.sendPacket(SystemMessageId._HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR);
                } else {
                    BuyListHolder.NpcTradeList tradeList = new BuyListHolder.NpcTradeList(0);
                    List<SeedProduction> seeds = castle.getSeedProduction(0);

                    for (SeedProduction s : seeds) {
                        TradeItem item = new TradeItem();
                        item.setItemId(s.getId());
                        item.setOwnersPrice(s.getPrice());
                        item.setCount(s.getCanProduce());
                        if ((item.getCount() > 0L) && (item.getOwnersPrice() > 0L)) {
                            tradeList.addItem(item);
                        }
                    }
                    BuyListSeed bl = new BuyListSeed(tradeList, castleId, activeChar.getAdena());
                    activeChar.sendPacket(bl);
                }
                break;
            case 2:
                activeChar.sendPacket(new ExShowSellCropList(activeChar, castleId, castle.getCropProcure(0)));
                break;
            case 3:
                if ((time == 1) && (!((Castle) ResidenceHolder.getInstance().getResidence(Castle.class, castleId)).isNextPeriodApproved()))
                    activeChar.sendPacket(new ExShowSeedInfo(castleId, castle.getSeedProduction(0)));
                else
                    activeChar.sendPacket(new ExShowSeedInfo(castleId, ((Castle) ResidenceHolder.getInstance().getResidence(Castle.class, castleId)).getSeedProduction(time)));
                break;
            case 4:
                if ((time == 1) && (!((Castle) ResidenceHolder.getInstance().getResidence(Castle.class, castleId)).isNextPeriodApproved()))
                    activeChar.sendPacket(new ExShowCropInfo(castleId, castle.getCropProcure(0)));
                else
                    activeChar.sendPacket(new ExShowCropInfo(castleId, ((Castle) ResidenceHolder.getInstance().getResidence(Castle.class, castleId)).getCropProcure(time)));
                break;
            case 5:
                activeChar.sendPacket(new ExShowManorDefaultInfo());
                break;
            case 6:
                npc.showShopWindow(activeChar, 3 + npc.getNpcId(), false);
                break;
            case 9:
                activeChar.sendPacket(new ExShowProcureCropDetail(state));
            case 7:
            case 8:
        }
    }
}
