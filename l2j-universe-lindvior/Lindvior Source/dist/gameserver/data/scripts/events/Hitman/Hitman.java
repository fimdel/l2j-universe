/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package events.Hitman;

import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.ItemFunctions;
import npc.model.events.HitmanInstance.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.11.12
 * Time: 13:41
 */
public class Hitman extends Functions implements ScriptFile, OnDeathListener {
    private static final Logger _log = LoggerFactory.getLogger(Hitman.class);

    private static final String DELETE_TERGET_FROM_DATABASE = "DELETE FROM event_hitman WHERE target=?";
    private static final String SAVE_TO_DATABASE = "INSERT INTO event_hitman VALUES (?,?,?,?,?,?)";
    private static final String LOAD_FROM_DATABASE = "SELECT * FROM event_hitman";
    private static final String DELETE_STOREID_FROM_DATABASE = "DELETE FROM event_hitman WHERE storedId=?";

    private static Map<Integer, Order> _orderMap;
    private static StringBuilder _itemsList;
    private static Map<String, Integer> _allowedItems;
    private static List<Integer> _inList;

    public static boolean checkPlayer(Player player) {
        if (player.isDead()) {
            show(new CustomMessage("scripts.events.Hitman.CancelledDead", player), player);
            return false;
        }

        if (player.getTeam() != TeamType.NONE) {
            show(new CustomMessage("scripts.events.Hitman.CancelledOtherEvent", player), player);
            return false;
        }

        if (player.isMounted()) {
            show(new CustomMessage("scripts.events.Hitman.Cancelled", player), player);
            return false;
        }

        if (player.isCursedWeaponEquipped()) {
            show(new CustomMessage("scripts.events.Hitman.Cancelled", player), player);
            return false;
        }

        if (player.isInDuel()) {
            show(new CustomMessage("scripts.events.Hitman.CancelledDuel", player), player);
            return false;
        }

        if (player.getOlympiadGame() != null || Olympiad.isRegistered(player)) {
            show(new CustomMessage("scripts.events.Hitman.CancelledOlympiad", player), player);
            return false;
        }

        if (player.isInParty() && player.getParty().isInDimensionalRift()) {
            show(new CustomMessage("scripts.events.Hitman.CancelledOtherEvent", player), player);
            return false;
        }

        if (player.isInObserverMode()) {
            show(new CustomMessage("scripts.events.Hitman.CancelledObserver", player), player);
            return false;
        }

        if (player.isTeleporting()) {
            show(new CustomMessage("scripts.events.Hitman.CancelledTeleport", player), player);
            return false;
        }

        return true;
    }

    @Override
    public void onLoad() {
        _orderMap = new HashMap<Integer, Order>();
        _itemsList = new StringBuilder();
        _allowedItems = new HashMap<String, Integer>();
        _inList = new ArrayList<Integer>();

        CharListenerList.addGlobal(this);

        for (int i = 0; i < Config.EVENT_HITMAN_ALLOWED_ITEM_LIST.length; i++) {
            final String itemName = ItemFunctions.createItem(Integer.parseInt(Config.EVENT_HITMAN_ALLOWED_ITEM_LIST[i])).getTemplate().getName();
            _itemsList.append(itemName).append(";");
            _allowedItems.put(itemName, Integer.parseInt(Config.EVENT_HITMAN_ALLOWED_ITEM_LIST[i]));
        }
        loadFromDatabase();
        _log.info("Loaded Event: Hitman");
    }

    @Override
    public void onDeath(Creature actor, Creature killer) {
        if (getOrderByTargetName(actor.getName()) != null && !actor.getName().equals(killer.getName())) {
            final Order order = getOrderByTargetName(actor.getName());
            Functions.addItem(killer.getPlayer(), order.getItemId(), order.getItemCount());
            Announcements.getInstance().announceToAll(new CustomMessage("scripts.events.Hitman.AnnounceKill", killer.getPlayer(), killer.getName(), actor.getName(), order.getItemCount(), ItemFunctions.createItem(order.getItemId()).getTemplate().getName()).toString());

            if (order.getKillsCount() > 1)
                order.decrementKillsCount();
            else {
                _orderMap.remove(World.getPlayer(order.getOwner()).getObjectId());
                _inList.remove((Object) World.getPlayer(order.getOwner()).getObjectId());
                deleteFromDatabase(actor.getName());
            }
        }
    }

