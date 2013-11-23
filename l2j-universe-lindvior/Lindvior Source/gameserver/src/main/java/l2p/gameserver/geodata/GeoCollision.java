package l2p.gameserver.geodata;

import l2p.commons.geometry.Shape;

public interface GeoCollision {
    public Shape getShape();

    public byte[][] getGeoAround();

    public void setGeoAround(byte[][] geo);

    public boolean isConcrete();
}
