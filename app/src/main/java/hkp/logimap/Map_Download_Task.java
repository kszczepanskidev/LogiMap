package hkp.logimap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Paweł Plichta on 2015-11-25.
 */


public class Map_Download_Task extends AsyncTask<String, Void, String> {


    private static final String TAG = Map_Download_Task.class.getCanonicalName();
    private String RouteID;
    private Context context;

    public Map_Download_Task(Context cntext, String id){
        RouteID=id;
        context=cntext;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        FileOutputStream outputStream;
        File file = new File(context.getFilesDir(),RouteID+"_groute.json");
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(result.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
