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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSetCrop extends L2GameClientPacket
{
	/**
	 * Field _manorId. Field _count.
	 */
	private int _count, _manorId;
	/**
	 * Field _items.
	 */
	private long[] _items;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		_count = readD();
		if (((_count * 21) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new long[_count * 4];
		for (int i = 0; i < _count; i++)
		{
			_items[(i * 4) + 0] = readD();
			_items[(i * 4) + 1] = readQ();
			_items[(i * 4) + 2] = readQ();
			_items[(i * 4) + 3] = readC();
			if ((_items[(i * 4) + 0] < 1) || (_items[(i * 4) + 1] < 0) || (_items[(i * 4) + 2] < 0))
			{
				_count = 0;
				return;
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_count == 0))
		{
			return;
		}
		if (activeChar.getClan() == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		Castle caslte = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		if ((caslte.getOwnerId() != activeChar.getClanId()) || ((activeChar.getClanPrivileges() & Clan.CP_CS_MANOR_ADMIN) != Clan.CP_CS_MANOR_ADMIN))
		{
			activeChar.sendActionFailed();
			return;
		}
		List<CropProcure> crops = new ArrayList<>(_count);
		for (int i = 0; i < _count; i++)
		{
			int id = (int) _items[(i * 4) + 0];
			long sales = _items[(i * 4) + 1];
			long price = _items[(i * 4) + 2];
			int type = (int) _items[(i * 4) + 3];
			if (id > 0)
			{
				CropProcure s = CastleManorManager.getInstance().getNewCropProcure(id, sales, type, price, sales);
				crops.add(s);
			}
		}
		caslte.setCropProcure(crops, CastleManorManager.PERIOD_NEXT);
		caslte.saveCropData(CastleManorManager.PERIOD_NEXT);
	}
}
