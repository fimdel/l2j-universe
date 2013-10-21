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
package lineage2.gameserver.model.base;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum AcquireType
{
	/**
	 * Field NORMAL.
	 */
	NORMAL,
	/**
	 * Field FISHING.
	 */
	FISHING,
	/**
	 * Field CLAN.
	 */
	CLAN,
	/**
	 * Field SUB_UNIT.
	 */
	SUB_UNIT,
	/**
	 * Field TRANSFORMATION.
	 */
	TRANSFORMATION,
	/**
	 * Field CERTIFICATION.
	 */
	CERTIFICATION,
	/**
	 * Field CERTIFICATION.
	 */
	DUAL_CERTIFICATION,
	/**
	 * Field COLLECTION.
	 */
	COLLECTION,
	/**
	 * Field TRANSFER_CARDINAL.
	 */
	TRANSFER_CARDINAL,
	/**
	 * Field TRANSFER_EVA_SAINTS.
	 */
	TRANSFER_EVA_SAINTS,
	/**
	 * Field TRANSFER_SHILLIEN_SAINTS.
	 */
	TRANSFER_SHILLIEN_SAINTS;
	/**
	 * Field VALUES.
	 */
	public static final AcquireType[] VALUES = AcquireType.values();
	
	/**
	 * Method transferType.
	 * @param classId int
	 * @return AcquireType
	 */
	public static AcquireType transferType(int classId)
	{
		switch (classId)
		{
			case 97:
				return TRANSFER_CARDINAL;
			case 105:
				return TRANSFER_EVA_SAINTS;
			case 112:
				return TRANSFER_SHILLIEN_SAINTS;
		}
		return null;
	}
	
	/**
	 * Method transferClassId.
	 * @return int
	 */
	public int transferClassId()
	{
		switch (this)
		{
			case TRANSFER_CARDINAL:
				return 97;
			case TRANSFER_EVA_SAINTS:
				return 105;
			case TRANSFER_SHILLIEN_SAINTS:
				return 112;
			default:
				break;
		}
		return 0;
	}
}
