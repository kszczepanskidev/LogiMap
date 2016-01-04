package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by krysztal on 02.01.16.
 */
public class RESTtestView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resttestlay);
    }

    public void runREST(View v) {
        RESTtest wtf = new RESTtest(((TextView) findViewById(R.id.responseView)));
        wtf.execute();
    }
}
