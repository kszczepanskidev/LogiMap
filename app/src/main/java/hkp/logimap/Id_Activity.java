package hkp.logimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by krysztal on 04.01.16.
 */
public class Id_Activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        //Load user data from settings


        //If there's no saved user data continue with activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_layout);
    }

    public void clickSignIn(View v) {
        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
    }
}