    public static void deleteFromDatabase(String target) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(DELETE_TERGET_FROM_DATABASE);
            statement.setString(1, target);
            statement.execute();
        } catch (Exception e) {

        }
    }

    @Override
    public void onReload() {
        _orderMap.clear();
        _allowedItems.clear();
    }

    @Override
    public void onShutdown() {
        onReload();
    }

    public static String getItemsList() {
        return _itemsList.toString();
    }

    public static Order getOrderById(int index) {
        return _orderMap.get(_inList.get(index));
    }

    public static int addOrder(Player player, String name, int killsCount, int itemcount, String itemname) {
        if (!checkPlayer(player))
            return -1;

        if (Functions.getItemCount(player, Config.EVENT_HITMAN_COST_ITEM_ID) < Config.EVENT_HITMAN_COST_ITEM_COUNT)
            return 0;

        if (Functions.getItemCount(player, _allowedItems.get(itemname)) < itemcount)
            return 0;

        if (isRegistered(player))
            return 1;

        if (World.getPlayer(name) == null)
            return 2;

        if (World.getPlayer(name).getObjectId() == player.getObjectId())
            return 3;

        final Order order = new Order(player.getName(), name, _allowedItems.get(itemname), itemcount, killsCount);

        _orderMap.put(player.getObjectId(), order);
        _inList.add(0, player.getObjectId());
        saveToDatabase(player.getObjectId(), player.getName(), name, _allowedItems.get(itemname), itemcount, killsCount);
        Functions.removeItem(player, Config.EVENT_HITMAN_COST_ITEM_ID, Config.EVENT_HITMAN_COST_ITEM_COUNT);
        Functions.removeItem(player, _allowedItems.get(itemname), itemcount * killsCount);

        Announcements.getInstance().announceToAll(new CustomMessage("scripts.events.Hitman.Announce", player, player.getName(), itemcount, itemname, name).toString());

        World.getPlayer(name).setIsOrdered(true);

        return 5;
    }

    public static int getOrdersCount() {
        return _orderMap.size();
    }

    private static void saveToDatabase(int objectId, String ownerName, String targetName, int itemId, int itemCount, int killsCount) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SAVE_TO_DATABASE);
            statement.setInt(1, objectId);
            statement.setString(2, ownerName);
            statement.setString(3, targetName);
            statement.setInt(4, itemId);
            statement.setInt(5, itemCount);
            statement.setInt(6, killsCount);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadFromDatabase() {
        Connection con;
        PreparedStatement statement = null;
        ResultSet rset = null;

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(LOAD_FROM_DATABASE);
            rset = statement.executeQuery();

            while (rset.next()) {
                _orderMap.put(rset.getInt(1), new Order(rset.getString(2), rset.getString(3), rset.getInt(4), rset.getInt(5), rset.getInt(6)));
                _inList.add(0, rset.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteFromDatabase(int storedId) {
        Connection con;
        PreparedStatement statement = null;

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(DELETE_STOREID_FROM_DATABASE);
            statement.setInt(1, storedId);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteOrder(int storedId) {
        if (!_orderMap.containsKey(storedId))
            return false;
        Functions.addItem(World.getPlayer(storedId), _orderMap.get(storedId).getItemId(), _orderMap.get(storedId).getItemCount() * _orderMap.get(storedId).getKillsCount());
        _orderMap.remove(storedId);
        _inList.remove((Object) storedId);
        deleteFromDatabase(storedId);
        GameObjectsStorage.getPlayer(storedId).setIsOrdered(false);
        return true;

    }

    public static boolean isRegistered(Player player) {
        if (_orderMap.containsKey(player.getObjectId()))
            return true;
        return false;
    }

    public Order getOrderByTargetName(String name) {
        for (final Order order : _orderMap.values())
            if (name.equals(order.getTargetName()))
                return order;
        return null;
    }
}
