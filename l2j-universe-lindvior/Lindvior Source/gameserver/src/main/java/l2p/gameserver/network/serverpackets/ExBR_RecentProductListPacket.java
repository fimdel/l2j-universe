/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.data.xml.holder.ProductHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.ProductItem;

import java.util.ArrayList;
import java.util.Collection;

public class ExBR_RecentProductListPacket extends L2GameServerPacket {
    private Collection<ProductItem> _products;

    public ExBR_RecentProductListPacket(Player activeChar) {
        _products = new ArrayList<ProductItem>();
        int[] products = activeChar.getRecentProductList();
        if (products != null) {
            for (int productId : products) {
                ProductItem product = ProductHolder.getInstance().getProduct(productId);
                if (product == null)
                    continue;

                _products.add(product);
            }
        }
    }

    @Override
    protected void writeImpl() {
        writeEx(0xDD);
        writeD(_products.size());
        for (ProductItem template : _products) {
            writeD(template.getProductId());    //product id
            writeH(template.getCategory());    //category 1 - enchant 2 - supplies  3 - decoration 4 - package 5 - other
            writeD(template.getPoints());    //points
            writeD(template.getTabId());    // show tab 2-th group - 1 показывает окошко про итем
            writeD((int) (template.getStartTimeSale() / 1000));    // start sale unix date in seconds
            writeD((int) (template.getEndTimeSale() / 1000));    // end sale unix date in seconds
            writeC(127);    // day week (127 = not daily goods)
            writeC(template.getStartHour());    // start hour
            writeC(template.getStartMin());    // start min
            writeC(template.getEndHour());    // end hour
            writeC(template.getEndMin());    // end min
            writeD(0);    // stock
            writeD(-1);    // max stock
        }
    }
}