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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.handler.voicecommands.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class VoteManager extends Functions implements IVoicedCommandHandler, ScriptFile
{
	/**
	 * @author Mobius
	 */
	private static class Vote
	{
		/**
		 * Constructor for Vote.
		 */
		public Vote()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Field active.
		 */
		public boolean active;
		/**
		 * Field name.
		 */
		public String name;
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field maxPerAccount.
		 */
		public int maxPerAccount;
		/**
		 * Field variants.
		 */
		public TreeMap<Integer, String> variants = new TreeMap<>();
		/**
		 * Field results.
		 */
		public Map<String, Integer[]> results = new HashMap<>();
	}
	
	/**
	 * Field VoteList.
	 */
	private static Map<Integer, Vote> VoteList = new HashMap<>();
	
	/**
	 * Method vote.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	private boolean vote(String command, Player activeChar, String args)
	{
		if ((args != null) && !args.isEmpty())
		{
			String[] param = args.split(" ");
			if ((param.length >= 2) && Util.isNumber(param[0]) && Util.isNumber(param[1]))
			{
				String playerId = activeChar.getAccountName();
				Vote v = VoteList.get(Integer.parseInt(param[0]));
				if ((v == null) || !v.active)
				{
					return false;
				}
				int var = Integer.parseInt(param[1]);
				Integer[] alreadyResults = v.results.get(playerId);
				if (alreadyResults == null)
				{
					v.results.put(playerId, new Integer[]
					{
						var
					});
					mysql.set("INSERT IGNORE INTO vote (`id`, `HWID`, `vote`) VALUES (?,?,?)", param[0], playerId, param[1]);
				}
				else if (alreadyResults.length < v.maxPerAccount)
				{
					for (int id : alreadyResults)
					{
						if (id == var)
						{
							show("Error: you have already voted for this entry.", activeChar);
							return false;
						}
					}
					v.results.put(playerId, ArrayUtils.add(alreadyResults, var));
					mysql.set("INSERT IGNORE INTO vote (`id`, `HWID`, `vote`) VALUES (?,?,?)", param[0], playerId, param[1]);
				}
				else
				{
					show("Error: you have reached votes limit.", activeChar);
					return false;
				}
			}
		}
		int count = 0;
		StringBuilder html = new StringBuilder("!VoteManager:\n<br>");
		String playerId = activeChar.getAccountName();
		for (Entry<Integer, Vote> e : VoteList.entrySet())
		{
			if (e.getValue().active)
			{
				count++;
				html.append(e.getValue().name).append(":<br>");
				Integer[] already = e.getValue().results.get(playerId);
				if ((already != null) && (already.length >= e.getValue().maxPerAccount))
				{
					html.append("You have already voted.<br>");
				}
				else
				{
					Entry<Integer, String>[] variants = new Entry[e.getValue().variants.size()];
					int i = 0;
					for (Entry<Integer, String> variant : e.getValue().variants.entrySet())
					{
						variants[i] = variant;
						i++;
					}
					shuffle(variants);
					variants:
					for (Entry<Integer, String> variant : variants)
					{
						if (already != null)
						{
							for (Integer et : already)
							{
								if (et.equals(variant.getKey()))
								{
									continue variants;
								}
							}
						}
						html.append("[user_vote " + e.getValue().id + " " + variant.getKey() + "|" + variant.getValue() + "]<br1>");
					}
					html.append("<br>");
				}
			}
		}
		if (count == 0)
		{
			html.append("No active votes now.");
		}
		show(html.toString(), activeChar);
		return true;
	}
	
	/**
	 * Method shuffle.
	 * @param array Entry<Integer,String>[]
	 */
	private static void shuffle(Entry<Integer, String>[] array)
	{
		int j;
		Entry<Integer, String> tmp;
		for (int i = array.length; i > 1; i--)
		{
			j = Rnd.get(i);
			tmp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = tmp;
		}
	}
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		VoteList.clear();
		try
		{
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/vote.xml");
			DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
			factory2.setValidating(false);
			factory2.setIgnoringComments(true);
			Document doc2 = factory2.newDocumentBuilder().parse(file);
			for (Node n2 = doc2.getFirstChild(); n2 != null; n2 = n2.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n2.getNodeName()))
				{
					for (Node d2 = n2.getFirstChild(); d2 != null; d2 = d2.getNextSibling())
					{
						if ("vote".equalsIgnoreCase(d2.getNodeName()))
						{
							Vote v = new Vote();
							v.id = Integer.parseInt(d2.getAttributes().getNamedItem("id").getNodeValue());
							v.maxPerAccount = Integer.parseInt(d2.getAttributes().getNamedItem("maxPerAccount").getNodeValue());
							v.name = d2.getAttributes().getNamedItem("name").getNodeValue();
							v.active = Boolean.parseBoolean(d2.getAttributes().getNamedItem("active").getNodeValue());
							for (Node i = d2.getFirstChild(); i != null; i = i.getNextSibling())
							{
								if ("variant".equalsIgnoreCase(i.getNodeName()))
								{
									v.variants.put(Integer.parseInt(i.getAttributes().getNamedItem("id").getNodeValue()), i.getAttributes().getNamedItem("desc").getNodeValue());
								}
							}
							VoteList.put(v.id, v);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.prepareStatement("SELECT * FROM vote");
			rs = st.executeQuery();
			while (rs.next())
			{
				Vote v = VoteList.get(rs.getInt("id"));
				if (v != null)
				{
					String HWID = rs.getString("HWID");
					Integer[] rez = v.results.get(HWID);
					v.results.put(HWID, ArrayUtils.add(rez, rs.getInt("vote")));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, st, rs);
		}
	}
	
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList = new String[]
	{
		"vote"
	};
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[] * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		if (command.equalsIgnoreCase("vote"))
		{
			return vote(command, activeChar, args);
		}
		return false;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		load();
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
}
