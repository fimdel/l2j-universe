package l2p.gameserver.model.entity.events.objects;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.data.xml.holder.EventHolder;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.events.EventType;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.model.entity.events.impl.DominionSiegeEvent;
import l2p.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.instances.TerritoryWardInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.attachment.FlagItemAttachment;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author VISTALL
 * @date 13:24/08.04.2011
 */
public class TerritoryWardObject implements SpawnableObject, FlagItemAttachment {
    /**
     *
     */
    private static final long serialVersionUID = -3179325927406629031L;
    private final int _itemId;
    private final NpcTemplate _template;
    private final Location _location;

    private NpcInstance _wardNpcInstance;
    private ItemInstance _wardItemInstance;

    public TerritoryWardObject(int itemId, int npcId, Location location) {
        _itemId = itemId;
        _template = NpcHolder.getInstance().getTemplate(npcId);
        _location = location;
    }

    @Override
    public void spawnObject(GlobalEvent event) {
        _wardItemInstance = ItemFunctions.createItem(_itemId);
        _wardItemInstance.setAttachment(this);

        _wardNpcInstance = new TerritoryWardInstance(IdFactory.getInstance().getNextId(), _template, this);
        _wardNpcInstance.addEvent(event);
        _wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp());
        _wardNpcInstance.spawnMe(_location);
    }

    @Override
    public void despawnObject(GlobalEvent event) {
        if (_wardItemInstance == null || _wardNpcInstance == null)
            return;

        Player owner = GameObjectsStorage.getPlayer(_wardItemInstance.getOwnerId());
        if (owner != null) {
            owner.getInventory().destroyItem(_wardItemInstance);
            owner.sendDisarmMessage(_wardItemInstance);
        }
        _wardItemInstance.setAttachment(null);
        _wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
        _wardItemInstance.delete();
        _wardItemInstance.deleteMe();
        _wardItemInstance = null;

        _wardNpcInstance.deleteMe();
        _wardNpcInstance = null;
    }

    @Override
    public void refreshObject(GlobalEvent event) {
        //
    }

    @Override
    public void onLogout(Player player) {
        player.getInventory().removeItem(_wardItemInstance);

        _wardItemInstance.setOwnerId(0);
        _wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
        _wardItemInstance.update();

        _wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp(), true);
        _wardNpcInstance.spawnMe(_location);
    }

    @Override
    public void onDeath(Player owner, Creature killer) {
        Location loc = owner.getLoc();

        owner.getInventory().removeItem(_wardItemInstance);
        owner.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_DROPPED_S1).addName(_wardItemInstance));

        _wardItemInstance.setOwnerId(0);
        _wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
        _wardItemInstance.update();

        _wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp(), true);
        _wardNpcInstance.spawnMe(loc);

        DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
        runnerEvent.broadcastTo(new SystemMessage2(SystemMsg.THE_CHARACTER_THAT_ACQUIRED_S1S_WARD_HAS_BEEN_KILLED).addResidenceName(getDominionId()));
    }

    @Override
    public boolean canPickUp(Player player) {
        return true;
    }

    @Override
    public void pickUp(Player player) {
        player.getInventory().addItem(_wardItemInstance);
        player.getInventory().equipItem(_wardItemInstance);

        player.sendPacket(SystemMsg.YOUVE_ACQUIRED_THE_WARD);

        DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
        runnerEvent.broadcastTo(new SystemMessage2(SystemMsg.THE_S1_WARD_HAS_BEEN_DESTROYED_C2_NOW_HAS_THE_TERRITORY_WARD).addResidenceName(getDominionId()).addName(player));
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

    }

    public Location getWardLocation() {
        if (_wardItemInstance == null || _wardNpcInstance == null)
            return null;

        if (_wardItemInstance.getOwnerId() > 0) {
            Player player = GameObjectsStorage.getPlayer(_wardItemInstance.getOwnerId());
            if (player != null)
                return player.getLoc();
        }

        return _wardNpcInstance.getLoc();
    }

    public NpcInstance getWardNpcInstance() {
        return _wardNpcInstance;
    }

    public ItemInstance getWardItemInstance() {
        return _wardItemInstance;
    }

    public int getDominionId() {
        return _itemId - 13479;
    }

    public DominionSiegeEvent getEvent() {
        return _wardNpcInstance.getEvent(DominionSiegeEvent.class);
    }
}
