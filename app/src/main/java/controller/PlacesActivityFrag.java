package controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import java.util.List;

import model.DatabaseHelper;
import model.GlobalVariables;
import model.Place;
import utils.HelperClass;

public class PlacesActivityFrag extends Fragment {


    private ListView list;
    private CustomListAdapter adapter;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_places, container, false);
        context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GlobalVariables.getInstance().openDataBase(context);
        List<Place> items = GlobalVariables.getInstance().getAllItems();
        Activity act = getActivity();
        if (act != null) {
            act.setTitle(act.getResources().getString(R.string.AllPlace));
            recyclerView.setLayoutManager(new GridLayoutManager(act,1));
            recyclerView.setAdapter(new CustomListAdapter(context, items));

        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        GlobalVariables.getInstance().openDataBase(context);
        List<Place> items = GlobalVariables.getInstance().getAllItems();

        //list = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter(this.context, items);
        //list.setAdapter(adapter);

/*        if (items.size() == 0) {
            h.showToast( R.string.NoPlaces, null );
            //Toast.makeText(this, R.string.NoPlaces, Toast.LENGTH_SHORT).show();
        }
        else{
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    long id = ((Place)adapterView.getItemAtPosition(i)).getId();
                    String itemName = ((TextView) view.findViewById(R.id.name)).getText().toString();
                    Intent intent = new Intent(context,PlaceInfoActivity.class );
                    //intent.putExtra("item_name", itemName);
                    intent.putExtra("item_id", id);
                    context.startActivity(intent);

                }
            });
        }*/
    }


    @Override
    public void onPause() {
        GlobalVariables.getInstance().closeDataBase();
        super.onPause();
    }

    public CustomListAdapter getAdapter() {
        return adapter;
    }
}
