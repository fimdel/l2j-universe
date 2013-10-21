package lineage2.gameserver.network.serverpackets;

import java.util.List;

import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.utils.Location;

public class PetInfo extends L2GameServerPacket
{
	private int _runSpd, _walkSpd, MAtkSpd, PAtkSpd, pvp_flag, karma, rideable;
	private int _type, obj_id, npc_id, runing, incombat, dead, _sp, level;
	private int curFed, maxFed, curHp, maxHp, curMp, maxMp, curLoad, maxLoad;
	private int PAtk, PDef, MAtk, MDef, Accuracy, Evasion, Crit, sps, ss, type, _showSpawnAnimation;
	private Location _loc;
	private double col_redius, col_height;
	private long exp, exp_this_lvl, exp_next_lvl;
	private String _name, title;
	private TeamType _team;
	private int sumPoint, maxSumPoint;
	private int _ownerId;
	private final List<Integer> _aveList;
	private int _mevasion, _maccuracy, _mCritRate;

	public PetInfo(Summon summon)
	{
		_type = summon.getSummonType();
		_ownerId = summon.getPlayer().getObjectId();
		obj_id = summon.getObjectId();
		npc_id = summon.getTemplate().npcId;
		_loc = summon.getLoc();
		MAtkSpd = summon.getMAtkSpd();
		PAtkSpd = summon.getPAtkSpd();
		_runSpd = summon.getRunSpeed();
		_walkSpd = summon.getWalkSpeed();
		col_redius = summon.getColRadius();
		col_height = summon.getColHeight();
		runing = summon.isRunning() ? 1 : 0;
		incombat = summon.isInCombat() ? 1 : 0;
		dead = summon.isAlikeDead() ? 1 : 0;
		_name = summon.getName().equalsIgnoreCase(summon.getTemplate().name) ? "" : summon.getName();
		title = summon.getTitle();
		pvp_flag = summon.getPvpFlag();
		karma = summon.getKarma();
		curFed = summon.getCurrentFed();
		maxFed = summon.getMaxFed();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
		_sp = summon.getSp();
		level = summon.getLevel();
		exp = summon.getExp();
		exp_this_lvl = summon.getExpForThisLevel();
		exp_next_lvl = summon.getExpForNextLevel();
		curLoad = summon.isPet() ? summon.getInventory().getTotalWeight() : 0;
		maxLoad = summon.getMaxLoad();
		PAtk = summon.getPAtk(null);
		PDef = summon.getPDef(null);
		MAtk = summon.getMAtk(null, null);
		MDef = summon.getMDef(null, null);
		Accuracy = summon.getAccuracy();
		Evasion = summon.getEvasionRate(null);
		Crit = summon.getCriticalHit(null, null);
		if (summon.getPlayer().getTransformation() != 0)
			rideable = 0; // not rideable
		else
			rideable = PetDataTable.isMountable(npc_id) ? 1 : 0;
		_team = summon.getTeam();
		ss = summon.getSoulshotConsumeCount();
		sps = summon.getSpiritshotConsumeCount();
		_showSpawnAnimation = summon.getSpawnAnimation();
		type = summon.getFormId();
		sumPoint = summon.getPlayer().getSummonList().getUsedPoints();
		maxSumPoint = summon.getPlayer().getSummonPointMax();
		_aveList = summon.getAveList();
		_mevasion = summon.getMEvasionRate(null);
		_maccuracy = summon.getMAccuracy();
		_mCritRate = (int) summon.getMagicCriticalRate(null, null);
	}

	public PetInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xb2);
		writeD(_type);
		writeD(obj_id);
		writeD(npc_id + 1000000);
		writeD(0); // 1=attackable
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
		writeD(0);
		writeD(MAtkSpd);
		writeD(PAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd/* _swimRunSpd */);
		writeD(_walkSpd/* _swimWalkSpd */);
		writeD(_runSpd/* _flRunSpd */);
		writeD(_walkSpd/* _flWalkSpd */);
		writeD(_runSpd/* _flyRunSpd */);
		writeD(_walkSpd/* _flyWalkSpd */);
		writeF(1/* _cha.getProperMultiplier() */);
		writeF(1/* _cha.getAttackSpeedMultiplier() */);
		writeF(col_redius);
		writeF(col_height);
		writeD(0); // right hand weapon
		writeD(0); // body armor
		writeD(0); // left hand weapon
		writeC(_ownerId); // name above char 1=true ... ??
		writeC(runing); // running=1
		writeC(incombat); // attacking 1=true
		writeC(dead); // dead 1=true
		writeC(_showSpawnAnimation); // invisible ?? 0=false 1=true 2=summoned
		                             // (only works if model has a summon
		                             // animation)
		writeD(-1);
		writeS(_name);
		writeD(-1);
		writeS(title);
		writeD(1);
		writeD(pvp_flag); // 0=white, 1=purple, 2=purpleblink, if its greater
		                  // then karma = purple
		writeD(karma); // hmm karma ??
		writeD(curFed); // how fed it is
		writeD(maxFed); // max fed it can be
		writeD(curHp); // current hp
		writeD(maxHp); // max hp
		writeD(curMp); // current mp
		writeD(maxMp); // max mp
		writeD(_sp); // sp
		writeD(level);// lvl

		writeQ(exp);
		writeQ(exp_this_lvl); // 0% absolute value
		writeQ(exp_next_lvl); // 100% absoulte value

		writeD(curLoad); // weight
		writeD(maxLoad); // max weight it can carry
		writeD(PAtk);// patk
		writeD(PDef);// pdef
		writeD(Accuracy);// accuracy
		writeD(Evasion);// evasion
		writeD(Crit);// critical
		writeD(MAtk);// matk
		writeD(MDef);// mdef
		writeD(_mevasion);
		writeD(_maccuracy);
		writeD(_mCritRate);
		writeD(_runSpd);// speed
		writeD(PAtkSpd);// atkspeed
		writeD(MAtkSpd);// casting speed
		writeD(rideable);
		writeC(0); // c2
		writeC(_team.ordinal()); // team aura (1 = blue, 2 = red)
		writeD(ss);
		writeD(sps);
		writeD(type);
		writeD(0x00); // id
		writeD(sumPoint);
		writeD(maxSumPoint);
		if (_aveList != null)
		{
			writeD(_aveList.size());
			for (int i : _aveList)
			{
				writeD(i);
			}
		}
		else
		{
			writeD(0x00);
		}
		writeC(0);
	}
}