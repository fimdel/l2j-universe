/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.base;

import l2p.gameserver.data.xml.holder.ClassDataHolder;
import l2p.gameserver.templates.player.ClassData;

/**
 * This class defines all classes (ex : HUMAN fighter, darkFighter...) that a player can chose.<BR><BR>
 * <p/>
 * Data :<BR><BR>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId for male or null if this class is the root</li>
 * <li>parent2 : The parent2 ClassId for female or null if parent2 like parent</li>
 * <li>level : The child level of this Class</li><BR><BR>
 */
public enum ClassId {
    HUMAN_FIGHTER(0, ClassType.FIGHTER, Race.human, null, ClassLevel.NONE, null),
    WARRIOR(1, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.FIRST, null),
    GLADIATOR(2, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.SECOND, ClassType2.WARRIOR),
    WARLORD(3, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.SECOND, ClassType2.WARRIOR),
    KNIGHT(4, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.FIRST, null),
    PALADIN(5, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT),
    DARK_AVENGER(6, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT),
    ROGUE(7, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.FIRST, null),
    TREASURE_HUNTER(8, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.SECOND, ClassType2.ROGUE),
    HAWKEYE(9, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.SECOND, ClassType2.ARCHER),

    HUMAN_MAGE(10, ClassType.MYSTIC, Race.human, null, ClassLevel.NONE, null),
    WIZARD(11, ClassType.MYSTIC, Race.human, HUMAN_MAGE, ClassLevel.FIRST, null),
    SORCERER(12, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.SECOND, ClassType2.WIZARD),
    NECROMANCER(13, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.SECOND, ClassType2.WIZARD),
    WARLOCK(14, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.SECOND, ClassType2.SUMMONER),
    CLERIC(15, ClassType.MYSTIC, Race.human, HUMAN_MAGE, ClassLevel.FIRST, null),
    BISHOP(16, ClassType.MYSTIC, Race.human, CLERIC, ClassLevel.SECOND, ClassType2.HEALER),
    PROPHET(17, ClassType.MYSTIC, Race.human, CLERIC, ClassLevel.SECOND, ClassType2.ENCHANTER),

    ELVEN_FIGHTER(18, ClassType.FIGHTER, Race.elf, null, ClassLevel.NONE, null),
    ELVEN_KNIGHT(19, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.FIRST, null),
    TEMPLE_KNIGHT(20, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT),
    SWORDSINGER(21, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.SECOND, ClassType2.ENCHANTER),
    ELVEN_SCOUT(22, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.FIRST, null),
    PLAIN_WALKER(23, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.SECOND, ClassType2.ROGUE),
    SILVER_RANGER(24, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.SECOND, ClassType2.ARCHER),

    ELVEN_MAGE(25, ClassType.MYSTIC, Race.elf, null, ClassLevel.NONE, null),
    ELVEN_WIZARD(26, ClassType.MYSTIC, Race.elf, ELVEN_MAGE, ClassLevel.FIRST, null),
    SPELLSINGER(27, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.SECOND, ClassType2.WIZARD),
    ELEMENTAL_SUMMONER(28, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.SECOND, ClassType2.SUMMONER),
    ORACLE(29, ClassType.MYSTIC, Race.elf, ELVEN_MAGE, ClassLevel.FIRST, null),
    ELDER(30, ClassType.MYSTIC, Race.elf, ORACLE, ClassLevel.SECOND, ClassType2.HEALER),

    DARK_FIGHTER(31, ClassType.FIGHTER, Race.darkelf, null, ClassLevel.NONE, null),
    PALUS_KNIGHT(32, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.FIRST, null),
    SHILLEN_KNIGHT(33, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT),
    BLADEDANCER(34, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.SECOND, ClassType2.ENCHANTER),
    ASSASIN(35, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.FIRST, null),
    ABYSS_WALKER(36, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.SECOND, ClassType2.ROGUE),
    PHANTOM_RANGER(37, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.SECOND, ClassType2.ARCHER),

