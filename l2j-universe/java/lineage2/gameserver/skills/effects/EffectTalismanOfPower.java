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
import lineage2.gameserver.skills.AbnormalEffect;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectTalismanOfPower extends Effect
{

	public EffectTalismanOfPower(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		
		 switch(getSkill().getLevel()) 
		 { 
		 case 1: 
		 case 2: 
		 getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER1);
		 break;
		 case 3:
		 getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER2);
		 break;
		 case 4:
		 getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER3);
		 break;
		 case 5:
		 getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER4);
		 break;
		 case 6:
		 getEffected().startAbnormalEffect(AbnormalEffect.TALISMAN_POWER5);
		 break;
		 }
		 
	}

	@Override
	public void onExit()
	{
		super.onExit();
		 getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER1);
		 getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER2);
		 getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER3);
		 getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER4);
		 getEffected().stopAbnormalEffect(AbnormalEffect.TALISMAN_POWER5);
		 
	}

	@Override
	protected boolean onActionTime()
	{
		return false;
	}

}
