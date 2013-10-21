package lineage2.gameserver.network.serverpackets;

import lineage2.commons.net.nio.impl.SendablePacket;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.templates.item.ItemTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class L2GameServerPacket extends SendablePacket<GameClient> implements IStaticPacket
{
	private static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class);

	@Override
	public final boolean write()
	{
		try
		{
			writeImpl();
			return true;
		}
		catch (Exception e)
		{
			_log.error("Client: " + getClient() + " - Failed writing: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		return false;
	}

	protected abstract void writeImpl();

	protected void writeEx(int value)
	{
		writeC(0xFE);
		writeH(value);
	}

	protected void writeD(boolean b)
	{
		writeD(b ? 1 : 0);
	}

	protected void writeC(boolean b)
	{
		writeC(b ? 1 : 0);
	}

	/**
	 */
	protected void writeDD(int[] values, boolean sendCount)
	{
		if (sendCount)
			getByteBuffer().putInt(values.length);
		for (int value : values)
			getByteBuffer().putInt(value);
	}

	protected void writeDD(int[] values)
	{
		writeDD(values, false);
	}

	protected void writeItemInfo(ItemInstance item)
	{
		writeItemInfo(item, item.getCount());
	}

	protected void writeItemInfo(ItemInstance item, long count)
	{
		// dddQhhhdhhhhddhhhhhhhhhhhhd
		writeD(item.getObjectId());
		writeD(item.getItemId());
		writeD(item.getEquipSlot());
		writeQ(count);
		writeH(item.getTemplate().getType2ForPackets());
		writeH(item.getCustomType1());
		writeH(item.isEquipped() ? 1 : 0);
		writeD(item.getBodyPart());
		writeH(item.getEnchantLevel());
		writeH(item.getCustomType2());
		writeD(item.getAugmentationId()); // L2WT TEST!!! D = [HH] [00 00] [00
		                                  // 00]
		writeD(item.getShadowLifeTime());
		writeD(item.getTemporalLifeTime());
		writeH(0x01); // L2WT GOD
		writeH(item.getAttackElement().getId());
		writeH(item.getAttackElementValue());
		writeH(item.getDefenceFire());
		writeH(item.getDefenceWater());
		writeH(item.getDefenceWind());
		writeH(item.getDefenceEarth());
		writeH(item.getDefenceHoly());
		writeH(item.getDefenceUnholy());
		writeH(item.getEnchantOptions()[0]);
		writeH(item.getEnchantOptions()[1]);
		writeH(item.getEnchantOptions()[2]);
		writeD(item.getVisualId());
	}

	protected void writeItemInfo(ItemInfo item)
	{
		writeItemInfo(item, item.getCount());
	}

	protected void writeItemInfo(ItemInfo item, long count)
	{
		writeD(item.getObjectId());
		writeD(item.getItemId());
		writeD(item.getEquipSlot());
		writeQ(count);
		writeH(item.getItem().getType2ForPackets());
		writeH(item.getCustomType1());
		writeH(item.isEquipped() ? 1 : 0);
		writeD(item.getItem().getBodyPart());
		writeH(item.getEnchantLevel());
		writeH(item.getCustomType2());
		writeD(item.getAugmentationId()); // L2WT TEST!!! D = [HH] [00 00] [00
		                                  // 00]
		// writeH(0x00); //??
		writeD(item.getShadowLifeTime());
		writeD(item.getTemporalLifeTime());
		writeH(0x01); // L2WT GOD
		writeH(item.getAttackElement());
		writeH(item.getAttackElementValue());
		writeH(item.getDefenceFire());
		writeH(item.getDefenceWater());
		writeH(item.getDefenceWind());
		writeH(item.getDefenceEarth());
		writeH(item.getDefenceHoly());
		writeH(item.getDefenceUnholy());
		writeH(item.getEnchantOptions()[0]);
		writeH(item.getEnchantOptions()[1]);
		writeH(item.getEnchantOptions()[2]);
		writeD(item.getVisualId());
	}

	protected void writeItemElements(MultiSellIngredient item)
	{
		if (item.getItemId() <= 0)
		{
			writeItemElements();
			return;
		}
		ItemTemplate i = ItemHolder.getInstance().getTemplate(item.getItemId());
		if (item.getItemAttributes().getValue() > 0)
		{
			if (i.isWeapon())
			{
				Element e = item.getItemAttributes().getElement();
				writeH(e.getId()); // attack element (-1 - none)
				writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e)); // attack element value
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
			}
			else if (i.isArmor())
			{
				writeH(-1); // attack element (-1 - none)
				writeH(0); // attack element value
				for (Element e : Element.VALUES)
					writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e));
			}
			else
				writeItemElements();
		}
		else
			writeItemElements();
	}

/*	protected void writeInfo(final MultiSellIngredient ingr, final boolean product)
	{
		final int itemId = ingr.getItemId();
		final ItemTemplate template = (itemId > 0) ? ItemHolder.getInstance().getTemplate(ingr.getItemId()) : null;

		writeD(itemId);

		if(product)
		{
			writeD((itemId > 0) ? template.getBodyPart() : 0);
		}

		writeH((itemId > 0) ? template.getType2ForPackets() : 0);
		writeQ(ingr.getItemCount());
		writeH(ingr.getItemEnchant());

		if(product)
		{
			writeD(ingr.getChance(true));
		}

		writeAugmentationInfo(ingr);
		writeItemElements(ingr);
	}

	protected void writeAugmentationInfo(final MultiSellIngredient ingr)
	{
		if(ingr.getAugmentationId() != 0)
		{
			final int augm = ingr.getAugmentationId();

			writeD(augm & 0x0000FFFF);
			writeD(augm >> 16);
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
		}
	}
*/
	protected void writeItemElements()
	{
		writeH(-1); // attack element (-1 - none)
		writeH(0x00); // attack element value
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
	}

	public String getType()
	{
		return "[S] " + getClass().getSimpleName();
	}

	@Override
	public L2GameServerPacket packet(Player player)
	{
		return this;
	}

}