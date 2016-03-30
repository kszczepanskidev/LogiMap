package hkp.logimap;

/**
 * Created by krysztal on 23.01.16.
 */
public class PUT_Request {
    String url, newjson;
    Boolean done;

    PUT_Request(String url, String json) {
        this.url = url;
        this.newjson = json;
        this.done = false;
    }
}
