package com.chinarewards.metro.service.account;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.chinarewards.metro.domain.account.Unit;

public class NumberHelper {

	public static final BigDecimal BLANK_DECIMAL = new BigDecimal(-99999);

	public static final BigDecimal ZERO_DECIMAL = new BigDecimal(0);

	public NumberHelper() {
	}

	/**
	 * @param unit
	 * @param value
	 * @return
	 */
	public static double formatUnits(Unit unit, double value) {
		return NumberHelper.round(value, unit.getNumberOfDecimals());
	}

	/**
	 * truncate the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return double - The truncated value
	 * 
	 */
	public static double truncate(double aValue, int aScale) {
		aValue = new BigDecimal(aValue).setScale(aScale, BigDecimal.ROUND_DOWN)
				.doubleValue();

		return aValue;
	}

	/**
	 * round the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return double - The round value
	 * 
	 */
	public static double round(double aValue, int aScale) {
		aValue = new BigDecimal(aValue).setScale(aScale,
				BigDecimal.ROUND_HALF_UP).doubleValue();

		return aValue;
	}

	/**
	 * round the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return String - The Formatted Number
	 * 
	 */
	public static String getFormatNumber(double aInput, int aScale) {

		aInput = round(aInput, aScale);

		return NumberFormat.getInstance().format(aInput);
	}

	/**
	 * truncate the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return BigDecimal - The truncated value
	 * 
	 */
	public static BigDecimal truncate(BigDecimal aValue, int aScale) {
		aValue = aValue.setScale(aScale, BigDecimal.ROUND_DOWN);

		return aValue;
	}

	/**
	 * round the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return BigDecimal - The round value
	 * 
	 */
	public static BigDecimal round(BigDecimal aValue, int aScale) {
		aValue = aValue.setScale(aScale, BigDecimal.ROUND_HALF_UP);

		return aValue;
	}

	public static BigDecimal getBigDecimal(String aInput) {
		return getBigDecimal(aInput, BLANK_DECIMAL);
	}

	public static BigDecimal getBigDecimal(String aInput,
			BigDecimal aDefaultValue) {
		try {
			return new BigDecimal(aInput);
		} catch (Exception e) {
			return aDefaultValue;
		}
	}

	/**
	 * round the value according to scale
	 * 
	 * @param aValue
	 *            - The value
	 * @param aScale
	 *            - The maximum decimal place
	 * 
	 * @return String - The Formatted Number
	 * 
	 */
	public static String getFormatNumber(BigDecimal aInput, int aScale) {
		if (aInput == null)
			return null;

		aInput = round(aInput, aScale);

		return NumberFormat.getInstance().format(aInput);
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		
		return b1.subtract(b2).doubleValue();
	}
}
