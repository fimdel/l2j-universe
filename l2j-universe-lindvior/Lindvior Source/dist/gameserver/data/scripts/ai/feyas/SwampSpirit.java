/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.feyas;

import l2p.commons.collections.LazyArrayList;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.MagicSkillUse;
import l2p.gameserver.tables.SkillTable;

public class SwampSpirit extends DefaultAI
{

    public SwampSpirit(NpcInstance actor)
    {
        super(actor);
    }

    @Override
    protected boolean randomAnimation()
    {
        return false;
    }

    @Override
    protected boolean randomWalk()
    {
        return false;
    }

    @Override
    protected boolean thinkActive()
    {
        NpcInstance actor = getActor();

        if(actor == null)
            return false;

        for(Player player : World.getAroundPlayers(actor, 300, 300))
        {
            if(player != null && !player.isDead() && !player.isAlikeDead())
            {
                Skill skill;
                if(actor.getNpcId() == 32915)
                {
                    skill = SkillTable.getInstance().getInfo(14064, 1);
                    actor.broadcastPacket(new MagicSkillUse(actor, player, 14064, 1, 0, 0));
                }
                else
                {
                    skill = SkillTable.getInstance().getInfo(14065, 1);
                    actor.broadcastPacket(new MagicSkillUse(actor, player, 14065, 1, 0, 0));
                }
                LazyArrayList<Creature> target = new LazyArrayList<Creature>(); //if more than one
                target.add(player);
                actor.callSkill(skill, target, true);
                actor.decayMe();
                target.clear();
                target = null;
                ThreadPoolManager.getInstance().schedule(new SpawnTask(actor), 180000L);
                return true; //one player only
            }
        }
        return false;
    }

    private class SpawnTask extends RunnableImpl
    {
        private NpcInstance _npc;

        public SpawnTask(NpcInstance npc)
        {
            _npc = npc;
        }

        @Override
        public void runImpl()
        {
            _npc.spawnMe();
        }
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage)
    {}

    @Override
    protected void onEvtAggression(Creature target, int aggro)
    {}
}
