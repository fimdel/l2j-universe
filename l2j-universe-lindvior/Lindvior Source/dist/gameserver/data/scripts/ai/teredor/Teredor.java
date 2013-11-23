package ai.teredor;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;

import java.util.List;

/**
 * Класс контролирует АИ инстансого босса Teredor (Траджан) и спавн дополнительных яиц с АИ TeredorLairEggs
 * <p/>
 * Изначально находится в земле. После агра спавнит 3х Elite Millipede.
 * На определенных стадиях ХП уходит под землю на ~15 секунд. При этом его нельзя взять в таргет, он не агрится.
 * <p/>
 * User: Darvin
 * Date: 16.10.12
 * Time: 22:31
 */
public class Teredor extends Fighter {
    /* Список скиллов:
        14110	1	a,Teredor Blow
        14111	1	a,Teredor Range Attack
        14112	1	a,Teredor Poison
        14112	2	a,Teredor Poison
        14560	1	a,Teredor Poison Paralysis
        14113	1	a,Teredor Flu Infection
        14114	1	a,Teredor Physical Resistance
        14115	1	a,Teredor Magic Resistance
        14116	1	a,Teredor Rage
     */
    private static int jumpSkillId = 6268; // Уточнить ид
    private static int poisonSkillId = 14561;  // Зеленый туман

    //Teredor info
    // 19160 - под землей (или на проходе или стадия под землей)
    // 19172 - тоже самое что и 19160
    // 25785 - тоже самое
    // 25801 - так же

    private static int teredor = 19160;
    private static int eliteMillipede = 19015;
    private static int teredorLairEgg = 19023;

    // Additional locs for spawn eggs
    // 1 - 176360 -185096 -3826 - там же где и основные
    // 2 - 175896 -185576 -3826 - правее от основных (если смотреть со входа)

    private Location[] coordsToSpawnEggs = {
            new Location(176360, -185096, -3826),
            new Location(175896, -185576, -3826)
    };

    private static int timeFromPassiveToActive = 10;// секунды
    private static int delayEggTask = 90; // Секунды

    boolean _teredorActive = true;
    boolean _eliteSpawned = false;
    boolean _battleActive = false;
    boolean _jumpAttacked = false;
    boolean _canUsePoison = false;
    boolean _poisonCasted = false;

    List<NpcInstance> teredorEggs;

    private NpcInstance actor = getActor();
    private CurrentHpListener _currentHpListener = new CurrentHpListener();

    public Teredor(NpcInstance actor) {
        super(actor);
        MAX_PURSUE_RANGE = 7000;
        actor.addListener(_currentHpListener);
    }

    @Override
    protected void onEvtAggression(Creature attacker, int aggro) {

        super.onEvtAggression(attacker, aggro);
    }

    @Override
    protected void thinkAttack() {

        // Червяк сразу после агра бьет прыжком 1 раз.
        if (!_jumpAttacked) {
            if (!actor.isCastingNow())
                actor.doCast(SkillTable.getInstance().getInfo(jumpSkillId, 1), actor, true);
        }


        if (!_canUsePoison) {
            if (!actor.isCastingNow() && !_poisonCasted) {
                actor.doCast(SkillTable.getInstance().getInfo(poisonSkillId, 1), actor, true);
                _poisonCasted = true;
            }
        }

        // Назначаем 1 раз. Используется для таска на спавн яиц
        if (!_battleActive) {
            _battleActive = true;
            Reflection r = actor.getReflection();
            teredorEggs = r.getAllByNpcId(teredorLairEgg, true);
            // Каждый делей спавним яйцо
            ThreadPoolManager.getInstance().scheduleAtFixedDelay(new EggSpawnTask(r), 1000, delayEggTask * 1000);
        }

        // При агре спавним 3 элитника и агрим их на танка
        if (!_eliteSpawned) {

            SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(eliteMillipede));
            sp.setLoc(Location.findPointToStay(actor, 100, 120));

            for (int i = 0; i == 2; i++) {
                NpcInstance npc = sp.doSpawn(true);
                npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor.getAggroList().getMostHated(), Rnd.get(1, 100));
            }
            _eliteSpawned = true;
        }

        super.thinkAttack();
    }

    @Override
    protected void onEvtDead(Creature killer) {
        _battleActive = false;
        actor.removeListener(_currentHpListener);

        super.onEvtDead(killer);
    }

    @Override
    protected void teleportHome() {
        return;
    }

    public class CurrentHpListener implements OnCurrentHpDamageListener {
        @Override
        public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill) {
            if (actor == null || actor.isDead() || actor.getNpcId() != teredor) {
                return;
            }
            double newHp = actor.getCurrentHp() - damage;
            double maxHp = actor.getMaxHp();
            // Тередор уходит под землю на 80, 60, 40, 20 % здоровья.
            if (_teredorActive && (newHp == 0.8 * maxHp || newHp == 0.6 * maxHp
                    || newHp == 0.4 * maxHp || newHp == 0.2 * maxHp)) {
                _teredorActive = false;
                _eliteSpawned = false;
                ThreadPoolManager.getInstance().execute(new TeredorPassiveTask((NpcInstance) actor));
                if (newHp <= 0.8 * maxHp && !_canUsePoison)
                    _canUsePoison = true;
            }
        }
    }

    public class TeredorPassiveTask extends RunnableImpl {
        NpcInstance _npc;

        public TeredorPassiveTask(NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void runImpl() {
            if ((_npc != null) && (!_npc.isDead())) {
                _npc.getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT);
                _npc.getAggroList().clear(); // Удаляем весь агр.
                _npc.setTargetable(false);
                _npc.setIsInvul(true); // На всякий случай вешаем инвул, чтобы багом не били по земле
                ThreadPoolManager.getInstance().schedule(new TeredorActiveTask(_npc), timeFromPassiveToActive * 10);
            }
        }
    }

    public class TeredorActiveTask extends RunnableImpl {
        NpcInstance _npc;

        public TeredorActiveTask(NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void runImpl() {
            if ((_npc != null) && (!_npc.isDead())) {
                _npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                _npc.setTargetable(true);
                _npc.setUnAggred(false);
                _npc.setIsInvul(false);
                _eliteSpawned = false; // Позволяем снова спавнить элитников
                _teredorActive = true;
                _jumpAttacked = false;
                _poisonCasted = false;
            }
        }
    }

    public class EggSpawnTask extends RunnableImpl {
        Reflection _r;

        public EggSpawnTask(Reflection r) {
            _r = r;
        }

        @Override
        public void runImpl() {
            if (_battleActive) {
                // Спавним в радиусе 30-80 от одной из рандомных точек. По оффу спавнятся в 2х точках.
                Location _coords = Location.findPointToStay(coordsToSpawnEggs[Rnd.get(1)], 50, 100);
                _r.addSpawnWithoutRespawn(teredorLairEgg, _coords, 0);
            }
        }
    }

}
