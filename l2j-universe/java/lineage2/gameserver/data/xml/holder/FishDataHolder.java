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
package lineage2.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.item.support.FishGroup;
import lineage2.gameserver.templates.item.support.FishTemplate;
import lineage2.gameserver.templates.item.support.LureTemplate;
import lineage2.gameserver.templates.item.support.LureType;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishDataHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final FishDataHolder _instance = new FishDataHolder();
	/**
	 * Field _fishes.
	 */
	private final List<FishTemplate> _fishes = new ArrayList<>();
	/**
	 * Field _lures.
	 */
	private final IntObjectMap<LureTemplate> _lures = new HashIntObjectMap<>();
	/**
	 * Field _distributionsForZones.
	 */
	private final IntObjectMap<Map<LureType, Map<FishGroup, Integer>>> _distributionsForZones = new HashIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return FishDataHolder
	 */
	public static FishDataHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addFish.
	 * @param fishTemplate FishTemplate
	 */
	public void addFish(FishTemplate fishTemplate)
	{
		_fishes.add(fishTemplate);
	}
	
	/**
	 * Method addLure.
	 * @param template LureTemplate
	 */
	public void addLure(LureTemplate template)
	{
		_lures.put(template.getItemId(), template);
	}
	
	/**
	 * Method addDistribution.
	 * @param id int
	 * @param lureType LureType
	 * @param map Map<FishGroup,Integer>
	 */
	public void addDistribution(int id, LureType lureType, Map<FishGroup, Integer> map)
	{
		Map<LureType, Map<FishGroup, Integer>> byLureType = _distributionsForZones.get(id);
		if (byLureType == null)
		{
			_distributionsForZones.put(id, (byLureType = new HashMap<>()));
		}
		byLureType.put(lureType, map);
	}
	
	/**
	 * Method log.
	 */
	@Override
	public void log()
	{
		info("load " + _fishes.size() + " fish(es).");
		info("load " + _lures.size() + " lure(s).");
		info("load " + _distributionsForZones.size() + " distribution(s).");
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Deprecated
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
		_fishes.clear();
		_lures.clear();
		_distributionsForZones.clear();
	}
}
