package hkp.logimap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by krysztal on 02.01.16.
 */
class RESTtest extends AsyncTask<Void, Void, String> {
    private Context mContext;
    private View rootView;
    private TextView theView;

    RESTtest(TextView tv){
        this.theView = tv;
    }

    private Exception exception;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... urls) {
        try {
            final String basicAuth = "Basic " + Base64.encodeToString("admin:P@ssw0rd".getBytes(), Base64.NO_WRAP);
            URL url = new URL("http://10.0.2.2:8000/vehicles/1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        theView.setText("FINISH\n");
        Log.i("INFO", response);
        theView.append(response);
    }
}