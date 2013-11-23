package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.ShuttleTemplateHolder;
import l2p.gameserver.templates.ShuttleTemplate;
import l2p.gameserver.templates.ShuttleTemplate.ShuttleDoor;
import l2p.gameserver.templates.StatsSet;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

/**
 * @author Bonux
 */
public final class ShuttleTemplateParser extends AbstractFileParser<ShuttleTemplateHolder> {
    private static final ShuttleTemplateParser _instance = new ShuttleTemplateParser();

    public static ShuttleTemplateParser getInstance() {
        return _instance;
    }

    protected ShuttleTemplateParser() {
        super(ShuttleTemplateHolder.getInstance());
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/shuttle_data.xml");
    }

    @Override
    public String getDTDFileName() {
        return "shuttle_data.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element shuttleElement = iterator.next();

            int shuttleId = Integer.parseInt(shuttleElement.attributeValue("id"));
            ShuttleTemplate template = new ShuttleTemplate(shuttleId);
            for (Iterator<Element> doorsIterator = shuttleElement.elementIterator("doors"); doorsIterator.hasNext(); ) {
                Element doorsElement = doorsIterator.next();
                for (Iterator<Element> doorIterator = doorsElement.elementIterator("door"); doorIterator.hasNext(); ) {
                    Element doorElement = doorIterator.next();

                    int doorId = Integer.parseInt(doorElement.attributeValue("id"));
                    StatsSet set = new StatsSet();
                    for (Iterator<Element> setIterator = doorElement.elementIterator("set"); setIterator.hasNext(); ) {
                        Element setElement = setIterator.next();
                        set.set(setElement.attributeValue("name"), setElement.attributeValue("value"));
                    }
                    template.addDoor(new ShuttleDoor(doorId, set));
                }

            }

            getHolder().addTemplate(template);
        }
    }
}