    DARK_MAGE(38, ClassType.MYSTIC, Race.darkelf, null, ClassLevel.NONE, null),
    DARK_WIZARD(39, ClassType.MYSTIC, Race.darkelf, DARK_MAGE, ClassLevel.FIRST, null),
    SPELLHOWLER(40, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.SECOND, ClassType2.WIZARD),
    PHANTOM_SUMMONER(41, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.SECOND, ClassType2.SUMMONER),
    SHILLEN_ORACLE(42, ClassType.MYSTIC, Race.darkelf, DARK_MAGE, ClassLevel.FIRST, null),
    SHILLEN_ELDER(43, ClassType.MYSTIC, Race.darkelf, SHILLEN_ORACLE, ClassLevel.SECOND, ClassType2.HEALER),

    ORC_FIGHTER(44, ClassType.FIGHTER, Race.orc, null, ClassLevel.NONE, null),
    ORC_RAIDER(45, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.FIRST, null),
    DESTROYER(46, ClassType.FIGHTER, Race.orc, ORC_RAIDER, ClassLevel.SECOND, ClassType2.WARRIOR),
    ORC_MONK(47, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.FIRST, null),
    TYRANT(48, ClassType.FIGHTER, Race.orc, ORC_MONK, ClassLevel.SECOND, ClassType2.WARRIOR),

    ORC_MAGE(49, ClassType.MYSTIC, Race.orc, null, ClassLevel.NONE, null),
    ORC_SHAMAN(50, ClassType.MYSTIC, Race.orc, ORC_MAGE, ClassLevel.FIRST, null),
    OVERLORD(51, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.SECOND, ClassType2.ENCHANTER),
    WARCRYER(52, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.SECOND, ClassType2.ENCHANTER),

    DWARVEN_FIGHTER(53, ClassType.FIGHTER, Race.dwarf, null, ClassLevel.NONE, null),
    SCAVENGER(54, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.FIRST, null),
    BOUNTY_HUNTER(55, ClassType.FIGHTER, Race.dwarf, SCAVENGER, ClassLevel.SECOND, ClassType2.ROGUE),
    ARTISAN(56, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.FIRST, null),
    WARSMITH(57, ClassType.FIGHTER, Race.dwarf, ARTISAN, ClassLevel.SECOND, ClassType2.WARRIOR),

    DUMMY_ENTRY_1(58),
    DUMMY_ENTRY_2(59),
    DUMMY_ENTRY_3(60),
    DUMMY_ENTRY_4(61),
    DUMMY_ENTRY_5(62),
    DUMMY_ENTRY_6(63),
    DUMMY_ENTRY_7(64),
    DUMMY_ENTRY_8(65),
    DUMMY_ENTRY_9(66),
    DUMMY_ENTRY_10(67),
    DUMMY_ENTRY_11(68),
    DUMMY_ENTRY_12(69),
    DUMMY_ENTRY_13(70),
    DUMMY_ENTRY_14(71),
    DUMMY_ENTRY_15(72),
    DUMMY_ENTRY_16(73),
    DUMMY_ENTRY_17(74),
    DUMMY_ENTRY_18(75),
    DUMMY_ENTRY_19(76),
    DUMMY_ENTRY_20(77),
    DUMMY_ENTRY_21(78),
    DUMMY_ENTRY_22(79),
    DUMMY_ENTRY_23(80),
    DUMMY_ENTRY_24(81),
    DUMMY_ENTRY_25(82),
    DUMMY_ENTRY_26(83),
    DUMMY_ENTRY_27(84),
    DUMMY_ENTRY_28(85),
    DUMMY_ENTRY_29(86),
    DUMMY_ENTRY_30(87),

    DUELIST(88, ClassType.FIGHTER, Race.human, GLADIATOR, ClassLevel.THIRD, ClassType2.WARRIOR),
    DREADNOUGHT(89, ClassType.FIGHTER, Race.human, WARLORD, ClassLevel.THIRD, ClassType2.WARRIOR),
    PHOENIX_KNIGHT(90, ClassType.FIGHTER, Race.human, PALADIN, ClassLevel.THIRD, ClassType2.KNIGHT),
    HELL_KNIGHT(91, ClassType.FIGHTER, Race.human, DARK_AVENGER, ClassLevel.THIRD, ClassType2.KNIGHT),
    SAGITTARIUS(92, ClassType.FIGHTER, Race.human, HAWKEYE, ClassLevel.THIRD, ClassType2.ARCHER),
    ADVENTURER(93, ClassType.FIGHTER, Race.human, TREASURE_HUNTER, ClassLevel.THIRD, ClassType2.ROGUE),

