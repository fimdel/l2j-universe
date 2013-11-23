package npc.model;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExStartScenePlayer;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;

/**
 * @author pchayka
 */
public final class SealDeviceInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = -5919893436358653440L;
    private boolean _gaveItem = false;

    public SealDeviceInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage) {
        if (this.getCurrentHp() < i) {
            if (!_gaveItem && ItemFunctions.getItemCount(attacker.getPlayer(), 13846) < 4) {
                this.setRHandId(15281);
                this.broadcastCharInfo();
                ItemFunctions.addItem(attacker.getPlayer(), 13846, 1, true);
                _gaveItem = true;

                if (ItemFunctions.getItemCount(attacker.getPlayer(), 13846) >= 4) {
                    attacker.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SSQ_SEALING_EMPEROR_2ND);
                    ThreadPoolManager.getInstance().schedule(new TeleportPlayer(attacker.getPlayer()), 26500L);
                }
            }
            i = this.getCurrentHp() - 1;
        }
        attacker.reduceCurrentHp(450, 0, this, null, true, false, true, false, false, false, true);
        super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
    }

    private class TeleportPlayer extends RunnableImpl {
        Player _p;

        public TeleportPlayer(Player p) {
            _p = p;
        }

        @Override
        public void runImpl() throws Exception {
            for (NpcInstance n : _p.getReflection().getNpcs())
                if (n.getNpcId() != 32586 && n.getNpcId() != 32587)
                    n.deleteMe();
            _p.getPlayer().teleToLocation(new Location(-89560, 215784, -7488));
        }
    }

    @Override
    public boolean isFearImmune() {
        return true;
    }

    @Override
    public boolean isParalyzeImmune() {
        return true;
    }

    @Override
    public boolean isLethalImmune() {
        return true;
    }

    @Override
    public boolean isMovementDisabled() {
        return true;
    }
}