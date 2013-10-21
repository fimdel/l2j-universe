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

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.VillageMasterInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Europa - Updated by Maleiva2013
 * @version $Revision: 1.0 $
 */
public class RequestAquireSkill extends L2GameClientPacket
{
	/**
	 * Field _type.
	 */
	private AcquireType _type;
	/**
	 * Field _subUnit. Field _level. Field _id.
	 */
	private int _id, _level, _subUnit;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_id = readD();
		_level = readD();
		_type = ArrayUtils.valid(AcquireType.VALUES, readD());
		if (_type == AcquireType.SUB_UNIT)
		{
			_subUnit = readD();
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if ((player == null) || (player.getTransformation() != 0) || (_type == null))
		{
			return;
		}
		NpcInstance trainer = player.getLastNpc();
		if (((trainer == null) || (player.getDistance(trainer.getX(), trainer.getY()) > Creature.INTERACTION_DISTANCE)) && !player.isGM() && (_type != AcquireType.NORMAL))
		{
			return;
		}
		Skill skill = SkillTable.getInstance().getInfo(_id, _level);
		if (skill == null)
		{
			return;
		}
		if (!SkillAcquireHolder.getInstance().isSkillPossible(player, skill, _type))
		{
			return;
		}
		SkillLearn skillLearn = SkillAcquireHolder.getInstance().getSkillLearn(player, _id, _level, _type);
		if (skillLearn == null)
		{
			return;
		}
		if (!checkSpellbook(player, skillLearn))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_NECESSARY_MATERIALS_OR_PREREQUISITES_TO_LEARN_THIS_SKILL);
			return;
		}
		switch (_type)
		{
			case NORMAL:
				learnSimpleNextLevel(player, skillLearn, skill, 0);
				/*
				 * if (trainer != null) { trainer.showSkillList(player); }
				 */
				break;
			case TRANSFORMATION:
				learnSimpleNextLevel(player, skillLearn, skill, 0);
				if (trainer != null)
				{
					trainer.showTransformationSkillList(player, AcquireType.TRANSFORMATION);
				}
				break;
			case COLLECTION:
				learnSimpleNextLevel(player, skillLearn, skill, 0);
				if (trainer != null)
				{
					NpcInstance.showCollectionSkillList(player);
				}
				break;
			case TRANSFER_CARDINAL:
			case TRANSFER_EVA_SAINTS:
			case TRANSFER_SHILLIEN_SAINTS:
				learnSimple(player, skillLearn, skill);
				if (trainer != null)
				{
					trainer.showTransferSkillList(player);
				}
				break;
			case FISHING:
				learnSimpleNextLevel(player, skillLearn, skill, 0);
				if (trainer != null)
				{
					NpcInstance.showFishingSkillList(player);
				}
				break;
			case CLAN:
				learnClanSkill(player, skillLearn, trainer, skill);
				break;
			case SUB_UNIT:
				learnSubUnitSkill(player, skillLearn, trainer, skill, _subUnit);
				break;
			case CERTIFICATION:
				if (!player.getActiveSubClass().isBase())
				{
					player.sendPacket(SystemMsg.THIS_SKILL_CANNOT_BE_LEARNED_WHILE_IN_THE_SUBCLASS_STATE);
					return;
				}
				learnSimpleNextLevel(player, skillLearn, skill, 1);
				if (trainer != null)
				{
					trainer.showTransformationSkillList(player, AcquireType.CERTIFICATION);
				}
				break;
			case DUAL_CERTIFICATION:
				if (!player.getActiveSubClass().isBase())
				{
					player.sendPacket(SystemMsg.THIS_SKILL_CANNOT_BE_LEARNED_WHILE_IN_THE_SUBCLASS_STATE);
					return;
				}
				learnSimpleNextLevel(player, skillLearn, skill, 2);
				if (trainer != null)
				{
					trainer.showTransformationSkillList(player, AcquireType.DUAL_CERTIFICATION);
				}
				break;
		}
	}
	
	/**
	 * Method learnSimpleNextLevel.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @param skill Skill
	 */
	private static void learnSimpleNextLevel(Player player, SkillLearn skillLearn, Skill skill, int typeLearn)
	{
		final int skillLevel = player.getSkillLevel(skillLearn.getId(), 0);
		if (skillLevel != (skillLearn.getLevel() - 1))
		{
			return;
		}
		switch(typeLearn)
		{
			case 1:
				learnCertification(player, skillLearn, skill, false);
				break;
			case 2:
				learnCertification(player, skillLearn, skill, true);
				break;
			default:
				learnSimple(player, skillLearn, skill);
				break;
		}
		
	}
	
	/**
	 * Method learnSimple.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @param skill Skill
	 */
	private static void learnSimple(Player player, SkillLearn skillLearn, Skill skill)
	{
		if (player.getSp() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_SP_TO_LEARN_THIS_SKILL);
			return;
		}
		if (skillLearn.getItemId() > 0)
		{
			if (!player.consumeItem(skillLearn.getItemId(), skillLearn.getItemCount()))
			{
				return;
			}
		}
		player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skill.getId(), skill.getLevel()));
		player.setSp(player.getSp() - skillLearn.getCost());
		player.addSkill(skill, true);
		player.sendUserInfo();
		player.updateStats();
		player.sendPacket(new SkillList(player, skill.getId()));
		player.sendPacket(new ExAcquirableSkillListByClass(player));
		RequestExEnchantSkill.updateSkillShortcuts(player, skill.getId(), skill.getLevel());
	}

	
	/**
	 * Method learnCertification.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @param skill Skill
	 * @param boolean isDual 
	 */
	private static void learnCertification(Player player, SkillLearn skillLearn, Skill skill, final boolean isDual)
	{
		if (skillLearn.getItemId() > 0)
		{
			if (!player.consumeItem(skillLearn.getItemId(), skillLearn.getItemCount()))
			{
				return;
			}
		}
		player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_SKILL).addSkillName(skill.getId(), skill.getLevel()));
		player.addCertSkill(skill, isDual);
		player.sendUserInfo();
		player.updateStats();
		player.sendPacket(new SkillList(player, skill.getId()));
		player.sendPacket(new ExAcquirableSkillListByClass(player));
		RequestExEnchantSkill.updateSkillShortcuts(player, skill.getId(), skill.getLevel());
	}
	
	/**
	 * Method learnClanSkill.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @param trainer NpcInstance
	 * @param skill Skill
	 */
	private static void learnClanSkill(Player player, SkillLearn skillLearn, NpcInstance trainer, Skill skill)
	{
		if (!(trainer instanceof VillageMasterInstance))
		{
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMsg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}
		Clan clan = player.getClan();
		final int skillLevel = clan.getSkillLevel(skillLearn.getId(), 0);
		if (skillLevel != (skillLearn.getLevel() - 1))
		{
			return;
		}
		if (clan.getReputationScore() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
			return;
		}
		if (skillLearn.getItemId() > 0)
		{
			if (!player.consumeItem(skillLearn.getItemId(), skillLearn.getItemCount()))
			{
				return;
			}
		}
		clan.incReputation(-skillLearn.getCost(), false, "AquireSkill: " + skillLearn.getId() + ", lvl " + skillLearn.getLevel());
		clan.addSkill(skill, true);
		clan.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skill));
		NpcInstance.showClanSkillList(player);
	}
	
	/**
	 * Method learnSubUnitSkill.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @param trainer NpcInstance
	 * @param skill Skill
	 * @param id int
	 */
	private static void learnSubUnitSkill(Player player, SkillLearn skillLearn, NpcInstance trainer, Skill skill, int id)
	{
		Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		SubUnit sub = clan.getSubUnit(id);
		if (sub == null)
		{
			return;
		}
		if ((player.getClanPrivileges() & Clan.CP_CL_TROOPS_FAME) != Clan.CP_CL_TROOPS_FAME)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		int lvl = sub.getSkillLevel(skillLearn.getId(), 0);
		if (lvl >= skillLearn.getLevel())
		{
			player.sendPacket(SystemMsg.THIS_SQUAD_SKILL_HAS_ALREADY_BEEN_ACQUIRED);
			return;
		}
		if (lvl != (skillLearn.getLevel() - 1))
		{
			player.sendPacket(SystemMsg.THE_PREVIOUS_LEVEL_SKILL_HAS_NOT_BEEN_LEARNED);
			return;
		}
		if (clan.getReputationScore() < skillLearn.getCost())
		{
			player.sendPacket(SystemMsg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
			return;
		}
		if (skillLearn.getItemId() > 0)
		{
			if (!player.consumeItem(skillLearn.getItemId(), skillLearn.getItemCount()))
			{
				return;
			}
		}
		clan.incReputation(-skillLearn.getCost(), false, "AquireSkill2: " + skillLearn.getId() + ", lvl " + skillLearn.getLevel());
		sub.addSkill(skill, true);
		player.sendPacket(new SystemMessage2(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skill));
		if (trainer != null)
		{
			NpcInstance.showSubUnitSkillList(player);
		}
	}
	
	/**
	 * Method checkSpellbook.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 * @return boolean
	 */
	private static boolean checkSpellbook(Player player, SkillLearn skillLearn)
	{
		if (Config.ALT_DISABLE_SPELLBOOKS)
		{
			return true;
		}
		if (skillLearn.getItemId() == 0)
		{
			return true;
		}
		if (skillLearn.isClicked())
		{
			return false;
		}
		return player.getInventory().getCountOf(skillLearn.getItemId()) >= skillLearn.getItemCount();
	}
}
