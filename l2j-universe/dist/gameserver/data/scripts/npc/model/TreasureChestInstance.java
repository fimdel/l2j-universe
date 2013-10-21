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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.ChestInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TreasureChestInstance extends ChestInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field TREASURE_BOMB_ID. (value is 4143)
	 */
	private static final int TREASURE_BOMB_ID = 4143;
	
	/**
	 * Constructor for TreasureChestInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TreasureChestInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method tryOpen.
	 * @param opener Player
	 * @param skill Skill
	 */
	@Override
	public void tryOpen(Player opener, Skill skill)
	{
		double chance = calcChance(opener, skill);
		if (Rnd.chance(chance))
		{
			getAggroList().addDamageHate(opener, 10000, 0);
			doDie(opener);
		}
		else
		{
			fakeOpen(opener);
		}
	}
	
	/**
	 * Method calcChance.
	 * @param opener Player
	 * @param skill Skill
	 * @return double
	 */
	public double calcChance(Player opener, Skill skill)
	{
		double chance = skill.getActivateRate();
		int npcLvl = getLevel();
		if (!isCommonTreasureChest())
		{
			double levelmod = (double) skill.getMagicLevel() - npcLvl;
			chance += levelmod * skill.getLevelModifier();
		}
		else
		{
			int openerLvl = opener.getLevel();
			int lvlDiff = Math.max(openerLvl - npcLvl, 0);
			if (((openerLvl <= 77) && (lvlDiff >= 6)) || ((openerLvl >= 78) && (lvlDiff >= 5)))
			{
				chance = 0;
			}
		}
		if (chance < 0)
		{
			chance = 1;
		}
		return chance;
	}
	
	/**
	 * Method fakeOpen.
	 * @param opener Creature
	 */
	private void fakeOpen(Creature opener)
	{
		Skill bomb = SkillTable.getInstance().getInfo(TREASURE_BOMB_ID, getBombLvl());
		if (bomb != null)
		{
			doCast(bomb, opener, false);
		}
		onDecay();
	}
	
	/**
	 * Method getBombLvl.
	 * @return int
	 */
	private int getBombLvl()
	{
		int npcLvl = getLevel();
		int lvl = 1;
		if (npcLvl >= 78)
		{
			lvl = 10;
		}
		else if (npcLvl >= 72)
		{
			lvl = 9;
		}
		else if (npcLvl >= 66)
		{
			lvl = 8;
		}
		else if (npcLvl >= 60)
		{
			lvl = 7;
		}
		else if (npcLvl >= 54)
		{
			lvl = 6;
		}
		else if (npcLvl >= 48)
		{
			lvl = 5;
		}
		else if (npcLvl >= 42)
		{
			lvl = 4;
		}
		else if (npcLvl >= 36)
		{
			lvl = 3;
		}
		else if (npcLvl >= 30)
		{
			lvl = 2;
		}
		return lvl;
	}
	
	/**
	 * Method isCommonTreasureChest.
	 * @return boolean
	 */
	private boolean isCommonTreasureChest()
	{
		int npcId = getNpcId();
		if ((npcId >= 18265) && (npcId <= 18286))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method onReduceCurrentHp.
	 * @param damage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 */
	@Override
	public void onReduceCurrentHp(final double damage, final Creature attacker, Skill skill, final boolean awake, final boolean standUp, boolean directHp)
	{
		if (!isCommonTreasureChest())
		{
			fakeOpen(attacker);
		}
	}
}
