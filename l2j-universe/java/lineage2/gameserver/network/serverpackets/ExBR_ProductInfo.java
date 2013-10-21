package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.ProductItem;
import lineage2.gameserver.model.ProductItemComponent;

public class ExBR_ProductInfo extends L2GameServerPacket
{
	private ProductItem _productId;

	public ExBR_ProductInfo(int id)
	{
		_productId = ProductHolder.getInstance().getProduct(id);
	}

	@Override
	protected void writeImpl()
	{
		if (_productId == null)
			return;

		writeEx(0xD8);

		writeD(_productId.getProductId()); // product id
		writeD(_productId.getPoints()); // points
		writeD(_productId.getComponents().size()); // size

		for (ProductItemComponent com : _productId.getComponents())
		{
			writeD(com.getItemId()); // item id
			writeD(com.getCount()); // quality
			writeD(com.getWeight()); // weight
			writeD(com.isDropable() ? 1 : 0); // 0 - dont drop/trade
		}
	}
}