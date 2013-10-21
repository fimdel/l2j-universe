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
public class RequestChangeBookMarkSlot extends L2GameClientPacket
{
	/**
	 * Field slot_new. Field slot_old.
	 */
	@SuppressWarnings("unused")
	private int slot_old, slot_new;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		slot_old = readD();
		slot_new = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
	}
}
