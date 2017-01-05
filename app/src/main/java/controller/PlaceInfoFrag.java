package controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matan.hw1app.R;

import java.io.File;

import model.GlobalVariables;
import model.Place;

import static com.example.matan.hw1app.R.id.btnPickImg;
import static com.example.matan.hw1app.R.id.btnSave;

//import static com.example.matan.hw1app.R.id.item;

public class PlaceInfoFrag extends DialogFragment {

    private PlaceInfoFrag dialog = null;
    private ImageView img = null;
    private TextView name;
    private TextView desc;
    private long id;
    private Place p;
    AlertDialog alert;
    private int dialogResult = -1;
    private static final String SUBJECT_PREFIX = "share place details";
    private static final String MSG_PREFIX = "The place name is  : ";
    private static final String SHARE_TITLE = "Share via...";

    private Button btnEdit;
    private Button btnRemove;
    private Button btnShare;
    private Button btnFav;
    View fragView;

    public Place getP() {
        return p;
    }

    public void setP(Place p) {
        this.p = p;
    }

    public PlaceInfoFrag() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialog = this;
        fragView = inflater.inflate(R.layout.activity_place_info, container);
        getDialog().setTitle(R.string.PlaceInfoTitle);
        alert = AskOption();

        btnEdit  = (Button) fragView.findViewById(R.id.btnEdit);
        btnRemove  = (Button) fragView.findViewById(R.id.btnRemove);
        btnShare  = (Button) fragView.findViewById(R.id.btnShare);
        btnFav  = (Button) fragView.findViewById(R.id.btnFav);
        setClickListeners();
        return  fragView;
    }

    /**
     *  add on click listeners to buttons
     */
    private void setClickListeners(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showEditDialog();
            }

        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                alert.show();
            }

        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                shareIt(view);
            }

        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                p.setFavorite(true);
                GlobalVariables.getInstance().updateItem(p);
                Intent intent = new Intent(getActivity(), FavoriteActivities.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * This function will be invoked when sharing the place info
     * @param v - the view of context
     */
    private void shareIt(View v) {


        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        sharingIntent.setType("*/*");

        String imgName;
        String fileName;

        sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT_PREFIX);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, MSG_PREFIX + ((p != null) ? p.getName() : ""));

        // add picture data to share intent picture exist if not empty
        /*if(!p.getImgFileName().equals("")){

                imgName = p.getImgFileName();
                File imageFile = new File(Environment.getExternalStorageDirectory(), imgName);
                String imagePath = imageFile.getAbsolutePath();
                Uri attachment = Uri.fromFile(new File(imagePath));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        }*/

        // save the image to disk so we can use the Uri to share
        if (p.getImgData() != null) {
            imgName = p.getId().toString() + ".jpg";
            GlobalVariables.getInstance().saveSnap(p.getImgData(), imgName);
            File imageFile = new File(Environment.getExternalStorageDirectory(), imgName);
            String imagePath = imageFile.getAbsolutePath();
            Uri attachment = Uri.fromFile(new File(imagePath));
            sharingIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        }

        //invoke the share intent
        startActivity(Intent.createChooser(sharingIntent, SHARE_TITLE));
    }


/*    @Override
    public void onFinishEditDialog(Bundle input) {
        Place p = input.getParcelable("placeItem");
        if (p != null) {
            setPlaceInfo(p);
            //Toast.makeText(this, R.string.savedChanges , Toast.LENGTH_SHORT).show();
            GlobalVariables.getInstance().showToast( R.string.savedChanges, new Object[] { p.getName()});
        }
    }*/

    /**
     * invoke the {@link EditDialog} Edit Popup Dialog for editing the place
     */
    private void showEditDialog() {
        FragmentActivity frma = (FragmentActivity)getActivity();
        FragmentManager fm = frma.getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putLong("item_id",p.getId());
        EditDialog editDialog = new EditDialog();
        editDialog.setArguments(args);
        editDialog.show(fm, "fragment_edit_name");
    }

    @Override
    public void onResume() {
        super.onResume();

        GlobalVariables.getInstance().openDataBase(getActivity());
        Bundle args = getArguments();
        //String name = intent.getStringExtra("item_name");
        this.id = args.getLong("item_id");
        p = GlobalVariables.getInstance().getItemById(this.id);
        //p =  dbHelper.getRow(this.id);

        if(p == null){
            this.dismiss();
        }else{
            setPlaceInfo(p);
        }
    }

    /**
     * set the place info details when needed
     * @param p
     */
    public void setPlaceInfo(Place p) {

        if(fragView != null){
            name = (TextView) fragView.findViewById(R.id.placeTitle);
            desc = (TextView) fragView.findViewById(R.id.placeDescription);
            img = (ImageView) fragView.findViewById(R.id.placeImg);

            name.setText(p.getName());
            desc.setText(p.getDescription());
            if(p.getImgData() != null){
                img.setImageBitmap(p.getImgData());
            }
        }
    }

    /**
     * This function construct the alert dialog
     * @return {@link AlertDialog} an alert dialog
     */
    private AlertDialog AskOption()
    {


        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle(R.string.alertPopTitle)
                .setMessage(R.string.alertPopMsg)
                .setIcon(R.drawable.attention_48)
                .setPositiveButton(R.string.alertLblPositive, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                            if(p!=null){
                                String title  = p.getName();
                                GlobalVariables.getInstance().removeItem(p);
                                dialog.dismiss();
                                GlobalVariables.getInstance().showToast(R.string.removeMsg, new Object[] {title} );
                            }
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
