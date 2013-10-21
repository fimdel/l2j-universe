package lineage2.gameserver.network.serverpackets;

/**
 * @author : Darvin
 * @date : 26.01.12 18:13
 */
public class ExBR_PresentBuyProductPacket extends L2GameServerPacket
{
	public static final int RESULT_OK = 1; // OK
	public static final int RESULT_NOT_ENOUGH_POINTS = -1;// Недостаточно
	                                                      // средств для покупки
	public static final int RESULT_WRONG_PRODUCT = -2; // Невозможно приобрести
	                                                   // данный продукт
	public static final int RESULT_INVENTORY_FULL = -4; // Вы не можете купить
	                                                    // предмет, так как ваш
	                                                    // инвентарь переполнен
	public static final int RESULT_WRONG_ITEM = -5; // Невозможно приобрести
	                                                // данный предмет
	public static final int RESULT_SALE_PERIOD_ENDED = -7; // also -8 // Вы не
	                                                       // можете купить
	                                                       // данный предмет.
	                                                       // Закончилось время
	                                                       // продажи
	public static final int RESULT_WRONG_USER_STATE = -9; // also -11 // Вы не
	                                                      // можете сейчас
	                                                      // приобрести этот
	                                                      // предмет
	public static final int RESULT_WRONG_PACKAGE_ITEMS = -10;// Вы не можете
	                                                         // приобрести
	                                                         // упаковку
	                                                         // предметов
	public static final int RESULT_WRONG_DAY_OF_WEEK = -12;// В этот день недели
	                                                       // покупка невозможна
	public static final int RESULT_WRONG_SALE_PERIOD = -13;// В данный период
	                                                       // времени покупка
	                                                       // невозможна
	public static final int RESULT_ITEM_WAS_SALED = -14;// Предмет продан

	private int result;

	public ExBR_PresentBuyProductPacket(int result)
	{
		this.result = result;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x120);
		writeD(result);
		writeC(0x00);// (GOD) Unknown
	}
}
