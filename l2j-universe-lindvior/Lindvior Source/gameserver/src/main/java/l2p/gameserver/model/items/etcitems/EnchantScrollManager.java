/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.items.etcitems;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.Config;
import l2p.gameserver.templates.item.type.ItemGrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @author ALF
 * @date 28.06.2012
 */
public class EnchantScrollManager {
    private static final Logger _log = LoggerFactory.getLogger(EnchantScrollManager.class);

    private static TIntObjectHashMap<EnchantScrollInfo> _scrolls = new TIntObjectHashMap<EnchantScrollInfo>();

    public static void load() {
        _log.info("EnchantScrollManager: Loading stone data...");

        int _itemId = 0;
        EnchantScrollType _type = null;
        EnchantScrollTarget _target = null;
        ItemGrade _grade = null;
        int _chance = 0;
        int _min = 0;
        int _safe = 0;
        int _max = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);

        File file = new File(Config.DATAPACK_ROOT, "data/asc/model/etcitems/EnchantScroll.xml");
        Document doc = null;

        if (file.exists()) {
            try {
                doc = factory.newDocumentBuilder().parse(file);
            } catch (Exception e) {
                _log.warn("Could not parse EnchantScroll.xml file: " + e.getMessage(), e);
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

                            att = attrs.getNamedItem("type");
                            if (att != null)
                                _type = EnchantScrollType.valueOf(att.getNodeValue());

                            att = attrs.getNamedItem("target");
                            if (att != null)
                                _target = EnchantScrollTarget.valueOf(att.getNodeValue());

                            att = attrs.getNamedItem("grade");
                            if (att != null)
                                _grade = ItemGrade.valueOf(att.getNodeValue());

                            att = attrs.getNamedItem("chance");
                            if (att != null)
                                _chance = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("min");
                            if (att != null)
                                _min = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("safe");
                            if (att != null)
                                _safe = Integer.parseInt(att.getNodeValue());

                            att = attrs.getNamedItem("max");
                            if (att != null)
                                _max = Integer.parseInt(att.getNodeValue());

                            EnchantScrollInfo esi = new EnchantScrollInfo();
                            esi.setItemId(_itemId);
                            esi.setType(_type);
                            esi.setTarget(_target);
                            esi.setGrade(_grade);
                            esi.setChance(_chance);
                            esi.setMin(_min);
                            esi.setSafe(_safe);
                            esi.setMax(_max);

                            att = attrs.getNamedItem("targetItems");
                            if (att != null) {
                                String targetItems = att.getNodeValue();
                                String[] args = targetItems.split(";");

                                for (String a : args)
                                    esi.addTargetItems(Integer.parseInt(a));
                            }

                            _scrolls.put(_itemId, esi);
                        }
                    }
                }
            }
        }
        _log.info("EnchantScrollManager: Loaded " + _scrolls.size() + " scrolls data...");
    }

    public static EnchantScrollInfo getScrollInfo(int itemId) {
        return _scrolls.get(itemId);
    }

    public static int[] getEnchantScrollIds() {
        return _scrolls.keys();
    }
}
