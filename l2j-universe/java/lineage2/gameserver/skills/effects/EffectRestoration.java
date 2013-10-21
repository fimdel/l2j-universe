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
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectRestoration extends Effect
{
	/**
	 * Field itemId.
	 */
	private final int itemId;
	/**
	 * Field count.
	 */
	private final long count;
	
	/**
	 * Constructor for EffectRestoration.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectRestoration(Env env, EffectTemplate template)
	{
		super(env, template);
		String item = getTemplate().getParam().getString("Item");
		itemId = Integer.parseInt(item.split(":")[0]);
		count = Long.parseLong(item.split(":")[1]);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		ItemFunctions.addItem((Playable) getEffected(), itemId, count, true);
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
