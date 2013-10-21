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
package lineage2.gameserver.tables;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.etcitems.LifeStoneGrade;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.triggers.TriggerInfo;
import lineage2.gameserver.stats.triggers.TriggerType;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AugmentationData
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AugmentationData.class);
	/**
	 * Field _Instance.
	 */
	private static AugmentationData _Instance;
	
	/**
	 * Method getInstance.
	 * @return AugmentationData
	 */
	public static AugmentationData getInstance()
	{
		if (_Instance == null)
		{
			_Instance = new AugmentationData();
		}
		return _Instance;
	}
	
	/**
	 * Field STAT_BLOCKSIZE. (value is 3640)
	 */
	private static final int STAT_BLOCKSIZE = 3640;
	/**
	 * Field STAT_SUBBLOCKSIZE. (value is 91)
	 */
	private static final int STAT_SUBBLOCKSIZE = 91;
	/**
	 * Field STAT_NUM. (value is 13)
	 */
	private static final int STAT_NUM = 13;
	/**
	 * Field STATS1_MAP.
	 */
	private static final byte[] STATS1_MAP = new byte[STAT_SUBBLOCKSIZE];
	/**
	 * Field STATS2_MAP.
	 */
	private static final byte[] STATS2_MAP = new byte[STAT_SUBBLOCKSIZE];
	/**
	 * Field BLUE_START. (value is 14561)
	 */
	private static final int BLUE_START = 14561;
	/**
	 * Field SKILLS_BLOCKSIZE. (value is 178)
	 */
	private static final int SKILLS_BLOCKSIZE = 178;
	/**
	 * Field BASESTAT_STR. (value is 16341)
	 */
	private static final int BASESTAT_STR = 16341;
	/**
	 * Field BASESTAT_MEN. (value is 16344)
	 */
	private static final int BASESTAT_MEN = 16344;
	/**
	 * Field ACC_START. (value is 16669)
	 */
	private static final int ACC_START = 16669;
	/**
	 * Field ACC_BLOCKS_NUM. (value is 10)
	 */
	private static final int ACC_BLOCKS_NUM = 10;
	/**
	 * Field ACC_STAT_SUBBLOCKSIZE. (value is 21)
	 */
	private static final int ACC_STAT_SUBBLOCKSIZE = 21;
	/**
	 * Field ACC_STAT_NUM. (value is 6)
	 */
	private static final int ACC_STAT_NUM = 6;
	/**
	 * Field ACC_RING_START.
	 */
	private static final int ACC_RING_START = ACC_START;
	/**
	 * Field ACC_RING_SKILLS. (value is 18)
	 */
	private static final int ACC_RING_SKILLS = 18;
	/**
	 * Field ACC_RING_BLOCKSIZE.
	 */
	private static final int ACC_RING_BLOCKSIZE = ACC_RING_SKILLS + (4 * ACC_STAT_SUBBLOCKSIZE);
	/**
	 * Field ACC_RING_END.
	 */
	private static final int ACC_RING_END = (ACC_RING_START + (ACC_BLOCKS_NUM * ACC_RING_BLOCKSIZE)) - 1;
	/**
	 * Field ACC_EAR_START.
	 */
	private static final int ACC_EAR_START = ACC_RING_END + 1;
	/**
	 * Field ACC_EAR_SKILLS. (value is 18)
	 */
	private static final int ACC_EAR_SKILLS = 18;
	/**
	 * Field ACC_EAR_BLOCKSIZE.
	 */
	private static final int ACC_EAR_BLOCKSIZE = ACC_EAR_SKILLS + (4 * ACC_STAT_SUBBLOCKSIZE);
	/**
	 * Field ACC_EAR_END.
	 */
	private static final int ACC_EAR_END = (ACC_EAR_START + (ACC_BLOCKS_NUM * ACC_EAR_BLOCKSIZE)) - 1;
	/**
	 * Field ACC_NECK_START.
	 */
	private static final int ACC_NECK_START = ACC_EAR_END + 1;
	/**
	 * Field ACC_NECK_SKILLS. (value is 24)
	 */
	private static final int ACC_NECK_SKILLS = 24;
	/**
	 * Field ACC_NECK_BLOCKSIZE.
	 */
	private static final int ACC_NECK_BLOCKSIZE = ACC_NECK_SKILLS + (4 * ACC_STAT_SUBBLOCKSIZE);
	/**
	 * Field ACC_STATS1_MAP.
	 */
	private static final byte[] ACC_STATS1_MAP = new byte[ACC_STAT_SUBBLOCKSIZE];
	/**
	 * Field ACC_STATS2_MAP.
	 */
	private static final byte[] ACC_STATS2_MAP = new byte[ACC_STAT_SUBBLOCKSIZE];
	/**
	 * Field _augStats.
	 */
	private final List<?>[] _augStats = new ArrayList[4];
	/**
	 * Field _augAccStats.
	 */
	private final List<?>[] _augAccStats = new ArrayList[4];
	/**
	 * Field _blueSkills.
	 */
	private final List<?>[] _blueSkills = new ArrayList[10];
	/**
	 * Field _purpleSkills.
	 */
	private final List<?>[] _purpleSkills = new ArrayList[10];
	/**
	 * Field _redSkills.
	 */
	private final List<?>[] _redSkills = new ArrayList[10];
	/**
	 * Field _yellowSkills.
	 */
	private final List<?>[] _yellowSkills = new ArrayList[10];
	/**
	 * Field _allSkills.
	 */
	private final TIntObjectHashMap<TriggerInfo> _allSkills = new TIntObjectHashMap<>();
	
	/**
	 * Constructor for AugmentationData.
	 */
	public AugmentationData()
	{
		_log.info("Initializing AugmentationData.");
		_augStats[0] = new ArrayList<augmentationStat>();
		_augStats[1] = new ArrayList<augmentationStat>();
		_augStats[2] = new ArrayList<augmentationStat>();
		_augStats[3] = new ArrayList<augmentationStat>();
		_augAccStats[0] = new ArrayList<augmentationStat>();
		_augAccStats[1] = new ArrayList<augmentationStat>();
		_augAccStats[2] = new ArrayList<augmentationStat>();
		_augAccStats[3] = new ArrayList<augmentationStat>();
		int idx;
		for (idx = 0; idx < STAT_NUM; idx++)
		{
			STATS1_MAP[idx] = (byte) idx;
			STATS2_MAP[idx] = (byte) idx;
		}
		for (int i = 0; i < STAT_NUM; i++)
		{
			for (int j = i + 1; j < STAT_NUM; idx++, j++)
			{
				STATS1_MAP[idx] = (byte) i;
				STATS2_MAP[idx] = (byte) j;
			}
		}
		idx = 0;
		for (int i = 0; i < (ACC_STAT_NUM - 2); i++)
		{
			for (int j = i; j < ACC_STAT_NUM; idx++, j++)
			{
				ACC_STATS1_MAP[idx] = (byte) i;
				ACC_STATS2_MAP[idx] = (byte) j;
			}
		}
		ACC_STATS1_MAP[idx] = 4;
		ACC_STATS2_MAP[idx++] = 4;
		ACC_STATS1_MAP[idx] = 5;
		ACC_STATS2_MAP[idx++] = 5;
		ACC_STATS1_MAP[idx] = 4;
		ACC_STATS2_MAP[idx] = 5;
		for (int i = 0; i < 10; i++)
		{
			_blueSkills[i] = new ArrayList<Integer>();
			_purpleSkills[i] = new ArrayList<Integer>();
			_redSkills[i] = new ArrayList<Integer>();
			_yellowSkills[i] = new ArrayList<Integer>();
		}
		load();
		_log.info("AugmentationData: Loaded: " + (_augStats[0].size() * 4) + " augmentation stats.");
		_log.info("AugmentationData: Loaded: " + (_augAccStats[0].size() * 4) + " accessory augmentation stats.");
		for (int i = 0; i < 10; i++)
		{
			_log.info("AugmentationData: Loaded: " + _blueSkills[i].size() + " blue, " + _purpleSkills[i].size() + " purple and " + _redSkills[i].size() + " red skills for lifeStoneLevel " + i);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class augmentationStat
	{
		/**
		 * Field _stat.
		 */
		private final Stats _stat;
		/**
		 * Field _singleSize.
		 */
		private final int _singleSize;
		/**
		 * Field _combinedSize.
		 */
		private final int _combinedSize;
		/**
		 * Field _singleValues.
		 */
		private final double _singleValues[];
		/**
		 * Field _combinedValues.
		 */
		private final double _combinedValues[];
		
		/**
		 * Constructor for augmentationStat.
		 * @param stat Stats
		 * @param sValues double[]
		 * @param cValues double[]
		 */
		public augmentationStat(Stats stat, double sValues[], double cValues[])
		{
			_stat = stat;
			_singleSize = sValues.length;
			_singleValues = sValues;
			_combinedSize = cValues.length;
			_combinedValues = cValues;
		}
		
		/**
		 * Method getSingleStatSize.
		 * @return int
		 */
		public int getSingleStatSize()
		{
			return _singleSize;
		}
		
		/**
		 * Method getCombinedStatSize.
		 * @return int
		 */
		public int getCombinedStatSize()
		{
			return _combinedSize;
		}
		
		/**
		 * Method getSingleStatValue.
		 * @param i int
		 * @return double
		 */
		public double getSingleStatValue(int i)
		{
			if ((i >= _singleSize) || (i < 0))
			{
				return _singleValues[_singleSize - 1];
			}
			return _singleValues[i];
		}
		
		/**
		 * Method getCombinedStatValue.
		 * @param i int
		 * @return double
		 */
		public double getCombinedStatValue(int i)
		{
			if ((i >= _combinedSize) || (i < 0))
			{
				return _combinedValues[_combinedSize - 1];
			}
			return _combinedValues[i];
		}
		
		/**
		 * Method getStat.
		 * @return Stats
		 */
		public Stats getStat()
		{
			return _stat;
		}
	}
	
	/**
	 * Method load.
	 */
	@SuppressWarnings("unchecked")
	private final void load()
	{
		try
		{
			int badAugmantData = 0;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/stats/augmentation/augmentation_skillmap.xml");
			
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("augmentation".equalsIgnoreCase(d.getNodeName()))
						{
							NamedNodeMap attrs = d.getAttributes();
							int skillId = 0, augmentationId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							int skillLvL = 0;
							String type = "blue";
							TriggerType t = null;
							double chance = 0;
							for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
							{
								attrs = cd.getAttributes();
								if ("skillId".equalsIgnoreCase(cd.getNodeName()))
								{
									skillId = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
								}
								else if ("skillLevel".equalsIgnoreCase(cd.getNodeName()))
								{
									skillLvL = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
								}
								else if ("type".equalsIgnoreCase(cd.getNodeName()))
								{
									type = attrs.getNamedItem("val").getNodeValue();
								}
								else if ("trigger_type".equalsIgnoreCase(cd.getNodeName()))
								{
									t = TriggerType.valueOf(attrs.getNamedItem("val").getNodeValue());
								}
								else if ("trigger_chance".equalsIgnoreCase(cd.getNodeName()))
								{
									chance = Double.parseDouble(attrs.getNamedItem("val").getNodeValue());
								}
							}
							if (skillId == 0)
							{
								badAugmantData++;
								continue;
							}
							else if (skillLvL == 0)
							{
								badAugmantData++;
								continue;
							}
							int k = (augmentationId - BLUE_START) / SKILLS_BLOCKSIZE;
							if (type.equalsIgnoreCase("blue"))
							{
								((List<Integer>) _blueSkills[k]).add(augmentationId);
							}
							else if (type.equalsIgnoreCase("purple"))
							{
								((List<Integer>) _purpleSkills[k]).add(augmentationId);
							}
							else if (type.equalsIgnoreCase("red"))
							{
								((List<Integer>) _redSkills[k]).add(augmentationId);
							}
							_allSkills.put(augmentationId, new TriggerInfo(skillId, skillLvL, t, chance));
						}
					}
				}
			}
			if (badAugmantData != 0)
			{
				_log.info("AugmentationData: " + badAugmantData + " bad skill(s) were skipped.");
			}
		}
		catch (Exception e)
		{
			_log.error("Error parsing augmentation_skillmap.xml.", e);
			return;
		}
		for (int i = 1; i < 5; i++)
		{
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringComments(true);
				File file = new File(Config.DATAPACK_ROOT, "data/xml/stats/augmentation/augmentation_stats" + i + ".xml");
				Document doc = factory.newDocumentBuilder().parse(file);
				for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("list".equalsIgnoreCase(n.getNodeName()))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							if ("stat".equalsIgnoreCase(d.getNodeName()))
							{
								NamedNodeMap attrs = d.getAttributes();
								String statName = attrs.getNamedItem("name").getNodeValue();
								double soloValues[] = null, combinedValues[] = null;
								for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
								{
									if ("table".equalsIgnoreCase(cd.getNodeName()))
									{
										attrs = cd.getAttributes();
										String tableName = attrs.getNamedItem("name").getNodeValue();
										StringTokenizer data = new StringTokenizer(cd.getFirstChild().getNodeValue());
										TDoubleArrayList array = new TDoubleArrayList();
										while (data.hasMoreTokens())
										{
											array.add(Double.parseDouble(data.nextToken()));
										}
										if (tableName.equalsIgnoreCase("#soloValues"))
										{
											soloValues = new double[array.size()];
											int x = 0;
											for (double value : array.toArray())
											{
												soloValues[x++] = value;
											}
										}
										else
										{
											combinedValues = new double[array.size()];
											int x = 0;
											for (double value : array.toArray())
											{
												combinedValues[x++] = value;
											}
										}
									}
								}
								((List<augmentationStat>) _augStats[i - 1]).add(new augmentationStat(Stats.valueOfXml(statName), soloValues, combinedValues));
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.error("Error parsing augmentation_stats" + i + ".xml.", e);
				return;
			}
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringComments(true);
				File file = new File(Config.DATAPACK_ROOT, "data/xml/stats/augmentation/augmentation_jewel_stats" + i + ".xml");
				Document doc = factory.newDocumentBuilder().parse(file);
				for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("list".equalsIgnoreCase(n.getNodeName()))
					{
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
						{
							if ("stat".equalsIgnoreCase(d.getNodeName()))
							{
								NamedNodeMap attrs = d.getAttributes();
								String statName = attrs.getNamedItem("name").getNodeValue();
								double soloValues[] = null, combinedValues[] = null;
								for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
								{
									if ("table".equalsIgnoreCase(cd.getNodeName()))
									{
										attrs = cd.getAttributes();
										String tableName = attrs.getNamedItem("name").getNodeValue();
										StringTokenizer data = new StringTokenizer(cd.getFirstChild().getNodeValue());
										TDoubleArrayList array = new TDoubleArrayList();
										while (data.hasMoreTokens())
										{
											array.add(Double.parseDouble(data.nextToken()));
										}
										if (tableName.equalsIgnoreCase("#soloValues"))
										{
											soloValues = new double[array.size()];
											int x = 0;
											for (double value : array.toArray())
											{
												soloValues[x++] = value;
											}
										}
										else
										{
											combinedValues = new double[array.size()];
											int x = 0;
											for (double value : array.toArray())
											{
												combinedValues[x++] = value;
											}
										}
									}
								}
								((List<augmentationStat>) _augAccStats[i - 1]).add(new augmentationStat(Stats.valueOfXml(statName), soloValues, combinedValues));
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.error("Error parsing jewel augmentation_stats" + i + ".xml.", e);
				return;
			}
		}
	}
	
	/**
	 * Method generateRandomAugmentation.
	 * @param lifeStoneLevel int
	 * @param lifeStoneGrade LifeStoneGrade
	 * @param bodyPart int
	 * @return int
	 */
	public int generateRandomAugmentation(int lifeStoneLevel, LifeStoneGrade lifeStoneGrade, int bodyPart)
	{
		int lvl = (lifeStoneLevel - 46) / 3;
		lvl = Math.min(lvl, 10) - 1;
		switch (bodyPart)
		{
			case ItemTemplate.SLOT_L_FINGER:
			case ItemTemplate.SLOT_R_FINGER:
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
			case ItemTemplate.SLOT_L_EAR:
			case ItemTemplate.SLOT_R_EAR:
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
			case ItemTemplate.SLOT_NECK:
				return generateRandomAccessoryAugmentation(lvl, bodyPart);
			default:
				return generateRandomWeaponAugmentation(lvl, lifeStoneGrade);
		}
	}
	
	/**
	 * Method generateRandomWeaponAugmentation.
	 * @param lifeStoneLevel int
	 * @param lifeStoneGrade LifeStoneGrade
	 * @return int
	 */
	private int generateRandomWeaponAugmentation(int lifeStoneLevel, LifeStoneGrade lifeStoneGrade)
	{
		int stat12 = 0;
		int stat34 = 0;
		boolean generateSkill = false;
		boolean generateGlow = false;
		lifeStoneLevel = Math.min(lifeStoneLevel, 9);
		switch (lifeStoneGrade)
		{
			case LOW:
				generateSkill = Rnd.chance(Config.AUGMENTATION_NG_SKILL_CHANCE);
				generateGlow = Rnd.chance(Config.AUGMENTATION_NG_GLOW_CHANCE);
				break;
			case MIDDLE:
				generateSkill = Rnd.chance(Config.AUGMENTATION_MID_SKILL_CHANCE);
				generateGlow = Rnd.chance(Config.AUGMENTATION_MID_GLOW_CHANCE);
				break;
			case HIGHT:
				generateSkill = Rnd.chance(Config.AUGMENTATION_HIGH_SKILL_CHANCE);
				generateGlow = Rnd.chance(Config.AUGMENTATION_HIGH_GLOW_CHANCE);
				break;
			case TOP:
				generateSkill = Rnd.chance(Config.AUGMENTATION_TOP_SKILL_CHANCE);
				generateGlow = Rnd.chance(Config.AUGMENTATION_TOP_GLOW_CHANCE);
				break;
			default:
				break;
		}
		if (!generateSkill && (Rnd.get(1, 100) <= Config.AUGMENTATION_BASESTAT_CHANCE))
		{
			stat34 = Rnd.get(BASESTAT_STR, BASESTAT_MEN);
		}
		int resultColor = Rnd.get(0, 100);
		if ((stat34 == 0) && !generateSkill)
		{
			if (resultColor <= ((15 * lifeStoneGrade.ordinal()) + 40))
			{
				resultColor = 1;
			}
			else
			{
				resultColor = 0;
			}
		}
		else if ((resultColor <= ((10 * lifeStoneGrade.ordinal()) + 5)) || (stat34 != 0))
		{
			resultColor = 3;
		}
		else if (resultColor <= ((10 * lifeStoneGrade.ordinal()) + 10))
		{
			resultColor = 1;
		}
		else
		{
			resultColor = 2;
		}
		if (generateSkill)
		{
			switch (resultColor)
			{
				case 1:
					stat34 = (Integer) _blueSkills[lifeStoneLevel].get(Rnd.get(0, _blueSkills[lifeStoneLevel].size() - 1));
					break;
				case 2:
					stat34 = (Integer) _purpleSkills[lifeStoneLevel].get(Rnd.get(0, _purpleSkills[lifeStoneLevel].size() - 1));
					break;
				case 3:
					stat34 = (Integer) _redSkills[lifeStoneLevel].get(Rnd.get(0, _redSkills[lifeStoneLevel].size() - 1));
					break;
			}
		}
		int offset;
		if (stat34 == 0)
		{
			int temp = Rnd.get(2, 3);
			int colorOffset = (resultColor * 10 * STAT_SUBBLOCKSIZE) + (temp * STAT_BLOCKSIZE) + 1;
			offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + colorOffset;
			stat34 = Rnd.get(offset, (offset + STAT_SUBBLOCKSIZE) - 1);
			if (generateGlow && (lifeStoneGrade.ordinal() >= 2))
			{
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + ((temp - 2) * STAT_BLOCKSIZE) + (lifeStoneGrade.ordinal() * 10 * STAT_SUBBLOCKSIZE) + 1;
			}
			else
			{
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + ((temp - 2) * STAT_BLOCKSIZE) + (Rnd.get(0, 1) * 10 * STAT_SUBBLOCKSIZE) + 1;
			}
		}
		else if (!generateGlow)
		{
			offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (Rnd.get(0, 1) * STAT_BLOCKSIZE) + 1;
		}
		else
		{
			offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (Rnd.get(0, 1) * STAT_BLOCKSIZE) + (((lifeStoneGrade.ordinal() + resultColor) / 2) * 10 * STAT_SUBBLOCKSIZE) + 1;
		}
		stat12 = Rnd.get(offset, (offset + STAT_SUBBLOCKSIZE) - 1);
		return (stat34 << 16) + stat12;
	}
	
	/**
	 * Method generateRandomAccessoryAugmentation.
	 * @param lifeStoneLevel int
	 * @param bodyPart int
	 * @return int
	 */
	private int generateRandomAccessoryAugmentation(int lifeStoneLevel, int bodyPart)
	{
		int stat12 = 0;
		int stat34 = 0;
		int base = 0;
		int skillsLength = 0;
		lifeStoneLevel = Math.min(lifeStoneLevel, 9);
		switch (bodyPart)
		{
			case ItemTemplate.SLOT_L_FINGER:
			case ItemTemplate.SLOT_R_FINGER:
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
				base = ACC_RING_START + (ACC_RING_BLOCKSIZE * lifeStoneLevel);
				skillsLength = ACC_RING_SKILLS;
				break;
			case ItemTemplate.SLOT_L_EAR:
			case ItemTemplate.SLOT_R_EAR:
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
				base = ACC_EAR_START + (ACC_EAR_BLOCKSIZE * lifeStoneLevel);
				skillsLength = ACC_EAR_SKILLS;
				break;
			case ItemTemplate.SLOT_NECK:
				base = ACC_NECK_START + (ACC_NECK_BLOCKSIZE * lifeStoneLevel);
				skillsLength = ACC_NECK_SKILLS;
				break;
			default:
				return 0;
		}
		int resultColor = Rnd.get(0, 3);
		TriggerInfo triggerInfo = null;
		stat12 = Rnd.get(ACC_STAT_SUBBLOCKSIZE);
		if (Rnd.get(1, 100) <= Config.AUGMENTATION_ACC_SKILL_CHANCE)
		{
			stat34 = base + Rnd.get(skillsLength);
			triggerInfo = _allSkills.get(stat34);
		}
		if (triggerInfo == null)
		{
			stat34 = (stat12 + 1 + Rnd.get(ACC_STAT_SUBBLOCKSIZE - 1)) % ACC_STAT_SUBBLOCKSIZE;
			stat34 = base + skillsLength + (ACC_STAT_SUBBLOCKSIZE * resultColor) + stat34;
		}
		stat12 = base + skillsLength + (ACC_STAT_SUBBLOCKSIZE * resultColor) + stat12;
		return (stat34 << 16) + stat12;
	}
}
