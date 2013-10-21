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
package lineage2.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.Race;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SkillAcquireParser extends AbstractDirParser<SkillAcquireHolder>
{
	/**
	 * Field _instance.
	 */
	private static final SkillAcquireParser _instance = new SkillAcquireParser();
	
	/**
	 * Method getInstance.
	 * @return SkillAcquireParser
	 */
	public static SkillAcquireParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SkillAcquireParser.
	 */
	protected SkillAcquireParser()
	{
		super(SkillAcquireHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/skill_tree/");
	}
	
	/**
	 * Method isIgnored.
	 * @param b File
	 * @return boolean
	 */
	@Override
	public boolean isIgnored(File b)
	{
		return false;
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "tree.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("certification_skill_tree"); iterator.hasNext();)
		{
			getHolder().addAllCertificationLearns(parseSkillLearn(iterator.next()));
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("dualclass_certification_skill_tree"); iterator.hasNext();)
		{
			getHolder().addAllDualCertificationLearns(parseSkillLearn(iterator.next()));
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("sub_unit_skill_tree"); iterator.hasNext();)
		{
			getHolder().addAllSubUnitLearns(parseSkillLearn(iterator.next()));
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("pledge_skill_tree"); iterator.hasNext();)
		{
			getHolder().addAllPledgeLearns(parseSkillLearn(iterator.next()));
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("collection_skill_tree"); iterator.hasNext();)
		{
			getHolder().addAllCollectionLearns(parseSkillLearn(iterator.next()));
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("fishing_skill_tree"); iterator.hasNext();)
		{
			Element nxt = iterator.next();
			for (Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext();)
			{
				Element classElement = classIterator.next();
				int race = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllFishingLearns(race, learns);
			}
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("transfer_skill_tree"); iterator.hasNext();)
		{
			Element nxt = iterator.next();
			for (Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext();)
			{
				Element classElement = classIterator.next();
				int classId = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllTransferLearns(classId, learns);
			}
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("normal_skill_tree"); iterator.hasNext();)
		{
			HashMap<Integer, List<SkillLearn>> map = new HashMap<Integer, List<SkillLearn>>();
			Element nxt = iterator.next();
			for (Iterator<Element> classIterator = nxt.elementIterator("class"); classIterator.hasNext();)
			{
				Element classElement = classIterator.next();
				int classId = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				map.put(classId, learns);
			}
			getHolder().addAllNormalSkillLearns(map);
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("transformation_skill_tree"); iterator.hasNext();)
		{
			Element nxt = iterator.next();
			for (Iterator<Element> classIterator = nxt.elementIterator("race"); classIterator.hasNext();)
			{
				Element classElement = classIterator.next();
				int race = Integer.parseInt(classElement.attributeValue("id"));
				List<SkillLearn> learns = parseSkillLearn(classElement);
				getHolder().addAllTransformationLearns(race, learns);
			}
		}
		for (Iterator<Element> iterator = rootElement.elementIterator("awakening_keep_skill_tree"); iterator.hasNext();)
		{
			HashMap <Integer, HashMap<Integer,List<Integer>> > map = new HashMap<Integer, HashMap<Integer,List<Integer>>>();
			Element nxt = iterator.next();
			for (Iterator<Element> awakenClassIterator = nxt.elementIterator("awakenClass"); awakenClassIterator.hasNext();)
			{
				Element awakenClass = awakenClassIterator.next();
				int awakenClassId = Integer.parseInt(awakenClass.attributeValue("id"));
				HashMap<Integer,List<Integer>> transferClassList = new HashMap<Integer,List<Integer>>();
				for(Iterator <Element> fromClassIterator = awakenClass.elementIterator("fromClass"); fromClassIterator.hasNext();)
				{
					Element fromClass = fromClassIterator.next();
					int fromClassId = Integer.parseInt(fromClass.attributeValue("id"));
					List <Integer> keepSkill = parseKeepSkill(fromClass);
					transferClassList.put(fromClassId, keepSkill);
				}
				map.put(awakenClassId, transferClassList);
			}
			getHolder().addSkillsToMaintain(map);
		}
	}
	
	/**
	 * Method parseSkillLearn.
	 * @param tree Element
	 * @return List<SkillLearn>
	 */
	private List<SkillLearn> parseSkillLearn(Element tree)
	{
		List<SkillLearn> skillLearns = new ArrayList<SkillLearn>();
		for (Iterator<Element> iterator = tree.elementIterator("skill"); iterator.hasNext();)
		{
			Element element = iterator.next();
			int id = Integer.parseInt(element.attributeValue("id"));
			int level = Integer.parseInt(element.attributeValue("level"));
			int cost = element.attributeValue("cost") == null ? 0 : Integer.parseInt(element.attributeValue("cost"));
			int min_level = Integer.parseInt(element.attributeValue("min_level"));
			int item_id = element.attributeValue("item_id") == null ? 0 : Integer.parseInt(element.attributeValue("item_id"));
			long item_count = element.attributeValue("item_count") == null ? 1 : Long.parseLong(element.attributeValue("item_count"));
			boolean clicked = (element.attributeValue("clicked") != null) && Boolean.parseBoolean(element.attributeValue("clicked"));
			boolean deprecated = (element.attributeValue("deprecated") != null) && Boolean.parseBoolean(element.attributeValue("deprecated"));
			Race race = element.attributeValue("race") == null ? null : Race.valueOf(element.attributeValue("race"));
			
			skillLearns.add(new SkillLearn(id, level, min_level, cost, item_id, item_count, clicked, deprecated, race, new HashMap<Integer, Long>(), new ArrayList<Integer>()));
		}
		return skillLearns;
	}
	
	/**
	 * Method parseRemoveSkill
	 * @param tree Element
	 * @return List<Integer>
	 */
	private List<Integer> parseKeepSkill(Element tree)
	{
		List <Integer> skillRemove = new ArrayList<Integer>();
		for (Iterator<Element> iterator = tree.elementIterator("keepSkill"); iterator.hasNext();)
		{
			Element element = iterator.next();
			int id = Integer.parseInt(element.attributeValue("id"));
			skillRemove.add(id);
		}
		return skillRemove;
	}	
}
