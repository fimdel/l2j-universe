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
package lineage2.gameserver.network.clientpackets;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@Deprecated
public class RequestExGetOffAirShip extends L2GameClientPacket
{
	/**
	 * Field _x.
	 */
	@SuppressWarnings("unused")
	private int _x;
	/**
	 * Field _y.
	 */
	@SuppressWarnings("unused")
	private int _y;
	/**
	 * Field _z.
	 */
	@SuppressWarnings("unused")
	private int _z;
	/**
	 * Field _id.
	 */
	@SuppressWarnings("unused")
	private int _id;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_x = readD();
		_y = readD();
		_z = readD();
		_id = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
	}
}
