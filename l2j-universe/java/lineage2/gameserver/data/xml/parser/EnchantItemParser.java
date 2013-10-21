package lineage2.gameserver.data.xml.parser;

import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.EnchantItemHolder;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate;

public class EnchantItemParser extends AbstractDirParser<EnchantItemHolder>
{
	private static EnchantItemParser _instance = new EnchantItemParser();

	private EnchantItemParser()
	{
		super(EnchantItemHolder.getInstance());
	}

	public static EnchantItemParser getInstance()
	{
		return _instance;
	}

	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/AppareanceStones/");
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "appearance_stones.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("appearance_stone"); iterator.hasNext(); )
		{
			Element stoneElement = iterator.next();
			int itemId = Integer.parseInt(stoneElement.attributeValue("id"));

			String[] targetTypesStr = stoneElement.attributeValue("target_type").split(",");
			AppearanceStone.ShapeTargetType[] targetTypes = new AppearanceStone.ShapeTargetType[targetTypesStr.length];
			for(int i = 0; i < targetTypesStr.length; i++)
			{
				targetTypes[i] = AppearanceStone.ShapeTargetType.valueOf(targetTypesStr[i].toUpperCase());
			}

			AppearanceStone.ShapeType type = AppearanceStone.ShapeType.valueOf(stoneElement.attributeValue("shifting_type").toUpperCase());

			String[] gradesStr = stoneElement.attributeValue("grade") == null ? new String[0] : stoneElement.attributeValue("grade").split(",");
			ItemTemplate.Grade[] grades = new ItemTemplate.Grade[gradesStr.length];
			for(int i = 0; i < gradesStr.length; i++)
			{
				grades[i] = ItemTemplate.Grade.valueOf(gradesStr[i].toUpperCase());
			}

			long cost = stoneElement.attributeValue("cost") == null ? 0L : Long.parseLong(stoneElement.attributeValue("cost"));
			int extractItemId = stoneElement.attributeValue("extract_id") == null ? 0 : Integer.parseInt(stoneElement.attributeValue("extract_id"));

			String[] itemTypesStr = stoneElement.attributeValue("item_type") == null ? new String[0] : stoneElement.attributeValue("item_type").split(",");
			ExItemType[] itemTypes = new ExItemType[itemTypesStr.length];
			for(int i = 0; i < itemTypesStr.length; i++)
			{
				itemTypes[i] = ExItemType.valueOf(itemTypesStr[i].toUpperCase());
			}

			getHolder().addAppearanceStone(new AppearanceStone(itemId, targetTypes, type, grades, cost, extractItemId, itemTypes));
		}
	}
}