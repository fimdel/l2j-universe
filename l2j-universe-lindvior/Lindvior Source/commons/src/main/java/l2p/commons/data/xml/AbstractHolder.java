package l2p.commons.data.xml;

import l2p.commons.logging.LoggerObject;

/**
 * Author: VISTALL
 * Date:  18:34/30.11.2010
 */
public abstract class AbstractHolder extends LoggerObject {
    public void log() {
        info(String.format("loaded %d%s(s) count.", size(), formatOut(getClass().getSimpleName().replace("Holder", "")).toLowerCase()));
    }

    protected void process() {

    }

    public abstract int size();

    public abstract void clear();

    private static String formatOut(String st) {
        char[] chars = st.toCharArray();
        StringBuffer buf = new StringBuffer(chars.length);

        for (char ch : chars) {
            if (Character.isUpperCase(ch))
                buf.append(" ");

            buf.append(Character.toLowerCase(ch));
        }

        return buf.toString();
    }
}