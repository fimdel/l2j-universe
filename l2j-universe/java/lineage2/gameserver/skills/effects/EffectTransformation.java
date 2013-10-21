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
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.skills.skillclasses.Transformation;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectTransformation extends Effect
{
	/**
	 * Field isFlyingTransform.
	 */
	private final boolean isFlyingTransform;
	
	/**
	 * Constructor for EffectTransformation.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectTransformation(Env env, EffectTemplate template)
	{
		super(env, template);
		int id = (int) template._value;
		isFlyingTransform = template.getParam().getBool("isFlyingTransform", (id == 8) || (id == 9) || (id == 260));
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (!_effected.isPlayer())
		{
			return false;
		}
		if (isFlyingTransform && (_effected.getX() > -166168))
		{
			return false;
		}
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Player player = (Player) _effected;
		player.setTransformationTemplate(getSkill().getNpcId());
		if (getSkill() instanceof Transformation)
		{
			player.setTransformationName(((Transformation) getSkill()).transformationName);
		}
		int id = (int) calc();
		if (isFlyingTransform)
		{
			boolean isVisible = player.isVisible();
			for (Summon summon : player.getSummonList())
			{
				summon.unSummon();
			}
			player.decayMe();
			player.setFlying(true);
			player.setLoc(player.getLoc().changeZ(300));
			player.setTransformation(id);
			if (isVisible)
			{
				player.spawnMe();
			}
		}
		else
		{
			player.setTransformation(id);
		}
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if (_effected.isPlayer())
		{
			Player player = (Player) _effected;
			if (getSkill() instanceof Transformation)
			{
				player.setTransformationName(null);
			}
			if (isFlyingTransform)
			{
				boolean isVisible = player.isVisible();
				player.decayMe();
				player.setFlying(false);
				player.setLoc(player.getLoc().correctGeoZ());
				player.setTransformation(0);
				if (isVisible)
				{
					player.spawnMe();
				}
			}
			else
			{
				player.setTransformation(0);
			}
		}
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
