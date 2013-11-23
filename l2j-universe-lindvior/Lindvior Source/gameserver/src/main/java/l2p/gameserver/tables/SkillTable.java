package l2p.gameserver.tables;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.base.EnchantSkillLearn;
import l2p.gameserver.skills.SkillsEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SkillTable {
    private static final Logger _log = LoggerFactory.getLogger(SkillTable.class);

    private static final SkillTable _instance = new SkillTable();
    public Map<Integer, Integer> identifySkills = new HashMap<Integer, Integer>();

    private TIntObjectHashMap<Skill> _skills;
    private TIntIntHashMap _maxLevelsTable;
    private TIntIntHashMap _baseLevelsTable;

    public static SkillTable getInstance() {
        return _instance;
    }

    public void load() {
        _skills = SkillsEngine.getInstance().loadAllSkills();
        makeLevelsTable();
        loadtxtdata();
    }

    public void reload() {
        load();
    }

    public Skill getInfo(int skillId, int level) {
        Player player = null;
        if (Skill.isElementalySpecificMagic(skillId)) {
            if (player != null) {
                skillId = skillId + player.getElementalySpecific();
            }
        }
        return _skills.get(getSkillHashCode(skillId, level));
    }

    public Skill getInfo(int skillId, final Player player) {
        final int level = player.getSkillLevel(skillId);

        if (level == -1) {
            return null;
        }

        if (Skill.isElementalySpecificMagic(skillId)) {
            skillId = skillId + player.getElementalySpecific();
        }

        return getInfo(skillId, level);
    }

    public int getMaxLevel(int skillId) {
        return _maxLevelsTable.get(skillId);
    }

    public int getBaseLevel(int skillId) {
        return _baseLevelsTable.get(skillId);
    }

    public static int getSkillHashCode(Skill skill) {
        return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel());
    }

    public static int getSkillHashCode(int skillId, int skillLevel) {
        return skillId * 1000 + skillLevel;
    }

    private void makeLevelsTable() {
        _maxLevelsTable = new TIntIntHashMap();
        _baseLevelsTable = new TIntIntHashMap();
        for (Skill s : _skills.values(new Skill[0])) {
            int skillId = s.getId();
            int level = s.getLevel();
            int maxLevel = _maxLevelsTable.get(skillId);
            if (level > maxLevel)
                _maxLevelsTable.put(skillId, level);
            if (_baseLevelsTable.get(skillId) == 0)
                _baseLevelsTable.put(skillId, s.getBaseLevel());
        }
    }

    public void loadtxtdata() {
        _log.info("Starting load txt skills...");
        LineNumberReader lnr = null;
        try {
            File rsData = new File(Config.DATAPACK_ROOT, "data/asc/skills.txt");
            lnr = new LineNumberReader(new BufferedReader(new FileReader(rsData)));
            String line = null;
            while ((line = lnr.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#"))
                    continue;

                String args[] = line.split("\t", -1);

                int id = getInt(args[0]);
                int lvl = getInt(args[1]);
                int mp_consume = getInt(args[2]);
                int hp_consume = getInt(args[3]);
                int cool_time = getInt(args[4]);
                int cast_range = getInt(args[5]);
                int hit_time = getInt(args[6]);
                int reuse = getInt(args[7]);
                int is_magic = getInt(args[8]);

                if (lvl > 100) {
                    EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(id, lvl);
                    if (sl == null) {
                        //    _log.warn("[txt]: Error in skill id=" + id + " lvl=" + lvl);
                    } else
                        lvl = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), lvl, sl.getMaxLevel());
                }

                Skill s = getInfo(id, lvl);

                if (s != null) {
                    if (is_magic == 1 || is_magic == 22) {
                        s.setIsMagic();
                    }

                    if (is_magic == 0) {
                        s.setIsPhysic();
                    }

                    if (is_magic == 4) {
                        s.setIsSpecial();
                    }

                    if (is_magic == 3) {
                        s.setIsMusic();
                    }

                    if (reuse > 0) {
                        s.setReuseDelay(reuse);
                    }

                    if (cool_time > 0) {
                        s.setCoolTime(cool_time);
                    }

                    if (hit_time > 0) {
                        s.setHitTime(hit_time);
                    }

                    if (s.getSkillInterruptTime() == 0) {
                        s.setSkillInterruptTime(s.getHitTime() * 3 / 4);
                    }

                    if (mp_consume > 0) {
                        if (mp_consume / 4 >= 1 && is_magic == 1 || is_magic == 22) {
                            s.setMpConsume1(mp_consume * 1. / 4);
                            s.setMpConsume2(mp_consume * 3. / 4);
                        } else
                            s.setMpConsume2(mp_consume);
                    }
                    if (hp_consume > 0) {
                        s.setHpConsume(hp_consume);
                    }

                    if (cast_range > 0) {
                        s.setCastRange(cast_range);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            _log.info("data/asc/skills.txt is missing in data folder");
        } catch (Exception e) {
            _log.warn("error while loading txt skills ", e);
        } finally {
            try {
                if (lnr != null)
                    lnr.close();
            } catch (Exception ignored) {
            }
        }
        _log.info("Finish load txt skills...");
    }

    private static int getInt(String name) {
        String[] args = name.split("=", -1);
        return Integer.parseInt(args[1]);
    }
}
