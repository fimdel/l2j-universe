package l2p.gameserver.model.entity.events.objects;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.attachment.FlagItemAttachment;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author VISTALL
 * @date 11:59/24.03.2011
 * Item ID: 9819
 */
public class FortressCombatFlagObject implements SpawnableObject, FlagItemAttachment {
    /**
     *
     */
    private static final long serialVersionUID = 6432373466029724691L;
    private static final Logger _log = LoggerFactory.getLogger(FortressCombatFlagObject.class);
    private ItemInstance _item;
    private Location _location;

    private GlobalEvent _event;

    public FortressCombatFlagObject(Location location) {
        _location = location;
    }

    @Override
    public void spawnObject(GlobalEvent event) {
        if (_item != null) {
            _log.info("FortressCombatFlagObject: can't spawn twice: " + event);
            return;
        }
        _item = ItemFunctions.createItem(9819);
        _item.setAttachment(this);
        _item.dropMe(null, _location);
        _item.setDropTime(0);

        _event = event;
    }

    @Override
    public void despawnObject(GlobalEvent event) {
        if (_item == null)
            return;

        Player owner = GameObjectsStorage.getPlayer(_item.getOwnerId());
        if (owner != null) {
            owner.getInventory().destroyItem(_item);
            owner.sendDisarmMessage(_item);
        }

        _item.setAttachment(null);
        _item.setJdbcState(JdbcEntityState.UPDATED);
        _item.delete();

        _item.deleteMe();
        _item = null;

        _event = null;
    }

    @Override
    public void refreshObject(GlobalEvent event) {

    }

    @Override
    public void onLogout(Player player) {
        onDeath(player, null);
    }

    @Override
    public void onDeath(Player owner, Creature killer) {
        owner.getInventory().removeItem(_item);

        _item.setOwnerId(0);
        _item.setJdbcState(JdbcEntityState.UPDATED);
        _item.update();

        owner.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_DROPPED_S1).addItemName(_item.getItemId()));

        _item.dropMe(null, _location);
        _item.setDropTime(0);
    }

    @Override
    public boolean canPickUp(Player player) {
        if (player.getActiveWeaponFlagAttachment() != null)
            return false;
        FortressSiegeEvent event = player.getEvent(FortressSiegeEvent.class);
        if (event == null)
            return false;
        SiegeClanObject object = event.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
        if (object == null)
            return false;
        return true;
    }

    @Override
    public void pickUp(Player player) {
        player.getInventory().equipItem(_item);

        FortressSiegeEvent event = player.getEvent(FortressSiegeEvent.class);
        event.broadcastTo(new SystemMessage2(SystemMsg.C1_HAS_ACQUIRED_THE_FLAG).addName(player), SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
    }

    @Override
    public boolean canAttack(Player player) {
        player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS);
        return false;
    }

    @Override
    public boolean canCast(Player player, Skill skill) {
        Skill[] skills = player.getActiveWeaponItem().getAttachedSkills();
        if (!ArrayUtils.contains(skills, skill)) {
            player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL);
            return false;
        } else
            return true;
    }

    @Override
    public void setItem(ItemInstance item) {
        // ignored
    }

    public GlobalEvent getEvent() {
        return _event;
    }
}
