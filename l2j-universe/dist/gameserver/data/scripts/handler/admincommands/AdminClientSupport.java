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
package handler.admincommands;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminClientSupport extends ScriptAdminCommand
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AdminClientSupport.class);
	
	/**
	 * @author Mobius
	 */
	public enum Commands
	{
		/**
		 * Field admin_setskill.
		 */
		admin_setskill,
		/**
		 * Field admin_summon.
		 */
		admin_summon
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player player)
	{
		final Commands c = (Commands) comm;
		GameObject target = player.getTarget();
		switch (c)
		{
			case admin_setskill:
				if (wordList.length != 3)
				{
					return false;
				}
				if (!player.getPlayerAccess().CanEditChar)
				{
					return false;
				}
				if ((target == null) || !target.isPlayer())
				{
					return false;
				}
				try
				{
					final Skill skill = SkillTable.getInstance().getInfo(Integer.parseInt(wordList[1]), Integer.parseInt(wordList[2]));
					target.getPlayer().addSkill(skill, true);
					target.getPlayer().sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skill.getId(), skill.getLevel()));
				}
				catch (NumberFormatException e)
				{
					_log.info("AdminClientSupport:useAdminCommand(Enum,String[],String,L2Player): " + e, e);
					return false;
				}
				break;
			case admin_summon:
				if (wordList.length != 3)
				{
					return false;
				}
				if (!player.getPlayerAccess().CanEditChar)
				{
					return false;
				}
				try
				{
					final int id = Integer.parseInt(wordList[1]);
					final long count = Long.parseLong(wordList[2]);
					if (id >= 1000000)
					{
						if (target == null)
						{
							target = player;
						}
						final NpcTemplate template = NpcHolder.getInstance().getTemplate(id - 1000000);
						for (int i = 0; i < count; i++)
						{
							NpcInstance npc = template.getNewInstance();
							npc.setSpawnedLoc(target.getLoc());
							npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
							npc.spawnMe(npc.getSpawnedLoc());
						}
					}
					else
					{
						if (target == null)
						{
							target = player;
						}
						if (!target.isPlayer())
						{
							return false;
						}
						final ItemTemplate template = ItemHolder.getInstance().getTemplate(id);
						if (template == null)
						{
							return false;
						}
						if (template.isStackable())
						{
							final ItemInstance item = ItemFunctions.createItem(id);
							item.setCount(count);
							target.getPlayer().getInventory().addItem(item);
							target.getPlayer().sendPacket(SystemMessage2.obtainItems(item));
						}
						else
						{
							for (int i = 0; i < count; i++)
							{
								ItemInstance item = ItemFunctions.createItem(id);
								target.getPlayer().getInventory().addItem(item);
								target.getPlayer().sendPacket(SystemMessage2.obtainItems(item));
							}
						}
					}
				}
				catch (NumberFormatException e)
				{
					_log.info("AdminClientSupport:useAdminCommand(Enum,String[],String,L2Player): " + e, e);
					return false;
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