    ARCHMAGE(94, ClassType.MYSTIC, Race.human, SORCERER, ClassLevel.THIRD, ClassType2.WIZARD),
    SOULTAKER(95, ClassType.MYSTIC, Race.human, NECROMANCER, ClassLevel.THIRD, ClassType2.WIZARD),
    ARCANA_LORD(96, ClassType.MYSTIC, Race.human, WARLOCK, ClassLevel.THIRD, ClassType2.SUMMONER),
    CARDINAL(97, ClassType.MYSTIC, Race.human, BISHOP, ClassLevel.THIRD, ClassType2.HEALER),
    HIEROPHANT(98, ClassType.MYSTIC, Race.human, PROPHET, ClassLevel.THIRD, ClassType2.ENCHANTER),

    EVAS_TEMPLAR(99, ClassType.FIGHTER, Race.elf, TEMPLE_KNIGHT, ClassLevel.THIRD, ClassType2.KNIGHT),
    SWORD_MUSE(100, ClassType.FIGHTER, Race.elf, SWORDSINGER, ClassLevel.THIRD, ClassType2.ENCHANTER),
    WIND_RIDER(101, ClassType.FIGHTER, Race.elf, PLAIN_WALKER, ClassLevel.THIRD, ClassType2.ROGUE),
    MOONLIGHT_SENTINEL(102, ClassType.FIGHTER, Race.elf, SILVER_RANGER, ClassLevel.THIRD, ClassType2.ARCHER),

    MYSTIC_MUSE(103, ClassType.MYSTIC, Race.elf, SPELLSINGER, ClassLevel.THIRD, ClassType2.WIZARD),
    ELEMENTAL_MASTER(104, ClassType.MYSTIC, Race.elf, ELEMENTAL_SUMMONER, ClassLevel.THIRD, ClassType2.SUMMONER),
    EVAS_SAINT(105, ClassType.MYSTIC, Race.elf, ELDER, ClassLevel.THIRD, ClassType2.HEALER),

    SHILLIEN_TEMPLAR(106, ClassType.FIGHTER, Race.darkelf, SHILLEN_KNIGHT, ClassLevel.THIRD, ClassType2.KNIGHT),
    SPECTRAL_DANCER(107, ClassType.FIGHTER, Race.darkelf, BLADEDANCER, ClassLevel.THIRD, ClassType2.ENCHANTER),
    GHOST_HUNTER(108, ClassType.FIGHTER, Race.darkelf, ABYSS_WALKER, ClassLevel.THIRD, ClassType2.ROGUE),
    GHOST_SENTINEL(109, ClassType.FIGHTER, Race.darkelf, PHANTOM_RANGER, ClassLevel.THIRD, ClassType2.ARCHER),

    STORM_SCREAMER(110, ClassType.MYSTIC, Race.darkelf, SPELLHOWLER, ClassLevel.THIRD, ClassType2.WIZARD),
    SPECTRAL_MASTER(111, ClassType.MYSTIC, Race.darkelf, PHANTOM_SUMMONER, ClassLevel.THIRD, ClassType2.SUMMONER),
    SHILLIEN_SAINT(112, ClassType.MYSTIC, Race.darkelf, SHILLEN_ELDER, ClassLevel.THIRD, ClassType2.HEALER),

    TITAN(113, ClassType.FIGHTER, Race.orc, DESTROYER, ClassLevel.THIRD, ClassType2.WARRIOR),
    GRAND_KHAVATARI(114, ClassType.FIGHTER, Race.orc, TYRANT, ClassLevel.THIRD, ClassType2.WARRIOR),

    DOMINATOR(115, ClassType.MYSTIC, Race.orc, OVERLORD, ClassLevel.THIRD, ClassType2.ENCHANTER),
    DOOMCRYER(116, ClassType.MYSTIC, Race.orc, WARCRYER, ClassLevel.THIRD, ClassType2.ENCHANTER),

