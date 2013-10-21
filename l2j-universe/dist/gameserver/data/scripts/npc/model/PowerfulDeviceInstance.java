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
package npc.model;

import java.util.HashMap;
import java.util.List;

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.commons.util.Rnd; 
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PowerfulDeviceInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is -1632474353838420887)
	 */
	private static final long serialVersionUID = -1632474353838420887L;
	/**
	 * Field _NPC.
	 */
	private static TIntIntHashMap _NPC = new TIntIntHashMap(8);
	
	private static TIntIntHashMap _DESTINYCHANGECLASSES = new TIntIntHashMap(8);
	
	private static HashMap <Integer, String> _NAMECLASSES = new HashMap<Integer, String>();
	
	private int SCROLL_OF_AFTERLIFE = 17600;
	
	private int STONE_OF_DESTINY = 17722;
	
	/**
	 * Constructor for PowerfulDeviceInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PowerfulDeviceInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_NPC.clear();
		_NPC.put(33397, 139);
		_NPC.put(33398, 140);
		_NPC.put(33399, 141);
		_NPC.put(33400, 142);
		_NPC.put(33401, 143);
		_NPC.put(33402, 144);
		_NPC.put(33403, 145);
		_NPC.put(33404, 146);
		_DESTINYCHANGECLASSES.put(139,90);
		_DESTINYCHANGECLASSES.put(140,88);
		_DESTINYCHANGECLASSES.put(141,93);
		_DESTINYCHANGECLASSES.put(142,92);
		_DESTINYCHANGECLASSES.put(143,94);
		_DESTINYCHANGECLASSES.put(144,98);
		_DESTINYCHANGECLASSES.put(145,96);
		_DESTINYCHANGECLASSES.put(146,97);
		_NAMECLASSES.put(90,"Phoenix Knight");
		_NAMECLASSES.put(88,"Duelist");
		_NAMECLASSES.put(93,"Adventurer");
		_NAMECLASSES.put(92,"Sagittarius");
		_NAMECLASSES.put(94,"Archmage");
		_NAMECLASSES.put(98,"Hierophant");
		_NAMECLASSES.put(96,"Arcana Lord");
		_NAMECLASSES.put(97,"Cardinal");
		_NAMECLASSES.put(139,"Sigel Knight");
		_NAMECLASSES.put(140,"Tyrr Warrior");
		_NAMECLASSES.put(141,"Othell Rogue");
		_NAMECLASSES.put(142,"Yul Archer");
		_NAMECLASSES.put(143,"Feoh Wizard");
		_NAMECLASSES.put(144,"Iss Enchanter");
		_NAMECLASSES.put(145,"Wynn Summoner");
		_NAMECLASSES.put(146,"Aerore Healer");
	}
	
	
	/**
	 * Method ObtainIcon
	 * @param skillId
	 * @return format
	 */
	private String obtainIcon(int skillId)
	{
		String format = " ";
		String prefix = "icon.skill";
		if(skillId > 0 && skillId < 10 )
			format = "000" + skillId;
		else if(skillId > 9 && skillId < 100)
			format = "00" + skillId;
		else if(skillId == 995)
			format = "0793";
		else if(skillId == 331)
			format = "0330";
		else if(skillId == 626)
			format = "0312";
		else if(skillId == 926 || skillId == 934 || skillId == 935)
			format = "0925";
		else if(skillId > 778 && skillId < 784)
			format = "0779";
		else if(skillId == 933)
			format = "0470";
		else if(skillId > 99 && skillId < 1000)
			format = "0" + skillId;
		else if(skillId == 1565)
			format = "0213";
		else if(skillId == 1517)
			format = "1536";
		else if(skillId == 1518)
			format = "1537";
		else if(skillId == 1547)
			format = "0065";
		else if(skillId > 999 && skillId < 2000)
			format = String.valueOf(skillId);
		else if(skillId > 4550 && skillId < 4555)
			format = "5739";
		else if(skillId < 4698 && skillId < 4701)
			format = "1331";
		else if(skillId > 4701 && skillId < 4704)
			format = "1332";
		else if(skillId == 6049)
			format = "0094";
		else if(skillId == 20006)
		{
			prefix = "BranchSys2.icon.skill";
			format = "20006";
		}
		else
			format = String.valueOf(skillId);
		String finalCompose = prefix + format;
		return finalCompose;		
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		int oldClassId = 0;
		int newClassId = AwakingManager.getInstance().childOf(player.getClassId());
		if (command.equalsIgnoreCase("Awaken"))
		{
			int essencesCount = AwakingManager.getInstance().giveGiantEssences(player, true);
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.replace("%SP%", String.valueOf(Rnd.get(10000000)));
			htmlMessage.replace("%ESSENCES%", String.valueOf(essencesCount));
			String transferData = new String();
			if(_NPC.get(getNpcId()) != newClassId)
			{
				newClassId = _NPC.get(getNpcId());
				oldClassId = _DESTINYCHANGECLASSES.get(newClassId);
				player.unsetVar("awakenByStoneOfDestiny");
				player.unsetVar("classTarget");
				player.unsetVar("classKeepSkills");
				transferData = "I will ask again... do you wish to Awaken?<br><font color=af9878>(The "+ _NAMECLASSES.get(oldClassId) +"'s skills must be present before awakening as an " + _NAMECLASSES.get(newClassId)+ ").</font>";
			}
			else
			{			
				oldClassId = player.getClassId().getId();
				transferData = "You are not strong enough to receive the giant's power. You need to choose between the giant's power and the god's power.<br>In other words, you should be in the best shape to obtain all the power from the giant. Come back when you are ready.";
			}
			htmlMessage.replace("%TRANSFERDATA%", transferData);
			htmlMessage.setFile("default/" + getNpcId() + "-4.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken1"))
		{
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			String skillList = new String();
			skillList = skillList + "<table border=0 cellpading=8 cellspacing=4>";
			if(_NPC.get(getNpcId()) != newClassId)
			{
				newClassId = _NPC.get(getNpcId());
				oldClassId = _DESTINYCHANGECLASSES.get(newClassId);		
				player.setVar("awakenByStoneOfDestiny","true", 120000);
				player.setVar("classTarget",String.valueOf(newClassId), 120000);
				player.setVar("classKeepSkills",String.valueOf(oldClassId), 120000);		
			}
			else
			{
				oldClassId = player.getClassId().getId();
			}
			List <Integer> skillListId = SkillAcquireHolder.getInstance().getMaintainSkillOnAwake(oldClassId, newClassId);
			for(int sId : skillListId)
			{
				String iconData = obtainIcon(sId);
				String name = new String();
				Skill skl = SkillTable.getInstance().getInfo(sId, SkillTable.getInstance().getBaseLevel(sId));
				if(skl != null)
				{
					name = skl.getName();
				}
				else
				{
					continue;
				}
				skillList = skillList + "<tr><td width=34 height=34><img src="+ iconData +" width=32 height=32></td><td width=200> " + name + " </td></tr><tr><td colspan=2><br></td></tr>";
			}
			skillList = skillList +"</table>";
			htmlMessage.replace("%SKILLIST%", skillList);
			htmlMessage.setFile("default/" + getNpcId() + "-5.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken2"))
		{
			if(player.getVarB("awakenByStoneOfDestiny", false))
			{
				int targetClass = player.getVarInt("classTarget");			
				AwakingManager.getInstance().SendReqToAwaking(player,targetClass);
			}
			else
			{
				player.setVar("AwakenPrepared", "true", -1);
				AwakingManager.getInstance().SendReqToAwaking(player);
			}
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param replace Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		String htmlpath;
		int newClassId = 0;
		if (val == 0)
		{
			if ((player.getClassLevel() == 4) && (player.getInventory().getCountOf(SCROLL_OF_AFTERLIFE) > 0))
			{
				newClassId = AwakingManager.getInstance().childOf(player.getClassId());
				if (player.getSummonList().size() > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (player.getInventory().getCountOf(STONE_OF_DESTINY) > 0 && _NPC.get(getNpcId()) != newClassId)
				{	
					htmlpath = getHtmlPath(getNpcId(), 6, player);
				}
				else if (_NPC.get(getNpcId()) != newClassId)
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				if (player.getVarB("AwakenPrepared", false))
				{
					AwakingManager.getInstance().SendReqToAwaking(player);
					return;				
				}
			}
			else
			{
				htmlpath = getHtmlPath(getNpcId(), val, player);
			}
		}
		else
		{
			htmlpath = getHtmlPath(getNpcId(), val, player);
		}
		showChatWindow(player, htmlpath, replace);
	}
}
