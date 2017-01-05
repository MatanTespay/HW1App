package controller;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import java.util.ArrayList;
import java.util.List;

import model.DatabaseHelper;
import model.GlobalVariables;
import model.Place;
import utils.HelperClass;

import static com.example.matan.hw1app.R.id.inputSearch;

/**
 * Created by Matan on 26/11/2016.
 */
public class FavoriteActivities extends ListActivity {

    private ListView list;
    private CustomListAdapter adapter;
    private Context context;
    EditText inputSearch;
    List<Place> items;
    List<Place> favItems;
    List<Place> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_list);
        this.context = this;
        setTitle(R.string.FavTitle);
        //populatePlacesList();
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                filterSearch();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //items = GlobalVariables.getInstance().items;
        items  = GlobalVariables.getInstance().getAllItems();
        favItems  = new ArrayList<>();
        searchResult =   new ArrayList<>();
        //list = (ListView) findViewById(R.id.list);

        // add all favorites place to the view list
        for (Place p : items)
        {
            if (p.isFavorite()) {
                favItems.add(p);
                searchResult.add(p);
            }
        }

        adapter = new CustomListAdapter(this, searchResult);
        //setListAdapter(adapter);

        //list.setAdapter(adapter);

       if (favItems.size() == 0) {
           GlobalVariables.getInstance().showToast( R.string.nofav, null );
        }
        else{
           if(inputSearch.getText().toString().length() > 0){
               filterSearch();
           }
       }




    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {

        super.onListItemClick(l, view, position, id);
        Place item = (Place) getListAdapter().getItem(position);
        long item_id = item.getId();
        String itemName = ((TextView) view.findViewById(R.id.name)).getText().toString();
        Intent intent = new Intent(context,PlaceInfoActivity.class );
        //intent.putExtra("item_name", itemName);
        intent.putExtra("item_id", item_id);
        context.startActivity(intent);

    }

    private void filterSearch(){
        // When user changed the Text
        String searchString = inputSearch.getText().toString();
        int textLength = searchString.length();

        //clear the initial data set
        if(searchResult != null)
        searchResult.clear();
        else
        searchResult = new ArrayList<>();

        for(int i=0;i< favItems.size();i++)
        {
            String placeName = favItems.get(i).getName().toString();

            if(textLength <= placeName.length()){
                //compare the String in EditText with Names in the ArrayList
                if(searchString.equalsIgnoreCase(placeName.substring(0,textLength)))
                    searchResult.add(favItems.get(i));
            }
        }

        adapter.notifyDataSetChanged();
    }
}
