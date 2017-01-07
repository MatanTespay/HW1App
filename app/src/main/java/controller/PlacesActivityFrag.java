package controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.matan.hw1app.R;

import java.util.List;

import model.GlobalVariables;
import model.Place;
import utils.SmartFragmentManager;

public class PlacesActivityFrag extends Fragment {


    private ListView list;
    private CustomRecyclerAdapter adapter;
    RecyclerView recyclerView;
    private Context context;
    List<Place> items;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_places, container, false);
        context = view.getContext();
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setDate();

        return view;
    }

    public void setDate(){
        GlobalVariables.getInstance().openDataBase(context);
        items = GlobalVariables.getInstance().getAllItems();
        adapter = new CustomRecyclerAdapter(this.context, items);
        Activity act = getActivity();
        if (act != null) {
            act.setTitle(act.getResources().getString(R.string.AllPlace));
            recyclerView.setLayoutManager(new GridLayoutManager(act,1));
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.places_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment fragment = new MainActivityFrag();
                int containerViewId = R.id.content_frame;
                SmartFragmentManager.getInstance().ReplaceToFragment(fragment, containerViewId,"MainActivityFrag");
                return true;
            case R.id.action_profile:
                intent= new Intent(context, userProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_favorites:
                intent = new Intent(context, FavoriteActivities.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        setDate();
        /*GlobalVariables.getInstance().openDataBase(context);
        items = GlobalVariables.getInstance().getAllItems();

        //list = (ListView) findViewById(R.id.list);

        adapter = new CustomRecyclerAdapter(this.context, items);*/
        if (items.size() == 0) {
            GlobalVariables.getInstance().showToast(R.string.NoPlaces,null);
        }

    }


    @Override
    public void onPause() {
        GlobalVariables.getInstance().closeDataBase();
        super.onPause();
    }

    public CustomRecyclerAdapter getAdapter() {
        return adapter;
    }


}
