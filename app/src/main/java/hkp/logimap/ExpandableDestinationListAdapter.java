package hkp.logimap;

/**
 * Created by kryształ on 04.12.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpandableDestinationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Destination> _destinations;

    public ExpandableDestinationListAdapter(Context context, List<Destination> listDestinations) {
        this._context = context;
        this._destinations = listDestinations;
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
        final Destination child = _destinations.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_list_details, null);
        }
        ((TextView) convertView.findViewById(R.id.DestinationStatus)).setText("State: " + child.state);
        ((TextView) convertView.findViewById(R.id.DestinationTermin)).setText("Termin: " + child.date);
        ((TextView) convertView.findViewById(R.id.DestinationLoad)).setText("Packages: " + child.packages.size());

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
        String headerTitle = _destinations.get(groupPosition).name;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.destination_list_group, null);
        }

        Button show_btn = (Button)convertView.findViewById(R.id.show_packages_btn);
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
         public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), Package_List.class);
                i.putExtra("destination", _destinations.get(groupPosition));
                parent.getContext().startActivity(i);
            }
        });
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
