package controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import model.GlobalVariables;
import model.Place;
import utils.SmartFragmentManager;

public class MainActivity extends AppCompatActivity implements EditDialog.EditDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(null);
        //getSupportActionBar().setDisplayUseLogoEnabled(false);

        setTitle(this.getResources().getString(R.string.mainTitle));
        GlobalVariables.getInstance().openDataBase(this);
        SmartFragmentManager.getInstance().setfManager(getFragmentManager());
        Fragment fragment = new MainActivityFrag();
        int containerViewId = R.id.content_frame;
        SmartFragmentManager.getInstance().ReplaceToFragment(fragment,containerViewId,"MainActivityFrag");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment fragment = new MainActivityFrag();
                int containerViewId = R.id.content_frame;
                SmartFragmentManager.getInstance().ReplaceToFragment(fragment, containerViewId,"MainActivityFrag");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(Bundle input) {

        Place  p = input.getParcelable("placeItem");
        if(p != null) {

            GlobalVariables.getInstance().showToast(R.string.savedChanges , new Object[]{p.getName()});
            MainActivityFrag mainfrag = (MainActivityFrag)SmartFragmentManager.getInstance().getfManager().findFragmentByTag("MainActivityFrag");
            if(mainfrag != null && mainfrag.isVisible()){
                mainfrag.showPlaces();
            }else{

                final PlacesActivityFrag placeFrag = (PlacesActivityFrag)SmartFragmentManager.getInstance().getfManager().findFragmentByTag("PlacesActivityFrag");
                if(placeFrag != null && placeFrag.isVisible()){

                    PlaceInfoFrag itemFrag;
                    Bundle args = new Bundle();
                    args.putLong("item_id",p.getId());

                    Fragment item_dialog = getFragmentManager().findFragmentByTag(getResources().getResourceName(R.string.dialogPlaceTag ));

                    if(item_dialog != null ){
                        itemFrag = (PlaceInfoFrag)item_dialog;
                        itemFrag.setPlaceInfo(p);
                    }
                    else{
                        FragmentManager fm = getFragmentManager();
                        itemFrag = new PlaceInfoFrag();
                        itemFrag.setArguments(args);
                        itemFrag.show(fm,getResources().getResourceName(R.layout.activity_place_info ));
                    }


                    /*MainActivity.this.runOnUiThread(new Runnable() {

                        public void run() {
                                placeFrag.getAdapter().notifyDataSetChanged();
                        }
                    });*/
                }
            }
            /*Intent intent = new Intent(this, PlacesActivityFrag.class);
            startActivity(intent);*/

        }
    }

    @Override
    protected void onResume() {
        GlobalVariables.getInstance().openDataBase(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        GlobalVariables.getInstance().closeDataBase();
        super.onPause();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        PlacesActivityFrag frag;

        public LongOperation(PlacesActivityFrag frag) {
            this.frag = frag;
        }

        @Override
        protected String doInBackground(String... params) {
           /* for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }*/
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if(frag != null && frag.isVisible()){
                frag.getAdapter().notifyDataSetChanged();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}


