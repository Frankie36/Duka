package com.mystique.android.duka.Util;

import android.util.Base64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class StkPushUtils {
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }

    public static String sanitizePhoneNumber(String phone) {

        if (phone.equals("")) {
            return "";
        }

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if (phone.length() == 13 && phone.startsWith("+")) {
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return phone;
    }

    public static String getPassword(String businessShortCode, String passkey, String timestamp) {
        String str = businessShortCode + passkey + timestamp;
        //encode the password to Base64
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    public static String getBearer(String consumerKey, String consumerSecret) {
        String str = consumerKey + ":" + consumerSecret;
        //encode the password to Base64
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    public static boolean isValidMobile(String phone) {
        boolean check;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 13) {
                check = false;
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }
}
