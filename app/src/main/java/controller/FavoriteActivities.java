package controller;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.matan.hw1app.R;

import java.util.ArrayList;
import java.util.List;

import model.GlobalVariables;
import model.Place;

/**
 * Created by Matan on 26/11/2016.
 */
public class FavoriteActivities extends AppCompatActivity implements EditDialog.EditDialogListener  {

    private ListView list ;
    private CustomListViewAdapter adapter;
    private Context context;
    EditText inputSearch;
    List<Place> items;
    List<Place> favItems;
    List<Place> searchResult;
    private Button deletePlaceBtn;
    RelativeLayout rl;
    private boolean isEditMode = false;
    MenuItem editItem;
    AlertDialog alert;
    String alertQty;
    int select = 0;
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
        alert = AskOption();

        deletePlaceBtn = (Button) findViewById(R.id.deleteBtn);


        //btnEdit = (Button) findViewById(R.id.btnEditFav);
        rl =  (RelativeLayout) findViewById(R.id.action_layout);
        setDeleteBtnVisibility(true);

        list = (ListView) findViewById(R.id.listFav);
        deletePlaceBtn.setOnClickListener(deletePlaceListener);
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

    public void setDeleteBtnVisibility(Boolean state) {

            deletePlaceBtn.setVisibility((state == true) ? View.GONE : View.VISIBLE);

    }

    private void onEditClick() {

        if(isEditMode){
            isEditMode = false;
            editItem.setTitle(R.string.editLbl);
            updateAapterListItems(isEditMode);
            setDeleteBtnVisibility(true);
            //((Button)v).setText(getResources().getString(R.string.editLbl));
        }
        else{
            isEditMode = true;
            editItem.setTitle(R.string.closeLbl);
            setDeleteBtnVisibility(false);
            updateAapterListItems(isEditMode);
            //((Button)v).setText(getResources().getString(R.string.closeLbl));
        }
    }

    public void updateDataAfterEdit(){
        getData();
        updateAapterListItems(false);
    }
    private void updateAapterListItems(boolean edit) {

        adapter = new CustomListViewAdapter(this.context,R.layout.itemlayout,searchResult,isEditMode );
        list.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_places:
                Intent intent = new Intent(context,MainActivity.class );
                Bundle args = new Bundle();
                args.putInt("fragTag", R.string.MainActivityFrag);
                intent.putExtras(args);
                startActivity(intent);
                return true;
            case R.id.action_profile:
                intent= new Intent(context, userProfileActivity.class);
                startActivity(intent);
            case R.id.action_edit:
                onEditClick();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        GlobalVariables.getInstance().closeDataBase();
        super.onPause();
    }

    private void getData(){
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalVariables.getInstance().openDataBase(this.context);

        getData();
        adapter = new CustomListViewAdapter(this.context,R.layout.itemlayout,searchResult,isEditMode );
        //setListAdapter(adapter);

        list.setAdapter(adapter);

       if (favItems.size() == 0) {
           GlobalVariables.getInstance().showToast( R.string.nofav, null );

        }
        else{
           list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   if(isEditMode)
                       return;
                   long id = ((Place)adapterView.getItemAtPosition(i)).getId();
                   Bundle args = new Bundle();
                   args.putLong("item_id",id);
                   showEditPlaceDialog(args, 1);

               }
           });

           list.setLongClickable(true);
           list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> absListView, View view,
                                              int pos, long id) {
                   // TODO Auto-generated method stub
                   onEditClick();
                   return true;
               }
           });

           if(inputSearch.getText().toString().length() > 0){
               filterSearch();
           }

       }




    }

    private void deletePlace(){
        List<Place> deletedList = adapter.getSelectedItems();
        if(deletedList!=null && deletedList.size()>0){
            for(Place delItem :deletedList){
                GlobalVariables.getInstance().removeItem(delItem);
            }

            getData();
            updateAapterListItems(true);
        }
    }
    private View.OnClickListener deletePlaceListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(adapter.getSelectedCount() > 0)
            alert.show();
        }
    };

    private void setText(){
        select = adapter.getSelectedCount();
        alertQty = getResources().getQuantityString(R.plurals.deletePlacesQty, select, select);

    }
/*    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {

        super.onListItemClick(l, view, position, id);
        Place item = (Place) getListAdapter().getItem(position);
        long item_id = item.getId();

        Bundle args = new Bundle();
        args.putLong("item_id",item_id);
        showEditPlaceDialog(args, 1);



    }*/

    /**
     *  invoke the {@link EditDialog} for editing or {@link PlaceInfoFrag} for viewing place object
     * @param args Bundle object of params <b>Object ID</b>
     * @param fragmentId id representing which dialog to open:
     *<p>1 - {@link PlaceInfoFrag}, 2 - {@link EditDialog}</p>
     */
    public void showEditPlaceDialog(Bundle args, int fragmentId){
        FragmentManager fm = getFragmentManager();
        String tag = "";
        DialogFragment dialog = null;
        switch (fragmentId) {
            case 1:
                dialog = new PlaceInfoFrag();
                dialog.setArguments(args);
                tag = context.getResources().getResourceName(R.string.dialogPlaceTag );
                break;
            case 2:
                dialog = new EditDialog();
                dialog.setArguments(args);
                tag = context.getResources().getResourceName(R.string.dialogEditTag );
                break;
            default:
                break;
        }

        if(dialog != null)
        dialog.show(fm,tag);

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

        if(favItems == null) return;

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

    @Override
    public void onFinishEditDialog(Bundle data) {

        GlobalVariables.getInstance().onFinishEditDialog(data,this);

    }

    public CustomListViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.fav_frag_menu, menu);
        editItem = menu.findItem(R.id.action_edit);

        return (super.onCreateOptionsMenu(menu));
    }

    private AlertDialog AskOption()
    {



        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.alertPopTitle)
                .setMessage(R.string.deletePlacesMsg)
                .setIcon(R.drawable.attention_48)
                .setPositiveButton(R.string.alertLblPositive, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        deletePlace();
                    }

                })
                .setNegativeButton(R.string.alertLblNegative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
