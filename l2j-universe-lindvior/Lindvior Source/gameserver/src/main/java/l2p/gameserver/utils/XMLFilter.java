package l2p.gameserver.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.07.12
 * Time: 7:38
 */
public class XMLFilter implements FileFilter {
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(".xml");
    }
}
