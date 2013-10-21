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

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.ShuttleTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ShuttleTemplateHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ShuttleTemplateHolder _instance = new ShuttleTemplateHolder();
	/**
	 * Field _templates.
	 */
	private final TIntObjectHashMap<ShuttleTemplate> _templates = new TIntObjectHashMap<>();
	
	/**
	 * Method getInstance.
	 * @return ShuttleTemplateHolder
	 */
	public static ShuttleTemplateHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addTemplate.
	 * @param template ShuttleTemplate
	 */
	public void addTemplate(ShuttleTemplate template)
	{
		_templates.put(template.getId(), template);
	}
	
	/**
	 * Method getTemplate.
	 * @param id int
	 * @return ShuttleTemplate
	 */
	public ShuttleTemplate getTemplate(int id)
	{
		return _templates.get(id);
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
