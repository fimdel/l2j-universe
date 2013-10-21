package lineage2.gameserver.network.serverpackets;

public class TutorialShowHtml extends L2GameServerPacket
{

	/**
	 * <html><head><body><center> <font color="LEVEL">Quest</font> </center> <br>
	 * Speak to the <font color="LEVEL"> Paagrio Priests </font> of the Temple of Paagrio. They will explain the basics of combat through quests. <br>
	 * You must visit them, for they will give you a useful gift after you complete a quest. <br>
	 * They are marked in yellow on the radar, at the upper-right corner of the screen. You must visit them if you wish to advance. <br>
	 * <a action="link tutorial_close_0">Close Window</a> </body></html>
	 * <p/>
	 */
	private String _html;
	private int _type;
	public static final int TYPE_HTML = 1;
	public static final int TYPE_WINDOW = 2;
	public static final String QT_001 = "..\\L2Text\\QT_001_Radar_01.htm";
	public static final String QT_002 = "..\\L2Text\\QT_002_Guide_01.htm";
	public static final String QT_003 = "..\\L2Text\\QT_003_bullet_01.htm";
	public static final String QT_004 = "..\\L2Text\\QT_004_skill_01.htm";
	public static final String QT_009 = "..\\L2Text\\QT_009_enchant_01.htm";
	public static final String GUIDE_ADVENTURER = "..\\L2Text\\Guide_Ad.htm";

	public static final String GUIDE_40_50 = "..\\L2Text\\Guide_Ad_4050_01_ivorytower.htm";
	public static final String GUIDE_50_55 = "..\\L2Text\\Guide_Ad_5055_01_outlaws.htm";
	public static final String GUIDE_55_60 = "..\\L2Text\\Guide_Ad_5560_01_forsaken.htm";
	public static final String GUIDE_60_65 = "..\\L2Text\\Guide_Ad_6065_00_main.htm";
	public static final String GUIDE_65_70 = "..\\L2Text\\Guide_Ad_6570_00_main.htm";
	public static final String GUIDE_70_75 = "..\\L2Text\\Guide_Ad_7075_00_main.htm";
	public static final String GUIDE_75_80 = "..\\L2Text\\Guide_Ad_7580_00_main.htm";
	public static final String GUIDE_80_85 = "..\\L2Text\\Guide_Ad_8085_00_main.htm";

	public static final String GUIDE_AWAKING = "..\\L2text\\Guide_Aw.htm";
	public static final String AWAKING85_90 = "..\\L2text\\Guide_Aw_8590_00_main.htm";
	public static final String AWAKING90_95 = "..\\L2text\\Guide_Aw_9095_00_main.htm";
	public static final String AWAKING95_99 = "..\\L2text\\Guide_Aw_9599_00_main.htm";

	public TutorialShowHtml(String html)
	{
		_html = html;
	}

	public TutorialShowHtml(String html, int type)
	{
		_html = html;
		_type = type;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xa6);
		if (_type > 0)
			writeD(_type);
		writeS(_html);
	}
}