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
package lineage2.gameserver.model.base;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum ClassId
{
	/**
	 * Field HUMAN_FIGHTER.
	 */
	HUMAN_FIGHTER(0, ClassType.FIGHTER, Race.human, null, ClassLevel.First, null),
	/**
	 * Field WARRIOR.
	 */
	WARRIOR(1, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field GLADIATOR.
	 */
	GLADIATOR(2, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field WARLORD.
	 */
	WARLORD(3, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field KNIGHT.
	 */
	KNIGHT(4, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field PALADIN.
	 */
	PALADIN(5, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.Third, ClassType2.Knight),
	/**
	 * Field DARK_AVENGER.
	 */
	DARK_AVENGER(6, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.Third, ClassType2.Knight),
	/**
	 * Field ROGUE.
	 */
	ROGUE(7, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field TREASURE_HUNTER.
	 */
	TREASURE_HUNTER(8, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.Third, ClassType2.Rogue),
	/**
	 * Field HAWKEYE.
	 */
	HAWKEYE(9, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.Third, ClassType2.Archer),
	/**
	 * Field HUMAN_MAGE.
	 */
	HUMAN_MAGE(10, ClassType.MYSTIC, Race.human, null, ClassLevel.First, null),
	/**
	 * Field WIZARD.
	 */
	WIZARD(11, ClassType.MYSTIC, Race.human, HUMAN_MAGE, ClassLevel.Second, null),
	/**
	 * Field SORCERER.
	 */
	SORCERER(12, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field NECROMANCER.
	 */
	NECROMANCER(13, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field WARLOCK.
	 */
	WARLOCK(14, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Summoner),
	/**
	 * Field CLERIC.
	 */
	CLERIC(15, ClassType.PRIEST, Race.human, HUMAN_MAGE, ClassLevel.Second, null),
	/**
	 * Field BISHOP.
	 */
	BISHOP(16, ClassType.PRIEST, Race.human, CLERIC, ClassLevel.Third, ClassType2.Healer),
	/**
	 * Field PROPHET.
	 */
	PROPHET(17, ClassType.PRIEST, Race.human, CLERIC, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field ELVEN_FIGHTER.
	 */
	ELVEN_FIGHTER(18, ClassType.FIGHTER, Race.elf, null, ClassLevel.First, null),
	/**
	 * Field ELVEN_KNIGHT.
	 */
	ELVEN_KNIGHT(19, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field TEMPLE_KNIGHT.
	 */
	TEMPLE_KNIGHT(20, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.Third, ClassType2.Knight),
	/**
	 * Field SWORDSINGER.
	 */
	SWORDSINGER(21, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field ELVEN_SCOUT.
	 */
	ELVEN_SCOUT(22, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field PLAIN_WALKER.
	 */
	PLAIN_WALKER(23, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.Third, ClassType2.Rogue),
	/**
	 * Field SILVER_RANGER.
	 */
	SILVER_RANGER(24, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.Third, ClassType2.Archer),
	/**
	 * Field ELVEN_MAGE.
	 */
	ELVEN_MAGE(25, ClassType.MYSTIC, Race.elf, null, ClassLevel.First, null),
	/**
	 * Field ELVEN_WIZARD.
	 */
	ELVEN_WIZARD(26, ClassType.MYSTIC, Race.elf, ELVEN_MAGE, ClassLevel.Second, null),
	/**
	 * Field SPELLSINGER.
	 */
	SPELLSINGER(27, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field ELEMENTAL_SUMMONER.
	 */
	ELEMENTAL_SUMMONER(28, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.Third, ClassType2.Summoner),
	/**
	 * Field ORACLE.
	 */
	ORACLE(29, ClassType.PRIEST, Race.elf, ELVEN_MAGE, ClassLevel.Second, null),
	/**
	 * Field ELDER.
	 */
	ELDER(30, ClassType.PRIEST, Race.elf, ORACLE, ClassLevel.Third, ClassType2.Healer),
	/**
	 * Field DARK_FIGHTER.
	 */
	DARK_FIGHTER(31, ClassType.FIGHTER, Race.darkelf, null, ClassLevel.First, null),
	/**
	 * Field PALUS_KNIGHT.
	 */
	PALUS_KNIGHT(32, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field SHILLEN_KNIGHT.
	 */
	SHILLEN_KNIGHT(33, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.Third, ClassType2.Knight),
	/**
	 * Field BLADEDANCER.
	 */
	BLADEDANCER(34, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field ASSASIN.
	 */
	ASSASIN(35, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field ABYSS_WALKER.
	 */
	ABYSS_WALKER(36, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.Third, ClassType2.Rogue),
	/**
	 * Field PHANTOM_RANGER.
	 */
	PHANTOM_RANGER(37, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.Third, ClassType2.Archer),
	/**
	 * Field DARK_MAGE.
	 */
	DARK_MAGE(38, ClassType.MYSTIC, Race.darkelf, null, ClassLevel.First, null),
	/**
	 * Field DARK_WIZARD.
	 */
	DARK_WIZARD(39, ClassType.MYSTIC, Race.darkelf, DARK_MAGE, ClassLevel.Second, null),
	/**
	 * Field SPELLHOWLER.
	 */
	SPELLHOWLER(40, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field PHANTOM_SUMMONER.
	 */
	PHANTOM_SUMMONER(41, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.Third, ClassType2.Summoner),
	/**
	 * Field SHILLEN_ORACLE.
	 */
	SHILLEN_ORACLE(42, ClassType.PRIEST, Race.darkelf, DARK_MAGE, ClassLevel.Second, null),
	/**
	 * Field SHILLEN_ELDER.
	 */
	SHILLEN_ELDER(43, ClassType.PRIEST, Race.darkelf, SHILLEN_ORACLE, ClassLevel.Third, ClassType2.Healer),
	/**
	 * Field ORC_FIGHTER.
	 */
	ORC_FIGHTER(44, ClassType.FIGHTER, Race.orc, null, ClassLevel.First, null),
	/**
	 * Field ORC_RAIDER.
	 */
	ORC_RAIDER(45, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field DESTROYER.
	 */
	DESTROYER(46, ClassType.FIGHTER, Race.orc, ORC_RAIDER, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field ORC_MONK.
	 */
	ORC_MONK(47, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field TYRANT.
	 */
	TYRANT(48, ClassType.FIGHTER, Race.orc, ORC_MONK, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field ORC_MAGE.
	 */
	ORC_MAGE(49, ClassType.MYSTIC, Race.orc, null, ClassLevel.First, null),
	/**
	 * Field ORC_SHAMAN.
	 */
	ORC_SHAMAN(50, ClassType.MYSTIC, Race.orc, ORC_MAGE, ClassLevel.Second, null),
	/**
	 * Field OVERLORD.
	 */
	OVERLORD(51, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field WARCRYER.
	 */
	WARCRYER(52, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field DWARVEN_FIGHTER.
	 */
	DWARVEN_FIGHTER(53, ClassType.FIGHTER, Race.dwarf, null, ClassLevel.First, null),
	/**
	 * Field SCAVENGER.
	 */
	SCAVENGER(54, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field BOUNTY_HUNTER.
	 */
	BOUNTY_HUNTER(55, ClassType.FIGHTER, Race.dwarf, SCAVENGER, ClassLevel.Third, ClassType2.Rogue),
	/**
	 * Field ARTISAN.
	 */
	ARTISAN(56, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.Second, null),
	/**
	 * Field WARSMITH.
	 */
	WARSMITH(57, ClassType.FIGHTER, Race.dwarf, ARTISAN, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field DUMMY_ENTRY_1.
	 */
	DUMMY_ENTRY_1(58, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_2.
	 */
	DUMMY_ENTRY_2(59, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_3.
	 */
	DUMMY_ENTRY_3(60, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_4.
	 */
	DUMMY_ENTRY_4(61, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_5.
	 */
	DUMMY_ENTRY_5(62, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_6.
	 */
	DUMMY_ENTRY_6(63, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_7.
	 */
	DUMMY_ENTRY_7(64, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_8.
	 */
	DUMMY_ENTRY_8(65, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_9.
	 */
	DUMMY_ENTRY_9(66, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_10.
	 */
	DUMMY_ENTRY_10(67, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_11.
	 */
	DUMMY_ENTRY_11(68, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_12.
	 */
	DUMMY_ENTRY_12(69, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_13.
	 */
	DUMMY_ENTRY_13(70, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_14.
	 */
	DUMMY_ENTRY_14(71, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_15.
	 */
	DUMMY_ENTRY_15(72, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_16.
	 */
	DUMMY_ENTRY_16(73, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_17.
	 */
	DUMMY_ENTRY_17(74, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_18.
	 */
	DUMMY_ENTRY_18(75, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_19.
	 */
	DUMMY_ENTRY_19(76, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_20.
	 */
	DUMMY_ENTRY_20(77, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_21.
	 */
	DUMMY_ENTRY_21(78, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_22.
	 */
	DUMMY_ENTRY_22(79, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_23.
	 */
	DUMMY_ENTRY_23(80, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_24.
	 */
	DUMMY_ENTRY_24(81, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_25.
	 */
	DUMMY_ENTRY_25(82, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_26.
	 */
	DUMMY_ENTRY_26(83, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_27.
	 */
	DUMMY_ENTRY_27(84, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_28.
	 */
	DUMMY_ENTRY_28(85, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_29.
	 */
	DUMMY_ENTRY_29(86, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_30.
	 */
	DUMMY_ENTRY_30(87, null, null, null, null, null),
	/**
	 * Field DUELIST.
	 */
	DUELIST(88, ClassType.FIGHTER, Race.human, GLADIATOR, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field DREADNOUGHT.
	 */
	DREADNOUGHT(89, ClassType.FIGHTER, Race.human, WARLORD, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field PHOENIX_KNIGHT.
	 */
	PHOENIX_KNIGHT(90, ClassType.FIGHTER, Race.human, PALADIN, ClassLevel.Fourth, ClassType2.Knight),
	/**
	 * Field HELL_KNIGHT.
	 */
	HELL_KNIGHT(91, ClassType.FIGHTER, Race.human, DARK_AVENGER, ClassLevel.Fourth, ClassType2.Knight),
	/**
	 * Field SAGITTARIUS.
	 */
	SAGITTARIUS(92, ClassType.FIGHTER, Race.human, HAWKEYE, ClassLevel.Fourth, ClassType2.Archer),
	/**
	 * Field ADVENTURER.
	 */
	ADVENTURER(93, ClassType.FIGHTER, Race.human, TREASURE_HUNTER, ClassLevel.Fourth, ClassType2.Rogue),
	/**
	 * Field ARCHMAGE.
	 */
	ARCHMAGE(94, ClassType.MYSTIC, Race.human, SORCERER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field SOULTAKER.
	 */
	SOULTAKER(95, ClassType.MYSTIC, Race.human, NECROMANCER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field ARCANA_LORD.
	 */
	ARCANA_LORD(96, ClassType.MYSTIC, Race.human, WARLOCK, ClassLevel.Fourth, ClassType2.Summoner),
	/**
	 * Field CARDINAL.
	 */
	CARDINAL(97, ClassType.PRIEST, Race.human, BISHOP, ClassLevel.Fourth, ClassType2.Healer),
	/**
	 * Field HIEROPHANT.
	 */
	HIEROPHANT(98, ClassType.PRIEST, Race.human, PROPHET, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field EVAS_TEMPLAR.
	 */
	EVAS_TEMPLAR(99, ClassType.FIGHTER, Race.elf, TEMPLE_KNIGHT, ClassLevel.Fourth, ClassType2.Knight),
	/**
	 * Field SWORD_MUSE.
	 */
	SWORD_MUSE(100, ClassType.FIGHTER, Race.elf, SWORDSINGER, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field WIND_RIDER.
	 */
	WIND_RIDER(101, ClassType.FIGHTER, Race.elf, PLAIN_WALKER, ClassLevel.Fourth, ClassType2.Rogue),
	/**
	 * Field MOONLIGHT_SENTINEL.
	 */
	MOONLIGHT_SENTINEL(102, ClassType.FIGHTER, Race.elf, SILVER_RANGER, ClassLevel.Fourth, ClassType2.Archer),
	/**
	 * Field MYSTIC_MUSE.
	 */
	MYSTIC_MUSE(103, ClassType.MYSTIC, Race.elf, SPELLSINGER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field ELEMENTAL_MASTER.
	 */
	ELEMENTAL_MASTER(104, ClassType.MYSTIC, Race.elf, ELEMENTAL_SUMMONER, ClassLevel.Fourth, ClassType2.Summoner),
	/**
	 * Field EVAS_SAINT.
	 */
	EVAS_SAINT(105, ClassType.PRIEST, Race.elf, ELDER, ClassLevel.Fourth, ClassType2.Healer),
	/**
	 * Field SHILLIEN_TEMPLAR.
	 */
	SHILLIEN_TEMPLAR(106, ClassType.FIGHTER, Race.darkelf, SHILLEN_KNIGHT, ClassLevel.Fourth, ClassType2.Knight),
	/**
	 * Field SPECTRAL_DANCER.
	 */
	SPECTRAL_DANCER(107, ClassType.FIGHTER, Race.darkelf, BLADEDANCER, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field GHOST_HUNTER.
	 */
	GHOST_HUNTER(108, ClassType.FIGHTER, Race.darkelf, ABYSS_WALKER, ClassLevel.Fourth, ClassType2.Rogue),
	/**
	 * Field GHOST_SENTINEL.
	 */
	GHOST_SENTINEL(109, ClassType.FIGHTER, Race.darkelf, PHANTOM_RANGER, ClassLevel.Fourth, ClassType2.Archer),
	/**
	 * Field STORM_SCREAMER.
	 */
	STORM_SCREAMER(110, ClassType.MYSTIC, Race.darkelf, SPELLHOWLER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field SPECTRAL_MASTER.
	 */
	SPECTRAL_MASTER(111, ClassType.MYSTIC, Race.darkelf, PHANTOM_SUMMONER, ClassLevel.Fourth, ClassType2.Summoner),
	/**
	 * Field SHILLIEN_SAINT.
	 */
	SHILLIEN_SAINT(112, ClassType.PRIEST, Race.darkelf, SHILLEN_ELDER, ClassLevel.Fourth, ClassType2.Healer),
	/**
	 * Field TITAN.
	 */
	TITAN(113, ClassType.FIGHTER, Race.orc, DESTROYER, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field GRAND_KHAVATARI.
	 */
	GRAND_KHAVATARI(114, ClassType.FIGHTER, Race.orc, TYRANT, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field DOMINATOR.
	 */
	DOMINATOR(115, ClassType.MYSTIC, Race.orc, OVERLORD, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field DOOMCRYER.
	 */
	DOOMCRYER(116, ClassType.MYSTIC, Race.orc, WARCRYER, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field FORTUNE_SEEKER.
	 */
	FORTUNE_SEEKER(117, ClassType.FIGHTER, Race.dwarf, BOUNTY_HUNTER, ClassLevel.Fourth, ClassType2.Rogue),
	/**
	 * Field MAESTRO.
	 */
	MAESTRO(118, ClassType.FIGHTER, Race.dwarf, WARSMITH, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field DUMMY_ENTRY_31.
	 */
	DUMMY_ENTRY_31(119, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_32.
	 */
	DUMMY_ENTRY_32(120, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_33.
	 */
	DUMMY_ENTRY_33(121, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_34.
	 */
	DUMMY_ENTRY_34(122, null, null, null, null, null),
	/**
	 * Field KAMAEL_M_SOLDIER.
	 */
	KAMAEL_M_SOLDIER(123, ClassType.FIGHTER, Race.kamael, null, ClassLevel.First, null),
	/**
	 * Field KAMAEL_F_SOLDIER.
	 */
	KAMAEL_F_SOLDIER(124, ClassType.FIGHTER, Race.kamael, null, ClassLevel.First, null),
	/**
	 * Field TROOPER.
	 */
	TROOPER(125, ClassType.FIGHTER, Race.kamael, KAMAEL_M_SOLDIER, ClassLevel.Second, null),
	/**
	 * Field WARDER.
	 */
	WARDER(126, ClassType.FIGHTER, Race.kamael, KAMAEL_F_SOLDIER, ClassLevel.Second, null),
	/**
	 * Field BERSERKER.
	 */
	BERSERKER(127, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.Third, ClassType2.Warrior),
	/**
	 * Field M_SOUL_BREAKER.
	 */
	M_SOUL_BREAKER(128, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field F_SOUL_BREAKER.
	 */
	F_SOUL_BREAKER(129, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.Third, ClassType2.Wizard),
	/**
	 * Field ARBALESTER.
	 */
	ARBALESTER(130, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.Third, ClassType2.Archer),
	/**
	 * Field DOOMBRINGER.
	 */
	DOOMBRINGER(131, ClassType.FIGHTER, Race.kamael, BERSERKER, ClassLevel.Fourth, ClassType2.Warrior),
	/**
	 * Field M_SOUL_HOUND.
	 */
	M_SOUL_HOUND(132, ClassType.FIGHTER, Race.kamael, M_SOUL_BREAKER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field F_SOUL_HOUND.
	 */
	F_SOUL_HOUND(133, ClassType.FIGHTER, Race.kamael, F_SOUL_BREAKER, ClassLevel.Fourth, ClassType2.Wizard),
	/**
	 * Field TRICKSTER.
	 */
	TRICKSTER(134, ClassType.FIGHTER, Race.kamael, ARBALESTER, ClassLevel.Fourth, ClassType2.Archer),
	/**
	 * Field INSPECTOR.
	 */
	INSPECTOR(135, ClassType.FIGHTER, Race.kamael, TROOPER, WARDER, ClassLevel.Third, ClassType2.Enchanter),
	/**
	 * Field JUDICATOR.
	 */
	JUDICATOR(136, ClassType.FIGHTER, Race.kamael, INSPECTOR, ClassLevel.Fourth, ClassType2.Enchanter),
	/**
	 * Field DUMMY_ENTRY_35.
	 */
	DUMMY_ENTRY_35(137, null, null, null, null, null, null),
	/**
	 * Field DUMMY_ENTRY_36.
	 */
	DUMMY_ENTRY_36(138, null, null, null, null, null, null),
	/**
	 * Field sigelKnight.
	 */
	sigelKnight(139, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	/**
	 * Field tyrrWarrior.
	 */
	tyrrWarrior(140, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	/**
	 * Field othellRogue.
	 */
	othellRogue(141, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	/**
	 * Field yulArcher.
	 */
	yulArcher(142, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Archer),
	/**
	 * Field feohWizard.
	 */
	feohWizard(143, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	/**
	 * Field issEnchanter.
	 */
	issEnchanter(144, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	/**
	 * Field wynnSummoner.
	 */
	wynnSummoner(145, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Summoner),
	/**
	 * Field aeoreHealer.
	 */
	aeoreHealer(146, ClassType.PRIEST, null, null, null, ClassLevel.Awaking, ClassType2.Healer);
	/**
	 * Field VALUES.
	 */
	public static final ClassId[] VALUES;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _race.
	 */
	public Race _race;
	/**
	 * Field _parent.
	 */
	private final ClassId _parent;
	/**
	 * Field _parent2.
	 */
	private ClassId _parent2;
	/**
	 * Field _level.
	 */
	private final ClassLevel _level;
	/**
	 * Field _type.
	 */
	private final ClassType _type;
	/**
	 * Field _type2.
	 */
	private final ClassType2 _type2;
	
	/**
	 * Constructor for ClassId.
	 * @param id int
	 * @param classType ClassType
	 * @param race Race
	 * @param parent ClassId
	 * @param level ClassLevel
	 * @param classType2 ClassType2
	 */
	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 classType2)
	{
		this(id, classType, race, parent, null, level, classType2);
	}
	
	/**
	 * Constructor for ClassId.
	 * @param id int
	 * @param classType ClassType
	 * @param race Race
	 * @param parent ClassId
	 * @param parent2 ClassId
	 * @param level ClassLevel
	 * @param classType2 ClassType2
	 */
	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassId parent2, ClassLevel level, ClassType2 classType2)
	{
		_id = id;
		_type = classType;
		_race = race;
		_parent = parent;
		_parent2 = parent2;
		_level = level;
		_type2 = classType2;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * Method getRace.
	 * @return Race
	 */
	public final Race getRace()
	{
		return _race;
	}
	
	/**
	 * Method isOfRace.
	 * @param race Race
	 * @return boolean
	 */
	public final boolean isOfRace(Race race)
	{
		return _race == race;
	}
	
	/**
	 * Method getClassLevel.
	 * @return ClassLevel
	 */
	public final ClassLevel getClassLevel()
	{
		return _level;
	}
	
	/**
	 * Method isOfLevel.
	 * @param level ClassLevel
	 * @return boolean
	 */
	public final boolean isOfLevel(ClassLevel level)
	{
		return _level == level;
	}
	
	/**
	 * Method getType.
	 * @return ClassType
	 */
	public ClassType getType()
	{
		return _type;
	}
	
	/**
	 * Method isOfType.
	 * @param type ClassType
	 * @return boolean
	 */
	public final boolean isOfType(ClassType type)
	{
		return _type == type;
	}
	
	/**
	 * Method getType2.
	 * @return ClassType2
	 */
	public ClassType2 getType2()
	{
		return _type2;
	}
	
	/**
	 * Method isMage.
	 * @return boolean
	 */
	public boolean isMage()
	{
		return _type.isMagician();
	}
	
	/**
	 * Method childOf.
	 * @param cid ClassId
	 * @return boolean
	 */
	public final boolean childOf(ClassId cid)
	{
		if ((_parent == null) && (_id < 139))
		{
			return false;
		}
		if ((_parent == cid) || (_parent2 == cid))
		{
			return true;
		}
		if (_id > 138)
		{
			return _childClasses.get(this).contains(cid);
		}
		return _parent.childOf(cid);
	}
	
	/**
	 * Field _childClasses.
	 */
	public static final Map<ClassId, EnumSet<ClassId>> _childClasses = new HashMap<ClassId, EnumSet<ClassId>>()
	{
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		
		{
			put(ClassId.sigelKnight, EnumSet.of(ClassId.PHOENIX_KNIGHT, ClassId.HELL_KNIGHT, ClassId.EVAS_TEMPLAR, ClassId.SHILLIEN_TEMPLAR));
			put(ClassId.tyrrWarrior, EnumSet.of(ClassId.DUELIST, ClassId.DREADNOUGHT, ClassId.TITAN, ClassId.GRAND_KHAVATARI, ClassId.MAESTRO, ClassId.DOOMBRINGER));
			put(ClassId.othellRogue, EnumSet.of(ClassId.ADVENTURER, ClassId.WIND_RIDER, ClassId.GHOST_HUNTER, ClassId.FORTUNE_SEEKER));
			put(ClassId.yulArcher, EnumSet.of(ClassId.SAGITTARIUS, ClassId.MOONLIGHT_SENTINEL, ClassId.GHOST_SENTINEL, ClassId.TRICKSTER));
			put(ClassId.feohWizard, EnumSet.of(ClassId.ARCHMAGE, ClassId.SOULTAKER, ClassId.MYSTIC_MUSE, ClassId.STORM_SCREAMER, ClassId.M_SOUL_HOUND, ClassId.F_SOUL_HOUND));
			put(ClassId.issEnchanter, EnumSet.of(ClassId.HIEROPHANT, ClassId.SWORD_MUSE, ClassId.SPECTRAL_DANCER, ClassId.DOMINATOR, ClassId.DOOMCRYER, ClassId.JUDICATOR));
			put(ClassId.wynnSummoner, EnumSet.of(ClassId.ARCANA_LORD, ClassId.ELEMENTAL_MASTER, ClassId.SPECTRAL_MASTER));
			put(ClassId.aeoreHealer, EnumSet.of(ClassId.CARDINAL, ClassId.EVAS_SAINT, ClassId.SHILLIEN_SAINT));
		}
	};
	
	/**
	 * Method equalsOrChildOf.
	 * @param cid ClassId
	 * @return boolean
	 */
	public final boolean equalsOrChildOf(ClassId cid)
	{
		return (this == cid) || (childOf(cid));
	}
	
	/**
	 * Method getParent.
	 * @param sex int
	 * @return ClassId
	 */
	public final ClassId getParent(int sex)
	{
		return (sex == 0) || (_parent2 == null) ? _parent : _parent2;
	}
	static
	{
		VALUES = values();
	}


	/**
	 * Method getParent.
	 * @param sex int
	 * @return ClassId
	 */
	public final Integer getParentId()
	{
		return _parent.getId();
	}
	
	/**
	 * Method level.
	 * @return int
	 */
	public final int level()
	{
		if (_parent == null)
		{
			return 0;
		}
		return 1 + _parent.level();
	}
}
