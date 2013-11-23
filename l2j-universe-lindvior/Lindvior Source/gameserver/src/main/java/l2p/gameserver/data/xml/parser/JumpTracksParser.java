package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractFileParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.JumpTracksHolder;
import l2p.gameserver.templates.jump.JumpPoint;
import l2p.gameserver.templates.jump.JumpTrack;
import l2p.gameserver.templates.jump.JumpWay;
import l2p.gameserver.utils.Location;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public final class JumpTracksParser extends AbstractFileParser<JumpTracksHolder> {
    private static final JumpTracksParser _instance = new JumpTracksParser();

    public static JumpTracksParser getInstance() {
        return _instance;
    }

    protected JumpTracksParser() {
        super(JumpTracksHolder.getInstance());
    }

    @Override
    public File getXMLFile() {
        return new File(Config.DATAPACK_ROOT, "data/jumping_tracks.xml");
    }

    @Override
    public String getDTDFileName() {
        return "jumping_tracks.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element trackElement = iterator.next();

            int trackId = Integer.parseInt(trackElement.attributeValue("id"));
            Location startLoc = Location.parse(trackElement);
            JumpTrack jumpTrack = new JumpTrack(trackId, startLoc);
            for (Iterator<Element> wayIterator = trackElement.elementIterator("way"); wayIterator.hasNext(); ) {
                Element wayElement = wayIterator.next();

                int wayId = Integer.parseInt(wayElement.attributeValue("id"));
                JumpWay jumpWay = new JumpWay(wayId);
                for (Iterator<Element> pointIterator = wayElement.elementIterator("point"); pointIterator.hasNext(); ) {
                    Element pointElement = pointIterator.next();

                    Location pointLoc = Location.parse(pointElement);
                    int nextWayId = Integer.parseInt(pointElement.attributeValue("next_way_id"));
                    jumpWay.addPoint(new JumpPoint(pointLoc, nextWayId));
                }

                jumpTrack.addWay(jumpWay);
            }

            getHolder().addTrack(jumpTrack);
        }
    }
}
