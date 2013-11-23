/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.commons.net.nio.impl.SendablePacket;
import l2p.gameserver.Config;
import l2p.gameserver.GameServer;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Element;
import l2p.gameserver.model.base.MultiSellIngredient;
import l2p.gameserver.model.items.ItemInfo;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.ServerPacket;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.templates.item.ItemTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class L2GameServerPacket extends SendablePacket<GameClient> implements IStaticPacket {
    private static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class);

    @Override
    public final boolean write() {
        try {
            writeImpl();
            return true;
        } catch (Exception e) {
            _log.error("Client: " + getClient() + " - Failed writing: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
        }
        return false;
    }

    protected abstract void writeImpl();

    protected void writeEx(int value) {
        writeC(0xFE);
        writeH(value);
    }

    protected void writeEx(ServerPacket s) {
        writeC(s.getId());
        if (s.getEx() != 0)
            writeH(s.getEx() - 1);
    }

    protected void writeEx449(int value) {
        writeC(0xFE);
        if (Config.MAX_PROTOCOL_REVISION >= 449)
            writeH(value + 1);
        else
            writeH(value);
    }

    protected void writeD(boolean b) {
        writeD(b ? 1 : 0);
    }

    protected void writeC(boolean b) {
        writeC(b ? 1 : 0);
    }

    /**
     * Отсылает число позиций + массив
     */
    protected void writeDD(int[] values, boolean sendCount) {
        if (sendCount)
            getByteBuffer().putInt(values.length);
        for (int value : values)
            getByteBuffer().putInt(value);
    }

    protected void writeDD(int[] values) {
        writeDD(values, false);
    }

    protected void writeItemInfo(ItemInstance item) {
        writeItemInfo(item, item.getCount());
    }

    protected void writeItemInfo(ItemInstance item, long count) {
        //dddQhhhdhhhhddhhhhhhhhhhhhd
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
        writeD(item.getAugmentationId()); // L2WT TEST!!! D = [HH] [00 00] [00 00]
        writeD(item.getShadowLifeTime());
        writeD(item.getTemporalLifeTime());
        writeH(0x01); //L2WT GOD
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

    protected void writeItemInfo(ItemInfo item) {
        writeItemInfo(item, item.getCount());
    }

    protected void writeItemInfo(ItemInfo item, long count) {
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
        writeD(item.getAugmentationId()); // L2WT TEST!!! D = [HH] [00 00] [00 00]
        //    writeH(0x00); //??
        writeD(item.getShadowLifeTime());
        writeD(item.getTemporalLifeTime());
        writeH(0x01); //L2WT GOD
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

    protected void writeItemElements(MultiSellIngredient item) {
        if (item.getItemId() <= 0) {
            writeItemElements();
            return;
        }
        ItemTemplate i = ItemHolder.getInstance().getTemplate(item.getItemId());
        if (item.getItemAttributes().getValue() > 0) {
            if (i.isWeapon()) {
                Element e = item.getItemAttributes().getElement();
                writeH(e.getId()); // attack element (-1 - none)
                writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e)); // attack element value
                writeH(0); // водная стихия (fire pdef)
                writeH(0); // огненная стихия (water pdef)
                writeH(0); // земляная стихия (wind pdef)
                writeH(0); // воздушная стихия (earth pdef)
                writeH(0); // темная стихия (holy pdef)
                writeH(0); // светлая стихия (dark pdef)
            } else if (i.isArmor()) {
                writeH(-1); // attack element (-1 - none)
                writeH(0); // attack element value
                for (Element e : Element.VALUES)
                    writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e));
            } else
                writeItemElements();
        } else
            writeItemElements();
    }

    protected void writeItemElements() {
        writeH(-1); // attack element (-1 - none)
        writeH(0x00); // attack element value
        writeH(0x00); // водная стихия (fire pdef)
        writeH(0x00); // огненная стихия (water pdef)
        writeH(0x00); // земляная стихия (wind pdef)
        writeH(0x00); // воздушная стихия (earth pdef)
        writeH(0x00); // темная стихия (holy pdef)
        writeH(0x00); // светлая стихия (dark pdef)
    }

    public String getType() {
        return "[S] " + getClass().getSimpleName();
    }

    public L2GameServerPacket packet(Player player) {
        return this;
    }

}
