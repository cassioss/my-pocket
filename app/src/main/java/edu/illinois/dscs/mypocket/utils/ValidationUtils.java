package edu.illinois.dscs.mypocket.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Cassio
 * @version 1.0
 */
public final class ValidationUtils {

    public static void makeShortToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void makeLongToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean invalidDescription(String desc) {
        return desc.equals("");
    }

    public static boolean invalidValue(double value) {
        return value == 0.00;
    }

    public static boolean invalidDate(String date) {
        return date.contains("M") || date.contains("D") || date.contains("Y");
    }

}
