/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.skills;

public enum AbnormalEffect {
    NULL,    // ("null", 0x0),
    BLEEDING,    // ("bleeding", 0x00000001),
    POISON,    // ("poison", 0x00000002),
    REDCIRCLE,    // ("redcircle", 0x00000004),
    ICE,    // ("ice", 0x00000008),
    AFFRAID,    // ("affraid", 0x00000010),
    CONFUSED,    // ("confused", 0x00000020),
    STUN,    // ("stun", 0x00000040),
    SLEEP,    // ("sleep", 0x00000080),
    MUTED,    // ("muted", 0x00000100),
    ROOT,    // ("root", 0x00000200),
    HOLD1,    // ("hold1", 0x00000400),
    HOLD2,    // ("hold2", 0x00000800), // эффект от Dance of Medusa
    UNK13,    // ("unk13", 0x00001000),
    BIGHEAD,    // ("bighead", 0x00002000),
    FLAME,    // ("flame", 0x00004000),
    UNK16,    // ("unk16", 0x00008000), // труп с таким абнормалом становится белым
    GROW,    // ("grow", 0x00010000),
    FLOATROOT,    // ("floatroot", 0x00020000), // поднимает в воздух и удерживает
    DANCESTUN,    // ("dancestun", 0x00040000), // танцует со звездочками над головой
    FIREROOTSTUN,    // ("firerootstun", 0x00080000), // красная аура у ног со звездочками над головой
    SHADOW,    // ("shadow", 0x00100000),
    IMPRISION1,    // ("imprison1", 0x00200000), // синяя аура на уровне пояса
    IMPRISION2,    // ("imprison2", 0x00400000), // синяя аура на уровне пояса
    MAGICCIRCLE,    // ("magiccircle", 0x00800000), // большой синий круг вокруг чара
    ICE2,    // ("ice2", 0x01000000), // небольшая ледяная аура, скорее всего DOT
    EARTHQUAKE,    // ("earthquake", 0x02000000), // землетрясение
    UNK27,    // ("unk27", 0x04000000),
    INVUL1,    // ("invul1", 0x08000000), // Ultimate Defence
    VITALITY,    // ("vitality", 0x10000000), // Vitality херб, красное пламя
    REALTARGET,    // ("realtarget", 0x20000000), // дебафф Real Target (знак над головой)
    DEATHMARK,    // ("deathmark", 0x40000000), // голубая морда над головой
    SOULSHOCK,    // ("soulshock", 0x80000000), // голубой череп над головой
    // special effects
    INVUL2,    // ("invul2", 0x00000001, true), // целестиал
    REDGLOW,    // ("redglow", 0x00000002, true), // непонятное красное облако
    REDGLOW2,    // ("redglow2", 0x00000004, true), // непонятное красное облако
    BAGUETTESWORD,    // ("baguettesword", 0x00000008, true), // пусто
    YELLOWAFRO,    // ("yellowafro", 0x00000010, true), // Большая круглая желтая прическа с воткнутой в волосы расческой
    PINKAFRO,    // ("pinkafro", 0x00000020, true), // Большая круглая розовая прическа с воткнутой в волосы расческой
    BLACKAFRO,    // ("blackafro", 0x00000040, true), // Большая круглая черная прическа с воткнутой в волосы расческой
    SUNK8,    // ("sunk8", 0x00000080, true), // пусто
    STIGMA,    // ("stigma", 0x00000100, true), // Stigma of Shillen
    SUNK10,    // ("sunk10", 0x00000200, true), // какой то рут
    FROZENPILLAR,    // ("frozenpillar", 0x00000400, true), // Frozen Pillar (Freya) (превращает в глыбу льда)
    SUNK12,    // ("sunk12", 0x00000800, true), // пусто
    VESPER_RED,    // ("vesper_red", 0x00001000, true), // Фейковый сет Веспера
    VESPER_NOBLE,    // ("vesper_noble", 0x00002000, true), // фейковый сет Веспера Белый
    SOA_REWPAWN,    // ("soa_respawn", 0x00004000, true), // Мобы на респе СОА появляются с таким абнормалом
    ARCANE_INVUL,    // ("arcane_invul", 0x00008000, true), // Щит Арканы
    SUNK17,    // ("sunk17", 0x00010000, true), // пусто
    SUNK18,    // ("sunk18", 0x00020000, true), // пусто
    SUNK19,    // ("sunk19", 0x00040000, true), // пусто
    // Из прошлого;)
    NEVITSYSTEM,    // ("nevitSystem", 0x00080000, true), // пусто
    SUNK21,    // ("sunk21", 0x00100000, true), // пусто
    SUNK22,    // ("sunk22", 0x00200000, true), // пусто
    SUNK23,    // ("sunk23", 0x00400000, true), // пусто
    SUNK24,    // ("sunk24", 0x00800000, true), // пусто
    SUNK25,    // ("sunk25", 0x01000000, true), // пусто
    SUNK26,    // ("sunk26", 0x02000000, true), // пусто
    SUNK27,    // ("sunk27", 0x04000000, true), // пусто
    SUNK28,    // ("sunk28", 0x08000000, true), // пусто
    SUNK29,    // ("sunk29", 0x10000000, true), // пусто
    SUNK30,    // ("sunk30", 0x20000000, true), // пусто
    SUNK31,    // ("sunk31", 0x40000000, true), // пусто
    SUNK32,    // ("sunk32", 0x80000000, true), // пусто
    // event effects
    AFROBAGUETTE1,    // ("afrobaguette1", 0x000001, false, true),
    AFROBAGUETTE2,    // ("afrobaguette2", 0x000002, false, true),
    AFROBAGUETTE3,    // ("afrobaguette3", 0x000004, false, true),
    EVASWRATH,    // ("evaswrath", 0x000008, false, true),
    HEADPHONE,    // ("headphone", 0x000010, false, true),
    VESPER1,    // ("vesper1", 0x000020, false, true),
    VESPER2,    // ("vesper2", 0x000040, false, true),
    VESPER3,    // ("vesper3", 0x000080, false, true),
    STIGMA_1,    // ("stigma_1", 0x000100, true),
    STAKATOROOT,    // ("stakatoroot", 0x000200, true),
    ICE_SHACKLE,    // ("ice_shackle", 0x000400, true),
    VESPERS,    // ("vespers", 0x000800, false,true),
    VESPERC,    // ("vesperc", 0x001000, false,true),
    VESPERD,    // ("vesperd", 0x002000, false,true),
    HUNTING_BONUS,    // ("hunting_bonus", 0x004000,true),
    ARCANE_INVUL_1,    // ("arcane_invul_1", 0x008000,true),
    AIR_SHACKLE,    // ("air_shackle", 0x010000, true),
    SUNK50,    // ("sunk50", 0x020000, true),
    KNOCKDOWN,    // ("knockdown", 0x040000, true),
    SUNK52,    // ("sunk52", 0x080000, true),
    SUNK53,    // ("sunk53", 0x100000, true),
    SUNK54,    // ("sunk54", 0x200000, true),
    SUNK55,    // ("sunk55", 0x400000, true),
    CELLED_CUBE,    // ("celled_cube", 0x800000, true),
    SPECIAL_AURA,    // ("special_aura", 0x1000000, true),
    SPECIAL_AURA_1,    // ("special_aura_1", 0x2000000, true),
    SPECIAL_AURA_2,    // ("special_aura_2", 0x4000000, true),
    SPECIAL_AURA_3,    // ("special_aura_3", 0x8000000, true);
    SPECIAL_AURA_4,
    CHANGEBODY,
    E_RADUGA,
    TALISMANPOWER1,
    TALISMANPOWER2,
    TALISMANPOWER3,
    TALISMANPOWER4,
    TALISMANPOWER5,

    KNOCKBACK,
    MP_SHIELD,
    DEPORT,
    BR_SOUL_AVATAR,
    AURA_BUFF,
    CHANGE_7ANNIVERSARY,
    BR_BEAM_SWORD_ONEHAND,
    BR_BEAM_SWORD_DUAL,
    BR_POWER_OF_EVA,
    GHOST_STUN,
    FLOATING_ROOT,
    SEIZURE1,
    SEIZURE2,
    ULTIMATE_DEFENCE,
    VP_UP,
    SHAKE,
    SPEED_DOWN,
    TIME_BOMB,
    AIR_BATTLE_SLOW,
    AIR_BATTLE_ROOT,
    AURA_DEBUFF_SELF,
    AURA_DEBUFF,
    HURRICANE_SELF,
    HURRICANE
}
