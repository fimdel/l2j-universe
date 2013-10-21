package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.ProductItem;
import java.util.Collection;

public class ExBR_ProductList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0xD7);
		writeD(0);
		Collection<ProductItem> items = ProductHolder.getInstance().getAllItems();
		writeD(items.size());
		if (getClient().getActiveChar().isGM() && getClient().getActiveChar().isDebug())
		{
			getClient().getActiveChar().sendMessage("size:" + items.size());
		}
		for (ProductItem template : items)
		{
			if (System.currentTimeMillis() < template.getStartTimeSale())
				continue;
			if (System.currentTimeMillis() > template.getEndTimeSale())
				continue;
			writeD(template.getProductId()); // product id
			writeH(template.getCategory()); // category: 1 - enchant; 2 - supplies; 3 - decoration; 4 - package 5 - other
			writeD(template.getPoints()); // points
			writeD(0);
			writeD(template.getTabId());	//4 Popular - 2 Reccomended - 1 Event
			writeD((int) (template.getStartTimeSale() / 1000)); // start sale unix date in seconds
			writeD((int) (template.getEndTimeSale() / 1000)); // end sale unix date in seconds
			writeC(127); // day week (127 = not daily goods)
			writeC(template.getStartHour()); // start hour
			writeC(template.getStartMin()); // start min
			writeC(template.getEndHour()); // end hour
			writeC(template.getEndMin()); // end min
			writeD(0); // buyed stock
			writeD(-1); // max stock
			if (getClient().getRevision()>479)
				writeD(0); // ?
			writeD(1); // Sale % or Min Level ?
			int i = 0;
			while (i < template.getComponents().size())
			{
				writeD(template.getComponents().size() - i); // Component Number
				writeD(template.getComponents().get(i).getItemId()); // item id
				writeD(template.getComponents().get(i).getCount()); // item Count
				writeD(ItemHolder.getInstance().getTemplate(template.getComponents().get(i).getItemId()).getWeight()); // weight
				i++;
			}
			writeD(0); // ?
		}
	}
}