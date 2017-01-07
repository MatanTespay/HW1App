package utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.matan.hw1app.R;

import controller.FavoriteActivities;
import controller.MainActivity;
import controller.PlacesActivityFrag;

/**
 * Created by Matan on 07/01/2017.
 */

public class MyUiAsync extends AsyncTask<String, Void, String> {

    Activity act;

    public MyUiAsync(Activity act) {
        this.act = act;
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
        if(act != null){
            if(act instanceof MainActivity){
                PlacesActivityFrag placesFrag = (PlacesActivityFrag)SmartFragmentManager.getInstance().getfManager().findFragmentByTag(act.getResources().getString(R.string.PlacesActivityFrag));
                placesFrag.getAdapter().notifyDataSetChanged();
            }
            else if(act instanceof FavoriteActivities){
                ((FavoriteActivities)act).getAdapter().notifyDataSetChanged();

            }


               /* ((MainActivity)act).getAdapter().notifyDataSetChanged();
                else if()*/
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
