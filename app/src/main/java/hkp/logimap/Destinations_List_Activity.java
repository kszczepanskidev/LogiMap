package hkp.logimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;

public class Destinations_List_Activity extends AppCompatActivity{
    MyApplication application;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_list_layout);
        setTitle("Destinations");

        bt = (Button) findViewById(R.id.finishbt);
        //Destinations list
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.destinations_list);
        ExpandableDestinationListAdapter adapter = new ExpandableDestinationListAdapter(this, application);
        lv.setAdapter(adapter);

        showButtonIfFinished();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showButtonIfFinished();
    }

    private void showButtonIfFinished() {
        if(application.current_delivery.finished)
            bt.setVisibility(View.VISIBLE);
        else
            bt.setVisibility(View.GONE);
    }

    public void clickFinishDelivery(View v) {
        application.current_delivery.finish(application);
    }
}
