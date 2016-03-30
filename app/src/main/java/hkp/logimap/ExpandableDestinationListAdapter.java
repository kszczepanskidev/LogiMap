package hkp.logimap;

/**
 * Created by kryształ on 04.12.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExpandableDestinationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Location> _destinations;
    private MyApplication application;
    Delivery delivery;

    public ExpandableDestinationListAdapter(Context context, MyApplication app) {
        this._context = context;
        this.application = app;

        if(application.history_delivery != null)
            delivery = application.history_delivery;
        else
            delivery = application.current_delivery;

        this._destinations = new ArrayList<>(delivery.locations.values());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) { return this._destinations.get(groupPosition).name; }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Location child = _destinations.get(groupPosition);
        Integer _packageCounter = 0;

        for(Package p : delivery.packages)
            if(p.destination == child.id)
                _packageCounter++;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_list_details_layout, null);
        }

        ((TextView) convertView.findViewById(R.id.DestinationStatus)).setText("Name: " + child.name);
        ((TextView) convertView.findViewById(R.id.DestinationTermin)).setText("Termin: " + child.deadline);
        ((TextView) convertView.findViewById(R.id.DestinationLoad)).setText("Packages: " + _packageCounter);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._destinations.get(groupPosition).name;
    }

    @Override
    public int getGroupCount() { return this._destinations.size(); }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        Location header = _destinations.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.destination_list_group_layout, null);
        }
        Button show_btn = (Button)convertView.findViewById(R.id.show_packages_btn);
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_context, Package_List_Activity.class);
                i.putExtra("destinationID", _destinations.get(groupPosition).id);
                _context.startActivity(i);
            }
        });
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(header.name);
        if(header.shortDeadline)
            lblListHeader.setTextColor(Color.parseColor("#ff0000"));
        else if(header.finished)
            lblListHeader.setTextColor(Color.parseColor("#00ff00"));
        else
            lblListHeader.setTextColor(Color.parseColor("#646464"));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
