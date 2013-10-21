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
package lineage2.gameserver.ai;

import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Fighter extends DefaultAI
{
	/**
	 * Constructor for Fighter.
	 * @param actor NpcInstance
	 */
	public Fighter(NpcInstance actor)
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
		return super.thinkActive() || defaultThinkBuff(10);
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		return defaultFightTask();
	}
	
	/**
	 * Method getRatePHYS.
	 * @return int
	 */
	@Override
	public int getRatePHYS()
	{
		return 30;
	}
	
	/**
	 * Method getRateDOT.
	 * @return int
	 */
	@Override
	public int getRateDOT()
	{
		return 20;
	}
	
	/**
	 * Method getRateDEBUFF.
	 * @return int
	 */
	@Override
	public int getRateDEBUFF()
	{
		return 20;
	}
	
	/**
	 * Method getRateDAM.
	 * @return int
	 */
	@Override
	public int getRateDAM()
	{
		return 15;
	}
	
	/**
	 * Method getRateSTUN.
	 * @return int
	 */
	@Override
	public int getRateSTUN()
	{
		return 30;
	}
	
	/**
	 * Method getRateBUFF.
	 * @return int
	 */
	@Override
	public int getRateBUFF()
	{
		return 10;
	}
	
	/**
	 * Method getRateHEAL.
	 * @return int
	 */
	@Override
	public int getRateHEAL()
	{
		return 20;
	}
}
