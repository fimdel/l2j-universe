package handler.items;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;

import org.apache.commons.lang3.ArrayUtils;

/**
* @author KilRoy
* @date 02.10.2012 15:54
===================================================
32727	A Treasure Box - Double-click to acquire at random a hair Accessory Pack, pendant, box, enchant scroll box, and elixir box.
===================================================
33477	Hero's Treasure Box	- Box containing 1 of various R-Grade items (Elixirs, Enchant Scrolls, etc.)
===================================================
*/
public class TreasureBox extends SimpleItemHandler
{
	private static final int[] ITEM_IDS = new int[] { 
									32241,
									32728,
									32729,
									32730,
									32731,
									32732,
									32733,
									32734,
									32735,
									32736,
									32737,
									32738,
									32739,
									32740,
									32741,
									33477,
									32264,
									32265,
									32266,
									32267,
									32268,
									32269,
									32270,
									32271,
									33771,
									33772,
									33773};

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
			// -------------Chest
			case 33477:
				use33477(player, ctrl);
				break;
			// -------------Chest end
			// -------------Enchant
			case 32730:
				use32730(player, ctrl);
				break;
			case 32731:
				use32731(player, ctrl);
				break;
			case 32732:
				use32732(player, ctrl);
				break;
			case 32733:
				use32733(player, ctrl);
				break;
			case 32734:
				use32734(player, ctrl);
				break;
			case 32735:
				use32735(player, ctrl);
				break;
			case 34695:
				use34695(player, ctrl);
				break;
			case 34696:
				use34696(player, ctrl);
				break;
			// -------------Enchant end
			// -------------Elixir
			case 32736:
				use32736(player, ctrl);
				break;
			case 32737:
				use32737(player, ctrl);
				break;
			case 32738:
				use32738(player, ctrl);
				break;
			case 32739:
				use32739(player, ctrl);
				break;
			case 32740:
				use32740(player, ctrl);
				break;
			case 32741:
				use32741(player, ctrl);
				break;
			// -------------Elixir end
			// -------------Hair Accessory
			case 32728:
				use32728(player, ctrl);
				break;
			case 32729:
				use32729(player, ctrl);
				break;
			// -------------Hair Accessory end
			// -------------Awakening Chest
			case 32264:
				use32264(player, ctrl);
				break;
			case 32265:
				use32265(player, ctrl);
				break;
			case 32266:
				use32266(player, ctrl);
				break;
			case 32267:
				use32267(player, ctrl);
				break;
			case 32268:
				use32268(player, ctrl);
				break;
			case 32269:
				use32269(player, ctrl);
				break;
			case 32270:
				use32270(player, ctrl);
				break;
			case 32271:
				use32271(player, ctrl);
				break;
			// -------------Awakening Chest end
			// -------------Aden Hero's Treasure Box
			case 33771:
				use33771(player, ctrl);
				break;
			case 33772:
				use33772(player, ctrl);
				break;
			case 33773:
				use33773(player, ctrl);
				break;
			// -------------Aden Hero's Treasure Box end
			// -------------Other Items
			case 32241:
				use32241(player, ctrl);
			// -------------Other Items end
			default:
				return false;
		}

		return true;
	}

	// --------------------------------------------------Chest
	// Hero's Treasure Box
	private void use33477(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{34694, 1}, {34695, 1}, {34696, 1}, {34697, 1}, {34698, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 9.16666, 10.86999, 38.19444};
		extractRandomOneItem(player, list, chances);
	}

	// --------------------------------------------------Chest end

	// --------------------------------------------------Enchant
	// Enchant Scroll Box - R Grade
	private void use32731(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{17526, 1}, {19447, 1}, {17527, 1}, {19448, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Enchant Scroll Box - S Grade
	private void use32730(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{959, 1}, {6577, 1}, {960, 1}, {6578, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Enchant Scroll Box - A Grade
	private void use32735(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{729, 1}, {6569, 1}, {730, 1}, {6570, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Enchant Scroll Box - B Grade
	private void use32734(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{947, 1}, {6571, 1}, {948, 1}, {6572, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Enchant Scroll Box - C Grade 
	private void use32733(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{951, 1}, {6573, 1}, {952, 1}, {6574, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Enchant Scroll Box - D Grade 
	private void use32732(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{955, 1}, {6575, 1}, {956, 1}, {6576, 1}};
		double[] chances = new double[]{11.55555, 6.01515, 34.16666, 19.86999};
		extractRandomOneItem(player, list, chances);
	}
	
	// Hero's Enchant Scroll of Destruction Box
	private void use34695(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{33478, 1}, {33479, 1}, {22221, 1}, {22222, 1}, {22223, 1}, {22224, 1}};
		double[] chances = new double[]{5.55555, 9.01515, 14.55555, 21.01515, 34.16666, 41.86999};
		extractRandomOneItem(player, list, chances);
	}
	
	// Hero's Blessed Enchant Scroll Box
	private void use34696(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{19447, 1}, {19448, 1}, {6577, 1}, {6578, 1}, {6569, 1}, {6570, 1}};
		double[] chances = new double[]{5.55555, 9.01515, 14.55555, 21.01515, 34.16666, 41.86999};
		extractRandomOneItem(player, list, chances);
	}
	// --------------------------------------------------Enchant end

	// --------------------------------------------------Elixir
	// Elixir Box (R-Grade)
	private void use32737(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{32316, 1}, {30357, 1}, {30358, 1}, {30359, 1}};
		double[] chances = new double[]{11.55555, 24.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Elixir Box (S-Grade)
	private void use32736(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8627, 1}, {8633, 1}, {8639, 1}};
		double[] chances = new double[]{19.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Elixir Box (A-Grade)
	private void use32741(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8626, 1}, {8632, 1}, {8638, 1}};
		double[] chances = new double[]{19.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Elixir Box (B-Grade)
	private void use32740(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8625, 1}, {8631, 1}, {8637, 1}};
		double[] chances = new double[]{19.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Elixir Box (C-Grade)
	private void use32739(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8624, 1}, {8630, 1}, {8636, 1}};
		double[] chances = new double[]{19.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}

	// Elixir Box (D-Grade)
	private void use32738(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8623, 1}, {8629, 1}, {8635, 1}};
		double[] chances = new double[]{19.01515, 18.16666, 32.86999};
		extractRandomOneItem(player, list, chances);
	}
	// --------------------------------------------------Elixir end

	// --------------------------------------------------Hair Accessory
	// Hair Accessory Chest
	private void use32728(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{8921, 1}, {8920, 1}, {22168, 1}, {7680, 1}, {6843, 1}, {8565, 1}, {22159, 1}, {7681, 1}, {8916, 1}, {8184, 1}, {13494, 1}, {13495, 1}};
		double[] chances = new double[]{11.55555, 11.01515, 34.16666, 34.86999, 38.19444, 35.55555, 30.55555, 25.55555, 27.55555, 31.55555, 11.55555, 11.55555};
		extractRandomOneItem(player, list, chances);
	}

	// Pendant Box
	private void use32729(Player player, boolean ctrl)
	{
		int[][] list = new int[][]{{32767, 1}, {32768, 1}, {32769, 1}, {32764, 1}, {32765, 1}, {32766, 1}, {32762, 1}, {32763, 1}, {32746, 1}, {32747, 1}, {32744, 1}, {32745, 1}, {32742, 1}, {32743, 1}, {34635, 1}, {34636, 1}, {34637, 1}, {34638, 1}, {34639, 1}, {34640, 1}, {34641, 1}, {34642, 1}, {34643, 1}, {34644, 1}, {34645, 1}, {34646, 1}, {34647, 1}, {34648, 1}};
		double[] chances = new double[]{11.55555, 11.01515, 11.16666, 11.86999, 11.19444, 11.55555, 30.55555, 25.55555, 27.55555, 31.55555, 28.55555, 34.55555, 31.55555, 33.55555, 34.55555, 23.55555, 32.55555, 19.55555, 20.55555, 25.55555, 31.55555, 32.55555, 11.55555, 11.55555, 11.55555, 11.55555, 11.55555, 11.55555};
		extractRandomOneItem(player, list, chances);
	}
	// --------------------------------------------------Hair Accessory end
	
	// --------------------------------------------------Awakening Chest
	private void use32264(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30310, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32265(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30311, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32266(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30312, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32267(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30313, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32268(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30314, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32269(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30315, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32270(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30316, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	private void use32271(Player player, boolean ctrl)
	{
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 30317, 1);
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33766, 5);
	}
	// --------------------------------------------------Awakening Chest end

	// -------------Aden Hero's Treasure Box
	private void use33771(Player player, boolean ctrl) //Red
	{
		Functions.addItem(player, 57, 5000000);
		Functions.addItem(player, 6622, 1);
		Functions.addItem(player, 9570, 1);
	}
	private void use33772(Player player, boolean ctrl) //Blue
	{
		Functions.addItem(player, 57, 5000000);
		Functions.addItem(player, 6622, 1);
		Functions.addItem(player, 9571, 1);
	}
	private void use33773(Player player, boolean ctrl) //Green
	{
		Functions.addItem(player, 57, 5000000);
		Functions.addItem(player, 6622, 1);
		Functions.addItem(player, 9572, 1);
	}
	// -------------Aden Hero's Treasure Box end

	// -------------Other Items
	private void use32241(Player player, boolean ctrl) //Adventures Box
	{
		int[][] list = new int[][]{{22024, 1}, {22026, 1}, {736, 1}, {13410, 1}, {13404, 1}, {13400, 1}, {13401, 1}, {13411, 1}, {13395, 1}, {13402, 1}, {13412, 1}, {13413, 1}, {10131, 1}, {10132, 1}, {10134, 1}, {10135, 1}, {10136, 1}, {10137, 1}, {10138, 1}, {22025, 1}};
		double[] chances = new double[]{39.01515, 11.16666, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 22.86999, 14.86999, 14.86999, 14.86999, 14.86999, 14.86999, 14.86999, 14.86999, 11.86999};
		extractRandomOneItem(player, list, chances);
	}
	// -------------Other Items

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