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
package lineage2.gameserver.skills;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SkillsEngine
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SkillsEngine.class);
	/**
	 * Field _instance.
	 */
	private static final SkillsEngine _instance = new SkillsEngine();
	
	/**
	 * Method getInstance.
	 * @return SkillsEngine
	 */
	public static SkillsEngine getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SkillsEngine.
	 */
	private SkillsEngine()
	{
	}
	
	/**
	 * Method loadSkills.
	 * @param file File
	 * @return List<Skill>
	 */
	public List<Skill> loadSkills(File file)
	{
		if (file == null)
		{
			_log.warn("SkillsEngine: File not found!");
			return null;
		}
		DocumentSkill doc = new DocumentSkill(file);
		doc.parse();
		return doc.getSkills();
	}
	
	/**
	 * Method loadAllSkills.
	 * @return Map<Integer,Skill>
	 */
	public Map<Integer, Skill> loadAllSkills()
	{
		File dir = new File(Config.DATAPACK_ROOT, "data/xml/stats/skills");
		if (!dir.exists())
		{
			_log.info("Dir " + dir.getAbsolutePath() + " not exists");
			return Collections.emptyMap();
		}
		Collection<File> files = FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter(".xml"), FileFilterUtils.directoryFileFilter());
		Map<Integer, Skill> result = new HashMap<>();
		int maxId = 0, maxLvl = 0;
		for (File file : files)
		{
			List<Skill> s = loadSkills(file);
			if (s == null)
			{
				continue;
			}
			for (Skill skill : s)
			{
				result.put(SkillTable.getSkillHashCode(skill), skill);
				if (skill.getId() > maxId)
				{
					maxId = skill.getId();
				}
				if (skill.getLevel() > maxLvl)
				{
					maxLvl = skill.getLevel();
				}
			}
		}
		_log.info("SkillsEngine: Loaded " + result.size() + " skill templates from XML files. Max id: " + maxId + ", max level: " + maxLvl);
		return result;
	}
}
