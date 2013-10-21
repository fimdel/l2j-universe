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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MultiSellList;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.XMLUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MultiSellHolder
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MultiSellHolder.class);
	/**
	 * Field _instance.
	 */
	private static MultiSellHolder _instance = new MultiSellHolder();
	
	/**
	 * Method getInstance.
	 * @return MultiSellHolder
	 */
	public static MultiSellHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field NODE_PRODUCTION. (value is ""production"")
	 */
	private static final String NODE_PRODUCTION = "production";
	/**
	 * Field NODE_INGRIDIENT. (value is ""ingredient"")
	 */
	private static final String NODE_INGRIDIENT = "ingredient";
	/**
	 * Field entries.
	 */
	private final TIntObjectHashMap<MultiSellListContainer> entries = new TIntObjectHashMap<>();
	
	/**
	 * Method getList.
	 * @param id int
	 * @return MultiSellListContainer
	 */
	public MultiSellListContainer getList(int id)
	{
		return entries.get(id);
	}
	
	/**
	 * Constructor for MultiSellHolder.
	 */
	public MultiSellHolder()
	{
		parseData();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		parseData();
	}
	
	/**
	 * Method parseData.
	 */
	private void parseData()
	{
		entries.clear();
		parse();
	}
	
	/**
	 * @author Mobius
	 */
	public static class MultiSellListContainer
	{
		/**
		 * Field _listId.
		 */
		private int _listId;
		/**
		 * Field _isnew.
		 */
		private boolean _isnew = false;
		/**
		 * Field _showall.
		 */
		private boolean _showall = true;
		/**
		 * Field keep_enchanted.
		 */
		private boolean keep_enchanted = false;
		/**
		 * Field is_dutyfree.
		 */
		private boolean is_dutyfree = false;
		/**
		 * Field nokey.
		 */
		private boolean nokey = false;
		/**
		 * Field entries.
		 */
		final List<MultiSellEntry> entries = new ArrayList<>();
		
		/**
		 * Method setListId.
		 * @param listId int
		 */
		public void setListId(int listId)
		{
			_listId = listId;
		}
		
		/**
		 * Method getListId.
		 * @return int
		 */
		public int getListId()
		{
			return _listId;
		}
		
		/**
		 * Method setIsNew.
		 * @param bool boolean
		 */
		public void setIsNew(boolean bool)
		{
			_isnew = bool;
		}
		
		/**
		 * Method isNew.
		 * @return boolean
		 */
		public boolean isNew()
		{
			return _isnew;
		}
		
		/**
		 * Method setShowAll.
		 * @param bool boolean
		 */
		public void setShowAll(boolean bool)
		{
			_showall = bool;
		}
		
		/**
		 * Method isShowAll.
		 * @return boolean
		 */
		public boolean isShowAll()
		{
			return _showall;
		}
		
		/**
		 * Method setNoTax.
		 * @param bool boolean
		 */
		public void setNoTax(boolean bool)
		{
			is_dutyfree = bool;
		}
		
		/**
		 * Method isNoTax.
		 * @return boolean
		 */
		public boolean isNoTax()
		{
			return is_dutyfree;
		}
		
		/**
		 * Method setNoKey.
		 * @param bool boolean
		 */
		public void setNoKey(boolean bool)
		{
			nokey = bool;
		}
		
		/**
		 * Method isNoKey.
		 * @return boolean
		 */
		public boolean isNoKey()
		{
			return nokey;
		}
		
		/**
		 * Method setKeepEnchant.
		 * @param bool boolean
		 */
		public void setKeepEnchant(boolean bool)
		{
			keep_enchanted = bool;
		}
		
		/**
		 * Method isKeepEnchant.
		 * @return boolean
		 */
		public boolean isKeepEnchant()
		{
			return keep_enchanted;
		}
		
		/**
		 * Method addEntry.
		 * @param e MultiSellEntry
		 */
		public void addEntry(MultiSellEntry e)
		{
			entries.add(e);
		}
		
		/**
		 * Method getEntries.
		 * @return List<MultiSellEntry>
		 */
		public List<MultiSellEntry> getEntries()
		{
			return entries;
		}
		
		/**
		 * Method isEmpty.
		 * @return boolean
		 */
		public boolean isEmpty()
		{
			return entries.isEmpty();
		}
	}
	
	/**
	 * Method hashFiles.
	 * @param dirname String
	 * @param hash List<File>
	 */
	private void hashFiles(String dirname, List<File> hash)
	{
		File dir = new File(Config.DATAPACK_ROOT, "data/xml/" + dirname);
		if (!dir.exists())
		{
			_log.info("Dir " + dir.getAbsolutePath() + " not exists");
			return;
		}
		File[] files = dir.listFiles();
		for (File f : files)
		{
			if (f.getName().endsWith(".xml"))
			{
				hash.add(f);
			}
			else if (f.isDirectory() && !f.getName().equals(".svn"))
			{
				hashFiles(dirname + "/" + f.getName(), hash);
			}
		}
	}
	
	/**
	 * Method addMultiSellListContainer.
	 * @param id int
	 * @param list MultiSellListContainer
	 */
	public void addMultiSellListContainer(int id, MultiSellListContainer list)
	{
		if (entries.containsKey(id))
		{
			_log.warn("MultiSell redefined: " + id);
		}
		list.setListId(id);
		entries.put(id, list);
	}
	
	/**
	 * Method remove.
	 * @param s String
	 * @return MultiSellListContainer
	 */
	public MultiSellListContainer remove(String s)
	{
		return remove(new File(s));
	}
	
	/**
	 * Method remove.
	 * @param f File
	 * @return MultiSellListContainer
	 */
	public MultiSellListContainer remove(File f)
	{
		return remove(Integer.parseInt(f.getName().replaceAll(".xml", "")));
	}
	
	/**
	 * Method remove.
	 * @param id int
	 * @return MultiSellListContainer
	 */
	public MultiSellListContainer remove(int id)
	{
		return entries.remove(id);
	}
	
	/**
	 * Method parseFile.
	 * @param f File
	 */
	public void parseFile(File f)
	{
		int id = 0;
		try
		{
			id = Integer.parseInt(f.getName().replaceAll(".xml", ""));
		}
		catch (Exception e)
		{
			_log.error("Error loading file " + f, e);
			return;
		}
		Document doc = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			doc = factory.newDocumentBuilder().parse(f);
		}
		catch (Exception e)
		{
			_log.error("Error loading file " + f, e);
			return;
		}
		try
		{
			addMultiSellListContainer(id, parseDocument(doc, id));
		}
		catch (Exception e)
		{
			_log.error("Error in file " + f, e);
		}
	}
	
	/**
	 * Method parse.
	 */
	private void parse()
	{
		List<File> files = new ArrayList<>();
		hashFiles("multisell", files);
		for (File f : files)
		{
			parseFile(f);
		}
	}
	
	/**
	 * Method parseDocument.
	 * @param doc Document
	 * @param id int
	 * @return MultiSellListContainer
	 */
	protected MultiSellListContainer parseDocument(Document doc, int id)
	{
		MultiSellListContainer list = new MultiSellListContainer();
		int entId = 1;
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("item".equalsIgnoreCase(d.getNodeName()))
					{
						MultiSellEntry e = parseEntry(d, id);
						if (e != null)
						{
							e.setEntryId(entId++);
							list.addEntry(e);
						}
					}
					else if ("config".equalsIgnoreCase(d.getNodeName()))
					{
						list.setIsNew(XMLUtil.getAttributeBooleanValue(d, "isnew", false));
						list.setShowAll(XMLUtil.getAttributeBooleanValue(d, "showall", true));
						list.setNoTax(XMLUtil.getAttributeBooleanValue(d, "notax", false));
						list.setKeepEnchant(XMLUtil.getAttributeBooleanValue(d, "keepenchanted", false));
						list.setNoKey(XMLUtil.getAttributeBooleanValue(d, "nokey", false));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Method parseEntry.
	 * @param n Node
	 * @param multiSellId int
	 * @return MultiSellEntry
	 */
	protected MultiSellEntry parseEntry(Node n, int multiSellId)
	{
		MultiSellEntry entry = new MultiSellEntry();
		for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
		{
			if (NODE_INGRIDIENT.equalsIgnoreCase(d.getNodeName()))
			{
				int id = Integer.parseInt(d.getAttributes().getNamedItem("id").getNodeValue());
				long count = Long.parseLong(d.getAttributes().getNamedItem("count").getNodeValue());
				MultiSellIngredient mi = new MultiSellIngredient(id, count);
				if (d.getAttributes().getNamedItem("enchant") != null)
				{
					mi.setItemEnchant(Integer.parseInt(d.getAttributes().getNamedItem("enchant").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("mantainIngredient") != null)
				{
					mi.setMantainIngredient(Boolean.parseBoolean(d.getAttributes().getNamedItem("mantainIngredient").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("fireAttr") != null)
				{
					mi.getItemAttributes().setFire(Integer.parseInt(d.getAttributes().getNamedItem("fireAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("waterAttr") != null)
				{
					mi.getItemAttributes().setWater(Integer.parseInt(d.getAttributes().getNamedItem("waterAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("earthAttr") != null)
				{
					mi.getItemAttributes().setEarth(Integer.parseInt(d.getAttributes().getNamedItem("earthAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("windAttr") != null)
				{
					mi.getItemAttributes().setWind(Integer.parseInt(d.getAttributes().getNamedItem("windAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("holyAttr") != null)
				{
					mi.getItemAttributes().setHoly(Integer.parseInt(d.getAttributes().getNamedItem("holyAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("unholyAttr") != null)
				{
					mi.getItemAttributes().setUnholy(Integer.parseInt(d.getAttributes().getNamedItem("unholyAttr").getNodeValue()));
				}
				entry.addIngredient(mi);
			}
			else if (NODE_PRODUCTION.equalsIgnoreCase(d.getNodeName()))
			{
				int id = Integer.parseInt(d.getAttributes().getNamedItem("id").getNodeValue());
				long count = Long.parseLong(d.getAttributes().getNamedItem("count").getNodeValue());
				MultiSellIngredient mi = new MultiSellIngredient(id, count);
				if (d.getAttributes().getNamedItem("enchant") != null)
				{
					mi.setItemEnchant(Integer.parseInt(d.getAttributes().getNamedItem("enchant").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("chance") != null)
				{
					mi.setChance(Integer.parseInt(d.getAttributes().getNamedItem("chance").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("fireAttr") != null)
				{
					mi.getItemAttributes().setFire(Integer.parseInt(d.getAttributes().getNamedItem("fireAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("waterAttr") != null)
				{
					mi.getItemAttributes().setWater(Integer.parseInt(d.getAttributes().getNamedItem("waterAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("earthAttr") != null)
				{
					mi.getItemAttributes().setEarth(Integer.parseInt(d.getAttributes().getNamedItem("earthAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("windAttr") != null)
				{
					mi.getItemAttributes().setWind(Integer.parseInt(d.getAttributes().getNamedItem("windAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("holyAttr") != null)
				{
					mi.getItemAttributes().setHoly(Integer.parseInt(d.getAttributes().getNamedItem("holyAttr").getNodeValue()));
				}
				if (d.getAttributes().getNamedItem("unholyAttr") != null)
				{
					mi.getItemAttributes().setUnholy(Integer.parseInt(d.getAttributes().getNamedItem("unholyAttr").getNodeValue()));
				}
				if (!Config.ALT_ALLOW_SHADOW_WEAPONS && (id > 0))
				{
					ItemTemplate item = ItemHolder.getInstance().getTemplate(id);
					if ((item != null) && item.isShadowItem() && item.isWeapon() && !Config.ALT_ALLOW_SHADOW_WEAPONS)
					{
						return null;
					}
				}
				entry.addProduct(mi);
			}
		}
		if (entry.getIngredients().isEmpty() || entry.getProduction().isEmpty())
		{
			_log.warn("MultiSell [" + multiSellId + "] is empty!");
			return null;
		}
		if ((entry.getIngredients().size() == 1) && (entry.getProduction().size() == 1) && (entry.getIngredients().get(0).getItemId() == 57))
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(entry.getProduction().get(0).getItemId());
			if (item == null)
			{
				_log.warn("MultiSell [" + multiSellId + "] Production [" + entry.getProduction().get(0).getItemId() + "] not found!");
				return null;
			}
			if ((entry.getIngredients().get(0).getItemId() == 57 &&  item.getReferencePrice() > entry.getIngredients().get(0).getItemCount()) && !Config.LOGIN_SERVER_IS_PVP)
			{
				_log.warn("MultiSell [" + multiSellId + "] Production '" + item.getName() + "' [" + entry.getProduction().get(0).getItemId() + "] price is lower than referenced | " + item.getReferencePrice() + " > " + entry.getIngredients().get(0).getItemCount());
			}
		}
		return entry;
	}
	
	/**
	 * Method parseItemIdAndCount.
	 * @param s String
	 * @return long[]
	 */
	private static long[] parseItemIdAndCount(String s)
	{
		if ((s == null) || s.isEmpty())
		{
			return null;
		}
		String[] a = s.split(":");
		try
		{
			long id = Integer.parseInt(a[0]);
			long count = a.length > 1 ? Long.parseLong(a[1]) : 1;
			return new long[]
			{
				id,
				count
			};
		}
		catch (Exception e)
		{
			_log.error("", e);
			return null;
		}
	}
	
	/**
	 * Method parseEntryFromStr.
	 * @param s String
	 * @return MultiSellEntry
	 */
	public static MultiSellEntry parseEntryFromStr(String s)
	{
		if ((s == null) || s.isEmpty())
		{
			return null;
		}
		String[] a = s.split("->");
		if (a.length != 2)
		{
			return null;
		}
		long[] ingredient, production;
		if (((ingredient = parseItemIdAndCount(a[0])) == null) || ((production = parseItemIdAndCount(a[1])) == null))
		{
			return null;
		}
		MultiSellEntry entry = new MultiSellEntry();
		entry.addIngredient(new MultiSellIngredient((int) ingredient[0], ingredient[1]));
		entry.addProduct(new MultiSellIngredient((int) production[0], production[1]));
		return entry;
	}
	
	/**
	 * Method SeparateAndSend.
	 * @param listId int
	 * @param player Player
	 * @param taxRate double
	 */
	public void SeparateAndSend(int listId, Player player, double taxRate)
	{
		for (int i : Config.ALT_DISABLED_MULTISELL)
		{
			if (i == listId)
			{
				player.sendMessage(new CustomMessage("common.Disabled", player));
				return;
			}
		}
		MultiSellListContainer list = getList(listId);
		if (list == null)
		{
			player.sendMessage(new CustomMessage("common.Disabled", player));
			return;
		}
		SeparateAndSend(list, player, taxRate);
	}
	
	/**
	 * Method SeparateAndSend.
	 * @param list MultiSellListContainer
	 * @param player Player
	 * @param taxRate double
	 */
	public void SeparateAndSend(MultiSellListContainer list, Player player, double taxRate)
	{
		list = generateMultiSell(list, player, taxRate);
		MultiSellListContainer temp = new MultiSellListContainer();
		int page = 1;
		temp.setListId(list.getListId());
		player.setMultisell(list);
		for (MultiSellEntry e : list.getEntries())
		{
			if (temp.getEntries().size() == Config.MULTISELL_SIZE)
			{
				player.sendPacket(new MultiSellList(temp, page, 0));
				page++;
				temp = new MultiSellListContainer();
				temp.setListId(list.getListId());
			}
			temp.addEntry(e);
		}
		player.sendPacket(new MultiSellList(temp, page, 1));
	}
	
	/**
	 * Method generateMultiSell.
	 * @param container MultiSellListContainer
	 * @param player Player
	 * @param taxRate double
	 * @return MultiSellListContainer
	 */
	private MultiSellListContainer generateMultiSell(MultiSellListContainer container, Player player, double taxRate)
	{
		MultiSellListContainer list = new MultiSellListContainer();
		list.setListId(container.getListId());
		boolean enchant = container.isKeepEnchant();
		boolean notax = container.isNoTax();
		boolean showall = container.isShowAll();
		boolean nokey = container.isNoKey();
		list.setShowAll(showall);
		list.setKeepEnchant(enchant);
		list.setNoTax(notax);
		list.setNoKey(nokey);
		ItemInstance[] items = player.getInventory().getItems();
		for (MultiSellEntry origEntry : container.getEntries())
		{
			MultiSellEntry ent = origEntry.clone();
			List<MultiSellIngredient> ingridients;
			if (!notax && (taxRate > 0.))
			{
				double tax = 0;
				long adena = 0;
				ingridients = new ArrayList<>(ent.getIngredients().size() + 1);
				for (MultiSellIngredient i : ent.getIngredients())
				{
					if (i.getItemId() == 57)
					{
						adena += i.getItemCount();
						tax += i.getItemCount() * taxRate;
						continue;
					}
					ingridients.add(i);
					if (i.getItemId() == ItemTemplate.ITEM_ID_CLAN_REPUTATION_SCORE)
					{
						tax += (i.getItemCount() / 120) * 1000 * taxRate * 100;
					}
					if (i.getItemId() < 1)
					{
						continue;
					}
					ItemTemplate item = ItemHolder.getInstance().getTemplate(i.getItemId());
					if (item.isStackable())
					{
						tax += item.getReferencePrice() * i.getItemCount() * taxRate;
					}
				}
				adena = Math.round(adena + tax);
				if (adena > 0)
				{
					ingridients.add(new MultiSellIngredient(57, adena));
				}
				ent.setTax(Math.round(tax));
				ent.getIngredients().clear();
				ent.getIngredients().addAll(ingridients);
			}
			else
			{
				ingridients = ent.getIngredients();
			}
			if (showall)
			{
				list.entries.add(ent);
			}
			else
			{
				List<Integer> itms = new ArrayList<>();
				for (MultiSellIngredient ingredient : ingridients)
				{
					ItemTemplate template = ingredient.getItemId() <= 0 ? null : ItemHolder.getInstance().getTemplate(ingredient.getItemId());
					if ((ingredient.getItemId() <= 0) || nokey || template.isEquipment())
					{
						if (ingredient.getItemId() == 12374)
						{
							continue;
						}
						if (ingredient.getItemId() == ItemTemplate.ITEM_ID_CLAN_REPUTATION_SCORE)
						{
							if (!itms.contains(ingredient.getItemId()) && (player.getClan() != null) && (player.getClan().getReputationScore() >= ingredient.getItemCount()))
							{
								itms.add(ingredient.getItemId());
							}
							continue;
						}
						else if (ingredient.getItemId() == ItemTemplate.ITEM_ID_PC_BANG_POINTS)
						{
							if (!itms.contains(ingredient.getItemId()) && (player.getPcBangPoints() >= ingredient.getItemCount()))
							{
								itms.add(ingredient.getItemId());
							}
							continue;
						}
						else if (ingredient.getItemId() == ItemTemplate.ITEM_ID_FAME)
						{
							if (!itms.contains(ingredient.getItemId()) && (player.getFame() >= ingredient.getItemCount()))
							{
								itms.add(ingredient.getItemId());
							}
							continue;
						}
						for (final ItemInstance item : items)
						{
							if ((item.getItemId() == ingredient.getItemId()) && item.canBeExchanged(player))
							{
								if (itms.contains(enchant ? ingredient.getItemId() + (ingredient.getItemEnchant() * 100000L) : ingredient.getItemId()))
								{
									continue;
								}
								if (item.getEnchantLevel() < ingredient.getItemEnchant())
								{
									continue;
								}
								if (item.isStackable() && (item.getCount() < ingredient.getItemCount()))
								{
									break;
								}
								itms.add(enchant ? ingredient.getItemId() + (ingredient.getItemEnchant() * 100000) : ingredient.getItemId());
								MultiSellEntry possibleEntry = new MultiSellEntry(enchant ? ent.getEntryId() + (item.getEnchantLevel() * 100000) : ent.getEntryId());
								for (MultiSellIngredient p : ent.getProduction())
								{
									if (enchant && template.canBeEnchanted())
									{
										p.setItemEnchant(item.getEnchantLevel());
										p.setItemAttributes(item.getAttributes().clone());
									}
									possibleEntry.addProduct(p);
								}
								for (MultiSellIngredient ig : ingridients)
								{
									if (enchant && (ig.getItemId() > 0) && ItemHolder.getInstance().getTemplate(ig.getItemId()).canBeEnchanted())
									{
										ig.setItemEnchant(item.getEnchantLevel());
										ig.setItemAttributes(item.getAttributes().clone());
									}
									possibleEntry.addIngredient(ig);
								}
								list.entries.add(possibleEntry);
								break;
							}
						}
					}
				}
			}
		}
		return list;
	}
}
