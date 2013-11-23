package l2p.gameserver.model.actor.instances.player;

/**
 * Класс с бонусными рейтами для игрока
 */
public class Bonus {
    public static final int NO_BONUS = 0;
    public static final int BONUS_GLOBAL_ON_LOGINSERVER = 1;
    public static final int BONUS_GLOBAL_ON_GAMESERVER = 2;

    private double rateXp = 1.;
    private double rateSp = 1.;
    private double questRewardRate = 1.;
    private double questDropRate = 1.;
    private double dropAdena = 1.;
    private double dropItems = 1.;
    private double dropSpoil = 1.;

    private int bonusExpire;

    public double getRateXp() {
        return rateXp;
    }

    public void setRateXp(double rateXp) {
        this.rateXp = rateXp;
    }

    public double getRateSp() {
        return rateSp;
    }

    public void setRateSp(double rateSp) {
        this.rateSp = rateSp;
    }

    public double getQuestRewardRate() {
        return questRewardRate;
    }

    public void setQuestRewardRate(double questRewardRate) {
        this.questRewardRate = questRewardRate;
    }

    public double getQuestDropRate() {
        return questDropRate;
    }

    public void setQuestDropRate(double questDropRate) {
        this.questDropRate = questDropRate;
    }

    public double getDropAdena() {
        return dropAdena;
    }

    public void setDropAdena(double dropAdena) {
        this.dropAdena = dropAdena;
    }

    public double getDropItems() {
        return dropItems;
    }

    public void setDropItems(double dropItems) {
        this.dropItems = dropItems;
    }

    public double getDropSpoil() {
        return dropSpoil;
    }

    public void setDropSpoil(double dropSpoil) {
        this.dropSpoil = dropSpoil;
    }

    public int getBonusExpire() {
        return bonusExpire;
    }

    public void setBonusExpire(int bonusExpire) {
        this.bonusExpire = bonusExpire;
    }
}