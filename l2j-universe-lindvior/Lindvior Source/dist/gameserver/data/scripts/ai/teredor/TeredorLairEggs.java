package ai.teredor;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;

/**
 * Яйца в месте спавна Тередора
 * В промежутке 1-2 минуты яйцо становится синим и через ~10 секунд вылупляется 18995 Awakened Milipede
 * <p/>
 * User: Darvin
 * Date: 16.10.12
 * Time: 22:36
 */
public class TeredorLairEggs extends Fighter {
    // Eggs info
    // 19023 - обычное
    // 18997 - синее
    private static int awakenedMillipede = 18995;
    private static int teredorLarva = 19016;

    // Teredor's Larva - 19016
    // Elite Millipede  - 19015

    // Additional locs for spawn eggs
    // 1 - 176360 -185096 -3826 - там же где и основные
    // 2 - 175896 -185576 -3826 - правее от основных (если смотреть со входа)

    private static int timeToBlue = 60; // секунды
    private static int maxRandomTimeBlue = 80; // Яйца не одновременно становятся синими, поэтому выставляем рандом
    private static int monsterSpawnDelay = 15; // секунды

    //14561	1	a,Teredor Poison Area\0
    private static int poisonId = 14561;
    private static int poisonLevel = 1;
    private static int distanceToDebuff = 400;

    boolean _poisoned = false;
    boolean _activated = false;

    private NpcInstance actor = getActor();

    public TeredorLairEggs(NpcInstance actor) {
        super(actor);
        actor.startImmobilized();
    }

    @Override
    protected void thinkAttack() {
        if (!_activated) {
            Player player = (Player) actor.getAggroList().getMostHated();
            Reflection ref = actor.getReflection();
            ThreadPoolManager.getInstance().schedule(new TaskSetBlue(actor, player, ref), (timeToBlue + Rnd.get(maxRandomTimeBlue)) * 1000);
            _activated = true;
        }

        if (!_poisoned) {
            Player player = (Player) actor.getAggroList().getMostHated();
            // TODO: реализовать через зоны. (или скилл аурой?)
            // пойзон должен накладываться при заходе в зону. Зона активируется при входе в агрорендж яйца.
            // Вешаем на всю пати, в том числе и на петов пойзон
            if (player.getParty() != null) {
                for (Playable playable : player.getParty().getPartyMembersWithPets()) {
                    // Вешаем пойзон, если мембер пати не дальше 400 (регулируется переменной)
                    if (playable != null && actor.getDistance(playable.getLoc()) <= distanceToDebuff)
                        actor.doCast(SkillTable.getInstance().getInfo(poisonId, poisonLevel), playable, true);
                }
            }
            _poisoned = true;
        }

        super.thinkAttack();
    }

    /*
    ** При смерти яйца спавним Teredor's Larva, которая бездумно бродит.
     */
    @Override
    protected void onEvtDead(Creature killer) {
        SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(teredorLarva));
        sp.setLoc(Location.findPointToStay(actor, 100, 120));
        sp.doSpawn(true);

        super.onEvtDead(killer);
    }

    public class TaskSetBlue implements Runnable {
        NpcInstance _npc;
        Player _player;
        Reflection _ref;

        public TaskSetBlue(NpcInstance npc, Player player, Reflection ref) {
            _npc = npc;
            _player = player;
            _ref = ref;
        }

        public void run() {
            if ((_npc != null) && (!_npc.isDead())) {
                //TODO: нужно добавить синий абнормал
                ThreadPoolManager.getInstance().schedule(new SpawnMonster(_npc, _player, _ref), monsterSpawnDelay * 1000);
            }
        }
    }

    public class SpawnMonster extends RunnableImpl {
        NpcInstance _npc;
        Player _player;
        Reflection _ref;

        public SpawnMonster(NpcInstance npc, Player player, Reflection ref) {
            _npc = npc;
            _player = player;
            _ref = ref;
        }

        @Override
        public void runImpl() {
            if ((_npc != null) && (!_npc.isDead())) {
                if (_player != null) {
                    Location coords = Location.findPointToStay(actor, 100, 120);
                    NpcInstance npc = _ref.addSpawnWithoutRespawn(awakenedMillipede, coords, 0);
                    npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _player, Rnd.get(1, 100));

                } else
                    _npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
            }
        }
    }

}
