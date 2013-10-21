package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.scripts.Scripts.ScriptClassAndMethod;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the HTML parser in the client knowns these standard and non-standard tags and attributes VOLUMN UNKNOWN UL U TT TR TITLE TEXTCODE TEXTAREA TD TABLE
 * SUP SUB STRIKE SPIN SELECT RIGHT PRE P OPTION OL MULTIEDIT LI LEFT INPUT IMG I HTML H7 H6 H5 H4 H3 H2 H1 FONT EXTEND EDIT COMMENT COMBOBOX CENTER
 * BUTTON BR BODY BAR ADDRESS A SEL LIST VAR FORE READONL ROWS VALIGN FIXWIDTH BORDERCOLORLI BORDERCOLORDA BORDERCOLOR BORDER BGCOLOR BACKGROUND ALIGN
 * VALU READONLY MULTIPLE SELECTED TYP TYPE MAXLENGTH CHECKED SRC Y X QUERYDELAY NOSCROLLBAR IMGSRC B FG SIZE FACE COLOR DEFFON DEFFIXEDFONT WIDTH
 * VALUE TOOLTIP NAME MIN MAX HEIGHT DISABLED ALIGN MSG LINK HREF ACTION ClassId fstring
 */
// TODO [G1ta0] пересмотреть
public class NpcHtmlMessage extends L2GameServerPacket
{
	protected static final Logger _log = LoggerFactory.getLogger(NpcHtmlMessage.class);
	protected static final Pattern objectId = Pattern.compile("%objectId%");
	protected static final Pattern playername = Pattern.compile("%playername%");

	protected int _npcObjId;
	protected String _html;
	protected String _file = null;
	protected List<String> _replaces = new ArrayList<String>();
	protected boolean have_appends = false;

	public NpcHtmlMessage(Player player, int npcId, String filename, int val)
	{
		List<ScriptClassAndMethod> appends = Scripts.dialogAppends.get(npcId);
		if (appends != null && appends.size() > 0)
		{
			have_appends = true;
			if (filename != null && filename.equalsIgnoreCase("npcdefault.htm"))
				setHtml(""); // контент задается скриптами через DialogAppend_
			else
				setFile(filename);

			String replaces = "";

			// Добавить в конец странички текст, определенный в скриптах.
			Object[] script_args = new Object[] { new Integer(val) };
			for (ScriptClassAndMethod append : appends)
			{
				Object obj = Scripts.getInstance().callScripts(player, append.className, append.methodName, script_args);
				if (obj != null)
					replaces += obj;
			}

			if (!replaces.equals(""))
				replace("</body>", "\n" + Strings.bbParse(replaces) + "</body>");
		}
		else
			setFile(filename);
	}

	public NpcHtmlMessage(Player player, NpcInstance npc, String filename, int val)
	{
		this(player, npc.getNpcId(), filename, val);

		_npcObjId = npc.getObjectId();

		// FIXME [G1ta0] не есть истина, исправить
		player.setLastNpc(npc);

		replace("%npcId%", String.valueOf(npc.getNpcId()));
		replace("%npcname%", npc.getName());
	}

	public NpcHtmlMessage(Player player, NpcInstance npc)
	{
		if (npc == null)
		{
			_npcObjId = 5;
			player.setLastNpc(null);
		}
		else
		{
			_npcObjId = npc.getObjectId();
			player.setLastNpc(npc);
		}
	}

	public NpcHtmlMessage(int npcObjId)
	{
		_npcObjId = npcObjId;
	}

	public final NpcHtmlMessage setHtml(String text)
	{
		if (!text.contains("<html>"))
			text = "<html><body>" + text + "</body></html>"; // <title>Message:</title>
			                                                 // <br><br><br>
		_html = text;
		return this;
	}

	public final NpcHtmlMessage setFile(String file)
	{
		_file = file;
		if (_file.startsWith("data/html/"))
		{
			_log.info("NpcHtmlMessage: need fix : " + file, new Exception());
			_file = _file.replace("data/html/", "");
		}
		return this;
	}

	public NpcHtmlMessage replace(String pattern, String value)
	{
		if (pattern == null || value == null)
			return this;
		_replaces.add(pattern);
		_replaces.add(value);
		return this;
	}

	// <fstring></fstring> npcstring-?.dat
	public NpcHtmlMessage replaceNpcString(String pattern, NpcString npcString, Object... arg)
	{
		if (pattern == null)
			return this;
		if (npcString.getSize() != arg.length)
			throw new IllegalArgumentException("Not valid size of parameters: " + npcString);

		_replaces.add(pattern);
		_replaces.add(HtmlUtils.htmlNpcString(npcString, arg));
		return this;
	}

	@Override
	protected void writeImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		if (_file != null) // TODO может быть не очень хорошо здесь это
		                   // делать...
		{
			if (player.isGM())
				Functions.sendDebugMessage(player, "HTML: " + _file);
			String content = HtmCache.getInstance().getNotNull(_file, player);
			String content2 = HtmCache.getInstance().getNullable(_file, player);
			if (content2 == null)
				setHtml(have_appends && _file.endsWith(".htm") ? "" : content);
			else
				setHtml(content);
		}

		for (int i = 0; i < _replaces.size(); i += 2)
			_html = _html.replace(_replaces.get(i), _replaces.get(i + 1));

		if (_html == null)
			return;

		Matcher m = objectId.matcher(_html);
		if (m != null)
			_html = m.replaceAll(String.valueOf(_npcObjId));

		_html = playername.matcher(_html).replaceAll(player.getName());

		player.cleanBypasses(false);
		_html = player.encodeBypasses(_html, false);

		writeC(0x19);
		writeD(_npcObjId);
		writeS(_html);
		writeD(0x00);
	}
}