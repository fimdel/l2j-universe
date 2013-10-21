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
package ai.residences;

import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeGuardMystic extends SiegeGuard
{
	/**
	 * Constructor for SiegeGuardMystic.
	 * @param actor NpcInstance
	 */
	public SiegeGuardMystic(NpcInstance actor)
	{
		super(actor);
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
		return (_damSkills.length == 0) ? 25 : 0;
	}
	
	/**
	 * Method getRateDOT.
	 * @return int
	 */
	@Override
	public int getRateDOT()
	{
		return 25;
	}
	
	/**
	 * Method getRateDEBUFF.
	 * @return int
	 */
	@Override
	public int getRateDEBUFF()
	{
		return 25;
	}
	
	/**
	 * Method getRateDAM.
	 * @return int
	 */
	@Override
	public int getRateDAM()
	{
		return 100;
	}
	
	/**
	 * Method getRateSTUN.
	 * @return int
	 */
	@Override
	public int getRateSTUN()
	{
		return 10;
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
