package l2p.gameserver.model.base;

import l2p.gameserver.Config;
import l2p.gameserver.model.Creature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public enum BaseStats {
    STR {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getSTR();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : STRbonus[actor.getSTR()];
        }

        @Override
        public final double calcChanceMod(Creature actor) {
            return Math.min(2. - Math.sqrt(calcBonus(actor)), 1.); // не более 1
        }
    },
    INT {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getINT();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : INTbonus[actor.getINT()];
        }
    },
    DEX {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getDEX();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : DEXbonus[actor.getDEX()];
        }
    },
    WIT {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getWIT();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : WITbonus[actor.getWIT()];
        }
    },
    CON {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getCON();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : CONbonus[actor.getCON()];
        }
    },
    MEN {
        @Override
        public final int getStat(Creature actor) {
            return actor == null ? 1 : actor.getMEN();
        }

        @Override
        public final double calcBonus(Creature actor) {
            return actor == null ? 1. : MENbonus[actor.getMEN()];
        }
    },
    NONE;

    public static final BaseStats[] VALUES = values();

    protected static final Logger _log = LoggerFactory.getLogger(BaseStats.class);

    private static final int MAX_STAT_VALUE = 201;

    public static final double[] WITbonus = new double[MAX_STAT_VALUE];
    public static final double[] MENbonus = new double[MAX_STAT_VALUE];
    public static final double[] INTbonus = new double[MAX_STAT_VALUE];
    public static final double[] STRbonus = new double[MAX_STAT_VALUE];
    public static final double[] DEXbonus = new double[MAX_STAT_VALUE];
    public static final double[] CONbonus = new double[MAX_STAT_VALUE];

    public int getStat(Creature actor) {
        return 1;
    }

    public double calcBonus(Creature actor) {
        return 1.;
    }

    public double calcChanceMod(Creature actor) {
        return 2. - Math.sqrt(calcBonus(actor));
    }

    public static final BaseStats valueOfXml(String name) {
        name = name.intern();
        for (BaseStats s : VALUES)
            if (s.toString().equalsIgnoreCase(name)) {
                if (s == NONE) // для упрощения
                    return null;

                return s;
            }

        throw new NoSuchElementException("Unknown name '" + name + "' for enum BaseStats");
    }

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        File file = new File(Config.DATAPACK_ROOT, "data/BaseStatBonusData.xml");
        Document doc = null;

        try {
            doc = factory.newDocumentBuilder().parse(file);
        } catch (SAXException e) {
            _log.error("", e);
        } catch (IOException e) {
            _log.error("", e);
        } catch (ParserConfigurationException e) {
            _log.error("", e);
        }

        int i;
        double val;

        if (doc != null)
            for (Node z = doc.getFirstChild(); z != null; z = z.getNextSibling())
                for (Node n = z.getFirstChild(); n != null; n = n.getNextSibling()) {
                    if (n.getNodeName().equalsIgnoreCase("STR"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                STRbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                    if (n.getNodeName().equalsIgnoreCase("INT"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                INTbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                    if (n.getNodeName().equalsIgnoreCase("CON"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                CONbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                    if (n.getNodeName().equalsIgnoreCase("MEN"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                MENbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                    if (n.getNodeName().equalsIgnoreCase("DEX"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                DEXbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                    if (n.getNodeName().equalsIgnoreCase("WIT"))
                        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                            String node = d.getNodeName();
                            if (node.equalsIgnoreCase("stat")) {
                                i = Integer.valueOf(d.getAttributes().getNamedItem("value").getNodeValue());
                                val = Integer.valueOf(d.getAttributes().getNamedItem("bonus").getNodeValue());
                                WITbonus[i + 1] = (1000 + val) / 1000;
                            }
                        }
                }
    }
}