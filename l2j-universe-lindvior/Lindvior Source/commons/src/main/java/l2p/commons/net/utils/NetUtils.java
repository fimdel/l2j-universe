package l2p.commons.net.utils;

import l2p.commons.net.AdvancedIp;

public class NetUtils
{
	private final static NetList PRIVATE = new NetList();

	static
	{
		PRIVATE.add(Net.valueOf("127.0.0.0/8"));
		PRIVATE.add(Net.valueOf("10.0.0.0/8"));
		PRIVATE.add(Net.valueOf("172.16.0.0/12"));
		PRIVATE.add(Net.valueOf("192.168.0.0/16"));
		PRIVATE.add(Net.valueOf("169.254.0.0/16"));
	}

	public final static boolean isInternalIP(String address)
	{
		return PRIVATE.isInRange(address);
	}

	public static Boolean CheckSubNet(String ip, AdvancedIp advip)
	{
		String[] temp = ip.split("\\.");
		String[] temp2 = advip.bitMask.split("\\.");
		String result = "";
		for(int i = 0; i < temp.length; i++)
		{
			result += (Integer.valueOf(temp[i]) & Integer.valueOf(temp2[i])) + ".";
		}
		return result.equals(advip.ipMask.replace("\\.", "") + ".");
	}
}