    FORTUNE_SEEKER(117, ClassType.FIGHTER, Race.dwarf, BOUNTY_HUNTER, ClassLevel.THIRD, ClassType2.ROGUE),
    MAESTRO(118, ClassType.FIGHTER, Race.dwarf, WARSMITH, ClassLevel.THIRD, ClassType2.WARRIOR),

    DUMMY_ENTRY_31(119),
    DUMMY_ENTRY_32(120),
    DUMMY_ENTRY_33(121),
    DUMMY_ENTRY_34(122),

    KAMAEL_M_SOLDIER(123, ClassType.FIGHTER, Race.kamael, null, ClassLevel.NONE, null),
    KAMAEL_F_SOLDIER(124, ClassType.FIGHTER, Race.kamael, null, ClassLevel.NONE, null),
    TROOPER(125, ClassType.FIGHTER, Race.kamael, KAMAEL_M_SOLDIER, ClassLevel.FIRST, null),
    WARDER(126, ClassType.FIGHTER, Race.kamael, KAMAEL_F_SOLDIER, ClassLevel.FIRST, null),
    BERSERKER(127, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.SECOND, ClassType2.WARRIOR),
    M_SOUL_BREAKER(128, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.SECOND, ClassType2.WIZARD),
    F_SOUL_BREAKER(129, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.SECOND, ClassType2.WIZARD),
    ARBALESTER(130, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.SECOND, ClassType2.ARCHER),
    DOOMBRINGER(131, ClassType.FIGHTER, Race.kamael, BERSERKER, ClassLevel.THIRD, ClassType2.WARRIOR),
    M_SOUL_HOUND(132, ClassType.FIGHTER, Race.kamael, M_SOUL_BREAKER, ClassLevel.THIRD, ClassType2.WIZARD),
    F_SOUL_HOUND(133, ClassType.FIGHTER, Race.kamael, F_SOUL_BREAKER, ClassLevel.THIRD, ClassType2.WIZARD),
    TRICKSTER(134, ClassType.FIGHTER, Race.kamael, ARBALESTER, ClassLevel.THIRD, ClassType2.ARCHER),
    INSPECTOR(135, ClassType.FIGHTER, Race.kamael, TROOPER, WARDER, ClassLevel.SECOND, ClassType2.ENCHANTER),
    JUDICATOR(136, ClassType.FIGHTER, Race.kamael, INSPECTOR, ClassLevel.THIRD, ClassType2.ENCHANTER),

    DUMMY_ENTRY_35(137),
    DUMMY_ENTRY_36(138),


    // Awaking
    SIGEL_KNIGHT(139),

    TYR_WARRIOR(140),

    OTHELL_ROGUE(141),

    YR_ARCHER(142),

    FEOH_WIZARD(143),

    ISS_ENCHANTER(144),

    WYNN_SUMMONER(145),

    EOLH_HEALER(146),

    DUMMY_ENTRY_37(147),

    SigelKnight_PhoenixKnight(148, ClassType.FIGHTER, Race.human, PHOENIX_KNIGHT, ClassLevel.AWAKED, ClassType2.KNIGHT),
    SigelKnight_HellKnight(149, ClassType.FIGHTER, Race.human, HELL_KNIGHT, ClassLevel.AWAKED, ClassType2.KNIGHT),
    SigelKnight_EvaTemplar(150, ClassType.FIGHTER, Race.elf, EVAS_TEMPLAR, ClassLevel.AWAKED, ClassType2.KNIGHT),
    SigelKnight_ShillienTemplar(151, ClassType.FIGHTER, Race.darkelf, SHILLIEN_TEMPLAR, ClassLevel.AWAKED, ClassType2.KNIGHT),

