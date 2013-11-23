package npc.model;

import l2p.commons.lang.reference.HardReference;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.SpecialMonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public class SquashInstance extends SpecialMonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = 6930568735712026663L;
    public final static int Young_Squash = 12774;
    public final static int High_Quality_Squash = 12775;
    public final static int Low_Quality_Squash = 12776;
    public final static int Large_Young_Squash = 12777;
    public final static int High_Quality_Large_Squash = 12778;
    public final static int Low_Quality_Large_Squash = 12779;
    public final static int King_Squash = 13016;
    public final static int Emperor_Squash = 13017;

    private HardReference<Player> _spawnerRef;

    public SquashInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public void setSpawner(Player spawner) {
        _spawnerRef = spawner.getRef();
    }

    public Player getSpawner() {
        return _spawnerRef.get();
    }

    @Override
    public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage) {
        if (attacker.getActiveWeaponInstance() == null)
            return;

        int weaponId = attacker.getActiveWeaponInstance().getItemId();

        if (getNpcId() == Low_Quality_Large_Squash || getNpcId() == High_Quality_Large_Squash || getNpcId() == Emperor_Squash)
            // Разрешенное оружие для больших тыкв:
            // 4202 Chrono Cithara
            // 5133 Chrono Unitus
            // 5817 Chrono Campana
            // 7058 Chrono Darbuka
            // 8350 Chrono Maracas
            if (weaponId != 4202 && weaponId != 5133 && weaponId != 5817 && weaponId != 7058 && weaponId != 8350)
                return;

        i = 1;

        super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
    }

    @Override
    public long getRegenTick() {
        return 0L;
    }

    @Override
    public boolean canChampion() {
        return false;
    }
}