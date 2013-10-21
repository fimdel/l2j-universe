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
package handler.items;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import org.apache.commons.lang3.ArrayUtils;

import quests._464_Oath;
import bosses.AntharasManager;
import bosses.ValakasManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Special extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		8060,
		8556,
		13853,
		13808,
		13809,
		14835,
		15537,
		17268,
		17619,
		21899,
		21900,
		21901,
		21902,
		21903,
		21904,
		34033
	};
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
	
	/**
	 * Method useItemImpl.
	 * @param player Player
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		final int itemId = item.getItemId();
		switch (itemId)
		{
			//Key of Enigma
			case 8060:
				return use8060(player, ctrl);
			//Dewdrop of Destruction
			case 8556:
				return use8556(player, ctrl);
			//DestroyedDarknessFragmentPowder -> DestroyedLightFragmentPowder
			case 13853:
				return use13853(player, ctrl);
			//Holy Water for SSQ 2nd quest
			case 13808:
				return use13808(player, ctrl);
			//Court Mag Staff for SSQ 2nd quest
			case 13809:
				return use13809(player, ctrl);
			case 14835:
				return use14835(player, ctrl);
			//Strongbox of Promise
			case 15537:
				return use15537(player, ctrl);
			//Antharas Blood Crystal
			case 17268:
				return use17268(player, ctrl);
			//cruma quest
			//case 17619:
			//	return use17619(player, ctrl);
			case 21899:
				return use21899(player, ctrl);
			case 21900:
				return use21900(player, ctrl);
			case 21901:
				return use21901(player, ctrl);
			case 21902:
				return use21902(player, ctrl);
			case 21903:
				return use21903(player, ctrl);
			case 21904:
				return use21904(player, ctrl);
			//megameld quest
			//case 34033:
			//	return use34033(player, ctrl);
			default:
				return false;
		}
	}
	
	//Key of Enigma
	private boolean use8060(Player player, boolean ctrl)
	{
		if(Functions.removeItem(player, 8058, 1) == 1)
		{
			Functions.addItem(player, 8059, 1);
			return true;
		}
		return false;
	}

	//Dewdrop of Destruction
	private boolean use8556(Player player, boolean ctrl)
	{
		final int[] npcs =
		{
			29048,
			29049
		};
		final GameObject t = player.getTarget();
		if ((t == null) || !t.isNpc() || !ArrayUtils.contains(npcs, ((NpcInstance) t).getNpcId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(8556));
			return false;
		}
		if (player.getDistance(t) > 200)
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOUR_TARGET_IS_OUT_OF_RANGE));
			return false;
		}
		useItem(player, 8556, 1);
		((NpcInstance) t).doDie(player);
		return true;
	}

	//DestroyedDarknessFragmentPowder -> DestroyedLightFragmentPowde
	private boolean use13853(Player player, boolean ctrl)
	{
		if (!player.isInZone(ZoneType.mother_tree))
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
			return false;
		}
		useItem(player, 13853, 1);
		Functions.addItem(player, 13854, 1);
		return true;
	}
	
	/**
	 * Method use13808.
	 * @param player Player
	 * @param ctrl boolean
	 * @return boolean
	 */
	private boolean use13808(Player player, boolean ctrl)
	{
		final int[] allowedDoors =
		{
			17240101,
			17240105,
			17240109
		};
		final GameObject target = player.getTarget();
		if (player.getDistance(target) > 150)
		{
			return false;
		}
		if ((target != null) && target.isDoor())
		{
			final int _door = ((DoorInstance) target).getDoorId();
			if (ArrayUtils.contains(allowedDoors, _door))
			{
				player.getReflection().openDoor(_door);
			}
			else
			{
				player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
				return false;
			}
		}
		else
		{
			player.sendPacket(Msg.INVALID_TARGET);
			return false;
		}
		return true;
	}
	
	/**
	 * Method use13809.
	 * @param player Player
	 * @param ctrl boolean
	 * @return boolean
	 */
	private boolean use13809(Player player, boolean ctrl)
	{
		final int[] allowedDoors =
		{
			17240103,
			17240107
		};
		final GameObject target = player.getTarget();
		if ((target != null) && target.isDoor())
		{
			final int _door = ((DoorInstance) target).getDoorId();
			if (ArrayUtils.contains(allowedDoors, _door))
			{
				useItem(player, 13809, 1);
				player.getReflection().openDoor(_door);
				return false;
			}
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		player.sendPacket(Msg.INVALID_TARGET);
		return false;
	}
	
	/**
	 * Method use14835.
	 * @param player Player
	 * @param ctrl boolean
	 * @return boolean
	 */
	private boolean use14835(Player player, boolean ctrl)
	{
		if (player.isActionsDisabled() || player.isInOlympiadMode() || player.isInZone(ZoneType.no_escape))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(14835));
			return false;
		}
		useItem(player, 14835, 1);
		player.teleToLocation(89464, -44712, -2167, ReflectionManager.DEFAULT);
		return true;
	}
	
	/**
	 * Method use15537.
	 * @param player Player
	 * @param ctrl boolean
	 * @return boolean
	 */
	private boolean use15537(Player player, boolean ctrl)
	{
		final QuestState qs = player.getQuestState(_464_Oath.class);
		if ((player.getLevel() >= 82) && (qs == null))
		{
			useItem(player, 15537, 1);
			Functions.addItem(player, 15538, 1);
			final Quest q = QuestManager.getQuest(464);
			final QuestState st = q.newQuestState(player, Quest.CREATED);
			st.setState(Quest.STARTED);
			st.setCond(1);
		}
		else
		{
			player.sendMessage(new CustomMessage("Quest._464_Oath.QuestCannotBeTaken", player));
			return false;
		}
		return true;
	}
	
	//Totem
	private boolean use21899(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21899));
			return false;
		}
		Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 143);
		return true;
	}

	//Totem
	private boolean use21900(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21900));
			return false;
		}
		Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 144);
		return true;
	}

	//Totem
	private boolean use21901(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21901));
			return false;
		}
		Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 145);
		return true;
	}

	//Totem
	private boolean use21902(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21902));
			return false;
		}
		Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 146);
		return true;
	}
	
	// Refined Red Dragon Blood
	private boolean use21903(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21903));
			return false;
		}
		player.doCast(SkillTable.getInstance().getInfo(22298, 1), player, false);
		Functions.removeItem(player, 21903, 1);
		return true;
	}

	// Refined Blue Dragon Blood
	private boolean use21904(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()) && !player.isInZone(ValakasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(21904));
			return false;
		}
		player.doCast(SkillTable.getInstance().getInfo(22299, 1), player, false);
		Functions.removeItem(player, 21904, 1);
		return true;
	}
	
	// Antharas Blood Crystal
	private boolean use17268(Player player, boolean ctrl)
	{
		if(!player.isInZone(AntharasManager.getZone()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17268));
			return false;
		}
		player.doCast(SkillTable.getInstance().getInfo(9179, 1), player, false);
		Functions.removeItem(player, 17268, 1);
		return true;
	}
	
	//TODO ADD MISSING QUEST
	/*
	private boolean use17619(Player player, boolean ctrl)
	{
		//TODO[Iqman+Nosferatu] Define zone in cruma tower we can use this scroll only there!!
		QuestState qs = player.getQuestState(_10352_LegacyofCrumaTower.class);
		QuestState qs2 = player.getQuestState(_480_AnotherLegacyOfCrumaTower.class);
		if(player.getVar("MechanismSpawn") != null || qs == null || qs.getCond() > 4)
		{
			if(qs2 == null || qs2.getCond() > 4 || player.getVar("MechanismSpawn") != null)
			{
				player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(17619));
				return false;
			}	
		}	
		
		Functions.removeItem(player, 17619, 1);
		NpcInstance npc = Functions.spawn(Location.findPointToStay(player.getLoc(), 50, 100, player.getGeoIndex()), 17619);
		player.setVar("MechanismSpawn", "1", 120000);
		if(qs != null && !qs.isCompleted())
		{
			Quest q = QuestManager.getQuest(10352);
			player.processQuestEvent(q.getName(), "advanceCond3", null);
		}
		if(qs2 != null && !qs2.isCompleted())
		{
			Quest q2 = QuestManager.getQuest(480);
			player.processQuestEvent(q2.getName(), "advanceCond3", null);
		}		
		Functions.executeTask("handler.items.Special", "despawnNpc", new Object[] {npc, player}, 120000);
		return true;
	}

	public static void despawnNpc(NpcInstance npc, Player player)
	{
		if(npc != null)
			npc.deleteMe();
		if(player != null)
			player.unsetVar("CrystalsSpawn");
	}

	private boolean use34033(Player player, boolean ctrl)
	{
		QuestState qs = player.getQuestState(_10304_ForTheForgottenHeroes.class);
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		if(player.getLevel() >= 90 && qs == null)
		{
			Quest q = QuestManager.getQuest(10304);
			QuestState st = player.getQuestState(q.getClass());
			st = q.newQuestState(player, Quest.CREATED);
			st.setState(Quest.STARTED);
			st.setCond(1);
			useItem(player, 34033, 1);
			Functions.addItem(player, 17618, 1);
			msg.setFile("quests/_10304_ForTheForgottenHeroes/2.htm");
			player.sendPacket(msg);
		}
		else
		{
			msg.setFile("quests/_10304_ForTheForgottenHeroes/4.htm");
			player.sendPacket(msg);
			return false;
		}
		return true;
	}
	*/
	/**
	 * Method useItem.
	 * @param player Player
	 * @param itemId int
	 * @param count long
	 * @return long
	 */
	private static long useItem(Player player, int itemId, long count)
	{
		player.sendPacket(new SystemMessage(SystemMessage.YOU_USE_S1).addItemName(itemId));
		return Functions.removeItem(player, itemId, count);
	}
}
