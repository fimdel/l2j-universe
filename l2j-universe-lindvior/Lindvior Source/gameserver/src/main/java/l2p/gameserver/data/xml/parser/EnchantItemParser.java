/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractDirParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.templates.item.support.AppearanceStone;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;

import java.io.File;
import java.util.Iterator;

public class EnchantItemParser extends AbstractDirParser<EnchantItemHolder> {
    private static EnchantItemParser _instance = new EnchantItemParser();

    private EnchantItemParser() {
        super(EnchantItemHolder.getInstance());
    }

    public static EnchantItemParser getInstance() {
        return _instance;
    }

    @Override
    public File getXMLDir() {
        return new File(Config.DATAPACK_ROOT, "data/stats/items/enchant/");
    }

    @Override
    public boolean isIgnored(File f) {
        return false;
    }

    @Override
    public String getDTDFileName() {
        return "enchant_items.dtd";
    }

    @Override
    protected void readData(org.dom4j.Element rootElement)
            throws Exception {
        for (Iterator iterator = rootElement.elementIterator("appearance_stone"); iterator.hasNext(); ) {
            org.dom4j.Element stoneElement = (org.dom4j.Element) iterator.next();
            int itemId = Integer.parseInt(stoneElement.attributeValue("id"));

            String[] targetTypesStr = stoneElement.attributeValue("target_type").split(",");
            AppearanceStone.ShapeTargetType[] targetTypes = new AppearanceStone.ShapeTargetType[targetTypesStr.length];
            for (int i = 0; i < targetTypesStr.length; i++) {
                targetTypes[i] = AppearanceStone.ShapeTargetType.valueOf(targetTypesStr[i].toUpperCase());
            }
            AppearanceStone.ShapeType type = AppearanceStone.ShapeType.valueOf(stoneElement.attributeValue("shifting_type").toUpperCase());

            String[] gradesStr = stoneElement.attributeValue("grade") == null ? new String[0] : stoneElement.attributeValue("grade").split(",");
            ItemGrade[] grades = new ItemGrade[gradesStr.length];
            for (int i = 0; i < gradesStr.length; i++) {
                grades[i] = ItemGrade.valueOf(gradesStr[i].toUpperCase());
            }
            long cost = stoneElement.attributeValue("cost") == null ? 0L : Long.parseLong(stoneElement.attributeValue("cost"));
            int extractItemId = stoneElement.attributeValue("extract_id") == null ? 0 : Integer.parseInt(stoneElement.attributeValue("extract_id"));

            String[] itemTypesStr = stoneElement.attributeValue("item_type") == null ? new String[0] : stoneElement.attributeValue("item_type").split(",");
            ExItemType[] itemTypes = new ExItemType[itemTypesStr.length];
            for (int i = 0; i < itemTypesStr.length; i++) {
                itemTypes[i] = ExItemType.valueOf(itemTypesStr[i].toUpperCase());
            }
            getHolder().addAppearanceStone(new AppearanceStone(itemId, targetTypes, type, grades, cost, extractItemId, itemTypes));
        }
    }
}
