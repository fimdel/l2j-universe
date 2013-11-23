package l2p.gameserver.model.WorldStatistic;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 29.10.12
 * Time: 16:33
 */
public class CharacterStatisticElement {
    private CategoryType categoryType;
    private long value;
    private int subCat;
    private long monthlyValue;

    public CharacterStatisticElement(CategoryType type, int cubcat, long value, long monthlyValue) {
        categoryType = type;
        this.value = value;
        subCat = cubcat;
        this.monthlyValue = monthlyValue;
    }

    public CharacterStatisticElement(CategoryType type, long value) {
        this(type, 0, value, 0L);
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public long getValue() {
        return value;
    }

    public long getsubCat() {
        return subCat;
    }

    public long getMonthlyValue() {
        return monthlyValue;
    }
}
