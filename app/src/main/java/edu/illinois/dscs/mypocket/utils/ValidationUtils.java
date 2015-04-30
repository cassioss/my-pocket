package edu.illinois.dscs.mypocket.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Cassio
 * @version 1.0
 */
public final class ValidationUtils {

    /**
     * Creates a toast (pop-up window) with a given message.
     *
     * @param context the application context (gathered from the activity that called this method).
     * @param message a message to be shown.
     */
    public static void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Checks if a given description is empty, alerting the user.
     *
     * @param context the application context (gathered from the activity that called this method).
     * @param desc    a given description of a given Transaction.
     * @return <em>true</em> if the description is empty.
     */
    public static boolean invalidDescription(Context context, String desc) {
        if (desc.equals("")) {
            makeToast(context, "Please fill out the description");
            return true;
        } else return false;
    }

    /**
     * Checks if a given name is empty, alerting the user.
     *
     * @param context the application context (gathered from the activity that called this method).
     * @param name    a given name (for either an account or a category).
     * @return <em>true</em> if the name is empty.
     */
    public static boolean invalidName(Context context, String name) {
        if (name.equals("")) {
            makeToast(context, "Please fill out the name");
            return true;
        } else return false;
    }

    /**
     * Checks if a given value is zero, alerting the user.
     *
     * @param context the application context (gathered from the activity that called this method).
     * @param value   a given (money) value.
     * @return <em>true</em> if the value is equal to 0.
     */
    public static boolean invalidValue(Context context, double value) {
        if (value == 0.00) {
            makeToast(context, "Please set a non-zero value");
            return true;
        } else return false;
    }

    /**
     * Checks if a given date is incomplete, alerting the user.
     *
     * @param context the application context (gathered from the activity that called this method).
     * @param date    a given date (from a certain Transaction), shown as a string.
     * @return <em>true</em> if the date is incomplete.
     */
    public static boolean invalidDate(Context context, String date) {
        if (date.contains("M") || date.contains("D") || date.contains("Y")) {
            makeToast(context, "Please set a valid date");
            return true;
        } else return false;
    }

}
