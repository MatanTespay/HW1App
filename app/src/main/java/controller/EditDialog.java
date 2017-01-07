package controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import java.io.File;
import java.util.Random;

import model.DatabaseHelper;
import model.GlobalVariables;
import model.Place;
import utils.HelperClass;

import static android.R.attr.id;

/**
 * a DialogFragment representing the edit dialog for an item editing
 * Created by Matan on 25/11/2016.
 */

public class EditDialog extends DialogFragment implements TextView.OnEditorActionListener  {

    private EditDialog dialog = null;
    private ImageView img = null;
    private EditText name;
    private EditText desc;
    private Place placeItem ;
    private static final String CANCEL_ID = "cancel";
    private static final String SAVE_ID = "save";
    boolean isImageChange = false;
    Activity parentActivity;
    /**
     * Defines the listener interface with a method passing back data result.
     */
    public interface EditDialogListener {
        void onFinishEditDialog(Bundle data);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!GlobalVariables.getInstance().isDBOpen())
        GlobalVariables.getInstance().openDataBase(getActivity());
    }

    public EditDialog() {
        // Empty constructor required for DialogFragment
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialog = this;
        View view = inflater.inflate(R.layout.fragment_edit_name, container);
        name = (EditText) view.findViewById(R.id.dialogName);
        desc = (EditText) view.findViewById(R.id.dialogDesc);
        img = (ImageView) view.findViewById(R.id.newImg);

        ImagePicker.setMinQuality(100, 100);
        // Show soft keyboard automatically
        name.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getDialog().setTitle(R.string.PopUpEditTitle);
        EditDialogListener activity = (EditDialogListener) getActivity();
        this.parentActivity = getActivity();
        Bundle args = getArguments();
        Long id;
        if(args != null) {
            id = args.getLong("item_id");

            /**
             * check args passed from recycler view and set data
             */
            placeItem = GlobalVariables.getInstance().getItemById(id);
            if (placeItem != null) {
                name.setText(placeItem.getName());
                desc.setText(placeItem.getDescription());
                if (placeItem.getImgData() != null)
                    img.setImageBitmap(placeItem.getImgData());
            }
        }
        /*
        if (activity instanceof PlaceInfoActivity){
            // editing an existing place
            placeItem = ((PlaceInfoActivity)activity).getP();
            name.setText(placeItem.getName());
            desc.setText(placeItem.getDescription());
            if(placeItem.getImgData()!=null)
                img.setImageBitmap(placeItem.getImgData());

        }*/


        Button btnSave  = (Button) view.findViewById(R.id.btnDialogSave);
        Button btnCancel  = (Button) view.findViewById(R.id.btnDialogCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dialog_OnDismiss(view,SAVE_ID);
            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dialog_OnDismiss(view,CANCEL_ID);
            }

        });

        Button btnPickImg  = (Button) view.findViewById(R.id.btnPickImg);
        btnPickImg.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onPickImage(view);
            }

        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
        if (bitmap != null) {
            isImageChange = true;
            img.setImageBitmap(bitmap);
        }
    }

    /**
     * This method will be invoked when the the user wants to add picture
     * @param view
     */
    public void onPickImage(View view) {
        ImagePicker.pickImage(this, "Select picture from :");
    }

    /**
     * event handler for editing action of keyboard
     * @param v te alertQty element
     * @param actionId action id
     * @param event event type
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input alertQty to activity
            EditDialogListener activity = (EditDialogListener) getActivity();
            Bundle data = new Bundle();
            data.putParcelable("placeItem",placeItem);
            activity.onFinishEditDialog(data);
            this.dismiss();
            return true;
        }
        return false;
    }

    /**
     * This method will be invoked when the dialog is dismissed.
     * enable to save or dismiss the changes of the place
     * @param view the context view
     * @param btnCode the id @{@link String} of the trigger button
     */
    public void dialog_OnDismiss(View view, String btnCode) {
        EditDialogListener activity = (EditDialogListener) getActivity();
        Bitmap tempBitmap = null;
        String fileName  = "";
        long mili = System.currentTimeMillis();
        //boolean isImgSaved = false;

        // save image only if iy was changed.
        if(isImageChange){
            if(img != null && img.getDrawable() != null)
                tempBitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        }
        else{
            if(this.placeItem != null && placeItem.getImgData() != null) {
                tempBitmap = this.placeItem.getImgData();
            }
        }

        switch (btnCode){
            case SAVE_ID:


                if(this.placeItem == null){
                    // popup on main activity - adding new place
                    Place tempP  = new Place(mili ,name.getText().toString(),desc.getText().toString(), false, tempBitmap);
                    long newId = GlobalVariables.getInstance().addItem(tempP);
                    if(newId > -1){
                        placeItem = new Place(newId ,name.getText().toString(),desc.getText().toString(), false, tempBitmap);
                        if (activity instanceof MainActivity){
                            //GlobalVariables.getInstance().addItem(placeItem);
                            dialog.dismiss();
                            Bundle data = new Bundle();
                            data.putParcelable("placeItem",placeItem);
                            activity.onFinishEditDialog(data);

                        }
                    }else{
                        GlobalVariables.getInstance().showToast(R.string.ErrorChanges , null);
                    }

                }
                else{


                        placeItem.setName(name.getText().toString());
                        placeItem.setDescription(desc.getText().toString());
                        //placeItem.setImgFileName(fileName);
                        placeItem.setImgData(tempBitmap);
                        int result = GlobalVariables.getInstance().updateItem(placeItem);
                        if(result > 0)
                            GlobalVariables.getInstance().showToast(R.string.savedChanges , new Object[]{placeItem.getName()});
                        else
                            GlobalVariables.getInstance().showToast(R.string.ErrorChanges , null);

                        dialog.dismiss();
                        Bundle data = new Bundle();
                        data.putParcelable("placeItem",placeItem);
                        activity.onFinishEditDialog(data);

                }
                break;
            case CANCEL_ID:
                dialog.dismiss();
                break;

        }

    }

}
