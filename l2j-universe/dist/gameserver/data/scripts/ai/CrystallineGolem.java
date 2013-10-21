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

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CrystallineGolem extends Fighter
{
	/**
	 * Field CORAL_GARDEN_SECRETGATE. (value is 24220026)
	 */
	private static final int CORAL_GARDEN_SECRETGATE = 24220026;
	/**
	 * Field Crystal_Fragment. (value is 9693)
	 */
	private static final int Crystal_Fragment = 9693;
	/**
	 * Field itemToConsume.
	 */
	private ItemInstance itemToConsume = null;
	/**
	 * Field lastPoint.
	 */
	private Location lastPoint = null;
	/**
	 * Field says.
	 */
	private static final String[] says = new String[]
	{
		"�?�?м, н�?м!!!",
		"Дай!!!",
		"Хочу!!!",
		"�?оe!!!",
		"Еще!!!",
		"Еда!!!"
	};
	/**
	 * Field says2.
	 */
	private static final String[] says2 = new String[]
	{
		"�?тдай!!!",
		"Верни!!!",
		"Жадные вы, уйду �? от ва�?...",
		"�?уда оно подевало�?�??",
		"�?аверное показало�?�?..."
	};
	/**
	 * Field instanceInfo
	 */
	private static final TIntObjectHashMap<Info> instanceInfo = new TIntObjectHashMap<>();
	
	/**
	 * @author Mobius
	 */
	private static class Info
	{
		/**
		 * Constructor for Info.
		 */
		Info()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Field stage1.
		 */
		boolean stage1 = false;
		/**
		 * Field stage2.
		 */
		boolean stage2 = false;
	}
	
	/**
	 * Constructor for CrystallineGolem.
	 * @param actor NpcInstance
	 */
	public CrystallineGolem(NpcInstance actor)
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
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if (itemToConsume != null)
		{
			if (itemToConsume.isVisible())
			{
				itemToConsume.deleteMe();
				itemToConsume = null;
			}
			else
			{
				itemToConsume = null;
				Functions.npcSay(actor, says2[Rnd.get(says2.length)]);
				actor.setWalking();
				addTaskMove(lastPoint, true);
				lastPoint = null;
				return true;
			}
		}
		Info info = instanceInfo.get(actor.getReflectionId());
		if (info == null)
		{
			info = new Info();
			instanceInfo.put(actor.getReflectionId(), info);
		}
		boolean opened = info.stage1 && info.stage2;
		if (!info.stage1)
		{
			final int dx = actor.getX() - 142999;
			final int dy = actor.getY() - 151671;
			if (((dx * dx) + (dy * dy)) < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage1 = true;
			}
		}
		if (!info.stage2)
		{
			final int dx = actor.getX() - 139494;
			final int dy = actor.getY() - 151668;
			if (((dx * dx) + (dy * dy)) < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage2 = true;
			}
		}
		if (!opened && info.stage1 && info.stage2)
		{
			actor.getReflection().openDoor(CORAL_GARDEN_SECRETGATE);
		}
		if (Rnd.chance(10))
		{
			for (GameObject obj : World.getAroundObjects(actor, 300, 200))
			{
				if (obj.isItem())
				{
					ItemInstance item = (ItemInstance) obj;
					if (item.getItemId() == Crystal_Fragment)
					{
						if (Rnd.chance(50))
						{
							Functions.npcSay(actor, says[Rnd.get(says.length)]);
						}
						itemToConsume = item;
						lastPoint = actor.getLoc();
						actor.setRunning();
						addTaskMove(item.getLoc(), false);
						return true;
					}
				}
			}
		}
		if (randomAnimation())
		{
			return true;
		}
		return false;
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
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
