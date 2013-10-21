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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestChangeToAwakenedClass extends L2GameClientPacket
{
	/**
	 * Field SCROLL_OF_AFTERLIFE. (value is 17600)
	 */
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	
	/**
	 * Field STONE_OF_DESTINY. (value is 17722)
	 */
	private static final int STONE_OF_DESTINY = 17722;
	/**
	 * Field change.
	 */
	private int change;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		change = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (!Config.AWAKING_FREE)
		{
			final Player player = getClient().getActiveChar();
			if (player == null)
			{
				return;
			}
			if (player.getLevel() < 85)
			{
				return;
			}
			if (player.getClassId().level() < 3)
			{
				return;
			}
			if (change != 1)
			{
				return;
			}
			ClassId classId = player.getClassId();
			for (ClassId cid : ClassId.VALUES)
			{
				if (cid.childOf(classId) && (cid.getClassLevel().ordinal() == (classId.getClassLevel().ordinal() + 1)))
				{
					if (player.getInventory().getCountOf(SCROLL_OF_AFTERLIFE) > 0)
					{
						player.getInventory().destroyItemByItemId(SCROLL_OF_AFTERLIFE, 1);
						if(player.getVarB("awakenByStoneOfDestiny", false))
						{
							int classTarget = player.getVarInt("classTarget");
							int classKeepSkills = player.getVarInt("classKeepSkills");
							player.getInventory().destroyItemByItemId(STONE_OF_DESTINY, 1);
							AwakingManager.getInstance().SetAwakingId(player,classTarget,classKeepSkills);
						}
						else
						{
							AwakingManager.getInstance().SetAwakingId(player);
						}
						ThreadPoolManager.getInstance().schedule(new RunnableImpl()
						{
							@Override
							public void runImpl()
							{
								player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
							}
						}, 15000);
					}
					return;
				}
			}
		}
		else
		{
			Player player = getClient().getActiveChar();
			if (player == null)
			{
				return;
			}
			if (player.getLevel() < 85)
			{
				return;
			}
			if (player.getClassId().level() < 3)
			{
				return;
			}
			if (player.isAwaking())
			{
				return;
			}
			if(player.getVarB("awakenByStoneOfDestiny", false))
			{
				int classTarget = player.getVarInt("classTarget");
				int classKeepSkills = player.getVarInt("classKeepSkills");
				player.getInventory().removeItemByItemId(STONE_OF_DESTINY, 1);
				AwakingManager.getInstance().SetAwakingId(player,classTarget,classKeepSkills);
			}
			else
			{
				AwakingManager.getInstance().SetAwakingId(player);
			}
		}
	}
}
