package controller;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matan.hw1app.R;

import java.util.List;

import model.Place;
import utils.HelperClass;

/**
 *  custom list adapter to populate the list items in RecyclerView object
 * Created by Matan on 24/11/2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private List<Place> items = null;
    private Activity mainActivity = null;
    HelperClass h;
    private static final String FRAG_TAG = "place_info_frag";

    /**
     * ctor of the class
     * @param mainActivity the class mainActivity
     * @param items the items to populate in the view
     */
    public CustomRecyclerAdapter(Context mainActivity, List<Place> items) {
        this.items = items;
        this.mainActivity = (Activity) mainActivity;
        mLayoutInflater = LayoutInflater.from(mainActivity);
    }

    /**
     *
     * @return the size of the list items
     */
    public int getItemCount() {
        return items.size();
    }


    public List<Place> getItems () {
        return items;
    }

    public Place getPlaceById(Long id){

        for (Place p: items) {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.recycler_item, viewGroup, false));
    }

    /**
     * bind each view holder with place item in th recycler view
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Place item = items.get(position);
        viewHolder.setData(item);

        /**
         * when clicking on item in recycler
         */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AnimalDetailsFragment detailsFragment = new AnimalDetailsFragment();
                // send data to fragment
                Place selectedItem = items.get(position);
                Bundle args = new Bundle();
                args.putLong("item_id",selectedItem.getId());


                //open the place item dialog fragment
                if(mainActivity instanceof  FragmentActivity){
                    FragmentActivity frma = (FragmentActivity) mainActivity;
                    FragmentManager fm = frma.getFragmentManager();
                    PlaceInfoFrag itemFrag = new PlaceInfoFrag();
                    itemFrag.setArguments(args);
                    //itemFrag.show(fm,mainActivity.getResources().getResourceName(R.layout.fragment_edit_name ));
                    itemFrag.show(fm, mainActivity.getResources().getResourceName(R.string.dialogPlaceTag ));

                }


            }
        });
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mainActivity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.recycler_item, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.r_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.r_image);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.r_description);
        Place item = items.get(position);
        txtName.setText(item.getName());

        String fileName;

        if(item.getImgData() != null)
            imageView.setImageBitmap(item.getImgData());


        txtDesc.setText(item.getDescription());

        return rowView;

    }

    public void updatePlace(Place p) {
        Place placeToUpdate = getPlaceById(p.getId());
        int idx = items.indexOf(placeToUpdate);
        if(placeToUpdate != null && idx > -1){
            items.set(idx,p);
            notifyDataSetChanged();
        }

    }
}

/**
 * object the holds the data for each item in list
 */
class ViewHolder extends RecyclerView.ViewHolder  {
    // Views
    private ImageView image;
    private TextView name;
    private TextView desc;

    public ViewHolder(View itemView) {
        super(itemView);

        // Get references to image and name.
        image = (ImageView) itemView.findViewById(R.id.r_image);
        name = (TextView) itemView.findViewById(R.id.r_name);
        desc = (TextView) itemView.findViewById(R.id.r_description);
    }

    /**
     * set data in the view holder
     * @param item the item with the data to populate
     */
    public void setData(Place item) {
        image.setImageBitmap(item.getImgData());
        name.setText(item.getName());
        desc.setText(item.getDescription());
    }

}
