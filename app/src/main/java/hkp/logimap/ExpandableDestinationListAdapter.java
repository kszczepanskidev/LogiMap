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

import java.util.List;

public class ExpandableDestinationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listData;
    private Delivery _delivery;

    public ExpandableDestinationListAdapter(Context context, List<String> listData, Delivery delivery) {
        this._context = context;
        this._listData = listData;
        this._delivery = delivery;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listData.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String child = _listData.get(groupPosition);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_list_details, null);
        }

        TextView destination_state = (TextView) convertView.findViewById(R.id.DestinationStatus);
        TextView destination_date = (TextView) convertView.findViewById(R.id.DestinationTermin);
        TextView destination_load = (TextView) convertView.findViewById(R.id.DestinationLoad);

//        destination_state.setText("State: " + child.state);
//        destination_date.setText("Termin: " + child.date);
//        destination_load.setText(" " + child.load.size());
        destination_state.setText("State: Rotten");
        destination_date.setText( "Termin: Last christmas");
        destination_load.setText( "Packages: One hearth");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        String headerTitle = _listData.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.destination_list_group, null);
        }
        Button show_btn = (Button)convertView.findViewById(R.id.show_packages_btn);

        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
         public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), Package_List.class);
                i.putExtra("destination", _listData.get(groupPosition));
                i.putExtra("delivery", _delivery);
                parent.getContext().startActivity(i);
            }
        });

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
//        lblListHeader.setText("Delivery #" + headerTitle.id.toString());
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
