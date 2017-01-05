package controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.matan.hw1app.R;

import utils.SmartFragmentManager;

public class MainActivityFrag extends Fragment  {

    private Context context = null;
    private Button btnAddPlace = null;
    private Button btnPlaces = null;
    private Button btnFav = null;
    private Button btnProfile = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_frag, container, false);
        context = view.getContext();

        // get buttons
        btnPlaces = (Button) view.findViewById(R.id.btnPlaces);
        btnAddPlace = (Button) view.findViewById(R.id.btnAddPlace);
        btnFav = (Button) view.findViewById(R.id.btnFav);
        btnProfile = (Button) view.findViewById(R.id.btnProfile);
        prepareListeners();
        return view;
    }

    private void prepareListeners() {
        btnPlaces.setOnClickListener(btnPlacesListener);
        btnAddPlace.setOnClickListener(btnAddPlaceListener);
        btnFav.setOnClickListener(btnFavListener);
        btnProfile.setOnClickListener(btnProfileListener);

    }

    /**
     * Listener for btnPlaces to show all places
     * @param view
     */
    private View.OnClickListener btnPlacesListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(context, PlacesActivityFrag.class);
            startActivity(intent);*/
            showPlaces();
        }
    };

    public void showPlaces(){
        Fragment fragment = new PlacesActivityFrag();
        int containerViewId = R.id.content_frame;
        SmartFragmentManager.getInstance().ReplaceToFragment(fragment,containerViewId,"PlacesActivityFrag");
    }
    /**
     * Listener for btnFav to show all favorites
     * @param view
     */
    private View.OnClickListener btnFavListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, FavoriteActivities.class);
            startActivity(intent);
        }
    };

    /**
     * Listener for btnAddPlace to add place
     * @param view
     */
    private View.OnClickListener btnAddPlaceListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            showEditDialog();
        }
    };

    /**
     * function to show profile activity
     * @param view
     */
    private View.OnClickListener btnProfileListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //userProfileActivity
            Intent intent = new Intent(context, userProfileActivity.class);
            startActivity(intent);
        }
    };

    /**
     * show the popup edit dialog
     */
    private void showEditDialog() {
        if(getActivity() != null){
            FragmentActivity frma = (FragmentActivity)getActivity();
            FragmentManager fm = frma.getSupportFragmentManager();
            EditDialog editDialog = new EditDialog();
            editDialog.show(fm,getResources().getResourceName(R.layout.fragment_edit_name ));
        }
    }


}
