package l2p.gameserver.model.items.etcitems;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @author ALF
 * @date 27.06.2012
 */
public class LifeStoneManager {
    private static final Logger _log = LoggerFactory.getLogger(LifeStoneManager.class);
    private static TIntObjectHashMap<LifeStoneInfo> _stones = new TIntObjectHashMap<LifeStoneInfo>();

    public static void load() {
        _log.info("LifeStoneManager: Loading stone data...");

        int _itemId = 0;
        int _level = 0;
        LifeStoneGrade _grade = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);

        File file = new File(Config.DATAPACK_ROOT, "data/asc/model/etcitems/LifeStone.xml");
        Document doc = null;

        if (file.exists()) {
            try {
                doc = factory.newDocumentBuilder().parse(file);
            } catch (Exception e) {
                _log.warn("Could not parse AttributeStone.xml file: " + e.getMessage(), e);
                return;
            }

            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ("list".equalsIgnoreCase(n.getNodeName())) {
                    for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                        if ("item".equalsIgnoreCase(d.getNodeName())) {
                            NamedNodeMap attrs = d.getAttributes();
                            Node att;

                            att = attrs.getNamedItem("id");
                            if (att != null)
                                _itemId = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("level");
                            if (att != null)
                                _level = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("grade");
                            if (att != null)
                                _grade = LifeStoneGrade.valueOf(att.getNodeValue());

                            LifeStoneInfo lsi = new LifeStoneInfo();
                            lsi.setItemId(_itemId);
                            lsi.setLevel(_level);
                            lsi.setGrade(_grade);

                            _stones.put(_itemId, lsi);
                        }
                    }
                }
            }
        }
        _log.info("LifeStoneManager: Loaded " + _stones.size() + " stone data...");
    }

    public static LifeStoneInfo getStoneInfo(int itemId) {
        return _stones.get(itemId);
    }
}
