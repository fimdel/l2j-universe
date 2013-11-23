package l2p.gameserver.utils;

import javolution.util.FastList;
import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.reward.RewardList;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Util {
    static final String PATTERN = "0.0000000000E00";
    static final DecimalFormat df;

    /**
     * Форматтер для адены.<br>
     * Locale.KOREA заставляет его фортматировать через ",".<br>
     * Locale.FRANCE форматирует через " "<br>
     * Для форматирования через "." убрать с аргументов Locale.FRANCE
     */
    private static NumberFormat adenaFormatter;

    static {
        adenaFormatter = NumberFormat.getIntegerInstance(Locale.FRANCE);
        df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
        df.applyPattern(PATTERN);
        df.setPositivePrefix("+");
    }

    /**
     * Проверяет строку на соответсвие регулярному выражению
     *
     * @param text     Строка-источник
     * @param template Шаблон для поиска
     * @return true в случае соответвия строки шаблону
     */
    public static boolean isMatchingRegexp(String text, String template) {
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(template);
        } catch (PatternSyntaxException e) // invalid template
        {
            e.printStackTrace();
        }
        if (pattern == null)
            return false;
        Matcher regexp = pattern.matcher(text);
        return regexp.matches();
    }

    public static String formatDouble(double x, String nanString, boolean forceExponents) {
        if (Double.isNaN(x))
            return nanString;
        if (forceExponents)
            return df.format(x);
        if ((long) x == x)
            return String.valueOf((long) x);
        return String.valueOf(x);
    }

    /**
     * Return amount of adena formatted with " " delimiter
     *
     * @param amount
     * @return String formatted adena amount
     */
    public static String formatAdena(long amount) {
        return adenaFormatter.format(amount);
    }

    /**
     * форматирует время в секундах в дни/часы/минуты/секунды
     */
    public static String formatTime(int time) {
        if (time == 0)
            return "now";
        time = Math.abs(time);
        String ret = "";
        long numDays = time / 86400;
        time -= numDays * 86400;
        long numHours = time / 3600;
        time -= numHours * 3600;
        long numMins = time / 60;
        time -= numMins * 60;
        long numSeconds = time;
        if (numDays > 0)
            ret += numDays + "d ";
        if (numHours > 0)
            ret += numHours + "h ";
        if (numMins > 0)
            ret += numMins + "m ";
        if (numSeconds > 0)
            ret += numSeconds + "s";
        return ret.trim();
    }

    /**
     * Инструмент для подсчета выпавших вещей с учетом рейтов.
     * Возвращает 0 если шанс не прошел, либо количество если прошел.
     * Корректно обрабатывает шансы превышающие 100%.
     * Шанс в 1:1000000 (L2Drop.MAX_CHANCE)
     */
    public static long rollDrop(long min, long max, double calcChance, boolean rate) {
        if (calcChance <= 0 || min <= 0 || max <= 0)
            return 0;
        int dropmult = 1;
        if (rate)
            calcChance *= Config.RATE_DROP_ITEMS;
        if (calcChance > RewardList.MAX_CHANCE)
            if (calcChance % RewardList.MAX_CHANCE == 0) // если кратен 100% то тупо умножаем количество
                dropmult = (int) (calcChance / RewardList.MAX_CHANCE);
            else {
                dropmult = (int) Math.ceil(calcChance / RewardList.MAX_CHANCE); // множитель равен шанс / 100% округление вверх
                calcChance = calcChance / dropmult; // шанс равен шанс / множитель
            }
        return Rnd.chance(calcChance / 10000.) ? Rnd.get(min * dropmult, max * dropmult) : 0;
    }

    public static int packInt(int[] a, int bits) throws Exception {
        int m = 32 / bits;
        if (a.length > m)
            throw new Exception("Overflow");

        int result = 0;
        int next;
        int mval = (int) Math.pow(2, bits);
        for (int i = 0; i < m; i++) {
            result <<= bits;
            if (a.length > i) {
                next = a[i];
                if (next >= mval || next < 0)
                    throw new Exception("Overload, value is out of range");
            } else
                next = 0;
            result += next;
        }
        return result;
    }

    public static long packLong(int[] a, int bits) throws Exception {
        int m = 64 / bits;
        if (a.length > m)
            throw new Exception("Overflow");

        long result = 0;
        int next;
        int mval = (int) Math.pow(2, bits);
        for (int i = 0; i < m; i++) {
            result <<= bits;
            if (a.length > i) {
                next = a[i];
                if (next >= mval || next < 0)
                    throw new Exception("Overload, value is out of range");
            } else
                next = 0;
            result += next;
        }
        return result;
    }

    public static int[] unpackInt(int a, int bits) {
        int m = 32 / bits;
        int mval = (int) Math.pow(2, bits);
        int[] result = new int[m];
        int next;
        for (int i = m; i > 0; i--) {
            next = a;
            a = a >> bits;
            result[i - 1] = next - a * mval;
        }
        return result;
    }

    public static int[] unpackLong(long a, int bits) {
        int m = 64 / bits;
        int mval = (int) Math.pow(2, bits);
        int[] result = new int[m];
        long next;
        for (int i = m; i > 0; i--) {
            next = a;
            a = a >> bits;
            result[i - 1] = (int) (next - a * mval);
        }
        return result;
    }

    /**
     * Just alias
     */
    public static String joinStrings(String glueStr, String[] strings, int startIdx, int maxCount) {
        return Strings.joinStrings(glueStr, strings, startIdx, maxCount);
    }

    /**
     * Just alias
     */
    public static String joinStrings(String glueStr, String[] strings, int startIdx) {
        return Strings.joinStrings(glueStr, strings, startIdx, -1);
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isDigit(String text) {
        if (text == null)
            return false;
        return text.matches("[0-9]+");
    }

    public static String dumpObject(Object o, boolean simpleTypes, boolean parentFields, boolean ignoreStatics) {
        Class<?> cls = o.getClass();
        String val, type, result = "[" + (simpleTypes ? cls.getSimpleName() : cls.getName()) + "\n";
        Object fldObj;
        List<Field> fields = new ArrayList<Field>();
        while (cls != null) {
            for (Field fld : cls.getDeclaredFields())
                if (!fields.contains(fld)) {
                    if (ignoreStatics && Modifier.isStatic(fld.getModifiers()))
                        continue;
                    fields.add(fld);
                }
            cls = cls.getSuperclass();
            if (!parentFields)
                break;
        }

        for (Field fld : fields) {
            fld.setAccessible(true);
            try {
                fldObj = fld.get(o);
                if (fldObj == null)
                    val = "NULL";
                else
                    val = fldObj.toString();
            } catch (Throwable e) {
                e.printStackTrace();
                val = "<ERROR>";
            }
            type = simpleTypes ? fld.getType().getSimpleName() : fld.getType().toString();

            result += String.format("\t%s [%s] = %s;\n", fld.getName(), type, val);
        }

        result += "]\n";
        return result;
    }

    private static Pattern _pattern = Pattern.compile("<!--TEMPLET(\\d+)(.*?)TEMPLET-->", Pattern.DOTALL);

    public static HashMap<Integer, String> parseTemplate(String html) {
        Matcher m = _pattern.matcher(html);
        HashMap<Integer, String> tpls = new HashMap<Integer, String>();
        while (m.find()) {
            tpls.put(Integer.parseInt(m.group(1)), m.group(2));
            html = html.replace(m.group(0), "");
        }

        tpls.put(0, html);
        return tpls;
    }

    public static String reverseColor(String color) {
        if (color.length() != 6)
            return "000000";
        char[] ch1 = color.toCharArray();
        char[] ch2 = new char[6];
        ch2[0] = ch1[4];
        ch2[1] = ch1[5];
        ch2[2] = ch1[2];
        ch2[3] = ch1[3];
        ch2[4] = ch1[0];
        ch2[5] = ch1[1];
        return new String(ch2);
    }

    public static int decodeColor(String color) {
        return Integer.decode("0x" + reverseColor(color));
    }

    public static FastList<File> getAllFileList(File dir, String pathName) {
        FastList<File> list = new FastList<File>();
        if ((!dir.toString().endsWith("/")) && (!dir.toString().endsWith("\\")))
            dir = new File(dir.toString() + "/");
        if (!dir.exists()) {
            //_log.log(Level.ERROR, new StringBuilder().append(" Folder ").append(dir.getAbsolutePath()).append(" doesn't exist!").toString());
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                for (File fileName : getAllFileList(file, pathName)) {
                    if (fileName.toString().endsWith(pathName)) {
                        list.add(fileName);
                    }
                }
            } else {
                if (!file.toString().endsWith(pathName))
                    continue;
                list.add(file);
            }
        }

        return list;
    }

    public static String printData(byte[] data, int len) {
        StringBuilder result = new StringBuilder(len * 4);

        int counter = 0;

        for (int i = 0; i < len; i++) {
            if (counter % 16 == 0) {
                result.append(fillHex(i, 4)).append(": ");
            }

            result.append(fillHex(data[i] & 0xFF, 2)).append(" ");
            counter++;
            if (counter != 16)
                continue;
            result.append("   ");

            int charpoint = i - 15;
            for (int a = 0; a < 16; a++) {
                int t1 = 0xFF & data[(charpoint++)];
                if ((t1 > 31) && (t1 < 128))
                    result.append((char) t1);
                else {
                    result.append('.');
                }
            }
            result.append("\n");
            counter = 0;
        }

        int rest = data.length % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }

            int charpoint = data.length - rest;
            for (int a = 0; a < rest; a++) {
                int t1 = 0xFF & data[(charpoint++)];
                if ((t1 > 31) && (t1 < 128)) {
                    result.append((char) t1);
                } else {
                    result.append('.');
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String fillHex(int data, int digits) {
        String number = Integer.toHexString(data);

        for (int i = number.length(); i < digits; i++) {
            number = "0" + number;
        }
        return number;
    }

    public static boolean checkIfInRange(int range, GameObject obj1, GameObject obj2, boolean includeZAxis) {
        if ((obj1 == null) || (obj2 == null))
            return false;
        if (range == -1) {
            return true;
        }
        int rad = 0;
        if ((obj1 instanceof Creature))
            rad += ((Creature) obj1).getTemplate().getCollisionRadius();
        if ((obj2 instanceof Creature)) {
            rad += ((Creature) obj2).getTemplate().getCollisionRadius();
        }
        double dx = obj1.getX() - obj2.getX();
        double dy = obj1.getY() - obj2.getY();

        if (includeZAxis) {
            double dz = obj1.getZ() - obj2.getZ();
            double d = dx * dx + dy * dy + dz * dz;

            return d <= range * range + 2 * range * rad + rad * rad;
        }

        double d = dx * dx + dy * dy;

        return d <= range * range + 2 * range * rad + rad * rad;
    }


    public static int getThirdClassForId(int classId) {
        int result = -1;
        switch (classId) {
            case 30:
                result = 150;
                break;
            case 20:
                result = 99;
                break;
            case 8:
                result = 93;
                break;
            case 14:
                result = 96;
                break;
            case 12:
                result = 94;
                break;
            case 16:
                result = 97;
                break;
            case 51:
                result = 115;
                break;
            case 127:
                result = 131;
                break;
            case 52:
                result = 116;
                break;
            case 3:
                result = 89;
                break;
            case 2:
                result = 88;
                break;
            case 28:
                result = 104;
                break;
            case 55:
                result = 117;
                break;
            case 36:
                result = 108;
                break;
            case 37:
                result = 109;
                break;
            case 48:
                result = 114;
                break;
            case 6:
                result = 91;
                break;
            case 17:
                result = 98;
                break;
            case 57:
                result = 118;
                break;
            case 24:
                result = 102;
                break;
            case 27:
                result = 103;
                break;
            case 5:
                result = 90;
                break;
            case 9:
                result = 92;
                break;
            case 43:
                result = 112;
                break;
            case 33:
                result = 106;
                break;
            case 128:
                result = 132;
                break;
            case 129:
                result = 133;
                break;
            case 13:
                result = 95;
                break;
            case 34:
                result = 107;
                break;
            case 41:
                result = 111;
                break;
            case 40:
                result = 110;
                break;
            case 21:
                result = 100;
                break;
            case 46:
                result = 113;
                break;
            case 130:
                result = 134;
                break;
            case 23:
                result = 101;
            case 4:
            case 7:
            case 10:
            case 11:
            case 15:
            case 18:
            case 19:
            case 22:
            case 25:
            case 26:
            case 29:
            case 31:
            case 32:
            case 35:
            case 38:
            case 39:
            case 42:
            case 44:
            case 45:
            case 47:
            case 49:
            case 50:
            case 53:
            case 54:
            case 56:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
        }
        return result;
    }
}
