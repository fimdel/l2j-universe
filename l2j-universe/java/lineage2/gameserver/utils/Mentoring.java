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
package lineage2.gameserver.utils;

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Mentoring
{
	/**
	 * Field SIGN_OF_TUTOR.
	 */
	public static final Map<Integer, Integer> SIGN_OF_TUTOR = new HashMap<Integer, Integer>()
	{
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		
		{
			put(10, 1);
			put(20, 25);
			put(30, 30);
			put(40, 63);
			put(50, 68);
			put(51, 16);
			put(52, 7);
			put(53, 9);
			put(54, 11);
			put(55, 13);
			put(56, 16);
			put(57, 19);
			put(58, 23);
			put(59, 29);
			put(60, 37);
			put(61, 51);
			put(62, 20);
			put(63, 24);
			put(64, 30);
			put(65, 36);
			put(66, 44);
			put(67, 55);
			put(68, 67);
			put(69, 84);
			put(70, 107);
			put(71, 120);
			put(72, 92);
			put(73, 114);
			put(74, 139);
			put(75, 172);
			put(76, 213);
			put(77, 629);
			put(78, 322);
			put(79, 413);
			put(80, 491);
			put(81, 663);
			put(82, 746);
			put(83, 850);
			put(84, 987);
			put(85, 1149);
			put(86, 2015);
		}
	};
	/**
	 * Field effectsForMentee.
	 */
	public static int[] effectsForMentee =
	{
		9227,
		9228,
		9229,
		9230,
		9231,
		9232,
		9233
	};
	/**
	 * Field skillForMenee.
	 */
	public static int skillForMentee = 9379;
	/**
	 * Field skillsForMentor.
	 */
	public static int[] skillsForMentor =
	{
		9376,
		9377,
		9378
	};
	/**
	 * Field effectForMentor.
	 */
	public static int effectForMentor = 9256;
	
	/**
	 * Method applyMenteeBuffs.
	 * @param menteePlayer Player
	 */
	public static void applyMenteeBuffs(Player menteePlayer)
	{
		addMentoringSkills(menteePlayer);
		if (menteePlayer.getLevel() < 86)
		{
			for (int effect : effectsForMentee)
			{
				if (!menteePlayer.getEffectList().containEffectFromSkills(new int[effect]))
				{
					SkillTable.getInstance().getInfo(effect, 1).getEffects(menteePlayer, menteePlayer, false, false);
				}
			}
		}
	}
	
	/**
	 * Method applyMentorBuffs.
	 * @param dependPlayer Player
	 * @param updateMentees boolean
	 */
	public static void applyMentorBuffs(Player mentorPlayer)
	{
		addMentoringSkills(mentorPlayer);
		if (mentorPlayer.getClassId().getId() > 138 && mentorPlayer.getLevel() > 85)
		{
			if (!mentorPlayer.getEffectList().containEffectFromSkills(new int[effectForMentor]))
			{
				SkillTable.getInstance().getInfo(effectForMentor, mentorPlayer.getMenteeMentorList().getOnlineMenteesCount(mentorPlayer)).getEffects(mentorPlayer, mentorPlayer, false, false);
			}
		}
	}
	
	public static void cancelMenteeBuffs(Player mentee)
	{
		if (mentee.getMenteeMentorList().someOneOnline(false) 
				|| mentee.getMenteeMentorList().someOneOnline(true))
		{
			return;
		}
		for (int buff : effectsForMentee)
		{
			mentee.getEffectList().removeEffect(buff);
		}
	}
	
	public static void cancelMentorBuffs(Player mentor)
	{
		if (mentor.getMenteeMentorList().someOneOnline(false)
				|| mentor.getMenteeMentorList().someOneOnline(true))
		{
			mentor.getEffectList().removeEffect(effectForMentor);
			SkillTable.getInstance().getInfo(effectForMentor, mentor.getMenteeMentorList().getOnlineMenteesCount(mentor)).getEffects(mentor, mentor, false, false);
			return;
		}
		mentor.getEffectList().removeEffect(effectForMentor);
	}
	
	/**
	 * Method addMentoringSkills.
	 * @param mentoringPlayer Player
	 */
	public static void addMentoringSkills(Player mentoringPlayer)
	{
		if (mentoringPlayer.getMenteeMentorList().getMentor() == 0)
		{
			for (int skillId : skillsForMentor)
			{
				Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
				mentoringPlayer.addSkill(skill, true);
				mentoringPlayer.sendSkillList();
			}
		}
		else
		{
			Skill skill = SkillTable.getInstance().getInfo(skillForMentee, 1);
			mentoringPlayer.addSkill(skill, true);
			mentoringPlayer.sendSkillList();
		}
	}
	
	/**
	 * Method setTimePenalty.
	 * @param mentorId int
	 * @param timeTo long
	 * @param expirationTime long
	 */
	public static void setTimePenalty(int mentorId, long timeTo, long expirationTime)
	{
		Player mentor = World.getPlayer(mentorId);
		if ((mentor != null) && mentor.isOnline())
		{
			mentor.setVar("mentorPenalty", timeTo, -1);
		}
		else
		{
			mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (?,'user-var','mentorPenalty',?,?)", mentorId, timeTo, expirationTime);
		}
	}
	
	public static long getTimePenalty(int mentorId)
	{
		Player mentor = World.getPlayer(mentorId);
		if ((mentor != null) && mentor.isOnline())
		{
			return mentor.getVarLong("mentorPenalty");
		}
		else
		{
			return (long) mysql.get("SELECT value FROM character_variables WHERE obj_id = " + mentorId);
		}
	}
	
	/**
	 * Method sendMentorMail.
	 * @param receiver Player
	 * @param items Map<Integer,Long>
	 */
	public static void sendMentorMail(Player receiver, Map<Integer, Long> items)
	{
		if (receiver == null)
		{
			return;
		}
		if (items.keySet().size() > 8)
		{
			return;
		}
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName("Mentoring System");
		mail.setReceiverId(receiver.getObjectId());
		mail.setReceiverName(receiver.getName());
		mail.setTopic("Mentoring");
		mail.setBody("Sign of Tutor for Mentor");
		for (Map.Entry<Integer, Long> itm : items.entrySet())
		{
			ItemInstance item = ItemFunctions.createItem(itm.getKey());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(itm.getValue());
			item.save();
			mail.addAttachment(item);
		}
		mail.setType(Mail.SenderType.MENTOR);
		mail.setUnread(true);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		mail.save();
		receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
}
