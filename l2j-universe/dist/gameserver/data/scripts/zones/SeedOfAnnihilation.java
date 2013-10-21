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
package zones;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeedOfAnnihilation implements ScriptFile
{
	/**
	 * Field ANNIHILATION_FURNACE. (value is 18928)
	 */
	private static final int ANNIHILATION_FURNACE = 18928;
	/**
	 * Field ZONE_BUFFS_LIST.
	 */
	static final int[][] ZONE_BUFFS_LIST =
	{
		{
			1,
			2,
			3
		},
		{
			1,
			3,
			2
		},
		{
			2,
			1,
			3
		},
		{
			2,
			3,
			1
		},
		{
			3,
			2,
			1
		},
		{
			3,
			1,
			2
		}
	};
	/**
	 * Field _teleportZones.
	 */
	static final Map<String, Location> _teleportZones = new HashMap<>();
	static
	{
		_teleportZones.put("[14_23_telzone_to_cocracon]", new Location(-213175, 182648, -10992));
		_teleportZones.put("[14_23_telzone_to_raptilicon]", new Location(-180211, 182984, -15152));
		_teleportZones.put("[13_23_telzone_from_cocracon]", new Location(-181217, 186711, -10528));
		_teleportZones.put("[14_23_telzone_from_raptilicon]", new Location(-179275, 186802, -10720));
	}
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener;
	/**
	 * Field _regionsData.
	 */
	final SeedRegion[] _regionsData = new SeedRegion[3];
	/**
	 * Field _seedsNextStatusChange.
	 */
	Long _seedsNextStatusChange;
	
	/**
	 * @author Mobius
	 */
	private class SeedRegion
	{
		/**
		 * Field buff_zone_pc.
		 */
		public String[] buff_zone_pc;
		/**
		 * Field buff_zone_npc.
		 */
		public String[] buff_zone_npc;
		/**
		 * Field af_spawns.
		 */
		public int[][] af_spawns;
		/**
		 * Field af_npcs.
		 */
		public NpcInstance[] af_npcs = new NpcInstance[2];
		/**
		 * Field activeBuff.
		 */
		public int activeBuff = 0;
		
		/**
		 * Constructor for SeedRegion.
		 * @param bz_pc String[]
		 * @param bz_npc String[]
		 * @param as int[][]
		 */
		public SeedRegion(String[] bz_pc, String[] bz_npc, int[][] as)
		{
			buff_zone_pc = bz_pc;
			buff_zone_npc = bz_npc;
			af_spawns = as;
		}
	}
	
	/**
	 * Method loadSeedRegionData.
	 */
	public void loadSeedRegionData()
	{
		_zoneListener = new ZoneListener();
		if ((_teleportZones != null) && !_teleportZones.isEmpty())
		{
			for (String s : _teleportZones.keySet())
			{
				Zone zone = ReflectionUtils.getZone(s);
				zone.addListener(_zoneListener);
			}
		}
		_regionsData[0] = new SeedRegion(new String[]
		{
			"[14_23_beastacon_for_melee_for_pc]",
			"[14_23_beastacon_for_archer_for_pc]",
			"[14_23_beastacon_for_mage_for_pc]"
		}, new String[]
		{
			"[14_23_beastacon_for_melee]",
			"[14_23_beastacon_for_archer]",
			"[14_23_beastacon_for_mage]"
		}, new int[][]
		{
			{
				-180450,
				185507,
				-10574,
				11632
			},
			{
				-180005,
				185489,
				-10577,
				11632
			}
		});
		_regionsData[1] = new SeedRegion(new String[]
		{
			"[14_23_raptilicon_for_melee_for_pc]",
			"[14_23_raptilicon_for_archer_for_pc]",
			"[14_23_raptilicon_for_mage_for_pc]"
		}, new String[]
		{
			"[14_23_raptilicon_for_melee]",
			"[14_23_raptilicon_for_archer]",
			"[14_23_raptilicon_for_mage]"
		}, new int[][]
		{
			{
				-179600,
				186998,
				-10737,
				11632
			},
			{
				-179295,
				186444,
				-10733,
				11632
			}
		});
		_regionsData[2] = new SeedRegion(new String[]
		{
			"[13_23_cocracon_for_melee_for_pc]",
			"[13_23_cocracon_for_archer_for_pc]",
			"[13_23_cocracon_for_mage_for_pc]"
		}, new String[]
		{
			"[13_23_cocracon_for_melee]",
			"[13_23_cocracon_for_archer]",
			"[13_23_cocracon_for_mage]"
		}, new int[][]
		{
			{
				-180971,
				186361,
				-10557,
				11632
			},
			{
				-180758,
				186739,
				-10556,
				11632
			}
		});
		int buffsNow = 0;
		long nextStatusChange = ServerVariables.getLong("SeedNextStatusChange", 0);
		if (nextStatusChange < System.currentTimeMillis())
		{
			buffsNow = Rnd.get(ZONE_BUFFS_LIST.length);
			_seedsNextStatusChange = getNextSeedsStatusChangeTime();
			ServerVariables.set("SeedBuffsList", buffsNow);
			ServerVariables.set("SeedNextStatusChange", _seedsNextStatusChange);
		}
		else
		{
			_seedsNextStatusChange = nextStatusChange;
			buffsNow = ServerVariables.getInt("SeedBuffsList", 0);
		}
		for (int i = 0; i < _regionsData.length; i++)
		{
			_regionsData[i].activeBuff = ZONE_BUFFS_LIST[buffsNow][i];
		}
	}
	
	/**
	 * Method getNextSeedsStatusChangeTime.
	 * @return Long
	 */
	Long getNextSeedsStatusChangeTime()
	{
		Calendar reenter = Calendar.getInstance();
		reenter.set(Calendar.SECOND, 0);
		reenter.set(Calendar.MINUTE, 0);
		reenter.set(Calendar.HOUR_OF_DAY, 13);
		reenter.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		if (reenter.getTimeInMillis() <= System.currentTimeMillis())
		{
			reenter.add(Calendar.DAY_OF_MONTH, 7);
		}
		return reenter.getTimeInMillis();
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		loadSeedRegionData();
		startEffectZonesControl();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method startEffectZonesControl.
	 */
	private void startEffectZonesControl()
	{
		for (SeedRegion sr : _regionsData)
		{
			NpcTemplate template = NpcHolder.getInstance().getTemplate(ANNIHILATION_FURNACE);
			for (int i = 0; i < sr.af_spawns.length; i++)
			{
				NpcInstance npc = template.getNewInstance();
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
				npc.spawnMe(new Location(sr.af_spawns[i][0], sr.af_spawns[i][1], sr.af_spawns[i][2], sr.af_spawns[i][3]));
				npc.setNpcState(sr.activeBuff);
				sr.af_npcs[i] = npc;
			}
			chanceZoneActive(sr.buff_zone_pc[sr.activeBuff - 1], true);
			chanceZoneActive(sr.buff_zone_npc[sr.activeBuff - 1], true);
		}
		ThreadPoolManager.getInstance().schedule(new ChangeSeedsStatus(), _seedsNextStatusChange - System.currentTimeMillis());
	}
	
	/**
	 * @author Mobius
	 */
	private class ChangeSeedsStatus extends RunnableImpl
	{
		/**
		 * Constructor for ChangeSeedsStatus.
		 */
		public ChangeSeedsStatus()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			int buffsNow = Rnd.get(ZONE_BUFFS_LIST.length);
			_seedsNextStatusChange = getNextSeedsStatusChangeTime();
			ServerVariables.set("SeedBuffsList", buffsNow);
			ServerVariables.set("SeedNextStatusChange", _seedsNextStatusChange);
			for (int i = 0; i < _regionsData.length; i++)
			{
				int oldBuff = _regionsData[i].activeBuff;
				_regionsData[i].activeBuff = ZONE_BUFFS_LIST[buffsNow][i];
				for (NpcInstance af : _regionsData[i].af_npcs)
				{
					af.setNpcState(_regionsData[i].activeBuff);
				}
				chanceZoneActive(_regionsData[i].buff_zone_pc[oldBuff - 1], false);
				chanceZoneActive(_regionsData[i].buff_zone_npc[oldBuff - 1], false);
				chanceZoneActive(_regionsData[i].buff_zone_pc[_regionsData[i].activeBuff - 1], true);
				chanceZoneActive(_regionsData[i].buff_zone_npc[_regionsData[i].activeBuff - 1], true);
			}
			ThreadPoolManager.getInstance().schedule(new ChangeSeedsStatus(), _seedsNextStatusChange - System.currentTimeMillis());
		}
	}
	
	/**
	 * Method chanceZoneActive.
	 * @param zoneName String
	 * @param val boolean
	 */
	void chanceZoneActive(String zoneName, boolean val)
	{
		Zone zone = ReflectionUtils.getZone(zoneName);
		zone.setActive(val);
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (_teleportZones.containsKey(zone.getName()))
			{
				List<NpcInstance> around = cha.getAroundNpc(500, 300);
				if ((around != null) && !around.isEmpty())
				{
					for (NpcInstance npc : around)
					{
						if ((npc.getNpcId() == 32738) && (npc.getFollowTarget() != null))
						{
							if (npc.getFollowTarget().getObjectId() == cha.getObjectId())
							{
								npc.teleToLocation(_teleportZones.get(zone.getName()));
								npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, cha, Config.FOLLOW_RANGE);
							}
						}
					}
				}
				cha.teleToLocation(_teleportZones.get(zone.getName()));
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}
