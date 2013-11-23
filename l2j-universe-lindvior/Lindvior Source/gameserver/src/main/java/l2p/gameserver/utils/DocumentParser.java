package l2p.gameserver.utils;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.07.12
 * Time: 7:35
 */
public abstract class DocumentParser {
    protected final Logger _log;
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final XMLFilter xmlFilter = new XMLFilter();
    private File _currentFile;
    private Document _currentDocument;

    public DocumentParser() {
        this._log = LogManager.getLogger(DocumentParser.class);
    }

    public abstract void load();

    protected void parseFile(File f) {
        if (!xmlFilter.accept(f)) {
            this._log.log(Level.WARN, "Could not parse " + f.getName() + " is not a file or it doesn't exist!");
            return;
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setIgnoringComments(true);
        this._currentDocument = null;
        this._currentFile = f;
        try {
            dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new XMLErrorHandler());
            this._currentDocument = db.parse(f);
        } catch (Exception e) {
            this._log.log(Level.ERROR, "Could not parse " + f.getName() + " file: " + e.getMessage());
            return;
        }
        parseDocument();
    }

    public File getCurrentFile() {
        return this._currentFile;
    }

    protected Document getCurrentDocument() {
        return this._currentDocument;
    }

    protected boolean parseDirectory(String path) {
        return parseDirectory(new File(path));
    }

    protected boolean parseDirectory(File dir) {
        if (!dir.exists()) {
            this._log.log(Level.WARN, "Folder " + dir.getAbsolutePath() + " doesn't exist!");
            return false;
        }

        List<File> listOfFiles = Util.getAllFileList(dir, "xml");
        for (File f : listOfFiles) {
            parseFile(f);
        }
        return true;
    }

    protected void parseDocument(Document doc) {
    }

    protected abstract void parseDocument();

    protected static int parseInt(NamedNodeMap n, String name) {
        return Integer.parseInt(n.getNamedItem(name).getNodeValue());
    }

    protected static Integer parseInteger(NamedNodeMap n, String name) {
        return Integer.valueOf(n.getNamedItem(name).getNodeValue());
    }

    protected static int parseInt(Node n) {
        return Integer.parseInt(n.getNodeValue());
    }

    protected static Integer parseInteger(Node n) {
        return Integer.valueOf(n.getNodeValue());
    }

    protected static Long parseLong(NamedNodeMap n, String name) {
        return Long.valueOf(n.getNamedItem(name).getNodeValue());
    }

    protected static Long parseLong(Node n) {
        return Long.valueOf(n.getNodeValue());
    }

    protected static boolean parseBoolean(NamedNodeMap n, String name) {
        Node b = n.getNamedItem(name);
        return (b != null) && (Boolean.parseBoolean(b.getNodeValue()));
    }

    protected static boolean parseBoolean(Node n) {
        return Boolean.parseBoolean(n.getNodeValue());
    }

    protected static byte parseByte(Node n) {
        return Byte.valueOf(n.getNodeValue());
    }

    protected static double parseDouble(NamedNodeMap n, String name) {
        return Double.valueOf(n.getNamedItem(name).getNodeValue());
    }

    protected static float parseFloat(NamedNodeMap n, String name) {
        return Float.valueOf(n.getNamedItem(name).getNodeValue());
    }

    protected static float parseFloat(Node n) {
        return Float.valueOf(n.getNodeValue());
    }

    protected class XMLErrorHandler
            implements ErrorHandler {
        protected XMLErrorHandler() {
        }

        public void warning(SAXParseException e) throws SAXParseException {
            throw e;
        }

        public void error(SAXParseException e)
                throws SAXParseException {
            throw e;
        }

        public void fatalError(SAXParseException e)
                throws SAXParseException {
            throw e;
        }
    }
}