    TyrrWarrior_Duelist(152, ClassType.FIGHTER, Race.human, DUELIST, ClassLevel.AWAKED, ClassType2.WARRIOR),
    TyrrWarrior_Dreadnought(153, ClassType.FIGHTER, Race.human, DREADNOUGHT, ClassLevel.AWAKED, ClassType2.WARRIOR),
    TyrrWarrior_Titan(154, ClassType.FIGHTER, Race.orc, TITAN, ClassLevel.AWAKED, ClassType2.WARRIOR),
    TyrrWarrior_GrandKhavatari(155, ClassType.FIGHTER, Race.orc, GRAND_KHAVATARI, ClassLevel.AWAKED, ClassType2.WARRIOR),
    TyrrWarrior_Maestro(156, ClassType.FIGHTER, Race.dwarf, MAESTRO, ClassLevel.AWAKED, ClassType2.WARRIOR),
    TyrrWarrior_Doombringer(157, ClassType.FIGHTER, Race.kamael, DOOMBRINGER, ClassLevel.AWAKED, ClassType2.WARRIOR),

    OthellRogue_Adventurer(158, ClassType.FIGHTER, Race.human, ADVENTURER, ClassLevel.AWAKED, ClassType2.ROGUE),
    OthellRogue_WindRider(159, ClassType.FIGHTER, Race.elf, WIND_RIDER, ClassLevel.AWAKED, ClassType2.ROGUE),
    OthellRogue_GhostHunter(160, ClassType.FIGHTER, Race.darkelf, GHOST_HUNTER, ClassLevel.AWAKED, ClassType2.ROGUE),
    OthellRogue_FortuneSeeker(161, ClassType.FIGHTER, Race.dwarf, FORTUNE_SEEKER, ClassLevel.AWAKED, ClassType2.ROGUE),

    YulArcher_Saggitarius(162, ClassType.FIGHTER, Race.human, SAGITTARIUS, ClassLevel.AWAKED, ClassType2.ARCHER),
    YulArcher_MoonlightSentinel(163, ClassType.FIGHTER, Race.elf, MOONLIGHT_SENTINEL, ClassLevel.AWAKED, ClassType2.ARCHER),
    YulArcher_GhostSentinel(164, ClassType.FIGHTER, Race.darkelf, GHOST_SENTINEL, ClassLevel.AWAKED, ClassType2.ARCHER),
    YulArcher_Trickster(165, ClassType.FIGHTER, Race.kamael, TRICKSTER, ClassLevel.AWAKED, ClassType2.ARCHER),

    FeohWizard_Archmage(166, ClassType.MYSTIC, Race.human, ARCHMAGE, ClassLevel.AWAKED, ClassType2.WIZARD),
    FeohWizard_Soultaker(167, ClassType.MYSTIC, Race.human, SOULTAKER, ClassLevel.AWAKED, ClassType2.WIZARD),
    FeohWizard_MysticMuse(168, ClassType.MYSTIC, Race.elf, MYSTIC_MUSE, ClassLevel.AWAKED, ClassType2.WIZARD),
    FeohWizard_StormScreamer(169, ClassType.MYSTIC, Race.darkelf, STORM_SCREAMER, ClassLevel.AWAKED, ClassType2.WIZARD),
    FeohWizard_SOUL_HOUND(170, ClassType.MYSTIC, Race.kamael, M_SOUL_HOUND, F_SOUL_HOUND, ClassLevel.AWAKED, ClassType2.WIZARD),

    IssEnchanter_Hierophant(171, ClassType.MYSTIC, Race.human, HIEROPHANT, ClassLevel.AWAKED, ClassType2.ENCHANTER),
    IssEnchanter_SwordMuse(172, ClassType.MYSTIC, Race.elf, SWORD_MUSE, ClassLevel.AWAKED, ClassType2.ENCHANTER),
    IssEnchanter_SpectralDancer(173, ClassType.MYSTIC, Race.darkelf, SPECTRAL_DANCER, ClassLevel.AWAKED, ClassType2.ENCHANTER),
    IssEnchanter_Dominator(174, ClassType.MYSTIC, Race.orc, DOMINATOR, ClassLevel.AWAKED, ClassType2.ENCHANTER),
    IssEnchanter_Doomcryer(175, ClassType.MYSTIC, Race.orc, DOOMCRYER, ClassLevel.AWAKED, ClassType2.ENCHANTER),

