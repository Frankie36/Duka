package com.mystique.android.duka.Util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by francis on 18/01/2018.
 */

public class JSONParser {
    public String loadJSONFromAsset(Context context, int jsonAsset) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(jsonAsset);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
