package hkp.logimap;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by krysztal on 07.12.15.
 */
public class JSONloader {
    String json = null;

    public String loadFromFile(InputStream is) {
        try {
    //        InputStream is = getAssets().open(jsonfile + ".json");

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

    public String loadFromHttp(String url) {
//        RestGet api = new RestGet();
        return json;
    }
}
