package controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import org.w3c.dom.Text;

import java.io.File;

import model.DatabaseHelper;
import model.GlobalVariables;
import model.Place;
import utils.HelperClass;

//import static com.example.matan.hw1app.R.id.item;

public class PlaceInfoActivity extends AppCompatActivity implements EditDialog.EditDialogListener {

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

    public Place getP() {
        return p;
    }

    public void setP(Place p) {
        this.p = p;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        setTitle(R.string.PlaceInfoTitle);
        alert = AskOption();

    }


    /**
     * This function will be invoked when sharing the place info
     * @param v - the view of context
     */
    private void shareIt(View v) {


        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        sharingIntent.setType("*/*");

        String imgName;
        String fileName;

        sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {""});
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT_PREFIX);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, MSG_PREFIX + ((p != null) ? p.getName() : ""));

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

    /**
     * This function will be invoked when removing an place item
     * @param view
     */
    public void btnRemove_onClick(View view) {
        alert.show();
    }

    /**
     * event handler for editing
     * @param view
     */
    public void btnEdit_onClick(View view) {

        showEditDialog();
    }

    /**
     * add place item to Favorite list
     * @param view
     */
    public void btnAddToFav_onClick(View view) {
        p.setFavorite(true);
        Intent intent = new Intent(this, FavoriteActivities.class);
        startActivity(intent);
    }

    /**
     * share event handler
     * @param view
     */
    public void btnShare_onClick(View view) {
        shareIt(view);
    }

    @Override
    public void onFinishEditDialog(Bundle input) {
        Place p = input.getParcelable("placeItem");
        if (p != null) {
            setPlaceInfo(p);
            //Toast.makeText(this, R.string.savedChanges , Toast.LENGTH_SHORT).show();
            GlobalVariables.getInstance().showToast( R.string.savedChanges, new Object[] { p.getName()});
        }
    }

    /**
     * invoke the {@link EditDialog} Edit Popup Dialog for editing the place
     */
    private void showEditDialog() {
        android.app.FragmentManager fm = getFragmentManager();
        EditDialog editDialog = new EditDialog();
        editDialog.show(fm, "fragment_edit_name");
    }

    @Override
    protected void onResume() {
        super.onResume();

        GlobalVariables.getInstance().openDataBase(this);
        Intent intent = getIntent();
        //String name = intent.getStringExtra("item_name");
        this.id = intent.getLongExtra("item_id",0);
        p = GlobalVariables.getInstance().getItemById(this.id);
        //p =  dbHelper.getRow(this.id);

        if(p == null){
            finish();
        }else{
            setPlaceInfo(p);
        }
    }

    /**
     * set the place info details when needed
     * @param p
     */
    private void setPlaceInfo(Place p) {


        name = (TextView) findViewById(R.id.placeTitle);
        desc = (TextView) findViewById(R.id.placeDescription);
        img = (ImageView) findViewById(R.id.placeImg);

        name.setText(p.getName());
        desc.setText(p.getDescription());
        String fileName;
        if(p.getImgData() != null){
            img.setImageBitmap(p.getImgData());
        }
        /*if(!p.getImgFileName().equals("")){
            fileName = p.getImgFileName();
            Bitmap imgTemp = h.loadSnap(fileName);
            if(imgTemp != null)
                img.setImageBitmap(imgTemp);
        }*/

    }

    /**
     * This function construct the alert dialog
     * @return {@link AlertDialog} an alert dialog
     */
    private AlertDialog AskOption()
    {


        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.alertPopTitle)
                .setMessage(R.string.alertPopMsg)
                .setIcon(R.drawable.attention_48)
                .setPositiveButton(R.string.alertLblPositive, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        Activity parent = PlaceInfoActivity.this;
                        if (parent instanceof PlaceInfoActivity){
                            Place p =   ((PlaceInfoActivity)parent).getP();
                            if(p!=null){
                                String title  = p.getName();
                                long result = GlobalVariables.getInstance().removeItem(p);
                                dialog.dismiss();
                                parent.finish();
                                GlobalVariables.getInstance().showToast(((result > 0) ? R.string.removeMsg : R.string.errorRemoveMsg), new Object[] {title} );
                            }

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
