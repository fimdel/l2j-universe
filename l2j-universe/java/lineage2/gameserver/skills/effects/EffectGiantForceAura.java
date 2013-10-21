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

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectGiantForceAura extends Effect
{
	/**
	 * Field forceSkillId.
	 */
	private final int forceSkillId;
	/**
	 * Field auraSkillId.
	 */
	private final int auraSkillId;
	/**
	 * Field startEffectTask.
	 */
	private ScheduledFuture<?> startEffectTask;
	
	/**
	 * Constructor for EffectGiantForceAura.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectGiantForceAura(Env env, EffectTemplate template)
	{
		super(env, template);
		forceSkillId = template.getParam().getInteger("forceSkillId", -1);
		auraSkillId = template.getParam().getInteger("auraSkillId", -1);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (forceSkillId > 0)
		{
			startEffectTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					updateAura();
				}
			}, 500 + Rnd.get(4000));
		}
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if (startEffectTask != null)
		{
			startEffectTask.cancel(false);
		}
	}
	
	/**
	 * Method updateAura.
	 */
	void updateAura()
	{
		Creature effector = getEffector();
		Skill forceSkill = SkillTable.getInstance().getInfo(forceSkillId, 1);
		Skill auraSkill = getSkill();
		if ((effector == null) || (forceSkill == null) || (auraSkill == null))
		{
			return;
		}
		List<Creature> targets = forceSkill.getTargets(effector, effector, false);
		for (Creature target : targets)
		{
			if (target.getEffectList().getEffectsBySkillId(forceSkillId) == null)
			{
				forceSkill.getEffects(effector, target, false, false);
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
		if (getEffected().isDead())
		{
			return false;
		}
		if (forceSkillId > 0)
		{
			updateAura();
		}
		else if (auraSkillId > 0)
		{
			Creature effector = getEffector();
			if ((effector == null) || (effector.getEffectList().getEffectsBySkillId(auraSkillId) == null))
			{
				return false;
			}
		}
		if (getEffected() instanceof Player)
		{
			if (!getEffected().getPlayer().isInParty() && getEffected() != getEffector())
			{
				return false;
			}
			else if (getEffected().getPlayer().getParty() != getEffector().getPlayer().getParty())
			{
				return false;
			}
		}
		// Count Active Aura
		int activeAura = 0;
		boolean psActive = false;
		Effect psEffect = null;
		if (getEffected().getEffectList() != null)
		{
			for (Effect e : getEffected().getEffectList().getAllEffects())
			{
				if (e == null)
				{
					continue;
				}
				if (e.getEffectType() == EffectType.GiantForceAura && !e.getStackType().contains("PartySolidarity") && !e.getSkill().isAuraSkill())
				{
					activeAura++;
				}
				if (e.getStackType().contains("PartySolidarity"))
				{
					psEffect = e;
					psActive = true;
				}
			}
		}
		if (activeAura >= 4) 
		{
			if (!psActive || psEffect.getSkill().getLevel() < Math.min((activeAura - 3), 3) || psEffect.getSkill().getLevel() > Math.min((activeAura - 3), 3))
			{
				Skill PartySolidarity = SkillTable.getInstance().getInfo(1955, Math.min((activeAura - 3), 3));
				PartySolidarity.getEffects(getEffected(), getEffected(), false, false);
			}
		}
		else if (psActive)
		{
			psEffect.exit();
		}
		return true;
	}
}
