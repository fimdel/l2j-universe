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

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.InvisibleType;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminEffects implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_invis.
		 */
		admin_invis,
		/**
		 * Field admin_vis.
		 */
		admin_vis,
		/**
		 * Field admin_offline_vis.
		 */
		admin_offline_vis,
		/**
		 * Field admin_offline_invis.
		 */
		admin_offline_invis,
		/**
		 * Field admin_earthquake.
		 */
		admin_earthquake,
		/**
		 * Field admin_block.
		 */
		admin_block,
		/**
		 * Field admin_unblock.
		 */
		admin_unblock,
		/**
		 * Field admin_changename.
		 */
		admin_changename,
		/**
		 * Field admin_gmspeed.
		 */
		admin_gmspeed,
		/**
		 * Field admin_invul.
		 */
		admin_invul,
		/**
		 * Field admin_setinvul.
		 */
		admin_setinvul,
		/**
		 * Field admin_getinvul.
		 */
		admin_getinvul,
		/**
		 * Field admin_social.
		 */
		admin_social,
		/**
		 * Field admin_abnormal.
		 */
		admin_abnormal,
		/**
		 * Field admin_abnormal.
		 */
		admin_effect,
		/**
		 * Field admin_transform.
		 */
		admin_transform,
		/**
		 * Field admin_showmovie.
		 */
		admin_showmovie
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
		if (!activeChar.getPlayerAccess().GodMode)
		{
			return false;
		}
		int val;
		AbnormalEffect ae = AbnormalEffect.NULL;
		String Skill = new String();
		GameObject target = activeChar.getTarget();
		switch (command)
		{
			case admin_invis:
			case admin_vis:
				if (activeChar.isInvisible())
				{
					activeChar.setInvisibleType(InvisibleType.NONE);
					activeChar.broadcastCharInfo();
					for (Summon summon : activeChar.getSummonList())
					{
						summon.broadcastCharInfo();
					}
				}
				else
				{
					activeChar.setInvisibleType(InvisibleType.NORMAL);
					activeChar.sendUserInfo();
					World.removeObjectFromPlayers(activeChar);
				}
				break;
			case admin_gmspeed:
				if (wordList.length < 2)
				{
					val = 0;
				}
				else
				{
					try
					{
						val = Integer.parseInt(wordList[1]);
					}
					catch (Exception e)
					{
						activeChar.sendMessage("USAGE: //gmspeed value=[0..4]");
						return false;
					}
				}
				List<Effect> superhaste = activeChar.getEffectList().getEffectsBySkillId(7029);
				int sh_level = superhaste == null ? 0 : superhaste.isEmpty() ? 0 : superhaste.get(0).getSkill().getLevel();
				if (val == 0)
				{
					if (sh_level != 0)
					{
						activeChar.doCast(SkillTable.getInstance().getInfo(7029, sh_level), activeChar, true);
					}
					activeChar.unsetVar("gm_gmspeed");
				}
				else if ((val >= 1) && (val <= 4))
				{
					if (Config.SAVE_GM_EFFECTS)
					{
						activeChar.setVar("gm_gmspeed", String.valueOf(val), -1);
					}
					if (val != sh_level)
					{
						if (sh_level != 0)
						{
							activeChar.doCast(SkillTable.getInstance().getInfo(7029, sh_level), activeChar, true);
						}
						activeChar.doCast(SkillTable.getInstance().getInfo(7029, val), activeChar, true);
					}
				}
				else
				{
					activeChar.sendMessage("USAGE: //gmspeed value=[0..4]");
				}
				break;
			case admin_invul:
				handleInvul(activeChar, activeChar);
				if (activeChar.isInvul())
				{
					if (Config.SAVE_GM_EFFECTS)
					{
						activeChar.setVar("gm_invul", "true", -1);
					}
				}
				else
				{
					activeChar.unsetVar("gm_invul");
				}
				break;
			default:
				break;
		}
		if (!activeChar.isGM())
		{
			return false;
		}
		switch (command)
		{
			case admin_offline_vis:
				for (Player player : GameObjectsStorage.getAllPlayers())
				{
					if ((player != null) && player.isInOfflineMode())
					{
						player.setInvisibleType(InvisibleType.NONE);
						player.decayMe();
						player.spawnMe();
					}
				}
				break;
			case admin_offline_invis:
				for (Player player : GameObjectsStorage.getAllPlayers())
				{
					if ((player != null) && player.isInOfflineMode())
					{
						player.setInvisibleType(InvisibleType.NORMAL);
						player.decayMe();
					}
				}
				break;
			case admin_earthquake:
				try
				{
					int intensity = Integer.parseInt(wordList[1]);
					int duration = Integer.parseInt(wordList[2]);
					activeChar.broadcastPacket(new Earthquake(activeChar.getLoc(), intensity, duration));
				}
				catch (Exception e)
				{
					activeChar.sendMessage("USAGE: //earthquake intensity duration");
					return false;
				}
				break;
			case admin_block:
				if ((target == null) || !target.isCreature())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				if (((Creature) target).isBlocked())
				{
					return false;
				}
				((Creature) target).abortAttack(true, false);
				((Creature) target).abortCast(true, false);
				((Creature) target).block();
				activeChar.sendMessage("Target blocked.");
				break;
			case admin_unblock:
				if ((target == null) || !target.isCreature())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				if (!((Creature) target).isBlocked())
				{
					return false;
				}
				((Creature) target).unblock();
				activeChar.sendMessage("Target unblocked.");
				break;
			case admin_changename:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //changename newName");
					return false;
				}
				if (target == null)
				{
					target = activeChar;
				}
				if (!target.isCreature())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				String oldName = ((Creature) target).getName();
				String newName = Util.joinStrings(" ", wordList, 1);
				((Creature) target).setName(newName);
				((Creature) target).broadcastCharInfo();
				activeChar.sendMessage("Changed name from " + oldName + " to " + newName + ".");
				break;
			case admin_setinvul:
				if ((target == null) || !target.isPlayer())
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
					return false;
				}
				handleInvul(activeChar, (Player) target);
				break;
			case admin_getinvul:
				if ((target != null) && target.isCreature())
				{
					activeChar.sendMessage("Target " + target.getName() + "(object ID: " + target.getObjectId() + ") is " + (!((Creature) target).isInvul() ? "NOT " : "") + "invul");
				}
				break;
			case admin_social:
				if (wordList.length < 2)
				{
					val = Rnd.get(1, 7);
				}
				else
				{
					try
					{
						val = Integer.parseInt(wordList[1]);
					}
					catch (NumberFormatException nfe)
					{
						activeChar.sendMessage("USAGE: //social value");
						return false;
					}
				}
				if ((target == null) || (target == activeChar))
				{
					activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), val));
				}
				else if (target.isCreature())
				{
					((Creature) target).broadcastPacket(new SocialAction(target.getObjectId(), val));
				}
				break;
			case admin_abnormal:
				try
				{
					if (wordList.length > 1)
					{
						ae = AbnormalEffect.getByName(wordList[1]);
					}
				}
				catch (Exception e)
				{
					activeChar.sendMessage("USAGE: //abnormal name");
					activeChar.sendMessage("//abnormal - Clears all abnormal effects");
					return false;
				}
				Creature effectTarget = target == null ? activeChar : (Creature) target;
				if (ae == AbnormalEffect.NULL)
				{
					effectTarget.startAbnormalEffect(AbnormalEffect.NULL);
					effectTarget.sendMessage("Abnormal effects clearned by admin.");
					if (effectTarget != activeChar)
					{
						effectTarget.sendMessage("Abnormal effects clearned.");
					}
				}
				else
				{
					effectTarget.startAbnormalEffect(ae);
					effectTarget.sendMessage("Admin added abnormal effect: " + ae.getName());
					if (effectTarget != activeChar)
					{
						effectTarget.sendMessage("Added abnormal effect: " + ae.getName());
					}
				}
				break;
			case admin_effect:
				try
				{
					if (wordList.length > 1 && wordList[1] != null)
					{
						Skill = wordList[1];
					}
				}
				catch (Exception e)
				{
					activeChar.sendMessage("USAGE: //effect skillId <optional> skillLevel hittime");
					return false;
				}
				String level = "1", hittime = "1";
				if (wordList.length > 2)
					level = wordList[2];
				if (wordList.length > 2)
					hittime = wordList[2];
				if (target == null)
					target = activeChar;
				if (!(target instanceof Creature))
					activeChar.sendPacket(Msg.INVALID_TARGET);
				else
				{
					((Creature)target).broadcastPacket(new MagicSkillUse(((Creature)target), activeChar, Integer.valueOf(Skill), Integer.valueOf(level), Integer.valueOf(hittime), 0));
					activeChar.sendMessage(((Creature)target).getName() + " performs MSU " + Skill + "/" + level + " by your request.");
				}
				break;
			case admin_transform:
				try
				{
					val = Integer.parseInt(wordList[1]);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("USAGE: //transform transform_id");
					return false;
				}
				activeChar.setTransformation(val);
				break;
			case admin_showmovie:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //showmovie id");
					return false;
				}
				int id;
				try
				{
					id = Integer.parseInt(wordList[1]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("You must specify id");
					return false;
				}
				activeChar.showQuestMovie(id);
				break;
			default:
				break;
		}
		return true;
	}
	
	/**
	 * Method handleInvul.
	 * @param activeChar Player
	 * @param target Player
	 */
	private void handleInvul(Player activeChar, Player target)
	{
		if (target.isInvul())
		{
			target.setIsInvul(false);
			target.stopAbnormalEffect(AbnormalEffect.S_INVINCIBLE);
			for (Summon summon : activeChar.getSummonList())
			{
				summon.setIsInvul(false);
				summon.stopAbnormalEffect(AbnormalEffect.S_INVINCIBLE);
			}
			activeChar.sendMessage(target.getName() + " is now mortal.");
		}
		else
		{
			target.setIsInvul(true);
			target.startAbnormalEffect(AbnormalEffect.S_INVINCIBLE);
			for (Summon summon : activeChar.getSummonList())
			{
				summon.setIsInvul(true);
				summon.startAbnormalEffect(AbnormalEffect.S_INVINCIBLE);
			}
			activeChar.sendMessage(target.getName() + " is now immortal.");
		}
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
