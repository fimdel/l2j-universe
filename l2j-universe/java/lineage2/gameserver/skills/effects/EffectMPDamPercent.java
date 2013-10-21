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
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectMPDamPercent extends Effect
{
	/**
	 * Constructor for EffectMPDamPercent.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectMPDamPercent(final Env env, final EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (_effected.isDead())
		{
			return;
		}
		double newMp = ((100. - calc()) * _effected.getMaxMp()) / 100.;
		newMp = Math.min(_effected.getCurrentMp(), Math.max(0, newMp));
		_effected.setCurrentMp(newMp);
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
