/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.admincommands.AdminCommandHandler;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Bash extends Functions implements IAdminCommandHandler, ScriptFile
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Bash.class);
	
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_bashreload.
		 */
		admin_bashreload
	}
	
	/**
	 * Field wrongPage.
	 */
	private static String wrongPage = "scripts/services/Bash-wrongPage.htm";
	/**
	 * Field notPage.
	 */
	private static String notPage = "scripts/services/Bash-notPage.htm";
	/**
	 * Field readPage.
	 */
	private static String readPage = "scripts/services/Bash-readPage.htm";
	/**
	 * Field xmlData.
	 */
	private static String xmlData = Config.DATAPACK_ROOT + "/data/bash.xml";
	/**
	 * Field quotes.
	 */
	private static List<String> quotes = new ArrayList<>();
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().IsEventGm)
		{
			return false;
		}
		loadData();
		activeChar.sendMessage("Bash service reloaded.");
		return true;
	}
	
	/**
	 * Method showQuote.
	 * @param var String[]
	 */
	public void showQuote(String[] var)
	{
		Player player = getSelf();
		NpcInstance lastNpc = player.getLastNpc();
		if (!NpcInstance.canBypassCheck(player, lastNpc))
		{
			return;
		}
		int page = 1;
		int totalPages = quotes.size();
		try
		{
			page = Integer.parseInt(var[0]);
		}
		catch (NumberFormatException e)
		{
			show(HtmCache.getInstance().getNotNull(wrongPage, player) + navBar(1, totalPages), player, lastNpc);
			return;
		}
		if ((page > totalPages) && (page == 1))
		{
			show(notPage, player, lastNpc);
			return;
		}
		if ((page > totalPages) || (page < 1))
		{
			show(HtmCache.getInstance().getNotNull(wrongPage, player) + navBar(1, totalPages), player, lastNpc);
			return;
		}
		String html = HtmCache.getInstance().getNotNull(readPage, player);
		html = html.replaceFirst("%quote%", quotes.get(page - 1));
		html = html.replaceFirst("%page%", String.valueOf(page));
		html = html.replaceFirst("%total_pages%", String.valueOf(totalPages));
		html += navBar(page, totalPages);
		show(html, player, lastNpc);
	}
	
	/**
	 * Method parseRSS.
	 * @return int
	 */
	private int parseRSS()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		Document doc = null;
		try
		{
			doc = factory.newDocumentBuilder().parse(new File(xmlData));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (doc == null)
		{
			return 0;
		}
		quotes.clear();
		int quotesCounter = 0;
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("rss".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("channel".equalsIgnoreCase(d.getNodeName()))
					{
						for (Node i = d.getFirstChild(); i != null; i = i.getNextSibling())
						{
							if ("item".equalsIgnoreCase(i.getNodeName()))
							{
								for (Node z = i.getFirstChild(); z != null; z = z.getNextSibling())
								{
									if ("description".equalsIgnoreCase(z.getNodeName()))
									{
										quotes.add(z.getTextContent().replaceAll("\\\\", "").replaceAll("\\$", ""));
										quotesCounter++;
									}
								}
							}
						}
					}
				}
			}
		}
		return quotesCounter;
	}
	
	/**
	 * Method getPage.
	 * @param url_server String
	 * @param url_document String
	 * @return String
	 */
	public String getPage(String url_server, String url_document)
	{
		StringBuilder buf = new StringBuilder();
		Socket s;
		try
		{
			try
			{
				s = new Socket(url_server, 80);
			}
			catch (Exception e)
			{
				return null;
			}
			s.setSoTimeout(30000);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "Cp1251"));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
			out.print("GET http://" + url_server + "/" + url_document + " HTTP/1.1\r\n" + "User-Agent: MMoCore\r\n" + "Host: " + url_server + "\r\n" + "Accept: */*\r\n" + "Connection: close\r\n" + "\r\n");
			out.flush();
			boolean header = true;
			for (String line = in.readLine(); line != null; line = in.readLine())
			{
				if (header && line.startsWith("<?xml "))
				{
					header = false;
				}
				if (!header)
				{
					buf.append(line).append("\r\n");
				}
				if (!header && line.startsWith("</rss>"))
				{
					break;
				}
			}
			s.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	/**
	 * Method navBar.
	 * @param curPage int
	 * @param totalPages int
	 * @return String
	 */
	private String navBar(int curPage, int totalPages)
	{
		String html;
		html = "<br><center><table border=0 width=240><tr><td widht=30>";
		if (curPage > 1)
		{
			html += "<a action=\"bypass -h scripts_services.Bash:showQuote " + (curPage - 1) + "\">";
		}
		html += "&lt;&lt;&lt; �?азад";
		if (curPage > 1)
		{
			html += "</a>";
		}
		html += "</td><td widht=160>&nbsp;[" + curPage + "]&nbsp;</td><td widht=40>";
		if (curPage < totalPages)
		{
			html += "<a action=\"bypass -h scripts_services.Bash:showQuote " + (curPage + 1) + "\">";
		}
		html += "Вперед &gt;&gt;&gt;";
		if (curPage < totalPages)
		{
			html += "</a>";
		}
		html += "</td></tr></table></center>";
		html += "<table border=0 width=240><tr><td width=150>";
		html += "�?ерейти на �?траницу:</td><td><edit var=\"page\" width=40></td><td>";
		html += "<button value=\"перейти\" action=\"bypass -h scripts_services.Bash:showQuote $page\" width=60 height=20>";
		html += "</td></tr></table>";
		return html;
	}
	
	/**
	 * Method DialogAppend_30086.
	 * @param val Integer
	 * @return String
	 */
	public static String DialogAppend_30086(Integer val)
	{
		if ((val != 0) || !Config.SERVICES_BASH_ENABLED)
		{
			return "";
		}
		return "<br><center><a action=\"bypass -h scripts_services.Bash:showQuote 1\">�?оч��тат�? Bash.ORG.RU</a></center>";
	}
	
	/**
	 * Method loadData.
	 */
	public void loadData()
	{
		if (Config.SERVICES_BASH_RELOAD_TIME > 0)
		{
			executeTask("services.Bash", "loadData", new Object[0], Config.SERVICES_BASH_RELOAD_TIME * 60 * 60 * 1000L);
		}
		String data;
		try
		{
			data = getPage("bash.im", "rss/");
		}
		catch (Exception E)
		{
			data = null;
		}
		if (data == null)
		{
			if (Config.SERVICES_BASH_SKIP_DOWNLOAD)
			{
				int parse = parseRSS();
				if (parse == 0)
				{
					_log.warn("Service: Bash - RSS data parse error.");
				}
			}
			else
			{
				_log.info("Service: Bash - RSS data download failed.");
			}
			return;
		}
		data = data.replaceFirst("windows-1251", "utf-8");
		if (!Config.SERVICES_BASH_SKIP_DOWNLOAD)
		{
			Files.writeFile(xmlData, data);
			_log.info("Service: Bash - RSS data download completed.");
		}
		int parse = parseRSS();
		if (parse == 0)
		{
			_log.warn("Service: Bash - RSS data parse error.");
			return;
		}
		_log.info("Service: Bash - RSS data parsed: loaded " + parse + " quotes.");
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		_log.info("Loaded Service: Bash [" + (Config.SERVICES_BASH_ENABLED ? "enabled]" : "disabled]"));
		if (Config.SERVICES_BASH_ENABLED)
		{
			AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
			loadData();
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[]
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
