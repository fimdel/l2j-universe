/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.item;

import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.type.EtcItemType;

public final class EtcItemTemplate extends ItemTemplate {

    public EtcItemTemplate(StatsSet set) {
        super(set);
        type = set.getEnum("type", EtcItemType.class);
        _type1 = TYPE1_ITEM_QUESTITEM_ADENA;
        switch (getItemType()) {
            case QUEST:
                _type2 = TYPE2_QUEST;
                break;
            case MONEY:
                _type2 = TYPE2_MONEY;
                break;
            default:
                _type2 = TYPE2_OTHER;
                break;
        }
    }

    /**
     * Returns the type of Etc Item
     *
     * @return L2EtcItemType
     */
    @Override
    public EtcItemType getItemType() {
        return (EtcItemType) super.type;
    }

    /**
     * Returns the ID of the Etc item after applying the mask.
     *
     * @return int : ID of the EtcItem
     */
    @Override
    public long getItemMask() {
        return getItemType().mask();
    }

    @Override
    public final boolean isShadowItem() {
        return false;
    }

    @Override
    public final boolean canBeEnchanted() {
        return false;
    }
}