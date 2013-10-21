package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;

public class StatusUpdate extends L2GameServerPacket
{
	private Creature _actor;
	private List<int[]> attributes = Collections.emptyList();
	private boolean hpRegActive = true;

	public StatusUpdate setHpRegActive(boolean v)
	{
		hpRegActive = v;
		return this;
	}

	public StatusUpdate(Creature creature)
	{
		_actor = creature;
	}

	public StatusUpdate addAttribute(StatusUpdateField... types)
	{
		for(StatusUpdateField s : types)
		{
			addAttribute(s);
		}
		return this;
	}

	public StatusUpdate addAttribute(Collection<StatusUpdateField> types)
	{
		for(StatusUpdateField s : types)
		{
			addAttribute(s);
		}
		return this;
	}

	public StatusUpdate addAttribute(StatusUpdateField type, int val)
	{
		if(attributes.isEmpty())
		{
			attributes = new ArrayList<>();
		}
		attributes.add(new int[]{type.ordinal(), val});
		return this;
	}

	public StatusUpdate addAttribute(StatusUpdateField type)
	{
		int value = -1;
		switch(type)
		{
			case LEVEL:
			{
				value = _actor.getLevel();
				break;
			}
			case EXP:
			{
				//long?
				break;
			}
			case STR:
			{
				value = _actor.getSTR();
				break;
			}
			case DEX:
			{
				value = _actor.getDEX();
				break;
			}
			case CON:
			{
				value = _actor.getCON();
				break;
			}
			case INT:
			{
				value = _actor.getINT();
				break;
			}
			case WIT:
			{
				value = _actor.getWIT();
				break;
			}
			case MEN:
			{
				value = _actor.getMEN();
				break;
			}
			case CUR_HP:
			{
				value = (int) _actor.getCurrentHp();
				break;
			}
			case MAX_HP:
			{
				value = _actor.getMaxHp();
				break;
			}
			case CUR_MP:
			{
				value = (int) _actor.getCurrentMp();
				break;
			}
			case MAX_MP:
			{
				value = _actor.getMaxMp();
				break;
			}
			case SP:
			{
				//           if (_actor.isPlayable() && (_actor.isPlayer() || _actor.isPet()))
				//           {
				//            }
				break;
			}
			case CUR_LOAD:
			{
				if(_actor.isPlayer())
				{
					value = _actor.getPlayer().getCurrentLoad();
				}
				break;
			}
			case MAX_LOAD:
			{
				if(_actor.isPlayer())
				{
					value = _actor.getPlayer().getMaxLoad();
				}
				break;
			}
			case P_ATK:
			{
				value = _actor.getPAtk(null);
				break;
			}
			case ATK_SPD:
			{
				value = _actor.getPAtkSpd();
				break;
			}
			case P_DEF:
			{
				value = _actor.getPDef(null);
				break;
			}
			case EVASION:
			{
				value = _actor.getEvasionRate(null);
				break;
			}
			case ACCURACY:
			{
				value = _actor.getAccuracy();
				break;
			}
			case CRITICAL:
			{
				value = _actor.getCriticalHit(null, null);
				break;
			}
			case M_ATK:
			{
				value = _actor.getMAtk(null, null);
				break;
			}
			case CAST_SPD:
			{
				value = _actor.getMAtkSpd();
				break;
			}
			case M_DEF:
			{
				value = _actor.getMDef(null, null);
				break;
			}
			case PVP_FLAG:
			{
				if(_actor.isPlayable())
				{
					value = _actor.getPvpFlag();
				}
				break;
			}
			case KARMA:
			{
				if(_actor.isPlayable())
				{
					value = _actor.getKarma();
				}
				break;
			}
			case CUR_CP:
			{
				if(_actor.isPlayer())
				{
					value = (int) _actor.getCurrentCp();
				}
				break;
			}
			case MAX_CP:
			{
				if(_actor.isPlayer())
				{
					value = _actor.getMaxCp();
				}
				break;
			}
			default:
				break;
		}
		if(value != -1)
		{
			if(attributes.isEmpty())
			{
				attributes = new ArrayList<>();
			}
			attributes.add(new int[]{type.ordinal(), value});
		}
		return this;
	}

	@Override
	protected final void writeImpl()
	{
		if(isEmpty())
		{
			return;
		}
		writeC(0x18);

		writeD(_actor.getObjectId());
		writeD(getReceiverId());
		writeD(hpRegActive);
		writeD(attributes.size());

		for(int[] temp : attributes)
		{
			writeD(temp[0]);
			writeD(temp[1]);
		}
	}

	public boolean isEmpty()
	{
		return attributes.isEmpty();
	}

	private int getReceiverId()
	{
		if(getClient().getActiveChar() != null)
		{
			Player act = getClient().getActiveChar();
			if(act == _actor || act.getTarget() == _actor)
			{
				return act.getObjectId();
			}
		}
		return 0;
	}

	public static enum StatusUpdateField
	{
		NONE(false),//0
		LEVEL(true),//1
		EXP(true),//2
		STR(true),//3
		DEX(true),//4
		CON(true),//5
		INT(true),//6
		WIT(true),//7
		MEN(true),//8
		CUR_HP(false),//9
		MAX_HP(false),//10
		CUR_MP(true),//11
		MAX_MP(true),//12
		SP(true),//13
		CUR_LOAD(true),//14
		MAX_LOAD(true),//15
		UNKNOWN1(false),//16
		P_ATK(true),//17
		ATK_SPD(false),//18
		P_DEF(true),//19
		EVASION(true),//20
		ACCURACY(true),//21
		CRITICAL(true),//22
		M_ATK(true),//23
		CAST_SPD(false),//24
		M_DEF(false),//25
		PVP_FLAG(false),//26
		KARMA(false),//27
		UNKNOWN3(false),//28
		UNKNOWN4(false),//29
		UNKNOWN5(false),//30
		UNKNOWN6(false),//31
		UNKNOWN7(false),//32
		CUR_CP(true), //33
		MAX_CP(true);//34
		final boolean _privateParam;

		private StatusUpdateField(boolean privateParam)
		{
			_privateParam = privateParam;
		}

		public boolean isPrivateParam()
		{
			return _privateParam;
		}
	}
}
