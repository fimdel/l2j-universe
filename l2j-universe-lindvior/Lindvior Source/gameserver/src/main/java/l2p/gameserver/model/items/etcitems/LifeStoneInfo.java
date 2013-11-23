package l2p.gameserver.model.items.etcitems;

/**
 * @author ALF
 * @date 27.06.2012
 */
public class LifeStoneInfo {
    private int itemId;
    private int level;
    private LifeStoneGrade grade;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public LifeStoneGrade getGrade() {
        return grade;
    }

    public void setGrade(LifeStoneGrade grade) {
        this.grade = grade;
    }
}
