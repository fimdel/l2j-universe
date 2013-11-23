package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.StaticObjectHolder;
import l2p.gameserver.templates.StaticObjectTemplate;
import l2p.gameserver.templates.StatsSet;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

/**
 * @author VISTALL
 * @date 22:21/09.03.2011
 */
public final class StaticObjectParser extends AbstractFileParser<StaticObjectHolder> {
    private static StaticObjectParser _instance = new StaticObjectParser();

    public static StaticObjectParser getInstance() {
        return _instance;
    }

    private StaticObjectParser() {
        super(StaticObjectHolder.getInstance());
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/staticobjects.xml");
    }

    @Override
    public String getDTDFileName() {
        return "staticobjects.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element staticObjectElement = iterator.next();

            StatsSet set = new StatsSet();
            set.set("uid", staticObjectElement.attributeValue("id"));
            set.set("stype", staticObjectElement.attributeValue("stype"));
            set.set("path", staticObjectElement.attributeValue("path"));
            set.set("map_x", staticObjectElement.attributeValue("map_x"));
            set.set("map_y", staticObjectElement.attributeValue("map_y"));
            set.set("name", staticObjectElement.attributeValue("name"));
            set.set("x", staticObjectElement.attributeValue("x"));
            set.set("y", staticObjectElement.attributeValue("y"));
            set.set("z", staticObjectElement.attributeValue("z"));
            set.set("spawn", staticObjectElement.attributeValue("spawn"));

            getHolder().addTemplate(new StaticObjectTemplate(set));
        }
    }
}
