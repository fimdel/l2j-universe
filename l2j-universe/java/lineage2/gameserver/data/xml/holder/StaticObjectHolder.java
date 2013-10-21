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

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.templates.StaticObjectTemplate;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StaticObjectHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final StaticObjectHolder _instance = new StaticObjectHolder();
	/**
	 * Field _templates.
	 */
	private final IntObjectMap<StaticObjectTemplate> _templates = new HashIntObjectMap<>();
	/**
	 * Field _spawned.
	 */
	private final IntObjectMap<StaticObjectInstance> _spawned = new HashIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return StaticObjectHolder
	 */
	public static StaticObjectHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addTemplate.
	 * @param template StaticObjectTemplate
	 */
	public void addTemplate(StaticObjectTemplate template)
	{
		_templates.put(template.getUId(), template);
	}
	
	/**
	 * Method getTemplate.
	 * @param id int
	 * @return StaticObjectTemplate
	 */
	public StaticObjectTemplate getTemplate(int id)
	{
		return _templates.get(id);
	}
	
	/**
	 * Method spawnAll.
	 */
	public void spawnAll()
	{
		for (StaticObjectTemplate template : _templates.values())
		{
			if (template.isSpawn())
			{
				StaticObjectInstance obj = template.newInstance();
				_spawned.put(template.getUId(), obj);
			}
		}
		info("spawned: " + _spawned.size() + " static object(s).");
	}
	
	/**
	 * Method getObject.
	 * @param id int
	 * @return StaticObjectInstance
	 */
	public StaticObjectInstance getObject(int id)
	{
		return _spawned.get(id);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _templates.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_templates.clear();
	}
}
