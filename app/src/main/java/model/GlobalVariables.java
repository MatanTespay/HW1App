package model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Matan on 24/11/2016.
 */

public class GlobalVariables {
    //public ArrayList<Place> items;
    private Context context = null;
    private ArrayList<String> locales;
    DatabaseHelper db;

    private GlobalVariables() {
        //items = new ArrayList<Place>();
        this.locales = getCountries();
    }

    private static GlobalVariables instance;

    public static GlobalVariables getInstance() {
        if (instance == null) instance = new GlobalVariables();
        return instance;
    }

    public Context getContext() {
        return context;

    }

    public void openDataBase(Context context) {
        this.context = context;
        if (context != null) {
            db = new DatabaseHelper(context);
            db.open();
        }
    }

    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    public long addItem(Place item){
      return  db.insertRow(item.getId(),item.getName(), item.getDescription(),item.isFavorite,item.getImgData());
    }

    public int removeItem(Place item){
       return db.deleteRow(item.getId());
    }

    public int updateItem(Place item){
        return db.updateRow(item.getId(),item.getName(), item.getDescription(),item.isFavorite,item.getImgData());
    }


/*    public Place getItemByName(String name) {

        Place p = null;
        for(Place place:items){
            if(name.equals(place.getName())){
                p = place;
                break;
            }
        }
        return p;
    }*/

    public List<Place> getAllItems(){
        return db.getAllRows();
    }


    public Place getItemById(long id) {


        /*Place p = null;
        for(Place place:items){
            if(place.getId() == id){
                p = place;
                break;
            }
        }*/

        return db.getRow(id);
    }

    /**
     * get the list of Local system location
     * @return {@link ArrayList} of countries
     */
    public  ArrayList<String> getCountries(){

        Locale[] loc  = Locale.getAvailableLocales();

        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : loc) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);

        return countries;
    }


    /**
     * get the country index by its name
     * @param name - the country name
     * @return - the index
     */
    public int getCountryIdxByName(String name){
        int idx = this.locales.indexOf(name);
        return  idx;
    }

    public void showToast(int StringId,  Object... args){
        Resources res = this.context.getResources();
        String text;
        if(args != null)
            text = String.format(res.getString(StringId),args );
        else
            text = res.getString(StringId);

        Toast.makeText(this.context ,text , Toast.LENGTH_SHORT).show();

    }

    /**
     * save a giving bitmap image by the the giving name
     * @param b - bitmap image
     * @param name - name of image
     * @return {@link Boolean} boolean result of the save operation , true if succeeded, otherwise false
     */
    public boolean saveSnap(Bitmap b, String name) {

        try {


            File imageFile = new File(Environment.getExternalStorageDirectory(), name);
            if(!imageFile.exists()){
                imageFile.createNewFile();
            }
            String imagePath = imageFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(imagePath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;

        } catch (Exception ioe) {
            ioe.printStackTrace();
            return  false;
        }
    }

    /**
     * load a picture from phone
     * @param name {@link String} the image file name
     * @return {@link Bitmap} an image
     */
    public Bitmap loadSnap(String name) {

        // Load profile photo from internal storage
        try {

            File imageFile = new File(Environment.getExternalStorageDirectory(), name);
            String imagePath = imageFile.getAbsolutePath();
            FileInputStream fis = new FileInputStream(imagePath);
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            return bmap;

        } catch (IOException e) {
            // Default profile photo if no photo saved before.
            return null;
        }

    }
}
