package hkp.logimap;

/**
 * Created by kryształ on 04.12.2015.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExpandablePackageListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Package> _listData;
    private MyApplication application;
    private Integer _destinationID;

    public ExpandablePackageListAdapter(Context context, MyApplication app, List<Package> packages) {
        this._context = context;
        this.application = app;
        this._listData = packages;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) { return this._listData.get(groupPosition); }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Package child = _listData.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.package_list_details_layout, null);
        }

        Button show_btn = (Button)convertView.findViewById(R.id.change_state);
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_context, Change_State_Activity.class);
                i.putExtra("packageID", child.id);
                i.putExtra("locationID", child.destination);
                i.putExtra("packageCode", child.code);
                _context.startActivity(i);
            }
        });


        ((TextView) convertView.findViewById(R.id.PackageStatus)).setText("State: " + application.package_states.get(child.state - 1));
        ((TextView) convertView.findViewById(R.id.PackageTermin)).setText("Deadline: " + child.deadline);

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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = _listData.get(groupPosition).code;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.package_list_group_layout, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText("Package #" + headerTitle);

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
