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
public enum Privilege
{
	/**
	 * Field FREE.
	 */
	FREE,
	/**
	 * Field CL_JOIN_CLAN.
	 */
	CL_JOIN_CLAN,
	/**
	 * Field CL_GIVE_TITLE.
	 */
	CL_GIVE_TITLE,
	/**
	 * Field CL_VIEW_WAREHOUSE.
	 */
	CL_VIEW_WAREHOUSE,
	/**
	 * Field CL_MANAGE_RANKS.
	 */
	CL_MANAGE_RANKS,
	/**
	 * Field CL_PLEDGE_WAR.
	 */
	CL_PLEDGE_WAR,
	/**
	 * Field CL_DISMISS.
	 */
	CL_DISMISS,
	/**
	 * Field CL_REGISTER_CREST.
	 */
	CL_REGISTER_CREST,
	/**
	 * Field CL_APPRENTICE.
	 */
	CL_APPRENTICE,
	/**
	 * Field CL_TROOPS_FAME.
	 */
	CL_TROOPS_FAME,
	/**
	 * Field CL_SUMMON_AIRSHIP.
	 */
	CL_SUMMON_AIRSHIP,
	/**
	 * Field CH_ENTER_EXIT.
	 */
	CH_ENTER_EXIT,
	/**
	 * Field CH_USE_FUNCTIONS.
	 */
	CH_USE_FUNCTIONS,
	/**
	 * Field CH_AUCTION.
	 */
	CH_AUCTION,
	/**
	 * Field CH_DISMISS.
	 */
	CH_DISMISS,
	/**
	 * Field CH_SET_FUNCTIONS.
	 */
	CH_SET_FUNCTIONS,
	/**
	 * Field CS_FS_ENTER_EXIT.
	 */
	CS_FS_ENTER_EXIT,
	/**
	 * Field CS_FS_MANOR_ADMIN.
	 */
	CS_FS_MANOR_ADMIN,
	/**
	 * Field CS_FS_SIEGE_WAR.
	 */
	CS_FS_SIEGE_WAR,
	/**
	 * Field CS_FS_USE_FUNCTIONS.
	 */
	CS_FS_USE_FUNCTIONS,
	/**
	 * Field CS_FS_DISMISS.
	 */
	CS_FS_DISMISS,
	/**
	 * Field CS_FS_MANAGER_TAXES.
	 */
	CS_FS_MANAGER_TAXES,
	/**
	 * Field CS_FS_MERCENARIES.
	 */
	CS_FS_MERCENARIES,
	/**
	 * Field CS_FS_SET_FUNCTIONS.
	 */
	CS_FS_SET_FUNCTIONS;
	/**
	 * Field ALL. (value is 16777214)
	 */
	public static final int ALL = 16777214;
	/**
	 * Field NONE. (value is 0)
	 */
	public static final int NONE = 0;
	/**
	 * Field _mask.
	 */
	private final int _mask;
	
	/**
	 * Constructor for Privilege.
	 */
	Privilege()
	{
		_mask = ordinal() == 0 ? 0 : 1 << ordinal();
	}
	
	/**
	 * Method mask.
	 * @return int
	 */
	public int mask()
	{
		return _mask;
	}
}
