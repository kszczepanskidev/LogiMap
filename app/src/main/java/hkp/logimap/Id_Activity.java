package hkp.logimap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by krysztal on 04.01.16.
 */
public class Id_Activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        if(!preferences.getBoolean("firstRun", true))
            goToMenu();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_layout);
    }

    public void clickSignIn(View v) {
        Encryptor encryptor = new Encryptor();

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor edit= preferences.edit();

        try {
            edit.putBoolean("firstRun", false);
            edit.putString("username", encryptor.encrypt(username));
            edit.putString("password", encryptor.encrypt(password));
            edit.commit();
        }catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        goToMenu();
    }

    void goToMenu() {
        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
        finish();
    }
}
