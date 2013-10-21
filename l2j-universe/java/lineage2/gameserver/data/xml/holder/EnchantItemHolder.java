package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public class EnchantItemHolder extends AbstractHolder
{
	private static EnchantItemHolder _instance = new EnchantItemHolder();

	private IntObjectMap<AppearanceStone> _appearanceStones = new HashIntObjectMap<AppearanceStone>();

	private EnchantItemHolder()
	{
	}

	public static EnchantItemHolder getInstance()
	{
		return _instance;
	}

	public void addAppearanceStone(AppearanceStone appearanceStone)
	{
		_appearanceStones.put(appearanceStone.getItemId(), appearanceStone);
	}

	public AppearanceStone getAppearanceStone(int id)
	{
		return _appearanceStones.get(id);
	}

	public int[] getAppearanceStones()
	{
		return _appearanceStones.keySet().toArray();
	}

	@Override
	public void log()
	{
		info("loaded " + _appearanceStones.size() + " appearance stone(s) count.");
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		_appearanceStones.clear();
	}
}