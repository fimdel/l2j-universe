/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectRestorationRandom extends Effect
{
	/**
	 * Field items.
	 */
	private final List<List<Item>> items;
	/**
	 * Field chances.
	 */
	private final double[] chances;
	/**
	 * Field groupPattern.
	 */
	private static final Pattern groupPattern = Pattern.compile("\\{\\[([\\d:;]+?)\\]([\\d.e-]+)\\}");
	
	/**
	 * Constructor for EffectRestorationRandom.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectRestorationRandom(Env env, EffectTemplate template)
	{
		super(env, template);
		String[] groups = getTemplate().getParam().getString("Items").split(";");
		items = new ArrayList<>(groups.length);
		chances = new double[groups.length];
		double prevChance = 0;
		for (int i = 0; i < groups.length; i++)
		{
			String group = groups[i];
			Matcher m = groupPattern.matcher(group);
			if (m.find())
			{
				String its = m.group(1);
				List<Item> list = new ArrayList<>(its.split(";").length);
				for (String item : its.split(";"))
				{
					String id = item.split(":")[0];
					String count = item.split(":")[1];
					Item it = new Item();
					it.itemId = Integer.parseInt(id);
					it.count = Long.parseLong(count);
					list.add(it);
				}
				double chance = Double.parseDouble(m.group(2));
				items.add(i, list);
				chances[i] = prevChance + chance;
				prevChance = chances[i];
			}
		}
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		double chance = (double) Rnd.get(0, 1000000) / 10000;
		double prevChance = 0.0D;
		int i = 0;
		for (; i < chances.length; i++)
		{
			if ((chance > prevChance) && (chance < chances[i]))
			{
				break;
			}
		}
		if (i < chances.length)
		{
			List<Item> itemList = items.get(i);
			for (Item item : itemList)
			{
				ItemFunctions.addItem((Playable) getEffected(), item.itemId, item.count, true);
			}
		}
		else
		{
			getEffected().sendPacket(SystemMsg.THERE_WAS_NOTHING_FOUND_INSIDE);
		}
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	protected boolean onActionTime()
	{
		return false;
	}
	
	/**
	 * @author Mobius
	 */
	private final class Item
	{
		/**
		 * Constructor for Item.
		 */
		public Item()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Field itemId.
		 */
		public int itemId;
		/**
		 * Field count.
		 */
		public long count;
	}
}
