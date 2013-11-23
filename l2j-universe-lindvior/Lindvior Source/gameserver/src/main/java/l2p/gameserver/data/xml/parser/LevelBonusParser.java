package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.LevelBonusHolder;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

public final class LevelBonusParser extends AbstractFileParser<LevelBonusHolder> {
    private static final LevelBonusParser _instance = new LevelBonusParser();

    public static LevelBonusParser getInstance() {
        return _instance;
    }

    private LevelBonusParser() {
        super(LevelBonusHolder.getInstance());
    }

    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/stats/pc_parameters/lvl_bonus_data.xml");
    }

    public String getDTDFileName() {
        return "lvl_bonus_data.dtd";
    }

    protected void readData(Element rootElement)
            throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element element = iterator.next();
            if ("lvl_bonus".equalsIgnoreCase(element.getName())) {
                for (Element e : element.elements()) {
                    int lvl = Integer.parseInt(e.attributeValue("lvl"));
                    double bonusMod = Double.parseDouble(e.attributeValue("value"));
                    (getHolder()).addLevelBonus(lvl, bonusMod);
                }
            }
        }
    }
}
