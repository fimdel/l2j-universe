package l2p.gameserver.model.items.listeners;

import l2p.gameserver.data.xml.holder.ArmorSetsHolder;
import l2p.gameserver.listener.inventory.OnEquipListener;
import l2p.gameserver.model.ArmorSet;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

import java.util.ArrayList;
import java.util.List;


public final class ArmorSetListener implements OnEquipListener {
    private static final ArmorSetListener _instance = new ArmorSetListener();

    public static ArmorSetListener getInstance() {
        return _instance;
    }

    @Override
    public void onEquip(int slot, ItemInstance item, Playable actor) {
        if (!item.isEquipable())
            return;

        Player player = (Player) actor;

        // checks if there is armorset for chest item that player worns
        ArmorSet armorSet = ArmorSetsHolder.getInstance().getArmorSet(item.getItemId());
        if (armorSet == null)
            return;

        boolean update = false;
        // checks if equipped item is part of set
        if (armorSet.containItem(slot, item.getItemId())) {
            List<Skill> skills = armorSet.getSkills(armorSet.getEquipedSetPartsCount(player));
            for (Skill skill : skills) {
                player.addSkill(skill, false);
                update = true;
            }

            if (armorSet.containAll(player)) {
                if (armorSet.containShield(player)) // has shield from set
                {
                    skills = armorSet.getShieldSkills();
                    for (Skill skill : skills) {
                        player.addSkill(skill, false);
                        update = true;
                    }
                }
                if (armorSet.isEnchanted6(player)) // has all parts of set enchanted to 6 or more
                {
                    skills = armorSet.getEnchant6skills();
                    for (Skill skill : skills) {
                        player.addSkill(skill, false);
                        update = true;
                    }
                }
            }
        } else if (armorSet.containShield(item.getItemId()) && armorSet.containAll(player)) {
            List<Skill> skills = armorSet.getShieldSkills();
            for (Skill skill : skills) {
                player.addSkill(skill, false);
                update = true;
            }
        }

        if (update) {
            player.sendSkillList();
            player.updateStats();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onUnequip(int slot, ItemInstance item, Playable actor) {
        if (!item.isEquipable())
            return;

        Player player = (Player) actor;

        boolean remove = false;
        boolean setPartUneqip = false;
        List<Skill> removeSkillId1 = new ArrayList<Skill>(); // set skill
        List<Skill> removeSkillId2 = new ArrayList<Skill>(); // shield skill
        List<Skill> removeSkillId3 = new ArrayList<Skill>(); // enchant +6 skill

        ArmorSet armorSet = ArmorSetsHolder.getInstance().getArmorSet(item.getItemId());
        if (armorSet == null)
            return;

        if (armorSet.containItem(slot, item.getItemId())) // removed part of set
        {
            remove = true;
            setPartUneqip = true;
            removeSkillId1 = armorSet.getSkillsToRemove();
            removeSkillId2 = armorSet.getShieldSkills();
            removeSkillId3 = armorSet.getEnchant6skills();
        } else if (armorSet.containShield(item.getItemId())) // removed shield
        {
            remove = true;
            removeSkillId2 = armorSet.getShieldSkills();
        }

        boolean update = false;
        if (remove) {
            for (Skill skill : removeSkillId1) {
                player.removeSkill(skill, false);
                update = true;
            }
            for (Skill skill : removeSkillId2) {
                player.removeSkill(skill);
                update = true;
            }
            for (Skill skill : removeSkillId3) {
                player.removeSkill(skill);
                update = true;
            }
        }

        List<Skill> skills = armorSet.getSkills(armorSet.getEquipedSetPartsCount(player));
        for (Skill skill : skills) {
            player.addSkill(skill, false);
            update = true;
        }

        if (update) {
            if (!player.getInventory().isRefresh) {
                // При снятии вещей из состава S80 или S84 сета снимаем плащ
                if (!player.getOpenCloak() && player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_BACK, null) != null)
                    player.sendPacket(SystemMsg.YOUR_CLOAK_HAS_BEEN_UNEQUIPPED_BECAUSE_YOUR_ARMOR_SET_IS_NO_LONGER_COMPLETE);
            }

            player.sendSkillList();
            player.updateStats();
        }
    }
}
