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
import java.util.List;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WalkerRoutesHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final WalkerRoutesHolder _instance = new WalkerRoutesHolder();
	/**
	 * Field _spawns.
	 */
	private final List<WalkerRouteTemplate> _spawns = new ArrayList<>();
	
	/**
	 * Method getInstance.
	 * @return WalkerRoutesHolder
	 */
	public static WalkerRoutesHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for WalkerRoutesHolder.
	 */
	protected WalkerRoutesHolder()
	{
	}
	
	/**
	 * Method addSpawn.
	 * @param spawn WalkerRouteTemplate
	 */
	public void addSpawn(WalkerRouteTemplate spawn)
	{
		_spawns.add(spawn);
	}
	
	/**
	 * Method getSpawns.
	 * @return List<WalkerRouteTemplate>
	 */
	public List<WalkerRouteTemplate> getSpawns()
	{
		return _spawns;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _spawns.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_spawns.clear();
	}
}
