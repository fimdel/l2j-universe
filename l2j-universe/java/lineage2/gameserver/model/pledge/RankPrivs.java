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
package lineage2.gameserver.model.pledge;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RankPrivs
{
	/**
	 * Field _rank.
	 */
	private final int _rank;
	/**
	 * Field _party.
	 */
	private int _party;
	/**
	 * Field _privs.
	 */
	private int _privs;
	
	/**
	 * Constructor for RankPrivs.
	 * @param rank int
	 * @param party int
	 * @param privs int
	 */
	public RankPrivs(int rank, int party, int privs)
	{
		_rank = rank;
		_party = party;
		_privs = privs;
	}
	
	/**
	 * Method getRank.
	 * @return int
	 */
	public int getRank()
	{
		return _rank;
	}
	
	/**
	 * Method getParty.
	 * @return int
	 */
	public int getParty()
	{
		return _party;
	}
	
	/**
	 * Method setParty.
	 * @param party int
	 */
	public void setParty(int party)
	{
		_party = party;
	}
	
	/**
	 * Method getPrivs.
	 * @return int
	 */
	public int getPrivs()
	{
		return _privs;
	}
	
	/**
	 * Method setPrivs.
	 * @param privs int
	 */
	public void setPrivs(int privs)
	{
		_privs = privs;
	}
}
