package utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.example.matan.hw1app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import controller.PlaceInfoActivity;

/**
 * helper class to preform custom help function
 * Created by Matan on 01/12/2016.
 */

public class HelperClass{

    private Context context;
    private ArrayList<String> locales;
    public HelperClass(Context context) {
        this.context = context;
        this.locales = getCountries();
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
}