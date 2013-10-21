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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.LockType;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectLockInventory extends Effect
{
	/**
	 * Field _lockType.
	 */
	private final LockType _lockType;
	/**
	 * Field _lockItems.
	 */
	private final int[] _lockItems;
	
	/**
	 * Constructor for EffectLockInventory.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectLockInventory(Env env, EffectTemplate template)
	{
		super(env, template);
		_lockType = template.getParam().getEnum("lockType", LockType.class);
		_lockItems = template.getParam().getIntegerArray("lockItems");
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Player player = _effector.getPlayer();
		player.getInventory().lockItems(_lockType, _lockItems);
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		Player player = _effector.getPlayer();
		player.getInventory().unlock();
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
