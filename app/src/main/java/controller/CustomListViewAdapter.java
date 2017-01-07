package controller;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.matan.hw1app.R;

import java.util.ArrayList;
import java.util.List;

import model.Place;
import utils.HelperClass;

import static android.R.attr.resource;
import static android.R.id.list;

/**
 *  custom list adapter to populate the list items in viewlist object
 * Created by Matan on 24/11/2016.
 */

public class CustomListViewAdapter extends ArrayAdapter<Place> implements CompoundButton.OnCheckedChangeListener {

    private LayoutInflater mLayoutInflater;
    private List<Place> items = null;
    private Activity context = null;
    private SparseBooleanArray mCheckStates;
    private boolean edit = false;

    HelperClass h;
    private static final String FRAG_TAG = "place_info_frag";

    /**
     * ctor of the class
     * @param context
     * @param items
     */

    /**
     *
     * @param context the class context
     * @param resource the layout of row
     * @param objects the items to populate in the view
     */
    public CustomListViewAdapter(Context context, int resource, List<Place> objects, boolean edit ) {
        super(context, resource, objects);
        this.items = objects;
        this.context = (Activity)context;
        mCheckStates = new SparseBooleanArray(items.size());
        this.edit = edit;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     *
     * @return the size of the list items
     */
    public int getItemCount() {
        return items.size();
    }



    public int getSelectedCount(){
        int c = 0;
        for(int i=0;i<getCount();i++)
        {
            if(mCheckStates.get(i)==true)
            {
                c++;
            }
        }

        return  c;
    }

    public List<Place> getSelectedItems(){
        List<Place> list = new ArrayList<Place>();

        for(int i=0;i<getCount();i++)
        {
            if(mCheckStates.get(i)==true)
            {
                Place p = getItem(i);
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }

    private class ViewContainer {
        TextView title;
        TextView description;
        CheckBox chkSelect;
        ImageView bImg;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewContainer holder = null;
        Place placeItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.itemlayout, null);
            holder = new ViewContainer();
            holder.title = (TextView) convertView.findViewById(R.id.name);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.chkSelect = (CheckBox) convertView.findViewById(R.id.item_chk);
            holder.bImg = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        }
         else{
            holder = (ViewContainer) convertView.getTag();
        }

        if (edit) {
            holder.chkSelect.setVisibility(View.VISIBLE);
        } else {
            holder.chkSelect.setVisibility(View.GONE);
        }

        holder.title.setText(placeItem.getName());
        holder.description.setText(placeItem.getDescription());
        holder.chkSelect.setTag(position);
        holder.chkSelect.setChecked(mCheckStates.get(position, false));
        holder.chkSelect.setOnCheckedChangeListener(this);
        if(placeItem.getImgData() != null)
            holder.bImg.setImageBitmap(placeItem.getImgData());
        return convertView;

/*
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.itemlayout, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);
        Place item = items.get(position);
        txtName.setText(item.getName());

        if(item.getImgData() != null)
            imageView.setImageBitmap(item.getImgData());


        txtDesc.setText(item.getDescription());

        return rowView;*/

    }

    public Place getPlaceById(Long id){

        for (Place p: items) {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }

    public List<Place> getItems() {
        return items;
    }
}

