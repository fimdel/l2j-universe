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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.ZoneTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZoneHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ZoneHolder _instance = new ZoneHolder();
	/**
	 * Field _zones.
	 */
	private final Map<String, ZoneTemplate> _zones = new HashMap<>();
	
	/**
	 * Method getInstance.
	 * @return ZoneHolder
	 */
	public static ZoneHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addTemplate.
	 * @param zone ZoneTemplate
	 */
	public void addTemplate(ZoneTemplate zone)
	{
		_zones.put(zone.getName(), zone);
	}
	
	/**
	 * Method getTemplate.
	 * @param name String
	 * @return ZoneTemplate
	 */
	public ZoneTemplate getTemplate(String name)
	{
		return _zones.get(name);
	}
	
	/**
	 * Method getZones.
	 * @return Map<String,ZoneTemplate>
	 */
	public Map<String, ZoneTemplate> getZones()
	{
		return _zones;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _zones.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_zones.clear();
	}
}
