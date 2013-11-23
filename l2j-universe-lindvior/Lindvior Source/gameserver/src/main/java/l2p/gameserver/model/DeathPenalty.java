package l2p.gameserver.model;

import l2p.commons.lang.reference.HardReference;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.tables.SkillTable;

public class DeathPenalty {
    private static final int _skillId = 14571;
    private static final int _fortuneOfNobleseSkillId = 1325;
    private static final int _charmOfLuckSkillId = 2168;

    private HardReference<Player> _playerRef;
    private int _level;
    private boolean _hasCharmOfLuck;

    public DeathPenalty(Player player, int level) {
        _playerRef = player.getRef();
        _level = level;
    }

    public Player getPlayer() {
        return _playerRef.get();
    }

    /*
      * For common usage
      */
    public int getLevel() {
        // Some checks if admin set incorrect value at database
        if (_level > 5)
            _level = 5;

        if (_level < 0)
            _level = 0;

        return Config.ALLOW_DEATH_PENALTY_C5 ? _level : 0;
    }

    /*
      * Used only when saving DB if admin for some reasons disabled it in config after it was enabled.
      * In if we will use getLevel() it will be reseted to 0
      */
    public int getLevelOnSaveDB() {
        if (_level > 5)
            _level = 5;

        if (_level < 0)
            _level = 0;

        return _level;
    }

    public void notifyDead(Creature killer) {
        if (!Config.ALLOW_DEATH_PENALTY_C5)
            return;

        if (_hasCharmOfLuck) {
            _hasCharmOfLuck = false;
            return;
        }

        if (killer == null || killer.isPlayable())
            return;

        Player player = getPlayer();
        if (player == null || player.getLevel() <= 10)
            return;

        int karmaBonus = player.getKarma() / Config.ALT_DEATH_PENALTY_C5_KARMA_PENALTY;
        if (karmaBonus < 0)
            karmaBonus = 0;

        if (Rnd.chance(Config.ALT_DEATH_PENALTY_C5_CHANCE + karmaBonus))
            addLevel();
    }

    public void restore(Player player) {
        Skill remove = player.getKnownSkill(_skillId);
        if (remove != null)
            player.removeSkill(remove, true);

        if (!Config.ALLOW_DEATH_PENALTY_C5)
            return;

        if (getLevel() > 0) {
            player.addSkill(SkillTable.getInstance().getInfo(_skillId, getLevel()), false);
            player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel()));
        }
        player.sendEtcStatusUpdate();
        player.updateStats();
    }

    public void addLevel() {
        Player player = getPlayer();
        if (player == null || getLevel() >= 5 || player.isGM())
            return;

        if (getLevel() != 0) {
            Skill remove = player.getKnownSkill(_skillId);
            if (remove != null)
                player.removeSkill(remove, true);
        }

        _level++;

        player.addSkill(SkillTable.getInstance().getInfo(_skillId, getLevel()), false);
        player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel()));
        player.sendEtcStatusUpdate();
        player.updateStats();
    }

    public void reduceLevel() {
        Player player = getPlayer();
        if (player == null || getLevel() <= 0)
            return;

        Skill remove = player.getKnownSkill(_skillId);
        if (remove != null)
            player.removeSkill(remove, true);

        _level--;

        if (getLevel() > 0) {
            player.addSkill(SkillTable.getInstance().getInfo(_skillId, getLevel()), false);
            player.sendPacket(new SystemMessage(SystemMessage.THE_LEVEL_S1_SHILENS_BREATH_WILL_BE_ASSESSED).addNumber(getLevel()));
        } else
            player.sendPacket(Msg.THE_SHILENS_BREATH_HAS_BEEN_LIFTED);

        player.sendEtcStatusUpdate();
        player.updateStats();
    }

    public void checkCharmOfLuck() {
        Player player = getPlayer();
        if (player != null)
            for (Effect e : player.getEffectList().getAllEffects())
                if (e.getSkill().getId() == _charmOfLuckSkillId || e.getSkill().getId() == _fortuneOfNobleseSkillId) {
                    _hasCharmOfLuck = true;
                    return;
                }

        _hasCharmOfLuck = false;
    }
}