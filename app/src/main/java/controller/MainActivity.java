package controller;

import android.app.Activity;
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

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity implements EditDialog.EditDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(null);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        setTitle(this.getResources().getString(R.string.mainTitle));
        GlobalVariables.getInstance().openDataBase(this);
        SmartFragmentManager.getInstance().setfManager(getFragmentManager());
        SmartFragmentManager.getInstance().setSupportfragManager(getSupportFragmentManager());

        Bundle args = getIntent().getExtras();
        if(args != null){
            int tagId = args.getInt("fragTag");
            Fragment fragment = new PlacesActivityFrag();
            int containerViewId = R.id.content_frame;
            SmartFragmentManager.getInstance().ReplaceToFragment(fragment,containerViewId,getResources().getResourceName(tagId));

        }
        else{
            Fragment fragment = new MainActivityFrag();
            int containerViewId = R.id.content_frame;
            SmartFragmentManager.getInstance().ReplaceToFragment(fragment,containerViewId,getResources().getResourceName(R.string.MainActivityFrag));

        }

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

        GlobalVariables.getInstance().onFinishEditDialog(input,this);

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



}


