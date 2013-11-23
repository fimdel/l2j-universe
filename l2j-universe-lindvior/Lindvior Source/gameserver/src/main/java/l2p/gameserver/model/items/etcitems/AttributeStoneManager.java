package l2p.gameserver.model.items.etcitems;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.Config;
import l2p.gameserver.model.base.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


/**
 * @author ALF
 * @data 26.06.2012
 */
public class AttributeStoneManager {
    private static final Logger _log = LoggerFactory.getLogger(AttributeStoneManager.class);

    private static TIntObjectHashMap<AttributeStoneInfo> _stones = new TIntObjectHashMap<AttributeStoneInfo>();

    public static void load() {
        _log.info("AttributeStoneManager: Loading stone data...");

        int _id, _min_arm, _max_arm, _min_weap, _max_weap, _inc_arm, _inc_weap, _inc_weap_arm;
        Element _element;
        int _chance;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);

        File file = new File(Config.DATAPACK_ROOT, "data/asc/model/etcitems/AttributeStone.xml");
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

                            _id = 0;
                            _min_arm = 0;
                            _max_arm = 0;
                            _min_weap = 0;
                            _max_weap = 0;
                            _inc_arm = 0;
                            _inc_weap = 0;
                            _inc_weap_arm = 0;
                            _element = Element.NONE;
                            _chance = 0;

                            att = attrs.getNamedItem("id");
                            if (att != null)
                                _id = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("min_arm");
                            if (att != null)
                                _min_arm = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("max_arm");
                            if (att != null)
                                _max_arm = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("min_weap");
                            if (att != null)
                                _min_weap = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("max_weap");
                            if (att != null)
                                _max_weap = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("inc_arm");
                            if (att != null)
                                _inc_arm = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("inc_weap");
                            if (att != null)
                                _inc_weap = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("inc_weap_arm");
                            if (att != null)
                                _inc_weap_arm = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("element");
                            if (att != null)
                                _element = Element.getElementByName(att.getNodeValue());

                            att = attrs.getNamedItem("chance");
                            if (att != null)
                                _chance = Integer.parseInt(att.getNodeValue());

                            AttributeStoneInfo asi = new AttributeStoneInfo();
                            asi.setItemId(_id);

                            asi.setMinArmor(_min_arm);
                            asi.setMaxArmor(_max_arm);

                            asi.setMaxWeapon(_min_weap);
                            asi.setMaxWeapon(_max_weap);

                            asi.setIncArmor(_inc_arm);
                            asi.setIncWeapon(_inc_weap);
                            asi.setInсWeaponArmor(_inc_weap_arm);

                            asi.setElement(_element);

                            asi.setChance(_chance);

                            _stones.put(_id, asi);
                        }
                    }
                }
            }
        }

        _log.info("AttributeStoneManager: Loaded " + _stones.size() + " stone data...");

    }

    public static AttributeStoneInfo getStoneInfo(int itemId) {
        return _stones.get(itemId);
    }

    public static int[] getAttributeStoneIds() {
        return _stones.keys();

    }
}
