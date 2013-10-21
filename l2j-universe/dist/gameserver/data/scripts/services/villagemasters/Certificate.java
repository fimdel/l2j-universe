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
package services.villagemasters;

import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.TrainerInstance;
import lineage2.gameserver.model.instances.VillageMasterInstance;
import lineage2.gameserver.scripts.Functions;

public class Certificate extends Functions
{
	public void CertificateOfCourage()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		NpcInstance npc = getNpc();
		
		// Grand Master Valfar
		if (npc.getNpcId() == 32146)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32146-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.kamael))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32146-done.htm");
			}
		}
		
		// Grand Master Rivan
		if (npc.getNpcId() == 32147)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32147-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.elf))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32147-done.htm");
			}
		}
		
		// High Perfect Took
		if (npc.getNpcId() == 32150)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32150-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.orc))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32150-done.htm");
			}
		}
		
		// High Priest Franco
		if (npc.getNpcId() == 32153)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32153-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.human))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32153-done.htm");
			}
		}
		
		// Head Blacksmith Moka
		if (npc.getNpcId() == 32157)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32157-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.dwarf))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32157-done.htm");
			}
		}
		
		// Grand Magister Devon
		if (npc.getNpcId() == 32160)
		{
			if ((!pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() < 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32160-no.htm");
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() == 2) && (pl.getRace() == Race.darkelf))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85555, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10331_StartofFate")) && (pl.getClassLevel() >= 2))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32160-done.htm");
			}
		}
	}
	
	public void CertificateOfJustice()
	{
		if ((getNpc() == null) || (getSelf() == null))
		{
			return;
		}
		Player pl = getSelf();
		NpcInstance npc = getNpc();
		
		// Grand Master Ellenia
		if (npc.getNpcId() == 30155)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((TrainerInstance) getNpc()).showChatWindow(pl, "trainer/30155-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.elf) && ((pl.getClassId() == ClassId.TEMPLE_KNIGHT) || (pl.getClassId() == ClassId.SWORDSINGER) || (pl.getClassId() == ClassId.PLAIN_WALKER) || (pl.getClassId() == ClassId.SILVER_RANGER)))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((TrainerInstance) getNpc()).showChatWindow(pl, "trainer/30155-done.htm");
			}
		}
		
		// Grand Master Rains
		if (npc.getNpcId() == 30288)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30288-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.human) && ((pl.getClassId() == ClassId.GLADIATOR) || (pl.getClassId() == ClassId.WARLORD) || (pl.getClassId() == ClassId.PALADIN) || (pl.getClassId() == ClassId.DARK_AVENGER) || (pl.getClassId() == ClassId.TREASURE_HUNTER) || (pl.getClassId() == ClassId.HAWKEYE)))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30288-done.htm");
			}
		}
		
		// High Priest Raymond
		if (npc.getNpcId() == 30289)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30289-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.human) && ((pl.getClassId() == ClassId.SORCERER) || (pl.getClassId() == ClassId.NECROMANCER) || (pl.getClassId() == ClassId.WARLOCK) || (pl.getClassId() == ClassId.BISHOP) || (pl.getClassId() == ClassId.PROPHET)))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30289-done.htm");
			}
		}
		
		// Grand Magister Esrandell
		if (npc.getNpcId() == 30158)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((TrainerInstance) getNpc()).showChatWindow(pl, "trainer/30158-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.elf) && ((pl.getClassId() == ClassId.SPELLSINGER) || (pl.getClassId() == ClassId.ELEMENTAL_SUMMONER) || (pl.getClassId() == ClassId.ELDER)))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((TrainerInstance) getNpc()).showChatWindow(pl, "trainer/30158-done.htm");
			}
		}
		
		// Grand Master Tobias
		if (npc.getNpcId() == 30297)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30297-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.darkelf))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30297-done.htm");
			}
		}
		
		// High Prefect Drikus
		if (npc.getNpcId() == 30505)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30505-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.orc))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30505-done.htm");
			}
		}
		
		// Grand Master Gershwin
		if (npc.getNpcId() == 32196)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32196-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.kamael))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/32196-done.htm");
			}
		}
		
		// Head Blacksmith Mendio
		if (npc.getNpcId() == 30504)
		{
			if ((!pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() < 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30504-no.htm");
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() == 3) && (pl.getRace() == Race.dwarf))
			{
				MultiSellHolder.getInstance().SeparateAndSend(85556, pl, 0);
			}
			else if ((pl.isQuestCompleted("_10360_CertificationOfFate")) && (pl.getClassLevel() >= 3))
			{
				((VillageMasterInstance) getNpc()).showChatWindow(pl, "villagemaster/30504-done.htm");
			}
		}
	}
}