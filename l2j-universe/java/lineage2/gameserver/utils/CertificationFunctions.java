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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.CharacterSubclassDAO;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CertificationFunctions
{
	/**
	 * Field PATH. (value is ""villagemaster/certification/"")
	 */
	public static final String PATH = "villagemaster/certification/";
	
	private static HashMap <Integer, Integer> _certificationList = new HashMap <Integer,Integer>();

	private static CertificationFunctions _instance;

	private static int CERTIFICATE = 10280;

	private static int DUAL_CERTIFICATE = 36078;
	
	
	/**
	 * Method getInstance.
	 * @return SubClassTable
	 */
	public static CertificationFunctions getInstance()
	{
		if (_instance == null)
		{
			_instance = new CertificationFunctions();
		}
		return _instance;
	}
	
	public CertificationFunctions()
	{
		_certificationList.put(65, SubClass.CERTIFICATION_65);
		_certificationList.put(70, SubClass.CERTIFICATION_70);
		_certificationList.put(75, SubClass.CERTIFICATION_75);
		_certificationList.put(80, SubClass.CERTIFICATION_80);
		_certificationList.put(85, SubClass.DUALCERTIFICATION_85);
		_certificationList.put(90, SubClass.DUALCERTIFICATION_90);
		_certificationList.put(95, SubClass.DUALCERTIFICATION_95);
		_certificationList.put(99, SubClass.DUALCERTIFICATION_99);
	}
	/**
	 * Method showCertificationList.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static boolean checkConditionSkillList(NpcInstance npc, Player player, Integer level)
	{
		if(!player.isBaseClassActive())
		{
			Functions.show(PATH + (level < 85 ? "certificateSkillList-nobase.htm" : "dualcertificateSkillList-nobase.htm"), player, npc);
			return false;
		}
		int certificate = level < 85 ? CERTIFICATE : DUAL_CERTIFICATE;
		if(player.getInventory().getItemByItemId(certificate) == null)
		{
			Functions.show(PATH + (level < 85 ? "certificateSkillList-nocertificate.htm" : "dualcertificateSkillList-nocertificate.htm"), player, npc);
			return false;			
		}
		return true;
	}
	
	/**
	 * Method showCertificationList.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void showCertificationList(NpcInstance npc, Player player, Integer level)
	{
		if (!checkConditions(level, npc, player, true))
		{
			return;
		}
		Functions.show(PATH + (level < 85 ? "certificatelist.htm" : "dualcertificatelist.htm"), player, npc);
	}
	
	/**
	 * Method getCertification.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void getCertification(Integer level, NpcInstance npc, Player player)
	{
		if (!checkConditions(level, npc, player, Config.ALT_GAME_SUB_BOOK))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if ((clzz.isCertificationGet(SubClass.CERTIFICATION_65) && level == 65) || (clzz.isDualCertificationGet(SubClass.DUALCERTIFICATION_85) && level == 85) || level < 85 ? clzz.isCertificationGet(_certificationList.get(level)) : clzz.isDualCertificationGet(_certificationList.get(level)) )
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}
		if(((level > 65 && level <= 80) && !clzz.isCertificationGet(SubClass.CERTIFICATION_65)) || ((level > 85 && level <= 99) && !clzz.isDualCertificationGet(SubClass.DUALCERTIFICATION_85)))
		{
			Functions.show(PATH + (level > 65 && level <= 80 ? "certificate-fail.htm" : "dualcertificate-fail.htm"), player, npc);
			return;			
		}
		Functions.show(PATH  + "certificate-confirmation.htm", player, npc, "<?LEVEL?>", String.valueOf(level));
	}
	

	/**
	 * Method confirmCertification.
	 * @param integer level
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void confirmCertification(Integer level, NpcInstance npc, Player player)
	{
		SubClass clzz = player.getActiveSubClass();
		Functions.addItem(player, level < 85 ? CERTIFICATE : DUAL_CERTIFICATE, 1);
		if(level < 85)
		{
			clzz.addCertification(_certificationList.get(level));
		}
		else
			clzz.addDualCertification(_certificationList.get(level));
		Functions.show(PATH  + "certificate-sucess.htm", player, npc);
		player.store(true);
	}
	
	/**
	 * Method cancelCertification.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void cancelCertification(NpcInstance npc, Player player, boolean isDualCertification, boolean isDualClassReset)
	{
		Integer adenaCost = isDualClassReset ? 0 : isDualCertification ? Config.ALT_GAME_RESET_DUALCERTIFICATION_COST : Config.ALT_GAME_RESET_CERTIFICATION_COST;
		AcquireType cancelAcquireType = isDualCertification ? AcquireType.DUAL_CERTIFICATION : AcquireType.CERTIFICATION;
		Integer DestroyItems = !isDualCertification ? CERTIFICATE : DUAL_CERTIFICATE; 
		if (player.getInventory().getAdena() < adenaCost)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		List <Integer> SkillIdToRemove = new ArrayList <Integer>();
		Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, cancelAcquireType);
		for (SkillLearn learn : skillLearnList)
		{
			Skill skill = player.getKnownSkill(learn.getId());
			if (skill != null)
			{
				SkillIdToRemove.add(skill.getId());
			}
		}
		if(SkillIdToRemove.size() <= 0 && !isDualClassReset)
		{
			Functions.show(PATH  + "certificate-nohaveskills.htm", player, npc);
			return;
		}
		player.removeCertSkill(SkillIdToRemove);
		player.getInventory().reduceAdena(adenaCost);
        player.getInventory().destroyItemByItemId(DestroyItems, player.getInventory().getCountOf(DestroyItems));
		for (SubClass subClass : player.getSubClassList().values())
		{
			if (isDualCertification)
			{
				if(subClass.isDouble())
				{
					subClass.setDualCertification(0);					
				}				
			}
			else
			{
				if(!subClass.isBase())
				{
					subClass.setCertification(0);
				}
			}
		}
		player.sendSkillList();
		CharacterSubclassDAO.getInstance().store(player);
		if(!isDualClassReset)
			Functions.show(new CustomMessage("scripts.services.SubclassSkills.SkillsDeleted", player), player);
	}	
	
	/**
	 * Method checkConditions.
	 * @param level int
	 * @param npc NpcInstance
	 * @param player Player
	 * @param first boolean
	 * @return boolean
	 */
	public static boolean checkConditions(int level, NpcInstance npc, Player player, boolean first)
	{
		String typeCertificate = new String();
		if (level < 85)
		{
			typeCertificate = "certificate";
			if (player.getLevel() < level)
			{
				Functions.show(PATH + typeCertificate +"-nolevel.htm", player, npc, "%level%", level);
				return false;
			}
			if (player.getActiveSubClass().isBase())
			{
				Functions.show(PATH + typeCertificate +"-nosub.htm", player, npc);
				return false;
			}
		}
		else
		{
			typeCertificate = "dualcertificate";
			int levelMain = 0, levelDual = 0;
			for(SubClass sc : player.getSubClassList().values())
			{
				if(sc.getType() == SubClassType.BASE_CLASS)
				{
					levelMain = sc.getLevel();				
				}
				else if (sc.getType() == SubClassType.DOUBLE_SUBCLASS)
				{
					levelDual = sc.getLevel();
				}
			}
			if(levelDual == 0)
			{
				Functions.show(PATH + typeCertificate + "-nodualoncharacter.htm", player, npc);
				return false;			
			}
			if(levelMain < level || levelDual < level)
			{
				Functions.show(PATH + typeCertificate + "-nolevel.htm", player, npc, "%level%", level);
				return false;
			}	
			if (!player.getActiveSubClass().isDouble())
			{
				Functions.show(PATH + typeCertificate + "-nodual.htm", player, npc);
				return false;
			}	
		}
		if (first)
		{
			return true;
		}
		return true;
	}
}
