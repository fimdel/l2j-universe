package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.WalkerRoutesHolder;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;


public final class WalkerRoutesParser extends AbstractFileParser<WalkerRoutesHolder> {
    private static final WalkerRoutesParser _instance = new WalkerRoutesParser();

    public static WalkerRoutesParser getInstance() {
        return _instance;
    }

    protected WalkerRoutesParser() {
        super(WalkerRoutesHolder.getInstance());
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/routes/walker_routes.xml");
    }

    @Override
    public String getDTDFileName() {
        return "walker_routes.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> routeIterator = rootElement.elementIterator(); routeIterator.hasNext(); ) {
            Element routeElement = routeIterator.next();
            if (routeElement.getName().equalsIgnoreCase("route")) {
                int npcId = Integer.parseInt(routeElement.attributeValue("npcId"));
                RouteType type = RouteType.valueOf(routeElement.attributeValue("type"));
                long baseDelay = Long.parseLong(routeElement.attributeValue("delay"));
                boolean isRunning = Boolean.parseBoolean(routeElement.attributeValue("isRunning"));
                int walkRange = Integer.parseInt(routeElement.attributeValue("walkRange"));

                WalkerRouteTemplate template = new WalkerRouteTemplate(npcId, baseDelay, type, isRunning, walkRange);

                for (Iterator<Element> subIterator = routeElement.elementIterator(); subIterator.hasNext(); ) {
                    Element subElement = subIterator.next();
                    if (subElement.getName().equalsIgnoreCase("point")) {
                        int x = Integer.parseInt(subElement.attributeValue("x"));
                        int y = Integer.parseInt(subElement.attributeValue("y"));
                        int z = Integer.parseInt(subElement.attributeValue("z"));
                        int h = subElement.attributeValue("h") == null ? -1 : Integer.parseInt(subElement.attributeValue("h"));
                        long delay = subElement.attributeValue("delay") == null ? 0 : Long.parseLong(subElement.attributeValue("delay"));
                        boolean end = subElement.attributeValue("endPoint") != null && Boolean.parseBoolean(routeElement.attributeValue("endPoint"));
                        //String npcString = subElement.attributeValue("npcString"); - for npcString Message,

                        template.setRoute(x, y, z, h, delay, end);
                    }
                }

                getHolder().addSpawn(template);
            }
        }
    }
}
