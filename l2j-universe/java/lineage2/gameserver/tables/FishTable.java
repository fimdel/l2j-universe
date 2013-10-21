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
package lineage2.gameserver.tables;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.templates.FishTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FishTable.class);
	/**
	 * Field _instance.
	 */
	private static final FishTable _instance = new FishTable();
	
	/**
	 * Method getInstance.
	 * @return FishTable
	 */
	public static final FishTable getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _fishes.
	 */
	private TIntObjectHashMap<List<FishTemplate>> _fishes;
	
	/**
	 * Constructor for FishTable.
	 */
	private FishTable()
	{
		load();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		_fishes = new TIntObjectHashMap<>();
		int count = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT id, level, name, hp, hpregen, fish_type, fish_group, fish_guts, guts_check_time, wait_time, combat_time FROM fish ORDER BY id");
			resultSet = statement.executeQuery();
			FishTemplate fish;
			List<FishTemplate> fishes;
			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				int lvl = resultSet.getInt("level");
				String name = resultSet.getString("name");
				int hp = resultSet.getInt("hp");
				int hpreg = resultSet.getInt("hpregen");
				int type = resultSet.getInt("fish_type");
				int group = resultSet.getInt("fish_group");
				int fish_guts = resultSet.getInt("fish_guts");
				int guts_check_time = resultSet.getInt("guts_check_time");
				int wait_time = resultSet.getInt("wait_time");
				int combat_time = resultSet.getInt("combat_time");
				fish = new FishTemplate(id, lvl, name, hp, hpreg, type, group, fish_guts, guts_check_time, wait_time, combat_time);
				if ((fishes = _fishes.get(group)) == null)
				{
					_fishes.put(group, fishes = new ArrayList<>());
				}
				fishes.add(fish);
				count++;
			}
			_log.info("FishTable: Loaded " + count + " fishes.");
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, resultSet);
		}
	}
	
	/**
	 * Method getFish.
	 * @param group int
	 * @param type int
	 * @param lvl int
	 * @return List<FishTemplate>
	 */
	public List<FishTemplate> getFish(int group, int type, int lvl)
	{
		List<FishTemplate> result = new ArrayList<>();
		List<FishTemplate> fishs = _fishes.get(group);
		if (fishs == null)
		{
			_log.warn("No fishes defined for group : " + group + "!");
			return null;
		}
		for (FishTemplate f : fishs)
		{
			if (f.getType() != type)
			{
				continue;
			}
			if (f.getLevel() != lvl)
			{
				continue;
			}
			result.add(f);
		}
		if (result.isEmpty())
		{
			_log.warn("No fishes for group : " + group + " type: " + type + " level: " + lvl + "!");
		}
		return result;
	}
}
