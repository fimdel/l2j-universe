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
package services;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Birthday extends Functions
{
	/**
	 * Field EXPLORERHAT. (value is 10250)
	 */
	private static final int EXPLORERHAT = 10250;
	/**
	 * Field HAT. (value is 13488)
	 */
	private static final int HAT = 13488;
	/**
	 * Field NPC_ALEGRIA. (value is 32600)
	 */
	private static final int NPC_ALEGRIA = 32600;
	/**
	 * Field msgSpawned. (value is ""scripts/services/Birthday-spawned.htm"")
	 */
	private static final String msgSpawned = "scripts/services/Birthday-spawned.htm";
	
	/**
	 * Method summonAlegria.
	 */
	public void summonAlegria()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null) || !NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		for (NpcInstance n : World.getAroundNpc(npc))
		{
			if (n.getNpcId() == NPC_ALEGRIA)
			{
				show(msgSpawned, player, npc);
				return;
			}
		}
		player.sendPacket(PlaySound.HB01);
		try
		{
			int x = (int) (npc.getX() + (40 * Math.cos(npc.headingToRadians((npc.getHeading() - 32768) + 8000))));
			int y = (int) (npc.getY() + (40 * Math.sin(npc.headingToRadians((npc.getHeading() - 32768) + 8000))));
			NpcInstance alegria = NpcUtils.spawnSingle(NPC_ALEGRIA, x, y, npc.getZ(), 180000);
			alegria.setHeading(PositionUtils.calculateHeadingFrom(alegria, player));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method exchangeHat.
	 */
	public void exchangeHat()
	{
		Player player = getSelf();
		final NpcInstance npc = getNpc();
		if ((player == null) || (npc == null) || !NpcInstance.canBypassCheck(player, player.getLastNpc()) || npc.isBusy())
		{
			return;
		}
		if (ItemFunctions.getItemCount(player, EXPLORERHAT) < 1)
		{
			show("default/32600-nohat.htm", player, npc);
			return;
		}
		ItemFunctions.removeItem(player, EXPLORERHAT, 1, true);
		ItemFunctions.addItem(player, HAT, 1, true);
		show("default/32600-successful.htm", player, npc);
		long now = System.currentTimeMillis() / 1000;
		player.setVar("Birthday", String.valueOf(now), -1);
		npc.setBusy(true);
		ThreadPoolManager.getInstance().execute(new GameObjectTasks.DeleteTask(npc));
	}
}
