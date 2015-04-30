package edu.illinois.dscs.mypocket.utils;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import edu.illinois.dscs.mypocket.dao.DBHelper;

/**
 * @author Cassio
 * @version 1.0
 */
public final class ValidationUtils {

    public static void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean invalidDescription(Context context, String desc) {
        if (desc.equals("")) {
            makeToast(context, "Please fill out the description");
            return true;
        } else return false;
    }

    public static boolean invalidName(Context context, String name) {
        if (name.equals("")) {
            makeToast(context, "Please fill out the name");
            return true;
        } else return false;
    }

    public static boolean invalidValue(Context context, double value) {
        if (value == 0.00) {
            makeToast(context, "Please set a non-zero value");
            return true;
        } else return false;
    }

    public static boolean invalidDate(Context context, String date) {
        if (date.contains("M") || date.contains("D") || date.contains("Y")) {
            makeToast(context, "Please set a valid date");
            return true;
        } else return false;
    }

    public static String setIncome(Cursor queryCursor, String absValue) {
        String transType = queryCursor.getString(queryCursor.getColumnIndex(DBHelper.KEY_TRANS_TYPE));
        Boolean isIncome = Integer.valueOf(transType) > 0;
        return !isIncome ? "-" + absValue : absValue;
    }

}
