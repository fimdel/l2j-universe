package ai.hellbound;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.utils.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Diamond
 */
public class Kechi extends DefaultAI {
    final Skill KechiDoubleCutter; // Attack by crossing the sword. Power 2957.
    final Skill KechiAirBlade; // Strikes the enemy a blow in a distance using sword energy. Critical enabled. Power 1812

    final Skill Invincible; // Invincible against general attack and skill, buff/de-buff.
    final Skill NPCparty60ClanHeal; // TODO

    private static final int GUARD1 = 22309;
    private static final int GUARD2 = 22310;
    private static final int GUARD3 = 22417;

    private static final Location guard_spawn_loc = new Location(153384, 149528, -12136);

    private static final int[][] guard_run = new int[][]{
            {GUARD1, 153384, 149528, -12136},
            {GUARD1, 153975, 149823, -12152},
            {GUARD1, 154364, 149665, -12151},
            {GUARD1, 153786, 149367, -12151},
            {GUARD2, 154188, 149825, -12152},
            {GUARD2, 153945, 149224, -12151},
            {GUARD3, 154374, 149399, -12152},
            {GUARD3, 153796, 149646, -12159}};

    private static String[] chat = new String[]{"Стража, убейте их!", "Стража!", "Стража, на помощь!", "Добейте их.", "Вы все умрете!"};

    private int stage = 0;

    public Kechi(NpcInstance actor) {
        super(actor);

        TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();

        KechiDoubleCutter = skills.get(733);
        KechiAirBlade = skills.get(734);

        Invincible = skills.get(5418);
        NPCparty60ClanHeal = skills.get(5439);
    }

    @Override
    protected boolean createNewTask() {
        clearTasks();
        Creature target;
        if ((target = prepareTarget()) == null)
            return false;

        NpcInstance actor = getActor();
        if (actor.isDead())
            return false;

        double actor_hp_precent = actor.getCurrentHpPercents();

        switch (stage) {
            case 0:
                if (actor_hp_precent < 80) {
                    spawnMobs();
                    return true;
                }
                break;
            case 1:
                if (actor_hp_precent < 60) {
                    spawnMobs();
                    return true;
                }
                break;
            case 2:
                if (actor_hp_precent < 40) {
                    spawnMobs();
                    return true;
                }
                break;
            case 3:
                if (actor_hp_precent < 30) {
                    spawnMobs();
                    return true;
                }
                break;
            case 4:
                if (actor_hp_precent < 20) {
                    spawnMobs();
                    return true;
                }
                break;
            case 5:
                if (actor_hp_precent < 10) {
                    spawnMobs();
                    return true;
                }
                break;
            case 6:
                if (actor_hp_precent < 5) {
                    spawnMobs();
                    return true;
                }
                break;
        }

        int rnd_per = Rnd.get(100);

        if (rnd_per < 5) {
            addTaskBuff(actor, Invincible);
            return true;
        }

        double distance = actor.getDistance(target);

        if (!actor.isAMuted() && rnd_per < 75)
            return chooseTaskAndTargets(null, target, distance);

        Map<Skill, Integer> d_skill = new HashMap<Skill, Integer>();

        addDesiredSkill(d_skill, target, distance, KechiDoubleCutter);
        addDesiredSkill(d_skill, target, distance, KechiAirBlade);

        Skill r_skill = selectTopSkill(d_skill);

        return chooseTaskAndTargets(r_skill, target, distance);
    }

    private void spawnMobs() {
        stage++;

        NpcInstance actor = getActor();
        Functions.npcSay(actor, chat[Rnd.get(chat.length)]);

        for (int[] run : guard_run)
            try {
                SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(run[0]));
                sp.setLoc(guard_spawn_loc);
                sp.setReflection(actor.getReflection());
                NpcInstance guard = sp.doSpawn(true);

                Location runLoc = new Location(run[1], run[2], run[3]);

                guard.setRunning();
                DefaultAI ai = (DefaultAI) guard.getAI();

                ai.addTaskMove(runLoc, true);
                ai.setGlobalAggro(0);

                // Выбираем случайную цель
                Creature hated = actor.getAggroList().getRandomHated();
                if (hated != null) {
                    // Делаем необходимые приготовления, для атаки в конце движения
                    guard.getAggroList().addDamageHate(hated, 0, Rnd.get(1, 100)); // Это нужно, чтобы гвард не перестал атаковать цель после первых ударов
                    ai.setAttackTimeout(getMaxAttackTimeout() + System.currentTimeMillis()); // Это нужно, чтобы не сработал таймаут
                    ai.setAttackTarget(hated); // На всякий случай, не обязательно делать
                    ai.changeIntention(CtrlIntention.AI_INTENTION_ATTACK, hated, null); // Переводим в состояние атаки
                    ai.addTaskAttack(hated); // Добавляем отложенное задание атаки, сработает в самом конце движения
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}