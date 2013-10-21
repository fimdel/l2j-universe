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
package actions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.admincommands.impl.AdminEditChar;
import lineage2.gameserver.model.AggroList.HateComparator;
import lineage2.gameserver.model.AggroList.HateInfo;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.Util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OnActionShift extends Functions
{
	/**
	 * Method OnActionShift_NpcInstance.
	 * @param player Player
	 * @param object GameObject
	 * @return boolean
	 */
	public boolean OnActionShift_NpcInstance(Player player, GameObject object)
	{
		if ((player == null) || (object == null))
		{
			return false;
		}
		if (!Config.ALLOW_NPC_SHIFTCLICK && !player.isGM())
		{
			if (Config.ALT_GAME_SHOW_DROPLIST && object.isNpc())
			{
				NpcInstance npc = (NpcInstance) object;
				if (npc.isDead())
				{
					return false;
				}
				droplist(player, npc);
			}
			return false;
		}
		if (object.isNpc())
		{
			NpcInstance npc = (NpcInstance) object;
			if (npc.isDead())
			{
				return false;
			}
			String dialog;
			if (Config.ALT_FULL_NPC_STATS_PAGE)
			{
				dialog = HtmCache.getInstance().getNotNull("scripts/actions/player.L2NpcInstance.onActionShift.full.htm", player);
				dialog = dialog.replaceFirst("%class%", String.valueOf(npc.getClass().getSimpleName().replaceFirst("L2", "").replaceFirst("Instance", "")));
				dialog = dialog.replaceFirst("%id%", String.valueOf(npc.getNpcId()));
				dialog = dialog.replaceFirst("%respawn%", String.valueOf((npc.getSpawn() != null) ? Util.formatTime(npc.getSpawn().getRespawnDelay()) : "0"));
				dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(npc.getWalkSpeed()));
				dialog = dialog.replaceFirst("%evs%", String.valueOf(npc.getEvasionRate(null)));
				dialog = dialog.replaceFirst("%acc%", String.valueOf(npc.getAccuracy()));
				dialog = dialog.replaceFirst("%crt%", String.valueOf(npc.getCriticalHit(null, null)));
				dialog = dialog.replaceFirst("%aspd%", String.valueOf(npc.getPAtkSpd()));
				dialog = dialog.replaceFirst("%cspd%", String.valueOf(npc.getMAtkSpd()));
				dialog = dialog.replaceFirst("%currentMP%", String.valueOf(npc.getCurrentMp()));
				dialog = dialog.replaceFirst("%currentHP%", String.valueOf(npc.getCurrentHp()));
				dialog = dialog.replaceFirst("%loc%", "");
				dialog = dialog.replaceFirst("%dist%", String.valueOf((int) npc.getDistance3D(player)));
				dialog = dialog.replaceFirst("%killed%", String.valueOf(0));
				dialog = dialog.replaceFirst("%spReward%", String.valueOf(npc.getSpReward()));
				dialog = dialog.replaceFirst("%xyz%", npc.getLoc().x + " " + npc.getLoc().y + " " + npc.getLoc().z);
				dialog = dialog.replaceFirst("%ai_type%", npc.getAI().getClass().getSimpleName());
				dialog = dialog.replaceFirst("%direction%", PositionUtils.getDirectionTo(npc, player).toString().toLowerCase());
				StringBuilder b = new StringBuilder("");
				for (GlobalEvent e : npc.getEvents())
				{
					b.append(e.toString()).append(';');
				}
				dialog = dialog.replaceFirst("%event%", b.toString());
			}
			else
			{
				dialog = HtmCache.getInstance().getNotNull("scripts/actions/player.L2NpcInstance.onActionShift.htm", player);
			}
			dialog = dialog.replaceFirst("%name%", nameNpc(npc));
			dialog = dialog.replaceFirst("%id%", String.valueOf(npc.getNpcId()));
			dialog = dialog.replaceFirst("%level%", String.valueOf(npc.getLevel()));
			dialog = dialog.replaceFirst("%respawn%", String.valueOf((npc.getSpawn() != null) ? Util.formatTime(npc.getSpawn().getRespawnDelay()) : "0"));
			dialog = dialog.replaceFirst("%factionId%", String.valueOf(npc.getFaction()));
			dialog = dialog.replaceFirst("%aggro%", String.valueOf(npc.getAggroRange()));
			dialog = dialog.replaceFirst("%maxHp%", String.valueOf(npc.getMaxHp()));
			dialog = dialog.replaceFirst("%maxMp%", String.valueOf(npc.getMaxMp()));
			dialog = dialog.replaceFirst("%pDef%", String.valueOf(npc.getPDef(null)));
			dialog = dialog.replaceFirst("%mDef%", String.valueOf(npc.getMDef(null, null)));
			dialog = dialog.replaceFirst("%pAtk%", String.valueOf(npc.getPAtk(null)));
			dialog = dialog.replaceFirst("%mAtk%", String.valueOf(npc.getMAtk(null, null)));
			dialog = dialog.replaceFirst("%expReward%", String.valueOf(npc.getExpReward()));
			dialog = dialog.replaceFirst("%spReward%", String.valueOf(npc.getSpReward()));
			dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(npc.getRunSpeed()));
			if (player.isGM())
			{
				dialog = dialog.replaceFirst("%AI%", String.valueOf(npc.getAI()) + ",<br1>active: " + npc.getAI().isActive() + ",<br1>intention: " + npc.getAI().getIntention());
			}
			else
			{
				dialog = dialog.replaceFirst("%AI%", "");
			}
			show(dialog, player, npc);
		}
		return true;
	}
	
	/**
	 * Method getNpcRaceById.
	 * @param raceId int
	 * @return String
	 */
	public String getNpcRaceById(int raceId)
	{
		switch (raceId)
		{
			case 1:
				return "Undead";
			case 2:
				return "Magic Creatures";
			case 3:
				return "Beasts";
			case 4:
				return "Animals";
			case 5:
				return "Plants";
			case 6:
				return "Humanoids";
			case 7:
				return "Spirits";
			case 8:
				return "Angels";
			case 9:
				return "Demons";
			case 10:
				return "Dragons";
			case 11:
				return "Giants";
			case 12:
				return "Bugs";
			case 13:
				return "Fairies";
			case 14:
				return "Humans";
			case 15:
				return "Elves";
			case 16:
				return "Dark Elves";
			case 17:
				return "Orcs";
			case 18:
				return "Dwarves";
			case 19:
				return "Others";
			case 20:
				return "Non-living Beings";
			case 21:
				return "Siege Weapons";
			case 22:
				return "Defending Army";
			case 23:
				return "Mercenaries";
			case 24:
				return "Unknown Creature";
			case 25:
				return "Kamael";
			default:
				return "Not defined";
		}
	}
	
	/**
	 * Method droplist.
	 */
	public void droplist()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		droplist(player, npc);
	}
	
	/**
	 * Method droplist.
	 * @param player Player
	 * @param npc NpcInstance
	 */
	public void droplist(Player player, NpcInstance npc)
	{
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (Config.ALT_GAME_SHOW_DROPLIST)
		{
			RewardListInfo.showInfo(player, npc);
		}
	}
	
	/**
	 * Method quests.
	 */
	public void quests()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		StringBuilder dialog = new StringBuilder("<html><body><center><font color=\"LEVEL\">");
		dialog.append(nameNpc(npc)).append("<br></font></center><br>");
		Map<QuestEventType, Quest[]> list = npc.getTemplate().getQuestEvents();
		for (Map.Entry<QuestEventType, Quest[]> entry : list.entrySet())
		{
			for (Quest q : entry.getValue())
			{
				dialog.append(entry.getKey()).append(' ').append(q.getClass().getSimpleName()).append("<br1>");
			}
		}
		dialog.append("</body></html>");
		show(dialog.toString(), player, npc);
	}
	
	/**
	 * Method skills.
	 */
	public void skills()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		StringBuilder dialog = new StringBuilder("<html><body><center><font color=\"LEVEL\">");
		dialog.append(nameNpc(npc)).append("<br></font></center>");
		Collection<Skill> list = npc.getAllSkills();
		if ((list != null) && !list.isEmpty())
		{
			dialog.append("<br>Active:<br>");
			for (Skill s : list)
			{
				if (s.isActive())
				{
					dialog.append(s.getName()).append("<br1>");
				}
			}
			dialog.append("<br>Passive:<br>");
			for (Skill s : list)
			{
				if (!s.isActive())
				{
					dialog.append(s.getName()).append("<br1>");
				}
			}
		}
		dialog.append("</body></html>");
		show(dialog.toString(), player, npc);
	}
	
	/**
	 * Method effects.
	 */
	public void effects()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		StringBuilder dialog = new StringBuilder("<html><body><center><font color=\"LEVEL\">");
		dialog.append(nameNpc(npc)).append("<br></font></center><br>");
		List<Effect> list = npc.getEffectList().getAllEffects();
		if ((list != null) && !list.isEmpty())
		{
			for (Effect e : list)
			{
				dialog.append(e.getSkill().getName()).append("<br1>");
			}
		}
		dialog.append("<br><center><button value=\"");
		dialog.append("Refresh");
		dialog.append("\" action=\"bypass -h scripts_actions.OnActionShift:effects\" width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" /></center></body></html>");
		show(dialog.toString(), player, npc);
	}
	
	/**
	 * Method stats.
	 */
	public void stats()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		String dialog = HtmCache.getInstance().getNotNull("scripts/actions/player.L2NpcInstance.stats.htm", player);
		dialog = dialog.replaceFirst("%name%", nameNpc(npc));
		dialog = dialog.replaceFirst("%level%", String.valueOf(npc.getLevel()));
		dialog = dialog.replaceFirst("%factionId%", String.valueOf(npc.getFaction()));
		dialog = dialog.replaceFirst("%aggro%", String.valueOf(npc.getAggroRange()));
		dialog = dialog.replaceFirst("%race%", getNpcRaceById(npc.getTemplate().getRace()));
		dialog = dialog.replaceFirst("%maxHp%", String.valueOf(npc.getMaxHp()));
		dialog = dialog.replaceFirst("%maxMp%", String.valueOf(npc.getMaxMp()));
		dialog = dialog.replaceFirst("%pDef%", String.valueOf(npc.getPDef(null)));
		dialog = dialog.replaceFirst("%mDef%", String.valueOf(npc.getMDef(null, null)));
		dialog = dialog.replaceFirst("%pAtk%", String.valueOf(npc.getPAtk(null)));
		dialog = dialog.replaceFirst("%mAtk%", String.valueOf(npc.getMAtk(null, null)));
		dialog = dialog.replaceFirst("%accuracy%", String.valueOf(npc.getAccuracy()));
		dialog = dialog.replaceFirst("%evasionRate%", String.valueOf(npc.getEvasionRate(null)));
		dialog = dialog.replaceFirst("%criticalHit%", String.valueOf(npc.getCriticalHit(null, null)));
		dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(npc.getRunSpeed()));
		dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(npc.getWalkSpeed()));
		dialog = dialog.replaceFirst("%pAtkSpd%", String.valueOf(npc.getPAtkSpd()));
		dialog = dialog.replaceFirst("%mAtkSpd%", String.valueOf(npc.getMAtkSpd()));
		show(dialog, player, npc);
	}
	
	/**
	 * Method resists.
	 */
	public void resists()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		StringBuilder dialog = new StringBuilder("<html><body><center><font color=\"LEVEL\">");
		dialog.append(nameNpc(npc)).append("<br></font></center><table width=\"80%\">");
		boolean hasResist;
		hasResist = addResist(dialog, "Fire", npc.calcStat(Stats.DEFENCE_FIRE, 0, null, null));
		hasResist |= addResist(dialog, "Wind", npc.calcStat(Stats.DEFENCE_WIND, 0, null, null));
		hasResist |= addResist(dialog, "Water", npc.calcStat(Stats.DEFENCE_WATER, 0, null, null));
		hasResist |= addResist(dialog, "Earth", npc.calcStat(Stats.DEFENCE_EARTH, 0, null, null));
		hasResist |= addResist(dialog, "Light", npc.calcStat(Stats.DEFENCE_HOLY, 0, null, null));
		hasResist |= addResist(dialog, "Darkness", npc.calcStat(Stats.DEFENCE_UNHOLY, 0, null, null));
		hasResist |= addResist(dialog, "Bleed", npc.calcStat(Stats.BLEED_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Poison", npc.calcStat(Stats.POISON_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Stun", npc.calcStat(Stats.STUN_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Root", npc.calcStat(Stats.ROOT_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Sleep", npc.calcStat(Stats.SLEEP_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Paralyze", npc.calcStat(Stats.PARALYZE_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Mental", npc.calcStat(Stats.MENTAL_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Debuff", npc.calcStat(Stats.DEBUFF_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Cancel", npc.calcStat(Stats.CANCEL_RESIST, 0, null, null));
		hasResist |= addResist(dialog, "Sword", 100 - npc.calcStat(Stats.SWORD_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Dual Sword", 100 - npc.calcStat(Stats.DUAL_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Blunt", 100 - npc.calcStat(Stats.BLUNT_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Dagger", 100 - npc.calcStat(Stats.DAGGER_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Bow", 100 - npc.calcStat(Stats.BOW_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Crossbow", 100 - npc.calcStat(Stats.CROSSBOW_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Polearm", 100 - npc.calcStat(Stats.POLE_WPN_VULNERABILITY, null, null));
		hasResist |= addResist(dialog, "Fist", 100 - npc.calcStat(Stats.FIST_WPN_VULNERABILITY, null, null));
		if (!hasResist)
		{
			dialog.append("</table>No resists</body></html>");
		}
		else
		{
			dialog.append("</table></body></html>");
		}
		show(dialog.toString(), player, npc);
	}
	
	/**
	 * Method addResist.
	 * @param dialog StringBuilder
	 * @param name String
	 * @param val double
	 * @return boolean
	 */
	private boolean addResist(StringBuilder dialog, String name, double val)
	{
		if (val == 0)
		{
			return false;
		}
		dialog.append("<tr><td>").append(name).append("</td><td>");
		if (val == Double.POSITIVE_INFINITY)
		{
			dialog.append("MAX");
		}
		else if (val == Double.NEGATIVE_INFINITY)
		{
			dialog.append("MIN");
		}
		else
		{
			dialog.append(String.valueOf((int) val));
			dialog.append("</td></tr>");
			return true;
		}
		dialog.append("</td></tr>");
		return true;
	}
	
	/**
	 * Method aggro.
	 */
	public void aggro()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		StringBuilder dialog = new StringBuilder("<html><body><table width=\"80%\"><tr><td>Attacker</td><td>Damage</td><td>Hate</td></tr>");
		Set<HateInfo> set = new TreeSet<>(HateComparator.getInstance());
		set.addAll(npc.getAggroList().getCharMap().values());
		for (HateInfo aggroInfo : set)
		{
			dialog.append("<tr><td>" + aggroInfo.attacker.getName() + "</td><td>" + aggroInfo.damage + "</td><td>" + aggroInfo.hate + "</td></tr>");
		}
		dialog.append("</table><br><center><button value=\"");
		dialog.append("Refresh");
		dialog.append("\" action=\"bypass -h scripts_actions.OnActionShift:aggro\" width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" /></center></body></html>");
		show(dialog.toString(), player, npc);
	}
	
	/**
	 * Method OnActionShift_DoorInstance.
	 * @param player Player
	 * @param object GameObject
	 * @return boolean
	 */
	public boolean OnActionShift_DoorInstance(Player player, GameObject object)
	{
		if ((player == null) || (object == null) || !player.getPlayerAccess().Door || !object.isDoor())
		{
			return false;
		}
		String dialog;
		DoorInstance door = (DoorInstance) object;
		dialog = HtmCache.getInstance().getNotNull("scripts/actions/admin.L2DoorInstance.onActionShift.htm", player);
		dialog = dialog.replaceFirst("%CurrentHp%", String.valueOf((int) door.getCurrentHp()));
		dialog = dialog.replaceFirst("%MaxHp%", String.valueOf(door.getMaxHp()));
		dialog = dialog.replaceAll("%ObjectId%", String.valueOf(door.getObjectId()));
		dialog = dialog.replaceFirst("%doorId%", String.valueOf(door.getDoorId()));
		dialog = dialog.replaceFirst("%pdef%", String.valueOf(door.getPDef(null)));
		dialog = dialog.replaceFirst("%mdef%", String.valueOf(door.getMDef(null, null)));
		dialog = dialog.replaceFirst("%type%", door.getDoorType().name());
		dialog = dialog.replaceFirst("%upgradeHP%", String.valueOf(door.getUpgradeHp()));
		dialog = dialog.replaceFirst("%geoIndex%", String.valueOf(door.getGeoIndex()));
		StringBuilder b = new StringBuilder("");
		for (GlobalEvent e : door.getEvents())
		{
			b.append(e.toString()).append(';');
		}
		dialog = dialog.replaceFirst("%event%", b.toString());
		show(dialog, player);
		player.sendActionFailed();
		return true;
	}
	
	/**
	 * Method OnActionShift_Player.
	 * @param player Player
	 * @param object GameObject
	 * @return boolean
	 */
	public boolean OnActionShift_Player(Player player, GameObject object)
	{
		if ((player == null) || (object == null) || !player.getPlayerAccess().CanViewChar)
		{
			return false;
		}
		if (object.isPlayer())
		{
			AdminEditChar.showCharacterList(player, (Player) object);
		}
		return true;
	}
	
	/**
	 * Method OnActionShift_PetInstance.
	 * @param player Player
	 * @param object GameObject
	 * @return boolean
	 */
	public boolean OnActionShift_PetInstance(Player player, GameObject object)
	{
		if ((player == null) || (object == null) || !player.getPlayerAccess().CanViewChar)
		{
			return false;
		}
		if (object.isPet())
		{
			PetInstance pet = (PetInstance) object;
			String dialog;
			dialog = HtmCache.getInstance().getNotNull("scripts/actions/admin.L2PetInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%name%", HtmlUtils.htmlNpcName(pet.getNpcId()));
			dialog = dialog.replaceFirst("%title%", String.valueOf(StringUtils.isEmpty(pet.getTitle()) ? "Empty" : pet.getTitle()));
			dialog = dialog.replaceFirst("%level%", String.valueOf(pet.getLevel()));
			dialog = dialog.replaceFirst("%class%", String.valueOf(pet.getClass().getSimpleName().replaceFirst("L2", "").replaceFirst("Instance", "")));
			dialog = dialog.replaceFirst("%xyz%", pet.getLoc().x + " " + pet.getLoc().y + " " + pet.getLoc().z);
			dialog = dialog.replaceFirst("%heading%", String.valueOf(pet.getLoc().h));
			dialog = dialog.replaceFirst("%owner%", String.valueOf(pet.getPlayer().getName()));
			dialog = dialog.replaceFirst("%ownerId%", String.valueOf(pet.getPlayer().getObjectId()));
			dialog = dialog.replaceFirst("%npcId%", String.valueOf(pet.getNpcId()));
			dialog = dialog.replaceFirst("%controlItemId%", String.valueOf(pet.getControlItem().getItemId()));
			dialog = dialog.replaceFirst("%exp%", String.valueOf(pet.getExp()));
			dialog = dialog.replaceFirst("%sp%", String.valueOf(pet.getSp()));
			dialog = dialog.replaceFirst("%maxHp%", String.valueOf(pet.getMaxHp()));
			dialog = dialog.replaceFirst("%maxMp%", String.valueOf(pet.getMaxMp()));
			dialog = dialog.replaceFirst("%currHp%", String.valueOf((int) pet.getCurrentHp()));
			dialog = dialog.replaceFirst("%currMp%", String.valueOf((int) pet.getCurrentMp()));
			dialog = dialog.replaceFirst("%pDef%", String.valueOf(pet.getPDef(null)));
			dialog = dialog.replaceFirst("%mDef%", String.valueOf(pet.getMDef(null, null)));
			dialog = dialog.replaceFirst("%pAtk%", String.valueOf(pet.getPAtk(null)));
			dialog = dialog.replaceFirst("%mAtk%", String.valueOf(pet.getMAtk(null, null)));
			dialog = dialog.replaceFirst("%accuracy%", String.valueOf(pet.getAccuracy()));
			dialog = dialog.replaceFirst("%evasionRate%", String.valueOf(pet.getEvasionRate(null)));
			dialog = dialog.replaceFirst("%crt%", String.valueOf(pet.getCriticalHit(null, null)));
			dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(pet.getRunSpeed()));
			dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(pet.getWalkSpeed()));
			dialog = dialog.replaceFirst("%pAtkSpd%", String.valueOf(pet.getPAtkSpd()));
			dialog = dialog.replaceFirst("%mAtkSpd%", String.valueOf(pet.getMAtkSpd()));
			dialog = dialog.replaceFirst("%dist%", String.valueOf((int) pet.getRealDistance(player)));
			dialog = dialog.replaceFirst("%STR%", String.valueOf(pet.getSTR()));
			dialog = dialog.replaceFirst("%DEX%", String.valueOf(pet.getDEX()));
			dialog = dialog.replaceFirst("%CON%", String.valueOf(pet.getCON()));
			dialog = dialog.replaceFirst("%INT%", String.valueOf(pet.getINT()));
			dialog = dialog.replaceFirst("%WIT%", String.valueOf(pet.getWIT()));
			dialog = dialog.replaceFirst("%MEN%", String.valueOf(pet.getMEN()));
			show(dialog, player);
		}
		return true;
	}
	
	/**
	 * Method OnActionShift_ItemInstance.
	 * @param player Player
	 * @param object GameObject
	 * @return boolean
	 */
	public boolean OnActionShift_ItemInstance(Player player, GameObject object)
	{
		if ((player == null) || (object == null) || !player.getPlayerAccess().CanViewChar)
		{
			return false;
		}
		if (object.isItem())
		{
			String dialog;
			ItemInstance item = (ItemInstance) object;
			dialog = HtmCache.getInstance().getNotNull("scripts/actions/admin.L2ItemInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%name%", String.valueOf(item.getTemplate().getName()));
			dialog = dialog.replaceFirst("%objId%", String.valueOf(item.getObjectId()));
			dialog = dialog.replaceFirst("%itemId%", String.valueOf(item.getItemId()));
			dialog = dialog.replaceFirst("%grade%", String.valueOf(item.getCrystalType()));
			dialog = dialog.replaceFirst("%count%", String.valueOf(item.getCount()));
			Player owner = GameObjectsStorage.getPlayer(item.getOwnerId());
			dialog = dialog.replaceFirst("%owner%", String.valueOf(owner == null ? "none" : owner.getName()));
			dialog = dialog.replaceFirst("%ownerId%", String.valueOf(item.getOwnerId()));
			for (Element e : Element.VALUES)
			{
				dialog = dialog.replaceFirst("%" + e.name().toLowerCase() + "Val%", String.valueOf(item.getAttributeElementValue(e, true)));
			}
			dialog = dialog.replaceFirst("%attrElement%", String.valueOf(item.getAttributeElement()));
			dialog = dialog.replaceFirst("%attrValue%", String.valueOf(item.getAttributeElementValue()));
			dialog = dialog.replaceFirst("%enchLevel%", String.valueOf(item.getEnchantLevel()));
			dialog = dialog.replaceFirst("%type%", String.valueOf(item.getItemType()));
			dialog = dialog.replaceFirst("%dropTime%", String.valueOf(item.getDropTimeOwner()));
			show(dialog, player);
			player.sendActionFailed();
		}
		return true;
	}
	
	/**
	 * Method nameNpc.
	 * @param npc NpcInstance
	 * @return String
	 */
	private String nameNpc(NpcInstance npc)
	{
		if (npc.getNameNpcString() == NpcString.NONE)
		{
			return HtmlUtils.htmlNpcName(npc.getNpcId());
		}
		return HtmlUtils.htmlNpcString(npc.getNameNpcString().getId(), npc.getName());
	}
}
