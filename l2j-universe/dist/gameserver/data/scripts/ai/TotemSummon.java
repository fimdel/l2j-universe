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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TotemSummon extends DefaultAI
{
	/**
	 * Field TotemofBody. (value is 143)
	 */
	private static final int TotemofBody = 143;
	/**
	 * Field TotemofSpirit. (value is 144)
	 */
	private static final int TotemofSpirit = 144;
	/**
	 * Field TotemofBravery. (value is 145)
	 */
	private static final int TotemofBravery = 145;
	/**
	 * Field TotemofFortitude. (value is 146)
	 */
	private static final int TotemofFortitude = 146;
	/**
	 * Field TotemofBodyBuff. (value is 23308)
	 */
	private static final int TotemofBodyBuff = 23308;
	/**
	 * Field TotemofSpiritBuff. (value is 23309)
	 */
	private static final int TotemofSpiritBuff = 23309;
	/**
	 * Field TotemofBraveryBuff. (value is 23310)
	 */
	private static final int TotemofBraveryBuff = 23310;
	/**
	 * Field TotemofFortitudeBuff. (value is 23311)
	 */
	private static final int TotemofFortitudeBuff = 23311;
	/**
	 * Field _timer.
	 */
	private long _timer = 0;
	
	/**
	 * Constructor for TotemSummon.
	 * @param actor NpcInstance
	 */
	public TotemSummon(NpcInstance actor)
	{
		super(actor);
		actor.setHasChatWindow(false);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (getActor() != null)
				{
					getActor().deleteMe();
				}
			}
		}, 30 * 60 * 1000L);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (_timer < System.currentTimeMillis())
		{
			_timer = System.currentTimeMillis() + 15000L;
			for (Creature c : getActor().getAroundCharacters(450, 200))
			{
				if (c.isPlayable() && !c.isDead())
				{
					c.altOnMagicUseTimer(c, SkillTable.getInstance().getInfo(getBuffId(getActor().getNpcId()), 1));
				}
			}
		}
		return true;
	}
	
	/**
	 * Method getBuffId.
	 * @param npcId int
	 * @return int
	 */
	private int getBuffId(int npcId)
	{
		int buffId = 0;
		switch (npcId)
		{
			case TotemofBody:
				buffId = TotemofBodyBuff;
				break;
			case TotemofSpirit:
				buffId = TotemofSpiritBuff;
				break;
			case TotemofBravery:
				buffId = TotemofBraveryBuff;
				break;
			case TotemofFortitude:
				buffId = TotemofFortitudeBuff;
				break;
			default:
				break;
		}
		return buffId;
	}
}
