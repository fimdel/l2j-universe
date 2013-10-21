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
package lineage2.commons.text;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PrintfFormat
{
	/**
	 * Constructor for PrintfFormat.
	 * @param fmtArg String
	 * @throws IllegalArgumentException
	 */
	public PrintfFormat(String fmtArg) throws IllegalArgumentException
	{
		this(Locale.getDefault(), fmtArg);
	}
	
	/**
	 * Constructor for PrintfFormat.
	 * @param locale Locale
	 * @param fmtArg String
	 * @throws IllegalArgumentException
	 */
	public PrintfFormat(Locale locale, String fmtArg) throws IllegalArgumentException
	{
		dfs = new DecimalFormatSymbols(locale);
		int ePos = 0;
		ConversionSpecification sFmt = null;
		String unCS = nonControl(fmtArg, 0);
		if (unCS != null)
		{
			sFmt = new ConversionSpecification();
			sFmt.setLiteral(unCS);
			vFmt.add(sFmt);
		}
		while ((cPos != -1) && (cPos < fmtArg.length()))
		{
			for (ePos = cPos + 1; ePos < fmtArg.length(); ePos++)
			{
				char c = 0;
				c = fmtArg.charAt(ePos);
				if (c == 'i')
				{
					break;
				}
				if (c == 'd')
				{
					break;
				}
				if (c == 'f')
				{
					break;
				}
				if (c == 'g')
				{
					break;
				}
				if (c == 'G')
				{
					break;
				}
				if (c == 'o')
				{
					break;
				}
				if (c == 'x')
				{
					break;
				}
				if (c == 'X')
				{
					break;
				}
				if (c == 'e')
				{
					break;
				}
				if (c == 'E')
				{
					break;
				}
				if (c == 'c')
				{
					break;
				}
				if (c == 's')
				{
					break;
				}
				if (c == '%')
				{
					break;
				}
			}
			ePos = Math.min(ePos + 1, fmtArg.length());
			sFmt = new ConversionSpecification(fmtArg.substring(cPos, ePos));
			vFmt.add(sFmt);
			unCS = nonControl(fmtArg, ePos);
			if (unCS != null)
			{
				sFmt = new ConversionSpecification();
				sFmt.setLiteral(unCS);
				vFmt.add(sFmt);
			}
		}
	}
	
	/**
	 * Method nonControl.
	 * @param s String
	 * @param start int
	 * @return String
	 */
	private String nonControl(String s, int start)
	{
		cPos = s.indexOf("%", start);
		if (cPos == -1)
		{
			cPos = s.length();
		}
		return s.substring(start, cPos);
	}
	
	/**
	 * Method sprintf.
	 * @param o Object[]
	 * @return String
	 */
	public String sprintf(Object... o)
	{
		char c = 0;
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				if (cs.isPositionalSpecification())
				{
					i = cs.getArgumentPosition() - 1;
					if (cs.isPositionalFieldWidth())
					{
						int ifw = cs.getArgumentPositionForFieldWidth() - 1;
						cs.setFieldWidthWithArg(((Integer) o[ifw]).intValue());
					}
					if (cs.isPositionalPrecision())
					{
						int ipr = cs.getArgumentPositionForPrecision() - 1;
						cs.setPrecisionWithArg(((Integer) o[ipr]).intValue());
					}
				}
				else
				{
					if (cs.isVariableFieldWidth())
					{
						cs.setFieldWidthWithArg(((Integer) o[i]).intValue());
						i++;
					}
					if (cs.isVariablePrecision())
					{
						cs.setPrecisionWithArg(((Integer) o[i]).intValue());
						i++;
					}
				}
				if (o[i] instanceof Byte)
				{
					sb.append(cs.internalsprintf(((Byte) o[i]).byteValue()));
				}
				else if (o[i] instanceof Short)
				{
					sb.append(cs.internalsprintf(((Short) o[i]).shortValue()));
				}
				else if (o[i] instanceof Integer)
				{
					sb.append(cs.internalsprintf(((Integer) o[i]).intValue()));
				}
				else if (o[i] instanceof Long)
				{
					sb.append(cs.internalsprintf(((Long) o[i]).longValue()));
				}
				else if (o[i] instanceof Float)
				{
					sb.append(cs.internalsprintf(((Float) o[i]).floatValue()));
				}
				else if (o[i] instanceof Double)
				{
					sb.append(cs.internalsprintf(((Double) o[i]).doubleValue()));
				}
				else if (o[i] instanceof Character)
				{
					sb.append(cs.internalsprintf(((Character) o[i]).charValue()));
				}
				else if (o[i] instanceof String)
				{
					sb.append(cs.internalsprintf((String) o[i]));
				}
				else
				{
					sb.append(cs.internalsprintf(o[i]));
				}
				if (!cs.isPositionalSpecification())
				{
					i++;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @return String
	 */
	public String sprintf()
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @param x int
	 * @return String * @throws IllegalArgumentException
	 */
	public String sprintf(int x) throws IllegalArgumentException
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				sb.append(cs.internalsprintf(x));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @param x long
	 * @return String * @throws IllegalArgumentException
	 */
	public String sprintf(long x) throws IllegalArgumentException
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				sb.append(cs.internalsprintf(x));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @param x double
	 * @return String * @throws IllegalArgumentException
	 */
	public String sprintf(double x) throws IllegalArgumentException
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				sb.append(cs.internalsprintf(x));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @param x String
	 * @return String * @throws IllegalArgumentException
	 */
	public String sprintf(String x) throws IllegalArgumentException
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				sb.append(cs.internalsprintf(x));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method sprintf.
	 * @param x Object
	 * @return String * @throws IllegalArgumentException
	 */
	public String sprintf(Object x) throws IllegalArgumentException
	{
		char c = 0;
		StringBuilder sb = new StringBuilder();
		for (ConversionSpecification cs : vFmt)
		{
			c = cs.getConversionCharacter();
			if (c == '\0')
			{
				sb.append(cs.getLiteral());
			}
			else if (c == '%')
			{
				sb.append('%');
			}
			else
			{
				if (x instanceof Byte)
				{
					sb.append(cs.internalsprintf(((Byte) x).byteValue()));
				}
				else if (x instanceof Short)
				{
					sb.append(cs.internalsprintf(((Short) x).shortValue()));
				}
				else if (x instanceof Integer)
				{
					sb.append(cs.internalsprintf(((Integer) x).intValue()));
				}
				else if (x instanceof Long)
				{
					sb.append(cs.internalsprintf(((Long) x).longValue()));
				}
				else if (x instanceof Float)
				{
					sb.append(cs.internalsprintf(((Float) x).floatValue()));
				}
				else if (x instanceof Double)
				{
					sb.append(cs.internalsprintf(((Double) x).doubleValue()));
				}
				else if (x instanceof Character)
				{
					sb.append(cs.internalsprintf(((Character) x).charValue()));
				}
				else if (x instanceof String)
				{
					sb.append(cs.internalsprintf((String) x));
				}
				else
				{
					sb.append(cs.internalsprintf(x));
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author Mobius
	 */
	private class ConversionSpecification
	{
		/**
		 * Constructor for ConversionSpecification.
		 */
		ConversionSpecification()
		{
		}
		
		/**
		 * Constructor for ConversionSpecification.
		 * @param fmtArg String
		 * @throws IllegalArgumentException
		 */
		ConversionSpecification(String fmtArg) throws IllegalArgumentException
		{
			if (fmtArg == null)
			{
				throw new NullPointerException();
			}
			if (fmtArg.length() == 0)
			{
				throw new IllegalArgumentException("Control strings must have positive" + " lengths.");
			}
			if (fmtArg.charAt(0) == '%')
			{
				fmt = fmtArg;
				pos = 1;
				setArgPosition();
				setFlagCharacters();
				setFieldWidth();
				setPrecision();
				setOptionalHL();
				if (setConversionCharacter())
				{
					if (pos == fmtArg.length())
					{
						if (leadingZeros && leftJustify)
						{
							leadingZeros = false;
						}
						if (precisionSet && leadingZeros)
						{
							if ((conversionCharacter == 'd') || (conversionCharacter == 'i') || (conversionCharacter == 'o') || (conversionCharacter == 'x'))
							{
								leadingZeros = false;
							}
						}
					}
					else
					{
						throw new IllegalArgumentException("Malformed conversion specification=" + fmtArg);
					}
				}
				else
				{
					throw new IllegalArgumentException("Malformed conversion specification=" + fmtArg);
				}
			}
			else
			{
				throw new IllegalArgumentException("Control strings must begin with %.");
			}
		}
		
		/**
		 * Method setLiteral.
		 * @param s String
		 */
		void setLiteral(String s)
		{
			fmt = s;
		}
		
		/**
		 * Method getLiteral.
		 * @return String
		 */
		String getLiteral()
		{
			StringBuilder sb = new StringBuilder();
			int i = 0;
			while (i < fmt.length())
			{
				if (fmt.charAt(i) == '\\')
				{
					i++;
					if (i < fmt.length())
					{
						char c = fmt.charAt(i);
						switch (c)
						{
							case 'a':
								sb.append((char) 0x07);
								break;
							case 'b':
								sb.append('\b');
								break;
							case 'f':
								sb.append('\f');
								break;
							case 'n':
								sb.append(System.getProperty("line.separator"));
								break;
							case 'r':
								sb.append('\r');
								break;
							case 't':
								sb.append('\t');
								break;
							case 'v':
								sb.append((char) 0x0b);
								break;
							case '\\':
								sb.append('\\');
								break;
						}
						i++;
					}
					else
					{
						sb.append('\\');
					}
				}
				else
				{
					i++;
				}
			}
			return fmt;
		}
		
		/**
		 * Method getConversionCharacter.
		 * @return char
		 */
		char getConversionCharacter()
		{
			return conversionCharacter;
		}
		
		/**
		 * Method isVariableFieldWidth.
		 * @return boolean
		 */
		boolean isVariableFieldWidth()
		{
			return variableFieldWidth;
		}
		
		/**
		 * Method setFieldWidthWithArg.
		 * @param fw int
		 */
		void setFieldWidthWithArg(int fw)
		{
			if (fw < 0)
			{
				leftJustify = true;
			}
			fieldWidthSet = true;
			fieldWidth = Math.abs(fw);
		}
		
		/**
		 * Method isVariablePrecision.
		 * @return boolean
		 */
		boolean isVariablePrecision()
		{
			return variablePrecision;
		}
		
		/**
		 * Method setPrecisionWithArg.
		 * @param pr int
		 */
		void setPrecisionWithArg(int pr)
		{
			precisionSet = true;
			precision = Math.max(pr, 0);
		}
		
		/**
		 * Method internalsprintf.
		 * @param s int
		 * @return String * @throws IllegalArgumentException
		 */
		String internalsprintf(int s) throws IllegalArgumentException
		{
			String s2 = "";
			switch (conversionCharacter)
			{
				case 'd':
				case 'i':
					if (optionalh)
					{
						s2 = printDFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printDFormat((long) s);
					}
					else
					{
						s2 = printDFormat(s);
					}
					break;
				case 'x':
				case 'X':
					if (optionalh)
					{
						s2 = printXFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printXFormat((long) s);
					}
					else
					{
						s2 = printXFormat(s);
					}
					break;
				case 'o':
					if (optionalh)
					{
						s2 = printOFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printOFormat((long) s);
					}
					else
					{
						s2 = printOFormat(s);
					}
					break;
				case 'c':
				case 'C':
					s2 = printCFormat((char) s);
					break;
				default:
					throw new IllegalArgumentException("Cannot format a int with a format using a " + conversionCharacter + " conversion character.");
			}
			return s2;
		}
		
		/**
		 * Method internalsprintf.
		 * @param s long
		 * @return String * @throws IllegalArgumentException
		 */
		String internalsprintf(long s) throws IllegalArgumentException
		{
			String s2 = "";
			switch (conversionCharacter)
			{
				case 'd':
				case 'i':
					if (optionalh)
					{
						s2 = printDFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printDFormat(s);
					}
					else
					{
						s2 = printDFormat((int) s);
					}
					break;
				case 'x':
				case 'X':
					if (optionalh)
					{
						s2 = printXFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printXFormat(s);
					}
					else
					{
						s2 = printXFormat((int) s);
					}
					break;
				case 'o':
					if (optionalh)
					{
						s2 = printOFormat((short) s);
					}
					else if (optionall)
					{
						s2 = printOFormat(s);
					}
					else
					{
						s2 = printOFormat((int) s);
					}
					break;
				case 'c':
				case 'C':
					s2 = printCFormat((char) s);
					break;
				default:
					throw new IllegalArgumentException("Cannot format a long with a format using a " + conversionCharacter + " conversion character.");
			}
			return s2;
		}
		
		/**
		 * Method internalsprintf.
		 * @param s double
		 * @return String * @throws IllegalArgumentException
		 */
		String internalsprintf(double s) throws IllegalArgumentException
		{
			String s2 = "";
			switch (conversionCharacter)
			{
				case 'f':
					s2 = printFFormat(s);
					break;
				case 'E':
				case 'e':
					s2 = printEFormat(s);
					break;
				case 'G':
				case 'g':
					s2 = printGFormat(s);
					break;
				default:
					throw new IllegalArgumentException("Cannot " + "format a double with a format using a " + conversionCharacter + " conversion character.");
			}
			return s2;
		}
		
		/**
		 * Method internalsprintf.
		 * @param s String
		 * @return String * @throws IllegalArgumentException
		 */
		String internalsprintf(String s) throws IllegalArgumentException
		{
			String s2 = "";
			if ((conversionCharacter == 's') || (conversionCharacter == 'S'))
			{
				s2 = printSFormat(s);
			}
			else
			{
				throw new IllegalArgumentException("Cannot " + "format a String with a format using a " + conversionCharacter + " conversion character.");
			}
			return s2;
		}
		
		/**
		 * Method internalsprintf.
		 * @param s Object
		 * @return String
		 */
		String internalsprintf(Object s)
		{
			String s2 = "";
			if ((conversionCharacter == 's') || (conversionCharacter == 'S'))
			{
				s2 = printSFormat(s.toString());
			}
			else
			{
				throw new IllegalArgumentException("Cannot format a String with a format using" + " a " + conversionCharacter + " conversion character.");
			}
			return s2;
		}
		
		/**
		 * Method fFormatDigits.
		 * @param x double
		 * @return char[]
		 */
		private char[] fFormatDigits(double x)
		{
			String sx;
			int i, j, k;
			int n1In, n2In;
			int expon = 0;
			boolean minusSign = false;
			if (x > 0.0)
			{
				sx = Double.toString(x);
			}
			else if (x < 0.0)
			{
				sx = Double.toString(-x);
				minusSign = true;
			}
			else
			{
				sx = Double.toString(x);
				if (sx.charAt(0) == '-')
				{
					minusSign = true;
					sx = sx.substring(1);
				}
			}
			int ePos = sx.indexOf('E');
			int rPos = sx.indexOf('.');
			if (rPos != -1)
			{
				n1In = rPos;
			}
			else if (ePos != -1)
			{
				n1In = ePos;
			}
			else
			{
				n1In = sx.length();
			}
			if (rPos != -1)
			{
				if (ePos != -1)
				{
					n2In = ePos - rPos - 1;
				}
				else
				{
					n2In = sx.length() - rPos - 1;
				}
			}
			else
			{
				n2In = 0;
			}
			if (ePos != -1)
			{
				int ie = ePos + 1;
				expon = 0;
				if (sx.charAt(ie) == '-')
				{
					for (++ie; ie < sx.length(); ie++)
					{
						if (sx.charAt(ie) != '0')
						{
							break;
						}
					}
					if (ie < sx.length())
					{
						expon = -Integer.parseInt(sx.substring(ie));
					}
				}
				else
				{
					if (sx.charAt(ie) == '+')
					{
						++ie;
					}
					for (; ie < sx.length(); ie++)
					{
						if (sx.charAt(ie) != '0')
						{
							break;
						}
					}
					if (ie < sx.length())
					{
						expon = Integer.parseInt(sx.substring(ie));
					}
				}
			}
			int p;
			if (precisionSet)
			{
				p = precision;
			}
			else
			{
				p = defaultDigits - 1;
			}
			char[] ca1 = sx.toCharArray();
			char[] ca2 = new char[n1In + n2In];
			char[] ca3, ca4, ca5;
			for (j = 0; j < n1In; j++)
			{
				ca2[j] = ca1[j];
			}
			i = j + 1;
			for (k = 0; k < n2In; j++, i++, k++)
			{
				ca2[j] = ca1[i];
			}
			if ((n1In + expon) <= 0)
			{
				ca3 = new char[-expon + n2In];
				for (j = 0, k = 0; k < (-n1In - expon); k++, j++)
				{
					ca3[j] = '0';
				}
				for (i = 0; i < (n1In + n2In); i++, j++)
				{
					ca3[j] = ca2[i];
				}
			}
			else
			{
				ca3 = ca2;
			}
			boolean carry = false;
			if (p < (-expon + n2In))
			{
				if (expon < 0)
				{
					i = p;
				}
				else
				{
					i = p + n1In;
				}
				carry = checkForCarry(ca3, i);
				if (carry)
				{
					carry = startSymbolicCarry(ca3, i - 1, 0);
				}
			}
			if ((n1In + expon) <= 0)
			{
				ca4 = new char[2 + p];
				if (!carry)
				{
					ca4[0] = '0';
				}
				else
				{
					ca4[0] = '1';
				}
				if (alternateForm || !precisionSet || (precision != 0))
				{
					ca4[1] = '.';
					for (i = 0, j = 2; i < Math.min(p, ca3.length); i++, j++)
					{
						ca4[j] = ca3[i];
					}
					for (; j < ca4.length; j++)
					{
						ca4[j] = '0';
					}
				}
			}
			else
			{
				if (!carry)
				{
					if (alternateForm || !precisionSet || (precision != 0))
					{
						ca4 = new char[n1In + expon + p + 1];
					}
					else
					{
						ca4 = new char[n1In + expon];
					}
					j = 0;
				}
				else
				{
					if (alternateForm || !precisionSet || (precision != 0))
					{
						ca4 = new char[n1In + expon + p + 2];
					}
					else
					{
						ca4 = new char[n1In + expon + 1];
					}
					ca4[0] = '1';
					j = 1;
				}
				for (i = 0; i < Math.min(n1In + expon, ca3.length); i++, j++)
				{
					ca4[j] = ca3[i];
				}
				for (; i < (n1In + expon); i++, j++)
				{
					ca4[j] = '0';
				}
				if (alternateForm || !precisionSet || (precision != 0))
				{
					ca4[j] = '.';
					j++;
					for (k = 0; (i < ca3.length) && (k < p); i++, j++, k++)
					{
						ca4[j] = ca3[i];
					}
					for (; j < ca4.length; j++)
					{
						ca4[j] = '0';
					}
				}
			}
			int nZeros = 0;
			if (!leftJustify && leadingZeros)
			{
				int xThousands = 0;
				if (thousands)
				{
					int xlead = 0;
					if ((ca4[0] == '+') || (ca4[0] == '-') || (ca4[0] == ' '))
					{
						xlead = 1;
					}
					int xdp = xlead;
					for (; xdp < ca4.length; xdp++)
					{
						if (ca4[xdp] == '.')
						{
							break;
						}
					}
					xThousands = (xdp - xlead) / 3;
				}
				if (fieldWidthSet)
				{
					nZeros = fieldWidth - ca4.length;
				}
				if ((!minusSign && (leadingSign || leadingSpace)) || minusSign)
				{
					nZeros--;
				}
				nZeros -= xThousands;
				if (nZeros < 0)
				{
					nZeros = 0;
				}
			}
			j = 0;
			if ((!minusSign && (leadingSign || leadingSpace)) || minusSign)
			{
				ca5 = new char[ca4.length + nZeros + 1];
				j++;
			}
			else
			{
				ca5 = new char[ca4.length + nZeros];
			}
			if (!minusSign)
			{
				if (leadingSign)
				{
					ca5[0] = '+';
				}
				if (leadingSpace)
				{
					ca5[0] = ' ';
				}
			}
			else
			{
				ca5[0] = '-';
			}
			for (i = 0; i < nZeros; i++, j++)
			{
				ca5[j] = '0';
			}
			for (i = 0; i < ca4.length; i++, j++)
			{
				ca5[j] = ca4[i];
			}
			int lead = 0;
			if ((ca5[0] == '+') || (ca5[0] == '-') || (ca5[0] == ' '))
			{
				lead = 1;
			}
			int dp = lead;
			for (; dp < ca5.length; dp++)
			{
				if (ca5[dp] == '.')
				{
					break;
				}
			}
			int nThousands = (dp - lead) / 3;
			if (dp < ca5.length)
			{
				ca5[dp] = dfs.getDecimalSeparator();
			}
			char[] ca6 = ca5;
			if (thousands && (nThousands > 0))
			{
				ca6 = new char[ca5.length + nThousands + lead];
				ca6[0] = ca5[0];
				for (i = lead, k = lead; i < dp; i++)
				{
					if ((i > 0) && (((dp - i) % 3) == 0))
					{
						ca6[k] = dfs.getGroupingSeparator();
						ca6[k + 1] = ca5[i];
						k += 2;
					}
					else
					{
						ca6[k] = ca5[i];
						k++;
					}
				}
				for (; i < ca5.length; i++, k++)
				{
					ca6[k] = ca5[i];
				}
			}
			return ca6;
		}
		
		/**
		 * Method fFormatString.
		 * @param x double
		 * @return String
		 */
		private String fFormatString(double x)
		{
			char[] ca6, ca7;
			if (Double.isInfinite(x))
			{
				if (x == Double.POSITIVE_INFINITY)
				{
					if (leadingSign)
					{
						ca6 = "+Inf".toCharArray();
					}
					else if (leadingSpace)
					{
						ca6 = " Inf".toCharArray();
					}
					else
					{
						ca6 = "Inf".toCharArray();
					}
				}
				else
				{
					ca6 = "-Inf".toCharArray();
				}
			}
			else if (Double.isNaN(x))
			{
				if (leadingSign)
				{
					ca6 = "+NaN".toCharArray();
				}
				else if (leadingSpace)
				{
					ca6 = " NaN".toCharArray();
				}
				else
				{
					ca6 = "NaN".toCharArray();
				}
			}
			else
			{
				ca6 = fFormatDigits(x);
			}
			ca7 = applyFloatPadding(ca6, false);
			return new String(ca7);
		}
		
		/**
		 * Method eFormatDigits.
		 * @param x double
		 * @param eChar char
		 * @return char[]
		 */
		private char[] eFormatDigits(double x, char eChar)
		{
			char[] ca1, ca2, ca3;
			String sx;
			int i, j, k, p;
			int expon = 0;
			int ePos, rPos, eSize;
			boolean minusSign = false;
			if (x > 0.0)
			{
				sx = Double.toString(x);
			}
			else if (x < 0.0)
			{
				sx = Double.toString(-x);
				minusSign = true;
			}
			else
			{
				sx = Double.toString(x);
				if (sx.charAt(0) == '-')
				{
					minusSign = true;
					sx = sx.substring(1);
				}
			}
			ePos = sx.indexOf('E');
			if (ePos == -1)
			{
				ePos = sx.indexOf('e');
			}
			rPos = sx.indexOf('.');
			if (ePos != -1)
			{
				int ie = ePos + 1;
				expon = 0;
				if (sx.charAt(ie) == '-')
				{
					for (++ie; ie < sx.length(); ie++)
					{
						if (sx.charAt(ie) != '0')
						{
							break;
						}
					}
					if (ie < sx.length())
					{
						expon = -Integer.parseInt(sx.substring(ie));
					}
				}
				else
				{
					if (sx.charAt(ie) == '+')
					{
						++ie;
					}
					for (; ie < sx.length(); ie++)
					{
						if (sx.charAt(ie) != '0')
						{
							break;
						}
					}
					if (ie < sx.length())
					{
						expon = Integer.parseInt(sx.substring(ie));
					}
				}
			}
			if (rPos != -1)
			{
				expon += rPos - 1;
			}
			if (precisionSet)
			{
				p = precision;
			}
			else
			{
				p = defaultDigits - 1;
			}
			if ((rPos != -1) && (ePos != -1))
			{
				ca1 = (sx.substring(0, rPos) + sx.substring(rPos + 1, ePos)).toCharArray();
			}
			else if (rPos != -1)
			{
				ca1 = (sx.substring(0, rPos) + sx.substring(rPos + 1)).toCharArray();
			}
			else if (ePos != -1)
			{
				ca1 = sx.substring(0, ePos).toCharArray();
			}
			else
			{
				ca1 = sx.toCharArray();
			}
			boolean carry = false;
			int i0 = 0;
			if (ca1[0] != '0')
			{
				i0 = 0;
			}
			else
			{
				for (i0 = 0; i0 < ca1.length; i0++)
				{
					if (ca1[i0] != '0')
					{
						break;
					}
				}
			}
			if ((i0 + p) < (ca1.length - 1))
			{
				carry = checkForCarry(ca1, i0 + p + 1);
				if (carry)
				{
					carry = startSymbolicCarry(ca1, i0 + p, i0);
				}
				if (carry)
				{
					ca2 = new char[i0 + p + 1];
					ca2[i0] = '1';
					for (j = 0; j < i0; j++)
					{
						ca2[j] = '0';
					}
					for (i = i0, j = i0 + 1; j < (p + 1); i++, j++)
					{
						ca2[j] = ca1[i];
					}
					expon++;
					ca1 = ca2;
				}
			}
			if ((Math.abs(expon) < 100) && !optionalL)
			{
				eSize = 4;
			}
			else
			{
				eSize = 5;
			}
			if (alternateForm || !precisionSet || (precision != 0))
			{
				ca2 = new char[2 + p + eSize];
			}
			else
			{
				ca2 = new char[1 + eSize];
			}
			if (ca1[0] != '0')
			{
				ca2[0] = ca1[0];
				j = 1;
			}
			else
			{
				for (j = 1; j < (ePos == -1 ? ca1.length : ePos); j++)
				{
					if (ca1[j] != '0')
					{
						break;
					}
				}
				if (((ePos != -1) && (j < ePos)) || ((ePos == -1) && (j < ca1.length)))
				{
					ca2[0] = ca1[j];
					expon -= j;
					j++;
				}
				else
				{
					ca2[0] = '0';
					j = 2;
				}
			}
			if (alternateForm || !precisionSet || (precision != 0))
			{
				ca2[1] = '.';
				i = 2;
			}
			else
			{
				i = 1;
			}
			for (k = 0; (k < p) && (j < ca1.length); j++, i++, k++)
			{
				ca2[i] = ca1[j];
			}
			for (; i < (ca2.length - eSize); i++)
			{
				ca2[i] = '0';
			}
			ca2[i++] = eChar;
			if (expon < 0)
			{
				ca2[i++] = '-';
			}
			else
			{
				ca2[i++] = '+';
			}
			expon = Math.abs(expon);
			if (expon >= 100)
			{
				switch (expon / 100)
				{
					case 1:
						ca2[i] = '1';
						break;
					case 2:
						ca2[i] = '2';
						break;
					case 3:
						ca2[i] = '3';
						break;
					case 4:
						ca2[i] = '4';
						break;
					case 5:
						ca2[i] = '5';
						break;
					case 6:
						ca2[i] = '6';
						break;
					case 7:
						ca2[i] = '7';
						break;
					case 8:
						ca2[i] = '8';
						break;
					case 9:
						ca2[i] = '9';
						break;
				}
				i++;
			}
			switch ((expon % 100) / 10)
			{
				case 0:
					ca2[i] = '0';
					break;
				case 1:
					ca2[i] = '1';
					break;
				case 2:
					ca2[i] = '2';
					break;
				case 3:
					ca2[i] = '3';
					break;
				case 4:
					ca2[i] = '4';
					break;
				case 5:
					ca2[i] = '5';
					break;
				case 6:
					ca2[i] = '6';
					break;
				case 7:
					ca2[i] = '7';
					break;
				case 8:
					ca2[i] = '8';
					break;
				case 9:
					ca2[i] = '9';
					break;
			}
			i++;
			switch (expon % 10)
			{
				case 0:
					ca2[i] = '0';
					break;
				case 1:
					ca2[i] = '1';
					break;
				case 2:
					ca2[i] = '2';
					break;
				case 3:
					ca2[i] = '3';
					break;
				case 4:
					ca2[i] = '4';
					break;
				case 5:
					ca2[i] = '5';
					break;
				case 6:
					ca2[i] = '6';
					break;
				case 7:
					ca2[i] = '7';
					break;
				case 8:
					ca2[i] = '8';
					break;
				case 9:
					ca2[i] = '9';
					break;
			}
			int nZeros = 0;
			if (!leftJustify && leadingZeros)
			{
				int xThousands = 0;
				if (thousands)
				{
					int xlead = 0;
					if ((ca2[0] == '+') || (ca2[0] == '-') || (ca2[0] == ' '))
					{
						xlead = 1;
					}
					int xdp = xlead;
					for (; xdp < ca2.length; xdp++)
					{
						if (ca2[xdp] == '.')
						{
							break;
						}
					}
					xThousands = (xdp - xlead) / 3;
				}
				if (fieldWidthSet)
				{
					nZeros = fieldWidth - ca2.length;
				}
				if ((!minusSign && (leadingSign || leadingSpace)) || minusSign)
				{
					nZeros--;
				}
				nZeros -= xThousands;
				if (nZeros < 0)
				{
					nZeros = 0;
				}
			}
			j = 0;
			if ((!minusSign && (leadingSign || leadingSpace)) || minusSign)
			{
				ca3 = new char[ca2.length + nZeros + 1];
				j++;
			}
			else
			{
				ca3 = new char[ca2.length + nZeros];
			}
			if (!minusSign)
			{
				if (leadingSign)
				{
					ca3[0] = '+';
				}
				if (leadingSpace)
				{
					ca3[0] = ' ';
				}
			}
			else
			{
				ca3[0] = '-';
			}
			for (k = 0; k < nZeros; j++, k++)
			{
				ca3[j] = '0';
			}
			for (i = 0; (i < ca2.length) && (j < ca3.length); i++, j++)
			{
				ca3[j] = ca2[i];
			}
			int lead = 0;
			if ((ca3[0] == '+') || (ca3[0] == '-') || (ca3[0] == ' '))
			{
				lead = 1;
			}
			int dp = lead;
			for (; dp < ca3.length; dp++)
			{
				if (ca3[dp] == '.')
				{
					break;
				}
			}
			int nThousands = dp / 3;
			if (dp < ca3.length)
			{
				ca3[dp] = dfs.getDecimalSeparator();
			}
			char[] ca4 = ca3;
			if (thousands && (nThousands > 0))
			{
				ca4 = new char[ca3.length + nThousands + lead];
				ca4[0] = ca3[0];
				for (i = lead, k = lead; i < dp; i++)
				{
					if ((i > 0) && (((dp - i) % 3) == 0))
					{
						ca4[k] = dfs.getGroupingSeparator();
						ca4[k + 1] = ca3[i];
						k += 2;
					}
					else
					{
						ca4[k] = ca3[i];
						k++;
					}
				}
				for (; i < ca3.length; i++, k++)
				{
					ca4[k] = ca3[i];
				}
			}
			return ca4;
		}
		
		/**
		 * Method checkForCarry.
		 * @param ca1 char[]
		 * @param icarry int
		 * @return boolean
		 */
		private boolean checkForCarry(char[] ca1, int icarry)
		{
			boolean carry = false;
			if (icarry < ca1.length)
			{
				if ((ca1[icarry] == '6') || (ca1[icarry] == '7') || (ca1[icarry] == '8') || (ca1[icarry] == '9'))
				{
					carry = true;
				}
				else if (ca1[icarry] == '5')
				{
					int ii = icarry + 1;
					for (; ii < ca1.length; ii++)
					{
						if (ca1[ii] != '0')
						{
							break;
						}
					}
					carry = ii < ca1.length;
					if (!carry && (icarry > 0))
					{
						carry = ((ca1[icarry - 1] == '1') || (ca1[icarry - 1] == '3') || (ca1[icarry - 1] == '5') || (ca1[icarry - 1] == '7') || (ca1[icarry - 1] == '9'));
					}
				}
			}
			return carry;
		}
		
		/**
		 * Method startSymbolicCarry.
		 * @param ca char[]
		 * @param cLast int
		 * @param cFirst int
		 * @return boolean
		 */
		private boolean startSymbolicCarry(char[] ca, int cLast, int cFirst)
		{
			boolean carry = true;
			for (int i = cLast; carry && (i >= cFirst); i--)
			{
				carry = false;
				switch (ca[i])
				{
					case '0':
						ca[i] = '1';
						break;
					case '1':
						ca[i] = '2';
						break;
					case '2':
						ca[i] = '3';
						break;
					case '3':
						ca[i] = '4';
						break;
					case '4':
						ca[i] = '5';
						break;
					case '5':
						ca[i] = '6';
						break;
					case '6':
						ca[i] = '7';
						break;
					case '7':
						ca[i] = '8';
						break;
					case '8':
						ca[i] = '9';
						break;
					case '9':
						ca[i] = '0';
						carry = true;
						break;
				}
			}
			return carry;
		}
		
		/**
		 * Method eFormatString.
		 * @param x double
		 * @param eChar char
		 * @return String
		 */
		private String eFormatString(double x, char eChar)
		{
			char[] ca4, ca5;
			if (Double.isInfinite(x))
			{
				if (x == Double.POSITIVE_INFINITY)
				{
					if (leadingSign)
					{
						ca4 = "+Inf".toCharArray();
					}
					else if (leadingSpace)
					{
						ca4 = " Inf".toCharArray();
					}
					else
					{
						ca4 = "Inf".toCharArray();
					}
				}
				else
				{
					ca4 = "-Inf".toCharArray();
				}
			}
			else if (Double.isNaN(x))
			{
				if (leadingSign)
				{
					ca4 = "+NaN".toCharArray();
				}
				else if (leadingSpace)
				{
					ca4 = " NaN".toCharArray();
				}
				else
				{
					ca4 = "NaN".toCharArray();
				}
			}
			else
			{
				ca4 = eFormatDigits(x, eChar);
			}
			ca5 = applyFloatPadding(ca4, false);
			return new String(ca5);
		}
		
		/**
		 * Method applyFloatPadding.
		 * @param ca4 char[]
		 * @param noDigits boolean
		 * @return char[]
		 */
		private char[] applyFloatPadding(char[] ca4, boolean noDigits)
		{
			char[] ca5 = ca4;
			if (fieldWidthSet)
			{
				int i, j, nBlanks;
				if (leftJustify)
				{
					nBlanks = fieldWidth - ca4.length;
					if (nBlanks > 0)
					{
						ca5 = new char[ca4.length + nBlanks];
						for (i = 0; i < ca4.length; i++)
						{
							ca5[i] = ca4[i];
						}
						for (j = 0; j < nBlanks; j++, i++)
						{
							ca5[i] = ' ';
						}
					}
				}
				else if (!leadingZeros || noDigits)
				{
					nBlanks = fieldWidth - ca4.length;
					if (nBlanks > 0)
					{
						ca5 = new char[ca4.length + nBlanks];
						for (i = 0; i < nBlanks; i++)
						{
							ca5[i] = ' ';
						}
						for (j = 0; j < ca4.length; i++, j++)
						{
							ca5[i] = ca4[j];
						}
					}
				}
				else if (leadingZeros)
				{
					nBlanks = fieldWidth - ca4.length;
					if (nBlanks > 0)
					{
						ca5 = new char[ca4.length + nBlanks];
						i = 0;
						j = 0;
						if (ca4[0] == '-')
						{
							ca5[0] = '-';
							i++;
							j++;
						}
						for (int k = 0; k < nBlanks; i++, k++)
						{
							ca5[i] = '0';
						}
						for (; j < ca4.length; i++, j++)
						{
							ca5[i] = ca4[j];
						}
					}
				}
			}
			return ca5;
		}
		
		/**
		 * Method printFFormat.
		 * @param x double
		 * @return String
		 */
		private String printFFormat(double x)
		{
			return fFormatString(x);
		}
		
		/**
		 * Method printEFormat.
		 * @param x double
		 * @return String
		 */
		private String printEFormat(double x)
		{
			if (conversionCharacter == 'e')
			{
				return eFormatString(x, 'e');
			}
			return eFormatString(x, 'E');
		}
		
		/**
		 * Method printGFormat.
		 * @param x double
		 * @return String
		 */
		private String printGFormat(double x)
		{
			String sx, sy, sz, ret;
			int savePrecision = precision;
			int i;
			char[] ca4, ca5;
			if (Double.isInfinite(x))
			{
				if (x == Double.POSITIVE_INFINITY)
				{
					if (leadingSign)
					{
						ca4 = "+Inf".toCharArray();
					}
					else if (leadingSpace)
					{
						ca4 = " Inf".toCharArray();
					}
					else
					{
						ca4 = "Inf".toCharArray();
					}
				}
				else
				{
					ca4 = "-Inf".toCharArray();
				}
			}
			else if (Double.isNaN(x))
			{
				if (leadingSign)
				{
					ca4 = "+NaN".toCharArray();
				}
				else if (leadingSpace)
				{
					ca4 = " NaN".toCharArray();
				}
				else
				{
					ca4 = "NaN".toCharArray();
				}
			}
			else
			{
				if (!precisionSet)
				{
					precision = defaultDigits;
				}
				if (precision == 0)
				{
					precision = 1;
				}
				int ePos = -1;
				if (conversionCharacter == 'g')
				{
					sx = eFormatString(x, 'e').trim();
					ePos = sx.indexOf('e');
				}
				else
				{
					sx = eFormatString(x, 'E').trim();
					ePos = sx.indexOf('E');
				}
				i = ePos + 1;
				int expon = 0;
				if (sx.charAt(i) == '-')
				{
					for (++i; i < sx.length(); i++)
					{
						if (sx.charAt(i) != '0')
						{
							break;
						}
					}
					if (i < sx.length())
					{
						expon = -Integer.parseInt(sx.substring(i));
					}
				}
				else
				{
					if (sx.charAt(i) == '+')
					{
						++i;
					}
					for (; i < sx.length(); i++)
					{
						if (sx.charAt(i) != '0')
						{
							break;
						}
					}
					if (i < sx.length())
					{
						expon = Integer.parseInt(sx.substring(i));
					}
				}
				if (!alternateForm)
				{
					if ((expon >= -4) && (expon < precision))
					{
						sy = fFormatString(x).trim();
					}
					else
					{
						sy = sx.substring(0, ePos);
					}
					i = sy.length() - 1;
					for (; i >= 0; i--)
					{
						if (sy.charAt(i) != '0')
						{
							break;
						}
					}
					if ((i >= 0) && (sy.charAt(i) == '.'))
					{
						i--;
					}
					if (i == -1)
					{
						sz = "0";
					}
					else if (!Character.isDigit(sy.charAt(i)))
					{
						sz = sy.substring(0, i + 1) + "0";
					}
					else
					{
						sz = sy.substring(0, i + 1);
					}
					if ((expon >= -4) && (expon < precision))
					{
						ret = sz;
					}
					else
					{
						ret = sz + sx.substring(ePos);
					}
				}
				else
				{
					if ((expon >= -4) && (expon < precision))
					{
						ret = fFormatString(x).trim();
					}
					else
					{
						ret = sx;
					}
				}
				if (leadingSpace)
				{
					if (x >= 0)
					{
						ret = " " + ret;
					}
				}
				ca4 = ret.toCharArray();
			}
			ca5 = applyFloatPadding(ca4, false);
			precision = savePrecision;
			return new String(ca5);
		}
		
		/**
		 * Method printDFormat.
		 * @param x short
		 * @return String
		 */
		private String printDFormat(short x)
		{
			return printDFormat(Short.toString(x));
		}
		
		/**
		 * Method printDFormat.
		 * @param x long
		 * @return String
		 */
		private String printDFormat(long x)
		{
			return printDFormat(Long.toString(x));
		}
		
		/**
		 * Method printDFormat.
		 * @param x int
		 * @return String
		 */
		private String printDFormat(int x)
		{
			return printDFormat(Integer.toString(x));
		}
		
		/**
		 * Method printDFormat.
		 * @param sx String
		 * @return String
		 */
		private String printDFormat(String sx)
		{
			int nLeadingZeros = 0;
			int nBlanks = 0, n = 0;
			int i = 0, jFirst = 0;
			boolean neg = sx.charAt(0) == '-';
			if (sx.equals("0") && precisionSet && (precision == 0))
			{
				sx = "";
			}
			if (!neg)
			{
				if (precisionSet && (sx.length() < precision))
				{
					nLeadingZeros = precision - sx.length();
				}
			}
			else
			{
				if (precisionSet && ((sx.length() - 1) < precision))
				{
					nLeadingZeros = (precision - sx.length()) + 1;
				}
			}
			if (nLeadingZeros < 0)
			{
				nLeadingZeros = 0;
			}
			if (fieldWidthSet)
			{
				nBlanks = fieldWidth - nLeadingZeros - sx.length();
				if (!neg && (leadingSign || leadingSpace))
				{
					nBlanks--;
				}
			}
			if (nBlanks < 0)
			{
				nBlanks = 0;
			}
			if (leadingSign)
			{
				n++;
			}
			else if (leadingSpace)
			{
				n++;
			}
			n += nBlanks;
			n += nLeadingZeros;
			n += sx.length();
			char[] ca = new char[n];
			if (leftJustify)
			{
				if (neg)
				{
					ca[i++] = '-';
				}
				else if (leadingSign)
				{
					ca[i++] = '+';
				}
				else if (leadingSpace)
				{
					ca[i++] = ' ';
				}
				char[] csx = sx.toCharArray();
				jFirst = neg ? 1 : 0;
				for (int j = 0; j < nLeadingZeros; i++, j++)
				{
					ca[i] = '0';
				}
				for (int j = jFirst; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
				for (int j = 0; j < nBlanks; i++, j++)
				{
					ca[i] = ' ';
				}
			}
			else
			{
				if (!leadingZeros)
				{
					for (i = 0; i < nBlanks; i++)
					{
						ca[i] = ' ';
					}
					if (neg)
					{
						ca[i++] = '-';
					}
					else if (leadingSign)
					{
						ca[i++] = '+';
					}
					else if (leadingSpace)
					{
						ca[i++] = ' ';
					}
				}
				else
				{
					if (neg)
					{
						ca[i++] = '-';
					}
					else if (leadingSign)
					{
						ca[i++] = '+';
					}
					else if (leadingSpace)
					{
						ca[i++] = ' ';
					}
					for (int j = 0; j < nBlanks; j++, i++)
					{
						ca[i] = '0';
					}
				}
				for (int j = 0; j < nLeadingZeros; j++, i++)
				{
					ca[i] = '0';
				}
				char[] csx = sx.toCharArray();
				jFirst = neg ? 1 : 0;
				for (int j = jFirst; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
			}
			return new String(ca);
		}
		
		/**
		 * Method printXFormat.
		 * @param x short
		 * @return String
		 */
		private String printXFormat(short x)
		{
			String sx = null;
			if (x == Short.MIN_VALUE)
			{
				sx = "8000";
			}
			else if (x < 0)
			{
				String t;
				if (x == Short.MIN_VALUE)
				{
					t = "0";
				}
				else
				{
					t = Integer.toString((~(-x - 1)) ^ Short.MIN_VALUE, 16);
					if ((t.charAt(0) == 'F') || (t.charAt(0) == 'f'))
					{
						t = t.substring(16, 32);
					}
				}
				switch (t.length())
				{
					case 1:
						sx = "800" + t;
						break;
					case 2:
						sx = "80" + t;
						break;
					case 3:
						sx = "8" + t;
						break;
					case 4:
						switch (t.charAt(0))
						{
							case '1':
								sx = "9" + t.substring(1, 4);
								break;
							case '2':
								sx = "a" + t.substring(1, 4);
								break;
							case '3':
								sx = "b" + t.substring(1, 4);
								break;
							case '4':
								sx = "c" + t.substring(1, 4);
								break;
							case '5':
								sx = "d" + t.substring(1, 4);
								break;
							case '6':
								sx = "e" + t.substring(1, 4);
								break;
							case '7':
								sx = "f" + t.substring(1, 4);
								break;
						}
						break;
				}
			}
			else
			{
				sx = Integer.toString(x, 16);
			}
			return printXFormat(sx);
		}
		
		/**
		 * Method printXFormat.
		 * @param x long
		 * @return String
		 */
		private String printXFormat(long x)
		{
			String sx = null;
			if (x == Long.MIN_VALUE)
			{
				sx = "8000000000000000";
			}
			else if (x < 0)
			{
				String t = Long.toString((~(-x - 1)) ^ Long.MIN_VALUE, 16);
				switch (t.length())
				{
					case 1:
						sx = "800000000000000" + t;
						break;
					case 2:
						sx = "80000000000000" + t;
						break;
					case 3:
						sx = "8000000000000" + t;
						break;
					case 4:
						sx = "800000000000" + t;
						break;
					case 5:
						sx = "80000000000" + t;
						break;
					case 6:
						sx = "8000000000" + t;
						break;
					case 7:
						sx = "800000000" + t;
						break;
					case 8:
						sx = "80000000" + t;
						break;
					case 9:
						sx = "8000000" + t;
						break;
					case 10:
						sx = "800000" + t;
						break;
					case 11:
						sx = "80000" + t;
						break;
					case 12:
						sx = "8000" + t;
						break;
					case 13:
						sx = "800" + t;
						break;
					case 14:
						sx = "80" + t;
						break;
					case 15:
						sx = "8" + t;
						break;
					case 16:
						switch (t.charAt(0))
						{
							case '1':
								sx = "9" + t.substring(1, 16);
								break;
							case '2':
								sx = "a" + t.substring(1, 16);
								break;
							case '3':
								sx = "b" + t.substring(1, 16);
								break;
							case '4':
								sx = "c" + t.substring(1, 16);
								break;
							case '5':
								sx = "d" + t.substring(1, 16);
								break;
							case '6':
								sx = "e" + t.substring(1, 16);
								break;
							case '7':
								sx = "f" + t.substring(1, 16);
								break;
						}
						break;
				}
			}
			else
			{
				sx = Long.toString(x, 16);
			}
			return printXFormat(sx);
		}
		
		/**
		 * Method printXFormat.
		 * @param x int
		 * @return String
		 */
		private String printXFormat(int x)
		{
			String sx = null;
			if (x == Integer.MIN_VALUE)
			{
				sx = "80000000";
			}
			else if (x < 0)
			{
				String t = Integer.toString((~(-x - 1)) ^ Integer.MIN_VALUE, 16);
				switch (t.length())
				{
					case 1:
						sx = "8000000" + t;
						break;
					case 2:
						sx = "800000" + t;
						break;
					case 3:
						sx = "80000" + t;
						break;
					case 4:
						sx = "8000" + t;
						break;
					case 5:
						sx = "800" + t;
						break;
					case 6:
						sx = "80" + t;
						break;
					case 7:
						sx = "8" + t;
						break;
					case 8:
						switch (t.charAt(0))
						{
							case '1':
								sx = "9" + t.substring(1, 8);
								break;
							case '2':
								sx = "a" + t.substring(1, 8);
								break;
							case '3':
								sx = "b" + t.substring(1, 8);
								break;
							case '4':
								sx = "c" + t.substring(1, 8);
								break;
							case '5':
								sx = "d" + t.substring(1, 8);
								break;
							case '6':
								sx = "e" + t.substring(1, 8);
								break;
							case '7':
								sx = "f" + t.substring(1, 8);
								break;
						}
						break;
				}
			}
			else
			{
				sx = Integer.toString(x, 16);
			}
			return printXFormat(sx);
		}
		
		/**
		 * Method printXFormat.
		 * @param sx String
		 * @return String
		 */
		private String printXFormat(String sx)
		{
			int nLeadingZeros = 0;
			int nBlanks = 0;
			if (sx.equals("0") && precisionSet && (precision == 0))
			{
				sx = "";
			}
			if (precisionSet)
			{
				nLeadingZeros = precision - sx.length();
			}
			if (nLeadingZeros < 0)
			{
				nLeadingZeros = 0;
			}
			if (fieldWidthSet)
			{
				nBlanks = fieldWidth - nLeadingZeros - sx.length();
				if (alternateForm)
				{
					nBlanks = nBlanks - 2;
				}
			}
			if (nBlanks < 0)
			{
				nBlanks = 0;
			}
			int n = 0;
			if (alternateForm)
			{
				n += 2;
			}
			n += nLeadingZeros;
			n += sx.length();
			n += nBlanks;
			char[] ca = new char[n];
			int i = 0;
			if (leftJustify)
			{
				if (alternateForm)
				{
					ca[i++] = '0';
					ca[i++] = 'x';
				}
				for (int j = 0; j < nLeadingZeros; j++, i++)
				{
					ca[i] = '0';
				}
				char[] csx = sx.toCharArray();
				for (int j = 0; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
				for (int j = 0; j < nBlanks; j++, i++)
				{
					ca[i] = ' ';
				}
			}
			else
			{
				if (!leadingZeros)
				{
					for (int j = 0; j < nBlanks; j++, i++)
					{
						ca[i] = ' ';
					}
				}
				if (alternateForm)
				{
					ca[i++] = '0';
					ca[i++] = 'x';
				}
				if (leadingZeros)
				{
					for (int j = 0; j < nBlanks; j++, i++)
					{
						ca[i] = '0';
					}
				}
				for (int j = 0; j < nLeadingZeros; j++, i++)
				{
					ca[i] = '0';
				}
				char[] csx = sx.toCharArray();
				for (int j = 0; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
			}
			String caReturn = new String(ca);
			if (conversionCharacter == 'X')
			{
				caReturn = caReturn.toUpperCase();
			}
			return caReturn;
		}
		
		/**
		 * Method printOFormat.
		 * @param x short
		 * @return String
		 */
		private String printOFormat(short x)
		{
			String sx = null;
			if (x == Short.MIN_VALUE)
			{
				sx = "100000";
			}
			else if (x < 0)
			{
				String t = Integer.toString((~(-x - 1)) ^ Short.MIN_VALUE, 8);
				switch (t.length())
				{
					case 1:
						sx = "10000" + t;
						break;
					case 2:
						sx = "1000" + t;
						break;
					case 3:
						sx = "100" + t;
						break;
					case 4:
						sx = "10" + t;
						break;
					case 5:
						sx = "1" + t;
						break;
				}
			}
			else
			{
				sx = Integer.toString(x, 8);
			}
			return printOFormat(sx);
		}
		
		/**
		 * Method printOFormat.
		 * @param x long
		 * @return String
		 */
		private String printOFormat(long x)
		{
			String sx = null;
			if (x == Long.MIN_VALUE)
			{
				sx = "1000000000000000000000";
			}
			else if (x < 0)
			{
				String t = Long.toString((~(-x - 1)) ^ Long.MIN_VALUE, 8);
				switch (t.length())
				{
					case 1:
						sx = "100000000000000000000" + t;
						break;
					case 2:
						sx = "10000000000000000000" + t;
						break;
					case 3:
						sx = "1000000000000000000" + t;
						break;
					case 4:
						sx = "100000000000000000" + t;
						break;
					case 5:
						sx = "10000000000000000" + t;
						break;
					case 6:
						sx = "1000000000000000" + t;
						break;
					case 7:
						sx = "100000000000000" + t;
						break;
					case 8:
						sx = "10000000000000" + t;
						break;
					case 9:
						sx = "1000000000000" + t;
						break;
					case 10:
						sx = "100000000000" + t;
						break;
					case 11:
						sx = "10000000000" + t;
						break;
					case 12:
						sx = "1000000000" + t;
						break;
					case 13:
						sx = "100000000" + t;
						break;
					case 14:
						sx = "10000000" + t;
						break;
					case 15:
						sx = "1000000" + t;
						break;
					case 16:
						sx = "100000" + t;
						break;
					case 17:
						sx = "10000" + t;
						break;
					case 18:
						sx = "1000" + t;
						break;
					case 19:
						sx = "100" + t;
						break;
					case 20:
						sx = "10" + t;
						break;
					case 21:
						sx = "1" + t;
						break;
				}
			}
			else
			{
				sx = Long.toString(x, 8);
			}
			return printOFormat(sx);
		}
		
		/**
		 * Method printOFormat.
		 * @param x int
		 * @return String
		 */
		private String printOFormat(int x)
		{
			String sx = null;
			if (x == Integer.MIN_VALUE)
			{
				sx = "20000000000";
			}
			else if (x < 0)
			{
				String t = Integer.toString((~(-x - 1)) ^ Integer.MIN_VALUE, 8);
				switch (t.length())
				{
					case 1:
						sx = "2000000000" + t;
						break;
					case 2:
						sx = "200000000" + t;
						break;
					case 3:
						sx = "20000000" + t;
						break;
					case 4:
						sx = "2000000" + t;
						break;
					case 5:
						sx = "200000" + t;
						break;
					case 6:
						sx = "20000" + t;
						break;
					case 7:
						sx = "2000" + t;
						break;
					case 8:
						sx = "200" + t;
						break;
					case 9:
						sx = "20" + t;
						break;
					case 10:
						sx = "2" + t;
						break;
					case 11:
						sx = "3" + t.substring(1);
						break;
				}
			}
			else
			{
				sx = Integer.toString(x, 8);
			}
			return printOFormat(sx);
		}
		
		/**
		 * Method printOFormat.
		 * @param sx String
		 * @return String
		 */
		private String printOFormat(String sx)
		{
			int nLeadingZeros = 0;
			int nBlanks = 0;
			if (sx.equals("0") && precisionSet && (precision == 0))
			{
				sx = "";
			}
			if (precisionSet)
			{
				nLeadingZeros = precision - sx.length();
			}
			if (alternateForm)
			{
				nLeadingZeros++;
			}
			if (nLeadingZeros < 0)
			{
				nLeadingZeros = 0;
			}
			if (fieldWidthSet)
			{
				nBlanks = fieldWidth - nLeadingZeros - sx.length();
			}
			if (nBlanks < 0)
			{
				nBlanks = 0;
			}
			int n = nLeadingZeros + sx.length() + nBlanks;
			char[] ca = new char[n];
			int i;
			if (leftJustify)
			{
				for (i = 0; i < nLeadingZeros; i++)
				{
					ca[i] = '0';
				}
				char[] csx = sx.toCharArray();
				for (int j = 0; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
				for (int j = 0; j < nBlanks; j++, i++)
				{
					ca[i] = ' ';
				}
			}
			else
			{
				if (leadingZeros)
				{
					for (i = 0; i < nBlanks; i++)
					{
						ca[i] = '0';
					}
				}
				else
				{
					for (i = 0; i < nBlanks; i++)
					{
						ca[i] = ' ';
					}
				}
				for (int j = 0; j < nLeadingZeros; j++, i++)
				{
					ca[i] = '0';
				}
				char[] csx = sx.toCharArray();
				for (int j = 0; j < csx.length; j++, i++)
				{
					ca[i] = csx[j];
				}
			}
			return new String(ca);
		}
		
		/**
		 * Method printCFormat.
		 * @param x char
		 * @return String
		 */
		private String printCFormat(char x)
		{
			int nPrint = 1;
			int width = fieldWidth;
			if (!fieldWidthSet)
			{
				width = nPrint;
			}
			char[] ca = new char[width];
			int i = 0;
			if (leftJustify)
			{
				ca[0] = x;
				for (i = 1; i <= (width - nPrint); i++)
				{
					ca[i] = ' ';
				}
			}
			else
			{
				for (i = 0; i < (width - nPrint); i++)
				{
					ca[i] = ' ';
				}
				ca[i] = x;
			}
			return new String(ca);
		}
		
		/**
		 * Method printSFormat.
		 * @param x String
		 * @return String
		 */
		private String printSFormat(String x)
		{
			int nPrint = x.length();
			int width = fieldWidth;
			if (precisionSet && (nPrint > precision))
			{
				nPrint = precision;
			}
			if (!fieldWidthSet)
			{
				width = nPrint;
			}
			int n = 0;
			if (width > nPrint)
			{
				n += width - nPrint;
			}
			if (nPrint >= x.length())
			{
				n += x.length();
			}
			else
			{
				n += nPrint;
			}
			char[] ca = new char[n];
			int i = 0;
			if (leftJustify)
			{
				if (nPrint >= x.length())
				{
					char[] csx = x.toCharArray();
					for (i = 0; i < x.length(); i++)
					{
						ca[i] = csx[i];
					}
				}
				else
				{
					char[] csx = x.substring(0, nPrint).toCharArray();
					for (i = 0; i < nPrint; i++)
					{
						ca[i] = csx[i];
					}
				}
				for (int j = 0; j < (width - nPrint); j++, i++)
				{
					ca[i] = ' ';
				}
			}
			else
			{
				for (i = 0; i < (width - nPrint); i++)
				{
					ca[i] = ' ';
				}
				if (nPrint >= x.length())
				{
					char[] csx = x.toCharArray();
					for (int j = 0; j < x.length(); i++, j++)
					{
						ca[i] = csx[j];
					}
				}
				else
				{
					char[] csx = x.substring(0, nPrint).toCharArray();
					for (int j = 0; j < nPrint; i++, j++)
					{
						ca[i] = csx[j];
					}
				}
			}
			return new String(ca);
		}
		
		/**
		 * Method setConversionCharacter.
		 * @return boolean
		 */
		private boolean setConversionCharacter()
		{
			boolean ret = false;
			conversionCharacter = '\0';
			if (pos < fmt.length())
			{
				char c = fmt.charAt(pos);
				if ((c == 'i') || (c == 'd') || (c == 'f') || (c == 'g') || (c == 'G') || (c == 'o') || (c == 'x') || (c == 'X') || (c == 'e') || (c == 'E') || (c == 'c') || (c == 's') || (c == '%'))
				{
					conversionCharacter = c;
					pos++;
					ret = true;
				}
			}
			return ret;
		}
		
		/**
		 * Method setOptionalHL.
		 */
		private void setOptionalHL()
		{
			optionalh = false;
			optionall = false;
			optionalL = false;
			if (pos < fmt.length())
			{
				char c = fmt.charAt(pos);
				if (c == 'h')
				{
					optionalh = true;
					pos++;
				}
				else if (c == 'l')
				{
					optionall = true;
					pos++;
				}
				else if (c == 'L')
				{
					optionalL = true;
					pos++;
				}
			}
		}
		
		/**
		 * Method setPrecision.
		 */
		private void setPrecision()
		{
			int firstPos = pos;
			precisionSet = false;
			if ((pos < fmt.length()) && (fmt.charAt(pos) == '.'))
			{
				pos++;
				if ((pos < fmt.length()) && (fmt.charAt(pos) == '*'))
				{
					pos++;
					if (!setPrecisionArgPosition())
					{
						variablePrecision = true;
						precisionSet = true;
					}
					return;
				}
				while (pos < fmt.length())
				{
					char c = fmt.charAt(pos);
					if (Character.isDigit(c))
					{
						pos++;
					}
					else
					{
						break;
					}
				}
				if (pos > (firstPos + 1))
				{
					String sz = fmt.substring(firstPos + 1, pos);
					precision = Integer.parseInt(sz);
					precisionSet = true;
				}
			}
		}
		
		/**
		 * Method setFieldWidth.
		 */
		private void setFieldWidth()
		{
			int firstPos = pos;
			fieldWidth = 0;
			fieldWidthSet = false;
			if ((pos < fmt.length()) && (fmt.charAt(pos) == '*'))
			{
				pos++;
				if (!setFieldWidthArgPosition())
				{
					variableFieldWidth = true;
					fieldWidthSet = true;
				}
			}
			else
			{
				while (pos < fmt.length())
				{
					char c = fmt.charAt(pos);
					if (Character.isDigit(c))
					{
						pos++;
					}
					else
					{
						break;
					}
				}
				if ((firstPos < pos) && (firstPos < fmt.length()))
				{
					String sz = fmt.substring(firstPos, pos);
					fieldWidth = Integer.parseInt(sz);
					fieldWidthSet = true;
				}
			}
		}
		
		/**
		 * Method setArgPosition.
		 */
		private void setArgPosition()
		{
			int xPos;
			for (xPos = pos; xPos < fmt.length(); xPos++)
			{
				if (!Character.isDigit(fmt.charAt(xPos)))
				{
					break;
				}
			}
			if ((xPos > pos) && (xPos < fmt.length()))
			{
				if (fmt.charAt(xPos) == '$')
				{
					positionalSpecification = true;
					argumentPosition = Integer.parseInt(fmt.substring(pos, xPos));
					pos = xPos + 1;
				}
			}
		}
		
		/**
		 * Method setFieldWidthArgPosition.
		 * @return boolean
		 */
		private boolean setFieldWidthArgPosition()
		{
			boolean ret = false;
			int xPos;
			for (xPos = pos; xPos < fmt.length(); xPos++)
			{
				if (!Character.isDigit(fmt.charAt(xPos)))
				{
					break;
				}
			}
			if ((xPos > pos) && (xPos < fmt.length()))
			{
				if (fmt.charAt(xPos) == '$')
				{
					positionalFieldWidth = true;
					argumentPositionForFieldWidth = Integer.parseInt(fmt.substring(pos, xPos));
					pos = xPos + 1;
					ret = true;
				}
			}
			return ret;
		}
		
		/**
		 * Method setPrecisionArgPosition.
		 * @return boolean
		 */
		private boolean setPrecisionArgPosition()
		{
			boolean ret = false;
			int xPos;
			for (xPos = pos; xPos < fmt.length(); xPos++)
			{
				if (!Character.isDigit(fmt.charAt(xPos)))
				{
					break;
				}
			}
			if ((xPos > pos) && (xPos < fmt.length()))
			{
				if (fmt.charAt(xPos) == '$')
				{
					positionalPrecision = true;
					argumentPositionForPrecision = Integer.parseInt(fmt.substring(pos, xPos));
					pos = xPos + 1;
					ret = true;
				}
			}
			return ret;
		}
		
		/**
		 * Method isPositionalSpecification.
		 * @return boolean
		 */
		boolean isPositionalSpecification()
		{
			return positionalSpecification;
		}
		
		/**
		 * Method getArgumentPosition.
		 * @return int
		 */
		int getArgumentPosition()
		{
			return argumentPosition;
		}
		
		/**
		 * Method isPositionalFieldWidth.
		 * @return boolean
		 */
		boolean isPositionalFieldWidth()
		{
			return positionalFieldWidth;
		}
		
		/**
		 * Method getArgumentPositionForFieldWidth.
		 * @return int
		 */
		int getArgumentPositionForFieldWidth()
		{
			return argumentPositionForFieldWidth;
		}
		
		/**
		 * Method isPositionalPrecision.
		 * @return boolean
		 */
		boolean isPositionalPrecision()
		{
			return positionalPrecision;
		}
		
		/**
		 * Method getArgumentPositionForPrecision.
		 * @return int
		 */
		int getArgumentPositionForPrecision()
		{
			return argumentPositionForPrecision;
		}
		
		/**
		 * Method setFlagCharacters.
		 */
		private void setFlagCharacters()
		{
			thousands = false;
			leftJustify = false;
			leadingSign = false;
			leadingSpace = false;
			alternateForm = false;
			leadingZeros = false;
			for (; pos < fmt.length(); pos++)
			{
				char c = fmt.charAt(pos);
				if (c == '\'')
				{
					thousands = true;
				}
				else if (c == '-')
				{
					leftJustify = true;
					leadingZeros = false;
				}
				else if (c == '+')
				{
					leadingSign = true;
					leadingSpace = false;
				}
				else if (c == ' ')
				{
					if (!leadingSign)
					{
						leadingSpace = true;
					}
				}
				else if (c == '#')
				{
					alternateForm = true;
				}
				else if (c == '0')
				{
					if (!leftJustify)
					{
						leadingZeros = true;
					}
				}
				else
				{
					break;
				}
			}
		}
		
		/**
		 * Field thousands.
		 */
		private boolean thousands = false;
		/**
		 * Field leftJustify.
		 */
		private boolean leftJustify = false;
		/**
		 * Field leadingSign.
		 */
		private boolean leadingSign = false;
		/**
		 * Field leadingSpace.
		 */
		private boolean leadingSpace = false;
		/**
		 * Field alternateForm.
		 */
		private boolean alternateForm = false;
		/**
		 * Field leadingZeros.
		 */
		private boolean leadingZeros = false;
		/**
		 * Field variableFieldWidth.
		 */
		private boolean variableFieldWidth = false;
		/**
		 * Field fieldWidth.
		 */
		private int fieldWidth = 0;
		/**
		 * Field fieldWidthSet.
		 */
		private boolean fieldWidthSet = false;
		/**
		 * Field precision.
		 */
		private int precision = 0;
		/**
		 * Field defaultDigits. (value is 6)
		 */
		private final static int defaultDigits = 6;
		/**
		 * Field variablePrecision.
		 */
		private boolean variablePrecision = false;
		/**
		 * Field precisionSet.
		 */
		private boolean precisionSet = false;
		/**
		 * Field positionalSpecification.
		 */
		private boolean positionalSpecification = false;
		/**
		 * Field argumentPosition.
		 */
		private int argumentPosition = 0;
		/**
		 * Field positionalFieldWidth.
		 */
		private boolean positionalFieldWidth = false;
		/**
		 * Field argumentPositionForFieldWidth.
		 */
		private int argumentPositionForFieldWidth = 0;
		/**
		 * Field positionalPrecision.
		 */
		private boolean positionalPrecision = false;
		/**
		 * Field argumentPositionForPrecision.
		 */
		private int argumentPositionForPrecision = 0;
		/**
		 * Field optionalh.
		 */
		private boolean optionalh = false;
		/**
		 * Field optionall.
		 */
		private boolean optionall = false;
		/**
		 * Field optionalL.
		 */
		private boolean optionalL = false;
		/**
		 * Field conversionCharacter.
		 */
		private char conversionCharacter = '\0';
		/**
		 * Field pos.
		 */
		private int pos = 0;
		/**
		 * Field fmt.
		 */
		private String fmt;
	}
	
	/**
	 * Field vFmt.
	 */
	private final List<ConversionSpecification> vFmt = new ArrayList<>();
	/**
	 * Field cPos.
	 */
	private int cPos = 0;
	/**
	 * Field dfs.
	 */
	DecimalFormatSymbols dfs = null;
}
