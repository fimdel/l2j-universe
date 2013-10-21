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
package lineage2.gameserver.model.entity.events.objects;

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZoneObject implements InitableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _zone.
	 */
	private Zone _zone;
	
	/**
	 * Constructor for ZoneObject.
	 * @param name String
	 */
	public ZoneObject(String name)
	{
		_name = name;
	}
	
	/**
	 * Method initObject.
	 * @param e GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.InitableObject#initObject(GlobalEvent)
	 */
	@Override
	public void initObject(GlobalEvent e)
	{
		Reflection r = e.getReflection();
		_zone = r.getZone(_name);
	}
	
	/**
	 * Method setActive.
	 * @param a boolean
	 */
	public void setActive(boolean a)
	{
		_zone.setActive(a);
	}
	
	/**
	 * Method setActive.
	 * @param a boolean
	 * @param event GlobalEvent
	 */
	public void setActive(boolean a, GlobalEvent event)
	{
		setActive(a);
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	public Zone getZone()
	{
		return _zone;
	}
	
	/**
	 * Method getInsidePlayers.
	 * @return List<Player>
	 */
	public List<Player> getInsidePlayers()
	{
		return _zone.getInsidePlayers();
	}
	
	/**
	 * Method checkIfInZone.
	 * @param c Creature
	 * @return boolean
	 */
	public boolean checkIfInZone(Creature c)
	{
		return _zone.checkIfInZone(c);
	}
}
