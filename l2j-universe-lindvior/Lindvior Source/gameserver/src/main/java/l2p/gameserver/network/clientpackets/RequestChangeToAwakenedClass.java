/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.lang.reference.HardReference;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.model.instances.AwakeningManagerInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SocialAction;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.network.serverpackets.components.UsmVideo;
import l2p.gameserver.utils.ItemFunctions;

public class RequestChangeToAwakenedClass extends L2GameClientPacket {
    private boolean _change;

    protected void readImpl() {
        this._change = (readD() == 1);
    }

    protected void runImpl() {
        if (!this._change) {
            return;
        }
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        if (activeChar.getSummonList().size() > 0) {
            activeChar.sendActionFailed();
            return;
        }

        NpcInstance npc = activeChar.getLastNpc();
        if (npc == null) {
            activeChar.sendActionFailed();
            return;
        }

        if (!(npc instanceof AwakeningManagerInstance)) {
            activeChar.sendActionFailed();
            return;
        }

        AwakeningManagerInstance awakeningManager = (AwakeningManagerInstance) npc;
        int requestAwakeningId = activeChar.getVarInt(AwakeningManagerInstance.getAwakeningRequestVar(activeChar.getClassId()));
        /*     if (awakeningManager.getClassIdByNpc().getId() != requestAwakeningId) {
activeChar.sendActionFailed();
return;
}                 */

        if (ItemFunctions.getItemCount(activeChar, 17600) < 0L) {
            activeChar.sendActionFailed();
            return;
        }

        if (ItemFunctions.getItemCount(activeChar, 32227) < 0L) {
            activeChar.sendActionFailed();
            return;
        }

        ClassId awakedClassId = ClassId.VALUES[requestAwakeningId];
        if ((!awakedClassId.childOf(activeChar.getClassId())) && (ItemFunctions.getItemCount(activeChar, 17722) < 0L)) {
            activeChar.sendActionFailed();
            return;
        }

        if (!activeChar.isQuestContinuationPossible(false)) {
            activeChar.sendPacket(SystemMsg.YOU_CANNOT_AWAKEN_DUE_TO_WEIGHT_LIMITS_PLEASE_TRY_AWAKEN_AGAIN_AFTER_INCREASING_THE_ALLOWED_WEIGHT_BY_ORGANIZING_THE_INVENTORY);
            return;
        }

        if ((activeChar.getTransformation() != 0) || (activeChar.isMounted())) {
            activeChar.sendPacket(SystemMsg.YOU_CANNOT_AWAKEN_WHILE_YOURE_TRANSFORMED_OR_RIDING);
            return;
        }

        ItemFunctions.removeItem(activeChar, 17600, 1, true);
        ItemFunctions.removeItem(activeChar, 32227, 1, true);

        ItemFunctions.removeItem(activeChar, 17722, 1, true);

        activeChar.unsetVar(AwakeningManagerInstance.getAwakeningRequestVar(activeChar.getClassId()));

        int[] rewards = getRewardItem(awakedClassId, activeChar.getClassId());
        for (int rewardId : rewards) {
            if (rewardId > 0) {
                ItemFunctions.addItem(activeChar, rewardId, 1L, true);
            }
        }
        activeChar.setClassId(requestAwakeningId, false);
        activeChar.broadcastUserInfo(true);
        activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 20 + (requestAwakeningId - 139)));

        ThreadPoolManager.getInstance().schedule(new ShowUsmMovie(activeChar), 10000L);
    }

    private static int[] getRewardItem(ClassId awakedClassId, ClassId oldClassId) {
        int[] rewards = new int[2];

        switch (awakedClassId.ordinal()) {
            case 1:
                rewards[0] = 32264;
                break;
            case 2:
                rewards[0] = 32265;
                break;
            case 3:
                rewards[0] = 32266;
                break;
            case 4:
                rewards[0] = 32267;
                break;
            case 5:
                rewards[0] = 32268;
                break;
            case 6:
                rewards[0] = 32269;
                break;
            case 7:
                rewards[0] = 32270;
                break;
            case 8:
                rewards[0] = 32271;
                break;
            default:
                rewards[0] = 0;
        }

        switch (oldClassId.ordinal()) {
            case 9:
                rewards[1] = 33717;
                break;
            case 10:
                rewards[1] = 33718;
                break;
            case 11:
                rewards[1] = 33719;
                break;
            case 12:
                rewards[1] = 33720;
                break;
            case 13:
                rewards[1] = 33721;
                break;
            case 14:
                rewards[1] = 33722;
                break;
            case 15:
                rewards[1] = 33723;
                break;
            case 16:
                rewards[1] = 33724;
                break;
            case 17:
                rewards[1] = 33725;
                break;
            case 18:
                rewards[1] = 33726;
                break;
            case 19:
                rewards[1] = 33727;
                break;
            case 20:
                rewards[1] = 33728;
                break;
            case 21:
                rewards[1] = 33729;
                break;
            case 22:
                rewards[1] = 33730;
                break;
            case 23:
                rewards[1] = 33731;
                break;
            case 24:
                rewards[1] = 33732;
                break;
            case 25:
                rewards[1] = 33733;
                break;
            case 26:
                rewards[1] = 33734;
                break;
            case 27:
                rewards[1] = 33735;
                break;
            case 28:
                rewards[1] = 33736;
                break;
            case 29:
                rewards[1] = 33737;
                break;
            case 30:
                rewards[1] = 33738;
                break;
            case 31:
                rewards[1] = 33739;
                break;
            case 32:
                rewards[1] = 33740;
                break;
            case 33:
                rewards[1] = 33741;
                break;
            case 34:
                rewards[1] = 33742;
                break;
            case 35:
                rewards[1] = 33743;
                break;
            case 36:
                rewards[1] = 33744;
                break;
            case 37:
                rewards[1] = 33745;
                break;
            case 38:
                rewards[1] = 33746;
                break;
            case 39:
                rewards[1] = 33747;
                break;
            case 40:
                rewards[1] = 33760;
                break;
            case 41:
                rewards[1] = 33761;
                break;
            case 42:
                rewards[1] = 33762;
                break;
            case 43:
                rewards[1] = 33763;
                break;
            case 44:
                rewards[1] = 33765;
                break;
            default:
                rewards[1] = 0;
        }

        return rewards;
    }

    private static class ShowUsmMovie extends RunnableImpl {
        private final HardReference<Player> _playerRef;

        public ShowUsmMovie(Player player) {
            this._playerRef = player.getRef();
        }

        public void runImpl() {
            Player player = (Player) this._playerRef.get();
            if (player == null) {
                return;
            }
            player.sendPacket(UsmVideo.MOVIE_010.packet(player));
        }
    }
}