    WynnSummoner_ArcanaLord(176, ClassType.MYSTIC, Race.human, ARCANA_LORD, ClassLevel.AWAKED, ClassType2.SUMMONER),
    WynnSummoner_ElementalMaster(177, ClassType.MYSTIC, Race.elf, ELEMENTAL_MASTER, ClassLevel.AWAKED, ClassType2.SUMMONER),
    WynnSummoner_SpectralMaster(178, ClassType.MYSTIC, Race.darkelf, SPECTRAL_MASTER, ClassLevel.AWAKED, ClassType2.SUMMONER),

    AeoreHealer_Cardinal(179, ClassType.MYSTIC, Race.human, CARDINAL, ClassLevel.AWAKED, ClassType2.HEALER),
    AeoreHealer_EvaSaint(180, ClassType.MYSTIC, Race.elf, EVAS_SAINT, ClassLevel.AWAKED, ClassType2.HEALER),
    AeoreHealer_ShillenSaint(181, ClassType.MYSTIC, Race.darkelf, SHILLIEN_SAINT, ClassLevel.AWAKED, ClassType2.HEALER);


    public static final ClassId[] VALUES;
    private final int _id;
    public Race _race;
    private final ClassId _parent;
    private ClassId _parent2;
    private final ClassLevel _level;
    private final ClassType _type;
    private final ClassType2 _type2;
    private final boolean _isDummy;

    private ClassId(int clientId) {
        this(clientId, null, null, null, null, null, null, true);
    }

    private ClassId(int clientId, ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 type2) {
        this(clientId, classType, race, parent, null, level, type2, false);
    }

    private ClassId(int clientId, ClassType classType, Race race, ClassId parent, ClassId parent2, ClassLevel level, ClassType2 type2) {
        this(clientId, classType, race, parent, parent2, level, type2, false);
    }

    private ClassId(int clientId, ClassType classType, Race race, ClassId parent, ClassId parent2, ClassLevel level, ClassType2 type2, boolean isDummy) {
        _id = clientId;
        _type = classType;
        _race = race;
        _parent = parent;
        _parent2 = parent2;
        _level = level;
        _type2 = type2;
        _isDummy = isDummy;
    }

    public final int getId() {
        return _id;
    }

    public final Race getRace() {
        return _race;
    }

    public final boolean isOfRace(Race race) {
        return _race == race;
    }

    public final ClassLevel getClassLevel() {
        return _level;
    }

    public final boolean isOfLevel(ClassLevel level) {
        return _level == level;
    }

    public ClassType getType() {
        return _type;
    }

    public final boolean isOfType(ClassType type) {
        return _type == type;
    }

    public ClassType2 getType2() {
        return _type2;
    }

    public boolean isMage() {
        return _type.isMagician();
    }

    public final boolean childOf(ClassId cid) {
        if (isOfLevel(ClassLevel.AWAKED)) {
            return cid.getType2() == getType2();
        }
        if (_parent == null) {
            return false;
        }
        if (_parent == cid || _parent2 == cid) {
            return true;
        }
        return _parent.childOf(cid);
    }

    public final boolean equalsOrChildOf(ClassId cid) {
        return this == cid || childOf(cid);
    }

    public final ClassId getParent(int sex) {
        return (sex == 0) || (_parent2 == null) ? _parent : _parent2;
    }

    public ClassData getClassData() {
        return ClassDataHolder.getInstance().getClassData(getId());
    }

    public double getBaseCp(int level) {
        return getClassData().getLvlUpData(level).getCP();
    }

    public double getBaseHp(int level) {
        return getClassData().getLvlUpData(level).getHP();
    }

    public double getBaseMp(int level) {
        return getClassData().getLvlUpData(level).getMP();
    }

    static {
        VALUES = values();
    }

    public final int level() {
        if (_parent == null)
            return 0;

        return 1 + _parent.level();
    }

    public ClassId getAwakeParentId(int classId) {
        return getAwakeParentId(VALUES[classId]);
    }

    public ClassId getAwakeParentId(ClassId classId) {
        return this;
    }

    public final boolean isDummy() {
        return this._isDummy;
    }
}