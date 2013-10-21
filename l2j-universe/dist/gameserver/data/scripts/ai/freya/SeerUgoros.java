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
package ai.freya;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import quests._288_HandleWithCare;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeerUgoros extends Mystic
{
	/**
	 * Field _weeds.
	 */
	private int _weeds = 0;
	/**
	 * Field _skill.
	 */
	private static final Skill _skill = SkillTable.getInstance().getInfo(6426, 1);
	
	/**
	 * Constructor for SeerUgoros.
	 * @param actor NpcInstance
	 */
	public SeerUgoros(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		super.thinkActive();
		if (!getActor().getReflection().isDefault() && !getActor().getReflection().getPlayers().isEmpty())
		{
			for (Player p : getActor().getReflection().getPlayers())
			{
				notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 5000);
			}
		}
		return true;
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance actor = getActor();
		if (!actor.isMuted(_skill) && (actor.getCurrentHpPercents() < 80))
		{
			for (NpcInstance n : actor.getAroundNpc(2000, 300))
			{
				if ((n.getNpcId() == 18867) && !n.isDead())
				{
					actor.doCast(_skill, n, true);
					actor.setCurrentHp(actor.getMaxHp(), false);
					actor.broadcastCharInfo();
					_weeds++;
					return;
				}
			}
		}
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final QuestState qs = killer.getPlayer().getQuestState(_288_HandleWithCare.class);
		if ((qs != null) && (qs.getCond() == 1))
		{
			if (_weeds < 5)
			{
				qs.giveItems(15497, 1);
				qs.setCond(3);
			}
			else
			{
				qs.giveItems(15498, 1);
				qs.setCond(2);
			}
		}
		_weeds = 0;
		if (!getActor().getReflection().isDefault())
		{
			getActor().getReflection().addSpawnWithoutRespawn(32740, new Location(95688, 85688, -3757, 0), 0);
		}
		super.onEvtDead(killer);
	}
}
