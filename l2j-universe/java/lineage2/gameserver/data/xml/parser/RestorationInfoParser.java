package lineage2.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.RestorationInfoHolder;
import lineage2.gameserver.templates.skill.restoration.RestorationGroup;
import lineage2.gameserver.templates.skill.restoration.RestorationInfo;
import lineage2.gameserver.templates.skill.restoration.RestorationItem;

import org.dom4j.Element;

public final class RestorationInfoParser extends AbstractDirParser<RestorationInfoHolder>
{
	private static final RestorationInfoParser _instance = new RestorationInfoParser();

	public static RestorationInfoParser getInstance()
	{
		return _instance;
	}

  	protected RestorationInfoParser()
	{
  		super(RestorationInfoHolder.getInstance());
	}

	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/stats/skills/restoration_info");
	}

	public boolean isIgnored(File f)
	{
		return false;
	}

	public String getDTDFileName()
	{
		return "restoration_info.dtd";
	}

	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<?> restorationIterator = rootElement.elementIterator(); restorationIterator.hasNext(); )
		{
			Element restorationElement = (Element)restorationIterator.next();
			int skillId = Integer.parseInt(restorationElement.attributeValue("skill_id"));
			int skillLvl = Integer.parseInt(restorationElement.attributeValue("skill_level"));
			int consumeItemId = restorationElement.attributeValue("consume_item_id") == null ? -1 : Integer.parseInt(restorationElement.attributeValue("consume_item_id"));
			int consumeItemCount = restorationElement.attributeValue("consume_item_count") == null ? 1 : Integer.parseInt(restorationElement.attributeValue("consume_item_count"));
			RestorationInfo restorationInfo = new RestorationInfo(skillId, skillLvl, consumeItemId, consumeItemCount);
			for (Iterator<?> groupIterator = restorationElement.elementIterator(); groupIterator.hasNext(); )
			{
				Element groupElement = (Element)groupIterator.next();
				double chance = Double.parseDouble(groupElement.attributeValue("chance"));
				RestorationGroup restorationGroup = new RestorationGroup(chance);
				for (Iterator<?> itemIterator = groupElement.elementIterator(); itemIterator.hasNext(); )
				{
					Element itemElement = (Element)itemIterator.next();
					int id = Integer.parseInt(itemElement.attributeValue("id"));
					int minCount = Integer.parseInt(itemElement.attributeValue("min_count"));
					int maxCount = itemElement.attributeValue("max_count") == null ? minCount : Integer.parseInt(itemElement.attributeValue("max_count"));
					restorationGroup.addRestorationItem(new RestorationItem(id, minCount, maxCount));
				}
				restorationInfo.addRestorationGroup(restorationGroup);
			}
			(getHolder()).addRestorationInfo(restorationInfo);
		}
	}
}