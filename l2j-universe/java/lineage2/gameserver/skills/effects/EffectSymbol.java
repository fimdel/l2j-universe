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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.SymbolInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectSymbol extends Effect
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EffectSymbol.class);
	/**
	 * Field _symbol.
	 */
	private NpcInstance _symbol = null;
	
	/**
	 * Constructor for EffectSymbol.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectSymbol(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (getSkill().getTargetType() != Skill.SkillTargetType.TARGET_SELF)
		{
			_log.error("Symbol skill with target != self, id = " + getSkill().getId());
			return false;
		}
		Skill skill = getSkill().getFirstAddedSkill();
		if (skill == null)
		{
			_log.error("Not implemented symbol skill, id = " + getSkill().getId());
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
		Skill skill = getSkill().getFirstAddedSkill();
		skill.setMagicType(getSkill().getMagicType());
		Location loc = _effected.getLoc();
		if (_effected.isPlayer() && (((Player) _effected).getGroundSkillLoc() != null))
		{
			loc = ((Player) _effected).getGroundSkillLoc();
			((Player) _effected).setGroundSkillLoc(null);
		}
		NpcTemplate template = NpcHolder.getInstance().getTemplate(_skill.getSymbolId());
		if (getTemplate()._count <= 1)
		{
			_symbol = new SymbolInstance(IdFactory.getInstance().getNextId(), template, _effected, skill);
		}
		else
		{
			_symbol = new NpcInstance(IdFactory.getInstance().getNextId(), template);
		}
		_symbol.setLevel(_effected.getLevel());
		_symbol.setReflection(_effected.getReflection());
		_symbol.spawnMe(loc);
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if ((_symbol != null) && _symbol.isVisible())
		{
			_symbol.deleteMe();
		}
		_symbol = null;
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (getTemplate()._count <= 1)
		{
			return false;
		}
		Creature effector = getEffector();
		Skill skill = getSkill().getFirstAddedSkill();
		NpcInstance symbol = _symbol;
		double mpConsume = getSkill().getMpConsume();
		if ((effector == null) || (skill == null) || (symbol == null))
		{
			return false;
		}
		if (mpConsume > effector.getCurrentMp())
		{
			effector.sendPacket(Msg.NOT_ENOUGH_MP);
			return false;
		}
		effector.reduceCurrentMp(mpConsume, effector);
		for (Creature cha : World.getAroundCharacters(symbol, getSkill().getSkillRadius(), 200))
		{
			if (!cha.isDoor() && (cha.getEffectList().getEffectsBySkill(skill) == null) && (skill.checkTarget(effector, cha, cha, false, false) == null))
			{
				if (skill.isOffensive() && !GeoEngine.canSeeTarget(symbol, cha, false))
				{
					continue;
				}
				List<Creature> targets = new ArrayList<>(1);
				targets.add(cha);
				effector.callSkill(skill, targets, true);
				effector.broadcastPacket(new MagicSkillLaunched(symbol.getObjectId(), getSkill().getDisplayId(), getSkill().getDisplayLevel(), cha));
			}
		}
		return true;
	}
}
