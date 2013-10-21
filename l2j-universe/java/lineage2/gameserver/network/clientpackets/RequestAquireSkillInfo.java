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

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.AcquireSkillInfo;
import lineage2.gameserver.network.serverpackets.ExAcquireSkillInfo;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAquireSkillInfo extends L2GameClientPacket
{
	/**
	 * Field _id.
	 */
	private int _id;
	/**
	 * Field _level.
	 */
	private int _level;
	/**
	 * Field _type.
	 */
	private AcquireType _type;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_id = readD();
		_level = readD();
		_type = ArrayUtils.valid(AcquireType.VALUES, readD());
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if ((player == null) || (player.getTransformation() != 0) || (SkillTable.getInstance().getInfo(_id, _level) == null) || (_type == null))
		{
			return;
		}
		NpcInstance trainer = player.getLastNpc();
		if (((trainer == null) || (player.getDistance(trainer.getX(), trainer.getY()) > Creature.INTERACTION_DISTANCE)) && !player.isGM() && (_type != AcquireType.NORMAL))
		{
			return;
		}
		SkillLearn skillLearn = SkillAcquireHolder.getInstance().getSkillLearn(player, _id, _level, _type);
		if (skillLearn == null)
		{
			return;
		}
		if (_type == AcquireType.NORMAL)
		{
			sendPacket(new ExAcquireSkillInfo(player, skillLearn));
		}
		else
		{
			sendPacket(new AcquireSkillInfo(_type, skillLearn));
		}
	}
}
