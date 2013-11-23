/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.data.xml.parser;

import gnu.trove.TIntObjectHashMap;
import l2p.commons.data.xml.AbstractDirParser;
import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.SkillAcquireHolder;
import l2p.gameserver.model.SkillLearn;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: VISTALL
 * @date: 20:55/30.11.2010
 */
public final class SkillAcquireParser extends AbstractDirParser<SkillAcquireHolder> {
    private static final SkillAcquireParser _instance = new SkillAcquireParser();

    public static SkillAcquireParser getInstance() {
        return _instance;
    }

    protected SkillAcquireParser() {
        super(SkillAcquireHolder.getInstance());
    }

    @Override
    public File getXMLDir() {
        return new File(Config.DATAPACK_ROOT, "data/skill_tree/");
    }

    @Override
    public boolean isIgnored(File b) {
        return false;
    }

    @Override
    public String getDTDFileName() {
        return "tree.dtd";
    }

    @Override
    protected void readData(Element rootElement) throws Exception {
        for (Iterator<Element> iterator = rootElement.elementIterator("certification_skill_tree"); iterator.hasNext(); )
            getHolder().addAllCertificationLearns(parseSkillLearn(iterator.next()));
        for (Iterator<Element> iterator = rootElement.elementIterator("dual_certification_skill_tree"); iterator.hasNext(); )
            getHolder().addAllDualCertificationLearns(parseSkillLearn(iterator.next()));

        for (Iterator<Element> iterator = rootElement.elementIterator("sub_unit_skill_tree"); iterator.hasNext(); )
            getHolder().addAllSubUnitLearns(parseSkillLearn(iterator.next()));

        for (Iterator<Element> iterator = rootElement.elementIterator("pledge_skill_tree"); iterator.hasNext(); )
            getHolder().addAllPledgeLearns(parseSkillLearn(iterator.next()));

        for (Iterator<Element> iterator = rootElement.elementIterator("collection_skill_tree"); iterator.hasNext(); )
            getHolder().addAllCollectionLearns(parseSkillLearn(iterator.next()));

        for (Iterator<Element> iterator = rootElement.elementIterator("fishing_skill_tree"); iterator.hasNext(); ) {
            Element nxt = iterator.next();
            for (Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext(); ) {
                Element classElement = classIterator.next();
                int race = Integer.parseInt(classElement.attributeValue("id"));
                List<SkillLearn> learns = parseSkillLearn(classElement);
                getHolder().addAllFishingLearns(race, learns);
            }
        }

        for (Iterator<Element> iterator = rootElement.elementIterator("transfer_skill_tree"); iterator.hasNext(); ) {
            Element nxt = iterator.next();
            for (Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext(); ) {
                Element classElement = classIterator.next();
                int classId = Integer.parseInt(classElement.attributeValue("id"));
                List<SkillLearn> learns = parseSkillLearn(classElement);
                getHolder().addAllTransferLearns(classId, learns);
            }
        }

        for (Iterator<Element> iterator = rootElement.elementIterator("normal_skill_tree"); iterator.hasNext(); ) {
            TIntObjectHashMap<List<SkillLearn>> map = new TIntObjectHashMap<List<SkillLearn>>();
            Element nxt = iterator.next();
            for (Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext(); ) {
                Element classElement = classIterator.next();
                int classId = Integer.parseInt(classElement.attributeValue("id"));
                List<SkillLearn> learns = parseSkillLearn(classElement);

                map.put(classId, learns);
            }

            getHolder().addAllNormalSkillLearns(map);
        }

        for (Iterator<Element> iterator = rootElement.elementIterator("transformation_skill_tree"); iterator.hasNext(); ) {
            Element nxt = iterator.next();
            for (Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext(); ) {
                Element classElement = classIterator.next();
                int race = Integer.parseInt(classElement.attributeValue("id"));
                List<SkillLearn> learns = parseSkillLearn(classElement);
                getHolder().addAllTransformationLearns(race, learns);
            }
        }

        for (Iterator<Element> iterator = rootElement.elementIterator("spellbook_skill_tree"); iterator.hasNext(); ) {
            TIntObjectHashMap<List<SkillLearn>> map = new TIntObjectHashMap<List<SkillLearn>>();
            Element nxt = iterator.next();
            for (Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext(); ) {
                Element classElement = classIterator.next();
                int classId = Integer.parseInt(classElement.attributeValue("id"));
                List<SkillLearn> learns = parseSkillLearn(classElement);

                map.put(classId, learns);
            }
            getHolder().addAllSpellbookLearns(map);
        }

        /*  for (Iterator<Element> iterator = rootElement.elementIterator("awake_parent_skill_tree"); iterator.hasNext(); )
   {
       TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>> map = new TIntObjectHashMap<TIntObjectHashMap<List<SkillLearn>>>();
       Element nxt = iterator.next();
       for (Iterator<Element> awakeClassIterator = nxt.elementIterator("awake_class"); awakeClassIterator.hasNext(); )
       {
           TIntObjectHashMap<List<SkillLearn>> parentsMap = new TIntObjectHashMap<List<SkillLearn>>();
           Element awakeClassElement = awakeClassIterator.next();
           int awakeClassId = Integer.parseInt(awakeClassElement.attributeValue("id"));
           for (Iterator<Element> parentClassIterator = awakeClassElement.elementIterator("parent_class"); parentClassIterator.hasNext(); )
           {
               Element parentClassElement = parentClassIterator.next();
               int parentClassId = Integer.parseInt(parentClassElement.attributeValue("id"));
               List<SkillLearn> learns = parseSkillLearn(parentClassElement);
               parentsMap.put(parentClassId, learns);
           }
           map.put(awakeClassId, parentsMap);
       }

       getHolder().addAllAwakeParentSkillLearns(map);
   }     */
    }

    private List<SkillLearn> parseSkillLearn(Element tree) {
        List<SkillLearn> skillLearns = new ArrayList<SkillLearn>();
        for (Iterator<Element> iterator = tree.elementIterator("skill"); iterator.hasNext(); ) {
            Element element = iterator.next();

            int id = Integer.parseInt(element.attributeValue("id"));
            int level = Integer.parseInt(element.attributeValue("level"));
            int cost = element.attributeValue("cost") == null ? 0 : Integer.parseInt(element.attributeValue("cost"));
            int min_level = Integer.parseInt(element.attributeValue("min_level"));
            boolean clicked = element.attributeValue("clicked") != null && Boolean.parseBoolean(element.attributeValue("clicked"));

            int item_id = element.attributeValue("item_id") == null ? 0 : Integer.parseInt(element.attributeValue("item_id"));
            long item_count = element.attributeValue("item_count") == null ? 1 : Long.parseLong(element.attributeValue("item_count"));

            List<Integer> delete_skills = new ArrayList<Integer>();
            for (Element required_item : element.elements("delete_skill")) {
                int skill_id = Integer.parseInt(required_item.attributeValue("id"));
                delete_skills.add(skill_id);
            }
            skillLearns.add(new SkillLearn(id, level, min_level, cost, clicked, item_id, item_count, delete_skills));
        }

        return skillLearns;
    }
}
