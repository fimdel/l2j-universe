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
package lineage2.gameserver.model.entity;

import java.lang.reflect.Constructor;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MonsterRace
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MonsterRace.class);
	/**
	 * Field monsters.
	 */
	private final NpcInstance[] monsters;
	/**
	 * Field _instance.
	 */
	private static MonsterRace _instance;
	/**
	 * Field _constructor.
	 */
	private Constructor<?> _constructor;
	/**
	 * Field speeds.
	 */
	private int[][] speeds;
	/**
	 * Field second. Field first.
	 */
	private final int[] first, second;
	
	/**
	 * Constructor for MonsterRace.
	 */
	private MonsterRace()
	{
		monsters = new NpcInstance[8];
		speeds = new int[8][20];
		first = new int[2];
		second = new int[2];
	}
	
	/**
	 * Method getInstance.
	 * @return MonsterRace
	 */
	public static MonsterRace getInstance()
	{
		if (_instance == null)
		{
			_instance = new MonsterRace();
		}
		return _instance;
	}
	
	/**
	 * Method newRace.
	 */
	public void newRace()
	{
		int random = 0;
		for (int i = 0; i < 8; i++)
		{
			int id = 31003;
			random = Rnd.get(24);
			for (int j = i - 1; j >= 0; j--)
			{
				if (monsters[j].getTemplate().npcId == (id + random))
				{
					random = Rnd.get(24);
				}
			}
			try
			{
				NpcTemplate template = NpcHolder.getInstance().getTemplate(id + random);
				_constructor = template.getInstanceConstructor();
				int objectId = IdFactory.getInstance().getNextId();
				monsters[i] = (NpcInstance) _constructor.newInstance(objectId, template);
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
		newSpeeds();
	}
	
	/**
	 * Method newSpeeds.
	 */
	public void newSpeeds()
	{
		speeds = new int[8][20];
		int total = 0;
		first[1] = 0;
		second[1] = 0;
		for (int i = 0; i < 8; i++)
		{
			total = 0;
			for (int j = 0; j < 20; j++)
			{
				if (j == 19)
				{
					speeds[i][j] = 100;
				}
				else
				{
					speeds[i][j] = Rnd.get(65, 124);
				}
				total += speeds[i][j];
			}
			if (total >= first[1])
			{
				second[0] = first[0];
				second[1] = first[1];
				first[0] = 8 - i;
				first[1] = total;
			}
			else if (total >= second[1])
			{
				second[0] = 8 - i;
				second[1] = total;
			}
		}
	}
	
	/**
	 * Method getMonsters.
	 * @return NpcInstance[]
	 */
	public NpcInstance[] getMonsters()
	{
		return monsters;
	}
	
	/**
	 * Method getSpeeds.
	 * @return int[][]
	 */
	public int[][] getSpeeds()
	{
		return speeds;
	}
	
	/**
	 * Method getFirstPlace.
	 * @return int
	 */
	public int getFirstPlace()
	{
		return first[0];
	}
	
	/**
	 * Method getSecondPlace.
	 * @return int
	 */
	public int getSecondPlace()
	{
		return second[0];
	}
}
