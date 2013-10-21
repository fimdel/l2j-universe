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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Decoy extends Skill
{
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _lifeTime.
	 */
	private final int _lifeTime;
	
	/**
	 * Constructor for Decoy.
	 * @param set StatsSet
	 */
	public Decoy(StatsSet set)
	{
		super(set);
		_npcId = set.getInteger("npcId", 0);
		_lifeTime = set.getInteger("lifeTime", 1200) * 1000;
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (activeChar.isAlikeDead() || !activeChar.isPlayer() || (activeChar != target))
		{
			return false;
		}
		if (_npcId <= 0)
		{
			return false;
		}
		if (activeChar.isInObserverMode())
		{
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param caster Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature caster, List<Creature> targets)
	{
		Player activeChar = caster.getPlayer();
		NpcTemplate DecoyTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
		DecoyInstance decoy = new DecoyInstance(IdFactory.getInstance().getNextId(), DecoyTemplate, activeChar, _lifeTime);
		decoy.setCurrentHp(decoy.getMaxHp(), false);
		decoy.setCurrentMp(decoy.getMaxMp());
		decoy.setHeading(activeChar.getHeading());
		decoy.setReflection(activeChar.getReflection());
		activeChar.setDecoy(decoy);
		decoy.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
	}
}
