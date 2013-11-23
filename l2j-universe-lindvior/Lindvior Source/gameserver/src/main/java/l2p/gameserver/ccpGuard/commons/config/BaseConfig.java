/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.ccpGuard.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class BaseConfig {
    protected static Properties getSettings(String CONFIGURATION_FILE) throws Exception {
        Properties serverSettings = new Properties();
        InputStream is = new FileInputStream(new File(CONFIGURATION_FILE));
        serverSettings.load(is);
        is.close();
        return serverSettings;
    }

    protected static String getProperty(Properties prop, String name) {
        return prop.getProperty(name.trim(), null);
    }

    protected static String getProperty(Properties prop, String name, String _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : s;
    }

    protected static int getIntProperty(Properties prop, String name, int _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Integer.parseInt(s.trim());
    }

    protected static int getIntHexProperty(Properties prop, String name, int _default) {
        return (int) getLongHexProperty(prop, name, _default);
    }

    protected static long getLongProperty(Properties prop, String name, long _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Long.parseLong(s.trim());
    }

    protected static long getLongHexProperty(Properties prop, String name, long _default) {
        String s = getProperty(prop, name);
        if (s == null)
            return _default;
        s = s.trim();
        if (!s.startsWith("0x"))
            s = "0x" + s;
        return Long.decode(s);
    }

    protected static byte getByteProperty(Properties prop, String name, byte _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Byte.parseByte(s.trim());
    }

    protected static byte getByteProperty(Properties prop, String name, int _default) {
        return getByteProperty(prop, name, (byte) _default);
    }

    protected static boolean getBooleanProperty(Properties prop, String name, boolean _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Boolean.parseBoolean(s.trim());
    }

    protected static float getFloatProperty(Properties prop, String name, float _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Float.parseFloat(s.trim());
    }

    protected static float getFloatProperty(Properties prop, String name, double _default) {
        return getFloatProperty(prop, name, (float) _default);
    }

    protected static double getDoubleProperty(Properties prop, String name, double _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : Double.parseDouble(s.trim());
    }

    protected static int[] getIntArray(Properties prop, String name, int[] _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : parseCommaSeparatedIntegerArray(s.trim());
    }

    protected static float[] getFloatArray(Properties prop, String name, float[] _default) {
        String s = getProperty(prop, name);
        return s == null ? _default : parseCommaSeparatedFloatArray(s.trim());
    }

    protected static String[] getStringArray(Properties prop, String name, String[] _default, String delimiter) {
        String s = getProperty(prop, name);
        return s == null ? _default : s.split(delimiter);
    }

    protected static String[] getStringArray(Properties prop, String name, String[] _default) {
        return getStringArray(prop, name, _default, ",");
    }

    protected static float[] parseCommaSeparatedFloatArray(String s) {
        if (s.isEmpty())
            return new float[]{};
        String[] tmp = s.replaceAll(",", ";").split(";");
        float[] ret = new float[tmp.length];
        for (int i = 0; i < tmp.length; i++)
            ret[i] = Float.parseFloat(tmp[i]);
        return ret;
    }

    protected static int[] parseCommaSeparatedIntegerArray(String s) {
        if (s.isEmpty())
            return new int[]{};
        String[] tmp = s.replaceAll(",", ";").split(";");
        int[] ret = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++)
            ret[i] = Integer.parseInt(tmp[i]);
        return ret;
    }

    // it has no instancies
    protected BaseConfig() {
    }

/*	protected static void ipsLoad()
    {
		try
		{
			Properties ipsSettings = new Properties();
			InputStream is = new FileInputStream(new File(ADV_IP_FILE));
			ipsSettings.load(is);
			is.close();

			String NetMask;
			String ip;
			for(int i = 0; i < ipsSettings.size() / 2; i++)
			{
				NetMask = ipsSettings.getProperty("NetMask" + (i + 1));
				ip = ipsSettings.getProperty("IPAdress" + (i + 1));
				for(String mask : NetMask.split(","))
				{
					AdvIP advip = new AdvIP();
					advip.ipadress = ip;
					advip.ipmask = mask.split("/")[0];
					advip.bitmask = mask.split("/")[1];
					GAMEIPS.add(advip);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Error("Failed to Load " + ADV_IP_FILE + " File.");
		}
	}
*/


}
