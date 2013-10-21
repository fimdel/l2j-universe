package handler.items;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;

import org.apache.commons.lang3.ArrayUtils;

/**
* @author Gorodetskiy
*/
public class TrulyFree extends SimpleItemHandler
{
	private static final int[] ITEM_IDS = new int[] { 
									18549,
									32263,
									34760,
									35548,
									35700,
									35701,
									35704,
									35709,
									35720,
									35721,
									35745,
									35751};

	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}

	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		if(!canBeExtracted(player, item))
			return false;

		if(!useItem(player, item, 1))
			return false;

		switch(itemId)
		{
			case 18549:
				use18549(player, ctrl);
				break;
			case 32263:
				use32263(player, ctrl);
				break;
			case 34760:
				use34760(player, ctrl);
				break;
			case 35548:
				use35548(player, ctrl);
				break;
			//case 35700:
				//use35700(player, ctrl);
				//break;
			//case 35701:
				//use35701(player, ctrl);
				//break;
			//case 35704:
				//use35704(player, ctrl);
				//break;
			case 35709:
				use35709(player, ctrl);
				break;
			case 35720:
				use35720(player, ctrl);
				break;
			case 35721:
				use35721(player, ctrl);
				break;
			case 35745:
				use35745(player, ctrl);
				break;
			case 35751:
				use35751(player, ctrl);
				break;
			default:
				return false;
		}

		return true;
	}
	

	private void use18549(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{19448, 1}, {19447, 1}, {22428, 1}, {22627, 1}, {19440, 1}, {18219, 1}, {18230, 1}, {18225, 1}, {18222, 1}, {18228, 1},	{18233, 1},	{18224, 1}, {18235, 1}, {18238, 1}, {18236, 1},	{18237, 1},	{18221, 1}, {18227, 1}, {18232, 1}, {18231, 1}, {18234, 1}, {18223, 1}, {18229, 1}};
		double[] chances = new double[]{0.031, 0.001, 0.396, 0.033, 0.378, 0.013, 0.006, 0.01, 0.008, 0.007, 0.01, 0.011, 0.009, 0.02, 0.01, 0.008, 0.003, 0.004, 0.005, 0.002, 0.015, 0.014, 0.006};
		extractRandomOneItem(player, list, chances);
	}
	
	
	private void use32263(Player player, boolean ctrl)
	{
	Functions.addItem(player,13722, 350);
	Functions.addItem(player,30357, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,30358, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,30359, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,13750, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,32289, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,32291, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,32290, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,19447, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,19448, 1);
	Functions.addItem(player,13722, 250);
	Functions.addItem(player,30297, 1);
	Functions.addItem(player,13722, 250);
	}
	
	@SuppressWarnings("unused")
	private void use32741(Player player, boolean ctrl)
//	http://l2central.info/wiki/%D0%9A%D0%BE%D1%80%D0%BE%D0%B1%D0%BA%D0%B0_%D0%AD%D0%BB%D0%B8%D0%BA%D1%81%D0%B8%D1%80%D0%BE%D0%B2_(A)
	{
		Functions.addItem(player, 34702, 1);
		Functions.addItem(player, 8638, 1);
		Functions.addItem(player, 8626, 1);
		Functions.addItem(player, 8632, 1);
	}

	private void use34760(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33800, 1);
		Functions.addItem(player, 33801, 1);
		Functions.addItem(player, 34759, 1);
	}
	private void use35548(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{19448, 1}, {19447, 1}, {9555, 1}, {9553, 1}, {9554, 1}, {9552, 1}, {9557, 1}, {9556, 1}, {9549, 1}, 
		{9547, 1},	{9548, 1},	{9546, 1}, {9551, 1}, {9550, 1}, {22428, 1}, {17526, 1}};
		double[] chances = new double[]{6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25};
		extractRandomOneItem(player, list, chances);
	}
	
	private void use35709(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{9555, 1}, {9553, 1}, {9554, 1}, {9552, 1}, {9557, 1}, {9556, 1}, {9549, 1}, 
		{9547, 1},	{9548, 1},	{9546, 1}, {9551, 1}, {9550, 1}};
		double[] chances = new double[]{8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333, 8.33333};
		extractRandomOneItem(player, list, chances);
	}

	
	private void use35720(Player player, boolean ctrl)
	{
		Functions.addItem(player, 35718, 1);
	}
	
	private void use35721(Player player, boolean ctrl)
	{
		Functions.addItem(player, 35719, 1);
	}
	
		private void use35745(Player player, boolean ctrl)
	{
		Functions.addItem(player, 35722, 1);
		Functions.addItem(player, 35748, 1);
		Functions.addItem(player, 35915, 1);
		Functions.addItem(player, 35916, 1);
		Functions.addItem(player, 35917, 1);
		Functions.addItem(player, 35918, 1);
		Functions.addItem(player, 35919, 1);
		Functions.addItem(player, 35920, 1);
		Functions.addItem(player, 35921, 1);
		Functions.addItem(player, 35922, 1);
		Functions.addItem(player, 35923, 1);
		Functions.addItem(player, 35924, 1);
		Functions.addItem(player, 35925, 1);
		Functions.addItem(player, 35926, 1);
		Functions.addItem(player, 35927, 1);
		Functions.addItem(player, 35928, 1);
		Functions.addItem(player, 35746, 1);
		Functions.addItem(player, 35747, 1);
	}
	
	private void use35751(Player player, boolean ctrl)
	{
		Functions.addItem(player, 19463, 1);
		Functions.addItem(player, 19464, 1);
		Functions.addItem(player, 17623, 1);
	}
	
	private static boolean canBeExtracted(Player player, ItemInstance item)
	{
		if(player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL, new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		return true;
	}
		private static boolean extractRandomOneItem(Player player, int[][] items, double[] chances)
	{
		if(items.length != chances.length)
			return false;

		double extractChance = 0;
		for(double c : chances)
			extractChance += c;

		if(Rnd.chance(extractChance))
		{
			int[] successfulItems = new int[0];
			while(successfulItems.length == 0)
				for(int i = 0; i < items.length; i++)
					if(Rnd.chance(chances[i]))
						successfulItems = ArrayUtils.add(successfulItems, i);
			int[] item = items[successfulItems[Rnd.get(successfulItems.length)]];
			if(item.length < 2)
				return false;

			Functions.addItem(player, item[0], item[1]);
		}
		return true;
	}
}