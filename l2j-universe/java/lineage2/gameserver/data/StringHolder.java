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
package lineage2.gameserver.data;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Language;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StringHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final StringHolder _instance = new StringHolder();
	/**
	 * Field _strings.
	 */
	private final Map<Language, Map<String, String>> _strings = new HashMap<>();
	
	/**
	 * Method getInstance.
	 * @return StringHolder
	 */
	public static StringHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for StringHolder.
	 */
	private StringHolder()
	{
	}
	
	/**
	 * Method getNullable.
	 * @param player Player
	 * @param name String
	 * @return String
	 */
	public String getNullable(Player player, String name)
	{
		Language lang = player == null ? Language.ENGLISH : player.getLanguage();
		return get(lang, name);
	}
	
	/**
	 * Method getNotNull.
	 * @param player Player
	 * @param name String
	 * @return String
	 */
	public String getNotNull(Player player, String name)
	{
		Language lang = player == null ? Language.ENGLISH : player.getLanguage();
		String text = get(lang, name);
		if ((text == null) && (player != null))
		{
			text = "Not find string: " + name + "; for lang: " + lang;
			_strings.get(lang).put(name, text);
		}
		return text;
	}
	
	/**
	 * Method get.
	 * @param lang Language
	 * @param address String
	 * @return String
	 */
	private String get(Language lang, String address)
	{
		Map<String, String> strings = _strings.get(lang);
		return strings.get(address);
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		for (Language lang : Language.VALUES)
		{
			_strings.put(lang, new HashMap<String, String>());
			File f = new File(Config.DATAPACK_ROOT, "data/string/strings_" + lang.getShortName() + ".properties");
			if (!f.exists())
			{
				warn("Not find file: " + f.getAbsolutePath());
				continue;
			}
			LineNumberReader reader = null;
			try
			{
				reader = new LineNumberReader(new FileReader(f));
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					if ((line.length() > 0) && (line.charAt(0) == '#'))
					{
						continue;
					}
					StringTokenizer token = new StringTokenizer(line, "=");
					if (token.countTokens() < 2)
					{
						error("Error on line: " + line + "; file: " + f.getName());
						continue;
					}
					String name = token.nextToken();
					String value = token.nextToken();
					while (token.hasMoreTokens())
					{
						value += "=" + token.nextToken();
					}
					Map<String, String> strings = _strings.get(lang);
					strings.put(name, value);
				}
			}
			catch (Exception e)
			{
				error("Exception: " + e, e);
			}
			finally
			{
				try
				{
					reader.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		log();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		clear();
		load();
	}
	
	/**
	 * Method log.
	 */
	@Override
	public void log()
	{
		for (Map.Entry<Language, Map<String, String>> entry : _strings.entrySet())
		{
			info("load strings: " + entry.getValue().size() + " for lang: " + entry.getKey());
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_strings.clear();
	}
}
