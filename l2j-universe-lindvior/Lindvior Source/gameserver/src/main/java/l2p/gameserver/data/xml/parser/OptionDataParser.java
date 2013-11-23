package l2p.gameserver.data.xml.parser;

import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.OptionDataHolder;
import l2p.gameserver.model.Skill;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.OptionDataTemplate;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

/**
 * @author VISTALL
 * @date 20:36/19.05.2011
 */
public final class OptionDataParser extends StatParser<OptionDataHolder> {
    private static final OptionDataParser _instance = new OptionDataParser();

    public static OptionDataParser getInstance() {
        return _instance;
    }

    protected OptionDataParser() {
        super(OptionDataHolder.getInstance());
    }

    @Override
    public File getXMLDir() {
        return new File(Config.DATAPACK_ROOT, "data/stats/option_data");
    }

    @Override
    public boolean isIgnored(File f) {
        return false;
    }

    @Override
    public String getDTDFileName() {
        return "option_data.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> itemIterator = rootElement.elementIterator(); itemIterator.hasNext(); ) {
            Element optionDataElement = itemIterator.next();
            OptionDataTemplate template = new OptionDataTemplate(Integer.parseInt(optionDataElement.attributeValue("id")));
            for (Iterator<Element> subIterator = optionDataElement.elementIterator(); subIterator.hasNext(); ) {
                Element subElement = subIterator.next();
                String subName = subElement.getName();
                if (subName.equalsIgnoreCase("for"))
                    parseFor(subElement, template);
                else if (subName.equalsIgnoreCase("triggers"))
                    parseTriggers(subElement, template);
                else if (subName.equalsIgnoreCase("skills"))
                    for (Iterator<Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext(); ) {
                        Element nextElement = nextIterator.next();
                        int id = Integer.parseInt(nextElement.attributeValue("id"));
                        int level = Integer.parseInt(nextElement.attributeValue("level"));

                        Skill skill = SkillTable.getInstance().getInfo(id, level);

                        if (skill != null)
                            template.addSkill(skill);
                        else
                            info("Skill not found(" + id + "," + level + ") for option data:" + template.getId() + "; file:" + getCurrentFileName());
                    }
            }
            getHolder().addTemplate(template);
        }
    }

    @Override
    protected Object getTableValue(String name) {
        return null;
    }
}
