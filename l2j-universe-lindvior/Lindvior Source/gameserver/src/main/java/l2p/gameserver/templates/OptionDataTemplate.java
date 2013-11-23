package l2p.gameserver.templates;

import l2p.gameserver.model.Skill;
import l2p.gameserver.stats.StatTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date 19:17/19.05.2011
 */
public class OptionDataTemplate extends StatTemplate {
    private final List<Skill> _skills = new ArrayList<Skill>(0);
    private final int _id;

    public OptionDataTemplate(int id) {
        _id = id;
    }

    public void addSkill(Skill skill) {
        _skills.add(skill);
    }

    public List<Skill> getSkills() {
        return _skills;
    }

    public int getId() {
        return _id;
    }
}
