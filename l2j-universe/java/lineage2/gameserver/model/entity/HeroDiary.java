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
package lineage2.gameserver.model.entity;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.HtmlUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HeroDiary
{
	/**
	 * Field SIMPLE_FORMAT.
	 */
	private static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("HH:** dd.MM.yyyy");
	/**
	 * Field ACTION_RAID_KILLED. (value is 1)
	 */
	public static final int ACTION_RAID_KILLED = 1;
	/**
	 * Field ACTION_HERO_GAINED. (value is 2)
	 */
	public static final int ACTION_HERO_GAINED = 2;
	/**
	 * Field ACTION_CASTLE_TAKEN. (value is 3)
	 */
	public static final int ACTION_CASTLE_TAKEN = 3;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _time.
	 */
	private final long _time;
	/**
	 * Field _param.
	 */
	private final int _param;
	
	/**
	 * Constructor for HeroDiary.
	 * @param id int
	 * @param time long
	 * @param param int
	 */
	public HeroDiary(int id, long time, int param)
	{
		_id = id;
		_time = time;
		_param = param;
	}
	
	/**
	 * Method toString.
	 * @param player Player
	 * @return Map.Entry<String,String>
	 */
	public Map.Entry<String, String> toString(Player player)
	{
		CustomMessage message = null;
		switch (_id)
		{
			case ACTION_RAID_KILLED:
				message = new CustomMessage("lineage2.gameserver.model.entity.Hero.RaidBossKilled", player).addString(HtmlUtils.htmlNpcName(_param));
				break;
			case ACTION_HERO_GAINED:
				message = new CustomMessage("lineage2.gameserver.model.entity.Hero.HeroGained", player);
				break;
			case ACTION_CASTLE_TAKEN:
				message = new CustomMessage("lineage2.gameserver.model.entity.Hero.CastleTaken", player).addString(HtmlUtils.htmlResidenceName(_param));
				break;
			default:
				return null;
		}
		return new AbstractMap.SimpleEntry<>(SIMPLE_FORMAT.format(_time), message.toString());
	}
}
