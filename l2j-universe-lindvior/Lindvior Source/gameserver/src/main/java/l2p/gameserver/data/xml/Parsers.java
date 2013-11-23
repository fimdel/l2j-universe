/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data.xml;

import l2p.gameserver.data.StringHolder;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.data.xml.holder.BuyListHolder;
import l2p.gameserver.data.xml.holder.MultiSellHolder;
import l2p.gameserver.data.xml.holder.ProductHolder;
import l2p.gameserver.data.xml.holder.RecipeHolder;
import l2p.gameserver.data.xml.parser.*;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.tables.SpawnTable;

/**
 * @author VISTALL
 * @date 20:55/30.11.2010
 */
public abstract class Parsers {
    public static void parseAll() {
        // HtmCache
        HtmCache.getInstance().reload();
        // String
        StringHolder.getInstance().load();
        // Skill
        SkillTable.getInstance().load(); // - SkillParser.getInstance();
        // OptionData
        OptionDataParser.getInstance().load();
        // EtcItem
        EtcItemParser.getInstance().load();
        // WeaponItem
        WeaponItemParser.getInstance().load();
        // EnchantItem
        EnchantItemParser.getInstance().load();
        // ArmorItem
        ArmorItemParser.getInstance().load();
        // Npc
        NpcParser.getInstance().load();
        // Domain
        DomainParser.getInstance().load();
        // RestartPoint
        RestartPointParser.getInstance().load();
        // Static Object
        StaticObjectParser.getInstance().load();
        // Door
        DoorParser.getInstance().load();
        // Zone
        ZoneParser.getInstance().load();
        // Spawn
        SpawnTable.getInstance();
        SpawnParser.getInstance().load();
        // InstantZone
        InstantZoneParser.getInstance().load();
        // WalkerRoutes
        WalkerRoutesParser.getInstance().load();
        // Recipe
        RecipeParser.getInstance().load();
        // Reflection
        ReflectionManager.getInstance();
        // Airship Dock
        AirshipDockParser.getInstance().load();
        // Skill Acquire
        SkillAcquireParser.getInstance().load();
        // Residence
        ResidenceParser.getInstance().load();
        // Shuttle
        ShuttleTemplateParser.getInstance().load();
        // Event
        EventParser.getInstance().load();
        // support(cubic & agathion)
        // Cubic
        CubicParser.getInstance().load();
        // Buy List
        BuyListHolder.getInstance();
        // Recipe
        RecipeHolder.getInstance();
        // MultiSell
        MultiSellHolder.getInstance();
        // Product
        ProductHolder.getInstance();
        // AgathionParser.getInstance();
        // item support
        // Henna
        HennaParser.getInstance().load();
        // Jump Tracks
        JumpTracksParser.getInstance().load();
        // Soul Crystal
        SoulCrystalParser.getInstance().load();
        // Armor Sets
        ArmorSetsParser.getInstance().load();
        // Fish
        FishDataParser.getInstance().load();
        // PetitionGroup
        PetitionGroupParser.getInstance().load();
        // Player templates
        PlayerTemplateParser.getInstance().load();
        // Classes load
        ClassDataParser.getInstance().load();
        // LvL bonus load
        LevelBonusParser.getInstance().load();
        // Statistic
        StatuesSpawnParser.getInstance().load();

        DynamicQuestParser.getInstance().load();

        StartItemParser.getInstance().load();
    }
}
