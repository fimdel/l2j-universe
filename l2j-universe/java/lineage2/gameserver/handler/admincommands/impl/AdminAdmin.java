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
package lineage2.gameserver.handler.admincommands.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExChangeClientEffectInfo;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.stats.Stats;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminAdmin implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_admin.
		 */
		admin_admin,
		/**
		 * Field admin_play_sounds.
		 */
		admin_play_sounds,
		/**
		 * Field admin_play_sound.
		 */
		admin_play_sound,
		/**
		 * Field admin_silence.
		 */
		admin_silence,
		/**
		 * Field admin_tradeoff.
		 */
		admin_tradeoff,
		/**
		 * Field admin_cfg.
		 */
		admin_cfg,
		/**
		 * Field admin_config.
		 */
		admin_config,
		/**
		 * Field admin_show_html.
		 */
		admin_show_html,
		/**
		 * Field admin_setnpcstate.
		 */
		admin_setnpcstate,
		/**
		 * Field admin_setareanpcstate.
		 */
		admin_setareanpcstate,
		/**
		 * Field admin_showmovie.
		 */
		admin_showmovie,
		/**
		 * Field admin_setzoneinfo.
		 */
		admin_setzoneinfo,
		/**
		 * Field admin_eventtrigger.
		 */
		admin_eventtrigger,
		/**
		 * Field admin_debug.
		 */
		admin_debug,
		/**
		 * Field admin_uievent.
		 */
		admin_uievent,
		/**
		 * Field admin_opensod.
		 */
		admin_opensod,
		/**
		 * Field admin_closesod.
		 */
		admin_closesod,
		/**
		 * Field admin_setsoistage.
		 */
		admin_setsoistage,
		/**
		 * Field admin_soinotify.
		 */
		admin_soinotify,
		/**
		 * Field admin_forcenpcinfo.
		 */
		admin_forcenpcinfo,
		/**
		 * Field admin_loc.
		 */
		admin_loc,
		/**
		 * Field admin_locdump.
		 */
		admin_locdump,
		/**
		 * Field admin_undying.
		 */
		admin_undying
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		StringTokenizer st;
		if (activeChar.getPlayerAccess().Menu)
		{
			switch (command)
			{
				case admin_admin:
					activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/admin.htm"));
					break;
				case admin_play_sounds:
					if (wordList.length == 1)
					{
						activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/songs/songs.htm"));
					}
					else
					{
						try
						{
							activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/songs/songs" + wordList[1] + ".htm"));
						}
						catch (StringIndexOutOfBoundsException e)
						{
						}
					}
					break;
				case admin_play_sound:
					try
					{
						playAdminSound(activeChar, wordList[1]);
					}
					catch (StringIndexOutOfBoundsException e)
					{
					}
					break;
				case admin_silence:
					if (activeChar.getMessageRefusal())
					{
						activeChar.unsetVar("gm_silence");
						activeChar.setMessageRefusal(false);
						activeChar.sendPacket(Msg.MESSAGE_ACCEPTANCE_MODE);
						activeChar.sendEtcStatusUpdate();
					}
					else
					{
						if (Config.SAVE_GM_EFFECTS)
						{
							activeChar.setVar("gm_silence", "true", -1);
						}
						activeChar.setMessageRefusal(true);
						activeChar.sendPacket(Msg.MESSAGE_REFUSAL_MODE);
						activeChar.sendEtcStatusUpdate();
					}
					break;
				case admin_tradeoff:
					try
					{
						if (wordList[1].equalsIgnoreCase("on"))
						{
							activeChar.setTradeRefusal(true);
							Functions.sendDebugMessage(activeChar, "tradeoff enabled");
						}
						else if (wordList[1].equalsIgnoreCase("off"))
						{
							activeChar.setTradeRefusal(false);
							Functions.sendDebugMessage(activeChar, "tradeoff disabled");
						}
					}
					catch (Exception ex)
					{
						if (activeChar.getTradeRefusal())
						{
							Functions.sendDebugMessage(activeChar, "tradeoff currently enabled");
						}
						else
						{
							Functions.sendDebugMessage(activeChar, "tradeoff currently disabled");
						}
					}
					break;
				case admin_show_html:
					String html = wordList[1];
					try
					{
						if (html != null)
						{
							activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/" + html));
						}
						else
						{
							Functions.sendDebugMessage(activeChar, "Html page not found");
						}
					}
					catch (Exception npe)
					{
						Functions.sendDebugMessage(activeChar, "Html page not found");
					}
					break;
				case admin_setnpcstate:
					if (wordList.length < 2)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //setnpcstate state");
						return false;
					}
					int state;
					GameObject target = activeChar.getTarget();
					try
					{
						state = Integer.parseInt(wordList[1]);
					}
					catch (NumberFormatException e)
					{
						Functions.sendDebugMessage(activeChar, "You must specify state");
						return false;
					}
					if (!target.isNpc())
					{
						Functions.sendDebugMessage(activeChar, "You must target an NPC");
						return false;
					}
					NpcInstance npc = (NpcInstance) target;
					npc.setNpcState(state);
					break;
				case admin_setareanpcstate:
					try
					{
						final String val = fullString.substring(15).trim();
						String[] vals = val.split(" ");
						int range = NumberUtils.toInt(vals[0], 0);
						int astate = vals.length > 1 ? NumberUtils.toInt(vals[1], 0) : 0;
						for (NpcInstance n : activeChar.getAroundNpc(range, 200))
						{
							n.setNpcState(astate);
						}
					}
					catch (Exception e)
					{
						Functions.sendDebugMessage(activeChar, "Usage: //setareanpcstate [range] [state]");
					}
					break;
				case admin_showmovie:
					if (wordList.length < 2)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //showmovie id");
						return false;
					}
					int id;
					try
					{
						id = Integer.parseInt(wordList[1]);
					}
					catch (NumberFormatException e)
					{
						Functions.sendDebugMessage(activeChar, "You must specify id");
						return false;
					}
					activeChar.showQuestMovie(id);
					break;
				case admin_setzoneinfo:
					if (wordList.length < 2)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //setzoneinfo id");
						return false;
					}
					int stateid;
					try
					{
						stateid = Integer.parseInt(wordList[1]);
					}
					catch (NumberFormatException e)
					{
						Functions.sendDebugMessage(activeChar, "You must specify id");
						return false;
					}
					activeChar.broadcastPacket(new ExChangeClientEffectInfo(stateid));
					break;
				case admin_eventtrigger:
					if (wordList.length < 2)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //eventtrigger id");
						return false;
					}
					int triggerid;
					try
					{
						triggerid = Integer.parseInt(wordList[1]);
					}
					catch (NumberFormatException e)
					{
						Functions.sendDebugMessage(activeChar, "You must specify id");
						return false;
					}
					activeChar.broadcastPacket(new EventTrigger(triggerid, true));
					break;
				case admin_debug:
					GameObject ob = activeChar.getTarget();
					if (!ob.isPlayer())
					{
						Functions.sendDebugMessage(activeChar, "Only player target is allowed");
						return false;
					}
					Player pl = ob.getPlayer();
					List<String> _s = new ArrayList<>();
					_s.add("==========TARGET STATS:");
					_s.add("==Magic Resist: " + pl.calcStat(Stats.MAGIC_RESIST, null, null));
					_s.add("==Magic Power: " + pl.calcStat(Stats.MAGIC_POWER, 1, null, null));
					_s.add("==Skill Power: " + pl.calcStat(Stats.SKILL_POWER, 1, null, null));
					_s.add("==Cast Break Rate: " + pl.calcStat(Stats.CAST_INTERRUPT, 1, null, null));
					_s.add("==========Powers:");
					_s.add("==Bleed: " + pl.calcStat(Stats.BLEED_POWER, 1, null, null));
					_s.add("==Poison: " + pl.calcStat(Stats.POISON_POWER, 1, null, null));
					_s.add("==Stun: " + pl.calcStat(Stats.STUN_POWER, 1, null, null));
					_s.add("==Root: " + pl.calcStat(Stats.ROOT_POWER, 1, null, null));
					_s.add("==Mental: " + pl.calcStat(Stats.MENTAL_POWER, 1, null, null));
					_s.add("==Sleep: " + pl.calcStat(Stats.SLEEP_POWER, 1, null, null));
					_s.add("==Paralyze: " + pl.calcStat(Stats.PARALYZE_POWER, 1, null, null));
					_s.add("==Cancel: " + pl.calcStat(Stats.CANCEL_POWER, 1, null, null));
					_s.add("==Debuff: " + pl.calcStat(Stats.DEBUFF_POWER, 1, null, null));
					_s.add("==========PvP Stats:");
					_s.add("==Phys Attack Dmg: " + pl.calcStat(Stats.PVP_PHYS_DMG_BONUS, 1, null, null));
					_s.add("==Phys Skill Dmg: " + pl.calcStat(Stats.PVP_PHYS_SKILL_DMG_BONUS, 1, null, null));
					_s.add("==Magic Skill Dmg: " + pl.calcStat(Stats.PVP_MAGIC_SKILL_DMG_BONUS, 1, null, null));
					_s.add("==Phys Attack Def: " + pl.calcStat(Stats.PVP_PHYS_DEFENCE_BONUS, 1, null, null));
					_s.add("==Phys Skill Def: " + pl.calcStat(Stats.PVP_PHYS_SKILL_DEFENCE_BONUS, 1, null, null));
					_s.add("==Magic Skill Def: " + pl.calcStat(Stats.PVP_MAGIC_SKILL_DEFENCE_BONUS, 1, null, null));
					_s.add("==========Reflects:");
					_s.add("==Phys Dmg Chance: " + pl.calcStat(Stats.REFLECT_AND_BLOCK_DAMAGE_CHANCE, null, null));
					_s.add("==Phys Skill Dmg Chance: " + pl.calcStat(Stats.REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE, null, null));
					_s.add("==Magic Skill Dmg Chance: " + pl.calcStat(Stats.REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE, null, null));
					_s.add("==Counterattack: Phys Dmg Chance: " + pl.calcStat(Stats.REFLECT_DAMAGE_PERCENT, null, null));
					_s.add("==Counterattack: Phys Skill Dmg Chance: " + pl.calcStat(Stats.REFLECT_PSKILL_DAMAGE_PERCENT, null, null));
					_s.add("==Counterattack: Magic Skill Dmg Chance: " + pl.calcStat(Stats.REFLECT_MSKILL_DAMAGE_PERCENT, null, null));
					_s.add("==========MP Consume Rate:");
					_s.add("==Magic Skills: " + pl.calcStat(Stats.MP_MAGIC_SKILL_CONSUME, 1, null, null));
					_s.add("==Phys Skills: " + pl.calcStat(Stats.MP_PHYSICAL_SKILL_CONSUME, 1, null, null));
					_s.add("==Music: " + pl.calcStat(Stats.MP_DANCE_SKILL_CONSUME, 1, null, null));
					_s.add("==========Shield:");
					_s.add("==Shield Defence: " + pl.calcStat(Stats.SHIELD_DEFENCE, null, null));
					_s.add("==Shield Defence Rate: " + pl.calcStat(Stats.SHIELD_RATE, null, null));
					_s.add("==Shield Defence Angle: " + pl.calcStat(Stats.SHIELD_ANGLE, null, null));
					_s.add("==========Etc:");
					_s.add("==Fatal Blow Rate: " + pl.calcStat(Stats.FATALBLOW_RATE, null, null));
					_s.add("==Phys Skill Evasion Rate: " + pl.calcStat(Stats.PSKILL_EVASION, null, null));
					_s.add("==Counterattack Rate: " + pl.calcStat(Stats.COUNTER_ATTACK, null, null));
					_s.add("==Pole Attack Angle: " + pl.calcStat(Stats.POLE_ATTACK_ANGLE, null, null));
					_s.add("==Pole Target Count: " + pl.calcStat(Stats.POLE_TARGET_COUNT, 1, null, null));
					_s.add("==========DONE.");
					for (String s : _s)
					{
						Functions.sendDebugMessage(activeChar, s);
					}
					break;
				case admin_uievent:
					if (wordList.length < 5)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //uievent isHide doIncrease startTime endTime Text");
						return false;
					}
					int hide;
					int increase;
					int startTime;
					int endTime;
					String text;
					try
					{
						hide = Integer.parseInt(wordList[1]);
						increase = Integer.parseInt(wordList[2]);
						startTime = Integer.parseInt(wordList[3]);
						endTime = Integer.parseInt(wordList[4]);
						text = wordList[5];
					}
					catch (NumberFormatException e)
					{
						Functions.sendDebugMessage(activeChar, "Invalid format");
						return false;
					}
					activeChar.broadcastPacket(new ExSendUIEvent(activeChar, hide, increase, startTime, endTime, text));
					break;
				case admin_opensod:
					if (wordList.length < 1)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //opensod minutes");
						return false;
					}
					SoDManager.openSeed(Integer.parseInt(wordList[1]) * 60 * 1000L);
					break;
				case admin_closesod:
					SoDManager.closeSeed();
					break;
				case admin_setsoistage:
					if (wordList.length < 1)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //setsoistage stage[1-5]");
						return false;
					}
					SoIManager.setCurrentStage(Integer.parseInt(wordList[1]));
					break;
				case admin_soinotify:
					if (wordList.length < 1)
					{
						Functions.sendDebugMessage(activeChar, "USAGE: //soinotify [1-3]");
						return false;
					}
					switch (Integer.parseInt(wordList[1]))
					{
						case 1:
							SoIManager.notifyCohemenesKill();
							break;
						case 2:
							SoIManager.notifyEkimusKill();
							break;
						case 3:
							SoIManager.notifyHoEDefSuccess();
							break;
					}
					break;
				case admin_forcenpcinfo:
					GameObject obj2 = activeChar.getTarget();
					if (!obj2.isNpc())
					{
						Functions.sendDebugMessage(activeChar, "Only NPC target is allowed");
						return false;
					}
					((NpcInstance) obj2).broadcastCharInfo();
					break;
				case admin_loc:
					Functions.sendDebugMessage(activeChar, "Coords: X:" + activeChar.getLoc().x + " Y:" + activeChar.getLoc().y + " Z:" + activeChar.getLoc().z + " H:" + activeChar.getLoc().h);
					break;
				case admin_locdump:
					st = new StringTokenizer(fullString, " ");
					try
					{
						st.nextToken();
						try
						{
							new File("dumps").mkdir();
							File f = new File("dumps/locdump.txt");
							if (!f.exists())
							{
								f.createNewFile();
							}
							Functions.sendDebugMessage(activeChar, "Coords: X:" + activeChar.getLoc().x + " Y:" + activeChar.getLoc().y + " Z:" + activeChar.getLoc().z + " H:" + activeChar.getLoc().h);
							FileWriter writer = new FileWriter(f, true);
							writer.write("Loc: " + activeChar.getLoc().x + ", " + activeChar.getLoc().y + ", " + activeChar.getLoc().z + "\n");
							writer.close();
						}
						catch (Exception e)
						{
						}
					}
					catch (Exception e)
					{
					}
					break;
				case admin_undying:
					if (activeChar.isUndying())
					{
						activeChar.setUndying(false);
						Functions.sendDebugMessage(activeChar, "Undying state has been disabled.");
					}
					else
					{
						activeChar.setUndying(true);
						Functions.sendDebugMessage(activeChar, "Undying state has been enabled.");
					}
					break;
				default:
					activeChar.sendMessage("Command isn't implemented yet.");
					break;
			}
			return true;
		}
		if (activeChar.getPlayerAccess().CanTeleport)
		{
			switch (command)
			{
				case admin_show_html:
					String html = wordList[1];
					try
					{
						if (html != null)
						{
							if (html.startsWith("tele"))
							{
								activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/" + html));
							}
							else
							{
								activeChar.sendMessage("Access denied");
							}
						}
						else
						{
							activeChar.sendMessage("Html page not found");
						}
					}
					catch (Exception npe)
					{
						activeChar.sendMessage("Html page not found");
					}
					break;
				default:
					break;
			}
			return true;
		}
		return false;
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
	
	/**
	 * Method playAdminSound.
	 * @param activeChar Player
	 * @param sound String
	 */
	public void playAdminSound(Player activeChar, String sound)
	{
		activeChar.broadcastPacket(new PlaySound(sound));
		activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/admin.htm"));
		activeChar.sendMessage("Playing " + sound + ".");
	}
}
