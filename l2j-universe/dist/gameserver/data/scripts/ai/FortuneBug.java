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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FortuneBug extends DefaultAI
{
	/**
	 * Field MAX_RADIUS. (value is 500)
	 */
	private static final int MAX_RADIUS = 500;
	/**
	 * Field ItemName_A. (value is 57)
	 */
	private static final int ItemName_A = 57;
	/**
	 * Field ItemName_B_1. (value is 1881)
	 */
	private static final int ItemName_B_1 = 1881;
	/**
	 * Field ItemName_B_2. (value is 1890)
	 */
	private static final int ItemName_B_2 = 1890;
	/**
	 * Field ItemName_B_3. (value is 1880)
	 */
	private static final int ItemName_B_3 = 1880;
	/**
	 * Field ItemName_B_4. (value is 729)
	 */
	private static final int ItemName_B_4 = 729;
	/**
	 * Field s_display_bug_of_fortune1.
	 */
	private static final Skill s_display_bug_of_fortune1 = SkillTable.getInstance().getInfo(6045, 1);
	/**
	 * Field s_display_jackpot_firework.
	 */
	private static final Skill s_display_jackpot_firework = SkillTable.getInstance().getInfo(5778, 1);
	/**
	 * Field _nextEat.
	 */
	private long _nextEat;
	/**
	 * Field i_ai2. Field i_ai1. Field i_ai0.
	 */
	private int i_ai0, i_ai1, i_ai2;
	
	/**
	 * Constructor for FortuneBug.
	 * @param actor NpcInstance
	 */
	public FortuneBug(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(7778, 1000);
		i_ai0 = 0;
		i_ai1 = 0;
		i_ai2 = 0;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		super.onEvtArrived();
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		ItemInstance closestItem = null;
		if (_nextEat < System.currentTimeMillis())
		{
			for (GameObject obj : World.getAroundObjects(actor, 20, 200))
			{
				if (obj.isItem() && ((ItemInstance) obj).isStackable())
				{
					closestItem = (ItemInstance) obj;
				}
			}
			if (closestItem != null)
			{
				closestItem.deleteMe();
				actor.altUseSkill(s_display_bug_of_fortune1, actor);
				Functions.npcSayInRange(actor, 600, NpcString.YUMYUM_YUMYUM);
				i_ai0++;
				if ((i_ai0 > 1) && (i_ai0 <= 10))
				{
					i_ai1 = 1;
				}
				else if ((i_ai0 > 10) && (i_ai0 <= 100))
				{
					i_ai1 = 2;
				}
				else if ((i_ai0 > 100) && (i_ai0 <= 500))
				{
					i_ai1 = 3;
				}
				else if ((i_ai0 > 500) && (i_ai0 <= 1000))
				{
					i_ai1 = 4;
				}
				if (i_ai0 > 1000)
				{
					i_ai1 = 5;
				}
				switch (i_ai1)
				{
					case 0:
						i_ai2 = 0;
						break;
					case 1:
						if (Rnd.get(100) < 10)
						{
							i_ai2 = 2;
						}
						else if (Rnd.get(100) < 15)
						{
							i_ai2 = 3;
						}
						else
						{
							i_ai2 = 1;
						}
						break;
					case 2:
						if (Rnd.get(100) < 10)
						{
							i_ai2 = 3;
						}
						else if (Rnd.get(100) < 15)
						{
							i_ai2 = 4;
						}
						else
						{
							i_ai2 = 2;
						}
						break;
					case 3:
						if (Rnd.get(100) < 10)
						{
							i_ai2 = 4;
						}
						else
						{
							i_ai2 = 3;
						}
						break;
					case 4:
						if (Rnd.get(100) < 10)
						{
							i_ai2 = 3;
						}
						else
						{
							i_ai2 = 4;
						}
						break;
				}
				_nextEat = System.currentTimeMillis() + 10000;
			}
		}
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (!actor.isMoving && (_nextEat < System.currentTimeMillis()))
		{
			ItemInstance closestItem = null;
			for (GameObject obj : World.getAroundObjects(actor, MAX_RADIUS, 200))
			{
				if (obj.isItem() && ((ItemInstance) obj).isStackable())
				{
					closestItem = (ItemInstance) obj;
				}
			}
			if (closestItem != null)
			{
				actor.moveToLocation(closestItem.getLoc(), 0, true);
			}
		}
		return false;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if (killer != null)
		{
			if (i_ai2 == 0)
			{
				Functions.npcSayInRange(actor, 600, NpcString.I_HAVENT_EATEN_ANYTHING_IM_SO_WEAK);
			}
			else
			{
				actor.broadcastPacket(new MagicSkillUse(actor, s_display_jackpot_firework.getId(), 1, s_display_jackpot_firework.getHitTime(), 0));
			}
			int i0, i1;
			switch (i_ai2)
			{
				case 1:
					i0 = 695;
					i1 = 2245;
					actor.dropItem(killer.getPlayer(), ItemName_A, i0 + Rnd.get(i1 - i0));
					break;
				case 2:
					i0 = 3200;
					i1 = 8400;
					actor.dropItem(killer.getPlayer(), ItemName_A, i0 + Rnd.get(i1 - i0));
					break;
				case 3:
					i0 = 7;
					i1 = 17;
					actor.dropItem(killer.getPlayer(), ItemName_B_1, i0 + Rnd.get(i1 - i0));
					i0 = 1;
					i1 = 1;
					actor.dropItem(killer.getPlayer(), ItemName_B_2, i0);
					i0 = 7;
					i1 = 17;
					actor.dropItem(killer.getPlayer(), ItemName_B_3, i0 + Rnd.get(i1 - i0));
					break;
				case 4:
					i0 = 15;
					i1 = 45;
					actor.dropItem(killer.getPlayer(), ItemName_B_1, i0 + Rnd.get(i1 - i0));
					i0 = 10;
					i1 = 20;
					actor.dropItem(killer.getPlayer(), ItemName_B_2, i0 + Rnd.get(i1 - i0));
					i0 = 15;
					i1 = 45;
					actor.dropItem(killer.getPlayer(), ItemName_B_3, i0 + Rnd.get(i1 - i0));
					if (Rnd.get(100) < 10)
					{
						actor.dropItem(killer.getPlayer(), ItemName_B_4, 1);
					}
					break;
			}
		}
	}
	
	/**
	 * Method onEvtTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if (timerId == 7778)
		{
			switch (i_ai0)
			{
				case 0:
					Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IF_YOU_HAVE_ITEMS_PLEASE_GIVE_THEM_TO_ME : NpcString.MY_STOMACH_IS_EMPTY);
					break;
				case 1:
					Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IM_HUNGRY_IM_HUNGRY : NpcString.IM_STILL_NOT_FULL);
					break;
				case 2:
					Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.IM_STILL_HUNGRY : NpcString.I_FEEL_A_LITTLE_WOOZY);
					break;
				case 3:
					Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.GIVE_ME_SOMETHING_TO_EAT : NpcString.NOW_ITS_TIME_TO_EAT);
					break;
				case 4:
					Functions.npcSayInRange(actor, 600, Rnd.chance(50) ? NpcString.I_ALSO_NEED_A_DESSERT : NpcString.IM_STILL_HUNGRY_);
					break;
				case 5:
					Functions.npcSayInRange(actor, 600, NpcString.IM_FULL_NOW_I_DONT_WANT_TO_EAT_ANYMORE);
					break;
			}
			addTimer(7778, 10000 + (Rnd.get(10) * 1000));
		}
		else
		{
			super.onEvtTimer(timerId, arg1, arg2);
		}
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
