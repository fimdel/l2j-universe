package lineage2.gameserver.network.serverpackets;

/**
 * @author ALF
 * @data 07.02.2012 Интерфейс для пакетов, требующих отображение иконок эффектов
 */
public interface IconEffectPacket
{
	/**
	 * @param skillId
	 * @param level
	 * @param duration
	 */
	void addIconEffect(int skillId, int level, int duration, int obj);
}
