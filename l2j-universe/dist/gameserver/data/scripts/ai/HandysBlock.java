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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager.ArenaParticipantsHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.BlockCheckerEngine;
import lineage2.gameserver.model.instances.BlockInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameExtendedChangePoints;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HandysBlock extends DefaultAI
{
	/**
	 * Constructor for HandysBlock.
	 * @param actor NpcInstance
	 */
	public HandysBlock(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final BlockInstance actor = (BlockInstance) getActor();
		if (caster == null)
		{
			return;
		}
		if (!caster.isPlayer())
		{
			return;
		}
		final Player player = caster.getPlayer();
		final int arena = player.getBlockCheckerArena();
		if ((arena == -1) || (arena > 3))
		{
			return;
		}
		if (player.getTarget().equals(actor))
		{
			if ((skill.getId() == 5852) || (skill.getId() == 5853))
			{
				final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
				if ((holder.getPlayerTeam(player) == 0) && !actor.isRed())
				{
					actor.changeColor();
					increaseTeamPointsAndSend(player, holder.getEvent());
				}
				else if ((holder.getPlayerTeam(player) == 1) && actor.isRed())
				{
					actor.changeColor();
					increaseTeamPointsAndSend(player, holder.getEvent());
				}
				else
				{
					return;
				}
				final int random = Rnd.get(100);
				if ((random > 69) && (random <= 84))
				{
					dropItem(actor, 13787, holder.getEvent(), player);
				}
				else if (random > 84)
				{
					dropItem(actor, 13788, holder.getEvent(), player);
				}
			}
		}
	}
	
	/**
	 * Method increaseTeamPointsAndSend.
	 * @param player Player
	 * @param eng BlockCheckerEngine
	 */
	private void increaseTeamPointsAndSend(Player player, BlockCheckerEngine eng)
	{
		final int team = eng.getHolder().getPlayerTeam(player);
		eng.increasePlayerPoints(player, team);
		final int timeLeft = (int) ((eng.getStarterTime() - System.currentTimeMillis()) / 1000);
		final boolean isRed = eng.getHolder().getRedPlayers().contains(player);
		final ExCubeGameChangePoints changePoints = new ExCubeGameChangePoints(timeLeft, eng.getBluePoints(), eng.getRedPoints());
		final ExCubeGameExtendedChangePoints secretPoints = new ExCubeGameExtendedChangePoints(timeLeft, eng.getBluePoints(), eng.getRedPoints(), isRed, player, eng.getPlayerPoints(player, isRed));
		eng.getHolder().broadCastPacketToTeam(changePoints);
		eng.getHolder().broadCastPacketToTeam(secretPoints);
	}
	
	/**
	 * Method dropItem.
	 * @param block NpcInstance
	 * @param id int
	 * @param eng BlockCheckerEngine
	 * @param player Player
	 */
	private void dropItem(NpcInstance block, int id, BlockCheckerEngine eng, Player player)
	{
		final ItemInstance drop = ItemFunctions.createItem(id);
		drop.dropToTheGround(block, Location.findPointToStay(block, 50));
		eng.addNewDrop(drop);
	}
}
