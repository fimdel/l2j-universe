package l2p.gameserver.skills.skillclasses;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.FeedableBeastInstance;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class BeastFeed extends Skill {
    public BeastFeed(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(final Creature activeChar, List<Creature> targets) {
        for (final Creature target : targets)
            ThreadPoolManager.getInstance().execute(new RunnableImpl() {
                @Override
                public void runImpl() throws Exception {
                    if (target instanceof FeedableBeastInstance)
                        ((FeedableBeastInstance) target).onSkillUse((Player) activeChar, _id);
                }
            });
    }
}
