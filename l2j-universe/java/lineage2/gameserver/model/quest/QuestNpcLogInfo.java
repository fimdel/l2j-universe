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
package lineage2.gameserver.model.quest;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QuestNpcLogInfo
{
	/**
	 * Field _npcIds.
	 */
	private final int[] _npcIds;
	/**
	 * Field _varName.
	 */
	private final String _varName;
	/**
	 * Field _maxCount.
	 */
	private final int _maxCount;
	
	/**
	 * Constructor for QuestNpcLogInfo.
	 * @param npcIds int[]
	 * @param varName String
	 * @param maxCount int
	 */
	public QuestNpcLogInfo(int[] npcIds, String varName, int maxCount)
	{
		_npcIds = npcIds;
		_varName = varName;
		_maxCount = maxCount;
	}
	
	/**
	 * Method getNpcIds.
	 * @return int[]
	 */
	public int[] getNpcIds()
	{
		return _npcIds;
	}
	
	/**
	 * Method getVarName.
	 * @return String
	 */
	public String getVarName()
	{
		return _varName;
	}
	
	/**
	 * Method getMaxCount.
	 * @return int
	 */
	public int getMaxCount()
	{
		return _maxCount;
	}
}
