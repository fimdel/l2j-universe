package l2p.gameserver.model.entity.events.objects;

/**
 * @author VISTALL
 * @date 10:07/17.03.2011
 */
public class CastleDamageZoneObject extends ZoneObject {
    /**
     *
     */
    private static final long serialVersionUID = -2559177433034763358L;
    private final long _price;

    public CastleDamageZoneObject(String name, long price) {
        super(name);
        _price = price;
    }

    public long getPrice() {
        return _price;
    }
}
