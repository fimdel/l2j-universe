package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.SkillTable;

public class EvilNpc extends DefaultAI {
    private long _lastAction;
    private static final String[] _txt = {
            "отстань!",
            "уймись!",
            "я тебе отомщу, потом будешь прощения просить!",
            "у тебя будут неприятности!",
            "я на тебя пожалуюсь, тебя арестуют!"};

    public EvilNpc(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (attacker == null || attacker.getPlayer() == null)
            return;

        // Ругаемся и кастуем скилл не чаще, чем раз в 3 секунды
        if (System.currentTimeMillis() - _lastAction > 3000) {
            int chance = Rnd.get(0, 100);
            if (chance < 2)
                attacker.getPlayer().setKarma(attacker.getPlayer().getKarma() + 5);
            else if (chance < 4)
                actor.doCast(SkillTable.getInstance().getInfo(4578, 1), attacker, true); // Petrification
            else
                actor.doCast(SkillTable.getInstance().getInfo(4185, 7), attacker, true); // Sleep

            Functions.npcSay(actor, attacker.getName() + ", " + _txt[Rnd.get(_txt.length)]);
            _lastAction = System.currentTimeMillis();
        }
    }
}