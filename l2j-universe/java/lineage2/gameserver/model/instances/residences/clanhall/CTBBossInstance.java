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
package lineage2.gameserver.model.instances.residences.clanhall;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.ClanHallTeamBattleEvent;
import lineage2.gameserver.model.entity.events.objects.CTBSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.CTBTeamObject;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class CTBBossInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field SKILL.
	 */
	public static final Skill SKILL = SkillTable.getInstance().getInfo(5456, 1);
	/**
	 * Field _matchTeamObject.
	 */
	private CTBTeamObject _matchTeamObject;
	
	/**
	 * Constructor for CTBBossInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CTBBossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param damage double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if ((attacker.getLevel() > (getLevel() + 8)) && (attacker.getEffectList().getEffectsCountForSkill(SKILL.getId()) == 0))
		{
			doCast(SKILL, attacker, false);
			return;
		}
		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		CTBSiegeClanObject clan = _matchTeamObject.getSiegeClan();
		if ((clan != null) && attacker.isPlayable())
		{
			Player player = attacker.getPlayer();
			if (player.getClan() == clan.getClan())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return isAttackable(attacker);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		ClanHallTeamBattleEvent event = getEvent(ClanHallTeamBattleEvent.class);
		event.processStep(_matchTeamObject);
		super.onDeath(killer);
	}
	
	/**
	 * Method getTitle.
	 * @return String
	 */
	@Override
	public String getTitle()
	{
		CTBSiegeClanObject clan = _matchTeamObject.getSiegeClan();
		return clan == null ? StringUtils.EMPTY : clan.getClan().getName();
	}
	
	/**
	 * Method setMatchTeamObject.
	 * @param matchTeamObject CTBTeamObject
	 */
	public void setMatchTeamObject(CTBTeamObject matchTeamObject)
	{
		_matchTeamObject = matchTeamObject;
	}
}
