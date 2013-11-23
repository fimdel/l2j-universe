package l2p.gameserver.model.base;

import l2p.gameserver.Config;

public class Experience {

    public final static long LEVEL[] = {-1L, // level 0 (unreachable)
            /*1*/0L,
            /*2*/68L,
            /*3*/363L,
            /*4*/1168L,
            /*5*/2884L,
            /*6*/6038L,
            /*7*/11287L,
            /*8*/19423L,
            /*9*/31378L,
            /*10*/48229L,
            /*11*/71202L,
            /*12*/101677L,
            /*13*/141193L,
            /*14*/191454L,
            /*15*/254330L,
            /*16*/331867L,
            /*17*/426288L,
            /*18*/540000L,
            /*19*/675596L,
            /*20*/835862L,
            /*21*/920357L,
            /*22*/1015431L,
            /*23*/1123336L,
            /*24*/1246808L,
            /*25*/1389235L,
            /*26*/1554904L,
            /*27*/1749413L,
            /*28*/1980499L,
            /*29*/2260321L,
            /*30*/2634751L,
            /*31*/2844287L,
            /*32*/3093068L,
            /*33*/3389496L,
            /*34*/3744042L,
            /*35*/4169902L,
            /*36*/4683988L,
            /*37*/5308556L,
            /*38*/6074376L,
            /*39*/7029248L,
            /*40*/8342182L,
            /*41*/8718976L,
            /*42*/9289560L,
            /*43*/9991807L,
            /*44*/10856075L,
            /*45*/11920512L,
            /*46*/13233701L,
            /*47*/14858961L,
            /*48*/16882633L,
            /*49*/19436426L,
            /*50*/22977080L,
            /*51*/24605660L,
            /*52*/26635948L,
            /*53*/29161263L,
            /*54*/32298229L,
            /*55*/36193556L,
            /*56*/41033917L,
            /*57*/47093035L,
            /*58*/54711546L,
            /*59*/64407353L,
            /*60*/77947292L,
            /*61*/85775204L,
            /*62*/95595386L,
            /*63*/107869713L,
            /*64*/123174171L,
            /*65*/142229446L,
            /*66*/165944812L,
            /*67*/195677269L,
            /*68*/233072222L,
            /*69*/280603594L,
            /*70*/335732975L,
            /*71*/383597045L,
            /*72*/442752112L,
            /*73*/516018015L,
            /*74*/606913902L,
            /*75*/719832095L,
            /*76*/860289228L,
            /*77*/1035327669L,
            /*78*/1259458516L,
            /*79*/1534688053L,
            /*80*/1909610088L,
            /*81*/2342785974L,
            /*82*/2861857696L,
            /*83*/3478378664L,
            /*84*/4211039578L,
            /*85*/5078544041L,
            /*86*/10985069426L,
            /*87*/19192594397L,
            /*88*/33533938399L,
            /*89*/43503026615L,
            /*90*/61895085913L,
            /*91*/84465260437L,
            /*92*/112359133751L,
            /*93*/146853833970L,
            /*94*/189558054903L,
            /*95*/242517343994L,
            /*96*/343490462139L,
            /*97*/538901012155L,
            /*98*/923857608218L,
            /*99*/1701666675991L,
            /*100 */1801666675991L};

    /**
     * Return PenaltyModifier (can use in all cases)
     *
     * @param count    - how many times <percents> will be substructed
     * @param percents - percents to substruct
     * @author Styx
     */

    /*
      *  This is for fine view only ;)
      *
      *	public final static double penaltyModifier(int count, int percents)
      *	{
      *		int allPercents = 100;
      *		int allSubstructedPercents = count * percents;
      *		int penaltyInPercents = allPercents - allSubstructedPercents;
      *		double penalty = penaltyInPercents / 100.0;
      *		return penalty;
      *	}
      */
    public static double penaltyModifier(long count, double percents) {
        return Math.max(1. - count * percents / 100, 0);
    }

    /**
     * Максимальный достижимый уровень
     */
    public static int getMaxLevel() {
        return Config.ALT_MAX_LEVEL;
    }

    /**
     * Максимальный уровень для саба
     */
    public static int getMaxSubLevel() {
        return Config.ALT_MAX_SUB_LEVEL;
    }

    public static int getLevel(long thisExp) {
        int level = 0;
        for (int i = 0; i < LEVEL.length; i++) {
            long exp = LEVEL[i];
            if (thisExp >= exp)
                level = i;
        }
        return level;
    }

    public static long getExpForLevel(int lvl) {
        if (lvl >= Experience.LEVEL.length)
            return 0;
        return Experience.LEVEL[lvl];
    }

    public static double getExpPercent(int level, long exp) {
        return (exp - getExpForLevel(level)) / ((getExpForLevel(level + 1) - getExpForLevel(level)) / 100.0D) * 0.01D;
    }

    public static int getMaxDualLevel() {
        return 99;
    }
}