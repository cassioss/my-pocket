package edu.illinois.dscs.mypocket.utils;

import android.graphics.Color;

import java.text.NumberFormat;

/**
 * @author Cassio
 * @version 1.0
 */
public final class CurrencyUtils {

    /**
     * Given a string that contains a double value, returns a string with the correct format for currency.
     *
     * @param doubleValue string that corresponds to a double.
     * @return "$ " followed by the money sign (if any) and a number with commas for thousands and two decimal digits.
     */
    public static String moneyWithTwoDecimals(String doubleValue) {
        double parsedValue = Double.valueOf(doubleValue);
        return "$ " + NumberFormat.getCurrencyInstance().format(parsedValue).replaceAll("[$ ]", "").replaceAll("^-(?=0(.00*)?$)", "");
    }

    /**
     * Returns an integer that corresponds to a color for a given (money) value contained in a string.
     *
     * @param stringValue a string that contains a (money) value.
     * @return red if positive, green if negative, black if exactly 0.00
     */
    public static int setMoneyColor(String stringValue) {
        Double doubleValue = Double.valueOf(moneyWithTwoDecimals(stringValue).replaceAll("[$, ]", ""));
        if (doubleValue > 0.00)
            return Color.argb(255, 0, 200, 0);  // Green
        else if (doubleValue < 0.00)
            return Color.RED;
        else
            return Color.BLACK;
    }
}
