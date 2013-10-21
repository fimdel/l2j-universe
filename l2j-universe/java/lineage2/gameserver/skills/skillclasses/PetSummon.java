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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.SummonType;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetSummon extends Skill
{
	/**
	 * Constructor for PetSummon.
	 * @param set StatsSet
	 */
	public PetSummon(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = activeChar.getPlayer();
		if (player == null)
		{
			return false;
		}
		if (player.getPetControlItem() == null)
		{
			return false;
		}
		int npcId = PetDataTable.getSummonId(player.getPetControlItem());
		if (npcId == 0)
		{
			return false;
		}
		if (player.isInCombat() || player.getSummonList().isInCombat())
		{
			player.sendPacket(Msg.YOU_CANNOT_SUMMON_DURING_COMBAT);
			return false;
		}
		if (player.isProcessingRequest())
		{
			player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		if (player.isMounted() || !player.getSummonList().canSummon(SummonType.PET, 0))
		{
			player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
			return false;
		}
		if (player.isInBoat())
		{
			player.sendPacket(Msg.YOU_MAY_NOT_CALL_FORTH_A_PET_OR_SUMMONED_CREATURE_FROM_THIS_LOCATION);
			return false;
		}
		if (player.isInFlyingTransform())
		{
			return false;
		}
		if (player.isInOlympiadMode())
		{
			player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		if (player.isCursedWeaponEquipped())
		{
			player.sendPacket(Msg.YOU_MAY_NOT_USE_MULTIPLE_PETS_OR_SERVITORS_AT_THE_SAME_TIME);
			return false;
		}
		for (GameObject o : World.getAroundObjects(player, 120, 200))
		{
			if (o.isDoor())
			{
				player.sendPacket(Msg.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
				return false;
			}
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param caster Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature caster, List<Creature> targets)
	{
		Player activeChar = caster.getPlayer();
		ItemInstance controlItem = activeChar.getPetControlItem();
		if (controlItem == null)
		{
			return;
		}
		int npcId = PetDataTable.getSummonId(controlItem);
		if (npcId == 0)
		{
			return;
		}
		NpcTemplate petTemplate = NpcHolder.getInstance().getTemplate(npcId);
		if (petTemplate == null)
		{
			return;
		}
		PetInstance pet = PetInstance.restore(controlItem, petTemplate, activeChar);
		if (pet == null)
		{
			return;
		}
		if (!pet.isRespawned())
		{
			pet.setCurrentHp(pet.getMaxHp(), false);
			pet.setCurrentMp(pet.getMaxMp());
			pet.setCurrentFed(pet.getMaxFed());
			pet.updateControlItem();
			pet.store();
		}
		pet.getInventory().restore();
		activeChar.getSummonList().addSummon(pet);
		pet.setHeading(activeChar.getHeading());
		pet.setReflection(activeChar.getReflection());
		pet.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
		pet.setRunning();
		pet.setFollowMode(true);
		if (activeChar.isInOlympiadMode())
		{
			pet.getEffectList().stopAllEffects();
		}
		if (isSSPossible())
		{
			caster.unChargeShots(isMagic());
		}
	}
}
