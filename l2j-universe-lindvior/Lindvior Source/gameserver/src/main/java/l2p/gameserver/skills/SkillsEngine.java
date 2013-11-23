package l2p.gameserver.skills;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.Config;
import l2p.gameserver.model.Skill;
import l2p.gameserver.tables.SkillTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class SkillsEngine {
    private static final Logger _log = LoggerFactory.getLogger(SkillsEngine.class);

    private static final SkillsEngine _instance = new SkillsEngine();

    public static SkillsEngine getInstance() {
        return _instance;
    }

    private SkillsEngine() {
        //
    }

    public List<Skill> loadSkills(File file) {
        if (file == null) {
            _log.warn("SkillsEngine: File not found!");
            return null;
        }
        DocumentSkill doc = new DocumentSkill(file);
        doc.parse();
        return doc.getSkills();
    }

    public TIntObjectHashMap<Skill> loadAllSkills() {
        File dir = new File(Config.DATAPACK_ROOT, "data/stats/skills");
        if (!dir.exists()) {
            _log.info("Dir " + dir.getAbsolutePath() + " not exists");
            return new TIntObjectHashMap<Skill>(0);
        }

        Collection<File> files = FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter(".xml"), FileFilterUtils.directoryFileFilter());
        TIntObjectHashMap<Skill> result = new TIntObjectHashMap<Skill>();
        int maxId = 0, maxLvl = 0;

        for (File file : files) {
            List<Skill> s = loadSkills(file);
            if (s == null)
                continue;
            for (Skill skill : s) {
                result.put(SkillTable.getSkillHashCode(skill), skill);
                if (skill.getId() > maxId)
                    maxId = skill.getId();
                if (skill.getLevel() > maxLvl)
                    maxLvl = skill.getLevel();
                if (skill.identifyItemId() != 0)
                    SkillTable.getInstance().identifySkills.put(skill.identifyItemId(), skill.getId());
            }
        }

        _log.info("SkillsEngine: Loaded " + result.size() + " skill templates from XML files. Max id: " + maxId + ", max level: " + maxLvl);
        return result;
    }
}
