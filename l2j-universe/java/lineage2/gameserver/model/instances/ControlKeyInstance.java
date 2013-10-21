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
package lineage2.gameserver.model.instances;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.reference.L2Reference;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ControlKeyInstance extends GameObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field reference.
	 */
	protected HardReference<ControlKeyInstance> reference;
	
	/**
	 * Constructor for ControlKeyInstance.
	 */
	public ControlKeyInstance()
	{
		super(IdFactory.getInstance().getNextId());
		reference = new L2Reference<>(this);
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<ControlKeyInstance>
	 */
	@Override
	public HardReference<ControlKeyInstance> getRef()
	{
		return reference;
	}
	
	@Override
	public void onActionTargeted(final Player player, boolean forced)
	{
	}
}
