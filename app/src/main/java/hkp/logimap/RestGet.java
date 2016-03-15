package hkp.logimap;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by krysztal on 02.01.16.
 */
class RestGet extends AsyncTask<String, Void, String> {
    public interface AsyncResponse {
        void processFinish(String result);
    }
    public AsyncResponse delegate;

    Encryptor encryptor = new Encryptor();

    String username, password;

    RestGet(String username, String password, AsyncResponse delegate) {
        this.delegate = delegate;
        try {
            this.username = encryptor.decrypt(username);
            this.password = encryptor.decrypt(password);
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            final String basicAuth = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            URL url_address= new URL("http://10.0.2.2:8000/" +  params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url_address.openConnection();
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
            response = "ERROR";
        }
        delegate.processFinish(response);
    }
}