package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by krysztal on 02.01.16.
 */
public class RESTtestView_Activity extends AppCompatActivity {
    Encryptor encryptor = new Encryptor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resttestlay);
    }

    public void runREST(View v) {
        RestGet api = new RestGet(((TextView) findViewById(R.id.responseView)));
        try {
            api.execute("vehicles/1", "user", encryptor.encrypt("useruser"));
        }catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
