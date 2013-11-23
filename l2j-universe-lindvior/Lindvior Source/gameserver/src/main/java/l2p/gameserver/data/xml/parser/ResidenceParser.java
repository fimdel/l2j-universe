package l2p.gameserver.data.xml.parser;

import l2p.commons.data.xml.AbstractDirParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.TeleportLocation;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.Fortress;
import l2p.gameserver.model.entity.residence.Residence;
import l2p.gameserver.model.entity.residence.ResidenceFunction;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.support.MerchantGuard;
import l2p.gameserver.utils.Location;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author VISTALL
 * @date 0:18/12.02.2011
 */
public final class ResidenceParser extends AbstractDirParser<ResidenceHolder> {
    private static ResidenceParser _instance = new ResidenceParser();

    public static ResidenceParser getInstance() {
        return _instance;
    }

    private ResidenceParser() {
        super(ResidenceHolder.getInstance());
    }

    @Override
    public File getXMLDir() {
        return new File(Config.DATAPACK_ROOT, "data/residences/");
    }

    @Override
    public boolean isIgnored(File f) {
        return false;
    }

    @Override
    public String getDTDFileName() {
        return "residence.dtd";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void readData(Element rootElement) throws Exception {
        String impl = rootElement.attributeValue("impl");
        Class<?> clazz = null;

        StatsSet set = new StatsSet();
        for (Iterator<Attribute> iterator = rootElement.attributeIterator(); iterator.hasNext(); ) {
            Attribute element = iterator.next();
            set.set(element.getName(), element.getValue());
        }

        Residence residence = null;
        try {
            clazz = Class.forName("l2p.gameserver.model.entity.residence." + impl);
            Constructor constructor = clazz.getConstructor(StatsSet.class);
            residence = (Residence) constructor.newInstance(set);
            getHolder().addResidence(residence);
        } catch (Exception e) {
            error("fail to init: " + getCurrentFileName(), e);
            return;
        }

        for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
            Element element = iterator.next();
            String nodeName = element.getName();
            int level = element.attributeValue("level") == null ? 0 : Integer.valueOf(element.attributeValue("level"));
            int lease = (int) ((element.attributeValue("lease") == null ? 0 : Integer.valueOf(element.attributeValue("lease"))) * Config.RESIDENCE_LEASE_FUNC_MULTIPLIER);
            int npcId = element.attributeValue("npcId") == null ? 0 : Integer.valueOf(element.attributeValue("npcId"));
            int listId = element.attributeValue("listId") == null ? 0 : Integer.valueOf(element.attributeValue("listId"));

            ResidenceFunction function = null;
            if (nodeName.equalsIgnoreCase("teleport")) {
                function = checkAndGetFunction(residence, ResidenceFunction.TELEPORT);
                List<TeleportLocation> targets = new ArrayList<TeleportLocation>();
                for (Iterator<Element> it2 = element.elementIterator(); it2.hasNext(); ) {
                    Element teleportElement = it2.next();
                    if ("target".equalsIgnoreCase(teleportElement.getName())) {
                        int npcStringId = Integer.parseInt(teleportElement.attributeValue("name"));
                        long price = Long.parseLong(teleportElement.attributeValue("price"));
                        int itemId = teleportElement.attributeValue("item") == null ? ItemTemplate.ITEM_ID_ADENA : Integer.parseInt(teleportElement.attributeValue("item"));
                        TeleportLocation loc = new TeleportLocation(itemId, price, npcStringId, 0);
                        loc.set(Location.parseLoc(teleportElement.attributeValue("loc")));
                        targets.add(loc);
                    }
                }
                function.addTeleports(level, targets.toArray(new TeleportLocation[targets.size()]));
            } else if (nodeName.equalsIgnoreCase("support")) {
                if (level > 9 && !Config.ALT_CH_ALLOW_1H_BUFFS)
                    continue;
                function = checkAndGetFunction(residence, ResidenceFunction.SUPPORT);
                function.addBuffs(level);
            } else if (nodeName.equalsIgnoreCase("item_create")) {
                function = checkAndGetFunction(residence, ResidenceFunction.ITEM_CREATE);
                function.addBuylist(level, new int[]{npcId, listId});
            } else if (nodeName.equalsIgnoreCase("curtain"))
                function = checkAndGetFunction(residence, ResidenceFunction.CURTAIN);
            else if (nodeName.equalsIgnoreCase("platform"))
                function = checkAndGetFunction(residence, ResidenceFunction.PLATFORM);
            else if (nodeName.equalsIgnoreCase("restore_exp"))
                function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_EXP);
            else if (nodeName.equalsIgnoreCase("restore_hp"))
                function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_HP);
            else if (nodeName.equalsIgnoreCase("restore_mp"))
                function = checkAndGetFunction(residence, ResidenceFunction.RESTORE_MP);
            else if (nodeName.equalsIgnoreCase("skills"))
                for (Iterator<Element> nextIterator = element.elementIterator(); nextIterator.hasNext(); ) {
                    Element nextElement = nextIterator.next();
                    int id2 = Integer.parseInt(nextElement.attributeValue("id"));
                    int level2 = Integer.parseInt(nextElement.attributeValue("level"));

                    Skill skill = SkillTable.getInstance().getInfo(id2, level2);
                    if (skill != null)
                        residence.addSkill(skill);
                }
            else if (nodeName.equalsIgnoreCase("banish_points"))
                for (Iterator<Element> banishPointsIterator = element.elementIterator(); banishPointsIterator.hasNext(); ) {
                    Location loc = Location.parse(banishPointsIterator.next());

                    residence.addBanishPoint(loc);
                }
            else if (nodeName.equalsIgnoreCase("owner_restart_points"))
                for (Iterator<Element> ownerRestartPointsIterator = element.elementIterator(); ownerRestartPointsIterator.hasNext(); ) {
                    Location loc = Location.parse(ownerRestartPointsIterator.next());

                    residence.addOwnerRestartPoint(loc);
                }
            else if (nodeName.equalsIgnoreCase("other_restart_points"))
                for (Iterator<Element> otherRestartPointsIterator = element.elementIterator(); otherRestartPointsIterator.hasNext(); ) {
                    Location loc = Location.parse(otherRestartPointsIterator.next());

                    residence.addOtherRestartPoint(loc);
                }
            else if (nodeName.equalsIgnoreCase("chaos_restart_points"))
                for (Iterator<Element> chaosRestartPointsIterator = element.elementIterator(); chaosRestartPointsIterator.hasNext(); ) {
                    Location loc = Location.parse(chaosRestartPointsIterator.next());

                    residence.addChaosRestartPoint(loc);
                }
            else if (nodeName.equalsIgnoreCase("related_fortresses"))
                for (Iterator<Element> subElementIterator = element.elementIterator(); subElementIterator.hasNext(); ) {
                    Element subElement = subElementIterator.next();
                    if (subElement.getName().equalsIgnoreCase("domain"))
                        ((Castle) residence).addRelatedFortress(Fortress.DOMAIN, Integer.parseInt(subElement.attributeValue("fortress")));
                    else if (subElement.getName().equalsIgnoreCase("boundary"))
                        ((Castle) residence).addRelatedFortress(Fortress.BOUNDARY, Integer.parseInt(subElement.attributeValue("fortress")));
                }
            else if (nodeName.equalsIgnoreCase("merchant_guards"))
                for (Iterator<Element> subElementIterator = element.elementIterator(); subElementIterator.hasNext(); ) {
                    Element subElement = subElementIterator.next();

                    int itemId = Integer.parseInt(subElement.attributeValue("item_id"));
                    int npcId2 = Integer.parseInt(subElement.attributeValue("npc_id"));
                    int maxGuard = Integer.parseInt(subElement.attributeValue("max"));
                    ((Castle) residence).addMerchantGuard(new MerchantGuard(itemId, npcId2, maxGuard));
                }

            if (function != null)
                function.addLease(level, lease);
        }
    }

    private ResidenceFunction checkAndGetFunction(Residence residence, int type) {
        ResidenceFunction function = residence.getFunction(type);
        if (function == null) {
            function = new ResidenceFunction(residence.getId(), type);
            residence.addFunction(function);
        }
        return function;
    }
}
