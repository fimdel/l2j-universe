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
package lineage2.gameserver.data.xml;

import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.data.xml.parser.AirshipDockParser;
import lineage2.gameserver.data.xml.parser.ArmorItemParser;
import lineage2.gameserver.data.xml.parser.ArmorSetsParser;
import lineage2.gameserver.data.xml.parser.CubicParser;
import lineage2.gameserver.data.xml.parser.DomainParser;
import lineage2.gameserver.data.xml.parser.DoorParser;
import lineage2.gameserver.data.xml.parser.DropListDataParser;
import lineage2.gameserver.data.xml.parser.EnchantItemParser;
import lineage2.gameserver.data.xml.parser.EtcItemParser;
import lineage2.gameserver.data.xml.parser.EventParser;
import lineage2.gameserver.data.xml.parser.FishDataParser;
import lineage2.gameserver.data.xml.parser.HennaParser;
import lineage2.gameserver.data.xml.parser.InstantZoneParser;
import lineage2.gameserver.data.xml.parser.JumpTracksParser;
import lineage2.gameserver.data.xml.parser.LevelBonusParser;
import lineage2.gameserver.data.xml.parser.NpcParser;
import lineage2.gameserver.data.xml.parser.OptionDataParser;
import lineage2.gameserver.data.xml.parser.PetitionGroupParser;
import lineage2.gameserver.data.xml.parser.PlayerTemplateParser;
import lineage2.gameserver.data.xml.parser.RecipeParser;
import lineage2.gameserver.data.xml.parser.ResidenceParser;
import lineage2.gameserver.data.xml.parser.RestartPointParser;
import lineage2.gameserver.data.xml.parser.RestorationInfoParser;
import lineage2.gameserver.data.xml.parser.ShuttleTemplateParser;
import lineage2.gameserver.data.xml.parser.SkillAcquireParser;
import lineage2.gameserver.data.xml.parser.SoulCrystalParser;
import lineage2.gameserver.data.xml.parser.SpawnParser;
import lineage2.gameserver.data.xml.parser.StaticObjectParser;
import lineage2.gameserver.data.xml.parser.StatuesSpawnParser;
import lineage2.gameserver.data.xml.parser.WalkerRoutesParser;
import lineage2.gameserver.data.xml.parser.WeaponItemParser;
import lineage2.gameserver.data.xml.parser.ZoneParser;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.tables.CustomSpawnTable;
import lineage2.gameserver.tables.FishTable;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Parsers
{
	/**
	 * Method parseAll.
	 */
	public static void parseAll()
	{
		HtmCache.getInstance().reload();
		StringHolder.getInstance().load();
		SkillTable.getInstance().load();
		RestorationInfoParser.getInstance().load();
		OptionDataParser.getInstance().load();
		EtcItemParser.getInstance().load();
		WeaponItemParser.getInstance().load();
		ArmorItemParser.getInstance().load();
		EnchantItemParser.getInstance().load();
		NpcParser.getInstance().load();
		DropListDataParser.getInstance().load();
		DomainParser.getInstance().load();
		RestartPointParser.getInstance().load();
		StaticObjectParser.getInstance().load();
		DoorParser.getInstance().load();
		CustomSpawnTable.getInstance();
		ZoneParser.getInstance().load();
		SpawnParser.getInstance().load();
		InstantZoneParser.getInstance().load();
		WalkerRoutesParser.getInstance().load();
		RecipeParser.getInstance().load();
		ReflectionManager.getInstance();
		AirshipDockParser.getInstance().load();
		SkillAcquireParser.getInstance().load();
		ResidenceParser.getInstance().load();
		ShuttleTemplateParser.getInstance().load();
		EventParser.getInstance().load();
		CubicParser.getInstance().load();
		BuyListHolder.getInstance();
		RecipeHolder.getInstance();
		MultiSellHolder.getInstance();
		ProductHolder.getInstance();
		HennaParser.getInstance().load();
		JumpTracksParser.getInstance().load();
		SoulCrystalParser.getInstance().load();
		ArmorSetsParser.getInstance().load();
		FishDataParser.getInstance().load();
		FishTable.getInstance().reload();
		PetitionGroupParser.getInstance().load();
		PlayerTemplateParser.getInstance().load();
		LevelBonusParser.getInstance().load();

		StatuesSpawnParser.getInstance().load();
	}
}
