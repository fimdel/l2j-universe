package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.ArmorSetsHolder;
import l2p.gameserver.model.ArmorSet;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

public final class ArmorSetsParser extends AbstractFileParser<ArmorSetsHolder> {
    private static final ArmorSetsParser _instance = new ArmorSetsParser();

    public static ArmorSetsParser getInstance() {
        return _instance;
    }

    private ArmorSetsParser() {
        super(ArmorSetsHolder.getInstance());
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/armor_sets.xml");
    }

    @Override
    public String getDTDFileName() {
        return "armor_sets.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element element = iterator.next();
            if ("set".equalsIgnoreCase(element.getName())) {
                String[] chests = null, legs = null, head = null, gloves = null, feet = null, shield = null, shield_skills = null, enchant6skills = null;
                if (element.attributeValue("chests") != null)
                    chests = element.attributeValue("chests").split(";");
                if (element.attributeValue("legs") != null)
                    legs = element.attributeValue("legs").split(";");
                if (element.attributeValue("head") != null)
                    head = element.attributeValue("head").split(";");
                if (element.attributeValue("gloves") != null)
                    gloves = element.attributeValue("gloves").split(";");
                if (element.attributeValue("feet") != null)
                    feet = element.attributeValue("feet").split(";");
                if (element.attributeValue("shield") != null)
                    shield = element.attributeValue("shield").split(";");
                if (element.attributeValue("shield_skills") != null)
                    shield_skills = element.attributeValue("shield_skills").split(";");
                if (element.attributeValue("enchant6skills") != null)
                    enchant6skills = element.attributeValue("enchant6skills").split(";");

                ArmorSet armorSet = new ArmorSet(chests, legs, head, gloves, feet, shield, shield_skills, enchant6skills);
                for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext(); ) {
                    Element subElement = subIterator.next();
                    if ("set_skills".equalsIgnoreCase(subElement.getName())) {
                        int partsCount = Integer.parseInt(subElement.attributeValue("parts"));
                        String[] skills = subElement.attributeValue("skills").split(";");
                        armorSet.addSkills(partsCount, skills);
                    }
                }
                getHolder().addArmorSet(armorSet);
            }
        }
    }
}