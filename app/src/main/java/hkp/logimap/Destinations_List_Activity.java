package hkp.logimap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;

public class Destinations_List_Activity extends AppCompatActivity{
    MyApplication application;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    Button finish_button;
    Delivery delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        edit = preferences.edit();

        if(application.history_delivery != null)
            delivery = application.history_delivery;
        else
            delivery = application.current_delivery;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list_layout);
        setTitle("Destinations");

        finish_button = (Button) findViewById(R.id.finishbt);
        //Destinations list
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.destinations_list);
        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, application);
        lv.setAdapter(adapter);

        showButtonIfFinished();
    }

    @Override
    protected void onResume() {
        showButtonIfFinished();
        super.onResume();
    }

    private void showButtonIfFinished() {
        if(!delivery.history && delivery.finished)
            finish_button.setVisibility(View.VISIBLE);
        else
            finish_button.setVisibility(View.GONE);
    }

    public void clickFinishDelivery(View v) {
        edit.putInt("prevDeliveryID", delivery.id);

        delivery.finish(application);

        application.current_delivery = null;
        edit.putBoolean("deliveryInFile", false);
        edit.commit();

        finish();
    }
}
