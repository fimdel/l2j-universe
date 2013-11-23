package l2p.commons.lang.reference;

/**
 * Интерфейс хранителя ссылки.
 *
 * @param <T>
 * @author G1ta0
 */
public interface HardReference<T> {
    /**
     * Получить объект, который удерживается *
     */
    public T get();

    /**
     * Очистить сылку на удерживаемый объект *
     */
    public void clear();
}
