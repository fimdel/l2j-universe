package l2p.gameserver.model.items.etcitems;

import l2p.gameserver.model.base.Element;

public class AttributeStoneInfo {
    private int itemId;
    private int minArmor;
    private int maxArmor;
    private int minWeapon;
    private int maxWeapon;
    private int incArmor;
    private int incWeapon;
    private int inсWeaponArmor;    // Добавление атаки итрибута при аттрибутации брони
    private Element element;
    private int chance;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getMinArmor() {
        return minArmor;
    }

    public void setMinArmor(int min) {
        this.minArmor = min;
    }

    public int getMaxArmor() {
        return maxArmor;
    }

    public void setMaxArmor(int max) {
        this.maxArmor = max;
    }

    public int getMinWeapon() {
        return minWeapon;
    }

    public void setMinWeapon(int minWeapon) {
        this.minWeapon = minWeapon;
    }

    public int getMaxWeapon() {
        return maxWeapon;
    }

    public void setMaxWeapon(int maxWeapon) {
        this.maxWeapon = maxWeapon;
    }

    public int getIncArmor() {
        return incArmor;
    }

    public void setIncArmor(int incArmor) {
        this.incArmor = incArmor;
    }

    public int getIncWeapon() {
        return incWeapon;
    }

    public void setIncWeapon(int incWeapon) {
        this.incWeapon = incWeapon;
    }

    public int getInсWeaponArmor() {
        return inсWeaponArmor;
    }

    public void setInсWeaponArmor(int intWeaponArmor) {
        this.inсWeaponArmor = intWeaponArmor;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getStoneLevel() {
        return maxWeapon / 50;
    }
}
